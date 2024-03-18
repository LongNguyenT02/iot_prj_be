package com.filemanage.iot_prj_be.repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.filemanage.iot_prj_be.entities.DeviceEntity;
import com.filemanage.iot_prj_be.entities.DeviceLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

@Repository
public interface DeviceLogRepository extends JpaRepository<DeviceLogEntity,Long> {

}
