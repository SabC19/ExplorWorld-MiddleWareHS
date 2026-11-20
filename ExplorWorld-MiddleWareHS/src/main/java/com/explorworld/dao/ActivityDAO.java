package com.explorworld.dao;

import org.hibernate.Session;

import com.explorworld.model.Activity;
import com.explorworld.model.DataException;
import com.explorworld.model.Results;
import com.explorworld.model.criteria.ActivityCriteria;

public interface ActivityDAO {
	
    public Activity findById(Session session, Long id) throws DataException;

    public Results<Activity> findByCriteria(Session session, ActivityCriteria criteria, int startIndex, int count) throws DataException;

    public Activity create(Session session, Activity activity) throws DataException;

    public void update(Session session, Activity activity) throws DataException;

    public void delete(Session session, Activity activity) throws DataException;
}