package com.midorlo.k12.web.exception;

import com.midorlo.k12.config.ApplicationConstants;

public class EmailAlreadyUsedException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public EmailAlreadyUsedException() {
        super(ApplicationConstants.ErrorConstants.EMAIL_ALREADY_USED_TYPE, "Email is already in use!",
              "userManagement", "emailexists");
    }
}
