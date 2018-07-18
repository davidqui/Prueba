package com.laamware.ejercito.doc.web.enums;

/**
 * Modo de selección de usuario para los procesos de registro de documentos de
 * acta.
 *
 * @author jgarcia@controltechcg.com
 * @since 1.8
 * @version 05/28/2018 Issue #162 (SICDI-Controltech) feature-162.
 */
public enum DocumentoActaUsuarioSeleccion {
    /**
     * Selección de mín y máx 0 usuarios.
     */
    SELECCION_0_0,
    /**
     * Selección de mín y máx 1 usuario.
     */
    SELECCION_1_1,
    /**
     * Selección de mín 1 y máx n usuarios.
     */
    SELECCION_1_N;
}
