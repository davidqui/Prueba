package com.laamware.ejercito.doc.web.util;

/**
 * Utilitario para información del sistema.
 *
 * @author jgarcia@controltechcg.com
 * @since Sep 14, 2017.
 */
public final class SystemUtils {

    private SystemUtils() {
    }

    /**
     * Indica si el sistema operativo en el que se está ejecutando la aplicación
     * es una versión de Microsoft Windows. Esta evaluación se hace para evitar
     * inconvenientes en ambientes de desarrollo que utilizan este sistema
     * operativo.
     *
     * @return {@code true} si el sistema operativo es una versión de Microsoft
     * Windows; de lo contrario, {@code false}.
     */
    public static boolean isWindowsOS() {
        final String osName = System.getProperty("os.name");
        return osName.toLowerCase().contains("windows");
    }
}
