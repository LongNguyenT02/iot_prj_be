package com.filemanage.iot_prj_be;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.filemanage.iot_prj_be.dtos.DeviceDTO;
import com.filemanage.iot_prj_be.dtos.DeviceLogDTO;
import com.filemanage.iot_prj_be.dtos.MessageWebsocket;
import com.filemanage.iot_prj_be.entities.DeviceEntity;
import com.filemanage.iot_prj_be.entities.DeviceLogEntity;
import com.filemanage.iot_prj_be.repository.DeviceLogRepository;
import com.filemanage.iot_prj_be.repository.DeviceRepository;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Configuration
public class MqttBean {

    @Autowired
    private DeviceLogRepository deviceLogRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;


    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();

        options.setServerURIs(new String[] { "tcp://localhost:2003" });
        options.setUserName("admin");
        String pass = "12345678";
        options.setPassword(pass.toCharArray());
        options.setCleanSession(true);

        factory.setConnectionOptions(options);

        return factory;
    }
    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageProducer inbound() {
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter("serverIn",
                mqttClientFactory(), "#");

        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(2);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }


    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                String topic = Objects.requireNonNull(message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC)).toString();
                String payload = (String) message.getPayload();
                if(topic.equals("value")) {
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();

                        JsonNode rootNode = objectMapper.readTree(payload);

                        JsonNode valueNode = rootNode.get("data");
                        if (valueNode != null && valueNode.isArray()) {
                            List<DeviceLogDTO> deviceLogDTOS=new ArrayList<>();
                            for (JsonNode node : valueNode) {
                                DeviceEntity deviceEntity=deviceRepository.findByName(node.get("name").asText());

                                DeviceLogEntity deviceLogEntity=DeviceLogEntity.builder()
                                        .createdAt(new Date())
                                        .sensor(deviceEntity)
                                        .value(node.get("value").intValue())
                                        .build();

                                DeviceLogEntity deviceLogEntity1= deviceLogRepository.save(deviceLogEntity);
                                deviceLogDTOS.add(DeviceLogDTO.builder()
                                        .id(deviceLogEntity1.getId())
                                        .value(deviceLogEntity1.getValue())
                                    .createdAt(deviceLogEntity1.getCreatedAt())
                                        .sensor(DeviceDTO.builder()
                                                .id(deviceLogEntity1.getSensor().getId())
                                                .name(deviceLogEntity1.getSensor().getName())
                                                .type(deviceLogEntity1.getSensor().getType())
                                                .build())
                                        .build());
                            }
                            simpMessagingTemplate.convertAndSendToUser("1", "/deviceLogs", MessageWebsocket.builder().data(deviceLogDTOS).created_at(new Date()).build() );
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

        };
    }

    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }
    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound() {
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler("serverOut", mqttClientFactory());
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic("#");
        messageHandler.setDefaultRetained(false);
        return messageHandler;
    }
}
