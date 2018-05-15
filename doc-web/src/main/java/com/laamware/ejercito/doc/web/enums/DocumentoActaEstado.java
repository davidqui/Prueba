package com.laamware.ejercito.doc.web.enums;

import com.laamware.ejercito.doc.web.entity.DocumentoActa;

/**
 * Estados aplicables a {@link DocumentoActa}.
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
    ACTA_DIGITALIZADA(153);

    private final Integer id;

    private DocumentoActaEstado(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

}
