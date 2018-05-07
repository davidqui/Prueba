package com.laamware.ejercito.doc.web.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 * Dominio de red de usuarios.
 *
 * @author Jorge Alfonso García Espinosa
 * @since 1.8
 * @version 05/02/2018 Issue #159 (SICDI-Controltech) feature-159
 */
@Entity
@Table(name = "DOMINIO")
@SuppressWarnings("PersistenceUnitPresent")
public class Dominio implements Serializable {

    private static final long serialVersionUID = 6292429033724695281L;

    @Id
    @Basic(optional = false)
    @Column(name = "DOM_CODIGO")
    @LaamLabel("Código")
    @LaamListColumn(order = 20)
    @LaamCreate(order = 20)
    private String codigo;

    @Basic(optional = false)
    @Column(name = "NOMBRE")
    @LaamLabel("Nombre")
    @LaamListColumn(order = 30)
    @LaamCreate(order = 30)
    private String nombre;

    @Column(name = "DESCRIPCION")
    @LaamLabel("Descripción")
    @LaamListColumn(order = 40)
    @LaamCreate(order = 40)
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
    @JoinColumn(name = "QUIEN_MOD", updatable = true, insertable = true, nullable = false)
    private Usuario quienMod;

    @Column(name = "CUANDO_MOD")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date cuandoMod;

    @Basic(optional = false)
    @Column(name = "DOM_PRV_VER_LINK_OWA")
    @LaamLabel("Permitir ver link OWA")
    @LaamCreate(order = 50)
    @LaamListColumn(order = 50)
    @LaamWidget(value = "checkbox")
    private Boolean visualizaLinkOWA;

    /**
     * Constructor vacío.
     */
    public Dominio() {
    }

    /**
     * Constructor.
     *
     * @param codigo Código.
     */
    public Dominio(String codigo) {
        this.codigo = codigo;
    }

    /**
     * Constructor.
     *
     * @param codigo Código.
     * @param nombre Nombre.
     * @param quien Usuario creador.
     * @param cuando Fecha/hora de creación.
     * @param activo Indicador de actividad.
     * @param visualizaLinkOWA Indicador de visualización del link OWA.
     */
    public Dominio(String codigo, String nombre, Usuario quien, Date cuando, Boolean activo, Boolean visualizaLinkOWA) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.quien = quien;
        this.cuando = (cuando == null) ? null : new Date(cuando.getTime());
        this.quienMod = quien;
        this.cuandoMod = (cuando == null) ? null : new Date(cuando.getTime());
        this.activo = activo;
        this.visualizaLinkOWA = visualizaLinkOWA;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
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
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.codigo);
        hash = 47 * hash + Objects.hashCode(this.nombre);
        hash = 47 * hash + Objects.hashCode(this.descripcion);
        hash = 47 * hash + Objects.hashCode(this.quien);
        hash = 47 * hash + Objects.hashCode(this.cuando);
        hash = 47 * hash + Objects.hashCode(this.activo);
        hash = 47 * hash + Objects.hashCode(this.quienMod);
        hash = 47 * hash + Objects.hashCode(this.cuandoMod);
        hash = 47 * hash + Objects.hashCode(this.visualizaLinkOWA);
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
        final Dominio other = (Dominio) obj;
        if (!Objects.equals(this.codigo, other.codigo)) {
            return false;
        }
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        if (!Objects.equals(this.descripcion, other.descripcion)) {
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
        return "Dominio{" + "codigo=" + codigo + ", nombre=" + nombre + ", descripcion=" + descripcion + ", quien=" + quien + ", cuando=" + cuando + ", activo=" + activo + ", quienMod=" + quienMod + ", cuandoMod=" + cuandoMod + ", visualizaLinkOWA=" + visualizaLinkOWA + '}';
    }

}
