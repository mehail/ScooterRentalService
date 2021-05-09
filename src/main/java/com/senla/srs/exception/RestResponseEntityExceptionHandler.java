package com.senla.srs.exception;

import org.postgresql.util.PSQLException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { NotFoundEntityException.class })
    protected ResponseEntity<Object> handleNotFoundEntity(NotFoundEntityException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = { EmptyResultDataAccessException.class })
    protected ResponseEntity<Object> handleEmptyResultDataAccess(EmptyResultDataAccessException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = { PSQLException.class })
    protected ResponseEntity<Object> handlePSQLException(PSQLException ex, WebRequest request) {
        return handleExceptionInternal(ex, "Unable to delete/update, entity is in use", new HttpHeaders(),
                HttpStatus.BAD_REQUEST, request);
    }
    
}