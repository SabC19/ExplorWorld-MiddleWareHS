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

import com.explorworld.dao.ParticipantDAO;
import com.explorworld.model.DataException;
import com.explorworld.model.Participant;
import com.explorworld.model.Results;
import com.explorworld.model.criteria.ParticipantCriteria;
import com.explorworld.utils.HQLUtils;

@Repository
public class ParticipantDAOImpl implements ParticipantDAO {

    private static final Logger logger = LogManager.getLogger(ParticipantDAOImpl.class);

    @Override
    public Participant findById(Session session, Long id) throws DataException {
        logger.debug("findById: {}", id);
        try {
            return session.get(Participant.class, id);
        } catch (RuntimeException e) {
            logger.error("Error al buscar participante con id {}: {}", id, e);
            throw new DataException("Error al buscar participante con id: " + id, e);
        }
    }

    @Override
    public Participant findByEmail(Session session, String email) throws DataException {
        logger.debug("findByEmail: {}", email);
        try {
            String hql = "FROM Participant p WHERE LOWER(p.email) = :email";
            Query<Participant> query = session.createQuery(hql, Participant.class);
            query.setParameter("email", email.trim().toLowerCase());
            return query.uniqueResult();
        } catch (RuntimeException e) {
            logger.error("Error al buscar participante por email {}: {}", email, e);
            throw new DataException("Error al buscar participante por email: " + email, e);
        }
    }

    @Override
    public boolean emailExists(Session session, String email) throws DataException {
        logger.debug("emailExists: {}", email);
        try {
            String hql = "SELECT COUNT(p.id) FROM Participant p WHERE LOWER(p.email) = :email";
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("email", email.trim().toLowerCase());
            Long count = query.uniqueResult();
            return count != null && count > 0;
        } catch (RuntimeException e) {
            logger.error("Error al comprobar existencia de email {}: {}", email, e);
            throw new DataException("Error al comprobar existencia de email: " + email, e);
        }
    }

    @Override
    public List<Participant> findAll(Session session) throws DataException {
        logger.debug("findAll");
        try {
            String hql = "FROM Participant p ORDER BY p.lastName ASC, p.name ASC";
            return session.createQuery(hql, Participant.class).getResultList();
        } catch (RuntimeException e) {
            logger.error("Error al listar todos los participantes: {}", e);
            throw new DataException("Error al listar todos los participantes", e);
        }
    }

    @Override
    public Results<Participant> findByCriteria(Session session, ParticipantCriteria criteria, int startIndex, int count) throws DataException {
        logger.debug("findByCriteria - Criteria: {}, startIndex: {}, count: {}", criteria, startIndex, count);

        if (criteria == null) {
            criteria = new ParticipantCriteria();
        }

        Results<Participant> results = new Results<>();

        try {
            List<String> conditions = new ArrayList<>();
            Map<String, Object> params = new HashMap<>();

            HQLUtils.addClause(criteria.getId(), conditions, "p.id = :id", params, "id", criteria.getId());
            if (criteria.getId() == null) {
                HQLUtils.addClause(criteria.getName(), conditions, "UPPER(p.name) LIKE :name",
                        params, "name", HQLUtils.like(criteria.getName()));

                HQLUtils.addClause(criteria.getLastName(), conditions, "UPPER(p.lastName) LIKE :lastName",
                        params, "lastName", HQLUtils.like(criteria.getLastName()));

                HQLUtils.addClause(criteria.getEmail(), conditions, "UPPER(p.email) LIKE :email",
                        params, "email", HQLUtils.like(criteria.getEmail()));

                HQLUtils.addClause(criteria.getNumberPhone(), conditions, "p.phoneNumber LIKE :phoneNumber",
                        params, "phoneNumber", HQLUtils.like(criteria.getNumberPhone()));

                HQLUtils.addClause(criteria.getBirthDate(), conditions, "p.birthDate >= :birthDate",
                        params, "birthDate", criteria.getBirthDate());

                HQLUtils.addClause(criteria.getGenderId(), conditions, "p.gender.id = :genderId",
                        params, "genderId", criteria.getGenderId());
            }

            String where = conditions.isEmpty() ? "" : " WHERE " + String.join(" AND ", conditions);

            String hqlData = "SELECT p FROM Participant p" + where + " ORDER BY p.lastName ASC, p.name ASC";
            String hqlCount = "SELECT COUNT(p.id) FROM Participant p" + where;

            Query<Participant> dataQuery = session.createQuery(hqlData, Participant.class);
            Query<Long> countQuery = session.createQuery(hqlCount, Long.class);

            for (Map.Entry<String, Object> entry : params.entrySet()) {
                dataQuery.setParameter(entry.getKey(), entry.getValue());
                countQuery.setParameter(entry.getKey(), entry.getValue());
            }

            int offset = Math.max(startIndex, 0);
            dataQuery.setFirstResult(offset);
            dataQuery.setMaxResults(count);

            results.setPage(dataQuery.getResultList());
            results.setStartIndex(offset);
            Long total = countQuery.uniqueResult();
            results.setTotal(total != null ? total : 0L);

            return results;
        } catch (RuntimeException e) {
            logger.error("Error en findByCriteria de Participant: {}", e);
            throw new DataException("Error al buscar participantes con criterios", e);
        }
    }

    @Override
    public Participant create(Session session, Participant participant) throws DataException {
        logger.debug("create: {}", participant.getEmail());
        try {
            session.save(participant);
            return participant;
        } catch (RuntimeException e) {
            logger.error("Error al crear participante {}: {}", participant.getEmail(), e);
            throw new DataException("Error al crear participante: " + participant.getEmail(), e);
        }
    }

    @Override
    public void update(Session session, Participant participant) throws DataException {
        logger.debug("update ID: {}", participant.getId());
        try {
            session.update(participant);
        } catch (RuntimeException e) {
            logger.error("Error al actualizar participante ID {}: {}", participant.getId(), e);
            throw new DataException("Error al actualizar participante con ID: " + participant.getId(), e);
        }
    }

    @Override
    public void delete(Session session, Participant participant) throws DataException {
        logger.debug("delete ID: {}", participant.getId());
        try {
            session.delete(participant);
        } catch (RuntimeException e) {
            logger.error("Error al eliminar participante ID {}: {}", participant.getId(), e);
            throw new DataException("Error al eliminar participante con ID: " + participant.getId(), e);
        }
    }
}