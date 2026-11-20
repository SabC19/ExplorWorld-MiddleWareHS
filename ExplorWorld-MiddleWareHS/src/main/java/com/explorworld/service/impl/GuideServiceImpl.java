package com.explorworld.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.explorworld.dao.GuideDAO;
import com.explorworld.model.DataException;
import com.explorworld.model.Guide;
import com.explorworld.model.Results;
import com.explorworld.model.criteria.GuideCriteria;
import com.explorworld.service.EncryptionService;
import com.explorworld.service.GuideService;
import com.explorworld.service.MailException;
import com.explorworld.service.MailService;
import com.explorworld.service.ServiceException;

@Service
public class GuideServiceImpl implements GuideService {

    private static final Logger logger = LogManager.getLogger(GuideServiceImpl.class);

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private GuideDAO guideDAO;

    @Autowired
    private EncryptionService encryptionService;

    @Autowired
    private MailService mailService;

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public Guide register(Guide guide) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();

            if (guideDAO.emailExists(session, guide.getEmail())) {
                throw new ServiceException("Ya existe un guía registrado con ese email.");
            }

            guide.setEmail(guide.getEmail().trim().toLowerCase());
            if (guide.getNieDni() != null) {
                guide.setNieDni(guide.getNieDni().trim().toUpperCase());
            }
            guide.setPassword(encryptionService.encrypt(guide.getPassword()));

            Guide created = guideDAO.create(session, guide);
            logger.info("Guía registrado con ID: {}", created.getId());

            sendWelcomeEmail(created);
            return created;
        } catch (DataException e) {
            logger.error("Error de persistencia al registrar guía: {}", e.getMessage(), e);
            throw new ServiceException("No se pudo completar el registro del guía.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Guide login(String email, String plainPassword) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            Guide guide = guideDAO.findByEmail(session, email);

            if (guide == null || !encryptionService.check(plainPassword, guide.getPassword())) {
                throw new ServiceException("Credenciales incorrectas.");
            }

            logger.info("Login correcto para guía ID: {}", guide.getId());
            return guide;
        } catch (DataException e) {
            logger.error("Error de persistencia en login de guía {}: {}", email, e.getMessage(), e);
            throw new ServiceException("No se pudo iniciar sesión.", e);
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public boolean updatePassword(Long id, String oldPassword, String newPassword) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            Guide guide = guideDAO.findById(session, id);

            if (guide == null) {
                throw new ServiceException("Guía no encontrado con ID: " + id);
            }
            if (!encryptionService.check(oldPassword, guide.getPassword())) {
                return false;
            }
            if (encryptionService.check(newPassword, guide.getPassword())) {
                return false;
            }

            guide.setPassword(encryptionService.encrypt(newPassword));
            guideDAO.update(session, guide);
            logger.info("Contraseña actualizada para guía ID: {}", id);
            return true;
        } catch (DataException e) {
            logger.error("Error al actualizar contraseña de guía ID {}: {}", id, e.getMessage(), e);
            throw new ServiceException("No se pudo actualizar la contraseña.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Guide findById(Long id) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            return guideDAO.findById(session, id);
        } catch (DataException e) {
            logger.error("Error al buscar guía ID {}: {}", id, e.getMessage(), e);
            throw new ServiceException("No se pudo recuperar el guía.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Guide findByEmail(String email) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            return guideDAO.findByEmail(session, email);
        } catch (DataException e) {
            logger.error("Error al buscar guía por email {}: {}", email, e.getMessage(), e);
            throw new ServiceException("No se pudo buscar el guía por email.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Guide> findAll() throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            return guideDAO.findAll(session);
        } catch (DataException e) {
            logger.error("Error al listar guías: {}", e.getMessage(), e);
            throw new ServiceException("No se pudo obtener la lista de guías.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Results<Guide> findByCriteria(GuideCriteria criteria, int startIndex, int count) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            return guideDAO.findByCriteria(session, criteria, startIndex, count);
        } catch (DataException e) {
            logger.error("Error en búsqueda paginada de guías: {}", e.getMessage(), e);
            throw new ServiceException("Error al buscar guías.", e);
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void update(Guide guide) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            guideDAO.update(session, guide);
            logger.info("Guía actualizado con ID: {}", guide.getId());
        } catch (DataException e) {
            logger.error("Error al actualizar guía ID {}: {}", guide.getId(), e.getMessage(), e);
            throw new ServiceException("No se pudo actualizar el guía.", e);
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void delete(Long id) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            Guide existing = guideDAO.findById(session, id);
            if (existing == null) {
                throw new ServiceException("No existe ningún guía con ID: " + id);
            }
            guideDAO.delete(session, existing);
            logger.info("Guía eliminado con ID: {}", id);
        } catch (DataException e) {
            logger.error("Error al eliminar guía ID {}: {}", id, e.getMessage(), e);
            throw new ServiceException("No se pudo eliminar el guía.", e);
        }
    }

    private void sendWelcomeEmail(Guide guide) {
        try {
            mailService.sendEmail(
                    guide.getEmail(),
                    "Bienvenido al equipo de ExplorWorld",
                    "<h1>¡Hola " + guide.getName() + "!</h1><p>Tu cuenta de guía en ExplorWorld ha sido activada.</p>");
        } catch (MailException e) {
            logger.warn("No se pudo enviar el correo de bienvenida a {}: {}", guide.getEmail(), e.getMessage());
        }
    }
}