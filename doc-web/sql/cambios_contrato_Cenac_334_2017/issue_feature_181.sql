--
-- Author:  edison.gonzalez@controltechcg.com
-- Created: 27/07/2018 Issue #181 (SICDI-Controltech) feature-181
--

-- -----------------------------------------------------------------------------
-- BORRADO
-- -----------------------------------------------------------------------------
DROP SEQUENCE seq_PAR_NOMBRE_EXPEDIENTE;
DROP TABLE PAR_NOMBRE_EXPEDIENTE CASCADE CONSTRAINTS;
DROP SEQUENCE seq_EXPEDIENTE_ESTADO;
DROP TABLE EXPEDIENTE_ESTADO CASCADE CONSTRAINTS;
DROP SEQUENCE seq_EXPEDIENTE_TRANSICION;
DROP TABLE EXPEDIENTE_TRANSICION CASCADE CONSTRAINTS;
DROP SEQUENCE seq_EXPEDIENTE;
DROP TABLE EXPEDIENTE CASCADE CONSTRAINTS;
DROP SEQUENCE seq_EXP_OBSERVACION;
DROP TABLE EXP_OBSERVACION CASCADE CONSTRAINTS;
DROP SEQUENCE seq_EXP_TRD;
DROP TABLE EXP_TRD CASCADE CONSTRAINTS;
DROP SEQUENCE seq_EXP_USUARIO;
DROP TABLE EXP_USUARIO CASCADE CONSTRAINTS;
DROP SEQUENCE seq_EXP_DOCUMENTO;
DROP TABLE EXP_DOCUMENTO CASCADE CONSTRAINTS;

-- -----------------------------------------------------------------------------
-- TABLA: PAR_NOMBRE_EXPEDIENTE
-- -----------------------------------------------------------------------------

CREATE SEQUENCE seq_PAR_NOMBRE_EXPEDIENTE;
CREATE TABLE PAR_NOMBRE_EXPEDIENTE (
  PAR_ID           number(10) NOT NULL, 
  PAR_NOMBRE       varchar2(255) NOT NULL, 
  USU_CREACION     number(10) NOT NULL, 
  FEC_CREACION     timestamp(0) NOT NULL, 
  USU_MODIFICACION number(10), 
  FEC_MODIFICACION timestamp(0), 
  PRIMARY KEY (PAR_ID), 
  CONSTRAINT PAR_NOM_EXP_UNI1 
    UNIQUE (PAR_NOMBRE));
COMMENT ON COLUMN PAR_NOMBRE_EXPEDIENTE.PAR_ID IS 'Identificador unico de la tabla.';
COMMENT ON COLUMN PAR_NOMBRE_EXPEDIENTE.PAR_NOMBRE IS 'Descripción del parametro a visualizar en la creación del nombre del expediente.';
COMMENT ON COLUMN PAR_NOMBRE_EXPEDIENTE.USU_CREACION IS 'Identificador del usuario quien creo el registro';
COMMENT ON COLUMN PAR_NOMBRE_EXPEDIENTE.FEC_CREACION IS 'Fecha de creación del registro';
COMMENT ON COLUMN PAR_NOMBRE_EXPEDIENTE.USU_MODIFICACION IS 'Identificador del usuario quien modifico el registro';
COMMENT ON COLUMN PAR_NOMBRE_EXPEDIENTE.FEC_MODIFICACION IS 'Fecha de modificación del registro';
ALTER TABLE PAR_NOMBRE_EXPEDIENTE ADD CONSTRAINT FK_PAR_NOM_EXP_USU_CREADOR FOREIGN KEY (USU_MODIFICACION) REFERENCES USUARIO (USU_ID);
ALTER TABLE PAR_NOMBRE_EXPEDIENTE ADD CONSTRAINT FK_PAR_NOM_EXP_USU_MOD FOREIGN KEY (USU_CREACION) REFERENCES USUARIO (USU_ID);

-- -----------------------------------------------------------------------------
-- TABLA: EXPEDIENTE_ESTADO
-- -----------------------------------------------------------------------------

CREATE SEQUENCE seq_EXPEDIENTE_ESTADO;
CREATE TABLE EXPEDIENTE_ESTADO (
  EXP_EST_ID number(10) NOT NULL, 
  EST_NOMBRE varchar2(500) NOT NULL, 
  PRIMARY KEY (EXP_EST_ID), 
  CONSTRAINT EXP_ESTADO_UNI_1 
    UNIQUE (EST_NOMBRE));
