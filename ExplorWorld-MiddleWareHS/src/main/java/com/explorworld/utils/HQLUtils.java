package com.explorworld.utils;

import java.util.List;
import java.util.Map;

public class HQLUtils {

    /**
     * Evalúa si un parámetro de búsqueda tiene valor.
     * Si no es nulo (y no es un String vacío), agrega el fragmento HQL y guarda el parámetro nombrado.
     */
    public static void addClause(Object criteriaValue, List<String> conditions, String hqlCondition, 
                                 Map<String, Object> params, String paramName, Object paramValue) {
        if (criteriaValue == null) {
            return;
        }
        if (criteriaValue instanceof String && ((String) criteriaValue).trim().isEmpty()) {
            return;
        }
        conditions.add(hqlCondition);
        params.put(paramName, paramValue);
    }

    /**
     * Genera el comodín para búsquedas LIKE insensibles a mayúsculas/minúsculas.
     */
    public static String like(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return "%" + value.trim().toUpperCase() + "%";
    }
    
}