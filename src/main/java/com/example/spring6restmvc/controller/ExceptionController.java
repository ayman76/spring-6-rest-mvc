package com.example.spring6restmvc.controller;

import com.example.spring6restmvc.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

//@ControllerAdvice
@Slf4j
public class ExceptionController {
//    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity handleNotFoundException(){
        return ResponseEntity.notFound().build();
    }
}
