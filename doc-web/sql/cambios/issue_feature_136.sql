-- -----------------------------------------------------------------------------
-- 2017-10-31 edison.gonzalez@controltechcg.com Feature #136 (SICDI-Controntech)
-- feature-136
-- Creación de indices para que la consulta del motor de busqueda sea mas rapida.
-- -----------------------------------------------------------------------------

-- -----------------------------------------------------------------------------
-- TABLA: DOCUMENTO
-- -----------------------------------------------------------------------------
--USU_ID_ULTIMA_ACCION
CREATE INDEX "DOC"."DOCUMENTO_IDX_1" ON "DOC"."DOCUMENTO" ("USU_ID_ULTIMA_ACCION");

--DEP_ID_DES
CREATE INDEX "DOC"."DOCUMENTO_IDX_2" ON "DOC"."DOCUMENTO" ("DEP_ID_DES");

--USU_ID_ELABORA
CREATE INDEX "DOC"."DOCUMENTO_IDX_3" ON "DOC"."DOCUMENTO" ("USU_ID_ELABORA");

--USU_ID_APRUEBA
CREATE INDEX "DOC"."DOCUMENTO_IDX_4" ON "DOC"."DOCUMENTO" ("USU_ID_APRUEBA");

--USU_ID_VISTO_BUENO
CREATE INDEX "DOC"."DOCUMENTO_IDX_5" ON "DOC"."DOCUMENTO" ("USU_ID_VISTO_BUENO");

--USU_ID_FIRMA
CREATE INDEX "DOC"."DOCUMENTO_IDX_6" ON "DOC"."DOCUMENTO" ("USU_ID_FIRMA");

--CLA_ID
CREATE INDEX "DOC"."DOCUMENTO_IDX_7" ON "DOC"."DOCUMENTO" ("CLA_ID");

-- -----------------------------------------------------------------------------
-- TABLA: S_INSTANCIA_USUARIO_IDX_1
-- -----------------------------------------------------------------------------

--PIN_ID
CREATE INDEX "DOC"."S_INSTANCIA_USUARIO_IDX_1" ON "DOC"."S_INSTANCIA_USUARIO" ("PIN_ID");

--USU_ID
CREATE INDEX "DOC"."S_INSTANCIA_USUARIO_IDX_2" ON "DOC"."S_INSTANCIA_USUARIO" ("USU_ID");