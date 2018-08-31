
package com.laamware.ejercito.doc.web.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author egonzalezm
 * @since 1.8
 * @version 08/31/2018
 */
public class TransferenciaArchivoDTO implements Serializable{

    private static final long serialVersionUID = -9074865635471580820L;
    
    private Integer tarId;
    private Boolean activo;
    private String estado;
    private Date fechaCreacion;
    private Integer usuIdOrigen;
    private String usuNomOrigen;
    private Integer carIdOrigen;
    private String carNomOrigen;
    private Integer usuIdDestino;
    private String usuNomDestino;
    private Integer carIdDestino;
    private String carNomDestino;
    private Integer depId;
    private Integer usuIdJefe;
    private String justificacion;
    private Integer usuarioAsignado;
    private Boolean indAprobado;
    private Integer numDocumentos;
    private Integer numExpedientes;
    private String ultEstado;
    private Boolean esUsuarioOrigen;
    private Boolean esJefe;
    private Boolean esUsuarioDestino;

    public Integer getTarId() {
        return tarId;
    }

    public void setTarId(Integer tarId) {
        this.tarId = tarId;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Integer getUsuIdOrigen() {
        return usuIdOrigen;
    }

    public void setUsuIdOrigen(Integer usuIdOrigen) {
        this.usuIdOrigen = usuIdOrigen;
    }

    public String getUsuNomOrigen() {
        return usuNomOrigen;
    }

    public void setUsuNomOrigen(String usuNomOrigen) {
        this.usuNomOrigen = usuNomOrigen;
    }

    public Integer getCarIdOrigen() {
        return carIdOrigen;
    }

    public void setCarIdOrigen(Integer carIdOrigen) {
        this.carIdOrigen = carIdOrigen;
    }

    public String getCarNomOrigen() {
        return carNomOrigen;
    }

    public void setCarNomOrigen(String carNomOrigen) {
        this.carNomOrigen = carNomOrigen;
    }

    public Integer getUsuIdDestino() {
        return usuIdDestino;
    }

    public void setUsuIdDestino(Integer usuIdDestino) {
        this.usuIdDestino = usuIdDestino;
    }

    public String getUsuNomDestino() {
        return usuNomDestino;
    }

    public void setUsuNomDestino(String usuNomDestino) {
        this.usuNomDestino = usuNomDestino;
    }

    public Integer getCarIdDestino() {
        return carIdDestino;
    }

    public void setCarIdDestino(Integer carIdDestino) {
        this.carIdDestino = carIdDestino;
    }

    public String getCarNomDestino() {
        return carNomDestino;
    }

    public void setCarNomDestino(String carNomDestino) {
        this.carNomDestino = carNomDestino;
    }

    public Integer getDepId() {
        return depId;
    }

    public void setDepId(Integer depId) {
        this.depId = depId;
    }

    public Integer getUsuIdJefe() {
        return usuIdJefe;
    }

    public void setUsuIdJefe(Integer usuIdJefe) {
        this.usuIdJefe = usuIdJefe;
    }

    public String getJustificacion() {
        return justificacion;
    }

    public void setJustificacion(String justificacion) {
        this.justificacion = justificacion;
    }

    public Integer getUsuarioAsignado() {
        return usuarioAsignado;
    }

    public void setUsuarioAsignado(Integer usuarioAsignado) {
        this.usuarioAsignado = usuarioAsignado;
    }

    public Boolean getIndAprobado() {
        return indAprobado;
    }

    public void setIndAprobado(Boolean indAprobado) {
        this.indAprobado = indAprobado;
    }

    public Integer getNumDocumentos() {
        return numDocumentos;
    }

    public void setNumDocumentos(Integer numDocumentos) {
        this.numDocumentos = numDocumentos;
    }

    public Integer getNumExpedientes() {
        return numExpedientes;
    }

    public void setNumExpedientes(Integer numExpedientes) {
        this.numExpedientes = numExpedientes;
    }

    public String getUltEstado() {
        return ultEstado;
    }

    public void setUltEstado(String ultEstado) {
        this.ultEstado = ultEstado;
    }

    public Boolean getEsUsuarioOrigen() {
        return esUsuarioOrigen;
    }

    public void setEsUsuarioOrigen(Boolean esUsuarioOrigen) {
        this.esUsuarioOrigen = esUsuarioOrigen;
    }

    public Boolean getEsJefe() {
        return esJefe;
    }

    public void setEsJefe(Boolean esJefe) {
        this.esJefe = esJefe;
    }

    public Boolean getEsUsuarioDestino() {
        return esUsuarioDestino;
    }

    public void setEsUsuarioDestino(Boolean esUsuarioDestino) {
        this.esUsuarioDestino = esUsuarioDestino;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.tarId);
        hash = 79 * hash + Objects.hashCode(this.activo);
        hash = 79 * hash + Objects.hashCode(this.estado);
        hash = 79 * hash + Objects.hashCode(this.fechaCreacion);
        hash = 79 * hash + Objects.hashCode(this.usuIdOrigen);
        hash = 79 * hash + Objects.hashCode(this.usuNomOrigen);
        hash = 79 * hash + Objects.hashCode(this.carIdOrigen);
        hash = 79 * hash + Objects.hashCode(this.carNomOrigen);
        hash = 79 * hash + Objects.hashCode(this.usuIdDestino);
        hash = 79 * hash + Objects.hashCode(this.usuNomDestino);
        hash = 79 * hash + Objects.hashCode(this.carIdDestino);
        hash = 79 * hash + Objects.hashCode(this.carNomDestino);
        hash = 79 * hash + Objects.hashCode(this.depId);
        hash = 79 * hash + Objects.hashCode(this.usuIdJefe);
        hash = 79 * hash + Objects.hashCode(this.justificacion);
        hash = 79 * hash + Objects.hashCode(this.usuarioAsignado);
        hash = 79 * hash + Objects.hashCode(this.indAprobado);
        hash = 79 * hash + Objects.hashCode(this.numDocumentos);
        hash = 79 * hash + Objects.hashCode(this.numExpedientes);
        hash = 79 * hash + Objects.hashCode(this.ultEstado);
        hash = 79 * hash + Objects.hashCode(this.esUsuarioOrigen);
        hash = 79 * hash + Objects.hashCode(this.esJefe);
        hash = 79 * hash + Objects.hashCode(this.esUsuarioDestino);
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
        final TransferenciaArchivoDTO other = (TransferenciaArchivoDTO) obj;
        if (!Objects.equals(this.activo, other.activo)) {
            return false;
        }
        if (!Objects.equals(this.estado, other.estado)) {
            return false;
        }
        if (!Objects.equals(this.usuNomOrigen, other.usuNomOrigen)) {
            return false;
        }
        if (!Objects.equals(this.carNomOrigen, other.carNomOrigen)) {
            return false;
        }
        if (!Objects.equals(this.usuNomDestino, other.usuNomDestino)) {
            return false;
        }
        if (!Objects.equals(this.carNomDestino, other.carNomDestino)) {
            return false;
        }
        if (!Objects.equals(this.justificacion, other.justificacion)) {
            return false;
        }
        if (!Objects.equals(this.ultEstado, other.ultEstado)) {
            return false;
        }
        if (!Objects.equals(this.tarId, other.tarId)) {
            return false;
        }
        if (!Objects.equals(this.fechaCreacion, other.fechaCreacion)) {
            return false;
        }
        if (!Objects.equals(this.usuIdOrigen, other.usuIdOrigen)) {
            return false;
        }
        if (!Objects.equals(this.carIdOrigen, other.carIdOrigen)) {
            return false;
        }
        if (!Objects.equals(this.usuIdDestino, other.usuIdDestino)) {
            return false;
        }
        if (!Objects.equals(this.carIdDestino, other.carIdDestino)) {
            return false;
        }
        if (!Objects.equals(this.depId, other.depId)) {
            return false;
        }
        if (!Objects.equals(this.usuIdJefe, other.usuIdJefe)) {
            return false;
        }
        if (!Objects.equals(this.usuarioAsignado, other.usuarioAsignado)) {
            return false;
        }
        if (!Objects.equals(this.indAprobado, other.indAprobado)) {
            return false;
        }
        if (!Objects.equals(this.numDocumentos, other.numDocumentos)) {
            return false;
        }
        if (!Objects.equals(this.numExpedientes, other.numExpedientes)) {
            return false;
        }
        if (!Objects.equals(this.esUsuarioOrigen, other.esUsuarioOrigen)) {
            return false;
        }
        if (!Objects.equals(this.esJefe, other.esJefe)) {
            return false;
        }
        if (!Objects.equals(this.esUsuarioDestino, other.esUsuarioDestino)) {
            return false;
        }
        return true;
    }

    
    @Override
    public String toString() {
        return "TransferenciaArchivoDTO{" + "tarId=" + tarId + ", activo=" + activo + ", estado=" + estado + ", fechaCreacion=" + fechaCreacion + ", usuIdOrigen=" + usuIdOrigen + ", usuNomOrigen=" + usuNomOrigen + ", carIdOrigen=" + carIdOrigen + ", carNomOrigen=" + carNomOrigen + ", usuIdDestino=" + usuIdDestino + ", usuNomDestino=" + usuNomDestino + ", carIdDestino=" + carIdDestino + ", carNomDestino=" + carNomDestino + ", depId=" + depId + ", usuIdJefe=" + usuIdJefe + ", justificacion=" + justificacion + ", usuarioAsignado=" + usuarioAsignado + ", indAprobado=" + indAprobado + ", numDocumentos=" + numDocumentos + ", numExpedientes=" + numExpedientes + ", ultEstado=" + ultEstado + ", esUsuarioOrigen=" + esUsuarioOrigen + ", esJefe=" + esJefe + ", esUsuarioDestino=" + esUsuarioDestino + '}';
    }
}