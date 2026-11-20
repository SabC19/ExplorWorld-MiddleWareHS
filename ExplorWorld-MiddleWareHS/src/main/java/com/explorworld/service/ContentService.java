package com.explorworld.service;

import java.util.List;

import com.explorworld.model.ContentData;
import com.explorworld.model.ContentException;
import com.explorworld.model.ContentMetadata;
import com.explorworld.model.DataException;

public interface ContentService {

    public ContentData getContent(ContentMetadata metadata) throws ContentException;

    public List<ContentData> getContents(String entityType, String entityId) throws ContentException;

    public void save(ContentData data) throws ContentException;

    public void remove(ContentMetadata metadata) throws DataException;

    public void remove(String contentPath) throws DataException;
}