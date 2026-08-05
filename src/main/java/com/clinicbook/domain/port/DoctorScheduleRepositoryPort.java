package com.clinicbook.domain.port;

import com.clinicbook.domain.model.DoctorSchedule;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DoctorScheduleRepositoryPort {
    DoctorSchedule save(DoctorSchedule doctorSchedule);
    Optional<DoctorSchedule> findById(UUID id);
    List<DoctorSchedule> findByDoctorId(UUID doctorId);
    List<DoctorSchedule> findByDoctorIdAndDayOfWeek(UUID doctorId, DayOfWeek dayOfWeek);
    void delete(UUID id);
}
