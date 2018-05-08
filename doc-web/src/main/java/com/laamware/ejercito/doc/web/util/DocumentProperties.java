package com.laamware.ejercito.doc.web.util;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Mapa de propiedades aplicables a un documento ASPOSE.
 *
 * @author Jorge Alfonso García Espinosa
 * @since 1.8
 * @version 05/07/2018 Issue #160 (SICDI-Controltech) feature-160.
 */
public final class DocumentProperties implements Serializable {

    /**
     * Propiedades permitidas para el documento.
     */
    public enum Property {
        /**
         * Autor.
         */
        Author,
        /**
         * Comentarios.
         */
        Comments,
        /**
         * Fecha de creación.
         */
        CreatedTime,
        /**
         * Palabras clave.
         */
        Keywords,
        /**
         * Último usuario en guardar.
         */
        LastSavedBy,
        /**
         * Fecha de último guardado.
         */
        LastSavedTime,
        /**
         * Nombre de la aplicación.
         */
        NameOfApplication,
        /**
         * Asunto.
         */
        Subject,
        /**
         * Título.
         */
        Title
    }

    private static final long serialVersionUID = -8205554580306900147L;

    private final Map<Property, Object> properties = new LinkedHashMap<>();

    /**
     * Obtiene el valor de una propiedad.
     *
     * @param property Propiedad.
     * @return Valor de la propiedad.
     */
    public Object getValue(Property property) {
        return properties.get(property);
    }

    /**
     * Coloca el valor a una propiedad.
     *
     * @param property Propiedad.
     * @param value Valor.
     */
    public void putPropertyValue(Property property, Object value) {
        properties.put(property, value);
    }

}
