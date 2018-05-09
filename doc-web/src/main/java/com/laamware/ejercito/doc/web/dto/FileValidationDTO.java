package com.laamware.ejercito.doc.web.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * DTO para respuesta del proceso de validación de archivo.
 *
 * @author Jorge Alfonso García Espinosa
 * @since 1.8
 * @version 05/09/2018 Issue #160 (SICDI-Controltech) feature-160
 */
public final class FileValidationDTO implements Serializable {

    private static final long serialVersionUID = 2232820886953468449L;

    private boolean valid;

    private String message;

    private String procesoInstanciaID;

    private String documentoID;

    private String asuntoDocumento;

    private String nombreUsuarioCreador;

    private Date fechaCreacion;

    private String nombreUsuarioFirmaYEnvio;

    private Date fechaFirmaEnvio;

    private String nombreClasificacion;

    private String numeroRadicado;

    private String unidadOrigen;

    private String unidadDestino;

    private String nombreProceso;

    private String nombreEstado;

    /**
     * Constructor vacío.
     */
    public FileValidationDTO() {
    }

    /**
     * Constructor.
     *
     * @param valid Indica si el resultado de validación es correcta
     * ({@code true}) o no ({@code false}).
     * @param message Mensaje del proceso de validación.
     */
    public FileValidationDTO(boolean valid, String message) {
        this.valid = valid;
        this.message = message;
    }

