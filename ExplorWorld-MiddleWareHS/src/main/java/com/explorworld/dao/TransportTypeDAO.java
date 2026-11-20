package com.explorworld.dao;

import java.util.List;

import org.hibernate.Session;

import com.explorworld.model.DataException;
import com.explorworld.model.TransportType;

public interface TransportTypeDAO {
	
	public TransportType findById(Session session, Long id) throws DataException;

	public List<TransportType> findAll(Session session) throws DataException;

	public TransportType create(Session session, TransportType transportType) throws DataException;

	public void update(Session session, TransportType transportType) throws DataException;

	public void delete(Session session, TransportType transportType) throws DataException;

}
