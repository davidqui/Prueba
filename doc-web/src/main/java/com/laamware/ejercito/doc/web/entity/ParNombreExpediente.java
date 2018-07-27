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
@Table(name = "PAR_NOMBRE_EXPEDIENTE")
@XmlRootElement
public class ParNombreExpediente implements Serializable {

    private static final long serialVersionUID = -4336628453635398664L;

    @Id
    @GenericGenerator(name = "seq_PAR_NOMBRE_EXPEDIENTE", strategy = "sequence",
            parameters = {@Parameter(name = "sequence", value = "seq_PAR_NOMBRE_EXPEDIENTE"),@Parameter(name = "allocationSize", value = "1")})
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "seq_PAR_NOMBRE_EXPEDIENTE")
    @Basic(optional = false)
    @Column(name = "PAR_ID")
    private Long parId;
    @Basic(optional = false)
    @Column(name = "PAR_NOMBRE")
    private String parNombre;
    @Basic(optional = false)
    @Column(name = "FEC_CREACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCreacion;
    @Column(name = "FEC_MODIFICACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecModificacion;
    @JoinColumn(name = "USU_MODIFICACION", referencedColumnName = "USU_ID")
    @ManyToOne
    private Usuario usuModificacion;
    @JoinColumn(name = "USU_CREACION", referencedColumnName = "USU_ID")
    @ManyToOne(optional = false)
    private Usuario usuCreacion;

    public ParNombreExpediente() {
    }

    public ParNombreExpediente(Long parId) {
        this.parId = parId;
    }

    public ParNombreExpediente(Long parId, String parNombre, Date fecCreacion) {
        this.parId = parId;
        this.parNombre = parNombre;
        this.fecCreacion = fecCreacion;
    }

    public Long getParId() {
        return parId;
    }

    public void setParId(Long parId) {
        this.parId = parId;
    }

    public String getParNombre() {
        return parNombre;
    }

    public void setParNombre(String parNombre) {
        this.parNombre = parNombre;
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
        hash += (parId != null ? parId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ParNombreExpediente)) {
            return false;
        }
        ParNombreExpediente other = (ParNombreExpediente) object;
        if ((this.parId == null && other.parId != null) || (this.parId != null && !this.parId.equals(other.parId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.laamware.ejercito.doc.web.entity.ParNombreExpediente[ parId=" + parId + " ]";
    }

}
