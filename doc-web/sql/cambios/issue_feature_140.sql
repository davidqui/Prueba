-- -----------------------------------------------------------------------------
-- 2017-11-22 edison.gonzalez@controltechcg.com Feature #140 (SICDI-Controntech)
-- feature-140
-- Organizacion de las unidades jerarquicas.
-- -----------------------------------------------------------------------------

-- -----------------------------------------------------------------------------
-- TABLA: DEPENDENCIA
-- -----------------------------------------------------------------------------

--DEP_PADRE_ORGANICO

ALTER TABLE "DOC"."DEPENDENCIA" ADD DEP_PADRE_ORGANICO NUMBER;

CREATE INDEX DEPENDENCIA_IDX1 ON DEPENDENCIA(DEP_PADRE_ORGANICO);