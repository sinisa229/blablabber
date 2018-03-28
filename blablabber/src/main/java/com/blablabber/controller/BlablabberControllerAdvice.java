package com.blablabber.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class BlablabberControllerAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlablabberControllerAdvice.class);

    @ExceptionHandler(BindException.class)
    public void onBindException(BindException e, HttpServletResponse httpServletResponse) {
        LOGGER.info("Validation failed", e);
        // TODO describe the violations
        httpServletResponse.setStatus(400);
    }

    @ExceptionHandler(Exception.class)
    public void onException(Exception e, HttpServletResponse httpServletResponse) throws Exception {
        LOGGER.error("Error", e);
        //TODO create tech error response
        httpServletResponse.setStatus(500);
    }
}
