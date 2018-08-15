/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.laamware.ejercito.doc.web.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
 * @author edison.gonzalez@controltechcg.com
 * @since 1.8
 * @version 25/07/2018 Issue #182 (SICDI-Controltech) feature-182.
 */
@Entity
@Table(name = "NOTIFICACION_X_USUARIO")
@XmlRootElement
public class NotificacionXUsuario implements Serializable {

    private static final long serialVersionUID = -7583241974544815150L;

    @Id
    @GenericGenerator(name = "NOTIFICACION_X_USUARIO_SEQ", strategy = "sequence", parameters = {
        @Parameter(name = "sequence", value = "NOTIFICACION_X_USUARIO_SEQ"),
			@Parameter(name = "allocationSize", value = "1")})
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOTIFICACION_X_USUARIO_SEQ")
    @Basic(optional = false)
    @Column(name = "NOT_USU_ID")
    private Integer notUsuId;
    @JoinColumn(name = "TNF_ID", referencedColumnName = "TNF_ID")
    @ManyToOne(optional = false)
    private TipoNotificacion tnfId;
    @JoinColumn(name = "USU_ID", referencedColumnName = "USU_ID")
    @ManyToOne(optional = false)
    private Usuario usuId;

    public NotificacionXUsuario() {
    }

    public NotificacionXUsuario(Integer notUsuId) {
        this.notUsuId = notUsuId;
    }

    public Integer getNotUsuId() {
        return notUsuId;
    }

    public void setNotUsuId(Integer notUsuId) {
        this.notUsuId = notUsuId;
    }

    public TipoNotificacion getTnfId() {
        return tnfId;
    }

    public void setTnfId(TipoNotificacion tnfId) {
        this.tnfId = tnfId;
    }

    public Usuario getUsuId() {
        return usuId;
    }

    public void setUsuId(Usuario usuId) {
        this.usuId = usuId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (notUsuId != null ? notUsuId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NotificacionXUsuario)) {
            return false;
        }
        NotificacionXUsuario other = (NotificacionXUsuario) object;
        if ((this.notUsuId == null && other.notUsuId != null) || (this.notUsuId != null && !this.notUsuId.equals(other.notUsuId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.laamware.ejercito.doc.web.entity.NotificacionXUsuario[ notUsuId=" + notUsuId + " ]";
    }

}
