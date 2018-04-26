package com.laamware.ejercito.doc.web.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * DTO para la información de los documentos archivados.
 *
 * @author Jorge Alfonso García Espinosa
 * @since 1.8
 * @version 04/25/2018 Issue #151 (SICDI-Controltech) feature-151
 */
public class DocumentoDependenciaArchivoDTO implements Serializable {

    private static final long serialVersionUID = 6231543997660670439L;

    private String numeroRadicado;
    private String procesoInstanciaID;
    private String documentoAsunto;
    private Date fechaCreacionDocumento;
    private Date fechaCreacionArchivo;
    private String cargoNombre;

    public DocumentoDependenciaArchivoDTO() {
    }

    public DocumentoDependenciaArchivoDTO(String numeroRadicado, String procesoInstanciaID, String documentoAsunto, Date fechaCreacionDocumento, Date fechaCreacionArchivo, String cargoNombre) {
        this.numeroRadicado = numeroRadicado;
        this.procesoInstanciaID = procesoInstanciaID;
        this.documentoAsunto = documentoAsunto;
        this.fechaCreacionDocumento = fechaCreacionDocumento == null ? null : new Date(fechaCreacionDocumento.getTime());
        this.fechaCreacionArchivo = fechaCreacionArchivo == null ? null : new Date(fechaCreacionArchivo.getTime());
        this.cargoNombre = cargoNombre;
    }

    public String getNumeroRadicado() {
        return numeroRadicado;
    }

    public void setNumeroRadicado(String numeroRadicado) {
        this.numeroRadicado = numeroRadicado;
    }

    public String getProcesoInstanciaID() {
        return procesoInstanciaID;
    }

    public void setProcesoInstanciaID(String procesoInstanciaID) {
        this.procesoInstanciaID = procesoInstanciaID;
    }

    public String getDocumentoAsunto() {
        return documentoAsunto;
    }

    public void setDocumentoAsunto(String documentoAsunto) {
        this.documentoAsunto = documentoAsunto;
    }

    public Date getFechaCreacionDocumento() {
        return fechaCreacionDocumento == null ? null : new Date(fechaCreacionDocumento.getTime());
    }

    public void setFechaCreacionDocumento(Date fechaCreacionDocumento) {
        this.fechaCreacionDocumento = fechaCreacionDocumento == null ? null : new Date(fechaCreacionDocumento.getTime());
    }

    public Date getFechaCreacionArchivo() {
        return fechaCreacionArchivo == null ? null : new Date(fechaCreacionArchivo.getTime());
    }

    public void setFechaCreacionArchivo(Date fechaCreacionArchivo) {
        this.fechaCreacionArchivo = fechaCreacionArchivo == null ? null : new Date(fechaCreacionArchivo.getTime());
    }

    public String getCargoNombre() {
        return cargoNombre;
    }

    public void setCargoNombre(String cargoNombre) {
        this.cargoNombre = cargoNombre;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.numeroRadicado);
        hash = 59 * hash + Objects.hashCode(this.procesoInstanciaID);
        hash = 59 * hash + Objects.hashCode(this.documentoAsunto);
        hash = 59 * hash + Objects.hashCode(this.fechaCreacionDocumento);
        hash = 59 * hash + Objects.hashCode(this.fechaCreacionArchivo);
        hash = 59 * hash + Objects.hashCode(this.cargoNombre);
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
        final DocumentoDependenciaArchivoDTO other = (DocumentoDependenciaArchivoDTO) obj;
        if (!Objects.equals(this.numeroRadicado, other.numeroRadicado)) {
            return false;
        }
        if (!Objects.equals(this.procesoInstanciaID, other.procesoInstanciaID)) {
            return false;
        }
        if (!Objects.equals(this.documentoAsunto, other.documentoAsunto)) {
            return false;
        }
        if (!Objects.equals(this.cargoNombre, other.cargoNombre)) {
            return false;
        }
        if (!Objects.equals(this.fechaCreacionDocumento, other.fechaCreacionDocumento)) {
            return false;
        }
        return Objects.equals(this.fechaCreacionArchivo, other.fechaCreacionArchivo);
    }

    @Override
    public String toString() {
        return "DocumentoDependenciaArchivoDTO{" + "numeroRadicado=" + numeroRadicado + ", procesoInstanciaID=" + procesoInstanciaID + ", documentoAsunto=" + documentoAsunto + ", fechaCreacionDocumento=" + fechaCreacionDocumento + ", fechaCreacionArchivo=" + fechaCreacionArchivo + ", cargoNombre=" + cargoNombre + '}';
    }

}
