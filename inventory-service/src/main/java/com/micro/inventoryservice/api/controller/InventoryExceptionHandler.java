package com.micro.inventoryservice.api.controller;




import com.micro.inventoryservice.api.exception.BadRequestException;
import com.micro.inventoryservice.api.exception.NotValidException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice(annotations = RestController.class)
public class InventoryExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handleBadRequestException(BadRequestException exception, WebRequest request){
        Map<String, Object> messageBody = new HashMap<>();
        messageBody.put("exception_message", exception.getMessage());
        messageBody.put("status", HttpStatus.BAD_REQUEST);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageBody);
    }

    @ExceptionHandler(NotValidException.class)
    public ResponseEntity<Object> handleNotValidException(NotValidException exception, WebRequest request){
        Map<String, Object> messageBody = new HashMap<>();
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error->
                errors.put(error.getField(), error.getDefaultMessage()));
        messageBody.put("exception_message", errors);
        messageBody.put("status", HttpStatus.BAD_REQUEST);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageBody);
    }
}
