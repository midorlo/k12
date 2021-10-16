package com.midorlo.k12.configuration.web.problem;

import com.midorlo.k12.configuration.ApplicationConstants;

public class LoginAlreadyUsedProblem extends BadRequestAlertProblem {

    private static final long serialVersionUID = 1L;

    public LoginAlreadyUsedProblem() {
        super(ApplicationConstants.ErrorConstants.LOGIN_ALREADY_USED_TYPE, "Login name already used!",
              "userManagement", "userexists");
    }
}
