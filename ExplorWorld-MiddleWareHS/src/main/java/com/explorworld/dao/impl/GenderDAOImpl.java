package com.explorworld.dao.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.explorworld.dao.GenderDAO;
import com.explorworld.model.DataException;
import com.explorworld.model.Gender;

@Repository
public class GenderDAOImpl implements GenderDAO {

	private static final Logger logger = LogManager.getLogger(GenderDAOImpl.class);
	
	@Override
	// Hibernate hacer el select con los parametros automaticamente
	public Gender findById(Session session, Long id) throws DataException {
		logger.debug("findById: " + id);
		try {
			return session.get(Gender.class, id);
		} catch (RuntimeException e) {
			logger.error("Error al buscar el genero con id: " + id, e);
			throw new DataException("Error al buscar el genero con id: " + id, e);
		}
	}
	
	@Override
	public List<Gender> findAll(Session session) throws DataException{
		logger.debug("findAll");
		try {
			String hql = "FROM Gender g ORDER BY g.name ASC";
			Query<Gender> query = session.createQuery(hql, Gender.class);
			return query.getResultList();
		} catch (RuntimeException e) {
			logger.error("Error al buscar todos los generos", e);
			throw new DataException("Error al buscar todos los generos", e);
		}
	}
	
	@Override
	public Gender create(Session session, Gender gender) throws DataException {
		logger.debug("create: {}", gender);
		try {
			session.save(gender);
			return gender;
		} catch (RuntimeException e) {
			logger.error("Error al crear el genero: " + gender, e);
			throw new DataException("Error al crear el genero: " + gender, e);
		}
	}
	
	@Override
	public void update(Session session, Gender gender) throws DataException {
		logger.debug("update: {}", gender);
		try {
			session.update(gender);
		} catch (RuntimeException e) {
			logger.error("Error al actualizar el genero: " + gender, e);
			throw new DataException("Error al actualizar el genero: " + gender, e);
		}
	}
	
	@Override
	public void delete(Session session, Gender gender) throws DataException {
		logger.debug("delete: {}", gender);
		try {
			session.delete(gender);
		} catch (RuntimeException e) {
			logger.error("Error al eliminar el genero: " + gender, e);
			throw new DataException("Error al eliminar el genero: " + gender, e);
		}
	}
	
}
