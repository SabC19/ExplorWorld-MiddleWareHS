package com.explorworld.test;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.explorworld.model.Guide;

public class GuideTest {

	
	public static void main(String[] args) {
		
		SessionFactory factory = null;
		
		try {
			factory = new Configuration()
					.configure("hibernate.cfg.xml")
					.buildSessionFactory();
			
			try(Session session = factory.openSession()) {
				
				Long id = 1L;
				Guide guide = session.get(Guide.class, id);
				
				if (guide != null) {
					System.out.println("Guia Encontrado:");
					System.out.println(guide);
				} else {
					System.out.println("No existe un lugar con ID" + id);
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
