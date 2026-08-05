package com.clinicbook.infrastructure.persistence.repositories;

import com.clinicbook.domain.model.DoctorSchedule;
import com.clinicbook.domain.port.DoctorScheduleRepositoryPort;
import com.clinicbook.infrastructure.persistence.mappers.DoctorMapper;
import com.clinicbook.infrastructure.persistence.mappers.DoctorScheduleMapper;
import com.clinicbook.infrastructure.persistence.models.DoctorScheduleEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Component
public class DoctorScheduleRepositoryImpl implements DoctorScheduleRepositoryPort {
    private final DoctorScheduleMapper doctorScheduleMapper;
    private final DoctorScheduleJpaRepository doctorScheduleJpaRepo;

    @Override
    public DoctorSchedule save(DoctorSchedule doctorSchedule) {
        DoctorScheduleEntity entity = doctorScheduleMapper.toEntity(doctorSchedule);
        return this.doctorScheduleMapper.toDomain(doctorScheduleJpaRepo.save(entity));
    }

    @Override
    public Optional<DoctorSchedule> findById(UUID id) {
        return doctorScheduleJpaRepo.findById(id).map(doctorScheduleMapper::toDomain);
    }

    @Override
    public List<DoctorSchedule> findByDoctorId(UUID doctorId) {
        return doctorScheduleJpaRepo.findByDoctorId(doctorId)
                .stream()
                .map(doctorScheduleMapper::toDomain).toList();
    }

    @Override
    public List<DoctorSchedule> findByDoctorIdAndDayOfWeek(UUID doctorId, DayOfWeek dayOfWeek) {
        return doctorScheduleJpaRepo.findByDoctorIdAndDayOfWeek(doctorId, dayOfWeek)
                .stream()
                .map(doctorScheduleMapper::toDomain)
                .toList();
    }

    @Override
    public void delete(UUID id) {
        doctorScheduleJpaRepo.deleteById(id);
    }
}
