package com.laamware.ejercito.doc.web.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "GRADO")
@LaamLabel("Grados")
public class Grados extends AuditActivoModifySupport {

    @Id
    @LaamLabel("Sigla")
    @Column(name = "GRA_ID")
    @LaamListColumn(order = 20)
    @LaamCreate(order = 20)
    private String id;

    @Column(name = "GRA_NOMBRE")
    @LaamLabel("Nombre")
    @LaamCreate(order = 10)
    @LaamListColumn(order = 10)
    private String nombre;

    /*
         * 2017-10-05 edison.gonzalez@controltechcg.com Issue #131: Variable que 
	 * permite almacenar el peso.
     */
    @Column(name = "GRA_PESO_ORDEN", precision = 0)
    @LaamLabel("Peso")
    @LaamCreate(order = 30)
    @LaamListColumn(order = 30)
    private Integer pesoOrden;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getPesoOrden() {
        return pesoOrden;
    }

    public void setPesoOrden(Integer pesoOrden) {
        this.pesoOrden = pesoOrden;
    }
}
