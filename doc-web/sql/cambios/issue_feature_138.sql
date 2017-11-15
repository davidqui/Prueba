-- -----------------------------------------------------------------------------
-- 2017-11-14 edison.gonzalez@controltechcg.com Feature #131 (SICDI-Controntech)
-- feature-138
-- Ajuste del contador del numero de radicado.
-- -----------------------------------------------------------------------------

-- -----------------------------------------------------------------------------
-- 2017-11-14 edison.gonzalez@controltechcg.com Feature #138 (SICDI-Controntech)
-- feature-138
-- Creación de la tabla en la cual se parametriza la relacion entre el proceso y
-- el indicativo del numero de radicado.
-- -----------------------------------------------------------------------------

CREATE TABLE "DOC"."RADICACION"(
    "RAD_ID"            NUMBER(*,0)     NOT NULL ENABLE,
    "RAD_NOMBRE"        VARCHAR2(50),
    "RAD_INDICATIVO"    NUMBER(1)       NOT NULL ENABLE,
    "SECUENCIA"         VARCHAR2(32)    NOT NULL ENABLE,
    "PROCESO"           NUMBER(*,0),
    CONSTRAINT "PROCESO_RADICADO_PK" PRIMARY KEY ("RAD_ID"),
    CONSTRAINT FK_RADICACION_1
    FOREIGN KEY (PROCESO)
    REFERENCES PROCESO(PRO_ID)
);

-- INDEX

CREATE INDEX RADICACION_IDX1 ON RADICACION(PROCESO);

-- SEQUENCE

CREATE SEQUENCE  "DOC"."RADICACION_SEQ"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 100;

-- DATOS

INSERT INTO "DOC"."RADICACION" (RAD_ID, RAD_NOMBRE, RAD_INDICATIVO, SECUENCIA, PROCESO) VALUES ('1', 'Radicado para registro de documentos', '1', 'RADICADO_REGISTRO', '9');
INSERT INTO "DOC"."RADICACION" (RAD_ID, RAD_NOMBRE, RAD_INDICATIVO, SECUENCIA, PROCESO) VALUES ('2', 'Radicado para registro de documentos internos', '2', 'RADICADO_INTERNO', '8');
INSERT INTO "DOC"."RADICACION" (RAD_ID, RAD_NOMBRE, RAD_INDICATIVO, SECUENCIA, PROCESO) VALUES ('3', 'Radicado para registro de documentos externos', '3', 'RADICADO_EXTERNO', '41');
INSERT INTO "DOC"."RADICACION" (RAD_ID, RAD_NOMBRE, RAD_INDICATIVO, SECUENCIA, PROCESO) VALUES ('4', 'Radicado para transferencia de archivo', '2', 'RADICADO_INTERNO', null);

COMMIT;

-- -----------------------------------------------------------------------------
-- 2017-11-14 edison.gonzalez@controltechcg.com Feature #138 (SICDI-Controntech)
-- feature-138
-- Creación de las secuencias segun los tres tipos de procesos que se tienen
-- actualmente.
-- -----------------------------------------------------------------------------

CREATE SEQUENCE  "DOC"."RADICADO_REGISTRO"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1;
CREATE SEQUENCE  "DOC"."RADICADO_INTERNO"   MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1;
CREATE SEQUENCE  "DOC"."RADICADO_EXTERNO"   MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1;