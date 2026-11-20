package com.explorworld.service;

import com.explorworld.model.Activity;
import com.explorworld.model.Results;
import com.explorworld.model.criteria.ActivityCriteria;

public interface ActivityService {

    public Activity findById(Long id) throws ServiceException;

    public Results<Activity> findByCriteria(ActivityCriteria criteria, int startIndex, int count) throws ServiceException;

    public Activity create(Activity activity) throws ServiceException;

    public void update(Activity activity) throws ServiceException;

    public void delete(Long id) throws ServiceException;
}