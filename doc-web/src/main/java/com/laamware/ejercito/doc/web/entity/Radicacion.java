/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.laamware.ejercito.doc.web.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author egonzalezm
 */
@Entity
@Table(name = "RADICACION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Radicacion.findAll", query = "SELECT r FROM Radicacion r")
    , @NamedQuery(name = "Radicacion.findByRadId", query = "SELECT r FROM Radicacion r WHERE r.radId = :radId")
    , @NamedQuery(name = "Radicacion.findByRadNombre", query = "SELECT r FROM Radicacion r WHERE r.radNombre = :radNombre")
    , @NamedQuery(name = "Radicacion.findByRadIndicativo", query = "SELECT r FROM Radicacion r WHERE r.radIndicativo = :radIndicativo")
    , @NamedQuery(name = "Radicacion.findBySecuencia", query = "SELECT r FROM Radicacion r WHERE r.secuencia = :secuencia")})
public class Radicacion implements Serializable {

    private static final long serialVersionUID = -4480362290369650889L;

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "RAD_ID")
    private Integer radId;
    @Column(name = "RAD_NOMBRE")
    private String radNombre;
    @Basic(optional = false)
    @Column(name = "RAD_INDICATIVO")
    private Integer radIndicativo;
    @Basic(optional = false)
    @Column(name = "SECUENCIA")
    private String secuencia;
    @JoinColumn(name = "PROCESO", referencedColumnName = "PRO_ID")
    @ManyToOne
    private Proceso proceso;

    public Radicacion() {
    }

    public Radicacion(Integer radId) {
        this.radId = radId;
    }

    public Radicacion(Integer radId, Integer radIndicativo, String secuencia) {
        this.radId = radId;
        this.radIndicativo = radIndicativo;
        this.secuencia = secuencia;
    }

    public Integer getRadId() {
        return radId;
    }

    public void setRadId(Integer radId) {
        this.radId = radId;
    }

    public String getRadNombre() {
        return radNombre;
    }

    public void setRadNombre(String radNombre) {
        this.radNombre = radNombre;
    }

    public Integer getRadIndicativo() {
        return radIndicativo;
    }

    public void setRadIndicativo(Integer radIndicativo) {
        this.radIndicativo = radIndicativo;
    }

    public String getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(String secuencia) {
        this.secuencia = secuencia;
    }

    public Proceso getProceso() {
        return proceso;
    }

    public void setProceso(Proceso proceso) {
        this.proceso = proceso;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (radId != null ? radId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Radicacion)) {
            return false;
        }
        Radicacion other = (Radicacion) object;
        if ((this.radId == null && other.radId != null) || (this.radId != null && !this.radId.equals(other.radId))) {
            return false;
        }
        return true;
    }
}
