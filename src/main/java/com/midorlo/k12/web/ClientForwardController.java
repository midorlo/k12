package com.midorlo.k12.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@SuppressWarnings({ "SameReturnValue", "MVCPathVariableInspection" })
@Controller
@RequestMapping("/")
public class ClientForwardController {

    /**
     * If changing, also change the publicPath in quasar.conf.
     */
    @SuppressWarnings("SpringMVCViewInspection")
    @GetMapping(value = "/")
    public String forwardRoot() {
        return "forward:/index.html";
    }

    /**
     * Forwards any unmapped paths (except those containing a period) to the webapp {@code index.html}.
     *
     * @return forward to webapp {@code index.html}.
     */
    @SuppressWarnings({ "RegExpRedundantEscape" })
    @GetMapping(value = "/**/{path:[^\\.]*}")
    public String forward() {
        return "forward:/";
    }
}
