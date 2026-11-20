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

import com.explorworld.dao.GuideDAO;
import com.explorworld.model.DataException;
import com.explorworld.model.Guide;
import com.explorworld.model.Results;
import com.explorworld.model.criteria.GuideCriteria;
import com.explorworld.utils.HQLUtils;

@Repository
public class GuideDAOImpl implements GuideDAO {

    private static final Logger logger = LogManager.getLogger(GuideDAOImpl.class);

    @Override
    public Guide findById(Session session, Long id) throws DataException {
        logger.debug("findById: {}", id);
        try {
            return session.get(Guide.class, id);
        } catch (RuntimeException e) {
            logger.error("Error al buscar guia con id {}: {}", id, e);
            throw new DataException("Error al buscar guia con id: " + id, e);
        }
    }

    @Override
    public Guide findByEmail(Session session, String email) throws DataException {
        logger.debug("findByEmail: {}", email);
        try {
            String hql = "FROM Guide g WHERE LOWER(g.email) = :email";
            Query<Guide> query = session.createQuery(hql, Guide.class);
            query.setParameter("email", email.trim().toLowerCase());
            return query.uniqueResult();
        } catch (RuntimeException e) {
            logger.error("Error al buscar guia por email {}: {}", email, e);
            throw new DataException("Error al buscar guia por email: " + email, e);
        }
    }
    
    @Override
    public boolean emailExists(Session session, String email) throws DataException {
        logger.debug("emailExists: {}", email);
        try {
            String hql = "SELECT COUNT(g.id) FROM Guide g WHERE LOWER(g.email) = :email";
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("email", email.trim().toLowerCase());
            Long count = query.uniqueResult();
            return count != null && count > 0;
        } catch (RuntimeException e) {
            logger.error("Error al comprobar existencia de email en Guide {}: {}", email, e.getMessage(), e);
            throw new DataException("Error al comprobar existencia de email: " + email, e);
        }
    }

    @Override
    public List<Guide> findAll(Session session) throws DataException {
        logger.debug("findAll");
        try {
            String hql = "FROM Guide g ORDER BY g.lastName ASC, g.name ASC";
            return session.createQuery(hql, Guide.class).getResultList();
        } catch (RuntimeException e) {
            logger.error("Error al listar todos los guias: {}", e);
            throw new DataException("Error al listar todos los guias", e);
        }
    }

    @Override
    public Results<Guide> findByCriteria(Session session, GuideCriteria criteria, int startIndex, int count) throws DataException {
        logger.debug("findByCriteria - Criteria: {}, startIndex: {}, count: {}", criteria, startIndex, count);

        if (criteria == null) {
            criteria = new GuideCriteria();
        }

        Results<Guide> results = new Results<>();

        try {
            List<String> conditions = new ArrayList<>();
            Map<String, Object> params = new HashMap<>();

            HQLUtils.addClause(criteria.getId(), conditions, "g.id = :id", params, "id", criteria.getId());
            if (criteria.getId() == null) {
                HQLUtils.addClause(criteria.getName(), conditions, "UPPER(g.name) LIKE :name",
                        params, "name", HQLUtils.like(criteria.getName()));

                HQLUtils.addClause(criteria.getLastName(), conditions, "UPPER(g.lastName) LIKE :lastName",
                        params, "lastName", HQLUtils.like(criteria.getLastName()));

                HQLUtils.addClause(criteria.getNieDni(), conditions, "UPPER(g.nieDni) LIKE :nieDni",
                        params, "nieDni", HQLUtils.like(criteria.getNieDni()));

                HQLUtils.addClause(criteria.getEmail(), conditions, "UPPER(g.email) LIKE :email",
                        params, "email", HQLUtils.like(criteria.getEmail()));

                HQLUtils.addClause(criteria.getPhoneNumber(), conditions, "g.phoneNumber LIKE :phoneNumber",
                        params, "phoneNumber", HQLUtils.like(criteria.getPhoneNumber()));

                HQLUtils.addClause(criteria.getBirthDate(), conditions, "g.birthDate >= :birthDate",
                        params, "birthDate", criteria.getBirthDate());

                HQLUtils.addClause(criteria.getGenderId(), conditions, "g.gender.id = :genderId",
                        params, "genderId", criteria.getGenderId());
            }

            String where = conditions.isEmpty() ? "" : " WHERE " + String.join(" AND ", conditions);

            String hqlData = "SELECT g FROM Guide g" + where + " ORDER BY g.lastName ASC, g.name ASC";
            String hqlCount = "SELECT COUNT(g.id) FROM Guide g" + where;

            Query<Guide> dataQuery = session.createQuery(hqlData, Guide.class);
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
            logger.error("Error en findByCriteria de Guide: {}", e);
            throw new DataException("Error al buscar guias con criterios", e);
        }
    }

    @Override
    public Guide create(Session session, Guide guide) throws DataException {
        logger.debug("create: {}", guide.getEmail());
        try {
            session.save(guide);
            return guide;
        } catch (RuntimeException e) {
            logger.error("Error al crear guia {}: {}", guide.getEmail(), e);
            throw new DataException("Error al crear guia: " + guide.getEmail(), e);
        }
    }

    @Override
    public void update(Session session, Guide guide) throws DataException {
        logger.debug("update ID: {}", guide.getId());
        try {
            session.update(guide);
        } catch (RuntimeException e) {
            logger.error("Error al actualizar guia ID {}: {}", guide.getId(), e);
            throw new DataException("Error al actualizar guia con ID: " + guide.getId(), e);
        }
    }

    @Override
    public void delete(Session session, Guide guide) throws DataException {
        logger.debug("delete ID: {}", guide.getId());
        try {
            session.delete(guide);
        } catch (RuntimeException e) {
            logger.error("Error al eliminar guia ID {}: {}", guide.getId(), e);
            throw new DataException("Error al eliminar guia con ID: " + guide.getId(), e);
        }
    }
}