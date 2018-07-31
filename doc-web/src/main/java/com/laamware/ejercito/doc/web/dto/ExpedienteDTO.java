
package com.laamware.ejercito.doc.web.dto;

import java.io.Serializable;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author egonzalezm
 * @since 1.8
 * @version 07/30/2018
 */
@XmlRootElement
public class ExpedienteDTO implements Serializable{

    private static final long serialVersionUID = 1951932210617124282L;
    
    private Long expId;
    private String expNombre;
    private Date fecCreacion;
    private Integer depId;
    private String depNombre;
    private Integer trdIdPrincipal;
    private String trdNomIdPrincipal;
    private boolean indJefeDependencia;
    private boolean indUsuCreador;
    private boolean indAprobadoInicial;
    private String jefeDependencia;
    private String usuarioCreador;
    private String expTipo;
    private String expDescripcion;
    private Integer indUsuarioAsignado;
    private boolean indCerrado;
    private Integer numTrdComplejo;
    private Integer numUsuarios;
    private Integer numDocumentos;
    private boolean indIndexacion;
    
    public ExpedienteDTO() {
    }

    public ExpedienteDTO(Long expId, String expNombre, Date fecCreacion, Integer depId, String depNombre, Integer trdIdPrincipal, String trdNomIdPrincipal, boolean indJefeDependencia, boolean indUsuCreador, String jefeDependencia, String usuarioCreador) {
        this.expId = expId;
        this.expNombre = expNombre;
        this.fecCreacion = fecCreacion;
        this.depId = depId;
        this.depNombre = depNombre;
        this.trdIdPrincipal = trdIdPrincipal;
        this.trdNomIdPrincipal = trdNomIdPrincipal;
        this.indJefeDependencia = indJefeDependencia;
        this.indUsuCreador = indUsuCreador;
        this.jefeDependencia = jefeDependencia;
        this.usuarioCreador = usuarioCreador;
    }

    public Long getExpId() {
        return expId;
    }

    public void setExpId(Long expId) {
        this.expId = expId;
    }

    public String getExpNombre() {
        return expNombre;
    }

    public void setExpNombre(String expNombre) {
        this.expNombre = expNombre;
    }

    public Date getFecCreacion() {
        return fecCreacion;
    }

    public void setFecCreacion(Date fecCreacion) {
        this.fecCreacion = fecCreacion;
    }

    public Integer getDepId() {
        return depId;
    }

    public void setDepId(Integer depId) {
        this.depId = depId;
    }

    public String getDepNombre() {
        return depNombre;
    }

    public void setDepNombre(String depNombre) {
        this.depNombre = depNombre;
    }

    public Integer getTrdIdPrincipal() {
        return trdIdPrincipal;
    }

    public void setTrdIdPrincipal(Integer trdIdPrincipal) {
        this.trdIdPrincipal = trdIdPrincipal;
    }

    public String getTrdNomIdPrincipal() {
        return trdNomIdPrincipal;
    }

    public void setTrdNomIdPrincipal(String trdNomIdPrincipal) {
        this.trdNomIdPrincipal = trdNomIdPrincipal;
    }

    public boolean isIndJefeDependencia() {
        return indJefeDependencia;
    }

    public void setIndJefeDependencia(boolean indJefeDependencia) {
        this.indJefeDependencia = indJefeDependencia;
    }

    public boolean isIndUsuCreador() {
        return indUsuCreador;
    }

    public void setIndUsuCreador(boolean indUsuCreador) {
        this.indUsuCreador = indUsuCreador;
    }

    public String getJefeDependencia() {
        return jefeDependencia;
    }

    public void setJefeDependencia(String jefeDependencia) {
        this.jefeDependencia = jefeDependencia;
    }

    public String getUsuarioCreador() {
        return usuarioCreador;
    }

    public void setUsuarioCreador(String usuarioCreador) {
        this.usuarioCreador = usuarioCreador;
    }

    public boolean isIndAprobadoInicial() {
        return indAprobadoInicial;
    }

    public void setIndAprobadoInicial(boolean indAprobadoInicial) {
        this.indAprobadoInicial = indAprobadoInicial;
    }

    public String getExpTipo() {
        return expTipo;
    }

    public void setExpTipo(String expTipo) {
        this.expTipo = expTipo;
    }

    public String getExpDescripcion() {
        return expDescripcion;
    }

    public void setExpDescripcion(String expDescripcion) {
        this.expDescripcion = expDescripcion;
    }

    public Integer getIndUsuarioAsignado() {
        return indUsuarioAsignado;
    }

    public void setIndUsuarioAsignado(Integer indUsuarioAsignado) {
        this.indUsuarioAsignado = indUsuarioAsignado;
    }

    public boolean isIndCerrado() {
        return indCerrado;
    }

    public void setIndCerrado(boolean indCerrado) {
        this.indCerrado = indCerrado;
    }

    public Integer getNumTrdComplejo() {
        return numTrdComplejo;
    }

    public void setNumTrdComplejo(Integer numTrdComplejo) {
        this.numTrdComplejo = numTrdComplejo;
    }

    public Integer getNumUsuarios() {
        return numUsuarios;
    }

    public void setNumUsuarios(Integer numUsuarios) {
        this.numUsuarios = numUsuarios;
    }

    public Integer getNumDocumentos() {
        return numDocumentos;
    }

    public void setNumDocumentos(Integer numDocumentos) {
        this.numDocumentos = numDocumentos;
    }

    public boolean isIndIndexacion() {
        return indIndexacion;
    }

    public void setIndIndexacion(boolean indIndexacion) {
        this.indIndexacion = indIndexacion;
    }

    @Override
    public String toString() {
        return "ExpedienteDTO{" + "expId=" + expId + ", expNombre=" + expNombre + ", fecCreacion=" + fecCreacion + ", depId=" + depId + ", depNombre=" + depNombre + ", trdIdPrincipal=" + trdIdPrincipal + ", trdNomIdPrincipal=" + trdNomIdPrincipal + ", indJefeDependencia=" + indJefeDependencia + ", indUsuCreador=" + indUsuCreador + ", indAprobadoInicial=" + indAprobadoInicial + ", jefeDependencia=" + jefeDependencia + ", usuarioCreador=" + usuarioCreador + ", expTipo=" + expTipo + ", expDescripcion=" + expDescripcion + ", indUsuarioAsignado=" + indUsuarioAsignado + ", indCerrado=" + indCerrado + ", numTrdComplejo=" + numTrdComplejo + ", numUsuarios=" + numUsuarios + ", numDocumentos=" + numDocumentos + ", indIndexacion=" + indIndexacion + '}';
    }
}