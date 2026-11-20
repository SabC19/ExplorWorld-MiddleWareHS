package com.explorworld.dao;

import java.util.List;
import org.hibernate.Session;
import com.explorworld.model.DataException;
import com.explorworld.model.Participant;
import com.explorworld.model.Results;
import com.explorworld.model.criteria.ParticipantCriteria;

public interface ParticipantDAO {

    public Participant findById(Session session, Long id) throws DataException;

    public Participant findByEmail(Session session, String email) throws DataException;

    public boolean emailExists(Session session, String email) throws DataException;

    public List<Participant> findAll(Session session) throws DataException;

    public Results<Participant> findByCriteria(Session session, ParticipantCriteria criteria, int startIndex, int count) throws DataException;

    public Participant create(Session session, Participant participant) throws DataException;

    public void update(Session session, Participant participant) throws DataException;

    public void delete(Session session, Participant participant) throws DataException;
}