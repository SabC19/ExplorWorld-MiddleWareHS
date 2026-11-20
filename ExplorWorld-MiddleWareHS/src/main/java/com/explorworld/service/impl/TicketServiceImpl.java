package com.explorworld.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.explorworld.dao.ActivityDAO;
import com.explorworld.dao.ParticipantDAO;
import com.explorworld.dao.TicketDAO;
import com.explorworld.model.Activity;
import com.explorworld.model.DataException;
import com.explorworld.model.Participant;
import com.explorworld.model.Results;
import com.explorworld.model.Ticket;
import com.explorworld.model.criteria.TicketCriteria;
import com.explorworld.service.ServiceException;
import com.explorworld.service.TicketService;

@Service
public class TicketServiceImpl implements TicketService {

    private static final Logger logger = LogManager.getLogger(TicketServiceImpl.class);

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private TicketDAO ticketDAO;

    @Autowired
    private ActivityDAO activityDAO;

    @Autowired
    private ParticipantDAO participantDAO;

    @Override
    @Transactional(readOnly = true)
    public Ticket findById(Long id) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            return ticketDAO.findById(session, id);
        } catch (DataException e) {
            logger.error("Error al buscar ticket ID {}: {}", id, e.getMessage(), e);
            throw new ServiceException("No se pudo recuperar el ticket.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Ticket findByTicketNumber(String ticketNumber) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            return ticketDAO.findByTicketNumber(session, ticketNumber);
        } catch (DataException e) {
            logger.error("Error al buscar ticket por número {}: {}", ticketNumber, e.getMessage(), e);
            throw new ServiceException("No se pudo recuperar el ticket.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Ticket> findByParticipantId(Long participantId) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            return ticketDAO.findByParticipantId(session, participantId);
        } catch (DataException e) {
            logger.error("Error al listar tickets del participante ID {}: {}", participantId, e.getMessage(), e);
            throw new ServiceException("No se pudieron recuperar los tickets del participante.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Results<Ticket> findByCriteria(TicketCriteria criteria, int startIndex, int count) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            return ticketDAO.findByCriteria(session, criteria, startIndex, count);
        } catch (DataException e) {
            logger.error("Error en búsqueda paginada de tickets: {}", e.getMessage(), e);
            throw new ServiceException("Error al buscar tickets.", e);
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public Ticket create(Ticket ticket) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();

            Activity activity = activityDAO.findById(session, ticket.getActivity().getId());
            if (activity == null) {
                throw new ServiceException("La actividad seleccionada no existe.");
            }

            Participant participant = participantDAO.findById(session, ticket.getParticipant().getId());
            if (participant == null) {
                throw new ServiceException("El participante indicado no existe.");
            }

            // Comprobar aforo disponible
            if (activity.getCapacity() != null) {
                TicketCriteria countCriteria = new TicketCriteria();
                countCriteria.setActivityId(activity.getId());
                long sold = ticketDAO.findByCriteria(session, countCriteria, 0, 1).getTotal();
                if (sold >= activity.getCapacity()) {
                    throw new ServiceException("No quedan plazas disponibles para la actividad: " + activity.getName());
                }
            }

            ticket.setActivity(activity);
            ticket.setParticipant(participant);

            if (ticket.getRegistrationDate() == null) {
                ticket.setRegistrationDate(new Date());
            }
            if (ticket.getStatus() == null || ticket.getStatus().trim().isEmpty()) {
                ticket.setStatus("PENDING");
            }
            if (ticket.getTicketNumber() == null || ticket.getTicketNumber().trim().isEmpty()) {
                ticket.setTicketNumber("EXP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            }
            if (ticket.getPrice() == null) {
                ticket.setPrice(activity.getPriceMin());
            }

            Ticket created = ticketDAO.create(session, ticket);
            logger.info("Ticket creado: {} (ID: {})", created.getTicketNumber(), created.getId());
            return created;
        } catch (DataException e) {
            logger.error("Error al crear ticket: {}", e.getMessage(), e);
            throw new ServiceException("No se pudo crear la reserva.", e);
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void update(Ticket ticket) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            ticketDAO.update(session, ticket);
            logger.info("Ticket actualizado con ID: {}", ticket.getId());
        } catch (DataException e) {
            logger.error("Error al actualizar ticket ID {}: {}", ticket.getId(), e.getMessage(), e);
            throw new ServiceException("No se pudo actualizar el ticket.", e);
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void cancel(Long id) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            Ticket existing = ticketDAO.findById(session, id);
            if (existing == null) {
                throw new ServiceException("No existe ningún ticket con ID: " + id);
            }
            existing.setStatus("CANCELLED");
            ticketDAO.update(session, existing);
            logger.info("Ticket ID {} marcado como CANCELLED", id);
        } catch (DataException e) {
            logger.error("Error al cancelar ticket ID {}: {}", id, e.getMessage(), e);
            throw new ServiceException("No se pudo cancelar el ticket.", e);
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void delete(Long id) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            Ticket existing = ticketDAO.findById(session, id);
            if (existing == null) {
                throw new ServiceException("No existe ningún ticket con ID: " + id);
            }
            ticketDAO.delete(session, existing);
            logger.info("Ticket eliminado con ID: {}", id);
        } catch (DataException e) {
            logger.error("Error al eliminar ticket ID {}: {}", id, e.getMessage(), e);
            throw new ServiceException("No se pudo eliminar el ticket.", e);
        }
    }
}