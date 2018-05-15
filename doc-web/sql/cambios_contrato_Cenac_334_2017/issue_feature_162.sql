--
-- Author:  jgarcia@controltechcg.com
-- Created: 11/05/2018 Issue #162 (SICDI-Controltech) feature-162
--

-- -----------------------------------------------------------------------------
-- TABLA: ROL
-- -----------------------------------------------------------------------------

INSERT INTO ROL 
    (ROL_ID, QUIEN, CUANDO, QUIEN_MOD, CUANDO_MOD, ACTIVO, ROL_NOMBRE) 
VALUES 
    ('PROCESAR_REGISTRO_ACTAS', 3390, SYSDATE, 3390, SYSDATE, 1, 'Procesar Registro de actas')
;

INSERT INTO PERFIL_ROL 
    (PER_ROL_ID, ROL_ID, PER_ID, QUIEN, CUANDO, QUIEN_MOD, CUANDO_MOD, ACTIVO) 
SELECT
    NULL, 'PROCESAR_REGISTRO_ACTAS', PER_ID, 3390, SYSDATE, 3390, SYSDATE, 1
FROM
    PERFIL
WHERE
    ACTIVO = 1;

-- UPDATE ROL SET ACTIVO = 0 WHERE ROL_ID = 'PROCESAR_REGISTRO_ACTAS';

-- -----------------------------------------------------------------------------
-- TABLA: PROCESO
-- -----------------------------------------------------------------------------

INSERT INTO PROCESO
    (PRO_ID, PRO_NOMBRE, CUANDO, QUIEN, CUANDO_MOD, QUIEN_MOD, ACTIVO, PRO_DESCRIPCION,
        PRO_FACADE, PRO_IMAGEN, PRO_ID_RESPUESTA, PRO_ALIAS)
VALUES 
    (100, 'Registro de actas', SYSDATE, 3390, SYSDATE, 3390, 1, 'Proceso documental para el registro de actas',
        null, 'registro-actas.png', null, null)
;

-- UPDATE PROCESO SET ACTIVO = 0 WHERE PRO_ID = 100;

-- -----------------------------------------------------------------------------
-- TABLA: PROCESO_ESTADO
-- -----------------------------------------------------------------------------

INSERT INTO PROCESO_ESTADO 
    (PES_ID, PES_NOMBRE, PES_DESCRIPCION, CUANDO, QUIEN, CUANDO_MOD, QUIEN_MOD,
        ACTIVO, PRO_ID, PES_INICIAL, PES_FINAL, PES_LOCATION, PES_REASIGNACION,
        PES_TRUNCATED) 
VALUES
    (150, 'Registro de datos del acta', 'Registra la información inicial del acta', SYSDATE, 3390, SYSDATE, 3390,
        1, 100, 1, 0, '/documento-acta?pin={instancia.id}', null,
        null)
;

INSERT INTO PROCESO_ESTADO 
    (PES_ID, PES_NOMBRE, PES_DESCRIPCION, CUANDO, QUIEN, CUANDO_MOD, QUIEN_MOD,
        ACTIVO, PRO_ID, PES_INICIAL, PES_FINAL, PES_LOCATION, PES_REASIGNACION,
        PES_TRUNCATED) 
VALUES
    (151, 'Anulado', 'Acta anulada', SYSDATE, 3390, SYSDATE, 3390,
        1, 100, 0, 1, '/documento-acta?pin={instancia.id}', null,
        null)
;

INSERT INTO PROCESO_ESTADO 
    (PES_ID, PES_NOMBRE, PES_DESCRIPCION, CUANDO, QUIEN, CUANDO_MOD, QUIEN_MOD,
        ACTIVO, PRO_ID, PES_INICIAL, PES_FINAL, PES_LOCATION, PES_REASIGNACION,
        PES_TRUNCATED) 
VALUES
    (152, 'Número de radicación generado', 'El acta cuenta con número de radicación generado y se encuentra en espera de la carga del archivo', SYSDATE, 3390, SYSDATE, 3390,
        1, 100, 0, 0, '/documento-acta?pin={instancia.id}', null,
        null)
;

INSERT INTO PROCESO_ESTADO 
    (PES_ID, PES_NOMBRE, PES_DESCRIPCION, CUANDO, QUIEN, CUANDO_MOD, QUIEN_MOD,
        ACTIVO, PRO_ID, PES_INICIAL, PES_FINAL, PES_LOCATION, PES_REASIGNACION,
        PES_TRUNCATED) 
