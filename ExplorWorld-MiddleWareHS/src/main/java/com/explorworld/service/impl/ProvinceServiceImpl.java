package com.explorworld.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.explorworld.dao.ProvinceDAO;
import com.explorworld.model.DataException;
import com.explorworld.model.Province;
import com.explorworld.service.ProvinceService;
import com.explorworld.service.ServiceException;

@Service
public class ProvinceServiceImpl implements ProvinceService {

    private static final Logger logger = LogManager.getLogger(ProvinceServiceImpl.class);

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private ProvinceDAO provinceDAO;

    @Override
    @Transactional(readOnly = true)
    public Province findById(Long id) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            return provinceDAO.findById(session, id);
        } catch (DataException e) {
            logger.error("Error al buscar provincia ID {}: {}", id, e.getMessage(), e);
            throw new ServiceException("No se pudo recuperar la provincia.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Province> findAll() throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            return provinceDAO.findAll(session);
        } catch (DataException e) {
            logger.error("Error al listar provincias: {}", e.getMessage(), e);
            throw new ServiceException("No se pudo obtener la lista de provincias.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Province> findByName(String name) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            return provinceDAO.findByName(session, name);
        } catch (DataException e) {
            logger.error("Error al buscar provincias por nombre {}: {}", name, e.getMessage(), e);
            throw new ServiceException("No se pudieron buscar provincias por nombre.", e);
        }
    }

}