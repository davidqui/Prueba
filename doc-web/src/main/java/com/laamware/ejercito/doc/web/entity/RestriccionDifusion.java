/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.laamware.ejercito.doc.web.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * Entidad maestra de restriccion de difusion.
 *
 * @author edison.gonzalez@controltechcg.com
 * @since Sep 28, 2017
 * @version 1.0.0 (feature-129).
 */
@Entity
@Table(name = "RESTRICCION_DIFUSION")
@SuppressWarnings("PersistenceUnitPresent")
public class RestriccionDifusion implements Serializable {

    private static final long serialVersionUID = -28360048168646087L;

    /**
     * ID.
     */
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @GenericGenerator(name = "RESTRICCION_DIFUSION_SEQ", strategy = "sequence", parameters = {
        @Parameter(name = "sequence", value = "RESTRICCION_DIFUSION_SEQ")
        ,
			@Parameter(name = "allocationSize", value = "1")})
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RESTRICCION_DIFUSION_SEQ")
    @Basic(optional = false)
    @Column(name = "RES_ID")
    private BigDecimal id;
    /**
     * Descripcion.
     */
    @Column(name = "RES_DESCRIPCION")
    private String resDescripcion;
    /**
     * Estado.
     */
    @Basic(optional = false)
    @Column(name = "ACTIVO")
    private Boolean activo;

    public RestriccionDifusion() {
    }

    /**
     * Obtiene el ID.
     *
     * @return identificador unico.
     */
    public BigDecimal getId() {
        return id;
    }

    /**
     * Establece el ID.
     *
     * @param resId Secuencia.
     */
    public void setId(BigDecimal id) {
        this.id = id;
    }

    /**
     * Obtiene DESCRIPCION.
     *
     * @return descripcion.
     */
    public String getResDescripcion() {
        return resDescripcion;
    }

    /**
     * Establece la descripcion.
     *
     * @param resDescripcion Descripcion.
     */
    public void setResDescripcion(String resDescripcion) {
        this.resDescripcion = resDescripcion;
    }

    /**
     * Obtiene ACTIVO.
     *
     * @return activo.
     */
    public Boolean getActivo() {
        return activo;
    }

    /**
     * Establece el activo.
     *
     * @param activo activo.
     */
    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.id);
        hash = 37 * hash + Objects.hashCode(this.resDescripcion);
        hash = 37 * hash + Objects.hashCode(this.activo);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RestriccionDifusion other = (RestriccionDifusion) obj;
        if (!Objects.equals(this.resDescripcion, other.resDescripcion)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.activo, other.activo)) {
            return false;
        }
        return true;
    }

}
