package com.explorworld.dao;

import java.util.List;

import org.hibernate.Session;

import com.explorworld.model.ActivityType;
import com.explorworld.model.DataException;

public interface ActivityTypeDAO {

	public ActivityType findById(Session session, Long id) throws DataException;
	
	public List<ActivityType> findAll(Session session) throws DataException;
	
	public ActivityType create(Session session, ActivityType activityType) throws DataException;
	
	public void update(Session session, ActivityType activityType) throws DataException;
	
	public void delete(Session session, ActivityType activityType) throws DataException;
}
