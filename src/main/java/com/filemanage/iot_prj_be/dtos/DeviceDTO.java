package com.filemanage.iot_prj_be.dtos;

import com.filemanage.iot_prj_be.DeviceEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceDTO {
    private Long id;
    private String name;
    private Integer status;
    private DeviceEnum type;
}
