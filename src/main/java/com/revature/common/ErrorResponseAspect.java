package com.revature.common;

import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.revature.common.exceptions.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ErrorResponseAspect {
    
    
    private static Logger logger = LogManager.getLogger(ErrorResponseAspect.class);

    @ExceptionHandler({InvalidRequestException.class, JsonMappingException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error handleBadRequests(Exception e) {
        logger.warn("A bad request was received at {}, details: {}", LocalDateTime.now(), e.getMessage());
        return new Error(400, e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Error handleAuthenticationExceptions(AuthenticationException e) {
        logger.warn("A failed authentication occurred at {}, details: {}", LocalDateTime.now(), e.getMessage());
        return new Error(401, e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Error handleAuthorizationException(AuthorizationException e) {
        logger.warn("An unauthorized request was made at {}, details: {}", LocalDateTime.now(), e.getMessage());
        return new Error(403, e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Error handleResourceNotFoundException(ResourceNotFoundException e) {
        logger.info("No resource found using the provided search values, details: {}", e.getMessage());
        return new Error(404, e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public Error handleIsAlreadyExistException(IsAlreadyExist e) {
        logger.info("Could not persist the provided resource payload, details: {}", e.getMessage());
        return new Error(409, e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public Error handleDataSourceExceptions(DataSourceException e) {
        logger.error("A datasource exception was thrown at {}, details: {}", LocalDateTime.now(), e.getMessage());
        return new Error(502, e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Error handleOtherExceptions(Exception e) {
        e.printStackTrace();
        logger.error("A unhandled exception was thrown at {}, details: {}", LocalDateTime.now(), e.getMessage());
        return new Error(500, "An unexpected exception occurred. Devs, please check logs.");
    }
}
