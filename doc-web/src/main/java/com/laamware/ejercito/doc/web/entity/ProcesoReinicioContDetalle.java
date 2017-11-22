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
@Table(name = "PROCESO_REINICIO_CONT_DETALLE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProcesoReinicioContDetalle.findAll", query = "SELECT p FROM ProcesoReinicioContDetalle p")})
public class ProcesoReinicioContDetalle implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @GenericGenerator(name = "PRO_REINICIO_CONT_DETALLE_SEQ", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "PRO_REINICIO_CONT_DETALLE_SEQ"),
			@Parameter(name = "allocationSize", value = "1") })
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRO_REINICIO_CONT_DETALLE_SEQ")
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "SECUENCIA_NOMBRE")
    private String secuenciaNombre;
    @Basic(optional = false)
    @Column(name = "ULTIMO_VALOR_SEQ")
    private Integer ultimoValorSeq;
    @Basic(optional = false)
    @Column(name = "NUEVO_VALOR_SEQ")
    private Integer nuevoValorSeq;
    @JoinColumn(name = "PRO_REINICIO_CONTADOR", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private ProcesoReinicioContador proReinicioContador;
    @JoinColumn(name = "SECUENCIA", referencedColumnName = "SEQ_ID")
    @ManyToOne(optional = false)
    private SecuenciaRadicacion secuencia;

    public ProcesoReinicioContDetalle() {
    }

    public ProcesoReinicioContDetalle(Integer id) {
        this.id = id;
    }

    public ProcesoReinicioContDetalle(Integer id, String secuenciaNombre, Integer ultimoValorSeq, Integer nuevoValorSeq) {
        this.id = id;
        this.secuenciaNombre = secuenciaNombre;
        this.ultimoValorSeq = ultimoValorSeq;
        this.nuevoValorSeq = nuevoValorSeq;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSecuenciaNombre() {
        return secuenciaNombre;
    }

    public void setSecuenciaNombre(String secuenciaNombre) {
        this.secuenciaNombre = secuenciaNombre;
    }

    public Integer getUltimoValorSeq() {
        return ultimoValorSeq;
    }

    public void setUltimoValorSeq(Integer ultimoValorSeq) {
        this.ultimoValorSeq = ultimoValorSeq;
    }

    public Integer getNuevoValorSeq() {
        return nuevoValorSeq;
    }

    public void setNuevoValorSeq(Integer nuevoValorSeq) {
        this.nuevoValorSeq = nuevoValorSeq;
    }

    public ProcesoReinicioContador getProReinicioContador() {
        return proReinicioContador;
    }

    public void setProReinicioContador(ProcesoReinicioContador proReinicioContador) {
        this.proReinicioContador = proReinicioContador;
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
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProcesoReinicioContDetalle)) {
            return false;
        }
        ProcesoReinicioContDetalle other = (ProcesoReinicioContDetalle) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.laamware.ejercito.doc.web.entity.ProcesoReinicioContDetalle[ id=" + id + " ]";
    }
    
}
