package com.explorworld.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.explorworld.dao.PlaceDAO;
import com.explorworld.model.DataException;
import com.explorworld.model.Place;
import com.explorworld.service.PlaceService;
import com.explorworld.service.ServiceException;

@Service
public class PlaceServiceImpl implements PlaceService {

    private static final Logger logger = LogManager.getLogger(PlaceServiceImpl.class);

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private PlaceDAO placeDAO;

    @Override
    @Transactional(readOnly = true)
    public Place findById(Long id) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            return placeDAO.findById(session, id);
        } catch (DataException e) {
            logger.error("Error al buscar lugar ID {}: {}", id, e.getMessage(), e);
            throw new ServiceException("No se pudo recuperar el lugar.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Place> findAll() throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            return placeDAO.findAll(session);
        } catch (DataException e) {
            logger.error("Error al listar lugares: {}", e.getMessage(), e);
            throw new ServiceException("No se pudo obtener la lista de lugares.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Place> findByName(String name) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            return placeDAO.findByName(session, name);
        } catch (DataException e) {
            logger.error("Error al buscar lugares por nombre {}: {}", name, e.getMessage(), e);
            throw new ServiceException("No se pudieron buscar lugares por nombre.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Place> findByLocalityId(Long localityId) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            return placeDAO.findByLocalityId(session, localityId);
        } catch (DataException e) {
            logger.error("Error al buscar lugares de la localidad ID {}: {}", localityId, e.getMessage(), e);
            throw new ServiceException("No se pudieron obtener los lugares de la localidad.", e);
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public Place create(Place place) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            Place created = placeDAO.create(session, place);
            logger.info("Lugar creado con ID: {}", created.getId());
            return created;
        } catch (DataException e) {
            logger.error("Error al crear lugar: {}", e.getMessage(), e);
            throw new ServiceException("No se pudo crear el lugar.", e);
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void update(Place place) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            placeDAO.update(session, place);
            logger.info("Lugar actualizado con ID: {}", place.getId());
        } catch (DataException e) {
            logger.error("Error al actualizar lugar ID {}: {}", place.getId(), e.getMessage(), e);
            throw new ServiceException("No se pudo actualizar el lugar.", e);
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void delete(Long id) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            Place existing = placeDAO.findById(session, id);
            if (existing == null) {
                throw new ServiceException("No existe ningún lugar con ID: " + id);
            }
            placeDAO.delete(session, existing);
            logger.info("Lugar eliminado con ID: {}", id);
        } catch (DataException e) {
            logger.error("Error al eliminar lugar ID {}: {}", id, e.getMessage(), e);
            throw new ServiceException("No se pudo eliminar el lugar.", e);
        }
    }
}