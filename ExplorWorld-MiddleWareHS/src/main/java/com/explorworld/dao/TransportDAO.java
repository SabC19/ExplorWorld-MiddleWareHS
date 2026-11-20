package com.explorworld.dao;

import java.util.List;

import org.hibernate.Session;

import com.explorworld.model.DataException;
import com.explorworld.model.Transport;

public interface TransportDAO {

	public Transport findById(Session session, Long id) throws DataException;

	public List<Transport> findByTransportTypeId(Session session, Long transportTypeId) throws DataException;
	
	public List<Transport> findAll(Session session) throws DataException;

	public Transport create(Session session, Transport transport) throws DataException;

	public void update(Session session, Transport transport) throws DataException;

	public void delete(Session session, Transport transport) throws DataException;
}
