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
    private final DoctorRepositoryPort doctorRepositoryPort;
    private final DoctorScheduleRepositoryPort doctorScheduleRepositoryPort;
    private final AuthService authService;


    // ---------------- DOCTOR METHODS ----------------


    @Transactional
    public DoctorRegistrationResult registerDoctor(RegisterDoctorCommand command){

        // FETCH
        RegisterUserCommand userCommand = new RegisterUserCommand
        (command.fullName(), command.email(), command.rawPassword(), UserRole.DOCTOR);

        User user = authService.createUser(userCommand);

        Doctor doctor = new Doctor(user.getId(), user, command.specialty(), command.consultationDurationMinutes());

        doctorRepositoryPort.save(doctor);

        return new DoctorRegistrationResult(
                doctor.getId(), userCommand.email(), userCommand.fullName(), doctor.getSpecialty()
        );
    }

    public List<Doctor> searchBySpecialty(Specialty specialty){
        // FETCH
        return doctorRepositoryPort.findBySpecialty(specialty);
    }

    public Doctor getDoctorById(UUID id){
        // FETCH
        return doctorRepositoryPort.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException(id));
    }

    

    // ---------------- DOCTOR SCHEDULE METHODS ----------------


    @Transactional(readOnly = true)
    public List<DoctorSchedule> getWeeklySchedule(UUID doctorId){

        if(!doctorRepositoryPort.existsById(doctorId)){
            throw new DoctorNotFoundException(doctorId);
        }

        return doctorScheduleRepositoryPort.findByDoctorId(doctorId);
    }

    @Transactional
    public DoctorSchedule createScheduleBlock(CreateScheduleCommand command){

        // FETCH
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


        // PERSIST
        doctorScheduleRepositoryPort.save(newBlock);

        return newBlock;
    }

    public void deleteScheduleBlock(UUID scheduleId, UUID requestingDoctorId){

        // FETCH
        DoctorSchedule schedule = doctorScheduleRepositoryPort
                .findById(scheduleId)
                .orElseThrow(() -> new ScheduleNotFoundException(scheduleId)); // VALIDATE

        // VALIDATE
        if(!requestingDoctorId.equals(schedule.getDoctorId())){
            throw new InvalidOwnerException("You don't own this resource in order to delete it");
        }

        // PERSIST
        doctorScheduleRepositoryPort.delete(scheduleId);
    }

}
