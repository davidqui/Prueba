--
-- Author:  jgarcia@controltechcg.com
-- Created: 23/05/2018 Issue #172 (SICDI-Controltech) feature-172
--

-- -----------------------------------------------------------------------------
-- TABLA: DOC_OBSERVACION_DEFECTO
-- -----------------------------------------------------------------------------

CREATE TABLE DOC_OBSERVACION_DEFECTO (
    DOD_ID  NUMBER(38)  NOT NULL,
    TEXTO_OBSERVACION   VARCHAR2(64 CHAR)   NOT NULL,
    ACTIVO              NUMBER(1)           NOT NULL,
    QUIEN               NUMBER(38)          NOT NULL,
    CUANDO              DATE                NOT NULL,
    QUIEN_MOD           NUMBER(38)          NOT NULL,
    CUANDO_MOD          DATE                NOT NULL,
    PRIMARY KEY (DOD_ID)
);

COMMENT ON TABLE DOC_OBSERVACION_DEFECTO
IS 'Observaciones por defecto.';

COMMENT ON COLUMN DOC_OBSERVACION_DEFECTO.DOD_ID
IS 'ID autoincrementable.';
COMMENT ON COLUMN DOC_OBSERVACION_DEFECTO.TEXTO_OBSERVACION
IS 'Texto de la observación.';
COMMENT ON COLUMN DOC_OBSERVACION_DEFECTO.ACTIVO
IS 'Indicador de actividad (0=activo;1=inactivo).';
COMMENT ON COLUMN DOC_OBSERVACION_DEFECTO.QUIEN
IS 'ID del usuario creador de la observación.';
COMMENT ON COLUMN DOC_OBSERVACION_DEFECTO.CUANDO
IS 'Fecha y hora de creación de la observación.';
COMMENT ON COLUMN DOC_OBSERVACION_DEFECTO.QUIEN_MOD
IS 'ID del último usuario que modificó la observación.';
COMMENT ON COLUMN DOC_OBSERVACION_DEFECTO.CUANDO_MOD
IS 'Fecha y hora de la última modificación de la observación.';

CREATE SEQUENCE DOC_OBSERVACION_DEFECTO_SEQ;

ALTER TABLE DOC_OBSERVACION_DEFECTO
ADD CONSTRAINT DOC_OBS_DEFECTO_QUIEN_FK
FOREIGN KEY (QUIEN)
REFERENCES USUARIO (USU_ID);

ALTER TABLE DOC_OBSERVACION_DEFECTO
ADD CONSTRAINT DOC_OBS_DEFECTO_QUIEN_MOD_FK
FOREIGN KEY (QUIEN_MOD)
REFERENCES USUARIO (USU_ID);

CREATE INDEX DOC_OBS_DEFECTO_ACTIVO_IDX 
ON DOC_OBSERVACION_DEFECTO (ACTIVO);

-- -----------------------------------------------------------------------------
-- TABLA: ROL
-- -----------------------------------------------------------------------------

INSERT INTO ROL 
    (ROL_ID, QUIEN, CUANDO, QUIEN_MOD, CUANDO_MOD, ACTIVO, ROL_NOMBRE) 
VALUES 
    ('ADMIN_DOC_OBSERVACION_DEFECTO', 3390, SYSDATE, 3390, SYSDATE, 1, 'Administrar Observaciones por defecto')
;

