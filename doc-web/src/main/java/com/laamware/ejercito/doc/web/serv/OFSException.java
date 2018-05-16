package com.laamware.ejercito.doc.web.serv;

/**
 * Excepción para procesos del {@link OFS}
 *
 * @author Jorge Alfonso García Espinosa
 * @since 1.8
 * @version 04/17/2018 (SICDI-Controltech Issue #156 feature-156)
 */
public class OFSException extends Exception {

    private static final long serialVersionUID = -5793045481539809863L;

    /**
     * Constructor.
     *
     * @param message Mensaje.
     */
    public OFSException(String message) {
        super(message);
    }

    /**
     * Constructor.
     *
     * @param message Mensaje.
     * @param cause Error/Excepción causante.
     */
    public OFSException(String message, Throwable cause) {
        super(message, cause);
    }

}
