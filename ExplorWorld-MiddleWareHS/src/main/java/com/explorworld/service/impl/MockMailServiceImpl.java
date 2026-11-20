package com.explorworld.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.explorworld.service.MailService;

@Service("mockMailService")
//@Primary // Activo por defecto mientras pruebas tus Services en local
public class MockMailServiceImpl implements MailService {

    private static final Logger logger = LogManager.getLogger(MockMailServiceImpl.class);

    public MockMailServiceImpl() {
    }

    @Override
    public void sendEmail(String para, String asunto, String contenido) {
        logger.info("[MOCK EMAIL] Para: {} | Asunto: {} | Contenido: {}", para, asunto, contenido);
    }

    @Override
    public void sendEmail(List<String> para, String asunto, String contenido) {
        if (para == null || para.isEmpty()) {
            return;
        }
        for (String destinatario : para) {
            sendEmail(destinatario, asunto, contenido);
        }
        logger.info("[MOCK EMAIL] Enviado a {} destinatarios.", para.size());
    }
}