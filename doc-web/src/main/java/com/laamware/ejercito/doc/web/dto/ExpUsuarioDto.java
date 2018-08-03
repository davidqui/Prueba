
package com.laamware.ejercito.doc.web.dto;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author egonzalezm
 * @since 1.8
 * @version 08/02/2018
 */
public class ExpUsuarioDto implements Serializable{

    private static final long serialVersionUID = 6238300369430978161L;

    private Long expId;
    private Long expUsuId;
    private String permiso;
    private String nombre;
    private String cargo;
    private String fecCreacion;
    private String fecModificacion;
    private String clasificacion;

    public ExpUsuarioDto(Long expId, Long expUsuId, String permiso, String nombre, String cargo, String fecCreacion, String fecModificacion, String clasificacion) {
        this.expId = expId;
        this.expUsuId = expUsuId;
        this.permiso = permiso;
        this.nombre = nombre;
        this.cargo = cargo;
        this.fecCreacion = fecCreacion;
        this.fecModificacion = fecModificacion;
        this.clasificacion = clasificacion;
    }

    public Long getExpId() {
        return expId;
    }

    public void setExpId(Long expId) {
        this.expId = expId;
    }

    public Long getExpUsuId() {
        return expUsuId;
    }

    public void setExpUsuId(Long expUsuId) {
        this.expUsuId = expUsuId;
    }

    public String getPermiso() {
        return permiso;
    }

    public void setPermiso(String permiso) {
        this.permiso = permiso;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getFecCreacion() {
        return fecCreacion;
    }

    public void setFecCreacion(String fecCreacion) {
        this.fecCreacion = fecCreacion;
    }

    public String getFecModificacion() {
        return fecModificacion;
    }

    public void setFecModificacion(String fecModificacion) {
        this.fecModificacion = fecModificacion;
    }

    public String getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(String clasificacion) {
        this.clasificacion = clasificacion;
    }
}