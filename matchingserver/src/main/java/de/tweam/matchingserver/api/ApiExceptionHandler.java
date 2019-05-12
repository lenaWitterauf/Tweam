package de.tweam.matchingserver.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ApiExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String employeeNotFoundHandler(Exception ex) {
        logger.error("Error in Api:", ex);
        if(ex.getClass().equals(DataIntegrityViolationException.class)){
            ex =  new ApiException("Twitter-Handle is already registered");
        }
        return ex.getMessage();
    }


    static class ApiException extends RuntimeException {
        ApiException(String message) {
            super(message);
        }
    }
}
