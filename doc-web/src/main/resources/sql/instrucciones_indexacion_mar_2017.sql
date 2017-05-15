-- 
-- ALMACENAJE
-- 
CREATE INDEX ALMACENAJE_ALM_CODIGO_IDX ON ALMACENAJE(ALM_CODIGO);
CREATE INDEX ALMACENAJE_TAL_ID_IDX     ON ALMACENAJE(TAL_ID);
CREATE INDEX ALMACENAJE_CJ_ID_IDX      ON ALMACENAJE(CJ_ID);
CREATE INDEX ALMACENAJE_ACTIVO_IDX     ON ALMACENAJE(ACTIVO);
CREATE INDEX ALMACENAJE_QUIEN_IDX      ON ALMACENAJE(QUIEN);
CREATE INDEX ALMACENAJE_QUIEN_MOD_IDX  ON ALMACENAJE(QUIEN_MOD);

-- 
-- ARCHIVO
--
CREATE INDEX ARCHIVO_EDF_ID_IDX             ON ARCHIVO(EDF_ID);
CREATE INDEX ARCHIVO_PISO_ID_IDX            ON ARCHIVO(PISO_ID);
CREATE INDEX ARCHIVO_AREA_ID_IDX            ON ARCHIVO(AREA_ID);
CREATE INDEX ARCHIVO_MOD_ID_IDX             ON ARCHIVO(MOD_ID);
CREATE INDEX ARCHIVO_EST_ID_IDX             ON ARCHIVO(EST_ID);
CREATE INDEX ARCHIVO_ENT_ID_IDX             ON ARCHIVO(ENT_ID);
CREATE INDEX ARCHIVO_TCA_ID_IDX             ON ARCHIVO(TCA_ID);
CREATE INDEX ARCHIVO_ARC_NUM_CAJA_IDX       ON ARCHIVO(ARC_NUM_CAJA);
CREATE INDEX ARCHIVO_TAL_ID_IDX             ON ARCHIVO(TAL_ID);
CREATE INDEX ARCHIVO_ARC_NUM_ALMACENAJE_IDX ON ARCHIVO(ARC_NUM_ALMACENAJE);

-- 
-- ARCHIVO_EXPEDIENTE
-- 

CREATE INDEX ARCHIVO_EXPEDIENTE_ARC_ID_IDX  ON ARCHIVO_EXPEDIENTE(ARC_ID);
CREATE INDEX ARCHIVO_EXPEDIENTE_EXP_ID_IDX  ON ARCHIVO_EXPEDIENTE(EXP_ID);
CREATE INDEX ARCHIVO_EXPEDIENTE_ACTIVO_IDX  ON ARCHIVO_EXPEDIENTE(ACTIVO);
CREATE INDEX ARCHIVO_EXPEDIENTE_QUIEN_IDX   ON ARCHIVO_EXPEDIENTE(QUIEN);
CREATE INDEX ARCHV_EXPEDIENTE_QUIEN_MOD_IDX ON ARCHIVO_EXPEDIENTE(QUIEN_MOD);

-- 
-- AREA
-- 

CREATE INDEX AREA_AREA_SIGLA_IDX  ON AREA(AREA_SIGLA);
CREATE INDEX AREA_AREA_CODIGO_IDX ON AREA(AREA_CODIGO);
CREATE INDEX AREA_PISO_ID_IDX     ON AREA(PISO_ID);
CREATE INDEX AREA_ACTIVO_IDX      ON AREA(ACTIVO);
CREATE INDEX AREA_QUIEN_IDX       ON AREA(QUIEN);
CREATE INDEX AREA_QUIEN_MOD_IDX   ON AREA(QUIEN_MOD);

-- 
-- BANDEJA
-- 

CREATE INDEX BANDEJA_BAN_CODIGO_IDX  ON BANDEJA(BAN_CODIGO);
CREATE INDEX BANDEJA_BAN_COMPILA_IDX ON BANDEJA(BAN_COMPILA);
CREATE INDEX BANDEJA_ACTIVO_IDX      ON BANDEJA(ACTIVO);
CREATE INDEX BANDEJA_QUIEN_IDX       ON BANDEJA(QUIEN);
CREATE INDEX BANDEJA_QUIEN_MOD_IDX   ON BANDEJA(QUIEN_MOD);

-- 
-- BARRIO
-- 

CREATE INDEX BARRIO_MPO_ID_IDX    ON BARRIO(MPO_ID);
CREATE INDEX BARRIO_ACTIVO_IDX    ON BARRIO(ACTIVO);
CREATE INDEX BARRIO_QUIEN_IDX     ON BARRIO(QUIEN);
CREATE INDEX BARRIO_QUIEN_MOD_IDX ON BARRIO(QUIEN_MOD);

-- 
-- CAJA
-- 

CREATE INDEX CAJA_CJ_CODIGO_IDX ON CAJA(CJ_CODIGO);
CREATE INDEX CAJA_TC_ID_IDX     ON CAJA(TC_ID);
CREATE INDEX CAJA_ENT_ID_IDX    ON CAJA(ENT_ID);
CREATE INDEX CAJA_ACTIVO_IDX    ON CAJA(ACTIVO);
CREATE INDEX CAJA_QUIEN_IDX     ON CAJA(QUIEN);
CREATE INDEX CAJA_QUIEN_MOD_IDX ON CAJA(QUIEN_MOD);

-- 
-- CLASIFICACION
-- 

CREATE INDEX CLASIFICACION_QUIEN_IDX     ON CLASIFICACION(QUIEN);
CREATE INDEX CLASIFICACION_QUIEN_MOD_IDX ON CLASIFICACION(QUIEN_MOD);
CREATE INDEX CLASIFICACION_ACTIVO_IDX    ON CLASIFICACION(ACTIVO);
CREATE INDEX CLASIFICACION_CLA_ORDEN_IDX ON CLASIFICACION(CLA_ORDEN);

-- 
-- DEPARTAMENTO
-- 

CREATE INDEX DEPARTAMENTO_PAI_ID_IDX    ON DEPARTAMENTO(PAI_ID);
CREATE INDEX DEPARTAMENTO_ACTIVO_IDX    ON DEPARTAMENTO(ACTIVO);
CREATE INDEX DEPARTAMENTO_QUIEN_IDX     ON DEPARTAMENTO(QUIEN);
CREATE INDEX DEPARTAMENTO_QUIEN_MOD_IDX ON DEPARTAMENTO(QUIEN_MOD);

-- 
-- DEPENDENCIA
-- 

CREATE INDEX DEPENDENCIA_DEP_CODIGO_IDX     ON DEPENDENCIA(DEP_CODIGO);
CREATE INDEX DEPENDENCIA_DEP_PADRE_IDX      ON DEPENDENCIA(DEP_PADRE);
CREATE INDEX DPNDNC_USUIDFIRMAPRINCIPAL_IDX ON DEPENDENCIA(USU_ID_FIRMA_PRINCIPAL);
CREATE INDEX DPNDNC_USUIDJEFE_ENCARGADO_IDX ON DEPENDENCIA(USU_ID_JEFE_ENCARGADO);
CREATE INDEX DEPENDENCIA_ACTIVO_IDX         ON DEPENDENCIA(ACTIVO);
CREATE INDEX DEPENDENCIA_QUIEN_IDX          ON DEPENDENCIA(QUIEN);
CREATE INDEX DEPENDENCIA_QUIEN_MOD_IDX      ON DEPENDENCIA(QUIEN_MOD);
CREATE INDEX DEPENDENCIA_TRD_ID_IDX         ON DEPENDENCIA(TRD_ID);
CREATE INDEX DEPENDENCIA_DEPCODIGOORFEO_IDX ON DEPENDENCIA(DEP_CODIGO_ORFEO);
CREATE INDEX DEPENDENCIA_DEP_SIGLA_IDX      ON DEPENDENCIA(DEP_SIGLA);

