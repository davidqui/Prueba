--
-- Author:  edisson.gonzalez@controltechcg.com
-- Created: 25/07/2018 Issue #182 (SICDI-Controltech) feature-182
--

-- -----------------------------------------------------------------------------
-- TABLA: NOTIFICACION_X_USUARIO
-- -----------------------------------------------------------------------------

create TABLE NOTIFICACION_X_USUARIO(
    NOT_USU_ID      NUMBER NOT NULL,
    USU_ID          NUMBER NOT NULL,
    TNF_ID          NUMBER NOT NULL,
    PRIMARY KEY (NOT_USU_ID),
    CONSTRAINT "FK_NOT_USUARIO" FOREIGN KEY ("USU_ID")
	  REFERENCES "DOC"."USUARIO" ("USU_ID"),
    CONSTRAINT "FK_NOT_NOTIFICACION" FOREIGN KEY ("TNF_ID")
	  REFERENCES "DOC"."TIPO_NOTIFICACION" ("TNF_ID")
);

-- -----------------------------------------------------------------------------
-- SEQUENCE: NOTIFICACION_X_USUARIO_SEQ
-- -----------------------------------------------------------------------------
CREATE SEQUENCE  "DOC"."NOTIFICACION_X_USUARIO_SEQ"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1;

UPDATE "DOC"."TIPO_NOTIFICACION" SET VALOR = '-100' WHERE TNF_ID = 60;
UPDATE "DOC"."TIPO_NOTIFICACION" SET VALOR = '-100' WHERE TNF_ID = 70;
