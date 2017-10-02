-- -----------------------------------------------------------------------------
-- 2017-09-28 edison.gonzalez@controltechcg.com Feature #129 (SICDI-Controntech)
-- feature-129
-- Número de radicado.
-- -----------------------------------------------------------------------------

-- -----------------------------------------------------------------------------
-- 2017-09-28 edison.gonzalez@controltechcg.com Feature #129 (SICDI-Controntech)
-- feature-129
-- Creación de Entidad y alteracion de tabla correspondientes a 
-- a la creacion de los campos marca de agua, grado y restriccion de la difusion.
-- -----------------------------------------------------------------------------

-- -----------------------------------------------------------------------------
-- TABLA: RESTRICCION_DIFUSION
-- -----------------------------------------------------------------------------
create table RESTRICCION_DIFUSION(
    RES_ID              NUMBER(*,0)         NOT NULL,
    RES_DESCRIPCION     VARCHAR2(100 CHAR),
    ACTIVO              NUMBER(1)           NOT NULL,
    PRIMARY KEY(RES_ID)
);

-- DATOS

INSERT INTO "DOC"."RESTRICCION_DIFUSION" (RES_ID, RES_DESCRIPCION, ACTIVO) VALUES ('1', 'DE SOLO CONOCIMIENTO', '1')
INSERT INTO "DOC"."RESTRICCION_DIFUSION" (RES_ID, RES_DESCRIPCION, ACTIVO) VALUES ('2', 'DE USO EXCLUSIVO', '1')

COMMIT;

-- SEQUENCE

CREATE SEQUENCE RESTRICCION_DIFUSION_SEQ START WITH 3 INCREMENT BY 1;

-- -----------------------------------------------------------------------------
-- ALTERACION DE LA TABLA: DOCUMENTO
-- -----------------------------------------------------------------------------
alter table DOCUMENTO add GRADO_EXTERNO VARCHAR2(32);
alter table DOCUMENTO add MARCA_AGUA_EXTERNO VARCHAR2(64);
alter table DOCUMENTO add RESTRICCION_DIFUSION NUMBER;

-- FOREIGN KEY

ALTER TABLE DOCUMENTO ADD CONSTRAINT DOC_RES_DIFUSION_FK
FOREIGN KEY (RESTRICCION_DIFUSION) REFERENCES RESTRICCION_DIFUSION (RES_ID);

-- INDEX

CREATE INDEX DOCUMENTO_RESTRICCION_IDX ON DOCUMENTO(RESTRICCION_DIFUSION);

-- -----------------------------------------------------------------------------
-- ALTERACION DE LA TABLA: DOCUMENTO_PDF
-- -----------------------------------------------------------------------------
ALTER TABLE DOCUMENTO_PDF ADD PDF_TEXTO84 VARCHAR2(4000 BYTE);
ALTER TABLE DOCUMENTO_PDF ADD PDF_TEXTO85 VARCHAR2(4000 BYTE);
ALTER TABLE DOCUMENTO_PDF ADD PDF_TEXTO86 VARCHAR2(4000 BYTE);

