--
-- Author:  samuel.delgado@controltechcg.com
-- Created: 15/08/2018 Issue gogs #7 (SICDI-Controltech) feature-gogs-7
--

-- -----------------------------------------------------------------------------
-- TABLA: USUARIO
-- -----------------------------------------------------------------------------

ALTER TABLE USUARIO ADD (
    USU_ACTIVO NUMBER(1,0) DEFAULT 1 NOT NULL, 
    USU_TEXTO_ACTIVO VARCHAR2(255 CHAR) NULL
);