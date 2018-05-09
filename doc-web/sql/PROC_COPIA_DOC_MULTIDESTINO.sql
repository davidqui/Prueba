create or replace PROCEDURE "PROC_COPIA_DOC_MULTIDESTINO"
(
  P_DOC_ID_ORIGEN               IN  DOCUMENTO.DOC_ID%TYPE,
  P_PIN_ID_NUEVO                IN  DOCUMENTO.PIN_ID%TYPE,
  P_DOC_ID_NUEVO                IN  DOCUMENTO.DOC_ID%TYPE,
  P_DEP_ID_DES                  IN  DOCUMENTO.DEP_ID_DES%TYPE,
  P_DOC_CONTENT_FILE            IN  DOCUMENTO.DOC_CONTENT_FILE%TYPE,
  P_DOC_DOCX_DOCUMENTO          IN  DOCUMENTO.DOC_DOCX_DOCUMENTO%TYPE,
  P_ARRAY_UUID_DOC_ADJUNTO      IN  T_ARRAY_UUID
)
is
    V_PIN_ID_ORIGINAL   DOCUMENTO.PIN_ID%TYPE;
    V_CANTIDAD          NUMBER;
    V_PIV_ID_NUEVO      PROCESO_INSTANCIA_VAR.PIV_ID%TYPE;
    V_PIV_KEY_ATR       PROCESO_INSTANCIA_VAR.PIV_KEY%TYPE := 'doc.id';
    V_SIGLA_DEP_DESTINO DEPENDENCIA.DEP_SIGLA%TYPE;

    CURSOR C_PIVAR  IS
    SELECT PIV_ID, PIN_ID, PIV_KEY, COUNT(1) CANTIDAD FROM H_PROCESO_INSTANCIA_VAR WHERE PIN_ID = V_PIN_ID_ORIGINAL GROUP BY PIV_ID, PIN_ID, PIV_KEY ORDER BY PIV_ID DESC;
    
    CURSOR C_PIVAR_INSERT (PIV_ID_INSERT NUMBER) IS
    SELECT * FROM H_PROCESO_INSTANCIA_VAR WHERE PIV_ID = PIV_ID_INSERT ORDER BY HPIV_ID ASC;
    
    CURSOR C_DOC_ADJUNTO IS
    SELECT * FROM DOCUMENTO_ADJUNTO WHERE DOC_ID = P_DOC_ID_ORIGEN AND ACTIVO = 1 ORDER BY CUANDO DESC;
