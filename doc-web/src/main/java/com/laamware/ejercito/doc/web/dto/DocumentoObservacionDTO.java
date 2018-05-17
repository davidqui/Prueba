package com.laamware.ejercito.doc.web.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * DTO para observaciones de documentos.
 *
 * @author jgarcia@controltechcg.com
 * @since 1.8
 * @version 05/17/2018 Issue #162 (SICDI-Controltech) feature-162.
 */
public final class DocumentoObservacionDTO implements Serializable {

    private static final long serialVersionUID = -440371555911688422L;

    private String documentoID;

    private String observacionTexto;

    /**
     * Constructor vacío.
     */
    public DocumentoObservacionDTO() {
    }

    /**
     * Constructor.
     *
     * @param documentoID ID del documento.
     * @param observacion Observación a registrar.
     */
    public DocumentoObservacionDTO(String documentoID, String observacion) {
        this.documentoID = documentoID;
        this.observacionTexto = observacion;
    }

    public String getDocumentoID() {
        return documentoID;
    }

    public void setDocumentoID(String documentoID) {
        this.documentoID = documentoID;
    }

    public String getObservacionTexto() {
        return observacionTexto;
    }

    public void setObservacionTexto(String observacionTexto) {
        this.observacionTexto = observacionTexto;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.documentoID);
        hash = 23 * hash + Objects.hashCode(this.observacionTexto);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DocumentoObservacionDTO other = (DocumentoObservacionDTO) obj;
        if (!Objects.equals(this.documentoID, other.documentoID)) {
            return false;
        }
        return Objects.equals(this.observacionTexto, other.observacionTexto);
    }

    @Override
    public String toString() {
        return "DocumentoObservacionDTO{" + "documentoID=" + documentoID + ", observacion=" + observacionTexto + '}';
    }

}
