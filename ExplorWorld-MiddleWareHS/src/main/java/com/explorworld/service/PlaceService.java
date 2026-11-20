package com.explorworld.service;

import java.util.List;
import com.explorworld.model.Place;

public interface PlaceService {

    public Place findById(Long id) throws ServiceException;

    public List<Place> findAll() throws ServiceException;

    public List<Place> findByName(String name) throws ServiceException;

    public List<Place> findByLocalityId(Long localityId) throws ServiceException;

    public Place create(Place place) throws ServiceException;

    public void update(Place place) throws ServiceException;

    public void delete(Long id) throws ServiceException;
}