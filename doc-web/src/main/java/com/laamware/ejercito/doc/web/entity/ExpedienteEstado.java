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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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
@Table(name = "EXPEDIENTE_ESTADO")
@XmlRootElement
public class ExpedienteEstado implements Serializable {

    private static final long serialVersionUID = 110861540975235260L;

    @Id
    @GenericGenerator(name = "seq_EXPEDIENTE_ESTADO", strategy = "sequence",
            parameters = {@Parameter(name = "sequence", value = "seq_EXPEDIENTE_ESTADO"),@Parameter(name = "allocationSize", value = "1")})
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "seq_EXPEDIENTE_ESTADO")
    @Basic(optional = false)
    @Column(name = "EXP_EST_ID")
    private Long expEstId;
    @Basic(optional = false)
    @Column(name = "EST_NOMBRE")
    private String estNombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "expEstId")
    private List<ExpedienteTransicion> expedienteTransicionList;

    public ExpedienteEstado() {
    }

    public ExpedienteEstado(Long expEstId) {
        this.expEstId = expEstId;
    }

    public ExpedienteEstado(Long expEstId, String estNombre) {
        this.expEstId = expEstId;
        this.estNombre = estNombre;
    }

    public Long getExpEstId() {
        return expEstId;
    }

    public void setExpEstId(Long expEstId) {
        this.expEstId = expEstId;
    }

    public String getEstNombre() {
        return estNombre;
    }

    public void setEstNombre(String estNombre) {
        this.estNombre = estNombre;
    }

    @XmlTransient
    public List<ExpedienteTransicion> getExpedienteTransicionList() {
        return expedienteTransicionList;
    }

    public void setExpedienteTransicionList(List<ExpedienteTransicion> expedienteTransicionList) {
        this.expedienteTransicionList = expedienteTransicionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (expEstId != null ? expEstId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ExpedienteEstado)) {
            return false;
        }
        ExpedienteEstado other = (ExpedienteEstado) object;
        if ((this.expEstId == null && other.expEstId != null) || (this.expEstId != null && !this.expEstId.equals(other.expEstId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.laamware.ejercito.doc.web.entity.ExpedienteEstado[ expEstId=" + expEstId + " ]";
    }

}
