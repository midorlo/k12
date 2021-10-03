package com.midorlo.k12.security;

import com.midorlo.k12.security.AjaxAuthenticationSuccessHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletResponse;

import static javax.servlet.http.HttpServletResponse.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

class AjaxAuthenticationSuccessHandlerTest {

    private HttpServletResponse              response;
    private AjaxAuthenticationSuccessHandler handler;

    @BeforeEach
    void setup() {
        response = spy(HttpServletResponse.class);
        handler = new AjaxAuthenticationSuccessHandler();
    }

    @Test
    void testOnAuthenticationSuccess() {
        Throwable caughtException = catchThrowable(() -> {
            handler.onAuthenticationSuccess(null, response, null);
            verify(response).setStatus(SC_OK);
        });
        assertThat(caughtException).isNull();
    }
}
