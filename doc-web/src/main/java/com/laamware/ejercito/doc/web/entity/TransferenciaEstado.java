/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.laamware.ejercito.doc.web.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author egonzalezm
 * @since 1.8
 * @version 08/22/2018
 */
@Entity
@Table(name = "TRANSFERENCIA_ESTADO")
@XmlRootElement
@SuppressWarnings("PersistenceUnitPresent")
public class TransferenciaEstado implements Serializable {

    private static final long serialVersionUID = -3840103593548625322L;

    @Id
    @Basic(optional = false)
    @Column(name = "TRA_EST_ID")
    private Long traEstId;
    @Basic(optional = false)
    @Column(name = "TRA_EST_NOMBRE")
    private String traEstNombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "traEstId")
    private List<TransferenciaTransicion> transferenciaTransicionList;

    public TransferenciaEstado() {
    }

    public TransferenciaEstado(Long traEstId) {
        this.traEstId = traEstId;
    }

    public TransferenciaEstado(Long traEstId, String traEstNombre) {
        this.traEstId = traEstId;
        this.traEstNombre = traEstNombre;
    }

    public Long getTraEstId() {
        return traEstId;
    }

    public void setTraEstId(Long traEstId) {
        this.traEstId = traEstId;
    }

    public String getTraEstNombre() {
        return traEstNombre;
    }

    public void setTraEstNombre(String traEstNombre) {
        this.traEstNombre = traEstNombre;
    }

    @XmlTransient
    public List<TransferenciaTransicion> getTransferenciaTransicionList() {
        return transferenciaTransicionList;
    }

    public void setTransferenciaTransicionList(List<TransferenciaTransicion> transferenciaTransicionList) {
        this.transferenciaTransicionList = transferenciaTransicionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (traEstId != null ? traEstId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TransferenciaEstado)) {
            return false;
        }
        TransferenciaEstado other = (TransferenciaEstado) object;
        if ((this.traEstId == null && other.traEstId != null) || (this.traEstId != null && !this.traEstId.equals(other.traEstId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "TransferenciaEstado{" + "traEstId=" + traEstId + ", traEstNombre=" + traEstNombre + ", transferenciaTransicionList=" + transferenciaTransicionList + '}';
    }
}