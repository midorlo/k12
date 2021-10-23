package com.midorlo.k12.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.midorlo.k12.Application;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Composite annotation to describe an integration test (TYPE, RUNTIME, classes = Application.class)
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = Application.class)
public @interface IntegrationTest {
}
