package com.filemanage.iot_prj_be.repository;

import com.filemanage.iot_prj_be.entities.DeviceActivityLogEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface DeviceActivityLogRepository extends JpaRepository<DeviceActivityLogEntity,Long> {
    Page<DeviceActivityLogEntity> findAllByStatusInAndSensorNameIn(List<Integer> statusList, List<String> filterSensor, Pageable pageable);


    Page<DeviceActivityLogEntity> findAllBySensorNameIn(List<String> filterSensor, Pageable pageable);

    Page<DeviceActivityLogEntity> findAllByStatusIn(List<Integer> filterStatus, Pageable pageable);

    Page<DeviceActivityLogEntity> findAllByCreatedAtBetween(Date createdAt, Date createdAt2, Pageable pageable);

    Page<DeviceActivityLogEntity> findAllBySensorNameInAndCreatedAtBetween(List<String> filterSensor, Date startDate, Date endDate, Pageable pageable);

    Page<DeviceActivityLogEntity> findAllByStatusInAndCreatedAtBetween(List<Integer> filterStatus, Date startDate, Date endDate, Pageable pageable);

    Page<DeviceActivityLogEntity> findAllByStatusInAndSensorNameInAndCreatedAtBetween(List<Integer> filterStatus, List<String> filterSensor, Date startDate, Date endDate, Pageable pageable);
}
