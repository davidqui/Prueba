--
-- Author:  samuel.delgado@controltechcg.com
-- Created: 20/09/2018 Issue gogs #15 (SICDI-Controltech) feature-gogs-15
--

-- -----------------------------------------------------------------------------
-- TABLA: USUARIO
-- -----------------------------------------------------------------------------

ALTER TABLE DOC.USUARIO 
ADD CONSTRAINT USUARIO_DEPENDENCIA_FK
FOREIGN KEY (DEP_ID)
REFERENCES DOC.DEPENDENCIA (DEP_ID);
