package com.explorworld.dao;

import java.util.List;
import org.hibernate.Session;

import com.explorworld.model.DataException;
import com.explorworld.model.Gender;

public interface GenderDAO {
    
    public Gender findById(Session session, Long id) throws DataException;
    
    public List<Gender> findAll(Session session) throws DataException;
    
    public Gender create(Session session, Gender gender) throws DataException;
    
    public void update(Session session, Gender gender) throws DataException;
    
    public void delete(Session session, Gender gender) throws DataException;
}