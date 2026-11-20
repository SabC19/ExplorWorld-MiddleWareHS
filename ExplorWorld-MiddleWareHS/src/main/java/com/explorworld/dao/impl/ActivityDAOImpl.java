package com.explorworld.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.explorworld.dao.ActivityDAO;
import com.explorworld.model.Activity;
import com.explorworld.model.DataException;
import com.explorworld.model.Results;
import com.explorworld.model.criteria.ActivityCriteria;
import com.explorworld.utils.HQLUtils;

@Repository
public class ActivityDAOImpl implements ActivityDAO {

	private static final Logger logger = LogManager.getLogger(ActivityDAOImpl.class);
	
	@Override
	public Activity findById(Session session, Long id) throws DataException {
		logger.debug("findById: " + id);
		try {
			return session.get(Activity.class, id);
		} catch (RuntimeException e) {
			logger.error("Error al buscar la actividad con id: " + id, e);
			throw new DataException("Error al buscar la actividad con id: " + id, e);
		}
	}
	
	@Override
	public Results<Activity> findByCriteria(Session session, ActivityCriteria criteria, int startIndex, int count) throws DataException {
		logger.debug("findByCriteria - Criteria: {}, startIndex: {}, count: {}", criteria, startIndex, count);
		
		if (criteria == null) {
			criteria = new ActivityCriteria(); // Criteria vacia = sin filtros
		}
		
		Results<Activity> results = new Results<>();
		
		try {
			List<String> conditions = new ArrayList<>();
			Map<String, Object> params = new HashMap<>();
			
			// Filtrado dinamico mediante HQLUtils
			HQLUtils.addClause(criteria.getId(), conditions, "a.id = :id", params, "id", criteria.getId());
			if (criteria.getId() == null) {
				HQLUtils.addClause(criteria.getName(), conditions, "UPPER(a.name) LIKE :name", 
						params, "name", HQLUtils.like(criteria.getName()));
			
				HQLUtils.addClause(criteria.getPriceMin(), conditions, "a.priceMin >= :priceMin", 
						params, "priceMin", criteria.getPriceMin());
				
				HQLUtils.addClause(criteria.getPriceMax(), conditions, "a.priceMax <= :priceMax",
						params, "priceMax", criteria.getPriceMax());
				
				HQLUtils.addClause(criteria.getStartDate(), conditions, "a.startDate >= :startDate", 
						params, "startDate", criteria.getStartDate());
				
				HQLUtils.addClause(criteria.getEndDate(), conditions, "a.endDate <= :endDate", 
						params, "endDate", criteria.getEndDate());
				
				HQLUtils.addClause(criteria.getCapacity(), conditions, "a.capacity >= :capacity", 
						params, "capacity", criteria.getCapacity());
				
				HQLUtils.addClause(criteria.getPriceMin(), conditions, "a.priceMin >= :priceMin", 
						params, "priceMin", criteria.getPriceMin());
				
				HQLUtils.addClause(criteria.getPriceMax(), conditions, "a.priceMax <= :priceMax", 
						params, "priceMax", criteria.getPriceMax());
				
				HQLUtils.addClause(criteria.getActivityTypeId(), conditions, "a.activityType.id = :activityTypeId",
	                    params, "activityTypeId", criteria.getActivityTypeId());

	            HQLUtils.addClause(criteria.getStartPlaceId(), conditions, "a.startPlace.id = :startPlaceId",
	                    params, "startPlaceId", criteria.getStartPlaceId());

	            HQLUtils.addClause(criteria.getEndPlaceId(), conditions, "a.endPlace.id = :endPlaceId",
	                    params, "endPlaceId", criteria.getEndPlaceId());
				
			}
			
			// 2. Montar el WHERE con las condiciones que se activaron
			String where = conditions.isEmpty() ? "" : " WHERE " + String.join(" AND ", conditions);

			// 3. Query de datos (con orden) y query de conteo (sin orden)
			String hqlData = "SELECT a FROM Activity a" + where + " ORDER BY a.startDate DESC, a.id DESC";
			String hqlCount = "SELECT COUNT(a.id) FROM Activity a" + where;

			logger.debug("HQL datos: {} | params: {}", hqlData, params);

			Query<Activity> dataQuery = session.createQuery(hqlData, Activity.class);
			Query<Long> countQuery = session.createQuery(hqlCount, Long.class);

			// 4. Rellenar los parámetros en las dos queries
			for (Map.Entry<String, Object> entry : params.entrySet()) {
			dataQuery.setParameter(entry.getKey(), entry.getValue());
			countQuery.setParameter(entry.getKey(), entry.getValue());
			}

			// 5. Paginación
			int offset = Math.max(startIndex, 0);
			dataQuery.setFirstResult(offset);
			dataQuery.setMaxResults(count);

			List<Activity> page = dataQuery.getResultList();
			Long total = countQuery.uniqueResult();

			// 6. Rellenar el contenedor Results
			results.setPage(page);
			results.setStartIndex(offset);
			results.setTotal(total != null ? total : 0L);

			return results;
		} catch (RuntimeException e) {
			logger.error("Error en findByCriteria de Activity", e);
			throw new DataException("Error al buscar actividades con criterios: ", e);
		}
	}
	
	@Override
	public Activity create(Session session, Activity activity) throws DataException {
		logger.debug("create: {}", activity);
		try {
			session.save(activity);
			return activity;
		} catch (RuntimeException e) {
			logger.error("Error al crear la actividad {}: {}", activity, e);
			throw new DataException("Error al crear la actividad: " + activity, e);
		}
	}
	
	@Override
	public void update(Session session, Activity activity) throws DataException {
		logger.debug("update: {}", activity);
		try {
			session.update(activity);
		} catch (RuntimeException e) {
			logger.error("Error al actualizar la actividad {}: {}", activity, e);
			throw new DataException("Error al actualizar la actividad con ID: "+ activity.getId() 
			+ activity, e);
		}
	}
	
	@Override
	public void delete(Session session, Activity activity) throws DataException {
		logger.debug("delete: {}", activity);
		try {
			session.delete(activity);
		} catch (RuntimeException e) {
			logger.error("Error al eliminar la actividad {}: {}", activity, e);
			throw new DataException("Error al eliminar la actividad con ID: "+ activity.getId() 
			+ activity, e);
		}
	}
	
	
}
