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
@Table(name = "CAPACITACION")
@SuppressWarnings("PersistenceUnitPresent")
@LaamLabel("Capacitación")

public class Capacitacion implements Serializable {

    private static final long serialVersionUID = -152418114514515L;
    @Id
    @GenericGenerator(name = "SQ_CAPACITACION", strategy = "sequence",
            parameters = {@Parameter(name = "sequence", value = "SQ_CAPACITACION"),@Parameter(name = "allocationSize", value = "1")})
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SQ_CAPACITACION")
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @LaamLabel("Tema Capacitacion")
    @LaamListColumn(order = 10)
    @LaamCreate(order = 10)
    @Column(name = "TEMA_CAPACITACION")
    private int temaCapacitacion;
    @Size(max = 255)
    @Column(name = "NOTA_OBTENIDA")
    private String notaObtenida;
    @Size(max = 255)
    @Column(name = "RESULTADO")
    private String resultado;
    @Column(name = "NUMERO_CERTIFICADO")
    private BigInteger numeroCertificado;
    @Size(max = 800)
    @Column(name = "UBICACION_CERTIFICADO")
    private String ubicacionCertificado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CUANDO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date cuando;

    public Capacitacion() {
    }

    public Capacitacion(Integer id) {
        this.id = id;
    }

    public Capacitacion(Integer id, int temaCapacitacion, Date cuando) {
        this.id = id;
        this.temaCapacitacion = temaCapacitacion;
        this.cuando = cuando;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getTemaCapacitacion() {
        return temaCapacitacion;
    }

    public void setTemaCapacitacion(int temaCapacitacion) {
        this.temaCapacitacion = temaCapacitacion;
    }

    public String getNotaObtenida() {
        return notaObtenida;
    }

    public void setNotaObtenida(String notaObtenida) {
        this.notaObtenida = notaObtenida;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public BigInteger getNumeroCertificado() {
        return numeroCertificado;
    }

    public void setNumeroCertificado(BigInteger numeroCertificado) {
        this.numeroCertificado = numeroCertificado;
    }

    public String getUbicacionCertificado() {
        return ubicacionCertificado;
    }

    public void setUbicacionCertificado(String ubicacionCertificado) {
        this.ubicacionCertificado = ubicacionCertificado;
    }

    public Date getCuando() {
        return cuando;
    }

    public void setCuando(Date cuando) {
        this.cuando = cuando;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.id);
        hash = 83 * hash + this.temaCapacitacion;
        hash = 83 * hash + Objects.hashCode(this.notaObtenida);
        hash = 83 * hash + Objects.hashCode(this.resultado);
        hash = 83 * hash + Objects.hashCode(this.numeroCertificado);
        hash = 83 * hash + Objects.hashCode(this.ubicacionCertificado);
        hash = 83 * hash + Objects.hashCode(this.cuando);
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
        final Capacitacion other = (Capacitacion) obj;
        if (this.temaCapacitacion != other.temaCapacitacion) {
            return false;
        }
        if (!Objects.equals(this.notaObtenida, other.notaObtenida)) {
            return false;
        }
        if (!Objects.equals(this.resultado, other.resultado)) {
            return false;
        }
        if (!Objects.equals(this.ubicacionCertificado, other.ubicacionCertificado)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.numeroCertificado, other.numeroCertificado)) {
            return false;
        }
        if (!Objects.equals(this.cuando, other.cuando)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Capacitacion{" + "id=" + id + ", temaCapacitacion=" + temaCapacitacion + ", notaObtenida=" + notaObtenida + ", resultado=" + resultado + ", numeroCertificado=" + numeroCertificado + ", ubicacionCertificado=" + ubicacionCertificado + ", cuando=" + cuando + '}';
    }

    
    
}
