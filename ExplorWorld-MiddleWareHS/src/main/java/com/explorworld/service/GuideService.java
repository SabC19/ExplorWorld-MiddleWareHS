package com.explorworld.service;

import java.util.List;

import com.explorworld.model.Guide;
import com.explorworld.model.Results;
import com.explorworld.model.criteria.GuideCriteria;

public interface GuideService {

    public Guide register(Guide guide) throws ServiceException;

    public Guide login(String email, String plainPassword) throws ServiceException;

    public boolean updatePassword(Long id, String oldPassword, String newPassword) throws ServiceException;

    public Guide findById(Long id) throws ServiceException;

    public Guide findByEmail(String email) throws ServiceException;
    
    public List<Guide> findAll() throws ServiceException;

    public Results<Guide> findByCriteria(GuideCriteria criteria, int startIndex, int count) throws ServiceException;

    public void update(Guide guide) throws ServiceException;

    public void delete(Long id) throws ServiceException;
}