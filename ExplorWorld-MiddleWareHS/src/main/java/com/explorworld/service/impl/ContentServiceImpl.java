package com.explorworld.service.impl;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.explorworld.model.ContentData;
import com.explorworld.model.ContentException;
import com.explorworld.model.ContentMetadata;
import com.explorworld.model.DataException;
import com.explorworld.service.ContentService;
import com.explorworld.utils.FileUtils;

@Service
public class ContentServiceImpl implements ContentService {

	private static Logger logger = LogManager.getLogger(ContentServiceImpl.class);

	private static final String CMS_BASE_FOLDER = System.getProperty("os.name").toLowerCase().contains("win")
			? "c:/explorworld/cms"
			: System.getProperty("user.home") + "/explorworld/cms";

	public ContentServiceImpl() {

	}

	/**
	 * Genera la ruta del contenido en base a sus metadatos.
	 * Funciona tanto para guardar recursos unicos (Ejemplo: Imagen de usuario)
	 * como para recursos variados (Ejemplo: fotos_actividad>foto1.jpg,foto2.jpg,foto3.jpg)
	 * @param metadata
	 * @return
	 */
	protected String getPath(ContentMetadata metadata) {
		StringBuilder buffer = new StringBuilder();
		buffer.append(CMS_BASE_FOLDER)
				.append("/").append(metadata.getEntityType())
				.append("/").append(metadata.getEntityId());

		if (metadata.getResourceId() != null && !metadata.getResourceId().trim().isEmpty()) {
			buffer.append("/").append(metadata.getResourceId());
		}

		// Si tiene resourceName, significa que está dentro de una subcarpeta (relación 1 a N)
		if (metadata.getResourceName() != null && !metadata.getResourceName().trim().isEmpty()) {
			buffer.append("/").append(metadata.getResourceName());
		}

		buffer.append(metadata.getFormat());
		return buffer.toString();
	}

	@Override
	public ContentData getContent(ContentMetadata metadata) throws ContentException {
		String contentPath = getPath(metadata);

		try {
			byte[] raw = FileUtils.readFully(contentPath);

			ContentData data = new ContentData();
			data.setMetadata(metadata);
			data.setRaw(raw);

			return data;

		} catch (IOException ioe) {
			logger.error("Leyendo " + contentPath + ": " + ioe.getMessage(), ioe);
			throw new ContentException(contentPath, ioe);
		}
	}

	@Override
	public List<ContentData> getContents(String entityType, String entityId) throws ContentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(ContentData data) throws ContentException {
		ContentMetadata metadata = data.getMetadata();
		// En qué fichero en disco se va a guardar data.
		// Convencion de nombrado.
		String contentPath = getPath(metadata);
		try {
			FileUtils.write(data.getRaw(), contentPath, false);
		} catch (IOException ioe) {
			logger.error("Guardando " + contentPath + ": " + ioe.getMessage(), ioe);
			throw new ContentException(contentPath, ioe);
		}
	}

	@Override
	public void remove(ContentMetadata metadata) throws DataException {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(String id) throws DataException {
		// TODO Auto-generated method stub

	}
}