COMMENT ON COLUMN EXPEDIENTE_ESTADO.EXP_EST_ID IS 'Identificador unico de la tabla';
COMMENT ON COLUMN EXPEDIENTE_ESTADO.EST_NOMBRE IS 'Nombre del estado en el que se puede encontrar un expediente';

-- -----------------------------------------------------------------------------
-- TABLA: EXPEDIENTE
-- -----------------------------------------------------------------------------

CREATE SEQUENCE seq_EXPEDIENTE;
CREATE TABLE EXPEDIENTE (
  EXP_ID               number(10) NOT NULL, 
  DEP_ID               number(10) NOT NULL, 
  EXP_TIPO             number(1) NOT NULL, 
  TRD_ID_PRINCIPAL     number(10) NOT NULL, 
  EXP_NOMBRE           varchar2(255) NOT NULL, 
  EXP_DESCRIPCION      varchar2(255) NOT NULL, 
  FEC_CREACION         timestamp(0) NOT NULL, 
  USU_CREACION         number(10) NOT NULL, 
  FEC_MODIFICACION     timestamp(0), 
  USU_MODIFICACION     number(10), 
  USUARIO_ASIGNADO     number(1) DEFAULT 0 NOT NULL, 
  ESTADO_CAMBIO        number(1) DEFAULT 0 NOT NULL, 
  IND_APROBADO_INICIAL number(1) DEFAULT 0 NOT NULL, 
  IND_CERRADO          number(1) DEFAULT 0 NOT NULL, 
  PRIMARY KEY (EXP_ID));
COMMENT ON COLUMN EXPEDIENTE.EXP_ID IS 'Identificador unico de la tabla';
COMMENT ON COLUMN EXPEDIENTE.DEP_ID IS 'Identificacion de la dependencia del usuario quien creo el expediente';
COMMENT ON COLUMN EXPEDIENTE.EXP_TIPO IS 'Identificacion del tipo de expediente:

1 Expediente Simple
2 Expediente Complejo';
COMMENT ON COLUMN EXPEDIENTE.TRD_ID_PRINCIPAL IS 'Identificador de la tabla de retencion documental principal';
COMMENT ON COLUMN EXPEDIENTE.EXP_NOMBRE IS 'Nombre del expediente';
COMMENT ON COLUMN EXPEDIENTE.EXP_DESCRIPCION IS 'Descripción del expediente';
COMMENT ON COLUMN EXPEDIENTE.FEC_CREACION IS 'Fecha de creación del expediente';
COMMENT ON COLUMN EXPEDIENTE.USU_CREACION IS 'Identificador del usuario quien crea el expediente';
COMMENT ON COLUMN EXPEDIENTE.FEC_MODIFICACION IS 'Fecha de modificación del expediente';
COMMENT ON COLUMN EXPEDIENTE.USU_MODIFICACION IS 'Identificador del usuario quien modifica el expediente';
COMMENT ON COLUMN EXPEDIENTE.USUARIO_ASIGNADO IS 'Verifica el encargado de gestionar el expediente:

0. Usuario creador
1. Jefe de la dependencia';
COMMENT ON COLUMN EXPEDIENTE.ESTADO_CAMBIO IS 'Verifica el estado de los cambios en el expediente:

0. No aprobado
1. Aprobado';
COMMENT ON COLUMN EXPEDIENTE.IND_APROBADO_INICIAL IS 'Verifica si el expediente fue aprobado por el jefe de la dependencia inicialmente:

0. No
1. Si';
COMMENT ON COLUMN EXPEDIENTE.IND_CERRADO IS 'Verifica si el expediente se encuentra cerrado:

0. No
1. Si';
ALTER TABLE EXPEDIENTE ADD CONSTRAINT FK_EXP_DEPENDENCIA FOREIGN KEY (DEP_ID) REFERENCES DEPENDENCIA (DEP_ID);
ALTER TABLE EXPEDIENTE ADD CONSTRAINT FK_EXP_TRD FOREIGN KEY (TRD_ID_PRINCIPAL) REFERENCES TRD (TRD_ID);
ALTER TABLE EXPEDIENTE ADD CONSTRAINT FK_EXP_USU_CREADOR FOREIGN KEY (USU_CREACION) REFERENCES USUARIO (USU_ID);
ALTER TABLE EXPEDIENTE ADD CONSTRAINT FK_EXP_USU_MOD FOREIGN KEY (USU_MODIFICACION) REFERENCES USUARIO (USU_ID);