-- 
-- DEPENDENCIA_TRD
-- 

CREATE INDEX DEPENDENCIA_TRD_DEP_ID_IDX ON DEPENDENCIA_TRD(DEP_ID);
CREATE INDEX DEPENDENCIA_TRD_TRD_ID_IDX ON DEPENDENCIA_TRD(TRD_ID);
CREATE INDEX DEPENDENCIA_TRD_ACTIVO_IDX ON DEPENDENCIA_TRD(ACTIVO);
CREATE INDEX DEPENDENCIA_TRD_QUIEN_IDX  ON DEPENDENCIA_TRD(QUIEN);

-- 
-- DESTINATARIO
-- 

CREATE INDEX DESTINATARIO_DES_DOCUMENTO_IDX ON DESTINATARIO(DES_DOCUMENTO);
CREATE INDEX DESTINATARIO_DES_SIGLA_IDX     ON DESTINATARIO(DES_SIGLA);
CREATE INDEX DESTINATARIO_TDE_ID_IDX        ON DESTINATARIO(TDE_ID);
CREATE INDEX DESTINATARIO_ACTIVO_IDX        ON DESTINATARIO(ACTIVO);
CREATE INDEX DESTINATARIO_QUIEN_IDX         ON DESTINATARIO(QUIEN);
CREATE INDEX DESTINATARIO_QUIEN_MOD_IDX     ON DESTINATARIO(QUIEN_MOD);
CREATE INDEX DESTINATARIO_MPO_ID_IDX        ON DESTINATARIO(MPO_ID);
CREATE INDEX DESTINATARIO_REF_ID_IDX        ON DESTINATARIO(REF_ID);

-- 
-- DOCUMENTO
--  

CREATE INDEX DOCUMENTO_TRD_ID_IDX           ON DOCUMENTO(TRD_ID);
CREATE INDEX DOCUMENTO_QUIEN_IDX            ON DOCUMENTO(QUIEN);
CREATE INDEX DOCUMENTO_QUIEN_MOD_IDX        ON DOCUMENTO(QUIEN_MOD);
CREATE INDEX DOCUMENTO_CLA_ID_IDX           ON DOCUMENTO(CLA_ID);
CREATE INDEX DOCUMENTO_USU_ID_FIRMA_IDX     ON DOCUMENTO(USU_ID_FIRMA);
CREATE INDEX DOCUMENTO_USU_ID_ELABORA_IDX   ON DOCUMENTO(USU_ID_ELABORA);
CREATE INDEX DOCUMENTO_USUIDVISTO_BUENO_IDX ON DOCUMENTO(USU_ID_VISTO_BUENO);
CREATE INDEX DOCUMENTO_USU_ID_APRUEBA_IDX   ON DOCUMENTO(USU_ID_APRUEBA);
CREATE INDEX DOCUMENTO_DOC_RADICADO_IDX     ON DOCUMENTO(DOC_RADICADO);
CREATE INDEX DOCUMENTO_DOC_RELACIONADO_IDX  ON DOCUMENTO(DOC_RELACIONADO);
CREATE INDEX DOCUMENTO_EXP_ID_IDX           ON DOCUMENTO(EXP_ID);
CREATE INDEX DOCUMENTO_DOC_PDF_IDX          ON DOCUMENTO(DOC_PDF);
CREATE INDEX DOCUMENTO_DEP_ID_DES_IDX       ON DOCUMENTO(DEP_ID_DES);
CREATE INDEX DOCUMENTO_PLA_ID_IDX           ON DOCUMENTO(PLA_ID);
CREATE INDEX DOCUMENTO_DOC_CONTENT_FILE_IDX ON DOCUMENTO(DOC_CONTENT_FILE);
CREATE INDEX DOCUMENTO_DEP_ID_REM_IDX       ON DOCUMENTO(DEP_ID_REM);
CREATE INDEX DOCUMENTO_ESTADO_TMP_IDX       ON DOCUMENTO(ESTADO_TMP);
CREATE INDEX DOCUMENTO_DOCDOCXDOCUMENTO_IDX ON DOCUMENTO(DOC_DOCX_DOCUMENTO);
CREATE INDEX DOCUMENTO_USUIDULTACCION_IDX   ON DOCUMENTO(USU_ID_ULTIMA_ACCION);
CREATE INDEX DOCUMENTO_CODVALIDASCANNER_IDX ON DOCUMENTO(CODIGO_VALIDA_SCANNER);
CREATE INDEX DOCUMENTO_IDUSUVALCODSCANN_IDX ON DOCUMENTO(ID_USUARIO_VALIDA_COD_SCANNER);
CREATE INDEX DOCUMENTO_ESTCODVALSCANNER_IDX ON DOCUMENTO(ESTADO_CODIGO_VALIDA_SCANNER);

-- 
-- DOCUMENTO_ADJUNTO
-- 

CREATE INDEX DOCUMENTO_ADJUNTO_TDO_ID_IDX   ON DOCUMENTO_ADJUNTO(TDO_ID);
CREATE INDEX DOCUMENTO_ADJUNTO_QUIEN_IDX    ON DOCUMENTO_ADJUNTO(QUIEN);
CREATE INDEX DOCUMENTO_ADJUNTO_DOC_ID_IDX   ON DOCUMENTO_ADJUNTO(DOC_ID);
CREATE INDEX DOCUMENTO_ADJUNTO_DADCNTNT_IDX ON DOCUMENTO_ADJUNTO(DAD_CONTENT);
CREATE INDEX DOCUMENTO_ADJUNTO_ACTIVO_IDX   ON DOCUMENTO_ADJUNTO(ACTIVO);

-- 
-- DOCUMENTO_DEP_DESTINO
-- 

CREATE INDEX DOCUMENTO_DEPDESTINODEP_ID_IDX ON DOCUMENTO_DEP_DESTINO(DEP_ID);
CREATE INDEX DOCUMENTO_DEPDSTQUIENMOD_IDX   ON DOCUMENTO_DEP_DESTINO(QUIEN_MOD);
CREATE INDEX DOCUMENTO_DEPDESTINOACTIVO_IDX ON DOCUMENTO_DEP_DESTINO(ACTIVO);

-- 
-- DOCUMENTO_DEPENDENCIA
-- 

CREATE INDEX DOCUMENTO_DEPEND_DOC_ID_IDX ON DOCUMENTO_DEPENDENCIA(DOC_ID);
CREATE INDEX DOCUMENTO_DEPEND_DEP_ID_IDX ON DOCUMENTO_DEPENDENCIA(DEP_ID);
CREATE INDEX DOCUMENTO_DEPEND_ACTIVO_IDX ON DOCUMENTO_DEPENDENCIA(ACTIVO);
CREATE INDEX DOCUMENTO_DEPEND_QUIEN_IDX  ON DOCUMENTO_DEPENDENCIA(QUIEN);
CREATE INDEX DOCUMENTO_DEPEND_TRD_ID_IDX ON DOCUMENTO_DEPENDENCIA(TRD_ID);

-- 
-- DOCUMENTO_OBS
-- 

CREATE INDEX DOCUMENTO_OBS_QUIEN_IDX  ON DOCUMENTO_OBS(QUIEN);
CREATE INDEX DOCUMENTO_OBS_DOC_ID_IDX ON DOCUMENTO_OBS(DOC_ID);

-- 
-- DOCUMENTO_PDF
-- 

CREATE INDEX DOCUMENTO_PDF_DOCPDF_ID_IDX ON DOCUMENTO_PDF(DOCPDF_ID);
CREATE INDEX DOCUMENTO_PDF_PASO_IDX      ON DOCUMENTO_PDF(PASO);

