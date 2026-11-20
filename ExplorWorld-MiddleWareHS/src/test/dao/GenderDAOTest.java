package com.explorworld.test.dao;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.explorworld.dao.GenderDAO;
import com.explorworld.dao.impl.GenderDAOImpl;
import com.explorworld.model.Gender;
import com.explorworld.utils.HibernateUtil;

public class GenderDAOTest {

    private GenderDAO genderDAO;

    public GenderDAOTest() {
        this.genderDAO = new GenderDAOImpl();
    }

    // 1. Probar listar todos los géneros
    public void testFindAll() {
        System.out.println("\n--- Ejecutando testFindAll ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Gender> genders = genderDAO.findAll(session);
            if (genders != null && !genders.isEmpty()) {
                for (Gender g : genders) {
                    System.out.println("ID: " + g.getId() + " | Nombre: " + g.getName());
                }
            } else {
                System.out.println("No hay géneros registrados en la base de datos.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 2. Probar búsqueda por ID
    public void testFindById(Long id) {
        System.out.println("\n--- Ejecutando testFindById para ID: " + id + " ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Gender g = genderDAO.findById(session, id);
            if (g != null) {
                System.out.println("Género encontrado -> ID: " + g.getId() + " | Nombre: " + g.getName());
            } else {
                System.out.println("No se encontró ningún género con el ID: " + id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 3. Probar creación de un nuevo género
    public Long testCreate(String name) {
        System.out.println("\n--- Ejecutando testCreate ---");
        Transaction tx = null;
        Long generatedId = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            Gender newGender = new Gender();
            newGender.setName(name);

            genderDAO.create(session, newGender);

            tx.commit();
            generatedId = newGender.getId();
            System.out.println("Género creado exitosamente con ID: " + generatedId);
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        return generatedId;
    }

    // 4. Probar actualización de un género existente
    public void testUpdate(Long id, String newName) {
        System.out.println("\n--- Ejecutando testUpdate para ID: " + id + " ---");
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            Gender g = genderDAO.findById(session, id);
            if (g != null) {
                g.setName(newName);
                genderDAO.update(session, g);
                tx.commit();
                System.out.println("Género actualizado correctamente a: " + g.getName());
            } else {
                System.out.println("No se pudo actualizar. No existe el ID: " + id);
            }
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
        }
    }

    // 5. Probar eliminación de un género
    public void testDelete(Long id) {
        System.out.println("\n--- Ejecutando testDelete para ID: " + id + " ---");
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            Gender g = genderDAO.findById(session, id);
            if (g != null) {
                genderDAO.delete(session, g);
                tx.commit();
                System.out.println("Género con ID " + id + " eliminado exitosamente.");
            } else {
                System.out.println("No se pudo eliminar. No existe el ID: " + id);
            }
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        GenderDAOTest test = new GenderDAOTest();

        // 1. Listar todos los géneros actuales
        test.testFindAll();

        // 2. Buscar un género conocido (ej. ID 1)
        test.testFindById(1L);

        // 3. Crear, actualizar y borrar un género de prueba (Descomenta para probar el ciclo completo)
        
        Long idCreado = test.testCreate("Género Prueba");
        if (idCreado != null) {
            test.testUpdate(idCreado, "Género Prueba Modificado");
            test.testFindById(idCreado);
            test.testDelete(idCreado);
        }
        

        // Cerramos el SessionFactory al terminar todas las pruebas
        HibernateUtil.shutdown();
    }
}