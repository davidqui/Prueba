package com.laamware.ejercito.doc.web.util;

import java.util.Locale;

/**
 * Variables generales del sistema (Que no se definen en el archivo de
 * propiedades, por lo que son constantes para la aplicación.).
 *
 * @author Jorge Alfonso García Espinosa
 * @since 1.8
 * @version 05/07/2018 Issue #160 (SICDI-Controltech) feature-160.
 */
public final class Global {

    /**
     * Locale para Colombia.
     */
    public static final Locale COLOMBIA = new Locale("es", "CO");

    /**
     * Nombre de la aplicación.
     */
    public static final String APPLICATION_NAME = "SICDI";

    /**
     * Construcor privado.
     */
    private Global() {
    }

}
