/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.laamware.ejercito.doc.web.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 *
 * @author egonzalezm
 * @since 1.8
 * @version 07/27/2018
 */
@Entity
@Table(name = "EXP_USUARIO")
@XmlRootElement
public class ExpUsuario implements Serializable {

    private static final long serialVersionUID = 5365832763486492240L;

    @Id
    @GenericGenerator(name = "seq_EXP_USUARIO", strategy = "sequence",
            parameters = {@Parameter(name = "sequence", value = "seq_EXP_USUARIO"),@Parameter(name = "allocationSize", value = "1")})
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "seq_EXP_USUARIO")
    @Basic(optional = false)
    @Column(name = "EXP_USU_ID")
    private Long expUsuId;
    @Basic(optional = false)
    @Column(name = "PERMISO")
    private int permiso;
    @Basic(optional = false)
    @Column(name = "IND_APROBADO")
    private boolean indAprobado;
    @Basic(optional = false)
    @Column(name = "ACTIVO")
    private boolean activo;
    @Basic(optional = false)
    @Column(name = "FEC_CREACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCreacion;
    @Column(name = "FEC_MODIFICACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecModificacion;
    @JoinColumn(name = "CAR_ID", referencedColumnName = "CAR_ID")
    @ManyToOne(optional = false)
    private Cargo carId;
    @JoinColumn(name = "EXP_ID", referencedColumnName = "EXP_ID")
    @ManyToOne(optional = false)
    private Expediente expId;
    @JoinColumn(name = "USU_CREACION", referencedColumnName = "USU_ID")
    @ManyToOne(optional = false)
    private Usuario usuCreacion;
    @JoinColumn(name = "USU_MODIFICACION", referencedColumnName = "USU_ID")
    @ManyToOne
    private Usuario usuModificacion;
    @JoinColumn(name = "USU_ID", referencedColumnName = "USU_ID")
    @ManyToOne(optional = false)
    private Usuario usuId;

    public ExpUsuario() {
    }

    public ExpUsuario(Long expUsuId) {
        this.expUsuId = expUsuId;
    }

    public ExpUsuario(Long expUsuId, int permiso, boolean indAprobado, boolean activo, Date fecCreacion) {
        this.expUsuId = expUsuId;
        this.permiso = permiso;
        this.indAprobado = indAprobado;
        this.activo = activo;
        this.fecCreacion = fecCreacion;
    }

    public Long getExpUsuId() {
        return expUsuId;
    }

    public void setExpUsuId(Long expUsuId) {
        this.expUsuId = expUsuId;
    }

    public int getPermiso() {
        return permiso;
    }

    public void setPermiso(int permiso) {
        this.permiso = permiso;
    }

    public boolean getIndAprobado() {
        return indAprobado;
    }

    public void setIndAprobado(boolean indAprobado) {
        this.indAprobado = indAprobado;
    }

    public boolean getActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
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

    public Cargo getCarId() {
        return carId;
    }

    public void setCarId(Cargo carId) {
        this.carId = carId;
    }

    public Expediente getExpId() {
        return expId;
    }

    public void setExpId(Expediente expId) {
        this.expId = expId;
    }

    public Usuario getUsuCreacion() {
        return usuCreacion;
    }

    public void setUsuCreacion(Usuario usuCreacion) {
        this.usuCreacion = usuCreacion;
    }

    public Usuario getUsuModificacion() {
        return usuModificacion;
    }

    public void setUsuModificacion(Usuario usuModificacion) {
        this.usuModificacion = usuModificacion;
    }

    public Usuario getUsuId() {
        return usuId;
    }

    public void setUsuId(Usuario usuId) {
        this.usuId = usuId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (expUsuId != null ? expUsuId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ExpUsuario)) {
            return false;
        }
        ExpUsuario other = (ExpUsuario) object;
        if ((this.expUsuId == null && other.expUsuId != null) || (this.expUsuId != null && !this.expUsuId.equals(other.expUsuId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.laamware.ejercito.doc.web.entity.ExpUsuario[ expUsuId=" + expUsuId + " ]";
    }

}
