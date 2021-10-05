package com.midorlo.k12.web.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@Controller
public class ClientForwardController {

    @GetMapping(value = "/")
    public String forwardRoot() {
        //noinspection SpringMVCViewInspection
        return "forward:/spa/index.html";
    }

    /**
     * Forwards any unmapped paths (except those containing a period) to the client {@code index.html}.
     *
     * @return forward to client {@code index.html}.
     */
    @GetMapping(value = "/**/{path:[^.]*}")
    public String forward(@PathVariable String path) {
        log.info("path: {}", path);
        return "forward:/";
    }
}
