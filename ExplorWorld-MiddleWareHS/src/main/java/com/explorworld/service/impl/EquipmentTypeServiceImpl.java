package com.explorworld.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.explorworld.dao.EquipmentTypeDAO;
import com.explorworld.model.DataException;
import com.explorworld.model.EquipmentType;
import com.explorworld.service.EquipmentTypeService;
import com.explorworld.service.ServiceException;

@Service
public class EquipmentTypeServiceImpl implements EquipmentTypeService {

    private static final Logger logger = LogManager.getLogger(EquipmentTypeServiceImpl.class);

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private EquipmentTypeDAO equipmentTypeDAO;

    @Override
    @Transactional(readOnly = true)
    public EquipmentType findById(Long id) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            return equipmentTypeDAO.findById(session, id);
        } catch (DataException e) {
            logger.error("Error al buscar tipo de equipamiento ID {}: {}", id, e.getMessage(), e);
            throw new ServiceException("No se pudo recuperar el tipo de equipamiento.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<EquipmentType> findAll() throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            return equipmentTypeDAO.findAll(session);
        } catch (DataException e) {
            logger.error("Error al listar tipos de equipamiento: {}", e.getMessage(), e);
            throw new ServiceException("No se pudo obtener la lista de tipos de equipamiento.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<EquipmentType> findByModel(String model) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            return equipmentTypeDAO.findByModel(session, model);
        } catch (DataException e) {
            logger.error("Error al buscar equipamiento por modelo {}: {}", model, e.getMessage(), e);
            throw new ServiceException("No se pudieron buscar tipos de equipamiento por modelo.", e);
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public EquipmentType create(EquipmentType equipmentType) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            EquipmentType created = equipmentTypeDAO.create(session, equipmentType);
            logger.info("Tipo de equipamiento creado con ID: {}", created.getId());
            return created;
        } catch (DataException e) {
            logger.error("Error al crear tipo de equipamiento: {}", e.getMessage(), e);
            throw new ServiceException("No se pudo crear el tipo de equipamiento.", e);
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void update(EquipmentType equipmentType) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            equipmentTypeDAO.update(session, equipmentType);
            logger.info("Tipo de equipamiento actualizado con ID: {}", equipmentType.getId());
        } catch (DataException e) {
            logger.error("Error al actualizar tipo de equipamiento ID {}: {}", equipmentType.getId(), e.getMessage(), e);
            throw new ServiceException("No se pudo actualizar el tipo de equipamiento.", e);
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void delete(Long id) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            EquipmentType existing = equipmentTypeDAO.findById(session, id);
            if (existing == null) {
                throw new ServiceException("No existe ningún tipo de equipamiento con ID: " + id);
            }
            equipmentTypeDAO.delete(session, existing);
            logger.info("Tipo de equipamiento eliminado con ID: {}", id);
        } catch (DataException e) {
            logger.error("Error al eliminar tipo de equipamiento ID {}: {}", id, e.getMessage(), e);
            throw new ServiceException("No se pudo eliminar el tipo de equipamiento.", e);
        }
    }
}