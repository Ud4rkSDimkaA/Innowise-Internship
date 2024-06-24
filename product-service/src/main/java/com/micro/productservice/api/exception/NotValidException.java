package com.micro.productservice.api.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.BindingResult;

@Getter
@Setter
public class NotValidException extends RuntimeException{
    private final BindingResult bindingResult;
    public NotValidException(String message, BindingResult bindingResult) {
        super(message);
        this.bindingResult = bindingResult;
    }
}
