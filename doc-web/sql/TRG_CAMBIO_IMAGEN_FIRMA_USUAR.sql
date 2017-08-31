-- 
-- 2017-04-28 jgarcia@controltechcg.com Issue #65 (SICDI-Controltechcg).
-- Corrección en el Trigger para que genere los registros históricos de las firmas
-- en tiempo de inserción y de actualización.
--

create or replace TRIGGER "TRG_CAMBIO_IMAGEN_FIRMA_USUAR" 
AFTER INSERT OR UPDATE OF USU_IMAGEN_FIRMA,USU_IMAGEN_FIRMA_EXT ON USUARIO
FOR EACH ROW
BEGIN
  <<COLUMN_SEQUENCES>>
  BEGIN
    IF INSERTING AND :NEW.USU_IMAGEN_FIRMA IS NOT NULL THEN
    INSERT
            INTO USUARIO_HISTORIAL_FIRMA_IMG
        (
          USU_ID,
          QUIEN_MOD,
          USU_IMAGEN_FIRMA,
          USU_IMAGEN_FIRMA_EXT,
          CUANDO
        )
        VALUES (
        :NEW.USU_ID,
        :NEW.QUIEN_MOD,
        :NEW.USU_IMAGEN_FIRMA,
        :NEW.USU_IMAGEN_FIRMA_EXT,
        SYSDATE);
    END IF;
    IF UPDATING AND (:OLD.USU_IMAGEN_FIRMA IS NULL AND :NEW.USU_IMAGEN_FIRMA IS NOT NULL) OR (:OLD.USU_IMAGEN_FIRMA != :NEW.USU_IMAGEN_FIRMA) THEN
      INSERT
            INTO USUARIO_HISTORIAL_FIRMA_IMG
            (
              USU_ID,
              QUIEN_MOD,
              USU_IMAGEN_FIRMA,
              USU_IMAGEN_FIRMA_EXT,
              CUANDO
            )
            VALUES (
            :NEW.USU_ID,
            :NEW.QUIEN_MOD,
            :NEW.USU_IMAGEN_FIRMA,
            :NEW.USU_IMAGEN_FIRMA_EXT,
            SYSDATE);
      END IF;
    END COLUMN_SEQUENCES;
END;


-- 
-- VERSION ORIGINAL
--
--create or replace TRIGGER "TRG_CAMBIO_IMAGEN_FIRMA_USUAR" 
--AFTER UPDATE OF USU_IMAGEN_FIRMA,USU_IMAGEN_FIRMA_EXT ON USUARIO
--FOR EACH ROW
--BEGIN
--  <<COLUMN_SEQUENCES>>
--  BEGIN
--    IF UPDATING AND :OLD.USU_IMAGEN_FIRMA != :NEW.USU_IMAGEN_FIRMA AND :OLD.USU_ID IS NOT NULL THEN
--      INSERT
--            INTO USUARIO_HISTORIAL_FIRMA_IMG
--            (
--              USU_ID,
--              QUIEN_MOD,
--              USU_IMAGEN_FIRMA,
--              USU_IMAGEN_FIRMA_EXT,
--              CUANDO
--            )
--            VALUES (
--            :NEW.USU_ID,
--            :NEW.QUIEN_MOD,
--            :NEW.USU_IMAGEN_FIRMA,
--            :NEW.USU_IMAGEN_FIRMA_EXT,
--            SYSDATE);
--      END IF;
--    END COLUMN_SEQUENCES;
--END;