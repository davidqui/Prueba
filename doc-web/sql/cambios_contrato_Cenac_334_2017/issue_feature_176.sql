--
-- Author:  samuel.delgado@controltechcg.com
-- Created: 18/06/2018 Issue #176 (SICDI-Controltech) feature-176
--

-- -----------------------------------------------------------------------------
-- TABLA: PLANTILLA
-- -----------------------------------------------------------------------------

ALTER TABLE
   PLANTILLA
ADD
   (
      BOOK_NAME     VARCHAR2(64 CHAR) DEFAULT '' NULL,
      BOOK_VALUE    VARCHAR2(64 CHAR) DEFAULT '' NULL
   );

COMMENT ON COLUMN PLANTILLA.BOOK_NAME
IS 'Bookmark nombre identificador del documento.';

COMMENT ON COLUMN PLANTILLA.BOOK_VALUE
IS 'Bookmark valor para validar el documento.';

-- -----------------------------------------------------------------------------
-- TABLA: PLANTILLA
-- -----------------------------------------------------------------------------

 CREATE TABLE H_PLANTILLA 
   (	
        HPLA_ID                 NUMBER(*,0)                         NOT NULL ENABLE, 
	PLA_CODIGO              VARCHAR2(32 CHAR), 
	ACTIVO                  NUMBER(1,0)             DEFAULT 1   NOT NULL ENABLE, 
	QUIEN                   NUMBER(*,0)                         NOT NULL ENABLE, 
	CUANDO                  DATE                                NOT NULL ENABLE, 
	QUIEN_MOD               NUMBER(*,0), 
	CUANDO_MOD              DATE, 
	PLA_CONTENIDO_CABEZA    CLOB, 
	PLA_CONTENIDO_PIE       CLOB, 
	PLA_TIPO                VARCHAR2(10 BYTE), 
	PLA_NOMBRE              VARCHAR2(255 CHAR), 
	PLA_ARCHIVO             VARCHAR2(32 BYTE), 
	TEXTO_DEFAULT           CLOB, 
	PLA_DOCX_DOCUMENTO      VARCHAR2(100 BYTE), 
	DOC_DOCX_DOCUMENTO      VARCHAR2(100 BYTE),
        BOOK_NAME               VARCHAR2(64 CHAR)       DEFAULT '',
        BOOK_VALUE              VARCHAR2(64 CHAR)       DEFAULT '',
	PRIMARY KEY (HPLA_ID)
    );

CREATE SEQUENCE H_PLANTILLA_SEQ;

CREATE INDEX H_PLANTILLA_ACTIVO_IDX 
ON H_PLANTILLA (ACTIVO);

-- -----------------------------------------------------------------------------
-- TRIGGER: TRG_REGISTRO_H_PLANTILLA
-- -----------------------------------------------------------------------------

CREATE OR REPLACE TRIGGER TRG_REGISTRO_H_PLANTILLA
AFTER INSERT OR UPDATE ON PLANTILLA 
FOR EACH ROW
BEGIN
    INSERT INTO H_PLANTILLA
    (
        HPLA_ID,      
        PLA_CODIGO,
        ACTIVO,     
        QUIEN,    
        CUANDO,    
        QUIEN_MOD,     
        CUANDO_MOD,    
        PLA_CONTENIDO_CABEZA, 
        PLA_CONTENIDO_PIE,
        PLA_TIPO,
        PLA_NOMBRE, 
	PLA_ARCHIVO, 
	TEXTO_DEFAULT, 
	PLA_DOCX_DOCUMENTO, 
	DOC_DOCX_DOCUMENTO,
        BOOK_NAME,
        BOOK_VALUE
    ) VALUES (
        H_PLANTILLA_SEQ.NEXTVAL,      
        :NEW.PLA_CODIGO,
        :NEW.ACTIVO,     
        :NEW.QUIEN,    
        :NEW.CUANDO,    
        :NEW.QUIEN_MOD,     
        :NEW.CUANDO_MOD,    
        :NEW.PLA_CONTENIDO_CABEZA, 
        :NEW.PLA_CONTENIDO_PIE,
        :NEW.PLA_TIPO,
        :NEW.PLA_NOMBRE, 
	:NEW.PLA_ARCHIVO, 
	:NEW.TEXTO_DEFAULT, 
	:NEW.PLA_DOCX_DOCUMENTO, 
	:NEW.DOC_DOCX_DOCUMENTO,
        :NEW.BOOK_NAME,
        :NEW.BOOK_VALUE
    );
END;

