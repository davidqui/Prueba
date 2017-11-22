/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.laamware.ejercito.doc.web.entity;

import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 *
 * @author egonzalezm
 */
@Entity
@Table(name = "RADICACION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Radicacion.findAll", query = "SELECT r FROM Radicacion r")})
public class Radicacion implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @GenericGenerator(name = "RADICACION_SEQ", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "RADICACION_SEQ"),
			@Parameter(name = "allocationSize", value = "1") })
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RADICACION_SEQ")
    @Basic(optional = false)
    @Column(name = "RAD_ID")
    private Integer radId;
    @Column(name = "RAD_NOMBRE")
    private String radNombre;
    @Basic(optional = false)
    @Column(name = "RAD_INDICATIVO")
    private short radIndicativo;
    @JoinColumn(name = "PROCESO", referencedColumnName = "PRO_ID")
    @ManyToOne
    private Proceso proceso;
    @JoinColumn(name = "SECUENCIA", referencedColumnName = "SEQ_ID")
    @ManyToOne(optional = false)
    private SecuenciaRadicacion secuencia;

    public Radicacion() {
    }

    public Radicacion(Integer radId) {
        this.radId = radId;
    }

    public Radicacion(Integer radId, short radIndicativo) {
        this.radId = radId;
        this.radIndicativo = radIndicativo;
    }

    public Integer getRadId() {
        return radId;
    }

    public void setRadId(Integer radId) {
        this.radId = radId;
    }

    public String getRadNombre() {
        return radNombre;
    }

    public void setRadNombre(String radNombre) {
        this.radNombre = radNombre;
    }

    public short getRadIndicativo() {
        return radIndicativo;
    }

    public void setRadIndicativo(short radIndicativo) {
        this.radIndicativo = radIndicativo;
    }

    public Proceso getProceso() {
        return proceso;
    }

    public void setProceso(Proceso proceso) {
        this.proceso = proceso;
    }

    public SecuenciaRadicacion getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(SecuenciaRadicacion secuencia) {
        this.secuencia = secuencia;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (radId != null ? radId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Radicacion)) {
            return false;
        }
        Radicacion other = (Radicacion) object;
        if ((this.radId == null && other.radId != null) || (this.radId != null && !this.radId.equals(other.radId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.laamware.ejercito.doc.web.entity.Radicacion[ radId=" + radId + " ]";
    }
    
}
