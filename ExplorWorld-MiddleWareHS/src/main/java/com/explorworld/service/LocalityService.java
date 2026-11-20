package com.explorworld.service;

import java.util.List;
import com.explorworld.model.Locality;

public interface LocalityService {

    public Locality findById(Long id) throws ServiceException;

    public List<Locality> findAll() throws ServiceException;

    public List<Locality> findByName(String name) throws ServiceException;

    public List<Locality> findByProvinceId(Long provinceId) throws ServiceException;

}