    /**
     * Constructor.
     *
     * @param valid Indica si el resultado de validación es correcta
     * ({@code true}) o no ({@code false}).
     * @param procesoInstanciaID ID de la instancia del proceso.
     * @param documentoID ID del documento.
     * @param message Mensaje del proceso de validación.
     * @param asuntoDocumento Asunto del documento.
     * @param nombreUsuarioCreador Nombre del usuario creador del documento.
     * @param fechaCreacion Fecha de creación del documento.
     * @param nombreUsuarioFirmaYEnvio Nombre del usuario
     * @param fechaFirmaEnvio Fecha de firma y envío del documento.
     * @param nombreClasificacion Nombre de la clasificación.
     * @param numeroRadicado Número de radicado.
     * @param dependenciaOrigen Dependencia origen.
     * @param dependenciaDestino Dependencia destino.
     * @param nombreProceso Nombre del proceso.
     * @param nombreEstado Estado del documento.
     */
    public FileValidationDTO(boolean valid, String message, String procesoInstanciaID, String documentoID, String asuntoDocumento,
            String nombreUsuarioCreador, Date fechaCreacion, String nombreUsuarioFirmaYEnvio, Date fechaFirmaEnvio, String nombreClasificacion,
            String numeroRadicado, String dependenciaOrigen, String dependenciaDestino, String nombreProceso, String nombreEstado) {
        this.valid = valid;
        this.message = message;
        this.procesoInstanciaID = procesoInstanciaID;
        this.documentoID = documentoID;
        this.asuntoDocumento = asuntoDocumento;
        this.nombreUsuarioCreador = nombreUsuarioCreador;
        this.fechaCreacion = (fechaCreacion == null) ? null : new Date(fechaCreacion.getTime());
        this.nombreUsuarioFirmaYEnvio = nombreUsuarioFirmaYEnvio;
        this.fechaFirmaEnvio = (fechaFirmaEnvio == null) ? null : new Date(fechaFirmaEnvio.getTime());
        this.nombreClasificacion = nombreClasificacion;
        this.numeroRadicado = numeroRadicado;
        this.unidadOrigen = dependenciaOrigen;
        this.unidadDestino = dependenciaDestino;
        this.nombreProceso = nombreProceso;
        this.nombreEstado = nombreEstado;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getProcesoInstanciaID() {
        return procesoInstanciaID;
    }

    public void setProcesoInstanciaID(String procesoInstanciaID) {
        this.procesoInstanciaID = procesoInstanciaID;
    }

    public String getDocumentoID() {
        return documentoID;
    }

    public void setDocumentoID(String documentoID) {
        this.documentoID = documentoID;
    }

    public String getAsuntoDocumento() {
        return asuntoDocumento;
    }

    public void setAsuntoDocumento(String asuntoDocumento) {
        this.asuntoDocumento = asuntoDocumento;
    }

    public String getNombreUsuarioCreador() {
        return nombreUsuarioCreador;
    }

    public void setNombreUsuarioCreador(String nombreUsuarioCreador) {
        this.nombreUsuarioCreador = nombreUsuarioCreador;
    }

    public Date getFechaCreacion() {
        return (fechaCreacion == null) ? null : new Date(fechaCreacion.getTime());
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = (fechaCreacion == null) ? null : new Date(fechaCreacion.getTime());
    }

    public String getNombreUsuarioFirmaYEnvio() {
        return nombreUsuarioFirmaYEnvio;
    }

    public void setNombreUsuarioFirmaYEnvio(String nombreUsuarioFirmaYEnvio) {
        this.nombreUsuarioFirmaYEnvio = nombreUsuarioFirmaYEnvio;
    }

    public Date getFechaFirmaEnvio() {
        return (fechaFirmaEnvio == null) ? null : new Date(fechaFirmaEnvio.getTime());
    }

    public void setFechaFirmaEnvio(Date fechaFirmaEnvio) {
        this.fechaFirmaEnvio = (fechaFirmaEnvio == null) ? null : new Date(fechaFirmaEnvio.getTime());
    }

    public String getNombreClasificacion() {
        return nombreClasificacion;
    }

    public void setNombreClasificacion(String nombreClasificacion) {
        this.nombreClasificacion = nombreClasificacion;
    }

    public String getNumeroRadicado() {
        return numeroRadicado;
    }

    public void setNumeroRadicado(String numeroRadicado) {
        this.numeroRadicado = numeroRadicado;
    }

    public String getUnidadOrigen() {
        return unidadOrigen;
    }

    public void setUnidadOrigen(String unidadOrigen) {
        this.unidadOrigen = unidadOrigen;
    }

    public String getUnidadDestino() {
        return unidadDestino;
    }

    public void setUnidadDestino(String unidadDestino) {
        this.unidadDestino = unidadDestino;
    }

    public String getNombreProceso() {
        return nombreProceso;
    }

    public void setNombreProceso(String nombreProceso) {
        this.nombreProceso = nombreProceso;
    }

    public String getNombreEstado() {
        return nombreEstado;
    }

    public void setNombreEstado(String nombreEstado) {
        this.nombreEstado = nombreEstado;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 31 * hash + (this.valid ? 1 : 0);
        hash = 31 * hash + Objects.hashCode(this.message);
        hash = 31 * hash + Objects.hashCode(this.procesoInstanciaID);
        hash = 31 * hash + Objects.hashCode(this.documentoID);
        hash = 31 * hash + Objects.hashCode(this.asuntoDocumento);
        hash = 31 * hash + Objects.hashCode(this.nombreUsuarioCreador);
        hash = 31 * hash + Objects.hashCode(this.fechaCreacion);
        hash = 31 * hash + Objects.hashCode(this.nombreUsuarioFirmaYEnvio);
        hash = 31 * hash + Objects.hashCode(this.fechaFirmaEnvio);
        hash = 31 * hash + Objects.hashCode(this.nombreClasificacion);
        hash = 31 * hash + Objects.hashCode(this.numeroRadicado);
        hash = 31 * hash + Objects.hashCode(this.unidadOrigen);
        hash = 31 * hash + Objects.hashCode(this.unidadDestino);
        hash = 31 * hash + Objects.hashCode(this.nombreProceso);
        hash = 31 * hash + Objects.hashCode(this.nombreEstado);
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
        final FileValidationDTO other = (FileValidationDTO) obj;
        if (this.valid != other.valid) {
            return false;
        }
        if (!Objects.equals(this.message, other.message)) {
            return false;
        }
        if (!Objects.equals(this.procesoInstanciaID, other.procesoInstanciaID)) {
            return false;
        }
        if (!Objects.equals(this.documentoID, other.documentoID)) {
            return false;
        }
        if (!Objects.equals(this.asuntoDocumento, other.asuntoDocumento)) {
            return false;
        }
        if (!Objects.equals(this.nombreUsuarioCreador, other.nombreUsuarioCreador)) {
            return false;
        }
        if (!Objects.equals(this.nombreUsuarioFirmaYEnvio, other.nombreUsuarioFirmaYEnvio)) {
            return false;
        }
        if (!Objects.equals(this.nombreClasificacion, other.nombreClasificacion)) {
            return false;
        }
        if (!Objects.equals(this.numeroRadicado, other.numeroRadicado)) {
            return false;
        }
        if (!Objects.equals(this.unidadOrigen, other.unidadOrigen)) {
            return false;
        }
        if (!Objects.equals(this.unidadDestino, other.unidadDestino)) {
            return false;
        }
        if (!Objects.equals(this.nombreProceso, other.nombreProceso)) {
            return false;
        }
        if (!Objects.equals(this.nombreEstado, other.nombreEstado)) {
            return false;
        }
        if (!Objects.equals(this.fechaCreacion, other.fechaCreacion)) {
            return false;
        }
        return Objects.equals(this.fechaFirmaEnvio, other.fechaFirmaEnvio);
    }

    @Override
    public String toString() {
        return "FileValidationDTO{" + "valid=" + valid + ", message=" + message + ", procesoInstanciaID=" + procesoInstanciaID + ", documentoID=" + documentoID + ", asuntoDocumento=" + asuntoDocumento + ", nombreUsuarioCreador=" + nombreUsuarioCreador + ", fechaCreacion=" + fechaCreacion + ", nombreUsuarioFirmaYEnvio=" + nombreUsuarioFirmaYEnvio + ", fechaFirmaEnvio=" + fechaFirmaEnvio + ", nombreClasificacion=" + nombreClasificacion + ", numeroRadicado=" + numeroRadicado + ", unidadOrigen=" + unidadOrigen + ", unidadDestino=" + unidadDestino + ", nombreProceso=" + nombreProceso + ", nombreEstado=" + nombreEstado + '}';
    }

}
