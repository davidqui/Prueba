--
-- Author:  jgarcia@controltechcg.com
-- Created: 08/05/2018 Issue #160 (SICDI-Controltech) feature-160
--

-- -----------------------------------------------------------------------------
-- TABLA: DOCUMENTO
-- -----------------------------------------------------------------------------

ALTER TABLE DOCUMENTO ADD (DOC_FIRMA_ENVIO_UUID VARCHAR2(32 CHAR));

CREATE INDEX DOCUMENTO_FIRMA_ENVIO_UUID_IDX ON DOCUMENTO (DOC_FIRMA_ENVIO_UUID);

-- -----------------------------------------------------------------------------
-- TABLA: ROL
-- -----------------------------------------------------------------------------

INSERT INTO ROL 
(ROL_ID, QUIEN, CUANDO, QUIEN_MOD, CUANDO_MOD, ACTIVO, ROL_NOMBRE) 
VALUES 
('BUSCAR_X_DOC_FIRMA_ENVIO_UUID', 3390, SYSDATE, 3390, SYSDATE, 1, 'Buscar Documentos por Firma/Envío UUID');

INSERT INTO ROL 
(ROL_ID, QUIEN, CUANDO, QUIEN_MOD, CUANDO_MOD, ACTIVO, ROL_NOMBRE) 
VALUES 
('VALIDAR_PDF_DOCFIRMAENVIO_UUID', 3390, SYSDATE, 3390, SYSDATE, 1, 'Validar Documentos PDF por Firma/Envío UUID');