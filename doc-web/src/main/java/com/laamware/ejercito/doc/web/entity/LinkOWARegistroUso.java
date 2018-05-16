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
 * Registro auditable de uso del enlace al OWA.
 *
 * @author Jorge Alfonso García Espinosa
 * @since 1.8
 * @version 05/02/2018 Issue #159 (SICDI-Controltech) feature-159
 */
@Entity
@Table(name = "LINK_OWA_REGISTRO_USO")
@SuppressWarnings("PersistenceUnitPresent")
public class LinkOWARegistroUso implements Serializable {

    private static final long serialVersionUID = 3628483573036848812L;

    private static final String SEQUENCE_NAME = "LINK_OWA_REGISTRO_USO_SQ";

    @Id
    @GenericGenerator(name = SEQUENCE_NAME, strategy = "sequence",
            parameters = {
                @Parameter(name = "sequence", value = SEQUENCE_NAME)
                ,@Parameter(name = "allocationSize", value = "1")
            })
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    @Basic(optional = false)
    @Column(name = "LOWA_ID")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "QUIEN", updatable = false, insertable = true, nullable = false)
    private Usuario quien;

    @Column(name = "CUANDO", updatable = false, insertable = true, nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date cuando;

    @ManyToOne
    @JoinColumn(name = "DOM_CODIGO")
    private Dominio dominio;

    @Basic(optional = false)
    @Column(name = "LINK_URL")
    private String linkURL;

    /**
     * Constructor vacío.
     */
    public LinkOWARegistroUso() {
    }

    /**
     * Constructor.
     *
     * @param id ID.
     */
    public LinkOWARegistroUso(Integer id) {
        this.id = id;
    }

    /**
     * Consructor.
     *
     * @param quien Usuario a registrar.
     * @param cuando Fecha de la acción.
     * @param dominio Dominio del usuario.
     * @param linkURL URL del enlace accedido.
     */
    public LinkOWARegistroUso(Usuario quien, Date cuando, Dominio dominio, String linkURL) {
        this.quien = quien;
        this.cuando = (cuando == null) ? null : new Date(cuando.getTime());
        this.dominio = dominio;
        this.linkURL = linkURL;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Usuario getQuien() {
        return quien;
    }

    public void setQuien(Usuario quien) {
        this.quien = quien;
    }

    public Date getCuando() {
        return (cuando == null) ? null : new Date(cuando.getTime());
    }

    public void setCuando(Date cuando) {
        this.cuando = (cuando == null) ? null : new Date(cuando.getTime());
    }

    public Dominio getDominio() {
        return dominio;
    }

    public void setDominio(Dominio dominio) {
        this.dominio = dominio;
    }

    public String getLinkURL() {
        return linkURL;
    }

    public void setLinkURL(String linkURL) {
        this.linkURL = linkURL;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.id);
        hash = 29 * hash + Objects.hashCode(this.quien);
        hash = 29 * hash + Objects.hashCode(this.cuando);
        hash = 29 * hash + Objects.hashCode(this.dominio);
        hash = 29 * hash + Objects.hashCode(this.linkURL);
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
        final LinkOWARegistroUso other = (LinkOWARegistroUso) obj;
        if (!Objects.equals(this.linkURL, other.linkURL)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.quien, other.quien)) {
            return false;
        }
        if (!Objects.equals(this.cuando, other.cuando)) {
            return false;
        }
        return Objects.equals(this.dominio, other.dominio);
    }

    @Override
    public String toString() {
        return "LinkOWARegistroUso{" + "id=" + id + ", quien=" + quien + ", cuando=" + cuando + ", dominio=" + dominio + ", linkURL=" + linkURL + '}';
    }

}
