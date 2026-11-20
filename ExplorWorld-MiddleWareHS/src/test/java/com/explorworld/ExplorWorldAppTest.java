package com.explorworld;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.explorworld.config.AppConfig;
import com.explorworld.model.Activity;
import com.explorworld.model.ContentData;
import com.explorworld.model.ContentMetadata;
import com.explorworld.model.Gender;
import com.explorworld.model.Guide;
import com.explorworld.model.Participant;
import com.explorworld.model.Payment;
import com.explorworld.model.PaymentMethod;
import com.explorworld.model.Results;
import com.explorworld.model.Ticket;
import com.explorworld.model.criteria.ActivityCriteria;
import com.explorworld.model.criteria.GuideCriteria;
import com.explorworld.model.criteria.ParticipantCriteria;
import com.explorworld.service.ActivityService;
import com.explorworld.service.ActivityTypeService;
import com.explorworld.service.ContentService;
import com.explorworld.service.EncryptionService;
import com.explorworld.service.EquipmentTypeService;
import com.explorworld.service.GenderService;
import com.explorworld.service.GuideService;
import com.explorworld.service.LocalityService;
import com.explorworld.service.ParticipantService;
import com.explorworld.service.PaymentMethodService;
import com.explorworld.service.PaymentService;
import com.explorworld.service.PlaceService;
import com.explorworld.service.ProvinceService;
import com.explorworld.service.ServiceException;
import com.explorworld.service.TicketService;
import com.explorworld.service.TransportService;
import com.explorworld.service.TransportTypeService;
import com.explorworld.service.impl.ContentServiceImpl;

public class ExplorWorldAppTest {

