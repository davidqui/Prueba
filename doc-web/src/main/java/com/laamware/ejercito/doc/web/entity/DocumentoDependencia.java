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

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "DOCUMENTO_DEPENDENCIA")
public class DocumentoDependencia implements Serializable{

    private static final long serialVersionUID = -91798746378120176L;

    @Id
    @GenericGenerator(name = "DOCUMENTO_DEPENDENCIA_SEQ", strategy = "sequence", parameters = {
        @Parameter(name = "sequence", value = "DOCUMENTO_DEPENDENCIA_SEQ")
        ,
			@Parameter(name = "allocationSize", value = "1")})
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DOCUMENTO_DEPENDENCIA_SEQ")
    @Column(name = "DCDP_ID")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "DOC_ID")
    private Documento documento;

    @ManyToOne
    @JoinColumn(name = "DEP_ID")
    private Dependencia dependencia;

    @ManyToOne
    @JoinColumn(name = "TRD_ID")
    private Trd trd;

    /*
        * 2017-03-01 edison.gonzalez@controltechcg.com Issue #151 (SICDI-Controltech):
        * Se adiciona el cargo con el cual se archiva el documento.
     */
    @JoinColumn(name = "CARGO_ID", referencedColumnName = "CAR_ID")
    @ManyToOne
    private Cargo cargo;

    @Column(name = "ACTIVO")
    private Boolean activo = Boolean.TRUE;

    @Column(name = "CUANDO")
    private Date cuando;

    @Column(name = "QUIEN")
    private Integer quien;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Documento getDocumento() {
        return documento;
    }

    public void setDocumento(Documento documento) {
        this.documento = documento;
    }

    public Dependencia getDependencia() {
        return dependencia;
    }

    public void setDependencia(Dependencia dependencia) {
        this.dependencia = dependencia;
    }

    public Trd getTrd() {
        return trd;
    }

    public void setTrd(Trd trd) {
        this.trd = trd;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Date getCuando() {
        return cuando;
    }

    public void setCuando(Date cuando) {
        this.cuando = cuando;
    }

    public Integer getQuien() {
        return quien;
    }

    public void setQuien(Integer quien) {
        this.quien = quien;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.id);
        hash = 97 * hash + Objects.hashCode(this.documento);
        hash = 97 * hash + Objects.hashCode(this.dependencia);
        hash = 97 * hash + Objects.hashCode(this.trd);
        hash = 97 * hash + Objects.hashCode(this.cargo);
        hash = 97 * hash + Objects.hashCode(this.activo);
        hash = 97 * hash + Objects.hashCode(this.cuando);
        hash = 97 * hash + Objects.hashCode(this.quien);
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
        final DocumentoDependencia other = (DocumentoDependencia) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.documento, other.documento)) {
            return false;
        }
        if (!Objects.equals(this.dependencia, other.dependencia)) {
            return false;
        }
        if (!Objects.equals(this.trd, other.trd)) {
            return false;
        }
        if (!Objects.equals(this.cargo, other.cargo)) {
            return false;
        }
        if (!Objects.equals(this.activo, other.activo)) {
            return false;
        }
        if (!Objects.equals(this.cuando, other.cuando)) {
            return false;
        }
        if (!Objects.equals(this.quien, other.quien)) {
            return false;
        }
        return true;
    }
    
    
}
