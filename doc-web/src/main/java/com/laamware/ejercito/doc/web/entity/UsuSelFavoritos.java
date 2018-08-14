/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.laamware.ejercito.doc.web.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 *
 * @author egonzalezm
 * @since 1.8
 * @version 08/14/2018
 */
@Entity
@Table(name = "USU_SEL_FAVORITOS")
@XmlRootElement
public class UsuSelFavoritos implements Serializable {

    private static final long serialVersionUID = 2610226341798500045L;

    @GenericGenerator(name = "seq_USU_SEL_FAVORITOS", strategy = "sequence",
            parameters = {
                @Parameter(name = "sequence", value = "seq_USU_SEL_FAVORITOS")
                ,@Parameter(name = "allocationSize", value = "1")
            })
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "seq_USU_SEL_FAVORITOS")
    @Id
    @Basic(optional = false)
    @Column(name = "USU_SEL_ID")
    private Integer usuSelId;
    @Basic(optional = false)
    @Column(name = "CONTADOR")
    private Integer contador;
    @JoinColumn(name = "USU_ID", referencedColumnName = "USU_ID")
    @ManyToOne(optional = false)
    private Usuario usuId;
    @JoinColumn(name = "USU_FAV", referencedColumnName = "USU_ID")
    @ManyToOne(optional = false)
    private Usuario usuFav;

    public UsuSelFavoritos() {
    }

    public UsuSelFavoritos(Integer usuSelId) {
        this.usuSelId = usuSelId;
    }

    public UsuSelFavoritos(Integer usuSelId, Integer contador) {
        this.usuSelId = usuSelId;
        this.contador = contador;
    }

    public Integer getUsuSelId() {
        return usuSelId;
    }

    public void setUsuSelId(Integer usuSelId) {
        this.usuSelId = usuSelId;
    }

    public Integer getContador() {
        return contador;
    }

    public void setContador(Integer contador) {
        this.contador = contador;
    }

    public Usuario getUsuId() {
        return usuId;
    }

    public void setUsuId(Usuario usuId) {
        this.usuId = usuId;
    }

    public Usuario getUsuFav() {
        return usuFav;
    }

    public void setUsuFav(Usuario usuFav) {
        this.usuFav = usuFav;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (usuSelId != null ? usuSelId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsuSelFavoritos)) {
            return false;
        }
        UsuSelFavoritos other = (UsuSelFavoritos) object;
        if ((this.usuSelId == null && other.usuSelId != null) || (this.usuSelId != null && !this.usuSelId.equals(other.usuSelId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.laamware.ejercito.doc.web.entity.UsuSelFavoritos[ usuSelId=" + usuSelId + " ]";
    }

}