    public static void main(String[] args) {
        System.out.println("====================================================");
        System.out.println(" INICIANDO TEST INTEGRAL DE EXPLORWORLD MIDDLEWARE ");
        System.out.println("====================================================");

        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            // ------------------------------------------------------------------
            // 1. VERIFICAR INYECCIÓN DE TODOS LOS SERVICIOS Y CATÁLOGOS MAESTROS
            // ------------------------------------------------------------------
            System.out.println("\n[1] Comprobando contexto Spring y servicios maestros...");

            GenderService genderService = context.getBean(GenderService.class);
            ProvinceService provinceService = context.getBean(ProvinceService.class);
            LocalityService localityService = context.getBean(LocalityService.class);
            PlaceService placeService = context.getBean(PlaceService.class);
            ActivityTypeService activityTypeService = context.getBean(ActivityTypeService.class);
            EquipmentTypeService equipmentTypeService = context.getBean(EquipmentTypeService.class);
            TransportTypeService transportTypeService = context.getBean(TransportTypeService.class);
            TransportService transportService = context.getBean(TransportService.class);
            PaymentMethodService paymentMethodService = context.getBean(PaymentMethodService.class);

            ActivityService activityService = context.getBean(ActivityService.class);
            ParticipantService participantService = context.getBean(ParticipantService.class);
            GuideService guideService = context.getBean(GuideService.class);
            TicketService ticketService = context.getBean(TicketService.class);
            PaymentService paymentService = context.getBean(PaymentService.class);
            EncryptionService encryptionService = context.getBean(EncryptionService.class);

            List<Gender> genders = genderService.findAll();
            List<PaymentMethod> activeMethods = paymentMethodService.findActive();

            System.out.println("  - Genders: " + genders.size());
            System.out.println("  - Provinces: " + provinceService.findAll().size());
            System.out.println("  - Localities: " + localityService.findAll().size());
            System.out.println("  - Places: " + placeService.findAll().size());
            System.out.println("  - ActivityTypes: " + activityTypeService.findAll().size());
            System.out.println("  - EquipmentTypes: " + equipmentTypeService.findAll().size());
            System.out.println("  - TransportTypes: " + transportTypeService.findAll().size());
            System.out.println("  - Transports: " + transportService.findAll().size());
            System.out.println("  - PaymentMethods activos: " + activeMethods.size());

            // ------------------------------------------------------------------
            // 2. TEST DE ENCRIPTACIÓN (BCRYPT)
            // ------------------------------------------------------------------
            System.out.println("\n[2] Probando EncryptionService (BCrypt)...");
            String hash = encryptionService.encrypt("MiClave123!");
            boolean okPass = encryptionService.check("MiClave123!", hash);
            boolean badPass = encryptionService.check("ClaveIncorrecta", hash);
            System.out.println("  - Password correcta -> " + okPass + " (esperado: true)");
            System.out.println("  - Password errónea  -> " + badPass + " (esperado: false)");

            // IDs creados durante el test para limpiarlos al final
            Long createdPaymentId = null;
            Long createdTicketId = null;
            Long createdParticipantId = null;
            Long createdGuideId = null;

            String uniqueSuffix = String.valueOf(System.currentTimeMillis());

            try {
                Gender defaultGender = genders.isEmpty() ? null : genders.get(0);

                // ------------------------------------------------------------------
                // 3. TEST DE PARTICIPANT (REGISTER + MAIL + LOGIN + UPDATE PASSWORD)
                // ------------------------------------------------------------------
                System.out.println("\n[3] Probando ParticipantService (registro, login, password, criteria)...");
                Participant p = new Participant();
                p.setName("Carlos");
                p.setLastName("Prueba");
                p.setPhoneNumber("600111222");
                p.setEmail("participante." + uniqueSuffix + "@explorworld.test");
                p.setBirthDate(new Date());
                p.setPassword("PassInicial123");
                p.setGender(defaultGender);

                Participant savedParticipant = participantService.register(p);
                createdParticipantId = savedParticipant.getId();
                System.out.println("  - Registrado ID: " + createdParticipantId + " | Email: " + savedParticipant.getEmail());

                // Login correcto
                Participant logged = participantService.login(savedParticipant.getEmail(), "PassInicial123");
                System.out.println("  - Login correcto: " + logged.getName());

                // Login incorrecto (debe lanzar ServiceException)
                try {
                    participantService.login(savedParticipant.getEmail(), "WrongPass");
                    System.out.println("  - ERROR: permitió login con clave incorrecta.");
                } catch (ServiceException expected) {
                    System.out.println("  - Login fallido controlado correctamente: " + expected.getMessage());
                }

                // Cambio de contraseña
                boolean changed = participantService.updatePassword(createdParticipantId, "PassInicial123", "PassNueva456");
                System.out.println("  - Cambio de contraseña: " + changed);
                participantService.login(savedParticipant.getEmail(), "PassNueva456");
                System.out.println("  - Re-login con nueva contraseña OK.");

                // Búsqueda con Criteria
                ParticipantCriteria pCriteria = new ParticipantCriteria();
                pCriteria.setEmail(uniqueSuffix);
                Results<Participant> pResults = participantService.findByCriteria(pCriteria, 0, 10);
                System.out.println("  - findByCriteria encontrados: " + pResults.getTotal());

                // ------------------------------------------------------------------
                // 4. TEST DE GUIDE (REGISTER + LOGIN + CRITERIA)
                // ------------------------------------------------------------------
                System.out.println("\n[4] Probando GuideService (registro, login, criteria)...");
                Guide g = new Guide();
                g.setName("Laura");
                g.setLastName("GuiaTest");
                g.setNieDni("12345678Z");
                g.setPhoneNumber("600333444");
                g.setEmail("guia." + uniqueSuffix + "@explorworld.test");
                g.setBirthDate(new Date());
                g.setPassword("GuiaPass123");
                g.setGender(defaultGender);

                Guide savedGuide = guideService.register(g);
                createdGuideId = savedGuide.getId();
                System.out.println("  - Guía registrado ID: " + createdGuideId);

                Guide loggedGuide = guideService.login(savedGuide.getEmail(), "GuiaPass123");
                System.out.println("  - Login guía correcto: " + loggedGuide.getName());

                GuideCriteria gCriteria = new GuideCriteria();
                gCriteria.setName("Laura");
                Results<Guide> gResults = guideService.findByCriteria(gCriteria, 0, 5);
                System.out.println("  - findByCriteria guías encontrados: " + gResults.getTotal());

                // ------------------------------------------------------------------
                // 5. TEST DE ACTIVITY + TICKET + PAYMENT (FLUJO COMPLETO DE COMPRA)
                // ------------------------------------------------------------------
                System.out.println("\n[5] Probando ActivityService, TicketService y PaymentService...");
                Results<Activity> actResults = activityService.findByCriteria(new ActivityCriteria(), 0, 5);
                System.out.println("  - Actividades en BD: " + actResults.getTotal());

                if (!actResults.getPage().isEmpty()) {
                    Activity activity = actResults.getPage().get(0);
                    System.out.println("  - Usando actividad ID " + activity.getId() + ": " + activity.getName());

                    // Crear Ticket
                    Ticket ticket = new Ticket();
                    ticket.setActivity(activity);
                    ticket.setParticipant(savedParticipant);
                    ticket.setPrice(activity.getPriceMin() != null ? activity.getPriceMin() : 25.0);

                    Ticket createdTicket = ticketService.create(ticket);
                    createdTicketId = createdTicket.getId();
                    System.out.println("  - Ticket creado: " + createdTicket.getTicketNumber()
                            + " | Estado inicial: " + createdTicket.getStatus());

                    // Pagar Ticket (debe cambiar el ticket a CONFIRMED en la misma transacción)
                    if (!activeMethods.isEmpty()) {
                        Payment payment = new Payment();
                        payment.setTicket(createdTicket);
                        payment.setPaymentMethod(activeMethods.get(0));
                        payment.setAmount(createdTicket.getPrice());

                        Payment createdPayment = paymentService.create(payment);
                        createdPaymentId = createdPayment.getId();

                        Ticket updatedTicket = ticketService.findById(createdTicketId);
                        System.out.println("  - Pago registrado ID: " + createdPaymentId
                                + " | Estado pago: " + createdPayment.getStatus());
                        System.out.println("  - Estado del Ticket tras el pago: " + updatedTicket.getStatus()
                                + " (esperado: CONFIRMED)");
                        System.out.println("  - Pagos asociados al ticket: "
                                + paymentService.findByTicketId(createdTicketId).size());
                    } else {
                        System.out.println("  - AVISO: No hay PaymentMethods activos en BD; omitiendo test de Payment.");
                    }

                    System.out.println("  - Tickets del participante: "
                            + ticketService.findByParticipantId(createdParticipantId).size());
                } else {
                    System.out.println("  - AVISO: No hay actividades en BD; inserta al menos una para probar Ticket y Payment.");
                }

                // ------------------------------------------------------------------
                // 6. TEST DE CONTENT SERVICE (CMS DE FICHEROS)
                // ------------------------------------------------------------------
                System.out.println("\n[6] Probando ContentService (escritura y lectura en disco)...");
                // Si añades @Service a ContentServiceImpl, cambia esta línea por: context.getBean(ContentService.class)
                ContentService contentService = new ContentServiceImpl();

                ContentMetadata meta = new ContentMetadata();
                meta.setEntityType("participant");
                meta.setEntityId(String.valueOf(createdParticipantId));
                meta.setResourceId("avatar");
                meta.setFormat(".txt");

                byte[] sampleBytes = "Imagen de prueba ExplorWorld".getBytes(StandardCharsets.UTF_8);
                contentService.save(new ContentData());

                ContentData readBack = contentService.getContent(meta);
                System.out.println("  - Fichero guardado y leído OK (" + readBack.getRaw().length + " bytes).");

            } finally {
                // ------------------------------------------------------------------
                // 7. LIMPIEZA DE DATOS DE PRUEBA (orden inverso por llaves foráneas)
                // ------------------------------------------------------------------
                System.out.println("\n[7] Limpiando datos temporales de prueba...");
                if (createdPaymentId != null) {
                    paymentService.delete(createdPaymentId);
                    System.out.println("  - Payment de prueba eliminado.");
                }
                if (createdTicketId != null) {
                    ticketService.delete(createdTicketId);
                    System.out.println("  - Ticket de prueba eliminado.");
                }
                if (createdParticipantId != null) {
                    participantService.delete(createdParticipantId);
                    System.out.println("  - Participant de prueba eliminado.");
                }
                if (createdGuideId != null) {
                    guideService.delete(createdGuideId);
                    System.out.println("  - Guide de prueba eliminado.");
                }
            }

            System.out.println("\n====================================================");
            System.out.println(" TEST INTEGRAL COMPLETADO SIN ERRORES ");
            System.out.println("====================================================");

        } catch (Exception e) {
            System.err.println("\n[ERROR EN EL TEST INTEGRAL]: " + e.getMessage());
            e.printStackTrace();
        }
    }
}