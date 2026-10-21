
package com.explorworld.test;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.explorworld.model.Activity;
import com.explorworld.model.ActivityType;
import com.explorworld.model.Place;

public class ActivityTest {

    public static void main(String[] args) {

        SessionFactory factory = null;

        try {
            factory = new Configuration()
                    .configure("hibernate.cfg.xml")
                    .buildSessionFactory();

            Long activityId;

            try (Session session = factory.openSession()) {

                Transaction tx = session.beginTransaction();

                try {
                    ActivityType type =
                            session.get(ActivityType.class, 1L);

                    Place place =
                            session.get(Place.class, 1L);

                    if (type == null || place == null) {
                        throw new IllegalStateException(
                            "No existe el tipo de actividad o el lugar con ID 1."
                        );
                    }

                    SimpleDateFormat sdf =
                            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    Date startDate = sdf.parse("2026-11-10 10:00:00");
                    Date endDate = sdf.parse("2026-11-10 14:00:00");

                    Activity activity = new Activity();

                    activity.setName("Actividad de prueba Hibernate");
                    activity.setDescription(
                            "Registro creado desde Hibernate");
                    activity.setStartDate(startDate);
                    activity.setEndDate(endDate);
                    activity.setCapacity(10);
                    activity.setPriceMin(15.0);
                    activity.setPriceMax(25.0);

                    activity.setActivityType(type);
                    activity.setStartPlace(place);
                    activity.setEndPlace(place);

                    session.save(activity);

                    tx.commit();

                    activityId = activity.getId();

                    System.out.println(
                        "INSERT OK. ID generado: " + activityId);

                } catch (Exception e) {
                    if (tx.isActive()) {
                        tx.rollback();
                    }
                    throw e;
                }
            }

            // MODIFICAR
            try (Session session = factory.openSession()) {

                Transaction tx = session.beginTransaction();

                try {
                    Activity activity =
                            session.get(Activity.class, activityId);

                    if (activity == null) {
                        throw new IllegalStateException(
                            "No se encuentra la actividad insertada.");
                    }

                    activity.setName("Actividad de prueba MODIFICADA");

                    tx.commit();

                    System.out.println("UPDATE OK: "
                            + activity.getName());

                } catch (Exception e) {
                    if (tx.isActive()) {
                        tx.rollback();
                    }
                    throw e;
                }
            }

            // ELIMINAR
            try (Session session = factory.openSession()) {

                Transaction tx = session.beginTransaction();

                try {
                    Activity activity =
                            session.get(Activity.class, activityId);

                    if (activity != null) {
                        session.delete(activity);
                        tx.commit();

                        System.out.println(
                            "DELETE OK. ID eliminado: " + activityId);
                    } else {
                        tx.rollback();
                        System.out.println(
                            "No se encontró la actividad para eliminar.");
                    }

                } catch (Exception e) {
                    if (tx.isActive()) {
                        tx.rollback();
                    }
                    throw e;
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
