package com.example.userservice.handler;


import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.stream.Collectors;




@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<?> handleIllegalStateException(IllegalStateException exception, WebRequest request){
        String requestUri = ((ServletWebRequest)request).getRequest().getRequestURI().toString();

        ExceptionMessage exceptionMessage = new ExceptionMessage(exception.getMessage(), requestUri);

        return new ResponseEntity<>(exceptionMessage, new HttpHeaders(), HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException exception, WebRequest request){
        String requestUri = ((ServletWebRequest)request).getRequest().getRequestURI().toString();

        ExceptionMessage exceptionMessage = new ExceptionMessage(exception.getMessage(), requestUri);

        return new ResponseEntity<>(exceptionMessage, new HttpHeaders(), HttpStatus.BAD_REQUEST);

    }

    /*

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handleUsernameNotFoundException(UsernameNotFoundException exception, WebRequest request){
        String requestUri = ((ServletWebRequest)request).getRequest().getRequestURI().toString();

        ExceptionMessage exceptionMessage = new ExceptionMessage(exception.getMessage(), requestUri);

        return new ResponseEntity<>(exceptionMessage, new HttpHeaders(), HttpStatus.NOT_FOUND);

    }
     */


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleValidationException(MethodArgumentNotValidException ex)
    {
        List<String> errorMessages = ((MethodArgumentNotValidException)ex)
                .getBindingResult()
                .getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        return new ResponseEntity(errorMessages.toString(), HttpStatus.BAD_REQUEST);
    }

    /*
    @ExceptionHandler(value = ExpiredJwtException.class)
    public ResponseEntity<?> handleExpiredJwtException(ExpiredJwtException exception, WebRequest request) {

        String requestUri = ((ServletWebRequest)request).getRequest().getRequestURI().toString();

        ExceptionMessage exceptionMessage = new ExceptionMessage(exception.getMessage(), requestUri);

        return new ResponseEntity<>(exceptionMessage, new HttpHeaders(), HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(value = MalformedJwtException.class)
    public ResponseEntity<?> handleMalformedJwtException(MalformedJwtException exception, WebRequest request) {

        String requestUri = ((ServletWebRequest)request).getRequest().getRequestURI().toString();

        ExceptionMessage exceptionMessage = new ExceptionMessage(exception.getMessage(), requestUri);

        return new ResponseEntity<>(exceptionMessage, new HttpHeaders(), HttpStatus.FORBIDDEN);
    }
     */




}

