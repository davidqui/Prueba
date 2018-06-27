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

--
-- PARTE II
-- Author:  samuel.delgado@controltechcg.com
-- Created: 23/05/2018 Issue #? (SICDI-Controltech) feature-?
--

-- -----------------------------------------------------------------------------
-- TABLA: WILDCARD_PLANTILLAS_VALIDADAS
-- -----------------------------------------------------------------------------

CREATE TABLE WILDCARD_PLANTILLAS_VALIDADAS(
    WPV_ID              NUMBER(38)          NOT NULL,
    TEXTO               VARCHAR2(64 CHAR)   NOT NULL,
    QUIEN               NUMBER(38)          NOT NULL,
    CUANDO              DATE                NOT NULL,
    QUIEN_MOD           NUMBER(38)          NOT NULL,
    CUANDO_MOD          DATE                NOT NULL,
    PRIMARY KEY (WPV_ID)
);

COMMENT ON TABLE WILDCARD_PLANTILLAS_VALIDADAS
IS 'Wildcards para realizar la validación de las plantillas.';

COMMENT ON COLUMN WILDCARD_PLANTILLAS_VALIDADAS.WPV_ID
IS 'ID autoincrementable.';

COMMENT ON COLUMN WILDCARD_PLANTILLAS_VALIDADAS.TEXTO
IS 'Texto referente a la llave dentro del documento .docx.';

COMMENT ON COLUMN WILDCARD_PLANTILLAS_VALIDADAS.QUIEN
IS 'ID del usuario creador de el wildcard para plantillas validadas.';
COMMENT ON COLUMN WILDCARD_PLANTILLAS_VALIDADAS.CUANDO
IS 'Fecha y hora de creación de el wildcard para plantillas validadas.';
COMMENT ON COLUMN WILDCARD_PLANTILLAS_VALIDADAS.QUIEN_MOD
IS 'ID del último usuario que modificó el wildcard para plantillas validadas.';
COMMENT ON COLUMN WILDCARD_PLANTILLAS_VALIDADAS.CUANDO_MOD
IS 'Fecha y hora de la última modificación de el wildcard para plantillas validadas.';

ALTER TABLE WILDCARD_PLANTILLAS_VALIDADAS
ADD CONSTRAINT WILDCARD_PV_QUIEN_FK
FOREIGN KEY (QUIEN)
REFERENCES USUARIO (USU_ID);
 
ALTER TABLE WILDCARD_PLANTILLAS_VALIDADAS
ADD CONSTRAINT WILDCARD_PV_QUIEN_MOD_FK
FOREIGN KEY (QUIEN_MOD)
REFERENCES USUARIO (USU_ID);

-- -----------------------------------------------------------------------------
-- TABLA: WILDCARD_PV_PLANTILLAS
-- -----------------------------------------------------------------------------

CREATE TABLE WILDCARD_PV_PLANTILLAS(
    WPV_ID              NUMBER(38)          NOT NULL,
    PLA_ID              NUMBER(38)          NOT NULL,
    PRIMARY KEY(WPV_ID, PLA_ID)
);

ALTER TABLE WILDCARD_PV_PLANTILLAS
ADD CONSTRAINT WPV_WPVP_FK
FOREIGN KEY (WPV_ID)
REFERENCES WILDCARD_PLANTILLAS_VALIDADAS (WPV_ID);

ALTER TABLE WILDCARD_PV_PLANTILLAS
ADD CONSTRAINT WPV_PLANTILLA_FK
FOREIGN KEY (PLA_ID)
REFERENCES PLANTILLA (PLA_ID);



