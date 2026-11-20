package com.explorworld.service.impl;

import java.util.List;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.explorworld.service.MailException;
import com.explorworld.service.MailService;

@Service("apacheMailService")
// Descomenta @Primary aquí (y quítalo del Mock) cuando quieras enviar correos reales:
@Primary
public class MailServiceApacheImpl implements MailService {

    private static final Logger logger = LogManager.getLogger(MailServiceApacheImpl.class);

    public MailServiceApacheImpl() {
    }

    @Override
    public void sendEmail(String para, String asunto, String contenido) throws MailException {
        try {
            HtmlEmail email = new HtmlEmail();
            email.setHostName("smtp.gmail.com");
            email.setSmtpPort(587);
            email.setStartTLSEnabled(true);
            email.setSSLOnConnect(false);
            email.setCharset("UTF-8");
            email.setFrom("explorworldes@gmail.com", "Explorworld");
            email.setAuthentication("explorworldes@gmail.com", "orjd ycoq fsot yjyy");
            email.setSubject(asunto);
            email.setHtmlMsg(contenido);
            email.addTo(para);
            email.send();
            logger.info("Email enviado correctamente a: {}", para);
        } catch (EmailException e) {
            logger.error("Error al enviar email a {}: {}", para, e.getMessage(), e);
            throw new MailException("No se pudo enviar el correo a " + para, e);
        }
    }

    @Override
    public void sendEmail(List<String> para, String asunto, String contenido) throws MailException {
        if (para == null || para.isEmpty()) {
            return;
        }
        for (String destinatario : para) {
            sendEmail(destinatario, asunto, contenido);
        }
    }
}