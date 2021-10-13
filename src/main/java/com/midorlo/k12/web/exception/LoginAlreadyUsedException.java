package com.midorlo.k12.web.exception;

import com.midorlo.k12.config.ApplicationConstants;

public class LoginAlreadyUsedException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public LoginAlreadyUsedException() {
        super(ApplicationConstants.ErrorConstants.LOGIN_ALREADY_USED_TYPE, "Login name already used!", "userManagement", "userexists");
    }
}
