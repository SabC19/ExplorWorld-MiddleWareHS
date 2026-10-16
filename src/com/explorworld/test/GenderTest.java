package com.explorworld.test;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.explorworld.model.Gender;

public class GenderTest {

	public static void main(String[] args) {
		
		SessionFactory factory = null;
	
	try {
		factory = new Configuration()
				.configure("hibernate.cfg.xml")
				.buildSessionFactory();
		
		try(Session session = factory.openSession()) {
			
			Long id = 1L;
			Gender gender = session.get(Gender.class, id);
			
			if (gender != null) {
				System.out.println("Genero Encontrado:");
				System.out.println(gender);
			} else {
				System.out.println("No existe un genero con ID" + id);
			}
		}
				
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		if (factory !=null) {
			factory.close();
		}
	}

	}
}
