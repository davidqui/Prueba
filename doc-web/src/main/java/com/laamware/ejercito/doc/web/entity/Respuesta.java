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
@Table(name = "RESPUESTA")
@SuppressWarnings("PersistenceUnitPresent")
@LaamLabel("Respuesta")

public class Respuesta implements Serializable {

    private static final long serialVersionUID = -4326628253635398664L;
    @Id
    @GenericGenerator(name = "SQ_RESPUESTA", strategy = "sequence",
            parameters = {@Parameter(name = "sequence", value = "SQ_RESPUESTA"),@Parameter(name = "allocationSize", value = "1")})
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SQ_RESPUESTA")
    
    @Column(name = "ID")
    private Integer id;
    
    @Size(min = 1, max = 1000)
    @LaamLabel("Texto Respuesta")
    @LaamListColumn(order = 10)
    @LaamCreate(order = 10)
    @LaamWidget("textarea")
    @Column(name = "TEXTO_RESPUESTA")
    private String textoRespuesta;
    
    @LaamLabel("Respuesta Correcta")
    @LaamCreate(order = 60)
    @LaamWidget(value = "checkbox")
    @Column(name = "CORRECTA")
    private Boolean correcta;
    
    @Column(name = "ACTIVO")
    private Boolean activo;
    
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
    
    @JoinColumn(name = "PREGUNTA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Pregunta pregunta;

    public Respuesta() {
    }

    public Respuesta(Integer id) {
        this.id = id;
    }

    public Respuesta(Integer id, String textoRespuesta, Boolean correcta, Boolean activo, Date cuando, Date cuandoMod, Usuario quien, Usuario quienMod, Pregunta pregunta) {
        this.id = id;
        this.textoRespuesta = textoRespuesta;
        this.correcta = correcta;
        this.activo = activo;
        this.cuando = cuando;
        this.cuandoMod = cuandoMod;
        this.quien = quien;
        this.quienMod = quienMod;
        this.pregunta = pregunta;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTextoRespuesta() {
        return textoRespuesta;
    }

    public void setTextoRespuesta(String textoRespuesta) {
        this.textoRespuesta = textoRespuesta;
    }

    public Boolean getCorrecta() {
        return correcta;
    }

    public void setCorrecta(Boolean correcta) {
        this.correcta = correcta;
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

    public Pregunta getPregunta() {
        return pregunta;
    }

    public void setPregunta(Pregunta pregunta) {
        this.pregunta = pregunta;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.id);
        hash = 71 * hash + Objects.hashCode(this.textoRespuesta);
        hash = 71 * hash + Objects.hashCode(this.correcta);
        hash = 71 * hash + Objects.hashCode(this.activo);
        hash = 71 * hash + Objects.hashCode(this.cuando);
        hash = 71 * hash + Objects.hashCode(this.cuandoMod);
        hash = 71 * hash + Objects.hashCode(this.quien);
        hash = 71 * hash + Objects.hashCode(this.quienMod);
        hash = 71 * hash + Objects.hashCode(this.pregunta);
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
        final Respuesta other = (Respuesta) obj;
        if (!Objects.equals(this.textoRespuesta, other.textoRespuesta)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.correcta, other.correcta)) {
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
        if (!Objects.equals(this.pregunta, other.pregunta)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Respuesta{" + "id=" + id + ", textoRespuesta=" + textoRespuesta + ", correcta=" + correcta + ", activo=" + activo + ", cuando=" + cuando + ", cuandoMod=" + cuandoMod + ", quien=" + quien + ", quienMod=" + quienMod + ", pregunta=" + pregunta + '}';
    }

    
   

    
    
}
