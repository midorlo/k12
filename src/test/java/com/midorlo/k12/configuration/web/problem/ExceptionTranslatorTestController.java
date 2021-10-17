package com.midorlo.k12.configuration.web.problem;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.Data;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/exception-translator-test")
@SuppressWarnings({ "EmptyMethod", "unused" })
public class ExceptionTranslatorTestController {

    @GetMapping("/concurrency-failure")
    public void concurrencyFailure() {
        throw new ConcurrencyFailureException("test concurrency failure");
    }

    @PostMapping("/method-argument")
    public void methodArgument(@Valid @RequestBody TestDTO testDTO) {}

    @GetMapping("/missing-servlet-request-part")
    public void missingServletRequestPartException(@RequestPart String part) {}

    @GetMapping("/missing-servlet-request-parameter")
    public void missingServletRequestParameterException(@RequestParam String param) {}

    @GetMapping("/access-denied")
    public void accessdenied() {
        throw new AccessDeniedException("test access denied!");
    }

    @GetMapping("/unauthorized")
    public void unauthorized() {
        throw new BadCredentialsException("test authentication failed!");
    }

    @GetMapping("/response-status")
    public void exceptionWithResponseStatus() {
        throw new TestResponseStatusException();
    }

    @GetMapping("/internal-server-error")
    public void internalServerError() {
        throw new RuntimeException();
    }

    @Data
    public static class TestDTO {
        @NotNull
        private String test;
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "test response status")
    public static class TestResponseStatusException extends RuntimeException {}
}
