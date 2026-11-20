package com.explorworld.service;

import java.util.List;
import com.explorworld.model.Province;

public interface ProvinceService {

    public Province findById(Long id) throws ServiceException;

    public List<Province> findAll() throws ServiceException;

    public List<Province> findByName(String name) throws ServiceException;

}