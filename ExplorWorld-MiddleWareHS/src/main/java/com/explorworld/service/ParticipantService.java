package com.explorworld.service;

import java.util.List;

import com.explorworld.model.Participant;
import com.explorworld.model.Results;
import com.explorworld.model.criteria.ParticipantCriteria;

public interface ParticipantService {

    public Participant register(Participant participant) throws ServiceException;

    public Participant login(String email, String plainPassword) throws ServiceException;

    public boolean updatePassword(Long id, String oldPassword, String newPassword) throws ServiceException;

    public Participant findById(Long id) throws ServiceException;

    public Participant findByEmail(String email) throws ServiceException;

    public List<Participant> findAll() throws ServiceException;

    public Results<Participant> findByCriteria(ParticipantCriteria criteria, int startIndex, int count) throws ServiceException;

    public void update(Participant participant) throws ServiceException;

    public void delete(Long id) throws ServiceException;
}