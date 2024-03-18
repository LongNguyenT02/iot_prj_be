package com.filemanage.iot_prj_be.dtos;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor

public class ObjectResult implements Serializable {

    private Integer code;

    private String err;

    private Object data;

    private LocalDateTime time = LocalDateTime.now();

    public ObjectResult(@NonNull Integer code, @NonNull String err, Object data) {
        this.code = code;
        this.err = err;
        this.data = data;
    }
}
