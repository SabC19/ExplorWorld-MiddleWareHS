package com.explorworld.dao.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.explorworld.dao.TransportDAO;
import com.explorworld.model.DataException;
import com.explorworld.model.Transport;

@Repository
public class TransportDAOImpl implements TransportDAO {

	private static final Logger logger = LogManager.getLogger(TransportDAOImpl.class);
	
	@Override
	public Transport findById(Session session, Long id) throws DataException {
		logger.debug("findById: " + id);
		try {
			return session.get(Transport.class, id);
		} catch (RuntimeException e) {
			logger.error("Error al buscar el transporte con id: " + id, e);
			throw new DataException("Error al buscar el transporte con id: " + id, e);
		}
	}
	
	@Override
	public List<Transport> findByTransportTypeId(Session session, Long transportTypeId) throws DataException {
	    logger.debug("findByTransportTypeId: {}", transportTypeId);
	    try {
	        String hql = "FROM Transport t WHERE t.transportType.id = :transportTypeId ORDER BY t.plateNumber ASC";
	        Query<Transport> query = session.createQuery(hql, Transport.class);
	        query.setParameter("transportTypeId", transportTypeId);
	        return query.getResultList();
	    } catch (RuntimeException e) {
	        logger.error("Error al buscar transportes del tipo ID {}: {}", transportTypeId, e);
	        throw new DataException("Error al buscar transportes por tipo ID: " + transportTypeId, e);
	    }
	}
	
	@Override
	public List<Transport> findAll(Session session) throws DataException {
	    logger.debug("findAll");
	    try {
	        String hql = "FROM Transport t ORDER BY t.plateNumber ASC";
	        Query<Transport> query = session.createQuery(hql, Transport.class);
	        return query.getResultList();
	    } catch (RuntimeException e) {
	        logger.error("Error al buscar todos los transportes: {}", e.getMessage(), e);
	        throw new DataException("Error al buscar todos los transportes", e);
	    }
	}
	@Override
	public Transport create(Session session, Transport transport) throws DataException {
		logger.debug("create: {}", transport);
		try {
			session.save(transport);
			return transport;
		} catch (RuntimeException e) {
			logger.error("Error al crear el transporte: " + transport, e);
			throw new DataException("Error al crear el transporte: " + transport, e);
		}
	}
	
	@Override
	public void update(Session session, Transport transport) throws DataException {
		logger.debug("update: {}", transport);
		try {
			session.update(transport);
		} catch (RuntimeException e) {
			logger.error("Error al actualizar el transporte: " + transport, e);
			throw new DataException("Error al actualizar el transporte: " + transport, e);
		}
	}
	
	@Override
	public void delete(Session session, Transport transport) throws DataException {
		logger.debug("delete: {}", transport);
		try {
			session.delete(transport);
		} catch (RuntimeException e) {
			logger.error("Error al eliminar el transporte: " + transport, e);
			throw new DataException("Error al eliminar el transporte: " + transport, e);
		}
	}
	
}
