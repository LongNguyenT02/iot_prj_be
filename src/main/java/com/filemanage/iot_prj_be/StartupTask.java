package com.filemanage.iot_prj_be;

import com.filemanage.iot_prj_be.entities.DeviceEntity;
import com.filemanage.iot_prj_be.repository.DeviceRepository;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StartupTask implements CommandLineRunner {

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private MqttGateway mqttGateway;

    @Override
    public void run(String... args) throws Exception {

        List<DeviceEntity> devices = deviceRepository.findAll();
        for (DeviceEntity device : devices) {
            if(device.getType().equals(DeviceEnum.Fan)||device.getType().equals(DeviceEnum.Lux)){
                sendMqttMessageForDeviceStatus(device);
            }
        }

    }
    private void sendMqttMessageForDeviceStatus(DeviceEntity device) {
        String status = (device.getStatus() == 1) ? "on" : "off";
        JsonObject message = new JsonObject();
        message.addProperty("status", status);
        message.addProperty("name", device.getName());
        mqttGateway.senToMqtt(message.toString(), "action");
    }
}
