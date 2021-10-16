package com.midorlo.k12.configuration.web.problem;

import com.midorlo.k12.configuration.ApplicationConstants;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class InvalidPasswordProblem extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public InvalidPasswordProblem() {
        super(ApplicationConstants.ErrorConstants.INVALID_PASSWORD_TYPE, "Incorrect password", Status.BAD_REQUEST);
    }
}
