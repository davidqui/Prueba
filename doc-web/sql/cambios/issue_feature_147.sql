-- -----------------------------------------------------------------------------
-- 2018-01-30 edison.gonzalez@controltechcg.com Feature #147 (SICDI-Controntech)
-- feature-147
-- Organizacion de las unidades jerarquicas.
-- -----------------------------------------------------------------------------

-- -----------------------------------------------------------------------------
-- TABLA: DEPENDENCIA
-- -----------------------------------------------------------------------------

--DEP_IND_ENVIO_DOCUMNETOS

ALTER TABLE DEPENDENCIA ADD DEP_IND_ENVIO_DOCUMNETOS NUMBER(1) DEFAULT 0;

CREATE INDEX DEPENDENCIA_IDX2 ON DEPENDENCIA(DEP_IND_ENVIO_DOCUMNETOS);