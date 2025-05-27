package com.appointmentservice.service;

import com.appointmentservice.domain.Appointment;
import java.util.List;

public interface AppointmentService {
    Appointment create(Appointment appointment);
    Appointment getById(Long id);
    List<Appointment> getAll();
    Appointment update(Long id, Appointment appointment);
    void delete(Long id);
    List<Appointment> getByPatient(Long patientId);
    List<Appointment> getByDoctor(Long doctorId);
}
