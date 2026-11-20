package com.explorworld.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.explorworld.dao.TransportDAO;
import com.explorworld.model.DataException;
import com.explorworld.model.Transport;
import com.explorworld.service.ServiceException;
import com.explorworld.service.TransportService;

@Service
public class TransportServiceImpl implements TransportService {

    private static final Logger logger = LogManager.getLogger(TransportServiceImpl.class);

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private TransportDAO transportDAO;

    @Override
    @Transactional(readOnly = true)
    public Transport findById(Long id) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            return transportDAO.findById(session, id);
        } catch (DataException e) {
            logger.error("Error al buscar transporte ID {}: {}", id, e.getMessage(), e);
            throw new ServiceException("No se pudo recuperar el transporte.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Transport> findAll() throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            return transportDAO.findAll(session);
        } catch (DataException e) {
            logger.error("Error al listar transportes: {}", e.getMessage(), e);
            throw new ServiceException("No se pudo obtener la lista de transportes.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Transport> findByTransportTypeId(Long transportTypeId) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            return transportDAO.findByTransportTypeId(session, transportTypeId);
        } catch (DataException e) {
            logger.error("Error al buscar transportes del tipo ID {}: {}", transportTypeId, e.getMessage(), e);
            throw new ServiceException("No se pudieron obtener los transportes por tipo.", e);
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public Transport create(Transport transport) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            Transport created = transportDAO.create(session, transport);
            logger.info("Transporte creado con ID: {}", created.getId());
            return created;
        } catch (DataException e) {
            logger.error("Error al crear transporte: {}", e.getMessage(), e);
            throw new ServiceException("No se pudo crear el transporte.", e);
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void update(Transport transport) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            transportDAO.update(session, transport);
            logger.info("Transporte actualizado con ID: {}", transport.getId());
        } catch (DataException e) {
            logger.error("Error al actualizar transporte ID {}: {}", transport.getId(), e.getMessage(), e);
            throw new ServiceException("No se pudo actualizar el transporte.", e);
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void delete(Long id) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            Transport existing = transportDAO.findById(session, id);
            if (existing == null) {
                throw new ServiceException("No existe ningún transporte con ID: " + id);
            }
            transportDAO.delete(session, existing);
            logger.info("Transporte eliminado con ID: {}", id);
        } catch (DataException e) {
            logger.error("Error al eliminar transporte ID {}: {}", id, e.getMessage(), e);
            throw new ServiceException("No se pudo eliminar el transporte.", e);
        }
    }
}