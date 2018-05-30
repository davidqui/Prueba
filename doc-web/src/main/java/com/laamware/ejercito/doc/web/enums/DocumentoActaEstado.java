package com.laamware.ejercito.doc.web.enums;

/**
 * Estados aplicables a para el proceso de registro de actas.
 *
 * @author jgarcia@controltechcg.com
 * @since 1.8
 * @version 05/15/2018 Issue #162 (SICDI-Controltech) feature-162.
 */
public enum DocumentoActaEstado {

    /**
     * Registra la información inicial del acta.
     */
    REGISTRO_DE_DATOS_DEL_ACTA(150),
    /**
     * Acta anulada.
     */
    ANULADO(151),
    /**
     * El acta cuenta con número de radicación generado y se encuentra en espera
     * de la carga del archivo.
     */
    NUMERO_DE_RADICACION_GENERADO(152),
    /**
     * El acta se encuentra digitalizada y archivada en el sistema.
     */
    ACTA_DIGITALIZADA(153),
    /**
     * Registra los usuarios asociados al acta.
     */
    REGISTRO_DE_USUARIOS_DEL_ACTA(154);

    private final Integer id;

    private DocumentoActaEstado(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

}
