package com.wupipi.coedit.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: xudong
 * Date: 8/27/14
 * Time: 11:02 AM
 */
@ControllerAdvice
public class RestErrorAdvice {

    @ExceptionHandler(RestErrorException.class)
    @ResponseBody
    public RestError handleRestErrorRequest(HttpServletRequest request, HttpServletResponse response,
                                            RestErrorException ex) {
        response.setStatus(ex.getRestError().getStatus().value());
        return ex.getRestError();
    }



    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public RestError handleRestErrorRequest(HttpServletRequest request, HttpServletResponse response,
                                            MissingServletRequestParameterException ex) {
        String message = "Required " + ex.getParameterType() +
                " parameter " + ex.getParameterName() +
                " is not present";
        return new RestError(HttpStatus.BAD_REQUEST, 4002, message, message, ex);
    }

}

