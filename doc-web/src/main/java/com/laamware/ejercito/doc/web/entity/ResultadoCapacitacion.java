package com.laamware.ejercito.doc.web.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 *
 * @author dquijanor
 */
@Entity
@Table(name = "RESULTADO_CAPACITACION")
@SuppressWarnings("PersistenceUnitPresent")
@LaamLabel("Resultado Capacitación")

public class ResultadoCapacitacion implements Serializable {

    private static final long serialVersionUID = -4326628253635398224L;
    @Id
    @GenericGenerator(name = "SQ_RESULTADO_CAPACITACION", strategy = "sequence",
            parameters = {@Parameter(name = "sequence", value = "SQ_RESULTADO_CAPACITACION"),@Parameter(name = "allocationSize", value = "1")})
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SQ_RESULTADO_CAPACITACION")
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @LaamLabel("Capacitacion")
    @LaamListColumn(order = 10)
    @LaamCreate(order = 10)
    @Column(name = "CAPACITACION")
    private int capacitacion;
    @Column(name = "PREGUNTA")
    private Integer pregunta;
    @Column(name = "CORRECTA")
    private Boolean correcta;

    public ResultadoCapacitacion() {
    }

    public ResultadoCapacitacion(Integer id) {
        this.id = id;
    }

    public ResultadoCapacitacion(Integer id, int capacitacion) {
        this.id = id;
        this.capacitacion = capacitacion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getCapacitacion() {
        return capacitacion;
    }

    public void setCapacitacion(int capacitacion) {
        this.capacitacion = capacitacion;
    }

    public Integer getPregunta() {
        return pregunta;
    }

    public void setPregunta(Integer pregunta) {
        this.pregunta = pregunta;
    }

    public Boolean getCorrecta() {
        return correcta;
    }

    public void setCorrecta(Boolean correcta) {
        this.correcta = correcta;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.id);
        hash = 59 * hash + this.capacitacion;
        hash = 59 * hash + Objects.hashCode(this.pregunta);
        hash = 59 * hash + Objects.hashCode(this.correcta);
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
        final ResultadoCapacitacion other = (ResultadoCapacitacion) obj;
        if (this.capacitacion != other.capacitacion) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.pregunta, other.pregunta)) {
            return false;
        }
        if (!Objects.equals(this.correcta, other.correcta)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ResultadoCapacitacion{" + "id=" + id + ", capacitacion=" + capacitacion + ", pregunta=" + pregunta + ", correcta=" + correcta + '}';
    }

    
    
}
