package com.laamware.ejercito.doc.web.util;

import java.io.Serializable;
import java.lang.reflect.Field;
import javax.persistence.Column;

/**
 * Utilitario para funciones de reflection.
 *
 * @author jgarcia@controltechcg.com
 * @since 1.8
 * @version 05/25/2018 Issue #172 (SICDI-Controltech) feature-172
 */
public final class ReflectionUtil {

    private ReflectionUtil() {
    }

    /**
     * Obtiene la longitud establecida en la anotación {@link Column} del campo
     * indicado para la entidad.
     *
     * @param clazz Clase de la entidad.
     * @param fieldName Nombre del campo.
     * @return Longitud del campo.
     * @throws ReflectionException En caso que no exista el campo en la clase, o
     * este no tenga la anotación {@link Column}.
     */
    public static int getColumnLength(final Class<? extends Serializable> clazz, final String fieldName) throws ReflectionException {
        try {
            final Field field = clazz.getDeclaredField(fieldName);
            final Column column = field.getAnnotation(Column.class);
            return column.length();
        } catch (NoSuchFieldException | SecurityException ex) {
            throw new ReflectionException(clazz.getCanonicalName() + " " + fieldName, ex);
        }
    }

}