-- 
-- DOCUMENTO_PRESTAMO
-- 

CREATE INDEX DOCUMENTO_PRESTAMO_DOC_ID_IDX  ON DOCUMENTO_PRESTAMO(DOC_ID);
CREATE INDEX DOCUMENTO_PRESTAMO_PRE_ID_IDX  ON DOCUMENTO_PRESTAMO(PRE_ID);
CREATE INDEX DOCUMENTO_PRESTAMO_ACTIVO_IDX  ON DOCUMENTO_PRESTAMO(ACTIVO);
CREATE INDEX DOCUMENTO_PRESTAMO_QUIEN_IDX   ON DOCUMENTO_PRESTAMO(QUIEN);
CREATE INDEX DOCUMENTO_PRESTAMOQUIENMOD_IDX ON DOCUMENTO_PRESTAMO(QUIEN_MOD);

-- 
-- DOCUMENTO_USU_APRUEBA
-- 

CREATE INDEX DOCUMENTO_USUAPRUEBADOC_ID_IDX ON DOCUMENTO_USU_APRUEBA(DOC_ID);
CREATE INDEX DOCUMENTO_USUAPRUSUIDAPR_IDX   ON DOCUMENTO_USU_APRUEBA(USU_ID_APRUEBA);

-- 
-- DOCUMENTO_USU_ELABORA
-- 

CREATE INDEX DOCUMENTO_USUELABORADOC_ID_IDX ON DOCUMENTO_USU_ELABORA(DOC_ID);
CREATE INDEX DOCUMENTO_USUELABUSUIDELAB_IDX ON DOCUMENTO_USU_ELABORA(USU_ID_ELABORA);

-- 
-- DOCUMENTO_USU_FIRMA
-- 

CREATE INDEX DOCUMENTO_USU_FIRMA_DOC_ID_IDX ON DOCUMENTO_USU_FIRMA(DOC_ID);
CREATE INDEX DOCUMENTO_USUFRMUSUIDFIRMA_IDX ON DOCUMENTO_USU_FIRMA(USU_ID_FIRMA);

-- 
-- DOCUMENTO_USU_VISTOS_BUENOS
-- 

CREATE INDEX DOCUMENTO_USUVSTSBNSDOC_ID_IDX ON DOCUMENTO_USU_VISTOS_BUENOS(DOC_ID);
CREATE INDEX DCMNT_USUVSTSBNSUSUIDVSTBN_IDX ON DOCUMENTO_USU_VISTOS_BUENOS(USU_ID_VISTO_BUENO);

-- 
-- EDIFICIO
-- 

CREATE INDEX EDIFICIO_EDF_CODIGO_IDX ON EDIFICIO(EDF_CODIGO);
CREATE INDEX EDIFICIO_EDF_SIGLA_IDX  ON EDIFICIO(EDF_SIGLA);
CREATE INDEX EDIFICIO_PAI_ID_IDX     ON EDIFICIO(PAI_ID);
CREATE INDEX EDIFICIO_DPT_ID_IDX     ON EDIFICIO(DPT_ID);
CREATE INDEX EDIFICIO_MPO_ID_IDX     ON EDIFICIO(MPO_ID);
CREATE INDEX EDIFICIO_BAR_ID_IDX     ON EDIFICIO(BAR_ID);
CREATE INDEX EDIFICIO_ACTIVO_IDX     ON EDIFICIO(ACTIVO);
CREATE INDEX EDIFICIO_QUIEN_IDX      ON EDIFICIO(QUIEN);
CREATE INDEX EDIFICIO_QUIEN_MOD_IDX  ON EDIFICIO(QUIEN_MOD);

-- 
-- ENTREPANO
-- 

CREATE INDEX ENTREPANO_ENT_CODIGO_IDX ON ENTREPANO(ENT_CODIGO);
CREATE INDEX ENTREPANO_ESTT_ID_IDX    ON ENTREPANO(ESTT_ID);
CREATE INDEX ENTREPANO_ACTIVO_IDX     ON ENTREPANO(ACTIVO);
CREATE INDEX ENTREPANO_QUIEN_IDX      ON ENTREPANO(QUIEN);
CREATE INDEX ENTREPANO_QUIEN_MOD_IDX  ON ENTREPANO(QUIEN_MOD);

-- 
-- ERRORS
-- 

CREATE INDEX ERRORS_DOC_ID_IDX ON ERRORS(DOC_ID);
CREATE INDEX ERRORS_CODE_IDX   ON ERRORS(CODE);

-- 
-- ESTADO_EXPEDIENTE
-- 

CREATE INDEX ESTADO_EXPEDIENTE_QUIEN_IDX    ON ESTADO_EXPEDIENTE(QUIEN);
CREATE INDEX ESTADO_EXPEDIENTE_QUIENMOD_IDX ON ESTADO_EXPEDIENTE(QUIEN_MOD);
CREATE INDEX ESTADO_EXPEDIENTE_ACTIVO_IDX   ON ESTADO_EXPEDIENTE(ACTIVO);

-- 
-- ESTANTE
-- 

CREATE INDEX ESTANTE_ESTT_CODIGO_IDX ON ESTANTE(ESTT_CODIGO);
CREATE INDEX ESTANTE_MOD_ID_IDX      ON ESTANTE(MOD_ID);
CREATE INDEX ESTANTE_ACTIVO_IDX      ON ESTANTE(ACTIVO);
CREATE INDEX ESTANTE_QUIEN_IDX       ON ESTANTE(QUIEN);
CREATE INDEX ESTANTE_QUIEN_MOD_IDX   ON ESTANTE(QUIEN_MOD);

-- 
-- EXPEDIENTE
-- 

CREATE INDEX EXPEDIENTE_EXP_CODIGO_IDX ON EXPEDIENTE(EXP_CODIGO);
CREATE INDEX EXPEDIENTE_DEP_ID_IDX     ON EXPEDIENTE(DEP_ID);
CREATE INDEX EXPEDIENTE_TDF_ID_IDX     ON EXPEDIENTE(TDF_ID);
CREATE INDEX EXPEDIENTE_PLA_ID_IDX     ON EXPEDIENTE(PLA_ID);
CREATE INDEX EXPEDIENTE_QUIEN_IDX      ON EXPEDIENTE(QUIEN);
CREATE INDEX EXPEDIENTE_QUIEN_MOD_IDX  ON EXPEDIENTE(QUIEN_MOD);
CREATE INDEX EXPEDIENTE_ACTIVO_IDX     ON EXPEDIENTE(ACTIVO);
CREATE INDEX EXPEDIENTE_TRD_ID_IDX     ON EXPEDIENTE(TRD_ID);
CREATE INDEX EXPEDIENTE_ESEX_ID_IDX    ON EXPEDIENTE(ESEX_ID);
CREATE INDEX EXPEDIENTE_EXES_ID_IDX    ON EXPEDIENTE(EXES_ID);

-- 
-- EXPEDIENTE_ESTADO
-- 

CREATE INDEX EXPEDIENTE_ESTADO_ACTIVO_IDX   ON EXPEDIENTE_ESTADO(ACTIVO);
CREATE INDEX EXPEDIENTE_ESTADO_QUIEN_IDX    ON EXPEDIENTE_ESTADO(QUIEN);
CREATE INDEX EXPEDIENTE_ESTADO_QUIENMOD_IDX ON EXPEDIENTE_ESTADO(QUIEN_MOD);
CREATE INDEX EXPEDIENTE_ESTADO_EXP_ID_IDX   ON EXPEDIENTE_ESTADO(EXP_ID);
CREATE INDEX EXPEDIENTE_ESTADO_ESEX_ID_IDX  ON EXPEDIENTE_ESTADO(ESEX_ID);
CREATE INDEX EXPEDIENTE_ESTDUSUIDTRANSF_IDX ON EXPEDIENTE_ESTADO(USU_ID_TRANSFERENCIA);

