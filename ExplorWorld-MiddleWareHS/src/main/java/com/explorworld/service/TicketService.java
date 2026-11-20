package com.explorworld.service;

import java.util.List;

import com.explorworld.model.Results;
import com.explorworld.model.Ticket;
import com.explorworld.model.criteria.TicketCriteria;

public interface TicketService {

    public Ticket findById(Long id) throws ServiceException;

    public Ticket findByTicketNumber(String ticketNumber) throws ServiceException;

    public List<Ticket> findByParticipantId(Long participantId) throws ServiceException;

    public Results<Ticket> findByCriteria(TicketCriteria criteria, int startIndex, int count) throws ServiceException;

    public Ticket create(Ticket ticket) throws ServiceException;

    public void update(Ticket ticket) throws ServiceException;

    public void cancel(Long id) throws ServiceException;

    public void delete(Long id) throws ServiceException;
}