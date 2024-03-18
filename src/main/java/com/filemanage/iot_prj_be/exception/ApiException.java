package com.filemanage.iot_prj_be.exception;

import lombok.*;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Setter
@Getter
public class ApiException extends RuntimeException{
    private HttpStatus httpStatus;
    private String message;
    public ApiException() {
        super();
    }

    public ApiException(String message) {
        super(message);
    }
}
