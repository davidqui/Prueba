/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.laamware.ejercito.doc.web.entity;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 *
 * @author egonzalezm
 */
@Entity
@Table(name = "PROCESO_REINICIO_CONTADOR")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProcesoReinicioContador.findAll", query = "SELECT p FROM ProcesoReinicioContador p")})
public class ProcesoReinicioContador implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @GenericGenerator(name = "PROCESO_REINICIO_CONTADOR_SEQ", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "PROCESO_REINICIO_CONTADOR_SEQ"),
			@Parameter(name = "allocationSize", value = "1") })
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROCESO_REINICIO_CONTADOR_SEQ")
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "FECHA_HORA_EJECUCION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraEjecucion;
    @Basic(optional = false)
    @Column(name = "IP_EJECUCION")
    private String ipEjecucion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "proReinicioContador")
    private List<ProcesoReinicioContDetalle> procesoReinicioContDetalleList;

    public ProcesoReinicioContador() {
    }

    public ProcesoReinicioContador(Integer id) {
        this.id = id;
    }

    public ProcesoReinicioContador(Integer id, Date fechaHoraEjecucion, String ipEjecucion) {
        this.id = id;
        this.fechaHoraEjecucion = fechaHoraEjecucion;
        this.ipEjecucion = ipEjecucion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFechaHoraEjecucion() {
        return fechaHoraEjecucion;
    }

    public void setFechaHoraEjecucion(Date fechaHoraEjecucion) {
        this.fechaHoraEjecucion = fechaHoraEjecucion;
    }

    public String getIpEjecucion() {
        return ipEjecucion;
    }

    public void setIpEjecucion(String ipEjecucion) {
        this.ipEjecucion = ipEjecucion;
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
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProcesoReinicioContador)) {
            return false;
        }
        ProcesoReinicioContador other = (ProcesoReinicioContador) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.laamware.ejercito.doc.web.entity.ProcesoReinicioContador[ id=" + id + " ]";
    }
    
}
