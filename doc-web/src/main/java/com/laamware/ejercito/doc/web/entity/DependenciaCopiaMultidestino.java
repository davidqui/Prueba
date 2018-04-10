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

/**
 * Registro de copia de documento a dependencia multidestino.
 *
 * @author Jorge Alfonso García Espinosa
 * @since 1.8
 * @version 04/10/2018 (SICDI-Controltech Issue #156)
 */
@Entity
@Table(name = "DOCUMENTO_DEP_DESTINO")
@SuppressWarnings("PersistenceUnitPresent")
public class DependenciaCopiaMultidestino extends AuditModifySupport implements Serializable {

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
     */
    public DependenciaCopiaMultidestino(Documento documentoOriginal, Dependencia dependenciaDestino) {
        this.documentoOriginal = documentoOriginal;
        this.dependenciaDestino = dependenciaDestino;

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
        return fechaHoraCreacionDocumentoResultado == null ? null : new Date(fechaHoraCreacionDocumentoResultado.getTime());
    }

    public void setFechaHoraCreacionDocumentoResultado(Date fechaHoraCreacionDocumentoResultado) {
        this.fechaHoraCreacionDocumentoResultado = fechaHoraCreacionDocumentoResultado == null ? null : new Date(fechaHoraCreacionDocumentoResultado.getTime());
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.id);
        hash = 97 * hash + Objects.hashCode(this.documentoOriginal);
        hash = 97 * hash + Objects.hashCode(this.dependenciaDestino);
        hash = 97 * hash + Objects.hashCode(this.activo);
        hash = 97 * hash + Objects.hashCode(this.documentoResultado);
        hash = 97 * hash + Objects.hashCode(this.fechaHoraCreacionDocumentoResultado);
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
        return Objects.equals(this.fechaHoraCreacionDocumentoResultado, other.fechaHoraCreacionDocumentoResultado);
    }

}
