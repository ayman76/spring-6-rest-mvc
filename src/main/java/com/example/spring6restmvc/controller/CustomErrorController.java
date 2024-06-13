package com.example.spring6restmvc.controller;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomErrorController {

    @ExceptionHandler(TransactionSystemException.class)
    ResponseEntity handleJpaViolation(TransactionSystemException ex) {
        ResponseEntity.BodyBuilder responseEntity = ResponseEntity.badRequest();

        if(ex.getCause().getCause() instanceof ConstraintViolationException){
            ConstraintViolationException exception = (ConstraintViolationException) ex.getCause().getCause();
            List errors = exception.getConstraintViolations().stream()
                    .map(constraintViolation -> {
                        Map<String, String> errorMap = new HashMap<>();
                        errorMap.put(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage());
                        return errorMap;
                    }).collect(Collectors.toList());
            return responseEntity.body(errors);
        }

        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity handleBindException(MethodArgumentNotValidException ex) {

        List errorList = ex.getFieldErrors().stream()
                .map(fieldError -> {
                    Map<String, String> errorMap = new HashMap<>();
                    errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
                    return errorMap;
                }).collect(Collectors.toList());

        return ResponseEntity.badRequest().body(errorList);
    }
}
