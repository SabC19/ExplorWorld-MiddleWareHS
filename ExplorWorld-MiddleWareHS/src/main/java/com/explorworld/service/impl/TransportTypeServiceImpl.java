package com.explorworld.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.explorworld.dao.TransportTypeDAO;
import com.explorworld.model.DataException;
import com.explorworld.model.TransportType;
import com.explorworld.service.ServiceException;
import com.explorworld.service.TransportTypeService;

@Service
public class TransportTypeServiceImpl implements TransportTypeService {

    private static final Logger logger = LogManager.getLogger(TransportTypeServiceImpl.class);

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private TransportTypeDAO transportTypeDAO;

    @Override
    @Transactional(readOnly = true)
    public TransportType findById(Long id) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            return transportTypeDAO.findById(session, id);
        } catch (DataException e) {
            logger.error("Error al buscar tipo de transporte ID {}: {}", id, e.getMessage(), e);
            throw new ServiceException("No se pudo recuperar el tipo de transporte.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransportType> findAll() throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            return transportTypeDAO.findAll(session);
        } catch (DataException e) {
            logger.error("Error al listar tipos de transporte: {}", e.getMessage(), e);
            throw new ServiceException("No se pudo obtener la lista de tipos de transporte.", e);
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public TransportType create(TransportType transportType) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            TransportType created = transportTypeDAO.create(session, transportType);
            logger.info("Tipo de transporte creado con ID: {}", created.getId());
            return created;
        } catch (DataException e) {
            logger.error("Error al crear tipo de transporte: {}", e.getMessage(), e);
            throw new ServiceException("No se pudo crear el tipo de transporte.", e);
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void update(TransportType transportType) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            transportTypeDAO.update(session, transportType);
            logger.info("Tipo de transporte actualizado con ID: {}", transportType.getId());
        } catch (DataException e) {
            logger.error("Error al actualizar tipo de transporte ID {}: {}", transportType.getId(), e.getMessage(), e);
            throw new ServiceException("No se pudo actualizar el tipo de transporte.", e);
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void delete(Long id) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            TransportType existing = transportTypeDAO.findById(session, id);
            if (existing == null) {
                throw new ServiceException("No existe ningún tipo de transporte con ID: " + id);
            }
            transportTypeDAO.delete(session, existing);
            logger.info("Tipo de transporte eliminado con ID: {}", id);
        } catch (DataException e) {
            logger.error("Error al eliminar tipo de transporte ID {}: {}", id, e.getMessage(), e);
            throw new ServiceException("No se pudo eliminar el tipo de transporte.", e);
        }
    }
}