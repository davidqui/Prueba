package com.laamware.ejercito.doc.web.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;


/**
 * Wildcards para la validaciòn de las plantillas.
 *
 * @author samuel.delgado@controltechcg.com
 * @since 1.8
 * @version 27/06/2018 Issue #176 (SICDI-Controltech) feature-176
 */

@Entity
@Table(name = "WILDCARD_PLANTILLAS_VALIDADAS")
@SuppressWarnings("PersistenceUnitPresent")
@LaamLabel("Notificaciones")
public class WildcardPlantilla implements Serializable {
    
    private static final String SEQUENCE_NAME = "WILDCARD_PV_SEQ";

    @Id
    @GenericGenerator(name = SEQUENCE_NAME, strategy = "sequence",
            parameters = {
                @Parameter(name = "sequence", value = SEQUENCE_NAME)
                ,@Parameter(name = "allocationSize", value = "1")
            })
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    @Basic(optional = false)
    @Column(name = "WPV_ID")
    private Integer id;

    @Basic(optional = false)
    @Column(name = "TEXTO")
    private String texto;

    @ManyToOne
    @JoinColumn(name = "QUIEN", updatable = false, insertable = true, nullable = false)
    private Usuario quien;

    @Column(name = "CUANDO", updatable = false, insertable = true, nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date cuando;

    @ManyToOne
    @JoinColumn(name = "QUIEN_MOD", updatable = true, insertable = true, nullable = false)
    private Usuario quienMod;

    @Column(name = "CUANDO_MOD")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date cuandoMod;
        
    @ManyToMany(mappedBy = "wildCards_plantilla")
    private List<Plantilla> plantillas;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
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

    public Usuario getQuienMod() {
        return quienMod;
    }

    public void setQuienMod(Usuario quienMod) {
        this.quienMod = quienMod;
    }

    public Date getCuandoMod() {
        return cuandoMod;
    }

    public void setCuandoMod(Date cuandoMod) {
        this.cuandoMod = cuandoMod;
    }

    public List<Plantilla> getPlantilla() {
        return plantillas;
    }

    public void setPlantilla(List<Plantilla> plantillas) {
        this.plantillas = plantillas;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.id);
        hash = 29 * hash + Objects.hashCode(this.texto);
        hash = 29 * hash + Objects.hashCode(this.quien);
        hash = 29 * hash + Objects.hashCode(this.cuando);
        hash = 29 * hash + Objects.hashCode(this.quienMod);
        hash = 29 * hash + Objects.hashCode(this.cuandoMod);
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
        final WildcardPlantilla other = (WildcardPlantilla) obj;
        if (!Objects.equals(this.texto, other.texto)) {
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
        if (!Objects.equals(this.quienMod, other.quienMod)) {
            return false;
        }
        if (!Objects.equals(this.cuandoMod, other.cuandoMod)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "WildcardPlantilla{" + "id=" + id + ", texto=" + texto + "}";
    }

}
