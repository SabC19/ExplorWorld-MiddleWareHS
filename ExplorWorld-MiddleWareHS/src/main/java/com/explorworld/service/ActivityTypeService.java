package com.explorworld.service;

import java.util.List;
import com.explorworld.model.ActivityType;

public interface ActivityTypeService {

    public ActivityType findById(Long id) throws ServiceException;

    public List<ActivityType> findAll() throws ServiceException;

    public ActivityType create(ActivityType activityType) throws ServiceException;

    public void update(ActivityType activityType) throws ServiceException;

    public void delete(Long id) throws ServiceException;
}