package com.explorworld.service;

import java.util.List;
import com.explorworld.model.TransportType;

public interface TransportTypeService {

    public TransportType findById(Long id) throws ServiceException;

    public List<TransportType> findAll() throws ServiceException;

    public TransportType create(TransportType transportType) throws ServiceException;

    public void update(TransportType transportType) throws ServiceException;

    public void delete(Long id) throws ServiceException;
}