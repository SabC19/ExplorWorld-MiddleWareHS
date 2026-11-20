package com.explorworld.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.explorworld.dao.PaymentDAO;
import com.explorworld.dao.PaymentMethodDAO;
import com.explorworld.dao.TicketDAO;
import com.explorworld.model.DataException;
import com.explorworld.model.Payment;
import com.explorworld.model.PaymentMethod;
import com.explorworld.model.Results;
import com.explorworld.model.Ticket;
import com.explorworld.model.criteria.PaymentCriteria;
import com.explorworld.service.PaymentService;
import com.explorworld.service.ServiceException;

@Service
public class PaymentServiceImpl implements PaymentService {

    private static final Logger logger = LogManager.getLogger(PaymentServiceImpl.class);

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private PaymentDAO paymentDAO;

    @Autowired
    private TicketDAO ticketDAO;

    @Autowired
    private PaymentMethodDAO paymentMethodDAO;

    @Override
    @Transactional(readOnly = true)
    public Payment findById(Long id) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            return paymentDAO.findById(session, id);
        } catch (DataException e) {
            logger.error("Error al buscar pago ID {}: {}", id, e.getMessage(), e);
            throw new ServiceException("No se pudo recuperar el pago.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Payment> findByTicketId(Long ticketId) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            return paymentDAO.findByTicketId(session, ticketId);
        } catch (DataException e) {
            logger.error("Error al listar pagos del ticket ID {}: {}", ticketId, e.getMessage(), e);
            throw new ServiceException("No se pudieron recuperar los pagos del ticket.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Results<Payment> findByCriteria(PaymentCriteria criteria, int startIndex, int count) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            return paymentDAO.findByCriteria(session, criteria, startIndex, count);
        } catch (DataException e) {
            logger.error("Error en búsqueda paginada de pagos: {}", e.getMessage(), e);
            throw new ServiceException("Error al buscar pagos.", e);
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public Payment create(Payment payment) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();

            Ticket ticket = ticketDAO.findById(session, payment.getTicket().getId());
            if (ticket == null) {
                throw new ServiceException("El ticket asociado al pago no existe.");
            }

            PaymentMethod method = paymentMethodDAO.findById(session, payment.getPaymentMethod().getId());
            if (method == null) {
                throw new ServiceException("El método de pago indicado no existe.");
            }
            if (Boolean.FALSE.equals(method.getActive())) {
                throw new ServiceException("El método de pago seleccionado no está activo.");
            }

            payment.setTicket(ticket);
            payment.setPaymentMethod(method);

            if (payment.getPaymentDate() == null) {
                payment.setPaymentDate(new Date());
            }
            if (payment.getStatus() == null || payment.getStatus().trim().isEmpty()) {
                payment.setStatus("COMPLETED");
            }

            Payment created = paymentDAO.create(session, payment);

            // Confirmar el ticket en la misma transacción
            if ("COMPLETED".equalsIgnoreCase(created.getStatus())) {
                ticket.setStatus("CONFIRMED");
                ticketDAO.update(session, ticket);
            }

            logger.info("Pago creado con ID: {} para el ticket {}", created.getId(), ticket.getTicketNumber());
            return created;
        } catch (DataException e) {
            logger.error("Error al crear pago: {}", e.getMessage(), e);
            throw new ServiceException("No se pudo procesar el pago.", e);
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void update(Payment payment) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            paymentDAO.update(session, payment);
            logger.info("Pago actualizado con ID: {}", payment.getId());
        } catch (DataException e) {
            logger.error("Error al actualizar pago ID {}: {}", payment.getId(), e.getMessage(), e);
            throw new ServiceException("No se pudo actualizar el pago.", e);
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void delete(Long id) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            Payment existing = paymentDAO.findById(session, id);
            if (existing == null) {
                throw new ServiceException("No existe ningún pago con ID: " + id);
            }
            paymentDAO.delete(session, existing);
            logger.info("Pago eliminado con ID: {}", id);
        } catch (DataException e) {
            logger.error("Error al eliminar pago ID {}: {}", id, e.getMessage(), e);
            throw new ServiceException("No se pudo eliminar el pago.", e);
        }
    }
}