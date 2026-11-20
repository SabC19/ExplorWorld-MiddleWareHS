package com.explorworld.dao;

import java.util.List;
import org.hibernate.Session;
import com.explorworld.model.DataException;
import com.explorworld.model.Place;

public interface PlaceDAO {

    public Place findById(Session session, Long id) throws DataException;

    public List<Place> findAll(Session session) throws DataException;

    public List<Place> findByName(Session session, String name) throws DataException;

    public List<Place> findByLocalityId(Session session, Long localityId) throws DataException;

    public Place create(Session session, Place place) throws DataException;

    public void update(Session session, Place place) throws DataException;

    public void delete(Session session, Place place) throws DataException;
}