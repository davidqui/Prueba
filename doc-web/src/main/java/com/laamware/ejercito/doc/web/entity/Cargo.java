/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.laamware.ejercito.doc.web.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 *
 * @author egonzalezm
 */
@Entity
@Table(name = "CARGO")
@XmlRootElement
@LaamLabel("Cargos")
public class Cargo extends AuditModifySupport implements Serializable {

    private static final long serialVersionUID = 5906447631373801191L;

    @Id
    @GenericGenerator(name = "CARGO_SEQ", strategy = "sequence", parameters = {
        @Parameter(name = "sequence", value = "CARGO_SEQ")
        ,
			@Parameter(name = "allocationSize", value = "1")})
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CARGO_SEQ")
    @Basic(optional = false)
    @Column(name = "CAR_ID")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "CAR_NOMBRE")
    @LaamLabel("Descripción")
    @LaamListColumn(order = 20)
    @LaamCreate(order = 20)
    private String carNombre;
    @Basic(optional = false)
    @Column(name = "CAR_IND_LDAP")
    @LaamLabel("Indicador Ldap")
    @LaamListColumn(order = 30)
    @LaamCreate(order = 30)
    private Boolean carIndLdap;
    @OneToMany(mappedBy = "usuCargo7Id")
    private List<Usuario> usuarioList;
    @OneToMany(mappedBy = "usuCargo10Id")
    private List<Usuario> usuarioList1;
    @OneToMany(mappedBy = "usuCargo9Id")
    private List<Usuario> usuarioList2;
    @OneToMany(mappedBy = "usuCargo8Id")
    private List<Usuario> usuarioList3;
    @OneToMany(mappedBy = "usuCargo6Id")
    private List<Usuario> usuarioList4;
    @OneToMany(mappedBy = "usuCargo5Id")
    private List<Usuario> usuarioList5;
    @OneToMany(mappedBy = "usuCargo4Id")
    private List<Usuario> usuarioList6;
    @OneToMany(mappedBy = "usuCargo3Id")
    private List<Usuario> usuarioList7;
    @OneToMany(mappedBy = "usuCargo2Id")
    private List<Usuario> usuarioList8;
    @OneToMany(mappedBy = "usuCargo1Id")
    private List<Usuario> usuarioList9;
    @OneToMany(mappedBy = "usuCargoPrincipalId")
    private List<Usuario> usuarioList10;

    public Cargo() {
    }

    public Cargo(Integer carId) {
        this.id = carId;
    }

    public Cargo(Integer carId, String carNombre, Boolean carIndLdap) {
        this.id = carId;
        this.carNombre = carNombre;
        this.carIndLdap = carIndLdap;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCarNombre() {
        return carNombre;
    }

    public void setCarNombre(String carNombre) {
        this.carNombre = carNombre;
    }

    public Boolean getCarIndLdap() {
        return carIndLdap;
    }

    public void setCarIndLdap(Boolean carIndLdap) {
        this.carIndLdap = carIndLdap;
    }

    @XmlTransient
    public List<Usuario> getUsuarioList() {
        return usuarioList;
    }

    public void setUsuarioList(List<Usuario> usuarioList) {
        this.usuarioList = usuarioList;
    }

    @XmlTransient
    public List<Usuario> getUsuarioList1() {
        return usuarioList1;
    }

    public void setUsuarioList1(List<Usuario> usuarioList1) {
        this.usuarioList1 = usuarioList1;
    }

    @XmlTransient
    public List<Usuario> getUsuarioList2() {
        return usuarioList2;
    }

    public void setUsuarioList2(List<Usuario> usuarioList2) {
        this.usuarioList2 = usuarioList2;
    }

    @XmlTransient
    public List<Usuario> getUsuarioList3() {
        return usuarioList3;
    }

    public void setUsuarioList3(List<Usuario> usuarioList3) {
        this.usuarioList3 = usuarioList3;
    }

    @XmlTransient
    public List<Usuario> getUsuarioList4() {
        return usuarioList4;
    }

    public void setUsuarioList4(List<Usuario> usuarioList4) {
        this.usuarioList4 = usuarioList4;
    }

    @XmlTransient
    public List<Usuario> getUsuarioList5() {
        return usuarioList5;
    }

    public void setUsuarioList5(List<Usuario> usuarioList5) {
        this.usuarioList5 = usuarioList5;
    }

    @XmlTransient
    public List<Usuario> getUsuarioList6() {
        return usuarioList6;
    }

    public void setUsuarioList6(List<Usuario> usuarioList6) {
        this.usuarioList6 = usuarioList6;
    }

    @XmlTransient
    public List<Usuario> getUsuarioList7() {
        return usuarioList7;
    }

    public void setUsuarioList7(List<Usuario> usuarioList7) {
        this.usuarioList7 = usuarioList7;
    }

    @XmlTransient
    public List<Usuario> getUsuarioList8() {
        return usuarioList8;
    }

    public void setUsuarioList8(List<Usuario> usuarioList8) {
        this.usuarioList8 = usuarioList8;
    }

    @XmlTransient
    public List<Usuario> getUsuarioList9() {
        return usuarioList9;
    }

    public void setUsuarioList9(List<Usuario> usuarioList9) {
        this.usuarioList9 = usuarioList9;
    }

    @XmlTransient
    public List<Usuario> getUsuarioList10() {
        return usuarioList10;
    }

    public void setUsuarioList10(List<Usuario> usuarioList10) {
        this.usuarioList10 = usuarioList10;
    }

    @Override
    public String toString() {
        if (id != null) {
            return String.valueOf(id);
        }
        return null;
    }

}
