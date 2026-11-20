package com.explorworld.dao;

import java.util.List;
import org.hibernate.Session;
import com.explorworld.model.DataException;
import com.explorworld.model.Guide;
import com.explorworld.model.Results;
import com.explorworld.model.criteria.GuideCriteria;

public interface GuideDAO {

    public Guide findById(Session session, Long id) throws DataException;

    public Guide findByEmail(Session session, String email) throws DataException;
    
    public boolean emailExists(Session session, String email) throws DataException;

    public List<Guide> findAll(Session session) throws DataException;

    public Results<Guide> findByCriteria(Session session, GuideCriteria criteria, int startIndex, int count) throws DataException;

    public Guide create(Session session, Guide guide) throws DataException;

    public void update(Session session, Guide guide) throws DataException;

    public void delete(Session session, Guide guide) throws DataException;
}