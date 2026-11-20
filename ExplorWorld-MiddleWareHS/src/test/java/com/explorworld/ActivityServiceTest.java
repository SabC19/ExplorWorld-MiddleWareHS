package com.explorworld;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.explorworld.config.AppConfig;
import com.explorworld.model.Activity;
import com.explorworld.model.Results;
import com.explorworld.model.criteria.ActivityCriteria;
import com.explorworld.service.ActivityService;

public class ActivityServiceTest {

    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            ActivityService activityService = context.getBean(ActivityService.class);

            // 1. Probar findById
            Activity a = activityService.findById(1L);
            System.out.println("Actividad 1: " + (a != null ? a.getName() : "No encontrada"));

            // 2. Probar búsqueda paginada
            ActivityCriteria criteria = new ActivityCriteria();
            criteria.setName("Ruta");
            Results<Activity> results = activityService.findByCriteria(criteria, 0, 5);
            System.out.println("Total encontradas: " + results.getTotal());
            for (Activity act : results.getPage()) {
                System.out.println(" - " + act.getId() + ": " + act.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}