-- -----------------------------------------------------------------------------
-- TABLA: EXPEDIENTE_TRANSICION
-- -----------------------------------------------------------------------------

CREATE SEQUENCE seq_EXPEDIENTE_TRANSICION;
CREATE TABLE EXPEDIENTE_TRANSICION (
  EXP_TRA_ID        number(10) NOT NULL, 
  USU_CREACION      number(10) NOT NULL, 
  EXP_EST_ID        number(10) NOT NULL, 
  FEC_CREACION      timestamp(0) NOT NULL, 
  EXP_ID            number(10) NOT NULL, 
  DOC_ID            varchar2(32),
  USU_MODIFICADO    number(10), 
  PRIMARY KEY (EXP_TRA_ID));
COMMENT ON COLUMN EXPEDIENTE_TRANSICION.EXP_TRA_ID IS 'Identificador unico de la tabla';
COMMENT ON COLUMN EXPEDIENTE_TRANSICION.USU_CREACION IS 'Identificador del usuario quien crea la transición';
COMMENT ON COLUMN EXPEDIENTE_TRANSICION.EXP_EST_ID IS 'Identificador del estado del expediente';
COMMENT ON COLUMN EXPEDIENTE_TRANSICION.FEC_CREACION IS 'Fecha de creación del registro';
COMMENT ON COLUMN EXPEDIENTE_TRANSICION.EXP_ID IS 'Identificador del expediente asociado a la transición.';
COMMENT ON COLUMN EXPEDIENTE_TRANSICION.DOC_ID IS 'Identificador del documento, asociado a la transición del expediente.';
COMMENT ON COLUMN EXPEDIENTE_TRANSICION.USU_MODIFICADO IS 'Identificador del usuario que se modifica en el expediente.';
ALTER TABLE EXPEDIENTE_TRANSICION ADD CONSTRAINT FK_TRA_DOCUMENTO FOREIGN KEY (DOC_ID) REFERENCES DOCUMENTO (DOC_ID);
ALTER TABLE EXPEDIENTE_TRANSICION ADD CONSTRAINT FK_TRA_ESTADO FOREIGN KEY (EXP_EST_ID) REFERENCES EXPEDIENTE_ESTADO (EXP_EST_ID);
ALTER TABLE EXPEDIENTE_TRANSICION ADD CONSTRAINT FK_TRA_EXPEDIENTE FOREIGN KEY (EXP_ID) REFERENCES EXPEDIENTE (EXP_ID);
ALTER TABLE EXPEDIENTE_TRANSICION ADD CONSTRAINT FK_TRA_USUARIO FOREIGN KEY (USU_CREACION) REFERENCES USUARIO (USU_ID);
ALTER TABLE EXPEDIENTE_TRANSICION ADD CONSTRAINT FK_TRA_USUARIO_MOD FOREIGN KEY (USU_MODIFICADO) REFERENCES USUARIO (USU_ID);

-- -----------------------------------------------------------------------------
-- TABLA: EXP_OBSERVACION
-- -----------------------------------------------------------------------------

CREATE SEQUENCE seq_EXP_OBSERVACION;
CREATE TABLE EXP_OBSERVACION (
  EXP_OBS_ID      number(10) NOT NULL, 
  FEC_CREACION    timestamp(0) NOT NULL, 
  USU_ID          number(10) NOT NULL, 
  EXP_ID          number(10) NOT NULL, 
  EXP_OBSERVACION varchar2(255) NOT NULL, 
  PRIMARY KEY (EXP_OBS_ID));
COMMENT ON COLUMN EXP_OBSERVACION.EXP_OBS_ID IS 'Identificador unico de la tabla';
COMMENT ON COLUMN EXP_OBSERVACION.FEC_CREACION IS 'Fecha de creación del registro.';
COMMENT ON COLUMN EXP_OBSERVACION.USU_ID IS 'Identificador del usuario quien crea la observación.';
COMMENT ON COLUMN EXP_OBSERVACION.EXP_ID IS 'Identificador del expediente';
COMMENT ON COLUMN EXP_OBSERVACION.EXP_OBSERVACION IS 'Descripción de la observación.';
ALTER TABLE EXP_OBSERVACION ADD CONSTRAINT FK_EXPO_EXP FOREIGN KEY (EXP_ID) REFERENCES EXPEDIENTE (EXP_ID);
ALTER TABLE EXP_OBSERVACION ADD CONSTRAINT FK_EXPO_USUARIO FOREIGN KEY (USU_ID) REFERENCES USUARIO (USU_ID);

