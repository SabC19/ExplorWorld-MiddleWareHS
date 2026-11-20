package com.explorworld.service;

import java.util.List;

import com.explorworld.model.Payment;
import com.explorworld.model.Results;
import com.explorworld.model.criteria.PaymentCriteria;

public interface PaymentService {

    public Payment findById(Long id) throws ServiceException;

    public List<Payment> findByTicketId(Long ticketId) throws ServiceException;

    public Results<Payment> findByCriteria(PaymentCriteria criteria, int startIndex, int count) throws ServiceException;

    public Payment create(Payment payment) throws ServiceException;

    public void update(Payment payment) throws ServiceException;

    public void delete(Long id) throws ServiceException;
}