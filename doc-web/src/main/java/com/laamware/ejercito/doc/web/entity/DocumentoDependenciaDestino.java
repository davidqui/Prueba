package com.laamware.ejercito.doc.web.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.laamware.ejercito.doc.web.util.GeneralUtils;

/**
 *
 * @author Rafael G Blanco
 * @deprecated 2018-04-16 jgarcia@controltechcg.com Issue #156
 * (SICDI-Controltech) feature-156 Se reemplaza por la entidad
 * {@link DependenciaCopiaMultidestino}.
 */
@Entity
@Table(name = "DOCUMENTO_DEP_DESTINO")
@Deprecated
public class DocumentoDependenciaDestino {

    public static DocumentoDependenciaDestino create() {
        DocumentoDependenciaDestino x = new DocumentoDependenciaDestino();
        x.id = GeneralUtils.newId();
        return x;
    }

    @Id
    @Column(name = "DDEP_ID")
    private String id;

    @ManyToOne
    @JoinColumn(name = "DOC_ID")
    private Documento documento;

    @ManyToOne
    @JoinColumn(name = "DEP_ID")
    private Dependencia dependencia;

    @ManyToOne
    @JoinColumn(name = "QUIEN_MOD")
    private Usuario elabora;

    @Column(name = "CUANDO_MOD")
    private Date cuandoInserta;

    public DocumentoDependenciaDestino() {
        // TODO Auto-generated constructor stub
    }

    @Column(name = "ACTIVO")
    private Boolean activo = Boolean.TRUE;

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Documento getDocumento() {
        return documento;
    }

    public void setDocumento(Documento documento) {
        this.documento = documento;
    }

    public Usuario getElabora() {
        return elabora;
    }

    public void setElabora(Usuario elabora) {
        this.elabora = elabora;
    }

    public Date getCuandoInserta() {
        return cuandoInserta;
    }

    public void setCuandoInserta(Date cuandoInserta) {
        this.cuandoInserta = cuandoInserta;
    }

    public Dependencia getDependencia() {
        return dependencia;
    }

    public void setDependencia(Dependencia dependencia) {
        this.dependencia = dependencia;
    }

}
