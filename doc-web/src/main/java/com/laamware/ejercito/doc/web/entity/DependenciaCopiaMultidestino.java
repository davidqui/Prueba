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
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

/**
 * Registro de copia de documento a dependencia multidestino.
 *
 * @author Jorge Alfonso García Espinosa
 * @since 1.8
 * @version 04/10/2018 (SICDI-Controltech Issue #156)
 */
@Entity
@Table(name = "DEPENDENCIA_COPIA_MULTIDESTINO")
@SuppressWarnings("PersistenceUnitPresent")
public class DependenciaCopiaMultidestino implements Serializable {

    private static final long serialVersionUID = 5119424730370123685L;

    private static final String SEQUENCE_NAME = "DEPENDENCIA_COPIA_MULTIDES_SEQ";

    @Id
    @GenericGenerator(name = SEQUENCE_NAME, strategy = "sequence",
            parameters = {
                @Parameter(name = "sequence", value = SEQUENCE_NAME)
                ,@Parameter(name = "allocationSize", value = "1")
            })
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    @Basic(optional = false)
    @Column(name = "DCM_ID")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "DOC_ID_ORIGINAL")
    private Documento documentoOriginal;

    @ManyToOne
    @JoinColumn(name = "DEP_ID_DESTINO")
    private Dependencia dependenciaDestino;

    @Basic(optional = false)
    @Column(name = "ACTIVO")
    private Boolean activo;

    @ManyToOne
    @JoinColumn(name = "DOC_ID_RESULTADO")
    private Documento documentoResultado;

    @Column(name = "FECHAHORA_CREA_DOC_RESULTADO")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaHoraCreacionDocumentoResultado;

    @ManyToOne
    @JoinColumn(name = "QUIEN", updatable = false, insertable = true, nullable = false)
    private Usuario quien;

    @Column(name = "CUANDO", updatable = false, insertable = true, nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date cuando;

    @ManyToOne
    @JoinColumn(name = "QUIEN_MOD", updatable = false, insertable = true, nullable = false)
    private Usuario quienMod;

    @Column(name = "CUANDO_MOD")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date cuandoMod;

    /**
     * Constructor vacío.
     */
    public DependenciaCopiaMultidestino() {
    }

    /**
     * Constructor.
     *
     * @param id ID.
     */
    public DependenciaCopiaMultidestino(Integer id) {
        this.id = id;
    }

    /**
     * Constructor. Establece la instancia como activa.
     *
     * @param documentoOriginal Documento original.
     * @param dependenciaDestino Dependencia destino de la copia.
     * @param quien ID del usuario creador.
     * @param cuando Fecha y hora de creación del registro.
     */
    public DependenciaCopiaMultidestino(Documento documentoOriginal, Dependencia dependenciaDestino, Usuario quien, Date cuando) {
        this.documentoOriginal = documentoOriginal;
        this.dependenciaDestino = dependenciaDestino;
        this.quien = quien;
        this.cuando = cuando;
        this.quienMod = quien;
        this.cuandoMod = cuando;

        activo = Boolean.TRUE;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Documento getDocumentoOriginal() {
        return documentoOriginal;
    }

    public void setDocumentoOriginal(Documento documentoOriginal) {
        this.documentoOriginal = documentoOriginal;
    }

    public Dependencia getDependenciaDestino() {
        return dependenciaDestino;
    }

    public void setDependenciaDestino(Dependencia dependenciaDestino) {
        this.dependenciaDestino = dependenciaDestino;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Documento getDocumentoResultado() {
        return documentoResultado;
    }

    public void setDocumentoResultado(Documento documentoResultado) {
        this.documentoResultado = documentoResultado;
    }

    public Date getFechaHoraCreacionDocumentoResultado() {
        return fechaHoraCreacionDocumentoResultado;
    }

    public void setFechaHoraCreacionDocumentoResultado(Date fechaHoraCreacionDocumentoResultado) {
        this.fechaHoraCreacionDocumentoResultado = fechaHoraCreacionDocumentoResultado;
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

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.id);
        hash = 37 * hash + Objects.hashCode(this.documentoOriginal);
        hash = 37 * hash + Objects.hashCode(this.dependenciaDestino);
        hash = 37 * hash + Objects.hashCode(this.activo);
        hash = 37 * hash + Objects.hashCode(this.documentoResultado);
        hash = 37 * hash + Objects.hashCode(this.fechaHoraCreacionDocumentoResultado);
        hash = 37 * hash + Objects.hashCode(this.quien);
        hash = 37 * hash + Objects.hashCode(this.cuando);
        hash = 37 * hash + Objects.hashCode(this.quienMod);
        hash = 37 * hash + Objects.hashCode(this.cuandoMod);
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
        final DependenciaCopiaMultidestino other = (DependenciaCopiaMultidestino) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.documentoOriginal, other.documentoOriginal)) {
            return false;
        }
        if (!Objects.equals(this.dependenciaDestino, other.dependenciaDestino)) {
            return false;
        }
        if (!Objects.equals(this.activo, other.activo)) {
            return false;
        }
        if (!Objects.equals(this.documentoResultado, other.documentoResultado)) {
            return false;
        }
        if (!Objects.equals(this.fechaHoraCreacionDocumentoResultado, other.fechaHoraCreacionDocumentoResultado)) {
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

}
