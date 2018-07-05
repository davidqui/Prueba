package com.laamware.ejercito.doc.web.dto;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author rafar
 *
 */
public class DocumentoDTO implements Serializable{

    private static final long serialVersionUID = 8992741508072139360L;

    private String id;
    private String idInstancia;
    private String asunto;
    private Date cuandoMod;
    private String nombreProceso;
    private String nombreEstado;
    private String nombreUsuarioAsignado;
    private String nombreUsuarioEnviado;
    private String nombreUsuarioElabora;
    private String nombreUsuarioReviso;
    private String nombreUsuarioVbueno;
    private String nombreUsuarioFirma;
    private String nombreClasificacion;
    private String numeroRadicado;
    private String unidadOrigen;
    private String unidadDestino;
    private Boolean perteneceDocumento;

    public DocumentoDTO() {
        // TODO Auto-generated constructor stub
    }

    public DocumentoDTO(String id, String idInstancia, String asunto, Date cuandoMod, String nombreProceso, String nombreEstado, String nombreUsuarioAsignado, String nombreUsuarioEnviado, String nombreUsuarioElabora, String nombreUsuarioReviso, String nombreUsuarioVbueno, String nombreUsuarioFirma, String nombreClasificacion, String numeroRadicado, String unidadOrigen, String unidadDestino, Boolean perteneceDocumento) {
        this.id = id;
        this.idInstancia = idInstancia;
        this.asunto = asunto;
        this.cuandoMod = cuandoMod;
        this.nombreProceso = nombreProceso;
        this.nombreEstado = nombreEstado;
        this.nombreUsuarioAsignado = nombreUsuarioAsignado;
        this.nombreUsuarioEnviado = nombreUsuarioEnviado;
        this.nombreUsuarioElabora = nombreUsuarioElabora;
        this.nombreUsuarioReviso = nombreUsuarioReviso;
        this.nombreUsuarioVbueno = nombreUsuarioVbueno;
        this.nombreUsuarioFirma = nombreUsuarioFirma;
        this.nombreClasificacion = nombreClasificacion;
        this.numeroRadicado = numeroRadicado;
        this.unidadOrigen = unidadOrigen;
        this.unidadDestino = unidadDestino;
        this.perteneceDocumento = perteneceDocumento;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getIdInstancia() {
        return idInstancia;
    }

    public void setIdInstancia(String idInstancia) {
        this.idInstancia = idInstancia;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public Date getCuandoMod() {
        return cuandoMod;
    }

    public void setCuandoMod(Date cuandoMod) {
        this.cuandoMod = cuandoMod;
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

    public String getNombreUsuarioAsignado() {
        return nombreUsuarioAsignado;
    }

    public void setNombreUsuarioAsignado(String nombreUsuarioAsignado) {
        this.nombreUsuarioAsignado = nombreUsuarioAsignado;
    }

    public String getNombreUsuarioEnviado() {
        return nombreUsuarioEnviado;
    }

    public void setNombreUsuarioEnviado(String nombreUsuarioEnviado) {
        this.nombreUsuarioEnviado = nombreUsuarioEnviado;
    }

    public String getNombreUsuarioElabora() {
        return nombreUsuarioElabora;
    }

    public void setNombreUsuarioElabora(String nombreUsuarioElabora) {
        this.nombreUsuarioElabora = nombreUsuarioElabora;
    }

    public String getNombreUsuarioReviso() {
        return nombreUsuarioReviso;
    }

    public void setNombreUsuarioReviso(String nombreUsuarioReviso) {
        this.nombreUsuarioReviso = nombreUsuarioReviso;
    }

    public String getNombreUsuarioVbueno() {
        return nombreUsuarioVbueno;
    }

    public void setNombreUsuarioVbueno(String nombreUsuarioVbueno) {
        this.nombreUsuarioVbueno = nombreUsuarioVbueno;
    }

    public String getNombreUsuarioFirma() {
        return nombreUsuarioFirma;
    }

    public void setNombreUsuarioFirma(String nombreUsuarioFirma) {
        this.nombreUsuarioFirma = nombreUsuarioFirma;
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

    public Boolean getPerteneceDocumento() {
        return perteneceDocumento;
    }

    public void setPerteneceDocumento(Boolean perteneceDocumento) {
        this.perteneceDocumento = perteneceDocumento;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        return id.equals(((DocumentoDTO) obj).getId());
    }
}
