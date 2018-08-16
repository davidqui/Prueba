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
 * razones para inhabilitar usuarios.
 *
 * @author samuel.delgado@controltechcg.com
 * @since 1.8
 * @version 05/23/2018 Issue #172 (SICDI-Controltech) feature-172
 */
@Entity
@Table(name = "RAZON_INHABILITAR")
@SuppressWarnings("PersistenceUnitPresent")
@LaamLabel("Razones para Inhabilitar usuario")
public class RazonInhabilitar implements Serializable {
    
    private static final String SEQUENCE_NAME = "RAZON_INHABILITAR_SEQ";
    
    @Id
    @GenericGenerator(name = SEQUENCE_NAME, strategy = "sequence",
            parameters = {
                @Parameter(name = "sequence", value = SEQUENCE_NAME)
                ,@Parameter(name = "allocationSize", value = "1")
            })
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    @Basic(optional = false)
    @Column(name = "RAZ_ID")
    private Integer id;

    @LaamLabel("Texto")
    @LaamListColumn(order = 10)
    @LaamCreate(order = 10)
    @Basic(optional = false)
    @Column(name = "TEXTO_RAZON", length = 64)
    private String textoRazon;

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

    public String getTextoRazon() {
        return textoRazon;
    }

    public void setTextoRazon(String textoRazon) {
        this.textoRazon = textoRazon;
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
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
        hash = 97 * hash + Objects.hashCode(this.textoRazon);
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
        final RazonInhabilitar other = (RazonInhabilitar) obj;
        if (!Objects.equals(this.textoRazon, other.textoRazon)) {
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
        return "{id:"+this.id+", texto:"+this.textoRazon+"}";
    }

}
