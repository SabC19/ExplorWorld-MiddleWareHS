package com.explorworld.dao.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.explorworld.dao.LocalityDAO;
import com.explorworld.model.DataException;
import com.explorworld.model.Locality;

@Repository
public class LocalityDAOImpl implements LocalityDAO {

private static final Logger logger = LogManager.getLogger(LocalityDAOImpl.class);
	
	@Override
	public Locality findById(Session session, Long id) throws DataException {
		logger.debug("findById: " + id);
		try {
			return session.get(Locality.class, id);
		} catch (RuntimeException e) {
			logger.error("Error al buscar la Localidades con id: " + id, e);
			throw new DataException("Error al buscar la Localidades con id: " + id, e);
		}
	}
	
	@Override
	public List<Locality> findByProvinceId(Session session, Long provinceId) throws DataException {
		logger.debug("findByProvinceId: {}", provinceId);
		try {
			String hql = "FROM Locality l WHERE l.province.id = :provinceId ORDER BY l.name ASC";
			Query<Locality> query = session.createQuery(hql, Locality.class);
	        query.setParameter("provinceId", provinceId);
	        return query.getResultList();
	    } catch (RuntimeException e) {
	        logger.error("Error al buscar localidades de la provincia ID {}: {}", provinceId, e.getMessage(), e);
	        throw new DataException("Error al buscar localidades por provincia ID: " + provinceId, e);
		}
		
	}
	
	@Override
	public List<Locality> findByName(Session session, String name) throws DataException {
		logger.debug("findByName: {}", name);
		try {
			String hql = "FROM Locality l WHERE UPPER(l.name) LIKE :name ORDER BY l.name ASC";
			Query<Locality> query = session.createQuery(hql, Locality.class);
			query.setParameter("name", "%" + name.toUpperCase() + "%");
			return query.getResultList();
		} catch (RuntimeException e) {
			logger.error("Error al buscar las localidades por nombre {}: {}", name, e);
			throw new DataException("Erro la busca las localidades por nombre {}: {}", e);
		}
	}
	
	
	@Override
	public List<Locality> findAll(Session session) throws DataException{
		logger.debug("findAll");
		try {
			String hql = "FROM Locality l ORDER BY l.name ASC";
			Query<Locality> query = session.createQuery(hql, Locality.class);
			return query.getResultList();
		} catch (RuntimeException e) {
			logger.error("Error al buscar todas las localidades", e);
			throw new DataException("Error al buscar todas las localidades", e);
		}
	}
	
}
