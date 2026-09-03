package com.clinicbook.application.service;

import com.clinicbook.application.dtos.*;
import com.clinicbook.domain.enums.Specialty;
import com.clinicbook.domain.enums.UserRole;
import com.clinicbook.domain.exception.DoctorNotFoundException;
import com.clinicbook.domain.exception.InvalidOwnerException;
import com.clinicbook.domain.exception.ScheduleNotFoundException;
import com.clinicbook.domain.exception.ScheduleOverlapException;
import com.clinicbook.domain.model.Doctor;
import com.clinicbook.domain.model.DoctorSchedule;
import com.clinicbook.domain.model.User;
import com.clinicbook.domain.port.DoctorRepositoryPort;
import com.clinicbook.domain.port.DoctorScheduleRepositoryPort;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@AllArgsConstructor
@Service
public class DoctorService {
    private final DoctorRepositoryPort doctorRepository;
    private final DoctorScheduleRepositoryPort doctorScheduleRepositoryPort;
    private final AuthService authService;


    // ==================== DOCTOR ====================


    @Transactional
    public DoctorRegistrationResult registerDoctor(RegisterDoctorCommand command) {

        RegisterUserCommand userCommand = new RegisterUserCommand(
                command.fullName(),
                command.email(),
                command.rawPassword(),
                UserRole.DOCTOR
        );

        User user = authService.createUser(userCommand);

        Doctor doctor = new Doctor(
                user.getId(),
                user,
                command.specialty(),
                command.consultationDurationMinutes()
        );

        doctorRepository.save(doctor);

        return new DoctorRegistrationResult(
                doctor.getId(), user.getEmail(), user.getFullName(), doctor.getSpecialty()
        );
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public List<Doctor> searchBySpecialty(Specialty specialty){
        return doctorRepository.findBySpecialty(specialty);
    }

    public Doctor getDoctorById(UUID id){
        return doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException(id));
    }


    // ==================== DOCTOR SCHEDULE ====================


    @Transactional(readOnly = true)
    public List<DoctorSchedule> getWeeklySchedule(UUID doctorId){

        if(!doctorRepository.existsById(doctorId)){
            throw new DoctorNotFoundException(doctorId);
        }

        return doctorScheduleRepositoryPort.findByDoctorId(doctorId);
    }

    @Transactional
    public DoctorSchedule createScheduleBlock(CreateScheduleCommand command){

        List<DoctorSchedule> existingBlocks =
                        doctorScheduleRepositoryPort.
                        findByDoctorIdAndDayOfWeek(command.doctorId(), command.dayOfWeek());

        DoctorSchedule newBlock = new DoctorSchedule(
                UUID.randomUUID(), command.doctorId(), command.dayOfWeek(), command.startTime(), command.endTime());


        boolean hasOverlap = existingBlocks.stream()
                             .anyMatch(existing -> existing.overlapsWith(newBlock));

        if (hasOverlap){
            throw new ScheduleOverlapException(command.doctorId(), command.dayOfWeek());
        }


        doctorScheduleRepositoryPort.save(newBlock);

        return newBlock;
    }

    public void deleteScheduleBlock(UUID scheduleId, UUID requestingDoctorId){

        DoctorSchedule schedule = doctorScheduleRepositoryPort
                .findById(scheduleId)
                .orElseThrow(() -> new ScheduleNotFoundException(scheduleId));

        if(!requestingDoctorId.equals(schedule.getDoctorId())){
            throw new InvalidOwnerException("You don't own this resource in order to delete it");
        }

        doctorScheduleRepositoryPort.delete(scheduleId);
    }

    public void deleteScheduleBlockByStaff(UUID scheduleId){

        DoctorSchedule schedule = doctorScheduleRepositoryPort
                .findById(scheduleId)
                .orElseThrow(() -> new ScheduleNotFoundException(scheduleId));

        doctorScheduleRepositoryPort.delete(scheduleId);
    }
}
