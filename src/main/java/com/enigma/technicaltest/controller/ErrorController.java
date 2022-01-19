package com.enigma.technicaltest.controller;

import com.enigma.technicaltest.exception.BadRequestException;
import com.enigma.technicaltest.exception.NotFoundException;
import com.enigma.technicaltest.response.WebResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorController {
    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(NotFoundException exception){
        HttpStatus status = HttpStatus.NOT_FOUND;
        WebResponse<String> response = new WebResponse<>(
                exception.getMessage(),
                null
        );
        return new ResponseEntity<>(response,status);
    }

    @ExceptionHandler(value = {BadRequestException.class})
    public ResponseEntity<Object> handleBadRequestException(BadRequestException exception){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        WebResponse<String> response = new WebResponse<>(
                exception.getMessage(),
                null
        );
        return new ResponseEntity<>(response,status);
    }
}
