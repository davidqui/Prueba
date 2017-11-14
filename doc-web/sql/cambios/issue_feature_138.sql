-- -----------------------------------------------------------------------------
-- 2017-11-14 edison.gonzalez@controltechcg.com Feature #131 (SICDI-Controntech)
-- feature-138
-- Ajuste del contador del numero de radicado.
-- -----------------------------------------------------------------------------

-- -----------------------------------------------------------------------------
-- 2017-11-14 edison.gonzalez@controltechcg.com Feature #138 (SICDI-Controntech)
-- feature-138
-- Creación de la tabla que relaciona el proceso con el indicativo del numero
-- de radicado.
-- -----------------------------------------------------------------------------

CREATE TABLE "DOC"."PROCESO_RADICADO"(
    "ID"            NUMBER(*,0) NOT NULL ENABLE, 
    "PRO_ID"        NUMBER(*,0), 
    "INDICATIVO"    NUMBER(1)   NOT NULL ENABLE, 
    CONSTRAINT "PROCESO_RADICADO_PK" PRIMARY KEY ("ID"),
    CONSTRAINT FK_PROCESO_RADICADO_1
    FOREIGN KEY (PRO_ID)
    REFERENCES PROCESO(PRO_ID)
);

-- INDEX

CREATE INDEX PROCESO_RADICADO_IDX1 ON PROCESO_RADICADO(PRO_ID);

-- SEQUENCE

CREATE SEQUENCE  "DOC"."PROCESO_RADICADO_SEQ"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 100;

-- DATOS

INSERT INTO "DOC"."PROCESO_RADICADO" (ID, PRO_ID, INDICATIVO) VALUES ('1', '9', '1');
INSERT INTO "DOC"."PROCESO_RADICADO" (ID, PRO_ID, INDICATIVO) VALUES ('2', '8', '2');
INSERT INTO "DOC"."PROCESO_RADICADO" (ID, PRO_ID, INDICATIVO) VALUES ('3', '41', '3');

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