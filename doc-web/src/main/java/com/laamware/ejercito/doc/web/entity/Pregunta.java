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
@Table(name = "PREGUNTA")
@LaamLabel("Pregunta")
public class Pregunta implements Serializable {

    private static final long serialVersionUID = -4336628253635398664L;
    @Id
    @GenericGenerator(name = "SQ_PREGUNTA", strategy = "sequence",
            parameters = {@Parameter(name = "sequence", value = "SQ_PREGUNTA"),@Parameter(name = "allocationSize", value = "1")})
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SQ_PREGUNTA")
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1000)
    @LaamLabel("Pregunta")
    @LaamListColumn(order = 10)
    @LaamCreate(order = 10)
    @Column(name = "PREGUNTA")
    private String pregunta;
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
    @JoinColumn(name = "TEMA_CAPACITACION", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TemaCapacitacion temaCapacitacion;
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pregunta")
//    private List<Respuesta> respuestaList;

    public Pregunta() {
    }

    public Pregunta(Integer id) {
        this.id = id;
    }

    public Pregunta(Integer id, String pregunta, Boolean activo, Date cuando, Date cuandoMod, Usuario quien, Usuario quienMod, TemaCapacitacion temaCapacitacion) {
        this.id = id;
        this.pregunta = pregunta;
        this.activo = activo;
        this.cuando = cuando;
        this.cuandoMod = cuandoMod;
        this.quien = quien;
        this.quienMod = quienMod;
        this.temaCapacitacion = temaCapacitacion;
    }

    

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
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
    
    

    public TemaCapacitacion getTemaCapacitacion() {
        return temaCapacitacion;
    }

    public void setTemaCapacitacion(TemaCapacitacion temaCapacitacion) {
        this.temaCapacitacion = temaCapacitacion;
    }

//    public List<Respuesta> getRespuestaList() {
//        return respuestaList;
//    }
//
//    public void setRespuestaList(List<Respuesta> respuestaList) {
//        this.respuestaList = respuestaList;
//    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 19 * hash + Objects.hashCode(this.id);
        hash = 19 * hash + Objects.hashCode(this.pregunta);
        hash = 19 * hash + Objects.hashCode(this.activo);
        hash = 19 * hash + Objects.hashCode(this.cuando);
        hash = 19 * hash + Objects.hashCode(this.cuandoMod);
        hash = 19 * hash + Objects.hashCode(this.temaCapacitacion);
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
        final Pregunta other = (Pregunta) obj;
        if (!Objects.equals(this.pregunta, other.pregunta)) {
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
        if (!Objects.equals(this.cuandoMod, other.cuandoMod)) {
            return false;
        }
        if (!Objects.equals(this.temaCapacitacion, other.temaCapacitacion)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Pregunta{" + "id=" + id + ", pregunta=" + pregunta + ", activo=" + activo + ", cuando=" + cuando + ", cuandoMod=" + cuandoMod + '}';
    }

    
    
}
