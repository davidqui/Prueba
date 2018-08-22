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
@Table(name = "TRANSFERENCIA_OBSERVACION")
@XmlRootElement
@SuppressWarnings("PersistenceUnitPresent")
public class TransferenciaObservacion implements Serializable {

    private static final long serialVersionUID = 5697843832971768631L;

    @Id
    @GenericGenerator(name = "seq_TRANSFERENCIA_OBSERVACION", strategy = "sequence",
            parameters = {@Parameter(name = "sequence", value = "seq_TRANSFERENCIA_OBSERVACION"),@Parameter(name = "allocationSize", value = "1")})
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "seq_TRANSFERENCIA_OBSERVACION")
    @Basic(optional = false)
    @Column(name = "TRA_OBS_ID")
    private Long traObsId;
    @Basic(optional = false)
    @Column(name = "FEC_CREACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCreacion;
    @Column(name = "TRA_OBSERVACION")
    private String traObservacion;
    @JoinColumn(name = "TAR_ID", referencedColumnName = "TAR_ID")
    @ManyToOne(optional = false)
    private TransferenciaArchivo tarId;
    @JoinColumn(name = "USU_ID", referencedColumnName = "USU_ID")
    @ManyToOne(optional = false)
    private Usuario usuId;

    public TransferenciaObservacion() {
    }

    public TransferenciaObservacion(Long traObsId) {
        this.traObsId = traObsId;
    }

    public TransferenciaObservacion(Long traObsId, Date fecCreacion) {
        this.traObsId = traObsId;
        this.fecCreacion = fecCreacion;
    }

    public Long getTraObsId() {
        return traObsId;
    }

    public void setTraObsId(Long traObsId) {
        this.traObsId = traObsId;
    }

    public Date getFecCreacion() {
        return fecCreacion;
    }

    public void setFecCreacion(Date fecCreacion) {
        this.fecCreacion = fecCreacion;
    }

    public String getTraObservacion() {
        return traObservacion;
    }

    public void setTraObservacion(String traObservacion) {
        this.traObservacion = traObservacion;
    }

    public TransferenciaArchivo getTarId() {
        return tarId;
    }

    public void setTarId(TransferenciaArchivo tarId) {
        this.tarId = tarId;
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
        hash += (traObsId != null ? traObsId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TransferenciaObservacion)) {
            return false;
        }
        TransferenciaObservacion other = (TransferenciaObservacion) object;
        if ((this.traObsId == null && other.traObsId != null) || (this.traObsId != null && !this.traObsId.equals(other.traObsId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "TransferenciaObservacion{" + "traObsId=" + traObsId + ", fecCreacion=" + fecCreacion + ", traObservacion=" + traObservacion + ", tarId=" + tarId + ", usuId=" + usuId + '}';
    }
}