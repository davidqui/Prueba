package com.laamware.ejercito.doc.web.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
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
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * Observación por defecto para los documentos.
 *
 * @author jgarcia@controltechcg.com
 * @since 1.8
 * @version 05/23/2018 Issue #172 (SICDI-Controltech) feature-172
 */
@Entity
@Table(name = "DOC_OBSERVACION_DEFECTO")
@SuppressWarnings("PersistenceUnitPresent")
@LaamLabel("Observaciones por defecto")
public class DocumentoObservacionDefecto implements Serializable {

    private static final long serialVersionUID = 8751114401184751828L;

    private static final String SEQUENCE_NAME = "DOC_OBSERVACION_DEFECTO_SEQ";

    @Id
    @GenericGenerator(name = SEQUENCE_NAME, strategy = "sequence",
            parameters = {
                @Parameter(name = "sequence", value = SEQUENCE_NAME)
                ,@Parameter(name = "allocationSize", value = "1")
            })
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    @Basic(optional = false)
    @Column(name = "DOD_ID")
    private Integer id;

    @LaamLabel("Texto")
    @LaamListColumn(order = 10)
    @LaamCreate(order = 10)
    
    @Basic(optional = false)
    @Column(name = "TEXTO_OBSERVACION", length = 64)
    private String textoObservacion;

    @Basic(optional = false)
    @Column(name = "ACTIVO")
    private Boolean activo;

    @ManyToOne
    @JoinColumn(name = "QUIEN", updatable = false, insertable = true, nullable = false)
    private Usuario quien;

    @Column(name = "CUANDO", updatable = false, insertable = true, nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date cuando;

    @ManyToOne
    @JoinColumn(name = "QUIEN_MOD", updatable = true, insertable = true, nullable = false)
    private Usuario quienMod;

    @Column(name = "CUANDO_MOD")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date cuandoMod;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Usuario getQuien() {
        return quien;
    }

    public void setQuien(Usuario quien) {
        this.quien = quien;
    }

    public Date getCuando() {
        return cuando;
    }

    public void setCuando(Date cuando) {
        this.cuando = cuando;
    }

    public Usuario getQuienMod() {
        return quienMod;
    }

    public void setQuienMod(Usuario quienMod) {
        this.quienMod = quienMod;
    }

    public Date getCuandoMod() {
        return cuandoMod;
    }

    public void setCuandoMod(Date cuandoMod) {
        this.cuandoMod = cuandoMod;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.id);
        hash = 97 * hash + Objects.hashCode(this.textoObservacion);
        hash = 97 * hash + Objects.hashCode(this.activo);
        hash = 97 * hash + Objects.hashCode(this.quien);
        hash = 97 * hash + Objects.hashCode(this.cuando);
        hash = 97 * hash + Objects.hashCode(this.quienMod);
        hash = 97 * hash + Objects.hashCode(this.cuandoMod);
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
        final DocumentoObservacionDefecto other = (DocumentoObservacionDefecto) obj;
        if (!Objects.equals(this.textoObservacion, other.textoObservacion)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.activo, other.activo)) {
            return false;
        }
        if (!Objects.equals(this.quien, other.quien)) {
            return false;
        }
        if (!Objects.equals(this.cuando, other.cuando)) {
            return false;
        }
        if (!Objects.equals(this.quienMod, other.quienMod)) {
            return false;
        }
        return Objects.equals(this.cuandoMod, other.cuandoMod);
    }

    @Override
    public String toString() {
        return "DocumentoObservacionDefecto{" + "id=" + id + ", textoObservacion=" + textoObservacion + ", activo=" + activo + ", quien=" + quien + ", cuando=" + cuando + ", quienMod=" + quienMod + ", cuandoMod=" + cuandoMod + '}';
    }

}
