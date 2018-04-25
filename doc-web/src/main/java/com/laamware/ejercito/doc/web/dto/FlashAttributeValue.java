package com.laamware.ejercito.doc.web.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Clase que controla los mensajes de notificación cuando se realiza una asignación
 * de un usuario en un documento
 * @author Jorge Alfonso García Espinosa
 * @since 1.8
 * @version 04/25/2018 (SICDI-Controltech Issue #156 feature-156)
 */
public class FlashAttributeValue implements Serializable {

    private final String mainMessage;

    private final String altMessagesTitle;

    private final List<String> altMessagesList = new ArrayList<>();

    public FlashAttributeValue(String mainMessage, String altMessagesTitle) {
        this.mainMessage = mainMessage;
        this.altMessagesTitle = altMessagesTitle;
    }

    public String getMainMessage() {
        return mainMessage;
    }

    public String getAltMessagesTitle() {
        return altMessagesTitle;
    }

    public int getAltMessagesSize() {
        return altMessagesList.size();
    }

    public boolean isAltMessagesEmpty() {
        return altMessagesList.isEmpty();
    }

    public boolean addAltMessage(final String altMessage) {
        return altMessagesList.add(altMessage);
    }

    public String getAltMessages(int index) {
        return altMessagesList.get(index);
    }

    public Collection<String> getAllAltMessages() {
        return altMessagesList;
    }

}
