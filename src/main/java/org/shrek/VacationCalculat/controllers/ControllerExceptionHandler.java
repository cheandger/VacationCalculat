package org.shrek.VacationCalculat.controllers;

import org.shrek.VacationCalculat.exceptions.BusinessException;
import org.shrek.VacationCalculat.exceptions.TechnicalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    //400
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolationException(
            ConstraintViolationException ex,
            HttpServletRequest request,
            HttpServletResponse response) {
        log.warn("Validation exception", ex);
        return ResponseEntity.badRequest().body("Неверный запрос " + ex.getMessage());
    }

    //500
    @ExceptionHandler(TechnicalException.class)
    public ResponseEntity<String> handleTechnicalException(
            TechnicalException ex,
            HttpServletRequest request,
            HttpServletResponse response) {
        log.error("Technical exception", ex);
        return ResponseEntity.internalServerError().body(ex.toString());
    }

    //422
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<String> handleBusinessException(
            BusinessException ex,
            HttpServletRequest request,
            HttpServletResponse response) {
        log.error("Business exception", ex);
        return ResponseEntity.unprocessableEntity().body(ex.toString());
    }

    //parent
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex,
            Object body,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        log.error("Unhandled REST exception", ex);
        return super.handleExceptionInternal(ex, body, headers, status, request);
    }
}
