package com.clinicbook.application.service;


import com.clinicbook.application.dtos.AppointmentsDoctorResult;
import com.clinicbook.application.dtos.AppointmentsPatientResult;
import com.clinicbook.application.dtos.CreateAppointmentCommand;
import com.clinicbook.domain.model.TimeSlot;
import com.clinicbook.domain.exception.*;
import com.clinicbook.domain.model.Appointment;
import com.clinicbook.domain.model.DoctorSchedule;
import com.clinicbook.domain.port.AppointmentRepositoryPort;
import com.clinicbook.domain.port.DoctorRepositoryPort;
import com.clinicbook.domain.port.DoctorScheduleRepositoryPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
public class AppointmentService {
    private final AppointmentRepositoryPort appointmentRepository;
    private final DoctorScheduleRepositoryPort doctorScheduleRepository;
    private final DoctorRepositoryPort doctorRepository;

    // COMMANDS

    public Appointment createAppointment(CreateAppointmentCommand command){
        Appointment appointment = new Appointment(
                UUID.randomUUID(), command.patientId(), command.doctorId(), command.date(),command.startTime(), command.endTime()
        );

        List<DoctorSchedule> schedules = doctorScheduleRepository.findByDoctorIdAndDayOfWeek(command.doctorId(), command.date().getDayOfWeek());

        boolean isWithin =
                schedules.stream().anyMatch(s -> s.coversInterval(appointment.getStartTime(), appointment.getEndTime()));

        if(!isWithin){
            throw new AppointmentNotInScheduleException("The appointment is not within the doctor schedule");
        }

        if (appointmentRepository.findAllByDoctorIdAndDate(command.doctorId(), command.date()).stream().anyMatch(appointment::overlapsWith)){
            throw new AppointmentOverlapException("The new appointment overlaps with other");
        }

        return appointmentRepository.save(appointment);
    }

    public Appointment confirmAppointment(UUID appointmentId){

        // FETCH + VALIDATE
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("The appointment couldn't be found"));



        // MUTATE
        appointment.confirm();

        // SAVE
        return appointmentRepository.save(appointment);

    }

    public Appointment completeAsDoctor(UUID appointmentId, UUID requestingUserId){
        // FETCH + VALIDATE
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("The appointment couldn't be found"));

        if(!appointment.getDoctorId().equals(requestingUserId)){
            throw new InvalidOwnerException("You don't own this resource");
        }

        // MUTATE
        appointment.complete();

        // SAVE
        return appointmentRepository.save(appointment);
    }

    public Appointment completeAsReceptionist(UUID appointmentId){
        // FETCH + VALIDATE
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("The appointment couldn't be found"));

        // MUTATE
        appointment.complete();

        // SAVE
        return appointmentRepository.save(appointment);
    }


    public Appointment cancelOwnAppointment(UUID appointmentId, UUID requestingPatientId){
        // FETCH + VALIDATE
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("The appointment couldn't be found"));

        if(!appointment.getPatientId().equals(requestingPatientId)){
            throw new InvalidOwnerException("This appointment wasn't booked for you");
        }

        // MUTATE
        appointment.cancel();

        // SAVE
        return appointmentRepository.save(appointment);
    }

    public Appointment cancelAnyAppointment(UUID appointmentId){
        // FETCH + VALIDATE
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("The appointment couldn't be found"));

        // MUTATE
        appointment.cancel();

        // SAVE
        return appointmentRepository.save(appointment);
    }


    // QUERYs

    
    public List<AppointmentsPatientResult> getPatientAppointments(UUID patientId){
        return appointmentRepository.findPatientAgenda(patientId);
    }

    public List<AppointmentsDoctorResult> getDoctorAgendaByDate(UUID doctorId, LocalDate date){
        return appointmentRepository.findDoctorAgenda(doctorId, date);
    }

    public List<AppointmentsDoctorResult> getDoctorUpcomingAppointments(UUID doctorId){
        return appointmentRepository.findUpcomingByDoctorId(doctorId);
    }

    public List<TimeSlot> getAvailability(LocalDate date, UUID doctorId){

        List<Appointment> todayAppointments = appointmentRepository.findAllByDoctorIdAndDate(doctorId, date);
        List<DoctorSchedule> todayDoctorSchedules = doctorScheduleRepository.findByDoctorIdAndDayOfWeek(doctorId, date.getDayOfWeek());

        int doctorConsultationDurationMinutes = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new DoctorNotFoundException(doctorId))
                .getConsultationDurationMinutes();

        List<TimeSlot> timeSlots = new ArrayList<>();

        // ALL SCHEDULES
        for(DoctorSchedule s : todayDoctorSchedules){
            LocalTime timeCounter = s.getStartTime();
            while(!timeCounter.plusMinutes(doctorConsultationDurationMinutes).isAfter(s.getEndTime())){
                timeSlots.add(new TimeSlot(timeCounter, true));
                timeCounter = timeCounter.plusMinutes(doctorConsultationDurationMinutes);
            }
        }

        // MARK AS NOT AVAILABLE THE BUSY TIME SLOTS
        for(TimeSlot ts : timeSlots){
            for (Appointment appointment : todayAppointments){
                if((appointment.overlapsWith(ts.getTime(), ts.getTime().plusMinutes(doctorConsultationDurationMinutes))
                        && appointment.isActive())){
                    ts.setAvailable(false);
                }
            }
        }

        return timeSlots;

    }
}
