package com.filemanage.iot_prj_be.dtos;

import com.filemanage.iot_prj_be.entities.DeviceEntity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceLogDTO {
    private Long id;
    private Integer value;
    private Date createdAt;

    private DeviceDTO sensor;
}
