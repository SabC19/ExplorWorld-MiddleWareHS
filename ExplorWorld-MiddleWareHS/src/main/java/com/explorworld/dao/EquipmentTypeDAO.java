package com.explorworld.dao;

import java.util.List;

import org.hibernate.Session;

import com.explorworld.model.DataException;
import com.explorworld.model.EquipmentType;

public interface EquipmentTypeDAO {
	
	public EquipmentType findById(Session session, Long id) throws DataException;
	
	public List<EquipmentType> findByModel(Session session, String model) throws DataException;
	
	public List<EquipmentType> findAll(Session session) throws DataException;

    public EquipmentType create(Session session, EquipmentType equipmentType) throws DataException;

    public void update(Session session, EquipmentType equipmentType) throws DataException;

    public void delete(Session session, EquipmentType equipmentType) throws DataException;

}
