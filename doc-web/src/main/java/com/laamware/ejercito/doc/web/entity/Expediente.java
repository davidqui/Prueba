/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.laamware.ejercito.doc.web.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 *
 * @author egonzalezm
 * @since 1.8
 * @version 07/27/2018
 */
@Entity
@Table(name = "EXPEDIENTE")
@XmlRootElement
public class Expediente implements Serializable {

    private static final long serialVersionUID = 1762015541224845961L;
    
    /**
     * 2018-07-30 samuel.delgado@controltechcg.com #181 (SICDI-Controltech) 
     * feature-181: se agregan constantes para el usuario.
     */
    public static final Integer ASIGNADO_JEFE_DEP = 1;
    public static final Integer ASIGNADO_USUARIO = 0;
    

    @Id
    @GenericGenerator(name = "seq_EXPEDIENTE", strategy = "sequence",
            parameters = {@Parameter(name = "sequence", value = "seq_EXPEDIENTE"),@Parameter(name = "allocationSize", value = "1")})
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "seq_EXPEDIENTE")
    @Basic(optional = false)
    @Column(name = "EXP_ID")
    private Long expId;
    
    @Basic(optional = false)
    @Column(name = "EXP_TIPO")
    private int expTipo;
    @Basic(optional = false)
    @Column(name = "EXP_NOMBRE")
    private String expNombre;
    @Basic(optional = false)
    @Column(name = "EXP_DESCRIPCION")
    private String expDescripcion;
    @Basic(optional = false)
    @Column(name = "FEC_CREACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCreacion;
    @Column(name = "FEC_MODIFICACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecModificacion;
    @Basic(optional = false)
    @Column(name = "USUARIO_ASIGNADO")
    private int usuarioAsignado;
    @Basic(optional = false)
    @Column(name = "ESTADO_CAMBIO")
    private boolean estadoCambio;
    @Basic(optional = false)
    @Column(name = "IND_APROBADO_INICIAL")
    private boolean indAprobadoInicial;
    @Basic(optional = false)
    @Column(name = "IND_CERRADO")
    private boolean indCerrado;
    @OneToMany(mappedBy = "expediente")
    private List<Documento> documentoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "expId")
    private List<ExpedienteTransicion> expedienteTransicionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "expId")
    private List<ExpTrd> expTrdList;
    @JoinColumn(name = "DEP_ID", referencedColumnName = "DEP_ID")
    @ManyToOne(optional = false)
    private Dependencia depId;
    @JoinColumn(name = "TRD_ID_PRINCIPAL", referencedColumnName = "TRD_ID")
    @ManyToOne(optional = false)
    private Trd trdIdPrincipal;
    @JoinColumn(name = "USU_MODIFICACION", referencedColumnName = "USU_ID")
    @ManyToOne
    private Usuario usuModificacion;
    @JoinColumn(name = "USU_CREACION", referencedColumnName = "USU_ID")
    @ManyToOne(optional = false)
    private Usuario usuCreacion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "expId")
    private List<ExpObservacion> expObservacionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "expId")
    private List<ExpUsuario> expUsuarioList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "expId")
    private List<ExpDocumento> expDocumentoList;

    public Expediente() {
    }

    public Expediente(Long expId) {
        this.expId = expId;
    }

    public Expediente(Long expId, int expTipo, String expNombre, String expDescripcion, Date fecCreacion, int usuarioAsignado, boolean estadoCambio, boolean indAprobadoInicial, boolean indCerrado) {
        this.expId = expId;
        this.expTipo = expTipo;
        this.expNombre = expNombre;
        this.expDescripcion = expDescripcion;
        this.fecCreacion = fecCreacion;
        this.usuarioAsignado = usuarioAsignado;
        this.estadoCambio = estadoCambio;
        this.indAprobadoInicial = indAprobadoInicial;
        this.indCerrado = indCerrado;
    }

    public Long getExpId() {
        return expId;
    }

    public void setExpId(Long expId) {
        this.expId = expId;
    }

    public int getExpTipo() {
        return expTipo;
    }

    public void setExpTipo(int expTipo) {
        this.expTipo = expTipo;
    }

    public String getExpNombre() {
        return expNombre;
    }

    public void setExpNombre(String expNombre) {
        this.expNombre = expNombre;
    }

    public String getExpDescripcion() {
        return expDescripcion;
    }

    public void setExpDescripcion(String expDescripcion) {
        this.expDescripcion = expDescripcion;
    }

    public Date getFecCreacion() {
        return fecCreacion;
    }

    public void setFecCreacion(Date fecCreacion) {
        this.fecCreacion = fecCreacion;
    }

    public Date getFecModificacion() {
        return fecModificacion;
    }

    public void setFecModificacion(Date fecModificacion) {
        this.fecModificacion = fecModificacion;
    }

    public int getUsuarioAsignado() {
        return usuarioAsignado;
    }

    public void setUsuarioAsignado(int usuarioAsignado) {
        this.usuarioAsignado = usuarioAsignado;
    }

    public boolean getEstadoCambio() {
        return estadoCambio;
    }

    public void setEstadoCambio(boolean estadoCambio) {
        this.estadoCambio = estadoCambio;
    }

    public boolean getIndAprobadoInicial() {
        return indAprobadoInicial;
    }

    public void setIndAprobadoInicial(boolean indAprobadoInicial) {
        this.indAprobadoInicial = indAprobadoInicial;
    }

    public boolean getIndCerrado() {
        return indCerrado;
    }

    public void setIndCerrado(boolean indCerrado) {
        this.indCerrado = indCerrado;
    }

    @XmlTransient
    public List<Documento> getDocumentoList() {
        return documentoList;
    }

    public void setDocumentoList(List<Documento> documentoList) {
        this.documentoList = documentoList;
    }

    @XmlTransient
    public List<ExpedienteTransicion> getExpedienteTransicionList() {
        return expedienteTransicionList;
    }

    public void setExpedienteTransicionList(List<ExpedienteTransicion> expedienteTransicionList) {
        this.expedienteTransicionList = expedienteTransicionList;
    }

    @XmlTransient
    public List<ExpTrd> getExpTrdList() {
        return expTrdList;
    }

    public void setExpTrdList(List<ExpTrd> expTrdList) {
        this.expTrdList = expTrdList;
    }

    public Dependencia getDepId() {
        return depId;
    }

    public void setDepId(Dependencia depId) {
        this.depId = depId;
    }

    public Trd getTrdIdPrincipal() {
        return trdIdPrincipal;
    }

    public void setTrdIdPrincipal(Trd trdIdPrincipal) {
        this.trdIdPrincipal = trdIdPrincipal;
    }

    public Usuario getUsuModificacion() {
        return usuModificacion;
    }

    public void setUsuModificacion(Usuario usuModificacion) {
        this.usuModificacion = usuModificacion;
    }

    public Usuario getUsuCreacion() {
        return usuCreacion;
    }

    public void setUsuCreacion(Usuario usuCreacion) {
        this.usuCreacion = usuCreacion;
    }

    @XmlTransient
    public List<ExpObservacion> getExpObservacionList() {
        return expObservacionList;
    }

    public void setExpObservacionList(List<ExpObservacion> expObservacionList) {
        this.expObservacionList = expObservacionList;
    }

    @XmlTransient
    public List<ExpUsuario> getExpUsuarioList() {
        return expUsuarioList;
    }

    public void setExpUsuarioList(List<ExpUsuario> expUsuarioList) {
        this.expUsuarioList = expUsuarioList;
    }

    @XmlTransient
    public List<ExpDocumento> getExpDocumentoList() {
        return expDocumentoList;
    }

    public void setExpDocumentoList(List<ExpDocumento> expDocumentoList) {
        this.expDocumentoList = expDocumentoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (expId != null ? expId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Expediente)) {
            return false;
        }
        Expediente other = (Expediente) object;
        if ((this.expId == null && other.expId != null) || (this.expId != null && !this.expId.equals(other.expId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.laamware.ejercito.doc.web.entity.Expediente[ expId=" + expId + " ]";
    }

}
