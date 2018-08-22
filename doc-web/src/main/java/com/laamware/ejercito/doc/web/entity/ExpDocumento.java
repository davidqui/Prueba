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
@Table(name = "EXP_DOCUMENTO")
@XmlRootElement
public class ExpDocumento implements Serializable {

    private static final long serialVersionUID = -4414736481375280616L;

    @Id
    @GenericGenerator(name = "seq_EXP_DOCUMENTO", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "seq_EXP_DOCUMENTO"),
			@Parameter(name = "allocationSize", value = "1") })
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_EXP_DOCUMENTO")
    @Basic(optional = false)
    @Column(name = "EXP_DOC_ID")
    private Long expDocId;
    @Basic(optional = false)
    @Column(name = "FEC_CREACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCreacion;
    @JoinColumn(name = "USU_CREACION", referencedColumnName = "USU_ID")
    @ManyToOne(optional = false)
    private Usuario usuCreacion;
    @Column(name = "FEC_MODIFICACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecModificacion;
    @JoinColumn(name = "USU_MODIFICACION", referencedColumnName = "USU_ID")
    @ManyToOne
    private Usuario usuModificacion;
    @Basic(optional = false)
    @Column(name = "ACTIVO")
    private boolean activo;
    @JoinColumn(name = "DOC_ID", referencedColumnName = "DOC_ID")
    @ManyToOne(optional = false)
    private Documento docId;
    @JoinColumn(name = "EXP_ID", referencedColumnName = "EXP_ID")
    @ManyToOne(optional = false)
    private Expediente expId;

    public ExpDocumento() {
    }

    public ExpDocumento(Long expDocId) {
        this.expDocId = expDocId;
    }

    public ExpDocumento(Long expDocId, Date fecCreacion, Usuario usuCreacion, boolean activo, Documento docId, Expediente expId) {
        this.expDocId = expDocId;
        this.fecCreacion = fecCreacion;
        this.usuCreacion = usuCreacion;
        this.activo = activo;
        this.docId = docId;
        this.expId = expId;
    }

    public Long getExpDocId() {
        return expDocId;
    }

    public void setExpDocId(Long expDocId) {
        this.expDocId = expDocId;
    }

    public Date getFecCreacion() {
        return fecCreacion;
    }

    public void setFecCreacion(Date fecCreacion) {
        this.fecCreacion = fecCreacion;
    }

    public Usuario getUsuCreacion() {
        return usuCreacion;
    }

    public void setUsuCreacion(Usuario usuCreacion) {
        this.usuCreacion = usuCreacion;
    }

    public Date getFecModificacion() {
        return fecModificacion;
    }

    public void setFecModificacion(Date fecModificacion) {
        this.fecModificacion = fecModificacion;
    }

    public Usuario getUsuModificacion() {
        return usuModificacion;
    }

    public void setUsuModificacion(Usuario usuModificacion) {
        this.usuModificacion = usuModificacion;
    }

    public boolean getActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (expDocId != null ? expDocId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ExpDocumento)) {
            return false;
        }
        ExpDocumento other = (ExpDocumento) object;
        if ((this.expDocId == null && other.expDocId != null) || (this.expDocId != null && !this.expDocId.equals(other.expDocId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.laamware.ejercito.doc.web.entity.ExpDocumento[ expDocId=" + expDocId + " ]";
    }

}
