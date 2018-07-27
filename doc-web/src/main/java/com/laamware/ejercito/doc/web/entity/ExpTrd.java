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
@Table(name = "EXP_TRD")
@XmlRootElement
public class ExpTrd implements Serializable {

    private static final long serialVersionUID = 6974868085198733747L;

    @Id
    @GenericGenerator(name = "seq_EXP_TRD", strategy = "sequence",
            parameters = {@Parameter(name = "sequence", value = "seq_EXP_TRD"),@Parameter(name = "allocationSize", value = "1")})
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "seq_EXP_TRD")
    @Basic(optional = false)
    @Column(name = "EXP_TRD_ID")
    private Long expTrdId;
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
    @JoinColumn(name = "EXP_ID", referencedColumnName = "EXP_ID")
    @ManyToOne(optional = false)
    private Expediente expId;
    @JoinColumn(name = "TRD_ID", referencedColumnName = "TRD_ID")
    @ManyToOne(optional = false)
    private Trd trdId;
    @JoinColumn(name = "USU_MODIFICACION", referencedColumnName = "USU_ID")
    @ManyToOne
    private Usuario usuModificacion;
    @JoinColumn(name = "USU_CREACION", referencedColumnName = "USU_ID")
    @ManyToOne(optional = false)
    private Usuario usuCreacion;

    public ExpTrd() {
    }

    public ExpTrd(Long expTrdId) {
        this.expTrdId = expTrdId;
    }

    public ExpTrd(Long expTrdId, boolean indAprobado, boolean activo, Date fecCreacion) {
        this.expTrdId = expTrdId;
        this.indAprobado = indAprobado;
        this.activo = activo;
        this.fecCreacion = fecCreacion;
    }

    public Long getExpTrdId() {
        return expTrdId;
    }

    public void setExpTrdId(Long expTrdId) {
        this.expTrdId = expTrdId;
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

    public Expediente getExpId() {
        return expId;
    }

    public void setExpId(Expediente expId) {
        this.expId = expId;
    }

    public Trd getTrdId() {
        return trdId;
    }

    public void setTrdId(Trd trdId) {
        this.trdId = trdId;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (expTrdId != null ? expTrdId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ExpTrd)) {
            return false;
        }
        ExpTrd other = (ExpTrd) object;
        if ((this.expTrdId == null && other.expTrdId != null) || (this.expTrdId != null && !this.expTrdId.equals(other.expTrdId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.laamware.ejercito.doc.web.entity.ExpTrd[ expTrdId=" + expTrdId + " ]";
    }

}
