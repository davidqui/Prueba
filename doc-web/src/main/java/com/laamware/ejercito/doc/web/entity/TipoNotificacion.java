package com.laamware.ejercito.doc.web.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 * Tipos de Notificación
 *
 * @author samuel.delgado@controltechcg.com
 * @since 1.8
 * @version 30/05/2018 Issue #169 (SICDI-Controltech) feature-169
 */
@Entity
@Table(name = "TIPO_NOTIFICACION")
@SuppressWarnings("PersistenceUnitPresent")
@LaamLabel("Tipos de notificación")
public class TipoNotificacion implements Serializable {

    private static final long serialVersionUID = 8775290198456675507L;

    @Id
    @Basic(optional = false)
    @Column(name = "TNF_ID")
    private Integer id;

    @LaamLabel("Nombre")
    @Basic(optional = false)
    @Column(name = "NOMBRE", length = 32)
    private String nombre;

    @LaamLabel("Valor")
    @LaamListColumn(order = 10)
    @LaamCreate(order = 10)
    @Basic(optional = false)
    @Column(name = "VALOR")
    private Integer valor;

    @Basic(optional = false)
    @Column(name = "ACTIVO")
    private Boolean activo;

    @ManyToMany(cascade = {CascadeType.PERSIST})
    @JoinTable(
            name = "WILDCARD_TIPO_NOTIFICACION",
            joinColumns = {
                @JoinColumn(name = "TNF_ID", referencedColumnName = "TNF_ID")},
            inverseJoinColumns = {
                @JoinColumn(name = "WNF_ID", referencedColumnName = "WNF_ID")}
    )
    private List<WildCardNotificacion> wildCards;

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

    public TipoNotificacion() {
    }

    public TipoNotificacion(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getValor() {
        return valor;
    }

    public void setValor(Integer valor) {
        this.valor = valor;
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

    public List<WildCardNotificacion> getWildCards() {
        return wildCards;
    }

    public void setWildCards(List<WildCardNotificacion> wildCards) {
        this.wildCards = wildCards;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.id);
        hash = 53 * hash + Objects.hashCode(this.nombre);
        hash = 53 * hash + Objects.hashCode(this.valor);
        hash = 53 * hash + Objects.hashCode(this.activo);
        hash = 53 * hash + Objects.hashCode(this.wildCards);
        hash = 53 * hash + Objects.hashCode(this.quien);
        hash = 53 * hash + Objects.hashCode(this.cuando);
        hash = 53 * hash + Objects.hashCode(this.quienMod);
        hash = 53 * hash + Objects.hashCode(this.cuandoMod);
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
        final TipoNotificacion other = (TipoNotificacion) obj;
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.valor, other.valor)) {
            return false;
        }
        if (!Objects.equals(this.activo, other.activo)) {
            return false;
        }
        if (!Objects.equals(this.wildCards, other.wildCards)) {
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
        return nombre;
    }

}
