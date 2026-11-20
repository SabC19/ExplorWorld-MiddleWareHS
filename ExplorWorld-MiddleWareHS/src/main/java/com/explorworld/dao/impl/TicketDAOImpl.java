package com.explorworld.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.explorworld.dao.TicketDAO;
import com.explorworld.model.DataException;
import com.explorworld.model.Results;
import com.explorworld.model.Ticket;
import com.explorworld.model.criteria.TicketCriteria;
import com.explorworld.utils.HQLUtils;

@Repository
public class TicketDAOImpl implements TicketDAO {

    private static final Logger logger = LogManager.getLogger(TicketDAOImpl.class);

    @Override
    public Ticket findById(Session session, Long id) throws DataException {
        logger.debug("findById: {}", id);
        try {
            return session.get(Ticket.class, id);
        } catch (RuntimeException e) {
            logger.error("Error al buscar el ticket con id {}: {}", id, e.getMessage(), e);
            throw new DataException("Error al buscar el ticket con id: " + id, e);
        }
    }

    @Override
    public Ticket findByTicketNumber(Session session, String ticketNumber) throws DataException {
        logger.debug("findByTicketNumber: {}", ticketNumber);
        try {
            String hql = "FROM Ticket t WHERE t.ticketNumber = :ticketNumber";
            Query<Ticket> query = session.createQuery(hql, Ticket.class);
            query.setParameter("ticketNumber", ticketNumber);
            return query.uniqueResult();
        } catch (RuntimeException e) {
            logger.error("Error al buscar ticket por numero {}: {}", ticketNumber, e.getMessage(), e);
            throw new DataException("Error al buscar ticket por numero: " + ticketNumber, e);
        }
    }

    @Override
    public List<Ticket> findByParticipantId(Session session, Long participantId) throws DataException {
        logger.debug("findByParticipantId: {}", participantId);
        try {
            String hql = "FROM Ticket t WHERE t.participant.id = :participantId ORDER BY t.registrationDate DESC";
            Query<Ticket> query = session.createQuery(hql, Ticket.class);
            query.setParameter("participantId", participantId);
            return query.getResultList();
        } catch (RuntimeException e) {
            logger.error("Error al buscar tickets del participante ID {}: {}", participantId, e.getMessage(), e);
            throw new DataException("Error al buscar tickets del participante ID: " + participantId, e);
        }
    }

    @Override
    public Results<Ticket> findByCriteria(Session session, TicketCriteria criteria, int startIndex, int count) throws DataException {
        logger.debug("findByCriteria - Criteria: {}, startIndex: {}, count: {}", criteria, startIndex, count);

        if (criteria == null) {
            criteria = new TicketCriteria();
        }

        Results<Ticket> results = new Results<>();

        try {
            List<String> conditions = new ArrayList<>();
            Map<String, Object> params = new HashMap<>();

            HQLUtils.addClause(criteria.getId(), conditions, "t.id = :id", params, "id", criteria.getId());
            if (criteria.getId() == null) {
                HQLUtils.addClause(criteria.getTicketNumber(), conditions, "UPPER(t.ticketNumber) LIKE :ticketNumber",
                        params, "ticketNumber", HQLUtils.like(criteria.getTicketNumber()));

                HQLUtils.addClause(criteria.getStatus(), conditions, "UPPER(t.status) = :status",
                        params, "status", criteria.getStatus() != null ? criteria.getStatus().trim().toUpperCase() : null);

                HQLUtils.addClause(criteria.getRegistrationDate(), conditions, "t.registrationDate >= :registrationDate",
                        params, "registrationDate", criteria.getRegistrationDate());

                HQLUtils.addClause(criteria.getPrice(), conditions, "t.price <= :price",
                        params, "price", criteria.getPrice());

                HQLUtils.addClause(criteria.getActivityId(), conditions, "t.activity.id = :activityId",
                        params, "activityId", criteria.getActivityId());

                HQLUtils.addClause(criteria.getParticipantId(), conditions, "t.participant.id = :participantId",
                        params, "participantId", criteria.getParticipantId());
            }

            String where = conditions.isEmpty() ? "" : " WHERE " + String.join(" AND ", conditions);

            String hqlData = "SELECT t FROM Ticket t" + where + " ORDER BY t.registrationDate DESC, t.id DESC";
            String hqlCount = "SELECT COUNT(t.id) FROM Ticket t" + where;

            logger.debug("HQL datos: {} | params: {}", hqlData, params);

            Query<Ticket> dataQuery = session.createQuery(hqlData, Ticket.class);
            Query<Long> countQuery = session.createQuery(hqlCount, Long.class);

            for (Map.Entry<String, Object> entry : params.entrySet()) {
                dataQuery.setParameter(entry.getKey(), entry.getValue());
                countQuery.setParameter(entry.getKey(), entry.getValue());
            }

            int offset = Math.max(startIndex, 0);
            dataQuery.setFirstResult(offset);
            dataQuery.setMaxResults(count);

            List<Ticket> page = dataQuery.getResultList();
            Long total = countQuery.uniqueResult();

            results.setPage(page);
            results.setStartIndex(offset);
            results.setTotal(total != null ? total : 0L);

            return results;
        } catch (RuntimeException e) {
            logger.error("Error en findByCriteria de Ticket: {}", e.getMessage(), e);
            throw new DataException("Error al buscar tickets con criterios", e);
        }
    }

    @Override
    public Ticket create(Session session, Ticket ticket) throws DataException {
        logger.debug("create: {}", ticket);
        try {
            session.save(ticket);
            return ticket;
        } catch (RuntimeException e) {
            logger.error("Error al crear el ticket {}: {}", ticket, e.getMessage(), e);
            throw new DataException("Error al crear el ticket: " + ticket, e);
        }
    }

    @Override
    public void update(Session session, Ticket ticket) throws DataException {
        logger.debug("update: {}", ticket);
        try {
            session.update(ticket);
        } catch (RuntimeException e) {
            logger.error("Error al actualizar el ticket {}: {}", ticket, e.getMessage(), e);
            throw new DataException("Error al actualizar el ticket con ID: " + ticket.getId(), e);
        }
    }

    @Override
    public void delete(Session session, Ticket ticket) throws DataException {
        logger.debug("delete: {}", ticket);
        try {
            session.delete(ticket);
        } catch (RuntimeException e) {
            logger.error("Error al eliminar el ticket {}: {}", ticket, e.getMessage(), e);
            throw new DataException("Error al eliminar el ticket con ID: " + ticket.getId(), e);
        }
    }
}