package com.explorworld.dao;

import java.util.List;

import org.hibernate.Session;

import com.explorworld.model.DataException;
import com.explorworld.model.Province;

public interface ProvinceDAO {

	public Province findById(Session session, Long id) throws DataException;
	
	public List<Province> findByName(Session session, String name) throws DataException;
	
	public List<Province> findAll(Session session) throws DataException;
}
