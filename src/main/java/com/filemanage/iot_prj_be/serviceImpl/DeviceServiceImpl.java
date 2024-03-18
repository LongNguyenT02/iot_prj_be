package com.filemanage.iot_prj_be.serviceImpl;

import com.filemanage.iot_prj_be.entities.DeviceEntity;
import com.filemanage.iot_prj_be.repository.DeviceRepository;
import com.filemanage.iot_prj_be.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;

    @Override
    public List<DeviceEntity> getAllDevice() {
        return deviceRepository.findAll();
    }
}
