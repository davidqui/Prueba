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
 * Registro histórico de cambios en {@link Dominio}.
 *
 * @author Jorge Alfonso García Espinosa
 * @since 1.8
 * @version 05/02/2018 Issue #159 (SICDI-Controltech) feature-159
 */
@Entity
@Table(name = "H_DOMINIO")
@SuppressWarnings("PersistenceUnitPresent")
public class HistoricoDominio implements Serializable {

    private static final long serialVersionUID = -3900506339940681491L;

    private static final String SEQUENCE_NAME = "H_DOMINIO_SEQ";

    @Id
    @GenericGenerator(name = SEQUENCE_NAME, strategy = "sequence",
            parameters = {
                @Parameter(name = "sequence", value = SEQUENCE_NAME)
                ,@Parameter(name = "allocationSize", value = "1")
            })
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    @Basic(optional = false)
    @Column(name = "HDOM_ID")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "DOM_CODIGO")
    private Dominio dominio;

    @Basic(optional = false)
    @Column(name = "NOMBRE")
    private String nombre;

    @Column(name = "DESCRIPCION")
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "QUIEN", updatable = false, insertable = true, nullable = false)
    private Usuario quien;

    @Column(name = "CUANDO", updatable = false, insertable = true, nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date cuando;

    @Basic(optional = false)
    @Column(name = "ACTIVO")
    private Boolean activo;

    @ManyToOne
    @JoinColumn(name = "QUIEN_MOD", updatable = false, insertable = true, nullable = false)
    private Usuario quienMod;

    @Column(name = "CUANDO_MOD")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date cuandoMod;

    @Basic(optional = false)
    @Column(name = "DOM_PRV_VER_LINK_OWA")
    private Boolean visualizaLinkOWA;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Dominio getDominio() {
        return dominio;
    }

    public void setDominio(Dominio dominio) {
        this.dominio = dominio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Usuario getQuien() {
        return quien;
    }

    public void setQuien(Usuario quien) {
        this.quien = quien;
    }

    public Date getCuando() {
        return (cuando == null) ? null : new Date(cuando.getTime());
    }

    public void setCuando(Date cuando) {
        this.cuando = (cuando == null) ? null : new Date(cuando.getTime());
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Usuario getQuienMod() {
        return quienMod;
    }

    public void setQuienMod(Usuario quienMod) {
        this.quienMod = quienMod;
    }

    public Date getCuandoMod() {
        return (cuandoMod == null) ? null : new Date(cuandoMod.getTime());
    }

    public void setCuandoMod(Date cuandoMod) {
        this.cuandoMod = (cuandoMod == null) ? null : new Date(cuandoMod.getTime());
    }

    public Boolean getVisualizaLinkOWA() {
        return visualizaLinkOWA;
    }

    public void setVisualizaLinkOWA(Boolean visualizaLinkOWA) {
        this.visualizaLinkOWA = visualizaLinkOWA;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.id);
        hash = 79 * hash + Objects.hashCode(this.dominio);
        hash = 79 * hash + Objects.hashCode(this.nombre);
        hash = 79 * hash + Objects.hashCode(this.descripcion);
        hash = 79 * hash + Objects.hashCode(this.quien);
        hash = 79 * hash + Objects.hashCode(this.cuando);
        hash = 79 * hash + Objects.hashCode(this.activo);
        hash = 79 * hash + Objects.hashCode(this.quienMod);
        hash = 79 * hash + Objects.hashCode(this.cuandoMod);
        hash = 79 * hash + Objects.hashCode(this.visualizaLinkOWA);
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
        final HistoricoDominio other = (HistoricoDominio) obj;
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        if (!Objects.equals(this.descripcion, other.descripcion)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.dominio, other.dominio)) {
            return false;
        }
        if (!Objects.equals(this.quien, other.quien)) {
            return false;
        }
        if (!Objects.equals(this.cuando, other.cuando)) {
            return false;
        }
        if (!Objects.equals(this.activo, other.activo)) {
            return false;
        }
        if (!Objects.equals(this.quienMod, other.quienMod)) {
            return false;
        }
        if (!Objects.equals(this.cuandoMod, other.cuandoMod)) {
            return false;
        }
        return Objects.equals(this.visualizaLinkOWA, other.visualizaLinkOWA);
    }

    @Override
    public String toString() {
        return "HistoricoDominio{" + "id=" + id + ", dominio=" + dominio + ", nombre=" + nombre + ", descripcion=" + descripcion + ", quien=" + quien + ", cuando=" + cuando + ", activo=" + activo + ", quienMod=" + quienMod + ", cuandoMod=" + cuandoMod + ", visualizaLinkOWA=" + visualizaLinkOWA + '}';
    }

}