VALUES
    (153, 'Acta digitalizada', 'El acta se encuentra digitalizada y archivada en el sistema', SYSDATE, 3390, SYSDATE, 3390,
        1, 100, 0, 1, '/documento-acta?pin={instancia.id}', null,
        null)
;

-- UPDATE PROCESO_ESTADO SET ACTIVO = 0 WHERE PRO_ID = 100;

-- -----------------------------------------------------------------------------
-- TABLA: PROCESO_VAR
-- -----------------------------------------------------------------------------

INSERT INTO PROCESO_VAR 
    (PVA_ID, PRO_ID, PVA_KEY, PVA_VALUE, CUANDO, QUIEN, CUANDO_MOD, QUIEN_MOD, ACTIVO) 
VALUES 
    (150, 100, 'proc.current.location', '/documento-acta?pin={instancia.id}', SYSDATE, 3390, SYSDATE, 3390, 1)
;

INSERT INTO PROCESO_VAR 
    (PVA_ID, PRO_ID, PVA_KEY, PVA_VALUE, CUANDO, QUIEN, CUANDO_MOD, QUIEN_MOD, ACTIVO) 
VALUES 
    (151, 100, 'doc.mode', 'registro', SYSDATE, 3390, SYSDATE, 3390, 1)
;

INSERT INTO PROCESO_VAR 
    (PVA_ID, PRO_ID, PVA_KEY, PVA_VALUE, CUANDO, QUIEN, CUANDO_MOD, QUIEN_MOD, ACTIVO) 
VALUES 
    (152, 100, 'doc.remitente.mode', 'texto', SYSDATE, 3390, SYSDATE, 3390, 1)
;

-- UPDATE PROCESO_VAR SET ACTIVO = 0 WHERE PRO_ID = 100;

-- -----------------------------------------------------------------------------
-- TABLA: PROCESO_TRANSICION
-- -----------------------------------------------------------------------------

INSERT INTO PROCESO_TRANSICION 
    (PTR_ID, PTT_ID, PTR_DEFINICION, PTR_NOMBRE, PES_ID_INICIAL, PES_ID_FINAL,
        CUANDO, QUIEN, CUANDO_MOD, QUIEN_MOD, ACTIVO) 
VALUES 
    (150, 1, '/documento-acta/generar-numero-radicado', 'Generar No. Radicado', 150, 152,
        SYSDATE, 3390, SYSDATE, 3390, 1) 
;

INSERT INTO PROCESO_TRANSICION 
    (PTR_ID, PTT_ID, PTR_DEFINICION, PTR_NOMBRE, PES_ID_INICIAL, PES_ID_FINAL,
        CUANDO, QUIEN, CUANDO_MOD, QUIEN_MOD, ACTIVO) 
VALUES 
    (151, 1, '/documento-acta/anular', 'Anular', 150, 151,
        SYSDATE, 3390, SYSDATE, 3390, 1) 
;

INSERT INTO PROCESO_TRANSICION 
    (PTR_ID, PTT_ID, PTR_DEFINICION, PTR_NOMBRE, PES_ID_INICIAL, PES_ID_FINAL,
        CUANDO, QUIEN, CUANDO_MOD, QUIEN_MOD, ACTIVO) 
VALUES 
    (152, 1, '/documento-acta/cargar-acta-digitalizada', 'Digitalizar', 152, 153,
        SYSDATE, 3390, SYSDATE, 3390, 1) 
;

-- UPDATE PROCESO_TRANSICION SET ACTIVO = 0 WHERE PTR_ID IN (150, 151, 152);

-- -----------------------------------------------------------------------------
-- TABLA: DOCUMENTO_ACTA
-- -----------------------------------------------------------------------------

CREATE TABLE DOCUMENTO_ACTA (
    DOC_ID  VARCHAR2(32 CHAR)   NOT NULL,
    LUGAR   VARCHAR2(64 CHAR),
    PRIMARY KEY (DOC_ID)
);

COMMENT ON TABLE DOCUMENTO_ACTA
IS 'Información complementaria de documentos tipo acta.';

COMMENT ON COLUMN DOCUMENTO_ACTA.DOC_ID
IS 'ID del documento asociado como acta.';
COMMENT ON COLUMN DOCUMENTO_ACTA.LUGAR
IS 'Ciudad donde se llevó a cabo la actividad que motivo la elaboración del acta..';

ALTER TABLE DOCUMENTO_ACTA
ADD CONSTRAINT DOCUMENTO_ACTA_DOC_ID_FK
FOREIGN KEY (DOC_ID)
REFERENCES DOCUMENTO (DOC_ID);