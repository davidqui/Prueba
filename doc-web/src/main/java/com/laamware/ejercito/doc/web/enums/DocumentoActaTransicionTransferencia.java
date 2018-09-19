package com.laamware.ejercito.doc.web.enums;

/**
 * Transiciones del proceso de registro de actas, cuando se realiza un acta de
 * transferencia de archivo.
 *
 * @author edison.gonzalez@controltechcg.com
 * @since 1.8
 * @version 05/09/2018 Issue #4 (SICDI-Controltech) feature-4.
 */
public enum DocumentoActaTransicionTransferencia {

    /**
     * Registra la información de usuarios del acta.
     */
    SELECCIONAR_USUARIOS(151),
    /**
     * Generación del número de radicado
     */
    GENERAR_NUMERO_RADICADO(150),
    /**
     * Se envia el proceso a usuario de registro.
     */
    ENVIAR_REGISTRO(154),
    /**
     * Generación del sticker del acta.
     */
    GENERAR_STICKER(157),
    /**
     * Validación del acta.
     */
    VALIDAR(155),
    /**
     * El acta se encuentra digitalizada y archivada en el sistema.
     */
    APROBAR_ARCHIVAR(152);

    private final Integer id;

    private DocumentoActaTransicionTransferencia(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

}
