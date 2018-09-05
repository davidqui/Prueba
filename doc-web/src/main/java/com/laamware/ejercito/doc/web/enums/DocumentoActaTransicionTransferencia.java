package com.laamware.ejercito.doc.web.enums;

/**
 * Estados aplicables a para el proceso de registro de actas.
 *
 * @author jgarcia@controltechcg.com
 * @since 1.8
 * @version 05/15/2018 Issue #162 (SICDI-Controltech) feature-162.
 */
public enum DocumentoActaTransicionTransferencia {

    /**
     * Registra la información inicial del acta.
     */
    SELECCIONAR_USUARIOS(151),
    /**
     * Acta anulada.
     */
    GENERAR_NUMERO_RADICADO(150),
    /**
     * El acta cuenta con número de radicación generado y se encuentra en espera
     * de enviarlo al usuario registro.
     */
    NUMERO_DE_RADICACION_GENERADO(152),
    /**
     * El acta se encuentra digitalizada y archivada en el sistema.
     */
    ACTA_DIGITALIZADA(153),
    /**
     * Registra los usuarios asociados al acta.
     */
    REGISTRO_DE_USUARIOS_DEL_ACTA(154),
    /**
     * Registra los usuarios asociados al acta.
     */
    ENVIO_REGISTRO(155),
    /**
     * El acta cuenta con número de radicación generado y se encuentra en espera
     * de la carga del archivo
     */
    CARGA_ACTA(156),
    /**
     * El acta cuenta con el archivo cargado y se encuentra en espera
     * de la aprobación o devolución, por parte del usuario creador.
     */
    VALIDAR_ACTA(157);

    private final Integer id;

    private DocumentoActaTransicionTransferencia(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

}
