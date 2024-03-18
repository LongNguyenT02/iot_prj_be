package com.filemanage.iot_prj_be.entities;

import com.filemanage.iot_prj_be.DeviceEnum;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class DeviceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;
    private Integer status;

    @Enumerated(EnumType.STRING)
    private DeviceEnum type;

    @OneToMany(mappedBy = "sensor",fetch = FetchType.EAGER)
    private List<DeviceLogEntity> logs;

    @OneToMany(mappedBy = "sensor",fetch = FetchType.EAGER  )
    private List<DeviceActivityLogEntity> activityLogs;

}
