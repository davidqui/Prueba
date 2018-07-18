package com.laamware.ejercito.doc.web.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * Elementos del template de notificacion
 *
 * @author samuel.delgado@controltechcg.com
 * @since 1.8
 * @version 30/05/2018 Issue #169 (SICDI-Controltech) feature-169
 */
@Entity
@Table(name = "WILDCARD_NOTIFICACION")
@SuppressWarnings("PersistenceUnitPresent")
@LaamLabel("Elementos del cuerpo de notificacion")
public class WildCardNotificacion implements Serializable {

    private static final long serialVersionUID = 2660645405769057952L;

    @Id
    @Basic(optional = false)
    @Column(name = "WNF_ID")
    private Integer id;

    @LaamLabel("Nombre")
    @Basic(optional = false)
    @Column(name = "NOMBRE", length = 32)
    private String nombre;

    @LaamLabel("Valor")
    @Basic(optional = false)
    @LaamListColumn(order = 10)
    @LaamCreate(order = 10)
    @Column(name = "VALOR", length = 32)
    private String valor;

    @ManyToMany(mappedBy = "wildCards")
    private List<TipoNotificacion> tiposNotificaciones;

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

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public List<TipoNotificacion> getTiposNotificaciones() {
        return tiposNotificaciones;
    }

    public void setTiposNotificaciones(List<TipoNotificacion> tiposNotificaciones) {
        this.tiposNotificaciones = tiposNotificaciones;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.id);
        hash = 89 * hash + Objects.hashCode(this.nombre);
        hash = 89 * hash + Objects.hashCode(this.valor);
        hash = 89 * hash + Objects.hashCode(this.tiposNotificaciones);
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
        final WildCardNotificacion other = (WildCardNotificacion) obj;
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        if (!Objects.equals(this.valor, other.valor)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.tiposNotificaciones, other.tiposNotificaciones)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "WildCardNotificacion{" + "id=" + id + ", nombre=" + nombre + ", valor=" + valor + '}';
    }

}
