package com.explorworld.test;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.explorworld.model.Province;

public class PruebaHibernate {

    public static void main(String[] args) {

        SessionFactory factory = null;

        try {
            factory = new Configuration()
                    .configure("hibernate.cfg.xml")
                    .buildSessionFactory();

            try (Session session = factory.openSession()) {

                Province province = session.get(Province.class, 1L);

                if (province != null) {
                    System.out.println("Provincia encontrada:");
                    System.out.println(province);
                } else {
                    System.out.println(
                        "No existe una provincia con ID 1."
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (factory != null) {
                factory.close();
            }
        }
    }
}
