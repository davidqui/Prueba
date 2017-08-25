package com.laamware.ejercito.doc.web.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
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
 * Entidad detalle de transferencia de archivo.
 *
 * @author jgarcia@controltechcg.com
 * @since Ago 25, 2017
 * @version 1.0.0 (feature-120).
 */
@Entity
@Table(name = "TRANSFERENCIA_ARCHIVO_DETALLE")
@SuppressWarnings("PersistenceUnitPresent")
public class TransferenciaArchivoDetalle implements Serializable {

    /**
     * SerialVersionUID.
     */
    private static final long serialVersionUID = -8298741164687466772L;

    /**
     * ID.
     */
    @Id
    @GenericGenerator(name = "TRANSFERENCIA_ARCHIVO_DET_SEQ", strategy = "sequence",
            parameters = {
                @Parameter(name = "sequence", value = "TRANSFERENCIA_ARCHIVO_DET_SEQ")
                ,@Parameter(name = "allocationSize", value = "1")
            })
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "TRANSFERENCIA_ARCHIVO_DET_SEQ")
    @Column(name = "TAD_ID")
    private Integer id;

    /**
     * Transferencia de archivo.
     */
    @ManyToOne
    @JoinColumn(name = "TAR_ID")
    private TransferenciaArchivo transferenciaArchivo;

    /**
     * Documento dependencia.
     */
    @ManyToOne
    @JoinColumn(name = "DCDP_ID")
    private DocumentoDependencia documentoDependencia;

    /**
     * ID del documento.
     */
    @Column(name = "DOC_ID")
    private String documentoID;

    /**
     * Anterior dependencia.
     */
    @ManyToOne
    @JoinColumn(name = "ANTERIOR_DEP_ID")
    private Dependencia anteriorDependencia;

    /**
     * Anterior usuario.
     */
    @ManyToOne
    @JoinColumn(name = "ANTERIOR_QUIEN")
    private Usuario anteriorUsuario;

    /**
     * Anterior fecha de asignación.
     */
    @Column(name = "ANTERIOR_CUANDO")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date anteriorFechaAsignacion;

    /**
     * Nueva dependencia.
     */
    @ManyToOne
    @JoinColumn(name = "NUEVO_DEP_ID")
    private Dependencia nuevoDependencia;

    /**
     * Nuevo usuario.
     */
    @ManyToOne
    @JoinColumn(name = "NUEVO_QUIEN")
    private Usuario nuevoUsuario;

    /**
     * Nueva fecha de asignación.
     */
    @Column(name = "NUEVO_CUANDO")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date nuevoFechaAsignacion;

    /**
     * Constructor vacío.
     */
    public TransferenciaArchivoDetalle() {
    }

    /**
     * Constructor.
     *
     * @param transferenciaArchivo Transferencia de archivo.
     * @param documentoDependencia Documento dependencia.
     * @param documentoID ID del documento.
     * @param anteriorDependencia Anterior dependencia.
     * @param anteriorUsuario Anterior usuario.
     * @param anteriorFechaAsignacion Anterior fecha de asignación.
     * @param nuevoDependencia Nueva dependencia.
     * @param nuevoUsuario Nuevo usuario.
     * @param nuevoFechaAsignacion Nueva fecha de asignación.
     */
    public TransferenciaArchivoDetalle(TransferenciaArchivo transferenciaArchivo,
            DocumentoDependencia documentoDependencia, String documentoID,
            Dependencia anteriorDependencia, Usuario anteriorUsuario,
            Date anteriorFechaAsignacion, Dependencia nuevoDependencia,
            Usuario nuevoUsuario, Date nuevoFechaAsignacion) {
        this.transferenciaArchivo = transferenciaArchivo;
        this.documentoDependencia = documentoDependencia;
        this.documentoID = documentoID;
        this.anteriorDependencia = anteriorDependencia;
        this.anteriorUsuario = anteriorUsuario;
        this.anteriorFechaAsignacion = new Date(anteriorFechaAsignacion.getTime());
        this.nuevoDependencia = nuevoDependencia;
        this.nuevoUsuario = nuevoUsuario;
        this.nuevoFechaAsignacion = new Date(nuevoFechaAsignacion.getTime());
    }

    /**
     * Obtiene el ID.
     *
     * @return ID.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Establece el ID.
     *
     * @param id ID.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Obtiene la transferencia de archivo.
     *
     * @return Transferencia de archivo.
     */
    public TransferenciaArchivo getTransferenciaArchivo() {
        return transferenciaArchivo;
    }

    /**
     * Establece la transferencia de archivo.
     *
     * @param transferenciaArchivo Transferencia de archivo.
     */
    public void setTransferenciaArchivo(TransferenciaArchivo transferenciaArchivo) {
        this.transferenciaArchivo = transferenciaArchivo;
    }

    /**
     * Obtiene el documento dependencia.
     *
     * @return Documento dependencia.
     */
    public DocumentoDependencia getDocumentoDependencia() {
        return documentoDependencia;
    }

    /**
     * Establece el documento dependencia.
     *
     * @param documentoDependencia Documento dependencia.
     */
    public void setDocumentoDependencia(DocumentoDependencia documentoDependencia) {
        this.documentoDependencia = documentoDependencia;
    }

    /**
     * Obtiene el ID del documento.
     *
     * @return ID del documento.
     */
    public String getDocumentoID() {
        return documentoID;
    }

    /**
     * Establece el ID del documento.
     *
     * @param documentoID ID del documento.
     */
    public void setDocumentoID(String documentoID) {
        this.documentoID = documentoID;
    }

    /**
     * Obtiene la anterior dependencia.
     *
     * @return Anterior dependencia.
     */
    public Dependencia getAnteriorDependencia() {
        return anteriorDependencia;
    }

    /**
     * Establece la anterior dependencia.
     *
     * @param anteriorDependencia Anterior dependencia.
     */
    public void setAnteriorDependencia(Dependencia anteriorDependencia) {
        this.anteriorDependencia = anteriorDependencia;
    }

    /**
     * Obtiene el anterior usuario.
     *
     * @return Anterior usuario.
     */
    public Usuario getAnteriorUsuario() {
        return anteriorUsuario;
    }

    /**
     * Establece el anterior usuario.
     *
     * @param anteriorUsuario Anterior usuario.
     */
    public void setAnteriorUsuario(Usuario anteriorUsuario) {
        this.anteriorUsuario = anteriorUsuario;
    }

    /**
     * Obtiene la anterior fecha de asignación.
     *
     * @return Fecha de asignación.
     */
    public Date getAnteriorFechaAsignacion() {
        return new Date(anteriorFechaAsignacion.getTime());
    }

    /**
     * Establece la anterior fecha de asignación.
     *
     * @param anteriorFechaAsignacion Fecha de asignación.
     */
    public void setAnteriorFechaAsignacion(Date anteriorFechaAsignacion) {
        this.anteriorFechaAsignacion = new Date(anteriorFechaAsignacion.getTime());
    }

    /**
     * Obtiene la nueva dependencia.
     *
     * @return Nueva dependencia.
     */
    public Dependencia getNuevoDependencia() {
        return nuevoDependencia;
    }

    /**
     * Establece la nueva dependencia.
     *
     * @param nuevoDependencia Nueva dependencia.
     */
    public void setNuevoDependencia(Dependencia nuevoDependencia) {
        this.nuevoDependencia = nuevoDependencia;
    }

    /**
     * Obtiene el nuevo usuario.
     *
     * @return Nuevo usuario.
     */
    public Usuario getNuevoUsuario() {
        return nuevoUsuario;
    }

    /**
     * Establece el nuevo usuario.
     *
     * @param nuevoUsuario Nuevo usuario.
     */
    public void setNuevoUsuario(Usuario nuevoUsuario) {
        this.nuevoUsuario = nuevoUsuario;
    }

    /**
     * Obtiene la nueva fecha de asignación.
     *
     * @return Fecha de asignación.
     */
    public Date getNuevoFechaAsignacion() {
        return new Date(nuevoFechaAsignacion.getTime());
    }

    /**
     * Establece la nueva fecha de asignación.
     *
     * @param nuevoFechaAsignacion Fecha de asignación.
     */
    public void setNuevoFechaAsignacion(Date nuevoFechaAsignacion) {
        this.nuevoFechaAsignacion = new Date(nuevoFechaAsignacion.getTime());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.id);
        hash = 47 * hash + Objects.hashCode(this.transferenciaArchivo);
        hash = 47 * hash + Objects.hashCode(this.documentoDependencia);
        hash = 47 * hash + Objects.hashCode(this.documentoID);
        hash = 47 * hash + Objects.hashCode(this.anteriorDependencia);
        hash = 47 * hash + Objects.hashCode(this.anteriorUsuario);
        hash = 47 * hash + Objects.hashCode(this.anteriorFechaAsignacion);
        hash = 47 * hash + Objects.hashCode(this.nuevoDependencia);
        hash = 47 * hash + Objects.hashCode(this.nuevoUsuario);
        hash = 47 * hash + Objects.hashCode(this.nuevoFechaAsignacion);
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
        final TransferenciaArchivoDetalle other = (TransferenciaArchivoDetalle) obj;
        if (!Objects.equals(this.documentoID, other.documentoID)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.transferenciaArchivo, other.transferenciaArchivo)) {
            return false;
        }
        if (!Objects.equals(this.documentoDependencia, other.documentoDependencia)) {
            return false;
        }
        if (!Objects.equals(this.anteriorDependencia, other.anteriorDependencia)) {
            return false;
        }
        if (!Objects.equals(this.anteriorUsuario, other.anteriorUsuario)) {
            return false;
        }
        if (!Objects.equals(this.anteriorFechaAsignacion, other.anteriorFechaAsignacion)) {
            return false;
        }
        if (!Objects.equals(this.nuevoDependencia, other.nuevoDependencia)) {
            return false;
        }
        if (!Objects.equals(this.nuevoUsuario, other.nuevoUsuario)) {
            return false;
        }
        return Objects.equals(this.nuevoFechaAsignacion, other.nuevoFechaAsignacion);
    }

}
