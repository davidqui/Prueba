-- -----------------------------------------------------------------------------
-- 2017-11-14 edison.gonzalez@controltechcg.com Feature #131 (SICDI-Controntech)
-- feature-138
-- Ajuste del contador del numero de radicado.
-- -----------------------------------------------------------------------------

-- -----------------------------------------------------------------------------
-- 2017-11-14 edison.gonzalez@controltechcg.com Feature #138 (SICDI-Controntech)
-- feature-138
-- Creación de las secuencias segun los tres tipos de procesos que se tienen
-- actualmente.
-- -----------------------------------------------------------------------------

CREATE SEQUENCE  "DOC"."RADICADO_REGISTRO_SEQ"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1;
CREATE SEQUENCE  "DOC"."RADICADO_INTERNO_SEQ"   MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1;
CREATE SEQUENCE  "DOC"."RADICADO_EXTERNO_SEQ"   MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1;

-- -----------------------------------------------------------------------------
-- 2017-11-16 edison.gonzalez@controltechcg.com Feature #138 (SICDI-Controntech)
-- feature-138
-- Creación de la tabla en la cual se parametriza las secuencias utilizadas en 
-- los numeros de radicacion.
-- -----------------------------------------------------------------------------

CREATE TABLE "DOC"."SECUENCIA_RADICACION"(
    "SEQ_ID"        NUMBER(*,0)     NOT NULL ENABLE,
    "SEQ_NOMBRE"    VARCHAR2(32)    NOT NULL ENABLE,
    CONSTRAINT "SECUENCIA_RADICACION_PK" PRIMARY KEY ("SEQ_ID")
);

-- UNIQUE INDEX

ALTER TABLE SECUENCIA_RADICACION ADD CONSTRAINT SECUENCIA_RADICACION_UK UNIQUE (SEQ_NOMBRE);

-- SEQUENCE

CREATE SEQUENCE  "DOC"."SECUENCIA_RADICACION_SEQ"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1;

-- DATOS

INSERT INTO "DOC"."SECUENCIA_RADICACION" (SEQ_ID, SEQ_NOMBRE) VALUES ('1', 'RADICADO_REGISTRO_SEQ');
INSERT INTO "DOC"."SECUENCIA_RADICACION" (SEQ_ID, SEQ_NOMBRE) VALUES ('2', 'RADICADO_INTERNO_SEQ');
INSERT INTO "DOC"."SECUENCIA_RADICACION" (SEQ_ID, SEQ_NOMBRE) VALUES ('3', 'RADICADO_EXTERNO_SEQ');

COMMIT;

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
    "SECUENCIA"         NUMBER(*,0)     NOT NULL ENABLE,
    "PROCESO"           NUMBER(*,0),
    CONSTRAINT "PROCESO_RADICADO_PK" PRIMARY KEY ("RAD_ID"),
    CONSTRAINT FK_RADICACION_1 FOREIGN KEY (PROCESO) REFERENCES PROCESO(PRO_ID),
    CONSTRAINT FK_RADICACION_2 FOREIGN KEY (SECUENCIA) REFERENCES SECUENCIA_RADICACION(SEQ_ID)
);

-- INDEX

CREATE INDEX RADICACION_IDX1 ON RADICACION(PROCESO);

-- SEQUENCE

CREATE SEQUENCE  "DOC"."RADICACION_SEQ"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 100;

-- DATOS

INSERT INTO "DOC"."RADICACION" (RAD_ID, RAD_NOMBRE, RAD_INDICATIVO, SECUENCIA, PROCESO) VALUES ('1', 'Radicado para registro de documentos', '1', '1', '9');
INSERT INTO "DOC"."RADICACION" (RAD_ID, RAD_NOMBRE, RAD_INDICATIVO, SECUENCIA, PROCESO) VALUES ('2', 'Radicado para registro de documentos internos', '2', '2', '8');
INSERT INTO "DOC"."RADICACION" (RAD_ID, RAD_NOMBRE, RAD_INDICATIVO, SECUENCIA, PROCESO) VALUES ('3', 'Radicado para registro de documentos externos', '3', '3', '41');
INSERT INTO "DOC"."RADICACION" (RAD_ID, RAD_NOMBRE, RAD_INDICATIVO, SECUENCIA, PROCESO) VALUES ('4', 'Radicado para transferencia de archivo', '2', '2', null);

COMMIT;

-- -----------------------------------------------------------------------------
-- 2017-11-16 edison.gonzalez@controltechcg.com Feature #138 (SICDI-Controntech)
-- feature-138
-- Creación de la tabla encabezado que audita el proceso de reinicio de secuencias.
-- -----------------------------------------------------------------------------

CREATE TABLE "DOC"."PROCESO_REINICIO_CONTADOR"(
    "ID"                    NUMBER(*,0)     NOT NULL ENABLE,
    "FECHA_HORA_EJECUCION"  TIMESTAMP       NOT NULL ENABLE,
    "IP_EJECUCION"          VARCHAR2(50)    NOT NULL ENABLE,
    CONSTRAINT "PROCESO_REINICIO_CONTADOR_PK" PRIMARY KEY ("ID")
);

-- SEQUENCE

CREATE SEQUENCE  "DOC"."PROCESO_REINICIO_CONTADOR_SEQ"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1;

-- -----------------------------------------------------------------------------
-- 2017-11-16 edison.gonzalez@controltechcg.com Feature #138 (SICDI-Controntech)
-- feature-138
-- Creación de la tabla detalle que audita el proceso de reinicio de secuencias.
-- -----------------------------------------------------------------------------

CREATE TABLE "DOC"."PROCESO_REINICIO_CONT_DETALLE"(
    "ID"                    NUMBER(*,0)     NOT NULL ENABLE,
    "SECUENCIA"             NUMBER(*,0)     NOT NULL ENABLE,
    "SECUENCIA_NOMBRE"      VARCHAR2(32)    NOT NULL ENABLE,
    "ULTIMO_VALOR_SEQ"      NUMBER(*,0)     NOT NULL ENABLE,
    "NUEVO_VALOR_SEQ"       NUMBER(*,0)     NOT NULL ENABLE,
    "PRO_REINICIO_CONTADOR" NUMBER(*,0)     NOT NULL ENABLE,
    CONSTRAINT "PROCESO_REINICIO_CONT_DETALLE_PK" PRIMARY KEY ("ID"),
    CONSTRAINT FK_PRO_CONT_DETALLE_1 FOREIGN KEY (SECUENCIA) REFERENCES SECUENCIA_RADICACION(SEQ_ID),
    CONSTRAINT FK_PRO_CONT_DETALLE_2 FOREIGN KEY (PRO_REINICIO_CONTADOR) REFERENCES PROCESO_REINICIO_CONTADOR(ID)
);

-- SEQUENCE

CREATE SEQUENCE  "DOC"."PRO_REINICIO_CONT_DETALLE_SEQ"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1;