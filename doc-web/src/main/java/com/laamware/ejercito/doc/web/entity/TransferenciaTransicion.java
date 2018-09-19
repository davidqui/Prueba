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
 * @version 08/22/2018
 */
@Entity
@Table(name = "TRANSFERENCIA_TRANSICION")
@XmlRootElement
@SuppressWarnings("PersistenceUnitPresent")
public class TransferenciaTransicion implements Serializable {

    private static final long serialVersionUID = -4133235267388096796L;

    @Id
    @GenericGenerator(name = "seq_TRANSFERENCIA_TRANSICION", strategy = "sequence",
            parameters = {@Parameter(name = "sequence", value = "seq_TRANSFERENCIA_TRANSICION"),@Parameter(name = "allocationSize", value = "1")})
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "seq_TRANSFERENCIA_TRANSICION")
    @Basic(optional = false)
    @Column(name = "TTRA_ID")
    private Long ttraId;
    @Basic(optional = false)
    @Column(name = "FEC_CREACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCreacion;
    @JoinColumn(name = "TAR_ID", referencedColumnName = "TAR_ID")
    @ManyToOne(optional = false)
    private TransferenciaArchivo tarId;
    @JoinColumn(name = "TRA_EST_ID", referencedColumnName = "TRA_EST_ID")
    @ManyToOne(optional = false)
    private TransferenciaEstado traEstId;
    @JoinColumn(name = "USU_CREACION", referencedColumnName = "USU_ID")
    @ManyToOne(optional = false)
    private Usuario usuCreacion;

    public TransferenciaTransicion() {
    }

    public TransferenciaTransicion(Long ttraId) {
        this.ttraId = ttraId;
    }

    public TransferenciaTransicion(Long ttraId, Date fecCreacion) {
        this.ttraId = ttraId;
        this.fecCreacion = fecCreacion;
    }

    public Long getTtraId() {
        return ttraId;
    }

    public void setTtraId(Long ttraId) {
        this.ttraId = ttraId;
    }

    public Date getFecCreacion() {
        return fecCreacion;
    }

    public void setFecCreacion(Date fecCreacion) {
        this.fecCreacion = fecCreacion;
    }

    public TransferenciaArchivo getTarId() {
        return tarId;
    }

    public void setTarId(TransferenciaArchivo tarId) {
        this.tarId = tarId;
    }

    public TransferenciaEstado getTraEstId() {
        return traEstId;
    }

    public void setTraEstId(TransferenciaEstado traEstId) {
        this.traEstId = traEstId;
    }

    public Usuario getUsuCreacion() {
        return usuCreacion;
    }

    public void setUsuCreacion(Usuario usuCreacion) {
        this.usuCreacion = usuCreacion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ttraId != null ? ttraId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TransferenciaTransicion)) {
            return false;
        }
        TransferenciaTransicion other = (TransferenciaTransicion) object;
        if ((this.ttraId == null && other.ttraId != null) || (this.ttraId != null && !this.ttraId.equals(other.ttraId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "TransferenciaTransicion{" + "ttraId=" + ttraId + ", fecCreacion=" + fecCreacion + ", tarId=" + tarId + ", traEstId=" + traEstId + ", usuCreacion=" + usuCreacion + '}';
    }
}