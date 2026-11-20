package com.explorworld.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.explorworld.dao.ActivityDAO;
import com.explorworld.model.Activity;
import com.explorworld.model.DataException;
import com.explorworld.model.Results;
import com.explorworld.model.criteria.ActivityCriteria;
import com.explorworld.service.ActivityService;
import com.explorworld.service.ServiceException;

@Service
public class ActivityServiceImpl implements ActivityService {

    private static final Logger logger = LogManager.getLogger(ActivityServiceImpl.class);

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private ActivityDAO activityDAO;

    @Override
    @Transactional(readOnly = true)
    public Activity findById(Long id) throws ServiceException {
        if (id == null) {
            throw new ServiceException("El ID de la actividad no puede ser nulo.");
        }
        try {
            Session session = sessionFactory.getCurrentSession();
            return activityDAO.findById(session, id);
        } catch (DataException e) {
            logger.error("Error de persistencia al buscar actividad ID {}: {}", id, e.getMessage(), e);
            throw new ServiceException("No se pudo recuperar la actividad.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Results<Activity> findByCriteria(ActivityCriteria criteria, int startIndex, int count) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            return activityDAO.findByCriteria(session, criteria, startIndex, count);
        } catch (DataException e) {
            logger.error("Error de persistencia en búsqueda de actividades: {}", e.getMessage(), e);
            throw new ServiceException("Error al buscar actividades.", e);
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public Activity create(Activity activity) throws ServiceException {
        validateActivity(activity);
        try {
            Session session = sessionFactory.getCurrentSession();
            Activity created = activityDAO.create(session, activity);
            logger.info("Actividad creada con ID: {}", created.getId());
            return created;
        } catch (DataException e) {
            logger.error("Error de persistencia al crear actividad {}: {}", activity.getName(), e.getMessage(), e);
            throw new ServiceException("No se pudo crear la actividad.", e);
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void update(Activity activity) throws ServiceException {
        if (activity == null || activity.getId() == null) {
            throw new ServiceException("La actividad y su ID son obligatorios para actualizar.");
        }
        validateActivity(activity);
        try {
            Session session = sessionFactory.getCurrentSession();
            activityDAO.update(session, activity);
            logger.info("Actividad actualizada con ID: {}", activity.getId());
        } catch (DataException e) {
            logger.error("Error de persistencia al actualizar actividad ID {}: {}", activity.getId(), e.getMessage(), e);
            throw new ServiceException("No se pudo actualizar la actividad.", e);
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void delete(Long id) throws ServiceException {
        if (id == null) {
            throw new ServiceException("El ID es obligatorio para eliminar la actividad.");
        }
        try {
            Session session = sessionFactory.getCurrentSession();
            Activity existing = activityDAO.findById(session, id);
            if (existing == null) {
                throw new ServiceException("No existe ninguna actividad con ID: " + id);
            }
            activityDAO.delete(session, existing);
            logger.info("Actividad eliminada con ID: {}", id);
        } catch (DataException e) {
            logger.error("Error de persistencia al eliminar actividad ID {}: {}", id, e.getMessage(), e);
            throw new ServiceException("No se pudo eliminar la actividad.", e);
        }
    }

    // Reglas de negocio centralizadas
    private void validateActivity(Activity a) throws ServiceException {
        if (a == null) {
            throw new ServiceException("La actividad no puede ser nula.");
        }
        if (a.getName() == null || a.getName().trim().isEmpty()) {
            throw new ServiceException("El nombre de la actividad es obligatorio.");
        }
        if (a.getCapacity() != null && a.getCapacity() <= 0) {
            throw new ServiceException("La capacidad debe ser mayor que cero.");
        }
        if (a.getPriceMin() != null && a.getPriceMax() != null && a.getPriceMin() > a.getPriceMax()) {
            throw new ServiceException("El precio mínimo no puede ser mayor que el precio máximo.");
        }
        if (a.getStartDate() != null && a.getEndDate() != null && a.getStartDate().after(a.getEndDate())) {
            throw new ServiceException("La fecha de inicio debe ser anterior a la fecha de fin.");
        }
    }
}