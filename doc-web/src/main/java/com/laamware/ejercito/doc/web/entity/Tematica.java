package com.laamware.ejercito.doc.web.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
@Table(name = "TEMATICA")
@LaamLabel("Temática")
//@NamedQueries({
//    @NamedQuery(name = "Tematica.findAll", query = "SELECT t FROM Tematica t")})
public class Tematica implements Serializable {

    private static final long serialVersionUID = -152418114514515L;
    @Id
    @GenericGenerator(name = "SQ_TEMATICA", strategy = "sequence",
            parameters = {@Parameter(name = "sequence", value = "SQ_TEMATICA"),@Parameter(name = "allocationSize", value = "1")})
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SQ_TEMATICA")
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
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
    @Column(name = "CUANDO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date cuando;
    @JoinColumn(name = "QUIEN", referencedColumnName = "USU_ID")
    @ManyToOne(optional = false)
    private Usuario quien;
    @JoinColumn(name = "QUIEN_MOD", referencedColumnName = "USU_ID")
    @ManyToOne
    private Usuario quienMod;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CUANDO_MOD")
    @Temporal(TemporalType.TIMESTAMP)
    private Date cuandoMod;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tematica")
    private Collection<RecursoMultimedia> recursoMultimediaCollection;

    public Tematica() {
    }

    public Tematica(Integer id) {
        this.id = id;
    }

    public Tematica(Integer id, String nombre, String descripcion, Boolean activo, Date cuando, Date cuandoMod) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.activo = activo;
        this.cuando = cuando;
        this.cuandoMod = cuandoMod;
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
    
    public Collection<RecursoMultimedia> getRecursoMultimediaCollection() {
        return recursoMultimediaCollection;
    }

    public void setRecursoMultimediaCollection(Collection<RecursoMultimedia> recursoMultimediaCollection) {
        this.recursoMultimediaCollection = recursoMultimediaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.id);
        hash = 29 * hash + Objects.hashCode(this.nombre);
        hash = 29 * hash + Objects.hashCode(this.descripcion);
        hash = 29 * hash + Objects.hashCode(this.activo);
        hash = 29 * hash + Objects.hashCode(this.cuando);
        hash = 29 * hash + Objects.hashCode(this.quien);
        hash = 29 * hash + Objects.hashCode(this.quienMod);
        hash = 29 * hash + Objects.hashCode(this.cuandoMod);
        hash = 29 * hash + Objects.hashCode(this.recursoMultimediaCollection);
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
        final Tematica other = (Tematica) obj;
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        if (!Objects.equals(this.descripcion, other.descripcion)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.activo, other.activo)) {
            return false;
        }
        if (!Objects.equals(this.cuando, other.cuando)) {
            return false;
        }
        if (!Objects.equals(this.quien, other.quien)) {
            return false;
        }
        if (!Objects.equals(this.quienMod, other.quienMod)) {
            return false;
        }
        if (!Objects.equals(this.cuandoMod, other.cuandoMod)) {
            return false;
        }
        if (!Objects.equals(this.recursoMultimediaCollection, other.recursoMultimediaCollection)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Tematica{" + "id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion + ", activo=" + activo + ", cuando=" + cuando + ", quien=" + quien + ", quienMod=" + quienMod + ", cuandoMod=" + cuandoMod + '}';
    }
    
}
