package com.midorlo.k12.security;

import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

import com.midorlo.k12.security.AjaxAuthenticationFailureHandler;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AjaxAuthenticationFailureHandlerTest {

    private HttpServletResponse response;
    private AjaxAuthenticationFailureHandler handler;

    @BeforeEach
    void setup() {
        response = spy(HttpServletResponse.class);
        handler = new AjaxAuthenticationFailureHandler();
    }

    @Test
    void testOnAuthenticationFailure() {
        Throwable caught = catchThrowable(
            () -> {
                handler.onAuthenticationFailure(null, response, null);
                verify(response).sendError(SC_UNAUTHORIZED, AjaxAuthenticationFailureHandler.UNAUTHORIZED_MESSAGE);
            }
        );
        assertThat(caught).isNull();
    }

    @Test
    void testOnAuthenticationFailureWithException() {
        IOException exception = new IOException("Eek");
        Throwable caught = catchThrowable(
            () -> {
                doThrow(exception).when(response).sendError(anyInt(), anyString());
                handler.onAuthenticationFailure(null, response, null);
            }
        );
        assertThat(caught).isEqualTo(exception);
    }
}
