package com.laamware.ejercito.doc.web.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 *
 * @author sdelgadom
 */
@Entity
@Table(name = "DESTINO_EXTERNO")
@XmlRootElement
@LaamLabel("Destinos Externos")
public class DestinoExterno extends AuditModifySupport implements Serializable{
    
    @Id
    @GenericGenerator(name = "DESTINO_EXTERNO_SEQ", strategy = "sequence", parameters = {
        @Parameter(name = "sequence", value = "DESTINO_EXTERNO_SEQ"),
        @Parameter(name = "allocationSize", value = "1")})
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DESTINO_EXTERNO_SEQ")
    @Basic(optional = false)
    @Column(name = "ADE_ID")
    private Integer id;
    
    @Basic(optional = false)
    @Column(name = "NOMBRE", length = 128)
    @LaamLabel("Nombre")
    @LaamListColumn(order = 20)
    @LaamCreate(order = 20)
    private String nombre;
    
    @Basic(optional = false)
    @Column(name = "SIGLA")
    @LaamLabel("Sigla")
    @LaamListColumn(order = 30)
    @LaamCreate(order = 30)
    private String sigla;
    
    @Basic(optional = false)
    @Column(name = "ACTIVO")
    private Boolean activo;
    
    @Basic(optional = false)
    @Column(name = "TIPO")
    private Integer tipo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }
    
    @Override
    public String toString() {
        return "DestinoExterno{" + "id=" + id + ", nombre=" + nombre + ", sigla=" + sigla + ", activo=" + activo + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.id);
        hash = 17 * hash + Objects.hashCode(this.nombre);
        hash = 17 * hash + Objects.hashCode(this.sigla);
        hash = 17 * hash + Objects.hashCode(this.activo);
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
        final DestinoExterno other = (DestinoExterno) obj;
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        if (!Objects.equals(this.sigla, other.sigla)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.activo, other.activo)) {
            return false;
        }
        return true;
    }
    
    
}