BEGIN

    BEGIN
        SELECT PIN_ID INTO V_PIN_ID_ORIGINAL FROM DOCUMENTO WHERE DOC_ID = P_DOC_ID_ORIGEN;
    EXCEPTION WHEN NO_DATA_FOUND THEN
        RAISE_APPLICATION_ERROR( -20001, 'No se encontro el documento origen' );
    END;

    IF P_PIN_ID_NUEVO IS NULL THEN
        RAISE_APPLICATION_ERROR( -20001, 'El identificador de la tabla PROCESO_INSTANCIA esta vacío.' );
    END IF;
    
    SELECT COUNT(1) INTO V_CANTIDAD FROM PROCESO_INSTANCIA WHERE PIN_ID = P_PIN_ID_NUEVO;
    IF V_CANTIDAD > 0 THEN
        RAISE_APPLICATION_ERROR( -20001, 'El identificador nuevo de la tabla PROCESO_INSTANCIA ya se encuentró en el sistema.' );
    END IF;
    
    SELECT COUNT(1) INTO V_CANTIDAD FROM DOCUMENTO WHERE DOC_ID = P_DOC_ID_NUEVO;
    IF V_CANTIDAD > 0 THEN
        RAISE_APPLICATION_ERROR( -20001, 'El identificador nuevo de la tabla DOCUMENTO ya se encuentró en el sistema.' );
    END IF;
    
    SELECT COUNT(1) INTO V_CANTIDAD FROM DOCUMENTO WHERE DOC_CONTENT_FILE = P_DOC_CONTENT_FILE;
    IF V_CANTIDAD > 0 THEN
        RAISE_APPLICATION_ERROR( -20001, 'El identificador de la columna DOC_CONTENT_FILE en la tabla DOCUMENTO ya se encuentró en el sistema.' );
    END IF;
    
    SELECT COUNT(1) INTO V_CANTIDAD FROM DOCUMENTO WHERE DOC_DOCX_DOCUMENTO = P_DOC_DOCX_DOCUMENTO;
    IF V_CANTIDAD > 0 THEN
        RAISE_APPLICATION_ERROR( -20001, 'El identificador de la columna DOC_DOCX_DOCUMENTO en la tabla DOCUMENTO ya se encuentró en el sistema.' );
    END IF;
    
    BEGIN
        SELECT DEP_SIGLA INTO V_SIGLA_DEP_DESTINO FROM DEPENDENCIA WHERE DEP_ID = P_DEP_ID_DES;
    EXCEPTION WHEN NO_DATA_FOUND THEN
        RAISE_APPLICATION_ERROR( -20001, 'No se encontro la dependencia destino' );
    END;
    
    INSERT INTO PROCESO_INSTANCIA (SELECT P_PIN_ID_NUEVO,PRO_ID,CUANDO,QUIEN,PES_ID,CUANDO_MOD,QUIEN_MOD,USU_ID_ASIGNADO FROM PROCESO_INSTANCIA WHERE PIN_ID = V_PIN_ID_ORIGINAL);
    
    UPDATE PROCESO_INSTANCIA SET PES_ID = (SELECT PES_ID FROM PROCESO_INSTANCIA WHERE PIN_ID = V_PIN_ID_ORIGINAL) WHERE PIN_ID = P_PIN_ID_NUEVO;
    
    DELETE FROM H_PROCESO_INSTANCIA WHERE PIN_ID = P_PIN_ID_NUEVO;
    
    INSERT INTO H_PROCESO_INSTANCIA (PIN_ID, PRO_ID, CUANDO, QUIEN, PES_ID, CUANDO_MOD, QUIEN_MOD, USU_ID_ASIGNADO)
    (SELECT P_PIN_ID_NUEVO, PRO_ID, CUANDO, QUIEN, PES_ID, CUANDO_MOD, QUIEN_MOD, USU_ID_ASIGNADO FROM H_PROCESO_INSTANCIA a WHERE a.PIN_ID= V_PIN_ID_ORIGINAL);
    
    DELETE FROM PROCESO_INSTANCIA_VAR WHERE PIN_ID = P_PIN_ID_NUEVO;
    DELETE FROM H_PROCESO_INSTANCIA_VAR WHERE PIN_ID = P_PIN_ID_NUEVO;
    
    FOR AUX_C_PIVAR IN C_PIVAR LOOP
        V_CANTIDAD := 1;
        FOR AUX_C_PIVAR_INSERT IN C_PIVAR_INSERT(AUX_C_PIVAR.PIV_ID) LOOP
            IF V_CANTIDAD = 1 THEN
                INSERT INTO PROCESO_INSTANCIA_VAR (PIN_ID, PIV_KEY, PIV_VALUE, CUANDO, QUIEN, ACTIVO, CUANDO_MOD, QUIEN_MOD) VALUES
                (P_PIN_ID_NUEVO, AUX_C_PIVAR_INSERT.PIV_KEY, DECODE(AUX_C_PIVAR_INSERT.PIV_KEY, V_PIV_KEY_ATR,P_DOC_ID_NUEVO,AUX_C_PIVAR_INSERT.PIV_VALUE), AUX_C_PIVAR_INSERT.CUANDO, AUX_C_PIVAR_INSERT.QUIEN, AUX_C_PIVAR_INSERT.ACTIVO, AUX_C_PIVAR_INSERT.CUANDO_MOD, AUX_C_PIVAR_INSERT.QUIEN_MOD) RETURNING PIV_ID INTO V_PIV_ID_NUEVO ;
            ELSE
                UPDATE PROCESO_INSTANCIA_VAR
                SET PIV_VALUE   = DECODE(AUX_C_PIVAR_INSERT.PIV_KEY, V_PIV_KEY_ATR,P_DOC_ID_NUEVO,AUX_C_PIVAR_INSERT.PIV_VALUE),
                    CUANDO      = AUX_C_PIVAR_INSERT.CUANDO,
                    QUIEN       = AUX_C_PIVAR_INSERT.QUIEN,
                    ACTIVO      = AUX_C_PIVAR_INSERT.ACTIVO,
                    CUANDO_MOD  = AUX_C_PIVAR_INSERT.CUANDO_MOD,
                    QUIEN_MOD   = AUX_C_PIVAR_INSERT.QUIEN_MOD
                WHERE PIV_ID = V_PIV_ID_NUEVO;
            END IF;
            V_CANTIDAD := V_CANTIDAD + 1;
        END LOOP;
    END LOOP;
    
    DELETE FROM S_INSTANCIA_USUARIO WHERE PIN_ID = P_PIN_ID_NUEVO;
    INSERT INTO S_INSTANCIA_USUARIO (USU_ID, PIN_ID)
    (SELECT USU_ID, P_PIN_ID_NUEVO FROM S_INSTANCIA_USUARIO WHERE PIN_ID = V_PIN_ID_ORIGINAL);
    
    INSERT INTO DOCUMENTO (DOC_ID, DOC_ASUNTO, TRD_ID, QUIEN, CUANDO, QUIEN_MOD, CUANDO_MOD, CLA_ID, DOC_NUM_OFICIO, DOC_PLAZO, DOC_FCH_OFICIO, DOC_NUM_FOLIOS, PIN_ID, DOC_STICKER, DOC_CONTENIDO, USU_ID_FIRMA, USU_ID_ELABORA, USU_ID_VISTO_BUENO, USU_ID_APRUEBA, DOC_RADICADO, DOC_RELACIONADO, EXP_ID, DOC_PDF, DOC_DESCRIPCION, DOC_PAGINAS, DEP_ID_DES, DOC_DEST_NOMBRE, DOC_DEST_TITULO, DOC_DEST_DIRECCION, PLA_ID, DOC_CONTENT_FILE, DOC_PRESTADO, DOC_REM_NOMBRE, DOC_REM_TITULO, DOC_REM_DIRECCION, DEP_ID_REM, PRO_RAD_ORFEO, PRO_NUM_BOLSA, ESTADO_TMP, PDF_TEXTO26, FECHA_ULTI_TRG_EJECUTADO, DOC_DOCX_DOCUMENTO, USU_ID_ULTIMA_ACCION, CODIGO_VALIDA_SCANNER, ID_USUARIO_VALIDA_COD_SCANNER, FECHA_GEN_CODIGO_SCANNER, ESTADO_CODIGO_VALIDA_SCANNER, GRADO_EXTERNO, MARCA_AGUA_EXTERNO, RESTRICCION_DIFUSION, CARGO_ID_ELABORA, CARGO_ID_FIRMA)
    (SELECT P_DOC_ID_NUEVO, DOC_ASUNTO, TRD_ID, QUIEN, CUANDO,QUIEN_MOD,CUANDO_MOD, CLA_ID,DOC_NUM_OFICIO, DOC_PLAZO, DOC_FCH_OFICIO, DOC_NUM_FOLIOS, P_PIN_ID_NUEVO, DOC_STICKER, DOC_CONTENIDO, USU_ID_FIRMA, USU_ID_ELABORA, USU_ID_VISTO_BUENO, USU_ID_APRUEBA, DOC_RADICADO, DOC_RELACIONADO, EXP_ID, DOC_PDF, DOC_DESCRIPCION, DOC_PAGINAS, P_DEP_ID_DES, DOC_DEST_NOMBRE, DOC_DEST_TITULO, DOC_DEST_DIRECCION, PLA_ID, P_DOC_CONTENT_FILE, DOC_PRESTADO, DOC_REM_NOMBRE, DOC_REM_TITULO, DOC_REM_DIRECCION, DEP_ID_REM, PRO_RAD_ORFEO, PRO_NUM_BOLSA, ESTADO_TMP, PDF_TEXTO26, FECHA_ULTI_TRG_EJECUTADO, P_DOC_DOCX_DOCUMENTO, USU_ID_ULTIMA_ACCION, CODIGO_VALIDA_SCANNER, ID_USUARIO_VALIDA_COD_SCANNER, FECHA_GEN_CODIGO_SCANNER, ESTADO_CODIGO_VALIDA_SCANNER, GRADO_EXTERNO, MARCA_AGUA_EXTERNO, RESTRICCION_DIFUSION, CARGO_ID_ELABORA, CARGO_ID_FIRMA FROM DOCUMENTO WHERE DOC_ID = P_DOC_ID_ORIGEN);
    
    SELECT COUNT(1) INTO V_CANTIDAD FROM DOCUMENTO_ADJUNTO WHERE DOC_ID = P_DOC_ID_ORIGEN AND ACTIVO = 1 ORDER BY CUANDO ASC;
    
    IF V_CANTIDAD != P_ARRAY_UUID_DOC_ADJUNTO.COUNT THEN
        RAISE_APPLICATION_ERROR( -20001, 'La cantidad de registros de la tabla de documentos adjuntos no coincide con los UUID enviados.' );
    END IF;
    
    V_CANTIDAD := 1;
    FOR AUX_C_DOC_ADJUNTO IN C_DOC_ADJUNTO LOOP
        INSERT INTO DOCUMENTO_ADJUNTO (DAD_ID, TDO_ID, CUANDO, QUIEN, DOC_ID, DAD_CONTENT, DAD_ORIGINAL, DAD_DESCRIPCION, ACTIVO)
        VALUES (P_ARRAY_UUID_DOC_ADJUNTO(V_CANTIDAD),AUX_C_DOC_ADJUNTO.TDO_ID, AUX_C_DOC_ADJUNTO.CUANDO, AUX_C_DOC_ADJUNTO.QUIEN, P_DOC_ID_NUEVO, AUX_C_DOC_ADJUNTO.DAD_CONTENT, AUX_C_DOC_ADJUNTO.DAD_ORIGINAL, AUX_C_DOC_ADJUNTO.DAD_DESCRIPCION, AUX_C_DOC_ADJUNTO.ACTIVO);
        V_CANTIDAD := V_CANTIDAD + 1;
    END LOOP;
    
    INSERT INTO DOCUMENTO_OBS(DOB_OBS, CUANDO,QUIEN,DOC_ID)
    (SELECT DOB_OBS, CUANDO, QUIEN, P_DOC_ID_NUEVO FROM DOCUMENTO_OBS WHERE DOC_ID = P_DOC_ID_ORIGEN);
        
    INSERT INTO DOCUMENTO_PDF (DOCPDF_ID,PASO,PDF_TEXTO1,PDF_TEXTO2,PDF_TEXTO3,PDF_TEXTO4,PDF_TEXTO5,PDF_TEXTO6,PDF_TEXTO6_2,PDF_TEXTO7,PDF_TEXTO8,PDF_TEXTO9,PDF_TEXTO10,PDF_TEXTO12,PDF_TEXTO13,PDF_TEXTO14,PDF_TEXTO15,PDF_TEXTO16,PDF_TEXTO17,PDF_TEXTO18,PDF_TEXTO19,PDF_TEXTO20,PDF_TEXTO21,PDF_TEXTO22,PDF_TEXTO23,PDF_TEXTO24,PDF_TEXTO25,PDF_TEXTO11,PDF_TEXTO26,PDF_TEXTO27,PDF_TEXTO28,PDF_TEXTO29,PDF_TEXTO30,PDF_TEXTO31,PDF_TEXTO32,PDF_TEXTO33,PDF_TEXTO34,PDF_TEXTO35,PDF_TEXTO36,PDF_TEXTO37,PDF_TEXTO38,PDF_TEXTO39,PDF_TEXTO40,PDF_TEXTO41,PDF_TEXTO42,PDF_TEXTO43,PDF_TEXTO44,PDF_TEXTO45,PDF_TEXTO46,PDF_TEXTO47,PDF_TEXTO48,PDF_TEXTO49,PDF_TEXTO50,PDF_TEXTO51,PDF_TEXTO52,PDF_TEXTO53,PDF_TEXTO54,PDF_TEXTO55,PDF_TEXTO56,PDF_TEXTO58,PDF_TEXTO59,PDF_TEXTO60,PDF_TEXTO61,PDF_TEXTO62,PDF_TEXTO63,PDF_TEXTO64,PDF_TEXTO65,PDF_TEXTO67,PDF_TEXTO68,PDF_TEXTO69,PDF_TEXTO70,PDF_TEXTO57,PDF_TEXTO66,PDF_TEXTO71,PDF_TEXTO72,PDF_TEXTO73,PDF_TEXTO74,PDF_TEXTO75,PDF_TEXTO76,PDF_TEXTO77,PDF_TEXTO78,PDF_TEXTO79,PDF_TEXTO80,PDF_TEXTO81,PDF_TEXTO82,PDF_TEXTO83,PDF_TEXTO84,PDF_TEXTO85,PDF_TEXTO86)
    (SELECT P_DOC_ID_NUEVO,PASO,PDF_TEXTO1,PDF_TEXTO2,PDF_TEXTO3,PDF_TEXTO4,PDF_TEXTO5,PDF_TEXTO6,V_SIGLA_DEP_DESTINO,PDF_TEXTO7,PDF_TEXTO8,PDF_TEXTO9,PDF_TEXTO10,PDF_TEXTO12,PDF_TEXTO13,PDF_TEXTO14,PDF_TEXTO15,PDF_TEXTO16,PDF_TEXTO17,PDF_TEXTO18,PDF_TEXTO19,PDF_TEXTO20,PDF_TEXTO21,PDF_TEXTO22,PDF_TEXTO23,PDF_TEXTO24,PDF_TEXTO25,PDF_TEXTO11,PDF_TEXTO26,PDF_TEXTO27,PDF_TEXTO28,PDF_TEXTO29,PDF_TEXTO30,PDF_TEXTO31,PDF_TEXTO32,PDF_TEXTO33,PDF_TEXTO34,PDF_TEXTO35,PDF_TEXTO36,PDF_TEXTO37,PDF_TEXTO38,PDF_TEXTO39,PDF_TEXTO40,PDF_TEXTO41,PDF_TEXTO42,PDF_TEXTO43,PDF_TEXTO44,PDF_TEXTO45,PDF_TEXTO46,PDF_TEXTO47,PDF_TEXTO48,PDF_TEXTO49,PDF_TEXTO50,PDF_TEXTO51,PDF_TEXTO52,PDF_TEXTO53,PDF_TEXTO54,PDF_TEXTO55,PDF_TEXTO56,PDF_TEXTO58,PDF_TEXTO59,PDF_TEXTO60,PDF_TEXTO61,PDF_TEXTO62,PDF_TEXTO63,PDF_TEXTO64,PDF_TEXTO65,PDF_TEXTO67,PDF_TEXTO68,PDF_TEXTO69,PDF_TEXTO70,PDF_TEXTO57,PDF_TEXTO66,PDF_TEXTO71,PDF_TEXTO72,PDF_TEXTO73,PDF_TEXTO74,PDF_TEXTO75,PDF_TEXTO76,PDF_TEXTO77,PDF_TEXTO78,PDF_TEXTO79,PDF_TEXTO80,PDF_TEXTO81,PDF_TEXTO82,PDF_TEXTO83,PDF_TEXTO84,PDF_TEXTO85,PDF_TEXTO86 FROM DOCUMENTO_PDF WHERE DOCPDF_ID = P_DOC_ID_ORIGEN);
    
    INSERT INTO DOCUMENTO_USU_ELABORA(DOC_ID, USU_ID_ELABORA, CUANDO, CARGO_ID_ELABORA)
    (SELECT P_DOC_ID_NUEVO, USU_ID_ELABORA, CUANDO, CARGO_ID_ELABORA FROM DOCUMENTO_USU_ELABORA WHERE DOC_ID = P_DOC_ID_ORIGEN);
    
    INSERT INTO DOCUMENTO_USU_APRUEBA(DOC_ID, USU_ID_APRUEBA, CUANDO)
    (SELECT P_DOC_ID_NUEVO, USU_ID_APRUEBA, CUANDO FROM DOCUMENTO_USU_APRUEBA WHERE DOC_ID = P_DOC_ID_ORIGEN);
    
    INSERT INTO DOCUMENTO_USU_VISTOS_BUENOS(DOC_ID, USU_ID_VISTO_BUENO, CUANDO)
    (SELECT P_DOC_ID_NUEVO, USU_ID_VISTO_BUENO, CUANDO FROM DOCUMENTO_USU_VISTOS_BUENOS WHERE DOC_ID = P_DOC_ID_ORIGEN);
    
END "PROC_COPIA_DOC_MULTIDESTINO";