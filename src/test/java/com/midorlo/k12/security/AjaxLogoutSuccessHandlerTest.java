package com.midorlo.k12.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import com.midorlo.k12.security.AjaxLogoutSuccessHandler;
import javax.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AjaxLogoutSuccessHandlerTest {

    private HttpServletResponse response;
    private AjaxLogoutSuccessHandler handler;

    @BeforeEach
    void setup() {
        response = spy(HttpServletResponse.class);
        handler = new AjaxLogoutSuccessHandler();
    }

    @Test
    void testOnAuthenticationSuccess() {
        Throwable caughtException = catchThrowable(
            () -> {
                handler.onLogoutSuccess(null, response, null);
                verify(response).setStatus(HttpServletResponse.SC_OK);
            }
        );
        assertThat(caughtException).isNull();
    }
}
