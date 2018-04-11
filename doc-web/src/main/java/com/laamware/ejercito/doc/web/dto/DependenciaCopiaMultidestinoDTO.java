package com.laamware.ejercito.doc.web.dto;

import com.laamware.ejercito.doc.web.entity.DependenciaCopiaMultidestino;

/**
 * DTO para el envío de información a través de JSON para
 * {@link DependenciaCopiaMultidestino}.
 *
 * @author Jorge Alfonso García Espinosa
 * @since 1.8
 * @version 04/10/2018 (SICDI-Controltech Issue #156 feature-156)
 */
public class DependenciaCopiaMultidestinoDTO {

    private boolean ok;
    private String message;

    private int id;
    private String nombreDependencia;
    private String fechaCreacion;
    private String nombreUsuarioCreador;

    /**
     * Constructor vacío.
     */
    public DependenciaCopiaMultidestinoDTO() {
    }

    /**
     * Constructor.
     *
     * @param ok Bandera que indica si corresponde a un mensaje de éxito
     * ({@code true}) o un mensaje de error ({@code false}).
     * @param message Mensaje a presentar.
     */
    public DependenciaCopiaMultidestinoDTO(boolean ok, String message) {
        this.ok = ok;
        this.message = message;
    }

    /**
     * Constructor.
     *
     * @param ok Bandera que indica si corresponde a un mensaje de éxito
     * ({@code true}) o un mensaje de error ({@code false}).
     * @param message Mensaje a presentar.
     * @param id ID del registro generado.
     * @param nombreDependencia Nombre de la dependencia.
     * @param fechaCreacion Texto formateado con la fecha de creación del
     * registro.
     * @param nombreUsuarioCreador Nombre completo del usuario creador.
     */
    public DependenciaCopiaMultidestinoDTO(boolean ok, String message, int id, String nombreDependencia, String fechaCreacion, String nombreUsuarioCreador) {
        this.ok = ok;
        this.message = message;
        this.id = id;
        this.nombreDependencia = nombreDependencia;
        this.fechaCreacion = fechaCreacion;
        this.nombreUsuarioCreador = nombreUsuarioCreador;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreDependencia() {
        return nombreDependencia;
    }

    public void setNombreDependencia(String nombreDependencia) {
        this.nombreDependencia = nombreDependencia;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getNombreUsuarioCreador() {
        return nombreUsuarioCreador;
    }

    public void setNombreUsuarioCreador(String nombreUsuarioCreador) {
        this.nombreUsuarioCreador = nombreUsuarioCreador;
    }

}
