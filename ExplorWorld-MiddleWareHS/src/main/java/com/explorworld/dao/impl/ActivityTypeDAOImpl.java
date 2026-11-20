package com.explorworld.dao.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.explorworld.dao.ActivityTypeDAO;
import com.explorworld.model.ActivityType;
import com.explorworld.model.DataException;

@Repository
public class ActivityTypeDAOImpl implements ActivityTypeDAO {

	private static final Logger logger = LogManager.getLogger(ActivityTypeDAOImpl.class);
	
	@Override
	public ActivityType findById(Session session, Long id) throws DataException {
		logger.debug("findById: " + id);
		try {
			return session.get(ActivityType.class, id);
		} catch (RuntimeException e) {
			logger.error("Error al buscar el tipo de actividad con id: " + id, e);
			throw new DataException("Error al buscar el tipo de actividad con id: " + id, e);
		}
		
	}
		
		@Override
		public List<ActivityType> findAll(Session session) throws DataException {
			logger.debug("findAll");
			try {
				String hql = "FROM ActivityType at ORDER BY at.name ASC";
				Query<ActivityType> query = session.createQuery(hql, ActivityType.class);
				return query.getResultList();
			} catch (RuntimeException e) {
				logger.error("Error al buscar todos los tipos de actividad", e);
				throw new DataException("Error al buscar todos los tipos de actividad", e);
			}
		}
		
		@Override
		public ActivityType create(Session session, ActivityType activityType) throws DataException {
			logger.debug("create: {}", activityType);
			try {
				session.save(activityType);
				return activityType;
			} catch (RuntimeException e) {
				logger.error("Error al crear el tipo de actividad: " + activityType, e);
				throw new DataException("Error al crear el tipo de actividad: " + activityType, e);
			}
		}
		
		@Override
		public void update(Session session, ActivityType activityType) throws DataException {
			logger.debug("update: {}", activityType);
			try {
				session.update(activityType);
			} catch (RuntimeException e) {
				logger.error("Error al actualizar el tipo de actividad: " + activityType, e);
				throw new DataException("Error al actualizar el tipo de actividad: " + activityType, e);
			}
		}
		
		@Override
		public void delete(Session session, ActivityType activityType) throws DataException {
			logger.debug("delete: {}", activityType);
			try {
				session.delete(activityType);
			} catch (RuntimeException e) {
				logger.error("Error al eliminar el tipo de actividad: " + activityType, e);
				throw new DataException("Error al eliminar el tipo de actividad: " + activityType, e);
			}
		}
}
