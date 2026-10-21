package com.explorworld.dao;

import java.util.List;

import org.hibernate.Session;

import com.explorworld.model.Province;

public interface ProvinceDAO {

	public Province findById(Session session, Long id);
	
	public List<Province> findAll(Session session);
}
