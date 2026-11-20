package com.explorworld.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.explorworld.dao.PaymentMethodDAO;
import com.explorworld.model.DataException;
import com.explorworld.model.PaymentMethod;
import com.explorworld.service.PaymentMethodService;
import com.explorworld.service.ServiceException;

@Service
public class PaymentMethodServiceImpl implements PaymentMethodService {

    private static final Logger logger = LogManager.getLogger(PaymentMethodServiceImpl.class);

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private PaymentMethodDAO paymentMethodDAO;

    @Override
    @Transactional(readOnly = true)
    public PaymentMethod findById(Long id) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            return paymentMethodDAO.findById(session, id);
        } catch (DataException e) {
            logger.error("Error al buscar método de pago ID {}: {}", id, e.getMessage(), e);
            throw new ServiceException("No se pudo recuperar el método de pago.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentMethod> findAll() throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            return paymentMethodDAO.findAll(session);
        } catch (DataException e) {
            logger.error("Error al listar métodos de pago: {}", e.getMessage(), e);
            throw new ServiceException("No se pudo obtener la lista de métodos de pago.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentMethod> findActive() throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            return paymentMethodDAO.findActive(session);
        } catch (DataException e) {
            logger.error("Error al listar métodos de pago activos: {}", e.getMessage(), e);
            throw new ServiceException("No se pudieron obtener los métodos de pago activos.", e);
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public PaymentMethod create(PaymentMethod paymentMethod) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            PaymentMethod created = paymentMethodDAO.create(session, paymentMethod);
            logger.info("Método de pago creado con ID: {}", created.getId());
            return created;
        } catch (DataException e) {
            logger.error("Error al crear método de pago: {}", e.getMessage(), e);
            throw new ServiceException("No se pudo crear el método de pago.", e);
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void update(PaymentMethod paymentMethod) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            paymentMethodDAO.update(session, paymentMethod);
            logger.info("Método de pago actualizado con ID: {}", paymentMethod.getId());
        } catch (DataException e) {
            logger.error("Error al actualizar método de pago ID {}: {}", paymentMethod.getId(), e.getMessage(), e);
            throw new ServiceException("No se pudo actualizar el método de pago.", e);
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void delete(Long id) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            PaymentMethod existing = paymentMethodDAO.findById(session, id);
            if (existing == null) {
                throw new ServiceException("No existe ningún método de pago con ID: " + id);
            }
            paymentMethodDAO.delete(session, existing);
            logger.info("Método de pago eliminado con ID: {}", id);
        } catch (DataException e) {
            logger.error("Error al eliminar método de pago ID {}: {}", id, e.getMessage(), e);
            throw new ServiceException("No se pudo eliminar el método de pago.", e);
        }
    }
}