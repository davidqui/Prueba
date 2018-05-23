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
 * Relación de usuarios por documento tipo acta.
 *
 * @author jgarcia@controltechcg.com
 * @since 1.8
 * @version 05/23/2018 Issue #162 (SICDI-Controltech) feature-162.
 */
@Entity
@Table(name = "USUARIO_X_DOCUMENTO_ACTA")
@SuppressWarnings("PersistenceUnitPresent")
public class UsuarioXDocumentoActa implements Serializable {

    private static final long serialVersionUID = -6650547734598013582L;

    private static final String SEQUENCE_NAME = "USUARIO_X_DOCUMENTO_ACTA_SEQ";

    @Id
    @GenericGenerator(name = SEQUENCE_NAME, strategy = "sequence",
            parameters = {
                @Parameter(name = "sequence", value = SEQUENCE_NAME)
                ,@Parameter(name = "allocationSize", value = "1")
            })
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    @Basic(optional = false)
    @Column(name = "UXA_ID")
    private Integer id;

    @ManyToOne
    @Basic(optional = false)
    @JoinColumn(name = "USU_ID")
    private Usuario usuario;

    @ManyToOne
    @Basic(optional = false)
    @JoinColumn(name = "CAR_ID")
    private Cargo cargo;

    @ManyToOne
    @Basic(optional = false)
    @JoinColumn(name = "DOC_ID")
    private Documento documento;

    @Basic(optional = false)
    @Column(name = "ACTIVO")
    private Boolean activo;

    @ManyToOne
    @Basic(optional = false)
    @JoinColumn(name = "QUIEN", updatable = false, insertable = true, nullable = false)
    private Usuario quien;

    @Basic(optional = false)
    @Column(name = "CUANDO", updatable = false, insertable = true, nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date cuando;

    /**
     * Constructor vacío.
     */
    public UsuarioXDocumentoActa() {
    }

    /**
     * Constructor.
     *
     * @param id ID.
     */
    public UsuarioXDocumentoActa(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public Documento getDocumento() {
        return documento;
    }

    public void setDocumento(Documento documento) {
        this.documento = documento;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
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

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.id);
        hash = 37 * hash + Objects.hashCode(this.usuario);
        hash = 37 * hash + Objects.hashCode(this.cargo);
        hash = 37 * hash + Objects.hashCode(this.documento);
        hash = 37 * hash + Objects.hashCode(this.activo);
        hash = 37 * hash + Objects.hashCode(this.quien);
        hash = 37 * hash + Objects.hashCode(this.cuando);
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
        final UsuarioXDocumentoActa other = (UsuarioXDocumentoActa) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.usuario, other.usuario)) {
            return false;
        }
        if (!Objects.equals(this.cargo, other.cargo)) {
            return false;
        }
        if (!Objects.equals(this.documento, other.documento)) {
            return false;
        }
        if (!Objects.equals(this.activo, other.activo)) {
            return false;
        }
        if (!Objects.equals(this.quien, other.quien)) {
            return false;
        }
        return Objects.equals(this.cuando, other.cuando);
    }

    @Override
    public String toString() {
        return "UsuarioXDocumentoActa{" + "id=" + id + ", usuario=" + usuario + ", cargo=" + cargo + ", documento=" + documento + ", activo=" + activo + ", quien=" + quien + ", cuando=" + cuando + '}';
    }
}
