package com.explorworld.test;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.explorworld.model.Place;

public class PlaceTest {

	public static void main(String[] args) {
		
		SessionFactory factory = null;
		
		try {
			factory = new Configuration()
					.configure("hibernate.cfg.xml")
					.buildSessionFactory();
			
			try(Session session = factory.openSession()) {
				
				Long id = 1L;
				Place place = session.get(Place.class, id);
				
				if (place != null) {
					System.out.println("Lugar Encontrado:");
					System.out.println(place);
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
