package com.clinicbook.infrastructure.persistence.repositories;

import com.clinicbook.domain.model.DoctorSchedule;
import com.clinicbook.infrastructure.persistence.models.DoctorScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.UUID;

@Repository
public interface DoctorScheduleJpaRepository extends JpaRepository<DoctorScheduleEntity, UUID> {

    List<DoctorScheduleEntity> findByDoctorId(UUID doctorId);

    List<DoctorScheduleEntity> findByDoctorIdAndDayOfWeek(UUID doctorId, DayOfWeek dayOfWeek);
}