-- -----------------------------------------------------------------------------
-- TABLA: EXP_TRD
-- -----------------------------------------------------------------------------

CREATE SEQUENCE seq_EXP_TRD;
CREATE TABLE EXP_TRD (
  EXP_TRD_ID       number(10) NOT NULL, 
  EXP_ID           number(10) NOT NULL, 
  TRD_ID           number(10) NOT NULL, 
  IND_APROBADO     number(1) DEFAULT 0 NOT NULL, 
  ACTIVO           number(1) DEFAULT 0 NOT NULL, 
  FEC_CREACION     timestamp(0) NOT NULL, 
  USU_CREACION     number(10) NOT NULL, 
  FEC_MODIFICACION timestamp(0), 
  USU_MODIFICACION number(10), 
  PRIMARY KEY (EXP_TRD_ID), 
  CONSTRAINT EXPTRD_UNI_1 
    UNIQUE (EXP_ID, TRD_ID, ACTIVO));
COMMENT ON COLUMN EXP_TRD.EXP_TRD_ID IS 'Identificador unico de la tabla';
COMMENT ON COLUMN EXP_TRD.EXP_ID IS 'Identificador del expediente';
COMMENT ON COLUMN EXP_TRD.TRD_ID IS 'Identificador de la tabla de retencion documental';
COMMENT ON COLUMN EXP_TRD.IND_APROBADO IS 'Estado de la trd:

0. No aprobada
1. Aprobada';
COMMENT ON COLUMN EXP_TRD.ACTIVO IS 'Identifica si el registro se encuentra:

0. Eliminado
1. Activo';
COMMENT ON COLUMN EXP_TRD.FEC_CREACION IS 'Fecha de creacion del registro';
COMMENT ON COLUMN EXP_TRD.USU_CREACION IS 'Identificador del usuario quen crea el registro.';
COMMENT ON COLUMN EXP_TRD.FEC_MODIFICACION IS 'Fecha de modificación del registro.';
COMMENT ON COLUMN EXP_TRD.USU_MODIFICACION IS 'Identificador del usuario quien modifica el registro.';
ALTER TABLE EXP_TRD ADD CONSTRAINT FK_EXPTRD_EXP FOREIGN KEY (EXP_ID) REFERENCES EXPEDIENTE (EXP_ID);
ALTER TABLE EXP_TRD ADD CONSTRAINT FK_EXPTRD_TRD FOREIGN KEY (TRD_ID) REFERENCES TRD (TRD_ID);
ALTER TABLE EXP_TRD ADD CONSTRAINT FK_EXPTRD_USUCREADOR FOREIGN KEY (USU_CREACION) REFERENCES USUARIO (USU_ID);
ALTER TABLE EXP_TRD ADD CONSTRAINT FK_EXPTRD_USUMOD FOREIGN KEY (USU_MODIFICACION) REFERENCES USUARIO (USU_ID);

-- -----------------------------------------------------------------------------
-- TABLA: EXP_USUARIO
-- -----------------------------------------------------------------------------

CREATE SEQUENCE seq_EXP_USUARIO;
CREATE TABLE EXP_USUARIO (
  EXP_USU_ID       number(10) NOT NULL, 
  EXP_ID           number(10) NOT NULL, 
  USU_ID           number(10) NOT NULL, 
  CAR_ID           number(10) NOT NULL, 
  PERMISO          number(1) DEFAULT 1 NOT NULL, 
  IND_APROBADO     number(1) NOT NULL, 
  ACTIVO           number(1) DEFAULT 1 NOT NULL, 
  FEC_CREACION     timestamp(0) NOT NULL, 
  USU_CREACION     number(10) NOT NULL, 
  FEC_MODIFICACION timestamp(0), 
  USU_MODIFICACION number(10), 
  PRIMARY KEY (EXP_USU_ID), 
  CONSTRAINT EXP_UNI_USU
    UNIQUE (EXP_ID, USU_ID));
