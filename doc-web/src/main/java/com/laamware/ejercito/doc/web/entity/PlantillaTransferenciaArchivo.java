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
import javax.validation.constraints.Size;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * Plantilla para el acta de las transferencias de archivo.
 *
 * @author jgarcia@controltechcg.com
 * @since Ago 25, 2017
 * @version 1.0.0 (feature-120).
 */
@Entity
@Table(name = "PLANTILLA_TRANSF_ARCHIVO")
@SuppressWarnings("PersistenceUnitPresent")
public class PlantillaTransferenciaArchivo implements Serializable {

    /**
     * SerialVersionUID.
     */
    private static final long serialVersionUID = 3696585326778657167L;

    /**
     * ID.
     */
    @Id
    @GenericGenerator(name = "PLANTILLA_TRANSF_ARCHIVO_SEQ", strategy = "sequence",
            parameters = {
                @Parameter(name = "sequence", value = "PLANTILLA_TRANSF_ARCHIVO_SEQ")
                ,@Parameter(name = "allocationSize", value = "1")
            })
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "PLANTILLA_TRANSF_ARCHIVO_SEQ")
    @Column(name = "PTA_ID")
    private Integer id;

    /**
     * Bandera de activo.
     */
    @Column(name = "ACTIVO")
    private Boolean activo;

    /**
     * Nombre de archivo.
     */
    @Size(max = 512)
    @Column(name = "NOMBRE_ARCHIVO")
    private String nombreArchivo;

    /**
     * Tamaño de archivo.
     */
    @Column(name = "TAMANYO_ARCHIVO")
    private Integer tamanyoArchivo;

    /**
     * Firma MD5.
     */
    @Size(max = 32)
    @Column(name = "FIRMA_MD5")
    private String firmaMD5;

    /**
     * Código OFS.
     */
    @Size(max = 32)
    @Column(name = "CODIGO_OFS")
    private String codigoOFS;

    /**
     * Usuario.
     */
    @ManyToOne
    @JoinColumn(name = "QUIEN")
    private Usuario usuario;

    /**
     * Fecha.
     */
    @Column(name = "FIRMA_MD5")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fecha;

    /**
     * Constructor vacío.
     */
    public PlantillaTransferenciaArchivo() {
    }

    /**
     * Constructor.
     *
     * @param activo Bandera de activo.
     * @param nombreArchivo Nombre del archivo.
     * @param tamanyoArchivo Tamaño del archivo.
     * @param firmaMD5 Firma MD5.
     * @param codigoOFS Código OFS.
     * @param usuario Usuario.
     * @param fecha Fecha.
     */
    public PlantillaTransferenciaArchivo(Boolean activo, String nombreArchivo,
            Integer tamanyoArchivo, String firmaMD5, String codigoOFS,
            Usuario usuario, Date fecha) {
        this.activo = activo;
        this.nombreArchivo = nombreArchivo;
        this.tamanyoArchivo = tamanyoArchivo;
        this.firmaMD5 = firmaMD5;
        this.codigoOFS = codigoOFS;
        this.usuario = usuario;
        this.fecha = new Date(fecha.getTime());
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
     * Obtiene el nombre del archivo.
     *
     * @return Nombre del archivo.
     */
    public String getNombreArchivo() {
        return nombreArchivo;
    }

    /**
     * Establece el nombre del archivo.
     *
     * @param nombreArchivo Nombre del archivo.
     */
    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    /**
     * Obtiene el tamaño del archivo.
     *
     * @return Tamaño del archivo.
     */
    public Integer getTamanyoArchivo() {
        return tamanyoArchivo;
    }

    /**
     * Establece el tamaño del archivo.
     *
     * @param tamanyoArchivo Tamaño del archivo.
     */
    public void setTamanyoArchivo(Integer tamanyoArchivo) {
        this.tamanyoArchivo = tamanyoArchivo;
    }

    /**
     * Obtiene la firma MD5.
     *
     * @return Firma MD5.
     */
    public String getFirmaMD5() {
        return firmaMD5;
    }

    /**
     * Establece la firma MD5.
     *
     * @param firmaMD5 Firma MD5.
     */
    public void setFirmaMD5(String firmaMD5) {
        this.firmaMD5 = firmaMD5;
    }

    /**
     * Obtiene el código OFS.
     *
     * @return Código OFS.
     */
    public String getCodigoOFS() {
        return codigoOFS;
    }

    /**
     * Establece el código OFS.
     *
     * @param codigoOFS Código OFS.
     */
    public void setCodigoOFS(String codigoOFS) {
        this.codigoOFS = codigoOFS;
    }

    /**
     * Obtiene el usuario.
     *
     * @return Usuario.
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Establece el usuario.
     *
     * @param usuario Usuario.
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Obtiene la fecha.
     *
     * @return Fecha.
     */
    public Date getFecha() {
        return new Date(fecha.getTime());
    }

    /**
     * Establece la fecha.
     *
     * @param fecha Fecha.
     */
    public void setFecha(Date fecha) {
        this.fecha = new Date(fecha.getTime());
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + Objects.hashCode(this.id);
        hash = 17 * hash + Objects.hashCode(this.activo);
        hash = 17 * hash + Objects.hashCode(this.nombreArchivo);
        hash = 17 * hash + Objects.hashCode(this.tamanyoArchivo);
        hash = 17 * hash + Objects.hashCode(this.firmaMD5);
        hash = 17 * hash + Objects.hashCode(this.codigoOFS);
        hash = 17 * hash + Objects.hashCode(this.usuario);
        hash = 17 * hash + Objects.hashCode(this.fecha);
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
        final PlantillaTransferenciaArchivo other = (PlantillaTransferenciaArchivo) obj;
        if (!Objects.equals(this.nombreArchivo, other.nombreArchivo)) {
            return false;
        }
        if (!Objects.equals(this.firmaMD5, other.firmaMD5)) {
            return false;
        }
        if (!Objects.equals(this.codigoOFS, other.codigoOFS)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.activo, other.activo)) {
            return false;
        }
        if (!Objects.equals(this.tamanyoArchivo, other.tamanyoArchivo)) {
            return false;
        }
        if (!Objects.equals(this.usuario, other.usuario)) {
            return false;
        }
        return Objects.equals(this.fecha, other.fecha);
    }

}
