package com.explorworld.dao;

import java.util.List;
import org.hibernate.Session;
import com.explorworld.model.Gender;

public interface GenderDAO {
    
    public Gender findById(Session session, Long id);
    
    public List<Gender> findAll(Session session);
    
    public Gender create(Session session, Gender gender);
    
    public void update(Session session, Gender gender);
    
    public void delete(Session session, Gender gender);
}