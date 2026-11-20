package com.explorworld.dao;

import java.util.List;

import org.hibernate.Session;

import com.explorworld.model.DataException;
import com.explorworld.model.Locality;

public interface LocalityDAO {

	public Locality findById(Session session, Long id) throws DataException;
	
	public List<Locality> findByProvinceId(Session session, Long provinceId) throws DataException;
	
	public List<Locality> findByName(Session session, String name) throws DataException;
	
	public List<Locality> findAll(Session session) throws DataException;
}
