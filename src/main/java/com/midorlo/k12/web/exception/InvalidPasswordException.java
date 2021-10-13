package com.midorlo.k12.web.exception;

import com.midorlo.k12.config.ApplicationConstants;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class InvalidPasswordException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public InvalidPasswordException() {
        super(ApplicationConstants.ErrorConstants.INVALID_PASSWORD_TYPE, "Incorrect password", Status.BAD_REQUEST);
    }
}
