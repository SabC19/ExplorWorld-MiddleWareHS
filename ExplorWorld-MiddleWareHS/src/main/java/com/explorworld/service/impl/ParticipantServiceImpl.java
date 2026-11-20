package com.explorworld.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.explorworld.dao.ParticipantDAO;
import com.explorworld.model.DataException;
import com.explorworld.model.Participant;
import com.explorworld.model.Results;
import com.explorworld.model.criteria.ParticipantCriteria;
import com.explorworld.service.EncryptionService;
import com.explorworld.service.MailException;
import com.explorworld.service.MailService;
import com.explorworld.service.ParticipantService;
import com.explorworld.service.ServiceException;

@Service
public class ParticipantServiceImpl implements ParticipantService {

    private static final Logger logger = LogManager.getLogger(ParticipantServiceImpl.class);

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private ParticipantDAO participantDAO;

    @Autowired
    private EncryptionService encryptionService;

    @Autowired
    private MailService mailService;

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public Participant register(Participant participant) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();

            if (participantDAO.emailExists(session, participant.getEmail())) {
                throw new ServiceException("Ya existe un participante registrado con ese email.");
            }

            participant.setEmail(participant.getEmail().trim().toLowerCase());
            participant.setPassword(encryptionService.encrypt(participant.getPassword()));

            Participant created = participantDAO.create(session, participant);
            logger.info("Participante registrado con ID: {}", created.getId());

            sendWelcomeEmail(created);
            return created;
        } catch (ServiceException e) {
            throw e;
        } catch (DataException e) {
            logger.error("Error de persistencia al registrar participante: {}", e.getMessage(), e);
            throw new ServiceException("No se pudo completar el registro del participante.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Participant login(String email, String plainPassword) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            Participant participant = participantDAO.findByEmail(session, email);

            if (participant == null || !encryptionService.check(plainPassword, participant.getPassword())) {
                throw new ServiceException("Credenciales incorrectas.");
            }

            logger.info("Login correcto para participante ID: {}", participant.getId());
            return participant;
        } catch (ServiceException e) {
            throw e;
        } catch (DataException e) {
            logger.error("Error de persistencia en login de {}: {}", email, e.getMessage(), e);
            throw new ServiceException("No se pudo iniciar sesión.", e);
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public boolean updatePassword(Long id, String oldPassword, String newPassword) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            Participant participant = participantDAO.findById(session, id);

            if (participant == null) {
                throw new ServiceException("Participante no encontrado con ID: " + id);
            }
            if (!encryptionService.check(oldPassword, participant.getPassword())) {
                return false;
            }
            if (encryptionService.check(newPassword, participant.getPassword())) {
                return false;
            }

            participant.setPassword(encryptionService.encrypt(newPassword));
            participantDAO.update(session, participant);
            logger.info("Contraseña actualizada para participante ID: {}", id);
            return true;
        } catch (ServiceException e) {
            throw e;
        } catch (DataException e) {
            logger.error("Error al actualizar contraseña de participante ID {}: {}", id, e.getMessage(), e);
            throw new ServiceException("No se pudo actualizar la contraseña.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Participant findById(Long id) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            return participantDAO.findById(session, id);
        } catch (DataException e) {
            logger.error("Error al buscar participante ID {}: {}", id, e.getMessage(), e);
            throw new ServiceException("No se pudo recuperar el participante.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Participant findByEmail(String email) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            return participantDAO.findByEmail(session, email);
        } catch (DataException e) {
            logger.error("Error al buscar participante por email {}: {}", email, e.getMessage(), e);
            throw new ServiceException("No se pudo buscar el participante por email.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Participant> findAll() throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            return participantDAO.findAll(session);
        } catch (DataException e) {
            logger.error("Error al listar participantes: {}", e.getMessage(), e);
            throw new ServiceException("No se pudo obtener la lista de participantes.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Results<Participant> findByCriteria(ParticipantCriteria criteria, int startIndex, int count) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            return participantDAO.findByCriteria(session, criteria, startIndex, count);
        } catch (DataException e) {
            logger.error("Error en búsqueda paginada de participantes: {}", e.getMessage(), e);
            throw new ServiceException("Error al buscar participantes.", e);
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void update(Participant participant) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            participantDAO.update(session, participant);
            logger.info("Participante actualizado con ID: {}", participant.getId());
        } catch (DataException e) {
            logger.error("Error al actualizar participante ID {}: {}", participant.getId(), e.getMessage(), e);
            throw new ServiceException("No se pudo actualizar el participante.", e);
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void delete(Long id) throws ServiceException {
        try {
            Session session = sessionFactory.getCurrentSession();
            Participant existing = participantDAO.findById(session, id);
            if (existing == null) {
                throw new ServiceException("No existe ningún participante con ID: " + id);
            }
            participantDAO.delete(session, existing);
            logger.info("Participante eliminado con ID: {}", id);
        } catch (ServiceException e) {
            throw e;
        } catch (DataException e) {
            logger.error("Error al eliminar participante ID {}: {}", id, e.getMessage(), e);
            throw new ServiceException("No se pudo eliminar el participante.", e);
        }
    }

    private void sendWelcomeEmail(Participant participant) {
        try {
            mailService.sendEmail(
                    participant.getEmail(),
                    "Bienvenido a ExplorWorld",
                    "<h1>¡Hola " + participant.getName() + "!</h1><p>Tu cuenta en ExplorWorld se ha registrado correctamente.</p>");
        } catch (MailException e) {
            logger.warn("No se pudo enviar el correo de bienvenida a {}: {}", participant.getEmail(), e.getMessage());
        }
    }
}