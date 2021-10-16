package com.midorlo.k12.web.rest.webapp;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@SuppressWarnings({ "SameReturnValue", "MVCPathVariableInspection" })
@Controller
@RequestMapping("/")
public class ClientForwardController {

    public static final String FWD_ROOT = "forward:/spa/index.html";
    public static final String FWD_PATH = "forward:/";

    @GetMapping(value = "/")
    public String forwardRoot() {
        return "forward:/spa/index.html";
    }

    /**
     * Forwards any unmapped paths (except those containing a period) to the client {@code index.html}.
     * @return forward to client {@code index.html}.
     */
    @GetMapping(value = "/**/{path:[^\\.]*}")
    public String forward() {
        return "forward:/";
    }
}