-- 
-- EXPEDIENTE_PRESTAMO
-- 

CREATE INDEX EXPEDIENTE_PRESTAMO_EXP_ID_IDX ON EXPEDIENTE_PRESTAMO(EXP_ID);
CREATE INDEX EXPEDIENTE_PRESTAMO_ACTIVO_IDX ON EXPEDIENTE_PRESTAMO(ACTIVO);
CREATE INDEX EXPEDIENTE_PRESTAMO_QUIEN_IDX  ON EXPEDIENTE_PRESTAMO(QUIEN);
CREATE INDEX EXPEDIENTE_PRSTAMOQUIENMOD_IDX ON EXPEDIENTE_PRESTAMO(QUIEN_MOD);
CREATE INDEX EXPEDIENTE_PRESTAMO_PRE_ID_IDX ON EXPEDIENTE_PRESTAMO(PRE_ID);

-- 
-- FORMATO
-- 

CREATE INDEX FORMATO_FORM_CONTENT_IDX ON FORMATO(FORM_CONTENT);
CREATE INDEX FORMATO_DOC_ID_IDX       ON FORMATO(DOC_ID);
CREATE INDEX FORMATO_TRD_ID_IDX       ON FORMATO(TRD_ID);
CREATE INDEX FORMATO_ACTIVO_IDX       ON FORMATO(ACTIVO);
CREATE INDEX FORMATO_QUIEN_IDX        ON FORMATO(QUIEN);
CREATE INDEX FORMATO_QUIEN_MOD_IDX    ON FORMATO(QUIEN_MOD);

-- 
-- GRADO
-- 

CREATE INDEX GRADO_GRA_NOMBRE_IDX ON GRADO(GRA_NOMBRE);
CREATE INDEX GRADO_ACTIVO_IDX     ON GRADO(ACTIVO);
CREATE INDEX GRADO_QUIEN_IDX      ON GRADO(QUIEN);
CREATE INDEX GRADO_CUANDO_MOD_IDX ON GRADO(CUANDO_MOD);

-- 
-- H_BANDEJA
-- 

CREATE INDEX H_BANDEJA_BAN_ID_IDX      ON H_BANDEJA(BAN_ID);
CREATE INDEX H_BANDEJA_BAN_CODIGO_IDX  ON H_BANDEJA(BAN_CODIGO);
CREATE INDEX H_BANDEJA_BAN_COMPILA_IDX ON H_BANDEJA(BAN_COMPILA);
CREATE INDEX H_BANDEJA_ACTIVO_IDX      ON H_BANDEJA(ACTIVO);
CREATE INDEX H_BANDEJA_QUIEN_IDX       ON H_BANDEJA(QUIEN);
CREATE INDEX H_BANDEJA_QUIEN_MOD_IDX   ON H_BANDEJA(QUIEN_MOD);

-- 
-- H_JUEGO_RESPUESTA_USUARIO
-- 

CREATE INDEX H_JUEGO_RESPUSUARIO_JPR_ID_IDX ON H_JUEGO_RESPUESTA_USUARIO(JPR_ID);
CREATE INDEX H_JUEGO_RESPUSUARIO_JRE_ID_IDX ON H_JUEGO_RESPUESTA_USUARIO(JRE_ID);
CREATE INDEX H_JUEGO_RESPUSUARIO_USU_ID_IDX ON H_JUEGO_RESPUESTA_USUARIO(USU_ID);
CREATE INDEX H_JUEGO_RESPUSUARIO_ACTIVO_IDX ON H_JUEGO_RESPUESTA_USUARIO(ACTIVO);
CREATE INDEX H_JUEGO_RESPUSUARIO_QUIEN_IDX  ON H_JUEGO_RESPUESTA_USUARIO(QUIEN);

-- 
-- H_PROCESO_INSTANCIA
-- 

CREATE INDEX H_PROCESO_INSTANCIA_PIN_ID_IDX ON H_PROCESO_INSTANCIA(PIN_ID);
CREATE INDEX H_PROCESO_INSTANCIA_PRO_ID_IDX ON H_PROCESO_INSTANCIA(PRO_ID);
CREATE INDEX H_PROCESO_INSTANCIA_QUIEN_IDX  ON H_PROCESO_INSTANCIA(QUIEN);
CREATE INDEX H_PROCESO_INSTANCIA_PES_ID_IDX ON H_PROCESO_INSTANCIA(PES_ID);
CREATE INDEX H_PROCESO_INSTANCIQUIENMOD_IDX ON H_PROCESO_INSTANCIA(QUIEN_MOD);
CREATE INDEX H_PROCESO_INSTANCUSUIDASIG_IDX ON H_PROCESO_INSTANCIA(USU_ID_ASIGNADO);

-- 
-- H_PROCESO_INSTANCIA_VAR
-- 

CREATE INDEX H_PROCESO_INSTVAR_PIV_ID_IDX  ON H_PROCESO_INSTANCIA_VAR(PIV_ID);
CREATE INDEX H_PROCESO_INSTVAR_PIN_ID_IDX  ON H_PROCESO_INSTANCIA_VAR(PIN_ID);
CREATE INDEX H_PROCESO_INSTVAR_PIV_KEY_IDX ON H_PROCESO_INSTANCIA_VAR(PIV_KEY);
CREATE INDEX H_PROCESO_INSTVAR_QUIEN_IDX   ON H_PROCESO_INSTANCIA_VAR(QUIEN);
CREATE INDEX H_PROCESO_INSTVAR_ACTIVO_IDX  ON H_PROCESO_INSTANCIA_VAR(ACTIVO);
CREATE INDEX H_PROCESO_INSTVARQUIENMOD_IDX ON H_PROCESO_INSTANCIA_VAR(QUIEN_MOD);

-- 
-- INFORME
-- 

CREATE INDEX INFORME_INF_ROL_IDX   ON INFORME(INF_ROL);
CREATE INDEX INFORME_ACTIVO_IDX    ON INFORME(ACTIVO);
CREATE INDEX INFORME_QUIEN_IDX     ON INFORME(QUIEN);
CREATE INDEX INFORME_QUIEN_MOD_IDX ON INFORME(QUIEN_MOD);

-- 
-- INSTANCIA_BANDEJA
-- 

CREATE INDEX INSTANCIA_BANDEJA_QUIEN_IDX    ON INSTANCIA_BANDEJA(QUIEN);
CREATE INDEX INSTANCIA_BANDEJA_QUIENMOD_IDX ON INSTANCIA_BANDEJA(QUIEN_MOD);

-- 
-- JUEGO
-- 

CREATE INDEX JUEGO_ACTIVO_IDX    ON JUEGO(ACTIVO);
CREATE INDEX JUEGO_QUIEN_IDX     ON JUEGO(QUIEN);
CREATE INDEX JUEGO_QUIEN_MOD_IDX ON JUEGO(QUIEN_MOD);

-- 
-- JUEGO_AYUDA_USUARIO
-- 

CREATE INDEX JUEGO_AYUDA_USUARIO_JNI_ID_IDX ON JUEGO_AYUDA_USUARIO(JNI_ID);
CREATE INDEX JUEGO_AYUDA_USUARIO_USU_ID_IDX ON JUEGO_AYUDA_USUARIO(USU_ID);
CREATE INDEX JUEGO_AYUDA_USUARIO_AYUDA1_IDX ON JUEGO_AYUDA_USUARIO(AYUDA1);
CREATE INDEX JUEGO_AYUDA_USUARIO_ACTIVO_IDX ON JUEGO_AYUDA_USUARIO(ACTIVO);
CREATE INDEX JUEGO_AYUDA_USUARIO_QUIEN_IDX  ON JUEGO_AYUDA_USUARIO(QUIEN);

