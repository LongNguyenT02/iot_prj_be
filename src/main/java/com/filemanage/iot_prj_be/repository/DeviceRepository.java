package com.filemanage.iot_prj_be.repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.filemanage.iot_prj_be.entities.DeviceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends JpaRepository<DeviceEntity,Long> {
    DeviceEntity findByName(String name);
}
