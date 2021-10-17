package com.midorlo.k12.configuration.web.problem;

import com.midorlo.k12.configuration.ApplicationConstants;

public class EmailAlreadyUsedProblem extends BadRequestAlertProblem {

    private static final long serialVersionUID = 1L;

    public EmailAlreadyUsedProblem() {
        super(ApplicationConstants.ErrorConstants.EMAIL_ALREADY_USED_TYPE, "Email is already in use!", "userManagement", "emailexists");
    }
}