-- 
-- JUEGO_AYUDA2_USUARIO
-- 

CREATE INDEX JUEGO_AYUDA2_USUARIOJNI_ID_IDX ON JUEGO_AYUDA2_USUARIO(JNI_ID);
CREATE INDEX JUEGO_AYUDA2_USUARIOUSU_ID_IDX ON JUEGO_AYUDA2_USUARIO(USU_ID);
CREATE INDEX JUEGO_AYUDA2_USUARIOAYUDA2_IDX ON JUEGO_AYUDA2_USUARIO(AYUDA2);
CREATE INDEX JUEGO_AYUDA2_USUARIOACTIVO_IDX ON JUEGO_AYUDA2_USUARIO(ACTIVO);
CREATE INDEX JUEGO_AYUDA2_USUARIOQUIEN_IDX  ON JUEGO_AYUDA2_USUARIO(QUIEN);

-- 
-- JUEGO_NIVEL
-- 

CREATE INDEX JUEGO_NIVEL_JJU_ID_IDX         ON JUEGO_NIVEL(JJU_ID);
CREATE INDEX JUEGO_NIVEL_JNI_DIFICULTAD_IDX ON JUEGO_NIVEL(JNI_DIFICULTAD);
CREATE INDEX JUEGO_NIVEL_ACTIVO_IDX         ON JUEGO_NIVEL(ACTIVO);
CREATE INDEX JUEGO_NIVEL_QUIEN_IDX          ON JUEGO_NIVEL(QUIEN);
CREATE INDEX JUEGO_NIVEL_QUIEN_MOD_IDX      ON JUEGO_NIVEL(QUIEN_MOD);

-- 
-- JUEGO_NIVEL_USUARIO
-- 

CREATE INDEX JUEGO_NIVEL_USUARIO_JNI_ID_IDX ON JUEGO_NIVEL_USUARIO(JNI_ID);
CREATE INDEX JUEGO_NIVEL_USUARIO_USU_ID_IDX ON JUEGO_NIVEL_USUARIO(USU_ID);
CREATE INDEX JUEGO_NIVEL_USUARIO_QUIEN_IDX  ON JUEGO_NIVEL_USUARIO(QUIEN);
CREATE INDEX JUEGO_NIVEL_USUARIO_ACTIVO_IDX ON JUEGO_NIVEL_USUARIO(ACTIVO);

-- 
-- JUEGO_PREGUNTA
-- 

CREATE INDEX JUEGO_PREGUNTA_JNI_ID_IDX    ON JUEGO_PREGUNTA(JNI_ID);
CREATE INDEX JUEGO_PREGUNTA_ACTIVO_IDX    ON JUEGO_PREGUNTA(ACTIVO);
CREATE INDEX JUEGO_PREGUNTA_QUIEN_IDX     ON JUEGO_PREGUNTA(QUIEN);
CREATE INDEX JUEGO_PREGUNTA_QUIEN_MOD_IDX ON JUEGO_PREGUNTA(QUIEN_MOD);

-- 
-- JUEGO_PREGUNTA_TEMP
-- 

CREATE INDEX JUEGO_PREGUNTA_TEMP_JPR_ID_IDX ON JUEGO_PREGUNTA_TEMP(JPR_ID);
CREATE INDEX JUEGO_PREGUNTA_TMPQUIENMOD_IDX ON JUEGO_PREGUNTA_TEMP(QUIEN_MOD);
CREATE INDEX JUEGO_PREGUNTA_TEMP_QUIEN_IDX  ON JUEGO_PREGUNTA_TEMP(QUIEN);
CREATE INDEX JUEGO_PREGUNTA_TEMP_USU_ID_IDX ON JUEGO_PREGUNTA_TEMP(USU_ID);
CREATE INDEX JUEGO_PREGUNTA_TEMP_ACTIVO_IDX ON JUEGO_PREGUNTA_TEMP(ACTIVO);

-- 
-- JUEGO_RESPUESTA
-- 

CREATE INDEX JUEGO_RESPUESTA_JPR_ID_IDX     ON JUEGO_RESPUESTA(JPR_ID);
CREATE INDEX JUEGO_RESPUESTA_JRE_CORRCT_IDX ON JUEGO_RESPUESTA(JRE_CORRECTA);
CREATE INDEX JUEGO_RESPUESTA_ACTIVO_IDX     ON JUEGO_RESPUESTA(ACTIVO);
CREATE INDEX JUEGO_RESPUESTA_QUIEN_IDX      ON JUEGO_RESPUESTA(QUIEN);
CREATE INDEX JUEGO_RESPUESTA_QUIEN_MOD_IDX  ON JUEGO_RESPUESTA(QUIEN_MOD);

-- 
-- JUEGO_RESPUESTA_USUARIO
-- 

CREATE INDEX JUEGO_RESP_USUARIO_JPR_ID_IDX ON JUEGO_RESPUESTA_USUARIO(JPR_ID);
CREATE INDEX JUEGO_RESP_USUARIO_JRE_ID_IDX ON JUEGO_RESPUESTA_USUARIO(JRE_ID);
CREATE INDEX JUEGO_RESP_USUARIO_USU_ID_IDX ON JUEGO_RESPUESTA_USUARIO(USU_ID);
CREATE INDEX JUEGO_RESP_USUARIO_QUIEN_IDX  ON JUEGO_RESPUESTA_USUARIO(QUIEN);
CREATE INDEX JUEGO_RESP_USUARIO_ACTIVO_IDX ON JUEGO_RESPUESTA_USUARIO(ACTIVO);

-- 
-- LOG
-- 

CREATE INDEX LOG_QUIEN_IDX  ON LOG(QUIEN);
CREATE INDEX LOG_DOC_ID_IDX ON LOG(DOC_ID);

-- 
-- MODULO
-- 

CREATE INDEX MODULO_MOD_CODIGO_IDX ON MODULO(MOD_CODIGO);
CREATE INDEX MODULO_AREA_ID_IDX    ON MODULO(AREA_ID);
CREATE INDEX MODULO_ACTIVO_IDX     ON MODULO(ACTIVO);
CREATE INDEX MODULO_QUIEN_IDX      ON MODULO(QUIEN);
CREATE INDEX MODULO_QUIEN_MOD_IDX  ON MODULO(QUIEN_MOD);

-- 
-- MUNICIPIO
-- 

CREATE INDEX MUNICIPIO_DPT_ID_IDX    ON MUNICIPIO(DPT_ID);
CREATE INDEX MUNICIPIO_ACTIVO_IDX    ON MUNICIPIO(ACTIVO);
CREATE INDEX MUNICIPIO_QUIEN_IDX     ON MUNICIPIO(QUIEN);
CREATE INDEX MUNICIPIO_QUIEN_MOD_IDX ON MUNICIPIO(QUIEN_MOD);

-- 
-- OFS_STAGE
-- 

CREATE INDEX OFS_STAGE_OST_REF_IDX  ON OFS_STAGE(OST_REF);
CREATE INDEX OFS_STAGE_USU_ID_IDX   ON OFS_STAGE(USU_ID);
CREATE INDEX OFS_STAGE_OST_TIPO_IDX ON OFS_STAGE(OST_TIPO);

-- 
-- PAIS
-- 

CREATE INDEX PAIS_PAI_SIGLA_IDX ON PAIS(PAI_SIGLA);
CREATE INDEX PAIS_ACTIVO_IDX    ON PAIS(ACTIVO);
CREATE INDEX PAIS_QUIEN_IDX     ON PAIS(QUIEN);
CREATE INDEX PAIS_QUIEN_MOD_IDX ON PAIS(QUIEN_MOD);

-- 
-- PERFIL
-- 

