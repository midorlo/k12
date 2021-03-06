package com.midorlo.k12.web.util;

import static org.assertj.core.api.Assertions.assertThat;

import com.midorlo.k12.web.RestUtilities;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

@SuppressWarnings("UastIncorrectHttpHeaderInspection")
class HttpResponseUtilitiesTest {

    private static final String HEADER_NAME = "X-Test";
    private static final String HEADER_VALUE = "FooBar";

    private HttpHeaders headers;

    @BeforeEach
    void setup() {
        headers = new HttpHeaders();
        headers.add(HEADER_NAME, HEADER_VALUE);
    }

    @Test
    void testOptionalYesWithoutHeaders() {
        ResponseEntity<Integer> response = RestUtilities.wrapOrNotFound(
            Optional.of(42).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND))
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(42);
        assertThat(response.getHeaders()).isEmpty();
    }

    @Test
    void testOptionalYesWithHeaders() {
        ResponseEntity<Integer> response = RestUtilities.wrapOrNotFound(
            Optional.of(42).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)),
            headers
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(42);
        assertThat(response.getHeaders()).hasSize(1);
        List<String> strings = response.getHeaders().get(HEADER_NAME);
        assertThat(strings != null).isTrue();
        assertThat(strings).hasSize(1);
        assertThat(strings.get(0)).isEqualTo(HEADER_VALUE);
    }
}
