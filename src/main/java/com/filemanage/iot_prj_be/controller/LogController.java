package com.filemanage.iot_prj_be.controller;

import com.filemanage.iot_prj_be.dtos.DeviceActivityLogDTO;
import com.filemanage.iot_prj_be.dtos.DeviceDTO;
import com.filemanage.iot_prj_be.dtos.PaginationResponse;
import com.filemanage.iot_prj_be.entities.DeviceActivityLogEntity;
import com.filemanage.iot_prj_be.repository.DeviceActivityLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("logs")
public class LogController {

    @Autowired
    private DeviceActivityLogRepository deviceActivityLogRepository;

    @GetMapping("/getStatusLog")
    @CrossOrigin
    public ResponseEntity<?> getLogStatus(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sort", defaultValue = "createdAt") String sortField,
            @RequestParam(name = "direction", defaultValue = "asc") String sortDirection,
            @RequestParam(name = "filterStatus") List<Integer> filterStatus,
            @RequestParam(name = "filterSensor") List<String> filterSensor,
            @RequestParam(name = "startDate", required = false) String startDateStr,
            @RequestParam(name = "endDate", required = false) String endDateStr
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(sortDirection), sortField);

        Page<DeviceActivityLogEntity> deviceActivityLogsPage;
        Date startDate = null;
        Date endDate = null;

        if (startDateStr != null && !startDateStr.isEmpty()) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                startDate = dateFormat.parse(startDateStr);
            } catch (ParseException e) {
                e.printStackTrace();
                return ResponseEntity.badRequest().body("Invalid startDate format");
            }
        } else {
            startDate = new Date(0);
        }
        if (endDateStr != null && !endDateStr.isEmpty()) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                endDate = dateFormat.parse(endDateStr);
            } catch (ParseException e) {
                e.printStackTrace();
                return ResponseEntity.badRequest().body("Invalid endDate format");
            }
        } else {
            endDate = new Date();
        }

        if (filterStatus.isEmpty() && filterSensor.isEmpty()) {
            deviceActivityLogsPage = deviceActivityLogRepository.findAllByCreatedAtBetween(startDate, endDate, pageable);
        } else if (filterStatus.isEmpty()) {
            deviceActivityLogsPage = deviceActivityLogRepository.findAllBySensorNameInAndCreatedAtBetween(filterSensor, startDate, endDate, pageable);
        } else if (filterSensor.isEmpty()) {
            deviceActivityLogsPage = deviceActivityLogRepository.findAllByStatusInAndCreatedAtBetween(filterStatus, startDate, endDate, pageable);
        } else {
            deviceActivityLogsPage = deviceActivityLogRepository.findAllByStatusInAndSensorNameInAndCreatedAtBetween(filterStatus, filterSensor, startDate, endDate, pageable);
        }

        return ResponseEntity.ok(PaginationResponse.builder()
                .perPage(size)
                .count((int) deviceActivityLogsPage.getTotalElements())
                .page(page)
                .content(deviceActivityLogsPage.getContent().stream().map(dv -> {
                    return DeviceActivityLogDTO.builder()
                            .id(dv.getId())
                            .status(dv.getStatus())
                            .createdAt(dv.getCreatedAt())
                            .sensor(DeviceDTO.builder()
                                    .id(dv.getSensor().getId())
                                    .status(dv.getSensor().getStatus())
                                    .name(dv.getSensor().getName())
                                    .type(dv.getSensor().getType())
                                    .build())
                            .build();
                }).toList())
                .build());
    }


}
