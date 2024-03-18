package com.filemanage.iot_prj_be.controller;

import com.filemanage.iot_prj_be.dtos.LogDTO;
import com.filemanage.iot_prj_be.dtos.MessageWebsocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class LogWebsocketController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/message")
    public LogDTO receive(@Payload LogDTO message) throws Exception {
        simpMessagingTemplate.convertAndSendToUser(String.valueOf(message.getId()), "/deviceLogs", message);
        return message;
    }
}