package com.laamware.ejercito.doc.web.enums;

/**
 * Modos de edición para el proceso de registro de actas.
 *
 * @author jgarcia@controltechcg.com
 * @since 1.8
 * @version 05/15/2018 Issue #162 (SICDI-Controltech) feature-162.
 */
public enum DocumentoActaMode {
    /**
     * En este modo el formulario debe permitir el registro y edición de la
     * información básica.
     */
    EDICION_INFORMACION,
    /**
     * En este modo el formulario no debe permitir el registro y edición de la
     * información básica, pero debe permitir la carga del archivo digital del
     * acta.
     */
    CARGA_ACTA_DIGITAL,
    /**
     * En este modo el formulario únicamente debe permitir consultar la
     * información del acta.
     */
    SOLO_CONSULTA,
    /**
     * En este modo, el formulario no debe permitir el registro y edición de la
     * información básica, pero debe permitir la selección de los usuarios-.
     */
    SELECCION_USUARIOS;
}
