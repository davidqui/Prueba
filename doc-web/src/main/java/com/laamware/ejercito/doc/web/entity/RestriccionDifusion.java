/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.laamware.ejercito.doc.web.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
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
    private BigDecimal resId;
    /**
     * Descripcion.
     */
    @Column(name = "RES_DESCRIPCION")
    private String resDescripcion;
    /**
     * Estado.
     */
    @Basic(optional = false)
    @Column(name = "ESTADO")
    private String estado;

    /**
     * Documentos de la restriccion.
     */
    @OneToMany(mappedBy = "restriccionDifusion")
    private List<Documento> documentoList;

    public RestriccionDifusion() {
    }

    public RestriccionDifusion(BigDecimal resId) {
        this.resId = resId;
    }

    public RestriccionDifusion(BigDecimal resId, String estado) {
        this.resId = resId;
        this.estado = estado;
    }

    /**
     * Obtiene el ID.
     *
     * @return identificador unico.
     */
    public BigDecimal getResId() {
        return resId;
    }

    /**
     * Establece el ID.
     *
     * @param resId Secuencia.
     */
    public void setResId(BigDecimal resId) {
        this.resId = resId;
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
     * Obtiene ESTADO.
     *
     * @return estado.
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Establece el estado.
     *
     * @param estado estado.
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    /**
     * Obtiene documentos de la restriccion.
     *
     * @return lista de documentos.
     */
    @XmlTransient
    public List<Documento> getDocumentoList() {
        return documentoList;
    }

    /**
     * Establece la lista de documentos.
     *
     * @param documentoList Lista de documentos.
     */
    public void setDocumentoList(List<Documento> documentoList) {
        this.documentoList = documentoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (resId != null ? resId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RestriccionDifusion)) {
            return false;
        }
        RestriccionDifusion other = (RestriccionDifusion) object;
        if ((this.resId == null && other.resId != null) || (this.resId != null && !this.resId.equals(other.resId))) {
            return false;
        }
        return true;
    }
}
