package com.laamware.ejercito.doc.web.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
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
 * @author dquijanor
 */
@Entity
@Table(name = "TEMA_CAPACITACION")
@SuppressWarnings("PersistenceUnitPresent")
@LaamLabel("Tema Capacitación")
public class TemaCapacitacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GenericGenerator(name = "SQ_TEMA_CAPACITACION", strategy = "sequence",
            parameters = {@Parameter(name = "sequence", value = "SQ_TEMA_CAPACITACION"),@Parameter(name = "allocationSize", value = "1")})
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SQ_TEMA_CAPACITACION")
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Integer id;
    @LaamLabel("Clasificación")
    @LaamListColumn(order = 10)
    @LaamCreate(order = 10)
    @Column(name = "CLASIFICACION")
    private Integer clasificacion;
    @Basic(optional = false)
    @NotNull
    @LaamLabel("Téma")
    @LaamListColumn(order = 10)
    @LaamCreate(order = 10)
    @Size(min = 1, max = 255)
    @Column(name = "TEMA")
    private String tema;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ACTIVO")
    private Boolean activo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CUANDO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date cuando;
    @Column(name = "CUANDO_MOD")
    @Temporal(TemporalType.TIMESTAMP)
    private Date cuandoMod;
    @JoinColumn(name = "QUIEN", referencedColumnName = "USU_ID")
    @ManyToOne(optional = false)
    private Usuario quien;
    @JoinColumn(name = "QUIEN_MOD", referencedColumnName = "USU_ID")
    @ManyToOne
    private Usuario quienMod;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "temaCapacitacion")
    private List<Pregunta> preguntaList;

    public TemaCapacitacion() {
    }

    public TemaCapacitacion(Integer id) {
        this.id = id;
    }

    public TemaCapacitacion(Integer id, String tema, boolean activo, Date cuando) {
        this.id = id;
        this.tema = tema;
        this.activo = activo;
        this.cuando = cuando;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(Integer clasificacion) {
        this.clasificacion = clasificacion;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
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
    
    public List<Pregunta> getPreguntaList() {
        return preguntaList;
    }

    public void setPreguntaList(List<Pregunta> preguntaList) {
        this.preguntaList = preguntaList;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
        hash = 97 * hash + Objects.hashCode(this.clasificacion);
        hash = 97 * hash + Objects.hashCode(this.tema);
        hash = 97 * hash + Objects.hashCode(this.activo);
        hash = 97 * hash + Objects.hashCode(this.cuando);
        hash = 97 * hash + Objects.hashCode(this.cuandoMod);
        hash = 97 * hash + Objects.hashCode(this.quien);
        hash = 97 * hash + Objects.hashCode(this.quienMod);
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
        final TemaCapacitacion other = (TemaCapacitacion) obj;
        if (!Objects.equals(this.tema, other.tema)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.clasificacion, other.clasificacion)) {
            return false;
        }
        if (!Objects.equals(this.activo, other.activo)) {
            return false;
        }
        if (!Objects.equals(this.cuando, other.cuando)) {
            return false;
        }
        if (!Objects.equals(this.cuandoMod, other.cuandoMod)) {
            return false;
        }
        if (!Objects.equals(this.quien, other.quien)) {
            return false;
        }
        if (!Objects.equals(this.quienMod, other.quienMod)) {
            return false;
        }
        return true;
    }

    

    @Override
    public String toString() {
        return "TemaCapacitacion{" + "id=" + id + ", clasificacion=" + clasificacion + ", tema=" + tema + ", activo=" + activo + ", cuando=" + cuando + ", cuandoMod=" + cuandoMod + '}';
    }

    
}
