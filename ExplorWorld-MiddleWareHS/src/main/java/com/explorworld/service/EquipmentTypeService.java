package com.explorworld.service;

import java.util.List;
import com.explorworld.model.EquipmentType;

public interface EquipmentTypeService {

    public EquipmentType findById(Long id) throws ServiceException;

    public List<EquipmentType> findAll() throws ServiceException;

    public List<EquipmentType> findByModel(String model) throws ServiceException;

    public EquipmentType create(EquipmentType equipmentType) throws ServiceException;

    public void update(EquipmentType equipmentType) throws ServiceException;

    public void delete(Long id) throws ServiceException;
}