CREATE INDEX PERFIL_ACTIVO_IDX    ON PERFIL(ACTIVO);
CREATE INDEX PERFIL_QUIEN_IDX     ON PERFIL(QUIEN);
CREATE INDEX PERFIL_QUIEN_MOD_IDX ON PERFIL(QUIEN_MOD);

-- 
-- PERFIL_ROL
-- 

CREATE INDEX PERFIL_ROL_ROL_ID_IDX    ON PERFIL_ROL(ROL_ID);
CREATE INDEX PERFIL_ROL_PER_ID_IDX    ON PERFIL_ROL(PER_ID);
CREATE INDEX PERFIL_ROL_QUIEN_IDX     ON PERFIL_ROL(QUIEN);
CREATE INDEX PERFIL_ROL_QUIEN_MOD_IDX ON PERFIL_ROL(QUIEN_MOD);
CREATE INDEX PERFIL_ROL_ACTIVO_IDX    ON PERFIL_ROL(ACTIVO);

-- 
-- PISO
-- 

CREATE INDEX PISO_PISO_SIGLA_IDX  ON PISO(PISO_SIGLA);
CREATE INDEX PISO_PISO_CODIGO_IDX ON PISO(PISO_CODIGO);
CREATE INDEX PISO_EDF_ID_IDX      ON PISO(EDF_ID);
CREATE INDEX PISO_ACTIVO_IDX      ON PISO(ACTIVO);
CREATE INDEX PISO_QUIEN_IDX       ON PISO(QUIEN);
CREATE INDEX PISO_QUIEN_MOD_IDX   ON PISO(QUIEN_MOD);

-- 
-- PLANTILLA
-- 

CREATE INDEX PLANTILLA_PLA_CODIGO_IDX   ON PLANTILLA(PLA_CODIGO);
CREATE INDEX PLANTILLA_ACTIVO_IDX       ON PLANTILLA(ACTIVO);
CREATE INDEX PLANTILLA_QUIEN_IDX        ON PLANTILLA(QUIEN);
CREATE INDEX PLANTILLA_QUIEN_MOD_IDX    ON PLANTILLA(QUIEN_MOD);
CREATE INDEX PLANTILLA_PLA_TIPO_IDX     ON PLANTILLA(PLA_TIPO);
CREATE INDEX PLANTILLA_PLA_DOCX_DOC_IDX ON PLANTILLA(PLA_DOCX_DOCUMENTO);
CREATE INDEX PLANTILLA_DOC_DOCX_DOC_IDX ON PLANTILLA(DOC_DOCX_DOCUMENTO);

-- 
-- PRESTAMO
-- 

CREATE INDEX PRESTAMO_PRE_CODIGO_IDX      ON PRESTAMO(PRE_CODIGO);
CREATE INDEX PRESTAMO_USU_ID_PRESTA_IDX   ON PRESTAMO(USU_ID_PRESTA);
CREATE INDEX PRESTAMO_USU_ID_SOLICITA_IDX ON PRESTAMO(USU_ID_SOLICITA);
CREATE INDEX PRESTAMO_DEP_ID_IDX          ON PRESTAMO(DEP_ID);
CREATE INDEX PRESTAMO_ACTIVO_IDX          ON PRESTAMO(ACTIVO);
CREATE INDEX PRESTAMO_QUIEN_IDX           ON PRESTAMO(QUIEN);
CREATE INDEX PRESTAMO_QUIEN_MOD_IDX       ON PRESTAMO(QUIEN_MOD);

-- 
-- PRESTAMO_SOLICITUD
-- 

CREATE INDEX PRESTAMO_SOL_USU_ID_SOLIC_IDX ON PRESTAMO_SOLICITUD(USU_ID_SOLICITA);
CREATE INDEX PRESTAMO_SOL_DEP_ID_IDX       ON PRESTAMO_SOLICITUD(DEP_ID);
CREATE INDEX PRESTAMO_SOL_ACTIVO_IDX       ON PRESTAMO_SOLICITUD(ACTIVO);
CREATE INDEX PRESTAMO_SOL_QUIEN_IDX        ON PRESTAMO_SOLICITUD(QUIEN);
CREATE INDEX PRESTAMO_SOL_QUIEN_MOD_IDX    ON PRESTAMO_SOLICITUD(QUIEN_MOD);
CREATE INDEX PRESTAMO_SOL_DOC_ID_IDX       ON PRESTAMO_SOLICITUD(DOC_ID);
CREATE INDEX PRESTAMO_SOL_EXP_ID_IDX       ON PRESTAMO_SOLICITUD(EXP_ID);

-- 
-- PROCESO
-- 

CREATE INDEX PROCESO_QUIEN_IDX            ON PROCESO(QUIEN);
CREATE INDEX PROCESO_QUIEN_MOD_IDX        ON PROCESO(QUIEN_MOD);
CREATE INDEX PROCESO_ACTIVO_IDX           ON PROCESO(ACTIVO);
CREATE INDEX PROCESO_PRO_ID_RESPUESTA_IDX ON PROCESO(PRO_ID_RESPUESTA);
CREATE INDEX PROCESO_PRO_ALIAS_IDX        ON PROCESO(PRO_ALIAS);

-- 
-- PROCESO_ESTADO
-- 

CREATE INDEX PROCESO_ESTADO_PES_NOMBRE_IDX  ON PROCESO_ESTADO(PES_NOMBRE);
CREATE INDEX PROCESO_ESTADO_QUIEN_IDX       ON PROCESO_ESTADO(QUIEN);
CREATE INDEX PROCESO_ESTADO_QUIEN_MOD_IDX   ON PROCESO_ESTADO(QUIEN_MOD);
CREATE INDEX PROCESO_ESTADO_ACTIVO_IDX      ON PROCESO_ESTADO(ACTIVO);
CREATE INDEX PROCESO_ESTADO_PRO_ID_IDX      ON PROCESO_ESTADO(PRO_ID);
CREATE INDEX PROCESO_ESTADO_PES_INICIAL_IDX ON PROCESO_ESTADO(PES_INICIAL);
CREATE INDEX PROCESO_ESTADO_PES_FINAL_IDX   ON PROCESO_ESTADO(PES_FINAL);
CREATE INDEX PROCESO_ESTADO_PES_REASIGN_IDX ON PROCESO_ESTADO(PES_REASIGNACION);
CREATE INDEX PROCESO_ESTADO_PES_TRUNCAT_IDX ON PROCESO_ESTADO(PES_TRUNCATED);

-- 
-- PROCESO_INSTANCIA
-- 

CREATE INDEX PROCESO_INSTANCIA_PRO_ID_IDX   ON PROCESO_INSTANCIA(PRO_ID);
CREATE INDEX PROCESO_INSTANCIA_QUIEN_IDX    ON PROCESO_INSTANCIA(QUIEN);
CREATE INDEX PROCESO_INSTANCIA_QUIENMOD_IDX ON PROCESO_INSTANCIA(QUIEN_MOD);

-- 
-- PROCESO_INSTANCIA_OBS
-- 

CREATE INDEX PROCESO_INST_OBS_PIO_OBS_IDX ON PROCESO_INSTANCIA_OBS(PIO_OBS);
CREATE INDEX PROCESO_INST_OBS_QUIEN_IDX   ON PROCESO_INSTANCIA_OBS(QUIEN);
CREATE INDEX PROCESO_INST_OBS_PIN_ID_IDX  ON PROCESO_INSTANCIA_OBS(PIN_ID);

-- 
-- PROCESO_INSTANCIA_VAR
-- 

