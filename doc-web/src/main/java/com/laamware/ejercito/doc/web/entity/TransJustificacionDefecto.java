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
@Table(name = "TRANS_JUSTIFICACION_DEFECTO")
@XmlRootElement
@SuppressWarnings("PersistenceUnitPresent")
@LaamLabel("Observaciones Transferencia de Archivo")
public class TransJustificacionDefecto implements Serializable {

    private static final long serialVersionUID = -3216316165675237241L;

    @Id
    @GenericGenerator(name = "SEQ_TRANS_JUS_DEFECTO", strategy = "sequence",
            parameters = {@Parameter(name = "sequence", value = "SEQ_TRANS_JUS_DEFECTO"),@Parameter(name = "allocationSize", value = "1")})
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_TRANS_JUS_DEFECTO")
    @Basic(optional = false)
    @Column(name = "TJD_ID")
    private Long tjdId;
    @LaamLabel("Nombre de la Observación")
    @LaamListColumn(order = 10)
    @LaamCreate(order = 10)
    @Basic(optional = false)
    @Column(name = "TEXTO_OBSERVACION")
    private String textoObservacion;
    @Basic(optional = false)
    @Column(name = "ACTIVO")
    private Boolean activo;
    @Basic(optional = false)
    @Column(name = "CUANDO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date cuando;
    @Column(name = "CUANDO_MOD")
    @Temporal(TemporalType.TIMESTAMP)
    private Date cuandoMod;
    @JoinColumn(name = "QUIEN", referencedColumnName = "USU_ID")
    @ManyToOne(optional = false)
    private Usuario quien;
    @JoinColumn(name = "QUIEN_MOD", referencedColumnName = "USU_ID")
    @ManyToOne
    private Usuario quienMod;

    public TransJustificacionDefecto() {
    }

    public TransJustificacionDefecto(Long tjdId) {
        this.tjdId = tjdId;
    }

    public TransJustificacionDefecto(Long tjdId, String textoObservacion, Boolean activo, Date cuando) {
        this.tjdId = tjdId;
        this.textoObservacion = textoObservacion;
        this.activo = activo;
        this.cuando = cuando;
    }

    public Long getTjdId() {
        return tjdId;
    }

    public void setTjdId(Long tjdId) {
        this.tjdId = tjdId;
    }

    public String getTextoObservacion() {
        return textoObservacion;
    }

    public void setTextoObservacion(String textoObservacion) {
        this.textoObservacion = textoObservacion;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Date getCuando() {
        return cuando;
    }

    public void setCuando(Date cuando) {
        this.cuando = cuando;
    }

    public Date getCuandoMod() {
        return cuandoMod;
    }

    public void setCuandoMod(Date cuandoMod) {
        this.cuandoMod = cuandoMod;
    }

    public Usuario getQuien() {
        return quien;
    }

    public void setQuien(Usuario quien) {
        this.quien = quien;
    }

    public Usuario getQuienMod() {
        return quienMod;
    }

    public void setQuienMod(Usuario quienMod) {
        this.quienMod = quienMod;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tjdId != null ? tjdId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TransJustificacionDefecto)) {
            return false;
        }
        TransJustificacionDefecto other = (TransJustificacionDefecto) object;
        if ((this.tjdId == null && other.tjdId != null) || (this.tjdId != null && !this.tjdId.equals(other.tjdId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "TransJustificacionDefecto{" + "tjdId=" + tjdId + ", textoObservacion=" + textoObservacion + ", activo=" + activo + ", cuando=" + cuando + ", cuandoMod=" + cuandoMod + ", quien=" + quien + ", quienMod=" + quienMod + '}';
    }
}