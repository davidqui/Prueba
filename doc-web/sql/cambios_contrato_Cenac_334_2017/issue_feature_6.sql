--
-- Author:  edison.gonzalez@controltechcg.com
-- Created: 14/08/2018 Issue #6 (SICDI-Controltech) feature-6
--

-- -----------------------------------------------------------------------------
-- TABLA: USU_SEL_FAVORITOS
-- -----------------------------------------------------------------------------

CREATE SEQUENCE seq_USU_SEL_FAVORITOS;
CREATE TABLE USU_SEL_FAVORITOS(
    USU_SEL_ID  NUMBER NOT NULL,
    USU_ID      NUMBER NOT NULL,
    USU_FAV     NUMBER NOT NULL,
    CONTADOR    NUMBER NOT NULL,
    PRIMARY KEY (USU_SEL_ID),
    CONSTRAINT USUSEL_UNI_1 
    UNIQUE (USU_ID, USU_FAV)
);
COMMENT ON COLUMN USU_SEL_FAVORITOS.USU_SEL_ID IS 'Identificador unico de la tabla';
COMMENT ON COLUMN USU_SEL_FAVORITOS.USU_ID IS 'Identificador del usuario de origen';
COMMENT ON COLUMN USU_SEL_FAVORITOS.USU_FAV IS 'Identificador del usuario favorito';
COMMENT ON COLUMN USU_SEL_FAVORITOS.CONTADOR IS 'Identifica el numero de veces que selecciona el usuario favorito';
ALTER TABLE USU_SEL_FAVORITOS ADD CONSTRAINT FK_USU_SEL_ORIGEN FOREIGN KEY (USU_ID) REFERENCES USUARIO (USU_ID);
ALTER TABLE USU_SEL_FAVORITOS ADD CONSTRAINT FK_USU_SEL_FAVORITO FOREIGN KEY (USU_FAV) REFERENCES USUARIO (USU_ID);