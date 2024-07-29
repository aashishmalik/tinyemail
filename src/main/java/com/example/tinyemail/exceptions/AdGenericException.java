package com.example.tinyemail.exceptions;

import lombok.Getter;

public class AdGenericException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    @Getter
    private String errorMsg;

    public AdGenericException(String errMsg) {
        super(errMsg);
        this.errorMsg = errMsg;
    }

    public AdGenericException(String message, Throwable cause) {
        super(message, cause);
    }

}