CREATE INDEX PROCESO_INST_VAR_PIN_ID_IDX    ON PROCESO_INSTANCIA_VAR(PIN_ID);
CREATE INDEX PROCESO_INST_VAR_PIV_KEY_IDX   ON PROCESO_INSTANCIA_VAR(PIV_KEY);
CREATE INDEX PROCESO_INST_VAR_QUIEN_IDX     ON PROCESO_INSTANCIA_VAR(QUIEN);
CREATE INDEX PROCESO_INST_VAR_ACTIVO_IDX    ON PROCESO_INSTANCIA_VAR(ACTIVO);
CREATE INDEX PROCESO_INST_VAR_QUIEN_MOD_IDX ON PROCESO_INSTANCIA_VAR(QUIEN_MOD);

-- 
-- PROCESO_TRANSICION
-- 

CREATE INDEX PROCESO_TRNS_PTT_ID_IDX       ON PROCESO_TRANSICION(PTT_ID);
CREATE INDEX PROCESO_TRNS_PTR_NOMBRE_IDX   ON PROCESO_TRANSICION(PTR_NOMBRE);
CREATE INDEX PROCESO_TRNS_PES_ID_INICI_IDX ON PROCESO_TRANSICION(PES_ID_INICIAL);
CREATE INDEX PROCESO_TRNS_PES_ID_FINAL_IDX ON PROCESO_TRANSICION(PES_ID_FINAL);
CREATE INDEX PROCESO_TRNS_QUIEN_IDX        ON PROCESO_TRANSICION(QUIEN);
CREATE INDEX PROCESO_TRNS_QUIEN_MOD_IDX    ON PROCESO_TRANSICION(QUIEN_MOD);
CREATE INDEX PROCESO_TRNS_ACTIVO_IDX       ON PROCESO_TRANSICION(ACTIVO);

-- 
-- PROCESO_TRANSICION_COND
-- 

CREATE INDEX PROCESO_TRNS_COND_ACTIVO_IDX   ON PROCESO_TRANSICION_COND(ACTIVO);
CREATE INDEX PROCESO_TRNS_COND_QUIEN_IDX    ON PROCESO_TRANSICION_COND(QUIEN);
CREATE INDEX PROCESO_TRNS_COND_QUIENMOD_IDX ON PROCESO_TRANSICION_COND(QUIEN_MOD);
CREATE INDEX PROCESO_TRNS_COND_PTR_ID_IDX   ON PROCESO_TRANSICION_COND(PTR_ID);
CREATE INDEX PROCESO_TRNS_COND_PTC_COMP_IDX ON PROCESO_TRANSICION_COND(PTC_COMPILADO);

-- 
-- PROCESO_TRANSICION_TIPO
-- 

CREATE INDEX PROCESO_TRNS_TIPO_PTT_NOM_IDX ON PROCESO_TRANSICION_TIPO(PTT_NOMBRE);
CREATE INDEX PROCESO_TRNS_TIPO_CUANDO_IDX  ON PROCESO_TRANSICION_TIPO(CUANDO);
CREATE INDEX PROCESO_TRNS_TIPO_QUIEN_IDX   ON PROCESO_TRANSICION_TIPO(QUIEN);
CREATE INDEX PROCESO_TRNS_TIPO_CND_MOD_IDX ON PROCESO_TRANSICION_TIPO(CUANDO_MOD);
CREATE INDEX PROCESO_TRNS_TIPO_QN_MOD_IDX  ON PROCESO_TRANSICION_TIPO(QUIEN_MOD);
CREATE INDEX PROCESO_TRNS_TIPO_ACTIVO_IDX  ON PROCESO_TRANSICION_TIPO(ACTIVO);

-- 
-- PROCESO_VAR
-- 

CREATE INDEX PROCESO_VAR_PRO_ID_IDX    ON PROCESO_VAR(PRO_ID);
CREATE INDEX PROCESO_VAR_PVA_KEY_IDX   ON PROCESO_VAR(PVA_KEY);
CREATE INDEX PROCESO_VAR_QUIEN_IDX     ON PROCESO_VAR(QUIEN);
CREATE INDEX PROCESO_VAR_QUIEN_MOD_IDX ON PROCESO_VAR(QUIEN_MOD);
CREATE INDEX PROCESO_VAR_ACTIVO_IDX    ON PROCESO_VAR(ACTIVO);

-- 
-- REMITENTE
-- 

CREATE INDEX REMITENTE_QUIEN_IDX     ON REMITENTE(QUIEN);
CREATE INDEX REMITENTE_QUIEN_MOD_IDX ON REMITENTE(QUIEN_MOD);
CREATE INDEX REMITENTE_ACTIVO_IDX    ON REMITENTE(ACTIVO);
CREATE INDEX REMITENTE_REM_DOC_IDX   ON REMITENTE(REM_DOC);
CREATE INDEX REMITENTE_REM_TIPO_IDX  ON REMITENTE(REM_TIPO);

-- 
-- ROL
-- 

CREATE INDEX ROL_QUIEN_IDX     ON ROL(QUIEN);
CREATE INDEX ROL_QUIEN_MOD_IDX ON ROL(QUIEN_MOD);
CREATE INDEX ROL_ACTIVO_IDX    ON ROL(ACTIVO);

-- 
-- S_INSTANCIA_USUARIO
-- 

CREATE INDEX S_INSTANCIA_USUARIO_USU_ID_IDX ON S_INSTANCIA_USUARIO(USU_ID);
CREATE INDEX S_INSTANCIA_USUARIO_PIN_ID_IDX ON S_INSTANCIA_USUARIO(PIN_ID);

-- 
-- TIPO_ALMACENAJE
-- 

CREATE INDEX TIPO_ALMACENAJE_TAL_CODIGO_IDX ON TIPO_ALMACENAJE(TAL_CODIGO);
CREATE INDEX TIPO_ALMACENAJE_ACTIVO_IDX     ON TIPO_ALMACENAJE(ACTIVO);
CREATE INDEX TIPO_ALMACENAJE_QUIEN_IDX      ON TIPO_ALMACENAJE(QUIEN);
CREATE INDEX TIPO_ALMACENAJE_QUIEN_MOD_IDX  ON TIPO_ALMACENAJE(QUIEN_MOD);

-- 
-- TIPO_CAJA
-- 

CREATE INDEX TIPO_CAJA_TC_NUMERO_IDX  ON TIPO_CAJA(TC_NUMERO);
CREATE INDEX TIPO_CAJA_TC_CODIGO_IDX  ON TIPO_CAJA(TC_CODIGO);
CREATE INDEX TIPO_CAJA_TC_SIGLA_IDX   ON TIPO_CAJA(TC_SIGLA);
CREATE INDEX TIPO_CAJA_ACTIVO_IDX     ON TIPO_CAJA(ACTIVO);
CREATE INDEX TIPO_CAJA_QUIEN_IDX      ON TIPO_CAJA(QUIEN);
CREATE INDEX TIPO_CAJA_QUIEN_MOD_IDX  ON TIPO_CAJA(QUIEN_MOD);

-- 
-- TIPO_DESTINATARIO
-- 

CREATE INDEX TIPO_DESTINATARIO_QUIEN_IDX    ON TIPO_DESTINATARIO(QUIEN);
CREATE INDEX TIPO_DESTINATARIO_QUIENMOD_IDX ON TIPO_DESTINATARIO(QUIEN_MOD);
CREATE INDEX TIPO_DESTINATARIO_ACTIVO_IDX   ON TIPO_DESTINATARIO(ACTIVO);

-- 
-- TIPO_DOCUMENTO
-- 

CREATE INDEX TIPO_DOCUMENTO_PLA_ID_IDX     ON TIPO_DOCUMENTO(PLA_ID);
CREATE INDEX TIPO_DOCUMENTO_ACTIVO_IDX     ON TIPO_DOCUMENTO(ACTIVO);
CREATE INDEX TIPO_DOCUMENTO_QUIEN_IDX      ON TIPO_DOCUMENTO(QUIEN);
CREATE INDEX TIPO_DOCUMENTO_QUIEN_MOD_IDX  ON TIPO_DOCUMENTO(QUIEN_MOD);

