package com.filemanage.iot_prj_be.controller;

import com.filemanage.iot_prj_be.MqttGateway;
import com.filemanage.iot_prj_be.dtos.DeviceDTO;
import com.filemanage.iot_prj_be.entities.DeviceActivityLogEntity;
import com.filemanage.iot_prj_be.entities.DeviceEntity;
import com.filemanage.iot_prj_be.entities.DeviceLogEntity;
import com.filemanage.iot_prj_be.exception.ApiException;
import com.filemanage.iot_prj_be.repository.DeviceActivityLogRepository;
import com.filemanage.iot_prj_be.repository.DeviceRepository;
import com.filemanage.iot_prj_be.service.DeviceService;
import com.google.gson.JsonObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("device")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private DeviceActivityLogRepository deviceActivityLogRepository;

    @Autowired
    MqttGateway mqttGateway;

    @GetMapping("/getAllDevices")
    @CrossOrigin
    private ResponseEntity<?> getAllDevice(){
        List<DeviceEntity> deviceEntityList=deviceService.getAllDevice();
        List<DeviceDTO> deviceDTOS=deviceEntityList.stream().map(deviceEntity -> {
         DeviceDTO deviceDTO=new DeviceDTO();
            BeanUtils.copyProperties(deviceEntity,deviceDTO);
            return deviceDTO;
        }).toList();
        return ResponseEntity.ok(deviceDTOS);
    }

    @PutMapping("/changeDeviceStatus")
    @CrossOrigin
    private ResponseEntity<?> changeDeviceStatus(@RequestBody DeviceDTO request){
        DeviceEntity deviceEntity=deviceRepository.findById(request.getId()).orElseThrow(()-> new ApiException(HttpStatus.BAD_REQUEST,"Not found Device"));
        deviceEntity.setStatus(request.getStatus());
        DeviceEntity savedDevice= deviceRepository.save(deviceEntity);
        DeviceActivityLogEntity deviceActivityLogEntity= DeviceActivityLogEntity.builder()
                .createdAt(new Date())
                .status(request.getStatus())
                .sensor(savedDevice)
                .build();
        deviceActivityLogRepository.save(deviceActivityLogEntity);
        DeviceDTO deviceDTO=new DeviceDTO();
        BeanUtils.copyProperties(savedDevice,deviceDTO);

        String status = Objects.equals(request.getStatus(),1)?"on":"off";
        JsonObject message = new JsonObject();
        message.addProperty("status", status);
        message.addProperty("name", request.getName());

        mqttGateway.senToMqtt(message.toString(), "action");
        return ResponseEntity.ok(deviceDTO);
    }


    
}
