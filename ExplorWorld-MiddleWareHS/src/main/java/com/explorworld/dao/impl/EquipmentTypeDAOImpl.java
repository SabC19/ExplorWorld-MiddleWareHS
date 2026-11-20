package com.explorworld.dao.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.explorworld.dao.EquipmentTypeDAO;
import com.explorworld.model.DataException;
import com.explorworld.model.EquipmentType;

@Repository
public class EquipmentTypeDAOImpl implements EquipmentTypeDAO {
	
	private static final Logger logger = LogManager.getLogger(EquipmentTypeDAOImpl.class);
	
	@Override
	public EquipmentType findById(Session session, Long id) throws DataException {
		logger.debug("findById: " + id);
		try {
			return session.get(EquipmentType.class, id);
		} catch (RuntimeException e) {
			logger.error("Error al buscar el tipo de Equipamiento con id: " + id, e);
			throw new DataException("Error al buscar el Tipo de Equipamiento con id: " + id, e);
		}
	}
	
	@Override
	public List<EquipmentType> findByModel(Session session, String model) throws DataException {
		logger.debug("findByModel: {}", model);
		try {
			String hql = "FROM EquipmentType et WHERE UPPER(et.model) LIKE :model ORDER BY et.model ASC";
			Query<EquipmentType> query = session.createQuery(hql, EquipmentType.class);
			query.setParameter("model", "%" + model.toUpperCase() + "%");
			return query.getResultList();
		} catch (RuntimeException e) {
			logger.error("Error al buscar tipos de equipamiento por modelo {}: {}", model, e);
			throw new DataException("Error al buscar EquipmentType por modelo", e);
		}
	}

	@Override
	public List<EquipmentType> findAll(Session session) throws DataException {
		logger.debug("findAll");
		try {
			String hql = "FROM EquipmentType et ORDER BY et.model ASC";
			Query<EquipmentType> query = session.createQuery(hql, EquipmentType.class);
			return query.getResultList();
		} catch (RuntimeException e) {
			logger.error("Error al buscar todos los modelos de Tipos de Equipamientos", e);
			throw new DataException("Error al buscar todos los generos", e);
		}
	}
	
	@Override
	public EquipmentType create(Session session, EquipmentType equipmentType) throws DataException {
		logger.debug("create: {}", equipmentType);
		try {
			session.save(equipmentType);
		return equipmentType;
		} catch (RuntimeException e) {
			logger.error("Error al crear el tipo de equipamiento: " + equipmentType, e);
			throw new DataException("Erro al crear el tipo de equipamiento" + equipmentType, e);
		}
	}
	
	@Override
	public void update(Session session, EquipmentType equipmentType) throws DataException {
		logger.debug("update: {}", equipmentType);
		try {
			session.update(equipmentType);
		} catch (RuntimeException e) {
			logger.error("Error al actualizar el tipo de equipamiento: " + equipmentType, e);
			throw new DataException("Error al actualizar el tipo de equipamiento: " + equipmentType, e);
		}
	}
	
	@Override
	public void delete(Session session, EquipmentType equipmentType) throws DataException {
		logger.debug("delete: {}", equipmentType);
		try {
			session.delete(equipmentType);
		} catch (RuntimeException e) {
			logger.error("Error al eliminar el tipo de equipamiento: " + equipmentType, e);
			throw new DataException("Error al eliminar el tipo de equipamiento: " + equipmentType, e);
		}
	}
	
}
