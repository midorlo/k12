package com.midorlo.k12.web.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.midorlo.k12.web.WebAppForwardingController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Unit tests for the {@link WebAppForwardingController} REST controller.
 */
class WebAppForwardingControllerTest {

    private static final String TEST = "test";

    private MockMvc restMockMvc;

    @BeforeEach
    public void setup() {
        WebAppForwardingController webAppForwardingController = new WebAppForwardingController();
        this.restMockMvc = MockMvcBuilders.standaloneSetup(webAppForwardingController, new TestController()).build();
    }

    @Test
    void getBackendEndpoint() throws Exception {
        restMockMvc
            .perform(get("/test"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN_VALUE))
            .andExpect(content().string("test"));
    }

    @Test
    void getClientEndpoint() throws Exception {
        ResultActions perform = restMockMvc.perform(get("/non-existant-mapping"));
        perform.andExpect(status().isOk()).andExpect(forwardedUrl("/"));
    }

    @Test
    void getNestedClientEndpoint() throws Exception {
        restMockMvc.perform(get("/admin/user-management")).andExpect(status().isOk()).andExpect(forwardedUrl("/"));
    }

    @RestController
    public static class TestController {

        @RequestMapping(value = "/test")
        public String test() {
            return TEST;
        }
    }
}
