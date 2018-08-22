
package com.laamware.ejercito.doc.web.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author egonzalezm
 * @since 1.8
 * @version 08/08/2018
 */
public class DocumentoExpDTO implements Serializable{

    private static final long serialVersionUID = 3309958267862305258L;
    
    private String pinId;
    private String asunto;
    private String radicado;
    private String clasificacion;
    private boolean indVisualizacion;
    private boolean indJefeDependencia;
    private String docId;

    public DocumentoExpDTO() {
    }

    public DocumentoExpDTO(String pinId, String asunto, String radicado, String clasificacion, boolean indVisualizacion, boolean indJefeDependencia, String docId) {
        this.pinId = pinId;
        this.asunto = asunto;
        this.radicado = radicado;
        this.clasificacion = clasificacion;
        this.indVisualizacion = indVisualizacion;
        this.indJefeDependencia = indJefeDependencia;
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

    public String getRadicado() {
        return radicado;
    }

    public void setRadicado(String radicado) {
        this.radicado = radicado;
    }

    public String getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(String clasificacion) {
        this.clasificacion = clasificacion;
    }

    public boolean isIndVisualizacion() {
        return indVisualizacion;
    }

    public void setIndVisualizacion(boolean indVisualizacion) {
        this.indVisualizacion = indVisualizacion;
    }

    public boolean isIndJefeDependencia() {
        return indJefeDependencia;
    }

    public void setIndJefeDependencia(boolean indJefeDependencia) {
        this.indJefeDependencia = indJefeDependencia;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.pinId);
        hash = 37 * hash + Objects.hashCode(this.asunto);
        hash = 37 * hash + Objects.hashCode(this.radicado);
        hash = 37 * hash + Objects.hashCode(this.clasificacion);
        hash = 37 * hash + (this.indVisualizacion ? 1 : 0);
        hash = 37 * hash + (this.indJefeDependencia ? 1 : 0);
        hash = 37 * hash + Objects.hashCode(this.docId);
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
        final DocumentoExpDTO other = (DocumentoExpDTO) obj;
        if (this.indVisualizacion != other.indVisualizacion) {
            return false;
        }
        if (this.indJefeDependencia != other.indJefeDependencia) {
            return false;
        }
        if (!Objects.equals(this.pinId, other.pinId)) {
            return false;
        }
        if (!Objects.equals(this.asunto, other.asunto)) {
            return false;
        }
        if (!Objects.equals(this.radicado, other.radicado)) {
            return false;
        }
        if (!Objects.equals(this.clasificacion, other.clasificacion)) {
            return false;
        }
        if (!Objects.equals(this.docId, other.docId)) {
            return false;
        }
        return true;
    }
}
