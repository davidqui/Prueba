package com.laamware.ejercito.doc.web.dto;

/**
 *
 * @author edison.gonzalez@controltechcg.com
 * @since 1.8
 * @version 25/07/2018 Issue #182 (SICDI-Controltech) feature-182.
 */
public class NotificacionxUsuarioDTO {

    private Integer tnfId;
    private String nombre;
    private boolean activo;

    public NotificacionxUsuarioDTO(Integer tnfId, String nombre, boolean activo) {
        this.tnfId = tnfId;
        this.nombre = nombre;
        this.activo = activo;
    }

    public Integer getTnfId() {
        return tnfId;
    }

    public void setTnfId(Integer tnfId) {
        this.tnfId = tnfId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
