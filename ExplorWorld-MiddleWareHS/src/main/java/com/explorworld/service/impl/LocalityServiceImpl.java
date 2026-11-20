package com.explorworld.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.explorworld.dao.LocalityDAO;
import com.explorworld.model.DataException;
import com.explorworld.model.Locality;
import com.explorworld.service.LocalityService;
import com.explorworld.service.ServiceException;

@Service
public class LocalityServiceImpl implements LocalityService {

    private static final Logger logger = LogManager.getLogger(LocalityServiceImpl.class);

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private LocalityDAO localityDAO;

    @Override
    @Transactional(readOnly = true)
    public Locality findById(Long id) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            return localityDAO.findById(session, id);
        } catch (DataException e) {
            logger.error("Error al buscar localidad ID {}: {}", id, e.getMessage(), e);
            throw new ServiceException("No se pudo recuperar la localidad.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Locality> findAll() throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            return localityDAO.findAll(session);
        } catch (DataException e) {
            logger.error("Error al listar localidades: {}", e.getMessage(), e);
            throw new ServiceException("No se pudo obtener la lista de localidades.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Locality> findByName(String name) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            return localityDAO.findByName(session, name);
        } catch (DataException e) {
            logger.error("Error al buscar localidades por nombre {}: {}", name, e.getMessage(), e);
            throw new ServiceException("No se pudieron buscar localidades por nombre.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Locality> findByProvinceId(Long provinceId) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            return localityDAO.findByProvinceId(session, provinceId);
        } catch (DataException e) {
            logger.error("Error al buscar localidades de la provincia ID {}: {}", provinceId, e.getMessage(), e);
            throw new ServiceException("No se pudieron obtener las localidades de la provincia.", e);
        }
    }

}