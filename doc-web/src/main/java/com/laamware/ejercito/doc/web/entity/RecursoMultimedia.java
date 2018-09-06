package com.laamware.ejercito.doc.web.entity;

import java.io.Serializable;
import java.math.BigInteger;
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 *
 * @author aherreram
 */
@Entity
@Table(name = "RECURSO_MULTIMEDIA")
@LaamLabel("Contenido Multimedia")
//@NamedQueries({
//    @NamedQuery(name = "RecursoMultimedia.findAll", query = "SELECT r FROM RecursoMultimedia r")})
public class RecursoMultimedia implements Serializable {

    private static final long serialVersionUID = -19889764665489L;
    @Id
    @GenericGenerator(name = "SQ_RECURSO_MULTIMEDIA", strategy = "sequence",
            parameters = {@Parameter(name = "sequence", value = "SQ_RECURSO_MULTIMEDIA"),@Parameter(name = "allocationSize", value = "1")})
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SQ_RECURSO_MULTIMEDIA")
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @LaamLabel("Nombre")
    @LaamListColumn(order = 10)
    @LaamCreate(order = 10)
    @Column(name = "NOMBRE")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1000)
    @LaamLabel("Descripcion")
    @LaamListColumn(order = 10)
    @LaamCreate(order = 10)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ACTIVO")
    private Boolean activo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @LaamLabel("Fuente")
    @LaamListColumn(order = 10)
    @LaamCreate(order = 10)
    @Column(name = "FUENTE")
    private String fuente;
    @Column(name = "PESO_ORDEN")
    private BigInteger pesoOrden;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "TIPO")
    private String tipo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1000)
    @Column(name = "UBICACION")
    private String ubicacion;
    @Basic(optional = false)
    @NotNull
    @JoinColumn(name = "QUIEN", referencedColumnName = "USU_ID")
    @ManyToOne(optional = false)
    private Usuario quien;
    @Column(name = "CUANDO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date cuando;
    @JoinColumn(name = "QUIEN_MOD", referencedColumnName = "USU_ID")
    @ManyToOne
    private Usuario quienMod;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CUANDO_MOD")
    @Temporal(TemporalType.TIMESTAMP)
    private Date cuandoMod;
    @JoinColumn(name = "TEMATICA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Tematica tematica;

    public RecursoMultimedia() {
    }

    public RecursoMultimedia(Integer id) {
        this.id = id;
    }

    public RecursoMultimedia(Integer id, String nombre, String descripcion, Boolean activo, String fuente, BigInteger pesoOrden, String tipo, String ubicacion, Usuario quien, Date cuando, Usuario quienMod, Date cuandoMod, Tematica tematica) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.activo = activo;
        this.fuente = fuente;
        this.pesoOrden = pesoOrden;
        this.tipo = tipo;
        this.ubicacion = ubicacion;
        this.quien = quien;
        this.cuando = cuando;
        this.quienMod = quienMod;
        this.cuandoMod = cuandoMod;
        this.tematica = tematica;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getFuente() {
        return fuente;
    }

    public void setFuente(String fuente) {
        this.fuente = fuente;
    }

    public BigInteger getPesoOrden() {
        return pesoOrden;
    }

    public void setPesoOrden(BigInteger pesoOrden) {
        this.pesoOrden = pesoOrden;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
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

    public Tematica getTematica() {
        return tematica;
    }

    public void setTematica(Tematica tematica) {
        this.tematica = tematica;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.id);
        hash = 53 * hash + Objects.hashCode(this.nombre);
        hash = 53 * hash + Objects.hashCode(this.descripcion);
        hash = 53 * hash + Objects.hashCode(this.activo);
        hash = 53 * hash + Objects.hashCode(this.fuente);
        hash = 53 * hash + Objects.hashCode(this.pesoOrden);
        hash = 53 * hash + Objects.hashCode(this.tipo);
        hash = 53 * hash + Objects.hashCode(this.ubicacion);
        hash = 53 * hash + Objects.hashCode(this.quien);
        hash = 53 * hash + Objects.hashCode(this.cuando);
        hash = 53 * hash + Objects.hashCode(this.quienMod);
        hash = 53 * hash + Objects.hashCode(this.cuandoMod);
        hash = 53 * hash + Objects.hashCode(this.tematica);
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
        final RecursoMultimedia other = (RecursoMultimedia) obj;
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        if (!Objects.equals(this.descripcion, other.descripcion)) {
            return false;
        }
        if (!Objects.equals(this.fuente, other.fuente)) {
            return false;
        }
        if (!Objects.equals(this.tipo, other.tipo)) {
            return false;
        }
        if (!Objects.equals(this.ubicacion, other.ubicacion)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.activo, other.activo)) {
            return false;
        }
        if (!Objects.equals(this.pesoOrden, other.pesoOrden)) {
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
        if (!Objects.equals(this.cuandoMod, other.cuandoMod)) {
            return false;
        }
        if (!Objects.equals(this.tematica, other.tematica)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "RecursoMultimedia{" + "id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion + ", activo=" + activo + ", fuente=" + fuente + ", pesoOrden=" + pesoOrden + ", tipo=" + tipo + ", ubicacion=" + ubicacion + ", quien=" + quien + ", cuando=" + cuando + ", quienMod=" + quienMod + ", cuandoMod=" + cuandoMod + '}';
    }

    

}
