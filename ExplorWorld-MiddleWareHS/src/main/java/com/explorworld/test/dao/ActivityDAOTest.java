package com.explorworld.test.dao;

import java.util.Date;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.explorworld.dao.ActivityDAO;
import com.explorworld.dao.impl.ActivityDAOImpl;
import com.explorworld.model.Activity;
import com.explorworld.model.ActivityType;
import com.explorworld.model.Place;
import com.explorworld.model.Results;
import com.explorworld.model.criteria.ActivityCriteria;
import com.explorworld.utils.HibernateUtil;

public class ActivityDAOTest {

    private static final Logger logger = LogManager.getLogger(ActivityDAOTest.class);
    private final ActivityDAO activityDAO;

    public ActivityDAOTest() {
        this.activityDAO = new ActivityDAOImpl();
    }

    // 1. Probar búsqueda por ID
    public void testFindById(Long id) {
        System.out.println("\n--- Ejecutando testFindById para ID: " + id + " ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Activity a = activityDAO.findById(session, id);
            if (a != null) {
                System.out.println("Encontrada: " + a.getName() + " | Precio Min: " + a.getPriceMin() 
                        + " | Capacidad: " + a.getCapacity());
                if (a.getActivityType() != null) {
                    System.out.println("  -> Tipo: " + a.getActivityType().getName());
                }
                if (a.getStartPlace() != null) {
                    System.out.println("  -> Lugar Inicio: " + a.getStartPlace().getName());
                }
            } else {
                System.out.println("No se encontró ninguna actividad con ID: " + id);
            }
        } catch (Exception e) {
            logger.error("Error en testFindById", e);
        }
    }

    // 2. Probar búsqueda paginada sin filtros (traer primeros 5)
    public void testFindByCriteriaAll() {
        System.out.println("\n--- Ejecutando testFindByCriteria (Sin filtros, página 1) ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            ActivityCriteria criteria = new ActivityCriteria();
            int startIndex = 0;
            int count = 5;

            Results<Activity> results = activityDAO.findByCriteria(session, criteria, startIndex, count);

            System.out.println("Total actividades que cumplen el filtro en BD: " + results.getTotal());
            System.out.println("Actividades cargadas en esta página: " + results.getPage().size());

            for (Activity a : results.getPage()) {
                System.out.println("ID: " + a.getId() + " | " + a.getName() 
                        + " | Fecha: " + a.getStartDate() + " | Precio: " + a.getPriceMin());
            }
        } catch (Exception e) {
            logger.error("Error en testFindByCriteriaAll", e);
        }
    }

    // 3. Probar búsqueda con filtros dinámicos (Nombre + Rango de Precio + Paginación)
    public void testFindByCriteriaFiltered(String nameFilter, Double minPrice, Double maxPrice) {
        System.out.println("\n--- Ejecutando testFindByCriteria (Filtros: name=" + nameFilter 
                + ", min=" + minPrice + ", max=" + maxPrice + ") ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            ActivityCriteria criteria = new ActivityCriteria();
            criteria.setName(nameFilter);
            criteria.setPriceMin(minPrice);
            criteria.setPriceMax(maxPrice);

            Results<Activity> results = activityDAO.findByCriteria(session, criteria, 0, 10);

            System.out.println("Total coincidencias: " + results.getTotal());
            for (Activity a : results.getPage()) {
                System.out.println("ID: " + a.getId() + " | " + a.getName() + " | Precio: " + a.getPriceMin());
            }
        } catch (Exception e) {
            logger.error("Error en testFindByCriteriaFiltered", e);
        }
    }

    // 4. Probar creación de una nueva actividad (asociando entidades foráneas existentes)
    public Long testCreate() {
        System.out.println("\n--- Ejecutando testCreate ---");
        Transaction tx = null;
        Long generatedId = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            // Obtenemos entidades relacionadas para cumplir las claves foráneas (FK)
            ActivityType type = session.get(ActivityType.class, 1L);
            Place startPlace = session.get(Place.class, 1L);
            Place endPlace = session.get(Place.class, 1L);

            Activity newActivity = new Activity();
            newActivity.setName("Ruta Senderismo Test");
            newActivity.setDescription("Actividad de prueba creada desde ActivityDAOTest");
            newActivity.setStartDate(new Date());
            newActivity.setEndDate(new Date(System.currentTimeMillis() + 86400000L)); // +1 día
            newActivity.setCapacity(15);
            newActivity.setPriceMin(25.0);
            newActivity.setPriceMax(50.0);
            newActivity.setActivityType(type);
            newActivity.setStartPlace(startPlace);
            newActivity.setEndPlace(endPlace);

            activityDAO.create(session, newActivity);

            tx.commit();
            generatedId = newActivity.getId();
            System.out.println("Actividad creada con ID generado: " + generatedId);
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            logger.error("Error en testCreate", e);
        }
        return generatedId;
    }

    // 5. Probar actualización de la actividad creada
    public void testUpdate(Long id, String updatedName, Double updatedPrice) {
        System.out.println("\n--- Ejecutando testUpdate para ID: " + id + " ---");
        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            Activity a = activityDAO.findById(session, id);
            if (a != null) {
                a.setName(updatedName);
                a.setPriceMin(updatedPrice);

                activityDAO.update(session, a);
                tx.commit();
                System.out.println("Actividad actualizada -> Nombre: " + a.getName() + " | Precio: " + a.getPriceMin());
            } else {
                System.out.println("No existe actividad con ID: " + id);
            }
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            logger.error("Error en testUpdate", e);
        }
    }

    // 6. Probar eliminación de la actividad
    public void testDelete(Long id) {
        System.out.println("\n--- Ejecutando testDelete para ID: " + id + " ---");
        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            Activity a = activityDAO.findById(session, id);
            if (a != null) {
                activityDAO.delete(session, a);
                tx.commit();
                System.out.println("Actividad con ID " + id + " eliminada correctamente.");
            } else {
                System.out.println("No se encontró la actividad para eliminar.");
            }
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            logger.error("Error en testDelete", e);
        }
    }

    public static void main(String[] args) {
        ActivityDAOTest test = new ActivityDAOTest();

        // 1. Probar listado general paginado
        test.testFindByCriteriaAll();

        // 2. Probar búsqueda por ID conocido
        test.testFindById(1L);

        // 3. Probar búsqueda con filtros
        test.testFindByCriteriaFiltered("Ruta", 10.0, 100.0);

        // 4. Ciclo completo CRUD: Crear -> Buscar -> Modificar -> Borrar
        Long newId = test.testCreate();
        if (newId != null) {
            test.testFindById(newId);
            test.testUpdate(newId, "Ruta Senderismo Test MODIFICADA", 35.0);
            test.testDelete(newId);
        }

        // Cierre de factoría al finalizar
        HibernateUtil.shutdown();
    }
}