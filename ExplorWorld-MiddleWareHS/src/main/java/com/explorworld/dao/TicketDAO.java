package com.explorworld.dao;

import java.util.List;
import org.hibernate.Session;
import com.explorworld.model.DataException;
import com.explorworld.model.Results;
import com.explorworld.model.Ticket;
import com.explorworld.model.criteria.TicketCriteria;

public interface TicketDAO {

    public Ticket findById(Session session, Long id) throws DataException;

    public Ticket findByTicketNumber(Session session, String ticketNumber) throws DataException;

    public List<Ticket> findByParticipantId(Session session, Long participantId) throws DataException;

    public Results<Ticket> findByCriteria(Session session, TicketCriteria criteria, int startIndex, int count) throws DataException;

    public Ticket create(Session session, Ticket ticket) throws DataException;

    public void update(Session session, Ticket ticket) throws DataException;

    public void delete(Session session, Ticket ticket) throws DataException;
}