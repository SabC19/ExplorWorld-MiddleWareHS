package com.explorworld.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.explorworld.dao.GenderDAO;
import com.explorworld.model.DataException;
import com.explorworld.model.Gender;
import com.explorworld.service.GenderService;
import com.explorworld.service.ServiceException;

@Service
public class GenderServiceImpl implements GenderService {

    private static final Logger logger = LogManager.getLogger(GenderServiceImpl.class);

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private GenderDAO genderDAO;

    @Override
    @Transactional(readOnly = true)
    public Gender findById(Long id) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            return genderDAO.findById(session, id);
        } catch (DataException e) {
            logger.error("Error al buscar género ID {}: {}", id, e.getMessage(), e);
            throw new ServiceException("No se pudo recuperar el género.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Gender> findAll() throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            return genderDAO.findAll(session);
        } catch (DataException e) {
            logger.error("Error al listar géneros: {}", e.getMessage(), e);
            throw new ServiceException("No se pudo obtener la lista de géneros.", e);
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public Gender create(Gender gender) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            Gender created = genderDAO.create(session, gender);
            logger.info("Género creado con ID: {}", created.getId());
            return created;
        } catch (DataException e) {
            logger.error("Error al crear género: {}", e.getMessage(), e);
            throw new ServiceException("No se pudo crear el género.", e);
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void update(Gender gender) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            genderDAO.update(session, gender);
            logger.info("Género actualizado con ID: {}", gender.getId());
        } catch (DataException e) {
            logger.error("Error al actualizar género ID {}: {}", gender.getId(), e.getMessage(), e);
            throw new ServiceException("No se pudo actualizar el género.", e);
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void delete(Long id) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            Gender existing = genderDAO.findById(session, id);
            if (existing == null) {
                throw new ServiceException("No existe ningún género con ID: " + id);
            }
            genderDAO.delete(session, existing);
            logger.info("Género eliminado con ID: {}", id);
        } catch (DataException e) {
            logger.error("Error al eliminar género ID {}: {}", id, e.getMessage(), e);
            throw new ServiceException("No se pudo eliminar el género.", e);
        }
    }
}