COMMENT ON COLUMN EXP_USUARIO.EXP_USU_ID IS 'Identificador unico de la tabla';
COMMENT ON COLUMN EXP_USUARIO.EXP_ID IS 'Identificador del expediente';
COMMENT ON COLUMN EXP_USUARIO.USU_ID IS 'Identificador del usuario asignado al expediente';
COMMENT ON COLUMN EXP_USUARIO.CAR_ID IS 'Identificador del cargo del usuario';
COMMENT ON COLUMN EXP_USUARIO.PERMISO IS 'Identifica el permiso del usuario sobre el expediente:

1. Lectura
2. Indexación';
COMMENT ON COLUMN EXP_USUARIO.IND_APROBADO IS 'Indica el estado del usuario por parte del jefe de la dependencia:

0. No aprobado
1. Aprobado';
COMMENT ON COLUMN EXP_USUARIO.ACTIVO IS 'Identifica si el registro se encuentra:

1 Activo
0 Eliminado';
COMMENT ON COLUMN EXP_USUARIO.FEC_CREACION IS 'Fecha de creacion del registro';
COMMENT ON COLUMN EXP_USUARIO.USU_CREACION IS 'Identificador del usuario quen crea el registro.';
COMMENT ON COLUMN EXP_USUARIO.FEC_MODIFICACION IS 'Fecha de modificación del registro.';
COMMENT ON COLUMN EXP_USUARIO.USU_MODIFICACION IS 'Identificador del usuario quien modifica el registro.';
ALTER TABLE EXP_USUARIO ADD CONSTRAINT FK_EXPUSU_CARGO FOREIGN KEY (CAR_ID) REFERENCES CARGO (CAR_ID);
ALTER TABLE EXP_USUARIO ADD CONSTRAINT FK_EXPUSU_EXP FOREIGN KEY (EXP_ID) REFERENCES EXPEDIENTE (EXP_ID);
ALTER TABLE EXP_USUARIO ADD CONSTRAINT FK_EXPUSU_USUARIO FOREIGN KEY (USU_ID) REFERENCES USUARIO (USU_ID);
ALTER TABLE EXP_USUARIO ADD CONSTRAINT FK_EXPUSU_USUCREADOR FOREIGN KEY (USU_CREACION) REFERENCES USUARIO (USU_ID);
ALTER TABLE EXP_USUARIO ADD CONSTRAINT FK_EXPUSU_USUMOD FOREIGN KEY (USU_MODIFICACION) REFERENCES USUARIO (USU_ID);

-- -----------------------------------------------------------------------------
-- TABLA: EXP_DOCUMENTO
-- -----------------------------------------------------------------------------

CREATE SEQUENCE seq_EXP_DOCUMENTO;
CREATE TABLE EXP_DOCUMENTO (
  EXP_DOC_ID       number(10) NOT NULL, 
  EXP_ID           number(10) NOT NULL, 
  DOC_ID           varchar2(32) NOT NULL, 
  FEC_CREACION     timestamp(0) NOT NULL, 
  USU_CREACION     number(10) NOT NULL, 
  FEC_MODIFICACION timestamp(0), 
  USU_MODIFICACION number(10), 
  ACTIVO           number(1) DEFAULT 1 NOT NULL, 
  PRIMARY KEY (EXP_DOC_ID), 
  CONSTRAINT EXPDOC_UNI1
    UNIQUE (DOC_ID, ACTIVO));
COMMENT ON COLUMN EXP_DOCUMENTO.EXP_DOC_ID IS 'Identificador unico de la tabla';
COMMENT ON COLUMN EXP_DOCUMENTO.EXP_ID IS 'Identificador del expediente';
COMMENT ON COLUMN EXP_DOCUMENTO.DOC_ID IS 'Identificador del documento';
COMMENT ON COLUMN EXP_DOCUMENTO.FEC_CREACION IS 'Fecha de creacion del registro';
COMMENT ON COLUMN EXP_DOCUMENTO.USU_CREACION IS 'Identificador del usuario quen crea el registro.';
COMMENT ON COLUMN EXP_DOCUMENTO.FEC_MODIFICACION IS 'Fecha de modificación del registro.';
COMMENT ON COLUMN EXP_DOCUMENTO.USU_MODIFICACION IS 'Identificador del usuario quien modifica el registro.';
COMMENT ON COLUMN EXP_DOCUMENTO.ACTIVO IS 'Identifica si el registro se encuentra:

