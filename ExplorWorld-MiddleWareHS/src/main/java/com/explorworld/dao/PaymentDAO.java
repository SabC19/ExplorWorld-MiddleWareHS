package com.explorworld.dao;

import java.util.List;

import org.hibernate.Session;

import com.explorworld.model.DataException;
import com.explorworld.model.Payment;
import com.explorworld.model.Results;
import com.explorworld.model.criteria.PaymentCriteria;

public interface PaymentDAO {

    public Payment findById(Session session, Long id) throws DataException;

    public List<Payment> findByTicketId(Session session, Long ticketId) throws DataException;

    public Results<Payment> findByCriteria(Session session, PaymentCriteria criteria, int startIndex, int count) throws DataException;

    public Payment create(Session session, Payment payment) throws DataException;

    public void update(Session session, Payment payment) throws DataException;

    public void delete(Session session, Payment payment) throws DataException;
}