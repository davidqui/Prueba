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
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * Entidad maestra de transferencia de archivo.
 *
 * @author jgarcia@controltechcg.com
 * @since Ago 25, 2017
 * @version 1.0.0 (feature-120).
 */
@Entity
@Table(name = "TRANSFERENCIA_ARCHIVO")
@SuppressWarnings("PersistenceUnitPresent")
public class TransferenciaArchivo implements Serializable {

    /**
     * SerialVersionUID.
     */
    private static final long serialVersionUID = 3630553950712061442L;

    /**
     * Estado de transferencia creada.
     */
    public static final String CREADA_ESTADO = "C";

    /**
     * Estado de transferencia aprobada.
     */
    public static final String APROBADA_ESTADO = "A";

    /**
     * Estado de transferencia rechazada.
     */
    public static final String RECHAZADA_ESTADO = "R";

    /**
     * ID.
     */
    @Id
    @GenericGenerator(name = "TRANSFERENCIA_ARCHIVO_SEQ", strategy = "sequence",
            parameters = {
                @Parameter(name = "sequence", value = "TRANSFERENCIA_ARCHIVO_SEQ")
                ,@Parameter(name = "allocationSize", value = "1")
            })
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "TRANSFERENCIA_ARCHIVO_SEQ")
    @Column(name = "TAR_ID")
    private Integer id;

    /**
     * Bandera de activo.
     */
    @Column(name = "ACTIVO")
    private Boolean activo;

    /**
     * Estado.
     */
    @Size(max = 1)
    @Column(name = "ESTADO")
    private String estado;

    /**
     * Usuario creador.
     */
    @ManyToOne
    @JoinColumn(name = "CREADOR_USU_ID")
    private Usuario creadorUsuario;

    /**
     * Dependencia del usuario creador.
     */
    @ManyToOne
    @JoinColumn(name = "CREADOR_DEP_ID")
    private Dependencia creadorDependencia;

    /**
     * Grado del usuario creador.
     */
    @ManyToOne
    @JoinColumn(name = "CREADOR_GRA_ID")
    private Grados creadorGrado;

    /**
     * Cargo del usuario creador.
     */
    @Column(name = "CREADOR_USU_CARGO")
    private String creadorCargo;

    /**
     * Fecha de creación.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FECHA_CREACION")
    private Date fechaCreacion;

    /**
     * Usuario origen.
     */
    @ManyToOne
    @JoinColumn(name = "ORIGEN_USU_ID")
    private Usuario origenUsuario;

    /**
     * Dependencia del usuario origen.
     */
    @ManyToOne
    @JoinColumn(name = "ORIGEN_DEP_ID")
    private Dependencia origenDependencia;

    /**
     * Clasificación del usuario origen.
     */
    @ManyToOne
    @JoinColumn(name = "ORIGEN_CLA_ID")
    private Clasificacion origenClasificacion;

    /**
     * Grado del usuario origen.
     */
    @ManyToOne
    @JoinColumn(name = "ORIGEN_GRA_ID")
    private Grados origenGrado;

    /**
     * Cargo del usuario origen.
     */
    @Column(name = "ORIGEN_USU_CARGO")
    private String origenCargo;

    /**
     * Usuario destino.
     */
    @ManyToOne
    @JoinColumn(name = "DESTINO_USU_ID")
    private Usuario destinoUsuario;

    /**
     * Dependencia del usuario destino.
     */
    @ManyToOne
    @JoinColumn(name = "DESTINO_DEP_ID")
    private Dependencia destinoDependencia;

    /**
     * Clasificación del usuario destino.
     */
    @ManyToOne
    @JoinColumn(name = "DESTINO_CLA_ID")
    private Clasificacion destinoClasificacion;

    /**
     * Grado del usuario destino.
     */
    @ManyToOne
    @JoinColumn(name = "DESTINO_GRA_ID")
    private Grados destinoGrado;

    /**
     * Cargo del usuario destino.
     */
    @Column(name = "DESTINO_USU_CARGO")
    private String destinoCargo;

    /**
     * Número de documentos.
     */
    @Column(name = "NUM_DOCUMENTOS")
    private Integer numeroDocumentos;

    /**
     * Fecha de aprobación.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FECHA_APROBACION")
    private Date fechaAprobacion;

    /**
     * Código OFS del acta.
     */
    @Size(max = 32)
    @Column(name = "ACTA_OFS")
    private String actaOFS;

    /**
     * Constructor vacío.
     */
    public TransferenciaArchivo() {
    }

    /**
     * Constructor.
     *
     * @param creadorUsuario Usuario creador.
     * @param creadorDependencia Dependencia del usuario creador.
     * @param creadorGrado Grado del usuario creador.
     * @param creadorCargo Cargo del usuario creador.
     * @param fechaCreacion Fecha de creación.
     * @param origenUsuario Usuario origen.
     * @param origenDependencia Depependencia del usuario origen.
     * @param origenClasificacion Clasificación del usuario origen.
     * @param origenGrado Grado del usuario origen.
     * @param origenCargo Cargo del usuario origen.
     * @param destinoUsuario Usuario destino.
     * @param destinoDependencia Dependencia del usuario destino.
     * @param destinoClasificacion Clasificación del usuario destino.
     * @param destinoGrado Grado del usuario destino.
     * @param destinoCargo Cargo del usuario destino.
     * @param numeroDocumentos Número de documentos.
     */
    public TransferenciaArchivo(Usuario creadorUsuario, Dependencia creadorDependencia,
            Grados creadorGrado, String creadorCargo, Date fechaCreacion,
            Usuario origenUsuario, Dependencia origenDependencia,
            Clasificacion origenClasificacion, Grados origenGrado, String origenCargo,
            Usuario destinoUsuario, Dependencia destinoDependencia,
            Clasificacion destinoClasificacion, Grados destinoGrado,
            String destinoCargo, Integer numeroDocumentos) {
        this.creadorUsuario = creadorUsuario;
        this.creadorDependencia = creadorDependencia;
        this.creadorGrado = creadorGrado;
        this.creadorCargo = creadorCargo;
        this.fechaCreacion = new Date(fechaCreacion.getTime());
        this.origenUsuario = origenUsuario;
        this.origenDependencia = origenDependencia;
        this.origenClasificacion = origenClasificacion;
        this.origenGrado = origenGrado;
        this.origenCargo = origenCargo;
        this.destinoUsuario = destinoUsuario;
        this.destinoDependencia = destinoDependencia;
        this.destinoClasificacion = destinoClasificacion;
        this.destinoGrado = destinoGrado;
        this.destinoCargo = destinoCargo;
        this.numeroDocumentos = numeroDocumentos;
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
     * Obtiene la bandera de activo.
     *
     * @return Bandera de activo.
     */
    public Boolean getActivo() {
        return activo;
    }

    /**
     * Establece la bandera de activo.
     *
     * @param activo Bandera de activo.
     */
    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    /**
     * Obtiene el estado.
     *
     * @return Estado.
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Establece el estado.
     *
     * @param estado Estado.
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * Obtiene el usuario creador.
     *
     * @return Usuario.
     */
    public Usuario getCreadorUsuario() {
        return creadorUsuario;
    }

    /**
     * Establece el usuario creador.
     *
     * @param creadorUsuario Usuario.
     */
    public void setCreadorUsuario(Usuario creadorUsuario) {
        this.creadorUsuario = creadorUsuario;
    }

    /**
     * Obtiene la dependencia del usuario creador.
     *
     * @return Dependencia.
     */
    public Dependencia getCreadorDependencia() {
        return creadorDependencia;
    }

    /**
     * Establece la dependencia del usuario creador.
     *
     * @param creadorDependencia Dependencia.
     */
    public void setCreadorDependencia(Dependencia creadorDependencia) {
        this.creadorDependencia = creadorDependencia;
    }

    /**
     * Obtiene el grado del usuario creador.
     *
     * @return Grado.
     */
    public Grados getCreadorGrado() {
        return creadorGrado;
    }

    /**
     * Establece el grado del usuario creador.
     *
     * @param creadorGrado Grado.
     */
    public void setCreadorGrado(Grados creadorGrado) {
        this.creadorGrado = creadorGrado;
    }

    /**
     * Obtiene el cargo del usuario creador.
     *
     * @return Cargo.
     */
    public String getCreadorCargo() {
        return creadorCargo;
    }

    /**
     * Establece el cargo del usuario creador.
     *
     * @param creadorCargo Cargo.
     */
    public void setCreadorCargo(String creadorCargo) {
        this.creadorCargo = creadorCargo;
    }

    /**
     * Obtiene la fecha de creación.
     *
     * @return Fecha.
     */
    public Date getFechaCreacion() {
        return new Date(fechaCreacion.getTime());
    }

    /**
     * Establece la fecha de creación.
     *
     * @param fechaCreacion Fecha.
     */
    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = new Date(fechaCreacion.getTime());
    }

    /**
     * Obtiene el usuario origen.
     *
     * @return Usuario.
     */
    public Usuario getOrigenUsuario() {
        return origenUsuario;
    }

    /**
     * Establece el usuario origen.
     *
     * @param origenUsuario Usuario.
     */
    public void setOrigenUsuario(Usuario origenUsuario) {
        this.origenUsuario = origenUsuario;
    }

    /**
     * Obtiene la dependencia del usuario origen.
     *
     * @return Dependencia.
     */
    public Dependencia getOrigenDependencia() {
        return origenDependencia;
    }

    /**
     * Establece la dependencia del usuario origen.
     *
     * @param origenDependencia Dependencia.
     */
    public void setOrigenDependencia(Dependencia origenDependencia) {
        this.origenDependencia = origenDependencia;
    }

    /**
     * Obtiene la clasificacíón del usuario origen.
     *
     * @return Clasificación.
     */
    public Clasificacion getOrigenClasificacion() {
        return origenClasificacion;
    }

    /**
     * Establece la clasificación del usuario origen.
     *
     * @param origenClasificacion Clasificación.
     */
    public void setOrigenClasificacion(Clasificacion origenClasificacion) {
        this.origenClasificacion = origenClasificacion;
    }

    /**
     * Obtiene el grado del usuario origen.
     *
     * @return Grado.
     */
    public Grados getOrigenGrado() {
        return origenGrado;
    }

    /**
     * Establece el grado del usuario origen.
     *
     * @param origenGrado Grado.
     */
    public void setOrigenGrado(Grados origenGrado) {
        this.origenGrado = origenGrado;
    }

    /**
     * Obtiene el cargo del usuario origen.
     *
     * @return Cargo.
     */
    public String getOrigenCargo() {
        return origenCargo;
    }

    /**
     * Establece el cargo del usuario origen.
     *
     * @param origenCargo Cargo.
     */
    public void setOrigenCargo(String origenCargo) {
        this.origenCargo = origenCargo;
    }

    /**
     * Obtiene el usuario destino.
     *
     * @return Usuario.
     */
    public Usuario getDestinoUsuario() {
        return destinoUsuario;
    }

    /**
     * Establece el usuario destino.
     *
     * @param destinoUsuario Usuario.
     */
    public void setDestinoUsuario(Usuario destinoUsuario) {
        this.destinoUsuario = destinoUsuario;
    }

    /**
     * Obtiene la dependencia del usuario destino.
     *
     * @return Dependencia.
     */
    public Dependencia getDestinoDependencia() {
        return destinoDependencia;
    }

    /**
     * Establece la dependencia del usuario destino.
     *
     * @param destinoDependencia Dependencia.
     */
    public void setDestinoDependencia(Dependencia destinoDependencia) {
        this.destinoDependencia = destinoDependencia;
    }

    /**
     * Obtiene la clasificación del usuario destino.
     *
     * @return Clasificación.
     */
    public Clasificacion getDestinoClasificacion() {
        return destinoClasificacion;
    }

    /**
     * Establece la clasificación del usuario destino.
     *
     * @param destinoClasificacion Clasificación.
     */
    public void setDestinoClasificacion(Clasificacion destinoClasificacion) {
        this.destinoClasificacion = destinoClasificacion;
    }

    /**
     * Obtiene el grado del usuario destino.
     *
     * @return Grado.
     */
    public Grados getDestinoGrado() {
        return destinoGrado;
    }

    /**
     * Establece el grado del usuario destino.
     *
     * @param destinoGrado Grado.
     */
    public void setDestinoGrado(Grados destinoGrado) {
        this.destinoGrado = destinoGrado;
    }

    /**
     * Obtiene el cargo del usuario destino.
     *
     * @return Cargo.
     */
    public String getDestinoCargo() {
        return destinoCargo;
    }

    /**
     * Establece el cargo del usuario destino.
     *
     * @param destinoCargo Cargo.
     */
    public void setDestinoCargo(String destinoCargo) {
        this.destinoCargo = destinoCargo;
    }

    /**
     * Obtiene el número de documentos.
     *
     * @return Número de documentos.
     */
    public Integer getNumeroDocumentos() {
        return numeroDocumentos;
    }

    /**
     * Establece el número de documentos.
     *
     * @param numeroDocumentos Número de documentos.
     */
    public void setNumeroDocumentos(Integer numeroDocumentos) {
        this.numeroDocumentos = numeroDocumentos;
    }

    /**
     * Obtiene la fecha de aprobación.
     *
     * @return Fecha.
     */
    public Date getFechaAprobacion() {
        return (fechaAprobacion == null)
                ? null : new Date(fechaAprobacion.getTime());
    }

    /**
     * Establece la fecha de aprobación.
     *
     * @param fechaAprobacion Fecha.
     */
    public void setFechaAprobacion(Date fechaAprobacion) {
        this.fechaAprobacion = (fechaAprobacion == null)
                ? null : new Date(fechaAprobacion.getTime());
    }

    /**
     * Obtiene el código OFS del acta.
     *
     * @return Código OFS.
     */
    public String getActaOFS() {
        return actaOFS;
    }

    /**
     * Establece el código OFS del acta.
     *
     * @param actaOFS Código OFS.
     */
    public void setActaOFS(String actaOFS) {
        this.actaOFS = actaOFS;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.id);
        hash = 47 * hash + Objects.hashCode(this.activo);
        hash = 47 * hash + Objects.hashCode(this.estado);
        hash = 47 * hash + Objects.hashCode(this.creadorUsuario);
        hash = 47 * hash + Objects.hashCode(this.creadorDependencia);
        hash = 47 * hash + Objects.hashCode(this.creadorGrado);
        hash = 47 * hash + Objects.hashCode(this.creadorCargo);
        hash = 47 * hash + Objects.hashCode(this.fechaCreacion);
        hash = 47 * hash + Objects.hashCode(this.origenUsuario);
        hash = 47 * hash + Objects.hashCode(this.origenDependencia);
        hash = 47 * hash + Objects.hashCode(this.origenClasificacion);
        hash = 47 * hash + Objects.hashCode(this.origenGrado);
        hash = 47 * hash + Objects.hashCode(this.origenCargo);
        hash = 47 * hash + Objects.hashCode(this.destinoUsuario);
        hash = 47 * hash + Objects.hashCode(this.destinoDependencia);
        hash = 47 * hash + Objects.hashCode(this.destinoClasificacion);
        hash = 47 * hash + Objects.hashCode(this.destinoGrado);
        hash = 47 * hash + Objects.hashCode(this.destinoCargo);
        hash = 47 * hash + Objects.hashCode(this.numeroDocumentos);
        hash = 47 * hash + Objects.hashCode(this.fechaAprobacion);
        hash = 47 * hash + Objects.hashCode(this.actaOFS);
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
        final TransferenciaArchivo other = (TransferenciaArchivo) obj;
        if (!Objects.equals(this.estado, other.estado)) {
            return false;
        }
        if (!Objects.equals(this.creadorCargo, other.creadorCargo)) {
            return false;
        }
        if (!Objects.equals(this.origenCargo, other.origenCargo)) {
            return false;
        }
        if (!Objects.equals(this.destinoCargo, other.destinoCargo)) {
            return false;
        }
        if (!Objects.equals(this.actaOFS, other.actaOFS)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.activo, other.activo)) {
            return false;
        }
        if (!Objects.equals(this.creadorUsuario, other.creadorUsuario)) {
            return false;
        }
        if (!Objects.equals(this.creadorDependencia, other.creadorDependencia)) {
            return false;
        }
        if (!Objects.equals(this.creadorGrado, other.creadorGrado)) {
            return false;
        }
        if (!Objects.equals(this.fechaCreacion, other.fechaCreacion)) {
            return false;
        }
        if (!Objects.equals(this.origenUsuario, other.origenUsuario)) {
            return false;
        }
        if (!Objects.equals(this.origenDependencia, other.origenDependencia)) {
            return false;
        }
        if (!Objects.equals(this.origenClasificacion, other.origenClasificacion)) {
            return false;
        }
        if (!Objects.equals(this.origenGrado, other.origenGrado)) {
            return false;
        }
        if (!Objects.equals(this.destinoUsuario, other.destinoUsuario)) {
            return false;
        }
        if (!Objects.equals(this.destinoDependencia, other.destinoDependencia)) {
            return false;
        }
        if (!Objects.equals(this.destinoClasificacion, other.destinoClasificacion)) {
            return false;
        }
        if (!Objects.equals(this.destinoGrado, other.destinoGrado)) {
            return false;
        }
        if (!Objects.equals(this.numeroDocumentos, other.numeroDocumentos)) {
            return false;
        }
        return Objects.equals(this.fechaAprobacion, other.fechaAprobacion);
    }

}
