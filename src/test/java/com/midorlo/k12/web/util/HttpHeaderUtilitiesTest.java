package com.midorlo.k12.web.util;

import static org.assertj.core.api.Assertions.assertThat;

import com.midorlo.k12.web.RestUtilities;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

@SuppressWarnings("UastIncorrectHttpHeaderInspection")
class HttpHeaderUtilitiesTest {

    @Test
    void createAlert() {
        String message = "any.message";
        String param = "24";

        HttpHeaders headers = RestUtilities.createAlert("myApp", message, param);
        assertThat(headers.getFirst("X-myApp-alert")).isEqualTo(message);
        assertThat(headers.getFirst("X-myApp-params")).isEqualTo(param);
    }

    @Test
    void createEntityCreationAlertWithTranslation() {
        HttpHeaders headers = RestUtilities.createLocalizedEntityCreationAlert("myApp", "User", "2");
        assertThat(headers.getFirst("X-myApp-alert")).isEqualTo("myApp.User.created");
        assertThat(headers.getFirst("X-myApp-params")).isEqualTo("2");
    }

    @Test
    void createEntityCreationAlertNoTranslation() {
        HttpHeaders headers = RestUtilities.createEntityCreationAlert("myApp", "User", "2");
        assertThat(headers.getFirst("X-myApp-alert")).isEqualTo("A new User is created with identifier 2");
        assertThat(headers.getFirst("X-myApp-params")).isEqualTo("2");
    }

    @Test
    void createEntityUpdateAlertWithTranslation() {
        HttpHeaders headers = RestUtilities.createLocalizedEntityUpdateAlert("myApp", "User", "2");
        assertThat(headers.getFirst("X-myApp-alert")).isEqualTo("myApp.User.updated");
        assertThat(headers.getFirst("X-myApp-params")).isEqualTo("2");
    }

    @Test
    void createEntityUpdateAlertNoTranslation() {
        HttpHeaders headers = RestUtilities.createEntityUpdateAlert("myApp", "User", "2");
        assertThat(headers.getFirst("X-myApp-alert")).isEqualTo("A User is updated with identifier 2");
        assertThat(headers.getFirst("X-myApp-params")).isEqualTo("2");
    }

    @Test
    void createEntityDeletionAlertWithTranslation() {
        HttpHeaders headers = RestUtilities.createLocalizedEntityDeletionAlert("myApp", "User", "2");
        assertThat(headers.getFirst("X-myApp-alert")).isEqualTo("myApp.User.deleted");
        assertThat(headers.getFirst("X-myApp-params")).isEqualTo("2");
    }

    @Test
    void createEntityDeletionAlertNoTranslation() {
        HttpHeaders headers = RestUtilities.createEntityDeletionAlert("myApp", "User", "2");
        assertThat(headers.getFirst("X-myApp-alert")).isEqualTo("A User is deleted with identifier 2");
        assertThat(headers.getFirst("X-myApp-params")).isEqualTo("2");
    }

    @Test
    void createFailureAlertWithTranslation() {
        HttpHeaders headers = RestUtilities.createLocalizedFailureAlert("myApp", "User", "404", "Failed to find " + "user");
        assertThat(headers.getFirst("X-myApp-error")).isEqualTo("error.404");
        assertThat(headers.getFirst("X-myApp-params")).isEqualTo("User");
    }

    @Test
    void createFailureAlertNoTranslation() {
        HttpHeaders headers = RestUtilities.createFailureAlert("myApp", "User", "404", "Failed to find " + "user");
        assertThat(headers.getFirst("X-myApp-error")).isEqualTo("Failed to find user");
        assertThat(headers.getFirst("X-myApp-params")).isEqualTo("User");
    }
}
