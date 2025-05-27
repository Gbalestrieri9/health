package com.appointmentservice.service.impl;

import com.appointmentservice.domain.Appointment;
import com.appointmentservice.dto.AppointmentEvent;
import com.appointmentservice.exception.ResourceNotFoundException;
import com.appointmentservice.repository.AppointmentRepository;
import com.appointmentservice.service.AppointmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentServiceImpl.class);

    private final AppointmentRepository repository;
    private final RabbitTemplate rabbitTemplate;
    private final String exchange;
    private final String routingKey;

    public AppointmentServiceImpl(AppointmentRepository repository,
                                  RabbitTemplate rabbitTemplate,
                                  @Value("${notification.exchange}") String exchange,
                                  @Value("${notification.routingKey}") String routingKey) {
        this.repository = repository;
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
        this.routingKey = routingKey;
    }

    @Override
    public Appointment create(Appointment appointment) {
        logger.debug("Criando agendamento: {}", appointment);
        Appointment saved = repository.save(appointment);
        logger.info("Agendamento criado com id: {}", saved.getId());
        rabbitTemplate.convertAndSend(exchange, routingKey, toEvent(saved));
        logger.debug("Evento publicado para agendamento id: {}", saved.getId());
        return saved;
    }

    @Override
    public Appointment getById(Long id) {
        logger.debug("Buscando agendamento por id: {}", id);
        Appointment found = repository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Agendamento não encontrado para id: {}", id);
                    return new ResourceNotFoundException("Appointment not found with id " + id);
                });
        logger.info("Agendamento retornado: {}", found);
        return found;
    }

    @Override
    public List<Appointment> getAll() {
        logger.debug("Buscando todos os agendamentos");
        List<Appointment> list = repository.findAll();
        logger.info("Total de agendamentos encontrados: {}", list.size());
        return list;
    }

    @Override
    public Appointment update(Long id, Appointment appointment) {
        logger.debug("Atualizando agendamento id: {} com dados: {}", id, appointment);
        Appointment existing = getById(id);
        existing.setPatientId(appointment.getPatientId());
        existing.setDoctorId(appointment.getDoctorId());
        existing.setDateTime(appointment.getDateTime());
        existing.setStatus(appointment.getStatus());
        Appointment updated = repository.save(existing);
        logger.info("Agendamento atualizado: {}", updated);
        rabbitTemplate.convertAndSend(exchange, routingKey, toEvent(updated));
        logger.debug("Evento de atualização publicado para agendamento id: {}", updated.getId());
        return updated;
    }

    @Override
    public void delete(Long id) {
        logger.debug("Deletando agendamento id: {}", id);
        repository.deleteById(id);
        logger.info("Agendamento deletado id: {}", id);
    }

    @Override
    public List<Appointment> getByPatient(Long patientId) {
        logger.debug("Buscando agendamentos para patientId: {}", patientId);
        List<Appointment> list = repository.findByPatientId(patientId);
        logger.info("Agendamentos encontrados para paciente {}: {} registros", patientId, list.size());
        return list;
    }

    @Override
    public List<Appointment> getByDoctor(Long doctorId) {
        logger.debug("Buscando agendamentos para doctorId: {}", doctorId);
        List<Appointment> list = repository.findByDoctorId(doctorId);
        logger.info("Agendamentos encontrados para médico {}: {} registros", doctorId, list.size());
        return list;
    }

    private AppointmentEvent toEvent(Appointment appt) {
        AppointmentEvent ev = new AppointmentEvent();
        ev.setId(appt.getId());
        ev.setPatientId(appt.getPatientId());
        ev.setDoctorId(appt.getDoctorId());
        ev.setDateTime(appt.getDateTime());
        ev.setStatus(appt.getStatus());
        return ev;
    }
}
