package com.explorworld.dao.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.explorworld.dao.PaymentMethodDAO;
import com.explorworld.model.DataException;
import com.explorworld.model.PaymentMethod;

@Repository
public class PaymentMethodDAOImpl implements PaymentMethodDAO {

    private static final Logger logger = LogManager.getLogger(PaymentMethodDAOImpl.class);

    @Override
    public PaymentMethod findById(Session session, Long id) throws DataException {
        logger.debug("findById: {}", id);
        try {
            return session.get(PaymentMethod.class, id);
        } catch (RuntimeException e) {
            logger.error("Error al buscar el metodo de pago con id {}: {}", id, e);
            throw new DataException("Error al buscar el metodo de pago con id: " + id, e);
        }
    }

    @Override
    public List<PaymentMethod> findAll(Session session) throws DataException {
        logger.debug("findAll");
        try {
            String hql = "FROM PaymentMethod pm ORDER BY pm.name ASC";
            Query<PaymentMethod> query = session.createQuery(hql, PaymentMethod.class);
            return query.getResultList();
        } catch (RuntimeException e) {
            logger.error("Error al listar todos los metodos de pago: {}", e);
            throw new DataException("Error al listar todos los metodos de pago", e);
        }
    }

    @Override
    public List<PaymentMethod> findActive(Session session) throws DataException {
        logger.debug("findActive");
        try {
            String hql = "FROM PaymentMethod pm WHERE pm.active = true ORDER BY pm.name ASC";
            Query<PaymentMethod> query = session.createQuery(hql, PaymentMethod.class);
            return query.getResultList();
        } catch (RuntimeException e) {
            logger.error("Error al listar los metodos de pago activos: {}", e);
            throw new DataException("Error al listar los metodos de pago activos", e);
        }
    }

    @Override
    public PaymentMethod create(Session session, PaymentMethod paymentMethod) throws DataException {
        logger.debug("create: {}", paymentMethod);
        try {
            session.save(paymentMethod);
            return paymentMethod;
        } catch (RuntimeException e) {
            logger.error("Error al crear el metodo de pago {}: {}", paymentMethod, e);
            throw new DataException("Error al crear el metodo de pago: " + paymentMethod, e);
        }
    }

    @Override
    public void update(Session session, PaymentMethod paymentMethod) throws DataException {
        logger.debug("update: {}", paymentMethod);
        try {
            session.update(paymentMethod);
        } catch (RuntimeException e) {
            logger.error("Error al actualizar el metodo de pago {}: {}", paymentMethod, e);
            throw new DataException("Error al actualizar el metodo de pago con ID: " + paymentMethod.getId(), e);
        }
    }

    @Override
    public void delete(Session session, PaymentMethod paymentMethod) throws DataException {
        logger.debug("delete: {}", paymentMethod);
        try {
            session.delete(paymentMethod);
        } catch (RuntimeException e) {
            logger.error("Error al eliminar el metodo de pago {}: {}", paymentMethod, e);
            throw new DataException("Error al eliminar el metodo de pago con ID: " + paymentMethod.getId(), e);
        }
    }
}