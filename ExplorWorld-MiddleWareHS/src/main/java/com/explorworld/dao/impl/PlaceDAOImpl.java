package com.explorworld.dao.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.explorworld.dao.PlaceDAO;
import com.explorworld.model.DataException;
import com.explorworld.model.Place;

@Repository
public class PlaceDAOImpl implements PlaceDAO {

    private static final Logger logger = LogManager.getLogger(PlaceDAOImpl.class);

    @Override
    public Place findById(Session session, Long id) throws DataException {
        logger.debug("findById: {}", id);
        try {
            return session.get(Place.class, id);
        } catch (RuntimeException e) {
            logger.error("Error al buscar el lugar con id {}: {}", id, e);
            throw new DataException("Error al buscar el lugar con id: " + id, e);
        }
    }

    @Override
    public List<Place> findAll(Session session) throws DataException {
        logger.debug("findAll");
        try {
            String hql = "FROM Place p ORDER BY p.name ASC";
            Query<Place> query = session.createQuery(hql, Place.class);
            return query.getResultList();
        } catch (RuntimeException e) {
            logger.error("Error al listar todos los lugares: {}", e);
            throw new DataException("Error al listar todos los lugares", e);
        }
    }

    @Override
    public List<Place> findByName(Session session, String name) throws DataException {
        logger.debug("findByName: {}", name);
        try {
            String hql = "FROM Place p WHERE UPPER(p.name) LIKE :name ORDER BY p.name ASC";
            Query<Place> query = session.createQuery(hql, Place.class);
            query.setParameter("name", "%" + name.trim().toUpperCase() + "%");
            return query.getResultList();
        } catch (RuntimeException e) {
            logger.error("Error al buscar lugares por nombre {}: {}", name, e);
            throw new DataException("Error al buscar lugares por nombre: " + name, e);
        }
    }

    @Override
    public List<Place> findByLocalityId(Session session, Long localityId) throws DataException {
        logger.debug("findByLocalityId: {}", localityId);
        try {
            String hql = "FROM Place p WHERE p.locality.id = :localityId ORDER BY p.name ASC";
            Query<Place> query = session.createQuery(hql, Place.class);
            query.setParameter("localityId", localityId);
            return query.getResultList();
        } catch (RuntimeException e) {
            logger.error("Error al buscar lugares de la localidad ID {}: {}", localityId, e);
            throw new DataException("Error al buscar lugares por localidad ID: " + localityId, e);
        }
    }

    @Override
    public Place create(Session session, Place place) throws DataException {
        logger.debug("create: {}", place);
        try {
            session.save(place);
            return place;
        } catch (RuntimeException e) {
            logger.error("Error al crear el lugar {}: {}", place, e);
            throw new DataException("Error al crear el lugar: " + place, e);
        }
    }

    @Override
    public void update(Session session, Place place) throws DataException {
        logger.debug("update: {}", place);
        try {
            session.update(place);
        } catch (RuntimeException e) {
            logger.error("Error al actualizar el lugar {}: {}", place, e);
            throw new DataException("Error al actualizar el lugar con ID: " + place.getId(), e);
        }
    }

    @Override
    public void delete(Session session, Place place) throws DataException {
        logger.debug("delete: {}", place);
        try {
            session.delete(place);
        } catch (RuntimeException e) {
            logger.error("Error al eliminar el lugar {}: {}", place, e);
            throw new DataException("Error al eliminar el lugar con ID: " + place.getId(), e);
        }
    }
}