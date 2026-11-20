package com.explorworld.service;

import java.util.List;
import com.explorworld.model.Gender;

public interface GenderService {

    public Gender findById(Long id) throws ServiceException;

    public List<Gender> findAll() throws ServiceException;

    public Gender create(Gender gender) throws ServiceException;

    public void update(Gender gender) throws ServiceException;

    public void delete(Long id) throws ServiceException;
}