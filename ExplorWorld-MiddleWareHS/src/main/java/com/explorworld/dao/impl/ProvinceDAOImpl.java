package com.explorworld.dao.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.explorworld.dao.ProvinceDAO;
import com.explorworld.model.DataException;
import com.explorworld.model.Province;

@Repository
public class ProvinceDAOImpl implements ProvinceDAO {

	private static final Logger logger = LogManager.getLogger(ProvinceDAOImpl.class);
	
	@Override
	public Province findById(Session session, Long id) throws DataException {
		logger.debug("findById: " + id);
		try {
			return session.get(Province.class, id);
		} catch (RuntimeException e) {
			logger.error("Error al buscar la provincia con id: " + id, e);
			throw new DataException("Error al buscar la provincia con id: " + id, e);
		}
	}
	
	@Override
	public List<Province> findByName(Session session, String name) throws DataException {
		logger.debug("findByName: {}", name);
		try {
			String hql = "FROM Province p WHERE UPPER(p.name) LIKE :name ORDER BY p.name ASC";
			Query<Province> query = session.createQuery(hql, Province.class);
			query.setParameter("name", "%" + name.toUpperCase() + "%");
			return query.getResultList();
		} catch (RuntimeException e) {
			logger.error("Error al buscar las provincias por nombre {}: {}", name, e);
			throw new DataException("Erro la busca las provincias por nombre {}: {}", e);
		}
	}
	
	
	@Override
	public List<Province> findAll(Session session) throws DataException{
		logger.debug("findAll");
		try {
			String hql = "FROM Province p ORDER BY p.name ASC";
			Query<Province> query = session.createQuery(hql, Province.class);
			return query.getResultList();
		} catch (RuntimeException e) {
			logger.error("Error al buscar todas las provincias", e);
			throw new DataException("Error al buscar todas las provincias", e);
		}
	}

	
}
