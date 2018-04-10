package com.laamware.ejercito.doc.web.util;

/**
 * Excepción para las reglas de negocio del sistema.
 *
 * @author Jorge Alfonso García Espinosa
 * @since 1.8
 * @version 04/10/2018 (SICDI-Controltech Issue #156)
 */
public class BusinessLogicException extends Exception {

    private static final long serialVersionUID = -5775516340135003818L;

    /**
     * Constructor.
     *
     * @param message Mensaje.
     */
    public BusinessLogicException(String message) {
        super(message);
    }

    /**
     * Constructor.
     *
     * @param message Mensaje.
     * @param cause Error o excepción original.
     */
    public BusinessLogicException(String message, Throwable cause) {
        super(message, cause);
    }

}
