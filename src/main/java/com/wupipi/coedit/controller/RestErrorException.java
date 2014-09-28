package com.wupipi.coedit.controller;

/**
 * User: xudong
 * Date: 8/27/14
 * Time: 11:05 AM
 */
public class RestErrorException extends RuntimeException{
    private RestError restError;

    public RestErrorException(RestError restError) {
        this.restError = restError;
    }

    public RestError getRestError() {
        return restError;
    }
}