1 Activo
0 Eliminado';
ALTER TABLE EXP_DOCUMENTO ADD CONSTRAINT FK_EXPDOC_DOC FOREIGN KEY (DOC_ID) REFERENCES DOCUMENTO (DOC_ID);
ALTER TABLE EXP_DOCUMENTO ADD CONSTRAINT FK_EXPDOC_EXP FOREIGN KEY (EXP_ID) REFERENCES EXPEDIENTE (EXP_ID);
ALTER TABLE EXP_DOCUMENTO ADD CONSTRAINT FK_EXPDOC_USUCREACION FOREIGN KEY (USU_CREACION) REFERENCES USUARIO (USU_ID);
ALTER TABLE EXP_DOCUMENTO ADD CONSTRAINT FK_EXPDOC_USUMOD FOREIGN KEY (USU_MODIFICACION) REFERENCES USUARIO (USU_ID);

-- -----------------------------------------------------------------------------
-- TABLA: DOCUMENTO
-- -----------------------------------------------------------------------------

UPDATE DOCUMENTO
SET EXP_ID = NULL
WHERE EXP_ID IS NOT NULL;

ALTER TABLE DOCUMENTO ADD CONSTRAINT FK_DOC_EXPEDIENTE FOREIGN KEY (EXP_ID) REFERENCES EXPEDIENTE (EXP_ID);

-- -----------------------------------------------------------------------------
-- TABLA: ESTADOS DEL EXPEDIENTE
-- -----------------------------------------------------------------------------

INSERT INTO EXPEDIENTE_ESTADO (EXP_EST_ID, EST_NOMBRE) VALUES (1100, 'En construcción');
INSERT INTO EXPEDIENTE_ESTADO (EXP_EST_ID, EST_NOMBRE) VALUES (1101, 'Pendiente de aprobación');
INSERT INTO EXPEDIENTE_ESTADO (EXP_EST_ID, EST_NOMBRE) VALUES (1102, 'Aprobado');
INSERT INTO EXPEDIENTE_ESTADO (EXP_EST_ID, EST_NOMBRE) VALUES (1103, 'Rechazado');
INSERT INTO EXPEDIENTE_ESTADO (EXP_EST_ID, EST_NOMBRE) VALUES (1104, 'Documento agregado');
INSERT INTO EXPEDIENTE_ESTADO (EXP_EST_ID, EST_NOMBRE) VALUES (1105, 'Documento desvinculado');
INSERT INTO EXPEDIENTE_ESTADO (EXP_EST_ID, EST_NOMBRE) VALUES (1106, 'Expediente cerrado');
INSERT INTO EXPEDIENTE_ESTADO (EXP_EST_ID, EST_NOMBRE) VALUES (1107, 'Usuario Asignado');
INSERT INTO EXPEDIENTE_ESTADO (EXP_EST_ID, EST_NOMBRE) VALUES (1108, 'Usuario modificado');
INSERT INTO EXPEDIENTE_ESTADO (EXP_EST_ID, EST_NOMBRE) VALUES (1109, 'Usuario eliminado');
INSERT INTO EXPEDIENTE_ESTADO (EXP_EST_ID, EST_NOMBRE) VALUES (1110, 'Expediente Reabierto');
INSERT INTO EXPEDIENTE_ESTADO (EXP_EST_ID, EST_NOMBRE) VALUES (1111, 'Usuario Administrador Modificado');
INSERT INTO EXPEDIENTE_ESTADO (EXP_EST_ID, EST_NOMBRE) VALUES (1112, 'Tipo de expediente modificado');

-- -----------------------------------------------------------------------------
-- TABLA: ROL ANTERIOR
-- -----------------------------------------------------------------------------
DELETE FROM PERFIL_ROL WHERE ROL_ID = 'ADMIN_EXPEDIENTES';
DELETE FROM ROL WHERE ROL_ID = 'ADMIN_EXPEDIENTES';

INSERT INTO ROL 
    (ROL_ID, QUIEN, CUANDO, QUIEN_MOD, CUANDO_MOD, ACTIVO, ROL_NOMBRE) 
VALUES 
    ('ADMIN_PAR_NOMBRE_EXPEDIENTE',3390, SYSDATE,3390,SYSDATE,1,'Parametrizar Nombre Expediente')
;