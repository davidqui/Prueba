--
-- Author:  jgarcia@controltechcg.com
-- Created: 08/05/2018 Issue #160 (SICDI-Controltech) feature-160
--

-- -----------------------------------------------------------------------------
-- TABLA: DOCUMENTO
-- -----------------------------------------------------------------------------

ALTER TABLE DOCUMENTO ADD (DOC_FIRMA_ENVIO_UUID VARCHAR2(32 CHAR));

CREATE INDEX DOCUMENTO_FIRMA_ENVIO_UUID_IDX ON DOCUMENTO (DOC_FIRMA_ENVIO_UUID);