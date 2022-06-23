package com.epam.audio.streaming.album.microservice.controllers.exceptions;

import com.epam.audio.streaming.album.microservice.exceptions.EntityAlreadyExistsException;
import com.epam.audio.streaming.album.microservice.exceptions.EntityNotExistsException;
import com.epam.audio.streaming.album.microservice.exceptions.EntityValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.BindException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {EntityAlreadyExistsException.class})
    protected ResponseEntity<Object> alreadyExist(Exception e, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        log.error("Entity already exist exception! {}", e.getMessage());

        body.put("message", String.format("Entity already exist exception! %s", e.getMessage()));
        body.put("time", LocalDateTime.now());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {EntityValidationException.class})
    protected ResponseEntity<Object> validationException(Exception e, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        log.error("Validation exception! {}", e.getMessage());

        body.put("message", String.format("Validation exception! %s", e.getMessage()));
        body.put("time", LocalDateTime.now());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {EntityNotExistsException.class})
    protected ResponseEntity<Object> notFound(Exception e, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        log.error("Entity not found exception! {}", e.getMessage());

        body.put("message", String.format("Entity not found exception! %s", e.getMessage()));
        body.put("time", LocalDateTime.now());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {BindException.class})
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,  WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        log.error("Bind exception! {}", ex.getMessage());

        body.put("message", String.format("Bind exception! %s", ex.getMessage()));
        body.put("time", LocalDateTime.now());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        log.error("Validation exception! {}", ex.getMessage());

        body.put("message", String.format("Validation exception! %s", ex.getMessage()));
        body.put("time", LocalDateTime.now());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> serverException(Exception e, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        log.error("Internal server error! {}", e.getMessage());

        body.put("message", String.format("Internal server error! %s", e.getMessage()));
        body.put("time", LocalDateTime.now());
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
