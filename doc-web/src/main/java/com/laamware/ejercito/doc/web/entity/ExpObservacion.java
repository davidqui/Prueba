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
@Table(name = "EXP_OBSERVACION")
@XmlRootElement
public class ExpObservacion implements Serializable {

    private static final long serialVersionUID = 2279812468415961264L;

    @Id
    @GenericGenerator(name = "seq_EXP_OBSERVACION", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "seq_EXP_OBSERVACION"),
			@Parameter(name = "allocationSize", value = "1") })
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_EXP_OBSERVACION")
    @Basic(optional = false)
    @Column(name = "EXP_OBS_ID")
    private Long expObsId;
    @Basic(optional = false)
    @Column(name = "FEC_CREACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCreacion;
    @Basic(optional = false)
    @Column(name = "EXP_OBSERVACION")
    private String expObservacion;
    @JoinColumn(name = "EXP_ID", referencedColumnName = "EXP_ID")
    @ManyToOne(optional = false)
    private Expediente expId;
    @JoinColumn(name = "USU_ID", referencedColumnName = "USU_ID")
    @ManyToOne(optional = false)
    private Usuario usuId;

    public ExpObservacion() {
    }

    public ExpObservacion(Long expObsId) {
        this.expObsId = expObsId;
    }

    public ExpObservacion(Long expObsId, Date fecCreacion, String expObservacion) {
        this.expObsId = expObsId;
        this.fecCreacion = fecCreacion;
        this.expObservacion = expObservacion;
    }

    public Long getExpObsId() {
        return expObsId;
    }

    public void setExpObsId(Long expObsId) {
        this.expObsId = expObsId;
    }

    public Date getFecCreacion() {
        return fecCreacion;
    }

    public void setFecCreacion(Date fecCreacion) {
        this.fecCreacion = fecCreacion;
    }

    public String getExpObservacion() {
        return expObservacion;
    }

    public void setExpObservacion(String expObservacion) {
        this.expObservacion = expObservacion;
    }

    public Expediente getExpId() {
        return expId;
    }

    public void setExpId(Expediente expId) {
        this.expId = expId;
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
        hash += (expObsId != null ? expObsId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ExpObservacion)) {
            return false;
        }
        ExpObservacion other = (ExpObservacion) object;
        if ((this.expObsId == null && other.expObsId != null) || (this.expObsId != null && !this.expObsId.equals(other.expObsId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.laamware.ejercito.doc.web.entity.ExpObservacion[ expObsId=" + expObsId + " ]";
    }

}
