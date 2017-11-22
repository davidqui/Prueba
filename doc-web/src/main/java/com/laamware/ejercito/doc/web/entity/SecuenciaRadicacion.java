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
 */
@Entity
@Table(name = "SECUENCIA_RADICACION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SecuenciaRadicacion.findAll", query = "SELECT s FROM SecuenciaRadicacion s")})
public class SecuenciaRadicacion implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @GenericGenerator(name = "SECUENCIA_RADICACION_SEQ", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "SECUENCIA_RADICACION_SEQ"),
			@Parameter(name = "allocationSize", value = "1") })
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SECUENCIA_RADICACION_SEQ")
    @Basic(optional = false)
    @Column(name = "SEQ_ID")
    private Integer seqId;
    @Basic(optional = false)
    @Column(name = "SEQ_NOMBRE")
    private String seqNombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "secuencia")
    private List<Radicacion> radicacionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "secuencia")
    private List<ProcesoReinicioContDetalle> procesoReinicioContDetalleList;

    public SecuenciaRadicacion() {
    }

    public SecuenciaRadicacion(Integer seqId) {
        this.seqId = seqId;
    }

    public SecuenciaRadicacion(Integer seqId, String seqNombre) {
        this.seqId = seqId;
        this.seqNombre = seqNombre;
    }

    public Integer getSeqId() {
        return seqId;
    }

    public void setSeqId(Integer seqId) {
        this.seqId = seqId;
    }

    public String getSeqNombre() {
        return seqNombre;
    }

    public void setSeqNombre(String seqNombre) {
        this.seqNombre = seqNombre;
    }

    @XmlTransient
    public List<Radicacion> getRadicacionList() {
        return radicacionList;
    }

    public void setRadicacionList(List<Radicacion> radicacionList) {
        this.radicacionList = radicacionList;
    }

    @XmlTransient
    public List<ProcesoReinicioContDetalle> getProcesoReinicioContDetalleList() {
        return procesoReinicioContDetalleList;
    }

    public void setProcesoReinicioContDetalleList(List<ProcesoReinicioContDetalle> procesoReinicioContDetalleList) {
        this.procesoReinicioContDetalleList = procesoReinicioContDetalleList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (seqId != null ? seqId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SecuenciaRadicacion)) {
            return false;
        }
        SecuenciaRadicacion other = (SecuenciaRadicacion) object;
        if ((this.seqId == null && other.seqId != null) || (this.seqId != null && !this.seqId.equals(other.seqId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.laamware.ejercito.doc.web.entity.SecuenciaRadicacion[ seqId=" + seqId + " ]";
    }
    
}
