package com.explorworld.dao.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.explorworld.dao.TransportTypeDAO;
import com.explorworld.model.DataException;
import com.explorworld.model.TransportType;

@Repository
public class TransportTypeDAOImpl implements TransportTypeDAO {

	private static final Logger logger = LogManager.getLogger(TransportTypeDAOImpl.class);
	
	@Override
	public TransportType findById(Session session, Long id) throws DataException {
		logger.debug("findById: " + id);
		try {
			return session.get(TransportType.class, id);
		} catch (RuntimeException e) {
			logger.error("Error al buscar el tipo de transporte con id: " + id, e);
			throw new DataException("Error al buscar el tipo de transporte con id: " + id, e);
		}
	}
	
	@Override
	public List<TransportType> findAll(Session session) throws DataException {
		logger.debug("findAll");
		try {
			String hql = "FROM TransportType t ORDER BY t.name ASC";
			Query<TransportType> query = session.createQuery(hql, TransportType.class);
			return query.getResultList();
		} catch (RuntimeException e) {
			logger.error("Error al buscar todos los tipos de transporte", e);
			throw new DataException("Error al buscar todos los tipos de transporte", e);
		}
	}
	
	@Override
	public TransportType create(Session session, TransportType transportType) throws DataException {
		logger.debug("create: {}", transportType);
		try {
			session.save(transportType);
			return transportType;
		} catch (RuntimeException e) {
			logger.error("Error al crear el tipo de transporte: " + transportType, e);
			throw new DataException("Error al crear el tipo de transporte: " + transportType, e);
		}
	}
	
	@Override
	public void update(Session session, TransportType transportType) throws DataException {
		logger.debug("update: {}", transportType);
		try {
			session.update(transportType);
		} catch (RuntimeException e) {
			logger.error("Error al actualizar el tipo de transporte: " + transportType, e);
			throw new DataException("Error al actualizar el tipo de transporte: " + transportType, e);
		}
	}
	
	@Override
	public void delete(Session session, TransportType transportType) throws DataException {
		logger.debug("delete: {}", transportType);
		try {
			session.delete(transportType);
		} catch (RuntimeException e) {
			logger.error("Error al eliminar el tipo de transporte: " + transportType, e);
			throw new DataException("Error al eliminar el tipo de transporte: " + transportType, e);
		}
	}
}
