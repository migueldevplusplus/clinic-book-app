package com.clinicbook.application.service;

import com.clinicbook.application.dtos.CreateAppointmentCommand;
import com.clinicbook.domain.enums.AppointmentStatus;
import com.clinicbook.domain.exception.*;
import com.clinicbook.domain.model.Appointment;
import com.clinicbook.domain.model.Doctor;
import com.clinicbook.domain.model.DoctorSchedule;
import com.clinicbook.domain.port.AppointmentRepositoryPort;
import com.clinicbook.domain.port.DoctorRepositoryPort;
import com.clinicbook.domain.port.DoctorScheduleRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepositoryPort appointmentRepository;

    @Mock
    private DoctorScheduleRepositoryPort doctorScheduleRepository;

    @Mock
    private DoctorRepositoryPort doctorRepository;

    @InjectMocks
    private AppointmentService appointmentService;

    // ---------- confirmAppointment ----------

    @Test
    void shouldConfirmAppointmentWhenAppointmentExists() {
        UUID appointmentId = UUID.randomUUID();
        Appointment appointment = new Appointment(
                appointmentId, UUID.randomUUID(), UUID.randomUUID(),
                LocalDate.now().plusDays(1), LocalTime.of(10, 0), LocalTime.of(10, 30)
        );

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(appointment)).thenReturn(appointment);

        Appointment result = appointmentService.confirmAppointment(appointmentId);

        assertEquals(AppointmentStatus.CONFIRMED, result.getStatus());
        verify(appointmentRepository).save(appointment);
    }

    @Test
    void shouldThrowWhenConfirmingNonExistentAppointment() {
        UUID appointmentId = UUID.randomUUID();
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.empty());

        assertThrows(AppointmentNotFoundException.class,
                () -> appointmentService.confirmAppointment(appointmentId));
    }

    // ---------- completeAsDoctor ----------

    @Test
    void shouldThrowWhenCompletingAsDoctorWhoIsNotOwner() {
        UUID appointmentId = UUID.randomUUID();
        UUID actualDoctorId = UUID.randomUUID();
        UUID impostorDoctorId = UUID.randomUUID();

        Appointment appointment = new Appointment(
                appointmentId, UUID.randomUUID(), actualDoctorId,
                LocalDate.now().plusDays(1), LocalTime.of(10, 0), LocalTime.of(10, 30)
        );

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));

        assertThrows(InvalidOwnerException.class,
                () -> appointmentService.completeAsDoctor(appointmentId, impostorDoctorId));
    }

    // ---------- cancelOwnAppointment ----------

    @Test
    void shouldCancelOwnAppointmentWhenRequestingPatientIsOwner() {
        UUID appointmentId = UUID.randomUUID();
        UUID patientId = UUID.randomUUID();

        Appointment appointment = new Appointment(
                appointmentId, patientId, UUID.randomUUID(),
                LocalDate.now().plusDays(1), LocalTime.of(10, 0), LocalTime.of(10, 30)
        );

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(appointment)).thenReturn(appointment);

        Appointment result = appointmentService.cancelOwnAppointment(appointmentId, patientId);

        assertEquals(AppointmentStatus.CANCELLED, result.getStatus());
        verify(appointmentRepository).save(appointment);
    }

    @Test
    void shouldThrowWhenCancellingAppointmentNotOwnedByPatient() {
        UUID appointmentId = UUID.randomUUID();
        UUID actualPatientId = UUID.randomUUID();
        UUID impostorPatientId = UUID.randomUUID();

        Appointment appointment = new Appointment(
                appointmentId, actualPatientId, UUID.randomUUID(),
                LocalDate.now().plusDays(1), LocalTime.of(10, 0), LocalTime.of(10, 30)
        );

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));

        assertThrows(InvalidOwnerException.class,
                () -> appointmentService.cancelOwnAppointment(appointmentId, impostorPatientId));
    }

    // ---------- createAppointment ----------

    private static final int CONSULTATION_MINUTES = 30;

    /** A doctor working that day from 10:00 to 11:00, i.e. slots at 10:00 and 10:30. */
    private void givenDoctorWorking(UUID doctorId, LocalDate date) {
        Doctor doctor = mock(Doctor.class);
        when(doctor.getConsultationDurationMinutes()).thenReturn(CONSULTATION_MINUTES);
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));

        DoctorSchedule schedule = new DoctorSchedule(
                UUID.randomUUID(), doctorId, date.getDayOfWeek(),
                LocalTime.of(10, 0), LocalTime.of(11, 0)
        );
        when(doctorScheduleRepository.findByDoctorIdAndDayOfWeek(doctorId, date.getDayOfWeek()))
                .thenReturn(List.of(schedule));
    }

    @Test
    void shouldCreateAppointmentWhenSlotIsFree() {
        UUID doctorId = UUID.randomUUID();
        UUID patientId = UUID.randomUUID();
        LocalDate date = LocalDate.now().plusDays(1);

        CreateAppointmentCommand command = new CreateAppointmentCommand(
                patientId, doctorId, date, LocalTime.of(10, 0), LocalTime.of(10, 30));

        givenDoctorWorking(doctorId, date);
        when(appointmentRepository.findAllByDoctorIdAndDate(doctorId, date)).thenReturn(List.of());
        when(appointmentRepository.save(any(Appointment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0)); // It returns the same argument that was passed to 'save'

        Appointment result = appointmentService.createAppointment(command);

        assertEquals(patientId, result.getPatientId());
        assertEquals(doctorId, result.getDoctorId());
        assertEquals(AppointmentStatus.PENDING, result.getStatus());
    }

    @Test
    void shouldThrowWhenDurationDoesNotMatchTheDoctorConsultation() {
        UUID doctorId = UUID.randomUUID();
        UUID patientId = UUID.randomUUID();
        LocalDate date = LocalDate.now().plusDays(1);

        // 45 minutes against a doctor who only takes 30-minute consultations
        CreateAppointmentCommand command = new CreateAppointmentCommand(
                patientId, doctorId, date, LocalTime.of(10, 0), LocalTime.of(10, 45));

        Doctor doctor = mock(Doctor.class);
        when(doctor.getConsultationDurationMinutes()).thenReturn(CONSULTATION_MINUTES);
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));

        assertThrows(InvalidScheduleException.class,
                () -> appointmentService.createAppointment(command));

        verify(appointmentRepository, never()).save(any());
    }

    /**
     * Guards the slot grid: 10:15 lasts the right 30 minutes and falls inside the
     * doctor's working hours, but it is not one of the slots the availability
     * endpoint offers. Accepting it would let a booking sit across two slots and
     * silently disappear from the agenda.
     */
    @Test
    void shouldThrowWhenStartTimeIsNotAlignedWithTheSlotGrid() {
        UUID doctorId = UUID.randomUUID();
        UUID patientId = UUID.randomUUID();
        LocalDate date = LocalDate.now().plusDays(1);

        CreateAppointmentCommand command = new CreateAppointmentCommand(
                patientId, doctorId, date, LocalTime.of(10, 15), LocalTime.of(10, 45));

        givenDoctorWorking(doctorId, date);
        when(appointmentRepository.findAllByDoctorIdAndDate(doctorId, date)).thenReturn(List.of());

        assertThrows(TimeSlotNotAvailableException.class,
                () -> appointmentService.createAppointment(command));

        verify(appointmentRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenDoctorHasNoScheduleThatDay() {
        UUID doctorId = UUID.randomUUID();
        UUID patientId = UUID.randomUUID();
        LocalDate date = LocalDate.now().plusDays(1);

        CreateAppointmentCommand command = new CreateAppointmentCommand(
                patientId, doctorId, date, LocalTime.of(10, 0), LocalTime.of(10, 30));

        Doctor doctor = mock(Doctor.class);
        when(doctor.getConsultationDurationMinutes()).thenReturn(CONSULTATION_MINUTES);
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));
        when(doctorScheduleRepository.findByDoctorIdAndDayOfWeek(doctorId, date.getDayOfWeek()))
                .thenReturn(List.of());
        when(appointmentRepository.findAllByDoctorIdAndDate(doctorId, date)).thenReturn(List.of());

        assertThrows(TimeSlotNotAvailableException.class,
                () -> appointmentService.createAppointment(command));
    }

    @Test
    void shouldThrowWhenSlotIsAlreadyTaken() {
        UUID doctorId = UUID.randomUUID();
        UUID patientId = UUID.randomUUID();
        LocalDate date = LocalDate.now().plusDays(1);

        CreateAppointmentCommand command = new CreateAppointmentCommand(
                patientId, doctorId, date, LocalTime.of(10, 0), LocalTime.of(10, 30));

        Appointment existing = new Appointment(
                UUID.randomUUID(), UUID.randomUUID(), doctorId, date,
                LocalTime.of(10, 0), LocalTime.of(10, 30)
        );

        givenDoctorWorking(doctorId, date);
        when(appointmentRepository.findAllByDoctorIdAndDate(doctorId, date)).thenReturn(List.of(existing));

        assertThrows(TimeSlotNotAvailableException.class,
                () -> appointmentService.createAppointment(command));

        verify(appointmentRepository, never()).save(any());
    }

    @Test
    void shouldFreeTheSlotWhenTheExistingAppointmentIsCancelled() {
        UUID doctorId = UUID.randomUUID();
        UUID patientId = UUID.randomUUID();
        LocalDate date = LocalDate.now().plusDays(1);

        CreateAppointmentCommand command = new CreateAppointmentCommand(
                patientId, doctorId, date, LocalTime.of(10, 0), LocalTime.of(10, 30));

        Appointment cancelled = new Appointment(
                UUID.randomUUID(), UUID.randomUUID(), doctorId, date,
                LocalTime.of(10, 0), LocalTime.of(10, 30)
        );
        cancelled.cancel();

        givenDoctorWorking(doctorId, date);
        when(appointmentRepository.findAllByDoctorIdAndDate(doctorId, date)).thenReturn(List.of(cancelled));
        when(appointmentRepository.save(any(Appointment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Appointment result = appointmentService.createAppointment(command);

        assertEquals(AppointmentStatus.PENDING, result.getStatus());
    }
}