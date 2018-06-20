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
        1, 100, 0, 0, '/documento-acta/generar-numero-radicado?pin={instancia.id}', null,
        null)
;

INSERT INTO PROCESO_ESTADO 
    (PES_ID, PES_NOMBRE, PES_DESCRIPCION, CUANDO, QUIEN, CUANDO_MOD, QUIEN_MOD,
        ACTIVO, PRO_ID, PES_INICIAL, PES_FINAL, PES_LOCATION, PES_REASIGNACION,
        PES_TRUNCATED) 
VALUES
    (153, 'Acta digitalizada', 'El acta se encuentra digitalizada y archivada en el sistema', SYSDATE, 3390, SYSDATE, 3390,
        1, 100, 0, 1, '/documento-acta/cargar-acta-digitalizada?pin={instancia.id}', null,
        null)
;

INSERT INTO PROCESO_ESTADO 
    (PES_ID, PES_NOMBRE, PES_DESCRIPCION, CUANDO, QUIEN, CUANDO_MOD, QUIEN_MOD,
        ACTIVO, PRO_ID, PES_INICIAL, PES_FINAL, PES_LOCATION, PES_REASIGNACION,
        PES_TRUNCATED) 
VALUES
    (154, 'Registro de usuarios del acta', 'El acta cuenta con la información registrada y en espera de la selección de usuarios', SYSDATE, 3390, SYSDATE, 3390,
        1, 100, 0, 0, '/documento-acta/seleccionar-usuarios?pin={instancia.id}', null,
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
    (150, 1, '/documento-acta/generar-numero-radicado?pin={instancia.id}&tid={transicion.id}', 'Generar No. Radicado', 154, 152,
        SYSDATE, 3390, SYSDATE, 3390, 1) 
;

INSERT INTO PROCESO_TRANSICION 
    (PTR_ID, PTT_ID, PTR_DEFINICION, PTR_NOMBRE, PES_ID_INICIAL, PES_ID_FINAL,
        CUANDO, QUIEN, CUANDO_MOD, QUIEN_MOD, ACTIVO) 
VALUES 
    (151, 1, '/documento-acta/seleccionar-usuarios?pin={instancia.id}&tid={transicion.id}', 'Seleccionar Usuarios', 150, 154,
        SYSDATE, 3390, SYSDATE, 3390, 1) 
;

INSERT INTO PROCESO_TRANSICION 
    (PTR_ID, PTT_ID, PTR_DEFINICION, PTR_NOMBRE, PES_ID_INICIAL, PES_ID_FINAL,
        CUANDO, QUIEN, CUANDO_MOD, QUIEN_MOD, ACTIVO) 
VALUES 
    (152, 1, '/documento-acta/cargar-acta-digitalizada?pin={instancia.id}&tid={transicion.id}', 'Digitalizar', 152, 153,
        SYSDATE, 3390, SYSDATE, 3390, 1) 
;

INSERT INTO PROCESO_TRANSICION 
    (PTR_ID, PTT_ID, PTR_DEFINICION, PTR_NOMBRE, PES_ID_INICIAL, PES_ID_FINAL,
        CUANDO, QUIEN, CUANDO_MOD, QUIEN_MOD, ACTIVO) 
VALUES 
    (153, 1, '/documento-acta/anular?pin={instancia.id}&tid={transicion.id}', 'Anular', 150, 151,
        SYSDATE, 3390, SYSDATE, 3390, 1) 
;

-- UPDATE PROCESO_TRANSICION SET ACTIVO = 0 WHERE PTR_ID IN (150, 151, 152);

-- UPDATE PROCESO_TRANSICION SET PTR_DEFINICION = '/documento-acta/generar-numero-radicado?pin={instancia.id}&tid={transicion.id}' WHERE PTR_ID = 150;
-- UPDATE PROCESO_TRANSICION SET PTR_DEFINICION = '/documento-acta/anular?pin={instancia.id}&tid={transicion.id}' WHERE PTR_ID = 151;
-- UPDATE PROCESO_TRANSICION SET PTR_DEFINICION = '/documento-acta/cargar-acta-digitalizada?pin={instancia.id}&tid={transicion.id}' WHERE PTR_ID = 152;
-- UPDATE PROCESO_TRANSICION SET PES_ID_INICIAL = 154 WHERE PTR_ID = 150;

-- -----------------------------------------------------------------------------
-- TABLA: DOCUMENTO
-- -----------------------------------------------------------------------------

-- DROP TABLE IF EXISTS DOCUMENTO_ACTA;

ALTER TABLE DOCUMENTO ADD (ACTA_LUGAR               VARCHAR2(64 CHAR));
ALTER TABLE DOCUMENTO ADD (ACTA_FECHA_ELABORACION   DATE);
ALTER TABLE DOCUMENTO ADD (ACTA_DESCRIPCION         VARCHAR2(2000 CHAR));

COMMENT ON COLUMN DOCUMENTO.ACTA_LUGAR
IS 'Ciudad donde se llevó a cabo la actividad que motivo la elaboración del acta.';
COMMENT ON COLUMN DOCUMENTO.ACTA_FECHA_ELABORACION
IS 'Determina la fecha donde se llevó a cabo la actividad que motivo la elaboración del acta.';
COMMENT ON COLUMN DOCUMENTO.ACTA_DESCRIPCION
IS 'Determina la descripción del acta.';

CREATE INDEX DOC_ACTA_FECHA_ELABORACION_IDX ON DOCUMENTO (ACTA_FECHA_ELABORACION);

-- -----------------------------------------------------------------------------
-- TABLA: RADICACION
-- -----------------------------------------------------------------------------

-- TODO: Pendiente determinar el número con el cual iniciará esta secuencia.
CREATE SEQUENCE RADICADO_REGISTRO_ACTAS_SEQ START WITH 12345;

INSERT INTO SECUENCIA_RADICACION 
    (SEQ_ID, SEQ_NOMBRE)
VALUES 
    (4, 'RADICADO_REGISTRO_ACTAS_SEQ')
;

-- -----------------------------------------------------------------------------
-- TABLA: RADICACION
-- -----------------------------------------------------------------------------

INSERT INTO RADICACION
    (RAD_ID, RAD_NOMBRE, RAD_INDICATIVO, SECUENCIA, PROCESO) 
VALUES
    (5, 'Radicado para Registro de Actas', 4, 4, 100)
;

-- -----------------------------------------------------------------------------
-- TABLA: USUARIO_X_DOCUMENTO_ACTA
-- -----------------------------------------------------------------------------

CREATE TABLE USUARIO_X_DOCUMENTO_ACTA (
    UXA_ID  NUMBER(38)          NOT NULL,
    USU_ID  NUMBER(38)          NOT NULL,
    CAR_ID  NUMBER(38)          NOT NULL,
    DOC_ID  VARCHAR2(32 CHAR)   NOT NULL,
    ACTIVO  NUMBER(1)           NOT NULL,
    QUIEN   NUMBER(38)          NOT NULL,
    CUANDO  DATE                NOT NULL,
    PRIMARY KEY (UXA_ID)
);

COMMENT ON TABLE USUARIO_X_DOCUMENTO_ACTA
IS 'Relación de usuarios por documento acta.';

COMMENT ON COLUMN USUARIO_X_DOCUMENTO_ACTA.UXA_ID
IS 'ID autoincrementable.';
COMMENT ON COLUMN USUARIO_X_DOCUMENTO_ACTA.USU_ID
IS 'ID del usuario.';
COMMENT ON COLUMN USUARIO_X_DOCUMENTO_ACTA.CAR_ID
IS 'ID del cargo del usuario.';
COMMENT ON COLUMN USUARIO_X_DOCUMENTO_ACTA.DOC_ID
IS 'ID del documento acta.';
COMMENT ON COLUMN USUARIO_X_DOCUMENTO_ACTA.ACTIVO
IS 'Indicador de actividad (0=activo;1=inactivo).';
COMMENT ON COLUMN USUARIO_X_DOCUMENTO_ACTA.QUIEN
IS 'ID del usuario creador del registro.';
COMMENT ON COLUMN USUARIO_X_DOCUMENTO_ACTA.CUANDO
IS 'Fecha y hora de creación del registro.';

CREATE SEQUENCE USUARIO_X_DOCUMENTO_ACTA_SEQ;

ALTER TABLE USUARIO_X_DOCUMENTO_ACTA
ADD CONSTRAINT USU_X_DOC_ACTA_USU_ID_FK
FOREIGN KEY (USU_ID)
REFERENCES USUARIO (USU_ID);

ALTER TABLE USUARIO_X_DOCUMENTO_ACTA
ADD CONSTRAINT USU_X_DOC_ACTA_CAR_ID_FK
FOREIGN KEY (CAR_ID)
REFERENCES CARGO (CAR_ID);

ALTER TABLE USUARIO_X_DOCUMENTO_ACTA
ADD CONSTRAINT USU_X_DOC_ACTA_DOC_ID_FK
FOREIGN KEY (DOC_ID)
REFERENCES DOCUMENTO (DOC_ID);

ALTER TABLE USUARIO_X_DOCUMENTO_ACTA
ADD CONSTRAINT USU_X_DOC_ACTA_QUIEN_FK
FOREIGN KEY (QUIEN)
REFERENCES USUARIO (USU_ID);

CREATE INDEX USU_X_DOC_ACTIVO_IDX
ON USUARIO_X_DOCUMENTO_ACTA (ACTIVO);