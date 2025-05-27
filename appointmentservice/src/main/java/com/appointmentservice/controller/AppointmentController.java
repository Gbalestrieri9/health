package com.appointmentservice.controller;

import com.appointmentservice.domain.Appointment;
import com.appointmentservice.service.AppointmentService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentController.class);
    private final AppointmentService service;

    public AppointmentController(AppointmentService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Appointment> create(@Valid @RequestBody Appointment appointment) {
        logger.debug("Controller: create appointment request received: {}", appointment);
        Appointment created = service.create(appointment);
        logger.debug("Controller: appointment created response: {}", created);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getById(@PathVariable Long id) {
        logger.debug("Controller: getById request for id: {}", id);
        Appointment found = service.getById(id);
        logger.debug("Controller: getById response: {}", found);
        return ResponseEntity.ok(found);
    }

    @GetMapping
    public ResponseEntity<List<Appointment>> getAll() {
        logger.debug("Controller: getAll request");
        List<Appointment> list = service.getAll();
        logger.debug("Controller: getAll response count: {}", list.size());
        return ResponseEntity.ok(list);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Appointment> update(@PathVariable Long id,
                                              @Valid @RequestBody Appointment appointment) {
        logger.debug("Controller: update request for id: {}, data: {}", id, appointment);
        Appointment updated = service.update(id, appointment);
        logger.debug("Controller: update response: {}", updated);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.debug("Controller: delete request for id: {}", id);
        service.delete(id);
        logger.debug("Controller: delete completed for id: {}", id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Appointment>> byPatient(@PathVariable Long patientId) {
        logger.debug("Controller: byPatient request for patientId: {}", patientId);
        List<Appointment> list = service.getByPatient(patientId);
        logger.debug("Controller: byPatient response count: {}", list.size());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<Appointment>> byDoctor(@PathVariable Long doctorId) {
        logger.debug("Controller: byDoctor request for doctorId: {}", doctorId);
        List<Appointment> list = service.getByDoctor(doctorId);
        logger.debug("Controller: byDoctor response count: {}", list.size());
        return ResponseEntity.ok(list);
    }
}
