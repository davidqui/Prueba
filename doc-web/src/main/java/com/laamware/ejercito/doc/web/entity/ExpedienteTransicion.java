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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name = "EXPEDIENTE_TRANSICION")
@XmlRootElement
public class ExpedienteTransicion implements Serializable {

    private static final long serialVersionUID = -3122217044244557127L;

    @Id
    @GenericGenerator(name = "seq_EXPEDIENTE_TRANSICION", strategy = "sequence",
            parameters = {@Parameter(name = "sequence", value = "seq_EXPEDIENTE_TRANSICION"),@Parameter(name = "allocationSize", value = "1")})
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "seq_EXPEDIENTE_TRANSICION")
    @Basic(optional = false)
    @Column(name = "EXP_TRA_ID")
    private Long expTraId;
    @Basic(optional = false)
    @Column(name = "FEC_CREACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCreacion;
    @JoinColumn(name = "DOC_ID", referencedColumnName = "DOC_ID")
    @ManyToOne
    private Documento docId;
    @JoinColumn(name = "EXP_ID", referencedColumnName = "EXP_ID")
    @ManyToOne(optional = false)
    private Expediente expId;
    @JoinColumn(name = "EXP_EST_ID", referencedColumnName = "EXP_EST_ID")
    @ManyToOne(optional = false)
    private ExpedienteEstado expEstId;
    @JoinColumn(name = "USU_CREACION", referencedColumnName = "USU_ID")
    @ManyToOne(optional = false)
    private Usuario usuCreacion;

    public ExpedienteTransicion() {
    }

    public ExpedienteTransicion(Long expTraId) {
        this.expTraId = expTraId;
    }

    public ExpedienteTransicion(Long expTraId, Date fecCreacion) {
        this.expTraId = expTraId;
        this.fecCreacion = fecCreacion;
    }

    public Long getExpTraId() {
        return expTraId;
    }

    public void setExpTraId(Long expTraId) {
        this.expTraId = expTraId;
    }

    public Date getFecCreacion() {
        return fecCreacion;
    }

    public void setFecCreacion(Date fecCreacion) {
        this.fecCreacion = fecCreacion;
    }

    public Documento getDocId() {
        return docId;
    }

    public void setDocId(Documento docId) {
        this.docId = docId;
    }

    public Expediente getExpId() {
        return expId;
    }

    public void setExpId(Expediente expId) {
        this.expId = expId;
    }

    public ExpedienteEstado getExpEstId() {
        return expEstId;
    }

    public void setExpEstId(ExpedienteEstado expEstId) {
        this.expEstId = expEstId;
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
        hash += (expTraId != null ? expTraId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ExpedienteTransicion)) {
            return false;
        }
        ExpedienteTransicion other = (ExpedienteTransicion) object;
        if ((this.expTraId == null && other.expTraId != null) || (this.expTraId != null && !this.expTraId.equals(other.expTraId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.laamware.ejercito.doc.web.entity.ExpedienteTransicion[ expTraId=" + expTraId + " ]";
    }

}
