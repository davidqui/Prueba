package com.laamware.ejercito.doc.web.util;

/**
 * Excepción para operaciones de reflection.
 *
 * @author jgarcia@controltechcg.com
 * @since 1.8
 * @version 05/25/2018 Issue #172 (SICDI-Controltech) feature-172
 */
public class ReflectionException extends Exception {

    private static final long serialVersionUID = 1198864723353579913L;

    public ReflectionException(String message) {
        super(message);
    }

    public ReflectionException(String message, Throwable cause) {
        super(message, cause);
    }

}
