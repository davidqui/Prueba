package com.laamware.ejercito.doc.web.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * DTO para paso de información de documento acta desde el formulario.
 *
 * @author jgarcia@controltechcg.com
 * @since 1.8
 * @version 05/15/2018 Issue #162 (SICDI-Controltech) feature-162.
 */
public class DocumentoActaDTO implements Serializable {

    private static final long serialVersionUID = 3642511734616108819L;

    private String docId;
    private String pinId;
    private String asunto;
    private String actaLugar;
    private String actaFechaElaboracion;
    private String clasificacion;
    private String numeroFolios;
    private String trd;
    private String cargoElabora;

    public DocumentoActaDTO() {
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getPinId() {
        return pinId;
    }

    public void setPinId(String pinId) {
        this.pinId = pinId;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getActaLugar() {
        return actaLugar;
    }

    public void setActaLugar(String actaLugar) {
        this.actaLugar = actaLugar;
    }

    public String getActaFechaElaboracion() {
        return actaFechaElaboracion;
    }

    public void setActaFechaElaboracion(String actaFechaElaboracion) {
        this.actaFechaElaboracion = actaFechaElaboracion;
    }

    public String getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(String clasificacion) {
        this.clasificacion = clasificacion;
    }

    public String getNumeroFolios() {
        return numeroFolios;
    }

    public void setNumeroFolios(String numeroFolios) {
        this.numeroFolios = numeroFolios;
    }

    public String getTrd() {
        return trd;
    }

    public void setTrd(String trd) {
        this.trd = trd;
    }

    public String getCargoElabora() {
        return cargoElabora;
    }

    public void setCargoElabora(String cargoElabora) {
        this.cargoElabora = cargoElabora;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.docId);
        hash = 97 * hash + Objects.hashCode(this.pinId);
        hash = 97 * hash + Objects.hashCode(this.asunto);
        hash = 97 * hash + Objects.hashCode(this.actaLugar);
        hash = 97 * hash + Objects.hashCode(this.actaFechaElaboracion);
        hash = 97 * hash + Objects.hashCode(this.clasificacion);
        hash = 97 * hash + Objects.hashCode(this.numeroFolios);
        hash = 97 * hash + Objects.hashCode(this.trd);
        hash = 97 * hash + Objects.hashCode(this.cargoElabora);
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
        final DocumentoActaDTO other = (DocumentoActaDTO) obj;
        if (!Objects.equals(this.docId, other.docId)) {
            return false;
        }
        if (!Objects.equals(this.pinId, other.pinId)) {
            return false;
        }
        if (!Objects.equals(this.asunto, other.asunto)) {
            return false;
        }
        if (!Objects.equals(this.actaLugar, other.actaLugar)) {
            return false;
        }
        if (!Objects.equals(this.actaFechaElaboracion, other.actaFechaElaboracion)) {
            return false;
        }
        if (!Objects.equals(this.clasificacion, other.clasificacion)) {
            return false;
        }
        if (!Objects.equals(this.numeroFolios, other.numeroFolios)) {
            return false;
        }
        if (!Objects.equals(this.trd, other.trd)) {
            return false;
        }
        return Objects.equals(this.cargoElabora, other.cargoElabora);
    }

    @Override
    public String toString() {
        return "DocumentoActaDTO{" + "docId=" + docId + ", pinId=" + pinId + ", asunto=" + asunto + ", actaLugar=" + actaLugar + ", actaFechaElaboracion=" + actaFechaElaboracion + ", clasificacion=" + clasificacion + ", numeroFolios=" + numeroFolios + ", trd=" + trd + ", cargoElabora=" + cargoElabora + '}';
    }

}