-- 
-- TIPOLOGIA_DOCUMENTO
-- 

CREATE INDEX TIPOLOGIA_DOC_QUIEN_IDX       ON TIPOLOGIA_DOCUMENTO(QUIEN);
CREATE INDEX TIPOLOGIA_DOC_ACTIVO_IDX      ON TIPOLOGIA_DOCUMENTO(ACTIVO);
CREATE INDEX TIPOLOGIA_DOC_QUIEN_MOD_IDX   ON TIPOLOGIA_DOCUMENTO(QUIEN_MOD);
CREATE INDEX TIPOLOGIA_DOC_TDO_CODIGO_IDX  ON TIPOLOGIA_DOCUMENTO(TDO_CODIGO);
CREATE INDEX TIPOLOGIA_DOC_TDO_ARCHIVO_IDX ON TIPOLOGIA_DOCUMENTO(TDO_ARCHIVO);

-- 
-- TMP_ARBOL_DEPEN
-- 

CREATE INDEX TMP_ARBOL_DEPEN_DOC_ID_IDX ON TMP_ARBOL_DEPEN(DOC_ID);
CREATE INDEX TMP_ARBOL_DEPEN_VALOR_IDX  ON TMP_ARBOL_DEPEN(VALOR);

-- 
-- TRD
-- 

CREATE INDEX TRD_TRD_CODIGO_IDX           ON TRD(TRD_CODIGO);
CREATE INDEX TRD_TRD_SERIE_IDX            ON TRD(TRD_SERIE);
CREATE INDEX TRD_TRD_RET_ARCH_GENERAL_IDX ON TRD(TRD_RET_ARCHIVO_GENERAL);
CREATE INDEX TRD_TRD_RET_ARCH_CENTRAL_IDX ON TRD(TRD_RET_ARCHIVO_CENTRAL);
CREATE INDEX TRD_TRD_DIS_CT_IDX           ON TRD(TRD_DIS_CT);
CREATE INDEX TRD_TRD_DIS_D_IDX            ON TRD(TRD_DIS_D);
CREATE INDEX TRD_TRD_DIS_M_IDX            ON TRD(TRD_DIS_M);
CREATE INDEX TRD_TRD_DIS_S_IDX            ON TRD(TRD_DIS_S);
CREATE INDEX TRD_TRD_DIS_E_IDX            ON TRD(TRD_DIS_E);
CREATE INDEX TRD_QUIEN_IDX                ON TRD(QUIEN);
CREATE INDEX TRD_ACTIVO_IDX               ON TRD(ACTIVO);
CREATE INDEX TRD_TRD_PROCEDIMIENTO_IDX    ON TRD(TRD_PROCEDIMIENTO);
CREATE INDEX TRD_PLA_ID_IDX               ON TRD(PLA_ID);
CREATE INDEX TRD_QUIEN_MOD_IDX            ON TRD(QUIEN_MOD);
CREATE INDEX TRD_TDC_ID_IDX               ON TRD(TDC_ID);
CREATE INDEX TRD_TRD_PLAZO_IDX            ON TRD(TRD_PLAZO);

-- 
-- UNIDAD_EXTERNA
-- 

CREATE INDEX UNIDAD_EXT_UEX_DOCUMENTO_IDX ON UNIDAD_EXTERNA(UEX_DOCUMENTO);
CREATE INDEX UNIDAD_EXT_UEX_SIGLA_IDX     ON UNIDAD_EXTERNA(UEX_SIGLA);
CREATE INDEX UNIDAD_EXT_ACTIVO_IDX        ON UNIDAD_EXTERNA(ACTIVO);
CREATE INDEX UNIDAD_EXT_QUIEN_IDX         ON UNIDAD_EXTERNA(QUIEN);
CREATE INDEX UNIDAD_EXT_QUIEN_MOD_IDX     ON UNIDAD_EXTERNA(QUIEN_MOD);

-- 
-- USUARIO
-- 

CREATE INDEX USUARIO_QUIEN_IDX             ON USUARIO(QUIEN);
CREATE INDEX USUARIO_QUIEN_MOD_IDX         ON USUARIO(QUIEN_MOD);
CREATE INDEX USUARIO_ACTIVO_IDX            ON USUARIO(ACTIVO);
CREATE INDEX USUARIO_USU_DOCUMENTO_IDX     ON USUARIO(USU_DOCUMENTO);
CREATE INDEX USUARIO_USU_GRADO_IDX         ON USUARIO(USU_GRADO);
CREATE INDEX USUARIO_PER_ID_IDX            ON USUARIO(PER_ID);
CREATE INDEX USUARIO_UMA_ID_IDX            ON USUARIO(UMA_ID);
CREATE INDEX USUARIO_CLA_ID_IDX            ON USUARIO(CLA_ID);
CREATE INDEX USUARIO_USU_IMAGEN_FIRMA_IDX  ON USUARIO(USU_IMAGEN_FIRMA);
CREATE INDEX USUARIO_USU_CARGO_IDX         ON USUARIO(USU_CARGO);
CREATE INDEX USUARIO_USU_IMG_FIRMA_EXT_IDX ON USUARIO(USU_IMAGEN_FIRMA_EXT);

-- 
-- USUARIO_HISTORIAL_FIRMA_IMG
-- 

CREATE INDEX USUARIO_HST_FRMIMG_QN_MOD_IDX ON USUARIO_HISTORIAL_FIRMA_IMG(QUIEN_MOD);
CREATE INDEX USUARIO_HST_FRMIMG_USU_ID_IDX ON USUARIO_HISTORIAL_FIRMA_IMG(USU_ID);
CREATE INDEX USUARIO_HST_FRMIMG_CUANDO_IDX ON USUARIO_HISTORIAL_FIRMA_IMG(CUANDO);


-- -------------------------------------------------------------------------------------------------------------------------------------------------------------
-- -------------------------------------------------------------------------------------------------------------------------------------------------------------
-- -------------------------------------------------------------------------------------------------------------------------------------------------------------

-- 2017-02-04 jgarcia@controltechcg.com

CREATE INDEX DOCUMENTO_PIN_ID_IDX     ON DOCUMENTO(PIN_ID);
CREATE INDEX DOCUMENTO_CUANDO_IDX     ON DOCUMENTO(CUANDO);

CREATE INDEX INS_BANDEJA_PIN_ID_IDX   ON INSTANCIA_BANDEJA(PIN_ID);
CREATE INDEX INS_BANDEJA_USU_ID_IDX   ON INSTANCIA_BANDEJA(USU_ID);
CREATE INDEX INS_BANDEJA_ACTIVO_IDX   ON INSTANCIA_BANDEJA(ACTIVO);
CREATE INDEX INS_BANDEJA_BANDEJA_IDX  ON INSTANCIA_BANDEJA(BANDEJA);

CREATE INDEX PRO_INST_PES_ID_IDX      ON PROCESO_INSTANCIA(PES_ID);
CREATE INDEX PRO_INST_USU_IDASIG_IDX  ON PROCESO_INSTANCIA(USU_ID_ASIGNADO);

CREATE INDEX USUARIO_USU_LOGIN        ON USUARIO(USU_LOGIN);

-- 2017-02-04 jgarcia@controltechcg.com

CREATE INDEX DEPENDENCIA_USU_ID_JEFE_IDX ON DEPENDENCIA(USU_ID_JEFE);
CREATE INDEX USUARIO_DEP_ID_IDX          ON USUARIO(DEP_ID);
CREATE INDEX DOC_DEP_DESTINO_DOC_ID_IDX  ON DOCUMENTO_DEP_DESTINO(DOC_ID);
