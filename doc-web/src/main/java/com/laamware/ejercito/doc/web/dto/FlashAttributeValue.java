package com.laamware.ejercito.doc.web.dto;

import java.io.Serializable;

/**
 * Clase que controla los mensajes de notificación cuando se realiza una asignación
 * de un usuario en un documento
 * @author Jorge Alfonso García Espinosa
 * @since 1.8
 * @version 04/25/2018 (SICDI-Controltech Issue #156 feature-156)
 */
public class FlashAttributeValue implements Serializable {

    private final String mainMessage;

    private final String altMessageMultidestino;
    
    private final Boolean multidestino;
    
    private final Integer numRecords;

    public FlashAttributeValue(String mainMessage, String altMessageMultidestino,Boolean multidestino, Integer numRecords) {
        this.mainMessage = mainMessage;
        this.altMessageMultidestino = altMessageMultidestino;
        this.multidestino = multidestino;
        this.numRecords = numRecords;
    }

    public String getMainMessage() {
        return mainMessage;
    }

    public String getAltMessageMultidestino() {
        return altMessageMultidestino;
    }

    public Boolean getMultidestino() {
        return multidestino;
    }

    public Integer getNumRecords() {
        return numRecords;
    }
}
