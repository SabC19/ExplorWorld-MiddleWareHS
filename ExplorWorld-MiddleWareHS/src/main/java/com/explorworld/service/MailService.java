package com.explorworld.service;

import java.util.List;

public interface MailService {

    public void sendEmail(String para, String asunto, String contenido) throws MailException;

    public void sendEmail(List<String> para, String asunto, String contenido) throws MailException;
}