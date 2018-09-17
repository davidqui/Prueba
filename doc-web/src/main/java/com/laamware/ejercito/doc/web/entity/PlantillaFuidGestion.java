
package com.laamware.ejercito.doc.web.entity;

import java.io.Serializable;
import java.util.Date;
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
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 *
 * @author egonzalezm
 * @since 1.8
 * @version 09/11/2018
 */
@Entity
@Table(name = "PLANTILLA_FUID_GESTION")
@SuppressWarnings("PersistenceUnitPresent")
@XmlRootElement
public class PlantillaFuidGestion implements Serializable {

    private static final long serialVersionUID = -3422358052269185263L;

    @GenericGenerator(name = "PLANTILLA_FUID_GESTION_SEQ", strategy = "sequence",
            parameters = {
                @Parameter(name = "sequence", value = "PLANTILLA_FUID_GESTION_SEQ")
                ,@Parameter(name = "allocationSize", value = "1")
            })
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "PLANTILLA_FUID_GESTION_SEQ")
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "PFG_ID")
    private Integer pfgId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ACTIVO")
    private Boolean activo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "NOMBRE_ARCHIVO")
    private String nombreArchivo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TAMANYO_ARCHIVO")
    private Integer tamanyoArchivo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "FIRMA_MD5")
    private String firmaMd5;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "CODIGO_OFS")
    private String codigoOfs;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CUANDO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date cuando;
    @ManyToOne
    @JoinColumn(name = "QUIEN")
    private Usuario usuario;

    public PlantillaFuidGestion() {
    }

    public PlantillaFuidGestion(Integer pfgId) {
        this.pfgId = pfgId;
    }

    public PlantillaFuidGestion(Boolean activo, String nombreArchivo, Integer tamanyoArchivo, String firmaMd5, String codigoOfs, Usuario usuario, Date cuando) {
        this.activo = activo;
        this.nombreArchivo = nombreArchivo;
        this.tamanyoArchivo = tamanyoArchivo;
        this.firmaMd5 = firmaMd5;
        this.codigoOfs = codigoOfs;
        this.cuando = cuando;
        this.usuario = usuario;
    }

    public Integer getPfgId() {
        return pfgId;
    }

    public void setPfgId(Integer pfgId) {
        this.pfgId = pfgId;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public Integer getTamanyoArchivo() {
        return tamanyoArchivo;
    }

    public void setTamanyoArchivo(Integer tamanyoArchivo) {
        this.tamanyoArchivo = tamanyoArchivo;
    }

    public String getFirmaMd5() {
        return firmaMd5;
    }

    public void setFirmaMd5(String firmaMd5) {
        this.firmaMd5 = firmaMd5;
    }

    public String getCodigoOfs() {
        return codigoOfs;
    }

    public void setCodigoOfs(String codigoOfs) {
        this.codigoOfs = codigoOfs;
    }

    public Date getCuando() {
        return cuando;
    }

    public void setCuando(Date cuando) {
        this.cuando = cuando;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pfgId != null ? pfgId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PlantillaFuidGestion)) {
            return false;
        }
        PlantillaFuidGestion other = (PlantillaFuidGestion) object;
        if ((this.pfgId == null && other.pfgId != null) || (this.pfgId != null && !this.pfgId.equals(other.pfgId))) {
            return false;
        }
        return true;
    }
}
