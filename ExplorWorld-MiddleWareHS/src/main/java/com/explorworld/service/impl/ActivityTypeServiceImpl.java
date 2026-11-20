package com.explorworld.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.explorworld.dao.ActivityTypeDAO;
import com.explorworld.model.ActivityType;
import com.explorworld.model.DataException;
import com.explorworld.service.ActivityTypeService;
import com.explorworld.service.ServiceException;

@Service
public class ActivityTypeServiceImpl implements ActivityTypeService {

    private static final Logger logger = LogManager.getLogger(ActivityTypeServiceImpl.class);

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private ActivityTypeDAO activityTypeDAO;

    @Override
    @Transactional(readOnly = true)
    public ActivityType findById(Long id) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            return activityTypeDAO.findById(session, id);
        } catch (DataException e) {
            logger.error("Error al buscar tipo de actividad ID {}: {}", id, e.getMessage(), e);
            throw new ServiceException("No se pudo recuperar el tipo de actividad.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityType> findAll() throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            return activityTypeDAO.findAll(session);
        } catch (DataException e) {
            logger.error("Error al listar tipos de actividad: {}", e.getMessage(), e);
            throw new ServiceException("No se pudo obtener la lista de tipos de actividad.", e);
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public ActivityType create(ActivityType activityType) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            ActivityType created = activityTypeDAO.create(session, activityType);
            logger.info("Tipo de actividad creado con ID: {}", created.getId());
            return created;
        } catch (DataException e) {
            logger.error("Error al crear tipo de actividad: {}", e.getMessage(), e);
            throw new ServiceException("No se pudo crear el tipo de actividad.", e);
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void update(ActivityType activityType) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            activityTypeDAO.update(session, activityType);
            logger.info("Tipo de actividad actualizado con ID: {}", activityType.getId());
        } catch (DataException e) {
            logger.error("Error al actualizar tipo de actividad ID {}: {}", activityType.getId(), e.getMessage(), e);
            throw new ServiceException("No se pudo actualizar el tipo de actividad.", e);
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void delete(Long id) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            ActivityType existing = activityTypeDAO.findById(session, id);
            if (existing == null) {
                throw new ServiceException("No existe ningún tipo de actividad con ID: " + id);
            }
            activityTypeDAO.delete(session, existing);
            logger.info("Tipo de actividad eliminado con ID: {}", id);
        } catch (DataException e) {
            logger.error("Error al eliminar tipo de actividad ID {}: {}", id, e.getMessage(), e);
            throw new ServiceException("No se pudo eliminar el tipo de actividad.", e);
        }
    }
}