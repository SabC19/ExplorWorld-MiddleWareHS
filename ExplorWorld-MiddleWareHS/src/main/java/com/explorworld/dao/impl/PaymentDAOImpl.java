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

import com.explorworld.dao.PaymentDAO;
import com.explorworld.model.DataException;
import com.explorworld.model.Payment;
import com.explorworld.model.Results;
import com.explorworld.model.criteria.PaymentCriteria;
import com.explorworld.utils.HQLUtils;

@Repository
public class PaymentDAOImpl implements PaymentDAO {

    private static final Logger logger = LogManager.getLogger(PaymentDAOImpl.class);

    @Override
    public Payment findById(Session session, Long id) throws DataException {
        logger.debug("findById: {}", id);
        try {
            return session.get(Payment.class, id);
        } catch (RuntimeException e) {
            logger.error("Error al buscar el pago con id {}: {}", id, e.getMessage(), e);
            throw new DataException("Error al buscar el pago con id: " + id, e);
        }
    }

    @Override
    public List<Payment> findByTicketId(Session session, Long ticketId) throws DataException {
        logger.debug("findByTicketId: {}", ticketId);
        try {
            String hql = "FROM Payment p WHERE p.ticket.id = :ticketId ORDER BY p.paymentDate DESC";
            Query<Payment> query = session.createQuery(hql, Payment.class);
            query.setParameter("ticketId", ticketId);
            return query.getResultList();
        } catch (RuntimeException e) {
            logger.error("Error al buscar pagos del ticket ID {}: {}", ticketId, e.getMessage(), e);
            throw new DataException("Error al buscar pagos del ticket ID: " + ticketId, e);
        }
    }

    @Override
    public Results<Payment> findByCriteria(Session session, PaymentCriteria criteria, int startIndex, int count) throws DataException {
        logger.debug("findByCriteria - Criteria: {}, startIndex: {}, count: {}", criteria, startIndex, count);

        if (criteria == null) {
            criteria = new PaymentCriteria();
        }

        Results<Payment> results = new Results<>();

        try {
            List<String> conditions = new ArrayList<>();
            Map<String, Object> params = new HashMap<>();

            HQLUtils.addClause(criteria.getId(), conditions, "p.id = :id", params, "id", criteria.getId());
            if (criteria.getId() == null) {
                HQLUtils.addClause(criteria.getStatus(), conditions, "UPPER(p.status) = :status",
                        params, "status", criteria.getStatus() != null ? criteria.getStatus().trim().toUpperCase() : null);

                HQLUtils.addClause(criteria.getAmount(), conditions, "p.amount >= :amount",
                        params, "amount", criteria.getAmount());

                HQLUtils.addClause(criteria.getPaymentDate(), conditions, "p.paymentDate >= :paymentDate",
                        params, "paymentDate", criteria.getPaymentDate());

                HQLUtils.addClause(criteria.getTicketId(), conditions, "p.ticket.id = :ticketId",
                        params, "ticketId", criteria.getTicketId());

                HQLUtils.addClause(criteria.getPaymentMethodId(), conditions, "p.paymentMethod.id = :paymentMethodId",
                        params, "paymentMethodId", criteria.getPaymentMethodId());
            }

            String where = conditions.isEmpty() ? "" : " WHERE " + String.join(" AND ", conditions);

            String hqlData = "SELECT p FROM Payment p" + where + " ORDER BY p.paymentDate DESC, p.id DESC";
            String hqlCount = "SELECT COUNT(p.id) FROM Payment p" + where;

            logger.debug("HQL datos: {} | params: {}", hqlData, params);

            Query<Payment> dataQuery = session.createQuery(hqlData, Payment.class);
            Query<Long> countQuery = session.createQuery(hqlCount, Long.class);

            for (Map.Entry<String, Object> entry : params.entrySet()) {
                dataQuery.setParameter(entry.getKey(), entry.getValue());
                countQuery.setParameter(entry.getKey(), entry.getValue());
            }

            int offset = Math.max(startIndex, 0);
            dataQuery.setFirstResult(offset);
            dataQuery.setMaxResults(count);

            List<Payment> page = dataQuery.getResultList();
            Long total = countQuery.uniqueResult();

            results.setPage(page);
            results.setStartIndex(offset);
            results.setTotal(total != null ? total : 0L);

            return results;
        } catch (RuntimeException e) {
            logger.error("Error en findByCriteria de Payment: {}", e.getMessage(), e);
            throw new DataException("Error al buscar pagos con criterios", e);
        }
    }

    @Override
    public Payment create(Session session, Payment payment) throws DataException {
        logger.debug("create: {}", payment);
        try {
            session.save(payment);
            return payment;
        } catch (RuntimeException e) {
            logger.error("Error al crear el pago {}: {}", payment, e.getMessage(), e);
            throw new DataException("Error al crear el pago: " + payment, e);
        }
    }

    @Override
    public void update(Session session, Payment payment) throws DataException {
        logger.debug("update: {}", payment);
        try {
            session.update(payment);
        } catch (RuntimeException e) {
            logger.error("Error al actualizar el pago {}: {}", payment, e.getMessage(), e);
            throw new DataException("Error al actualizar el pago con ID: " + payment.getId(), e);
        }
    }

    @Override
    public void delete(Session session, Payment payment) throws DataException {
        logger.debug("delete: {}", payment);
        try {
            session.delete(payment);
        } catch (RuntimeException e) {
            logger.error("Error al eliminar el pago {}: {}", payment, e.getMessage(), e);
            throw new DataException("Error al eliminar el pago con ID: " + payment.getId(), e);
        }
    }
}