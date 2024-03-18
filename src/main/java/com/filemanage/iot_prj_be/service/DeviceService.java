package com.filemanage.iot_prj_be.service;

import com.filemanage.iot_prj_be.entities.DeviceEntity;

import java.util.List;

public interface DeviceService {
    List<DeviceEntity> getAllDevice();
}
