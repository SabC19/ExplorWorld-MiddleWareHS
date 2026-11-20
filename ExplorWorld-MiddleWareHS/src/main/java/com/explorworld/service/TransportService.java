package com.explorworld.service;

import java.util.List;
import com.explorworld.model.Transport;

public interface TransportService {

    public Transport findById(Long id) throws ServiceException;

    public List<Transport> findAll() throws ServiceException;

    public List<Transport> findByTransportTypeId(Long transportTypeId) throws ServiceException;

    public Transport create(Transport transport) throws ServiceException;

    public void update(Transport transport) throws ServiceException;

    public void delete(Long id) throws ServiceException;
}