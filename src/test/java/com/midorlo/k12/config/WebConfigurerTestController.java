package com.midorlo.k12.config;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SuppressWarnings("EmptyMethod")
@RestController
public class WebConfigurerTestController {

    @GetMapping("/api/test-cors")
    public void testCorsOnApiPath() {}

    @GetMapping("/test/test-cors")
    public void testCorsOnOtherPath() {}
}
