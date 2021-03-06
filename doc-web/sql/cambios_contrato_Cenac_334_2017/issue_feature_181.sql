--
-- Author:  edison.gonzalez@controltechcg.com
-- Created: 27/07/2018 Issue #181 (SICDI-Controltech) feature-181
--

-- -----------------------------------------------------------------------------
-- BORRADO
-- -----------------------------------------------------------------------------
SET DEFINE OFF
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

CREATE SEQUENCE seq_PAR_NOMBRE_EXPEDIENTE MINVALUE 0 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1;
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

CREATE SEQUENCE seq_EXPEDIENTE_ESTADO MINVALUE 0 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1;
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

CREATE SEQUENCE seq_EXPEDIENTE MINVALUE 0 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1;
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

CREATE SEQUENCE seq_EXPEDIENTE_TRANSICION MINVALUE 0 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1;
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

CREATE SEQUENCE seq_EXP_OBSERVACION MINVALUE 0 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1;
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

CREATE SEQUENCE seq_EXP_TRD MINVALUE 0 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1;
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

CREATE SEQUENCE seq_EXP_USUARIO MINVALUE 0 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1;
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
  PRIMARY KEY (EXP_USU_ID));
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

CREATE SEQUENCE seq_EXP_DOCUMENTO MINVALUE 0 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1;
CREATE TABLE EXP_DOCUMENTO (
  EXP_DOC_ID       number(10) NOT NULL, 
  EXP_ID           number(10) NOT NULL, 
  DOC_ID           varchar2(32) NOT NULL, 
  FEC_CREACION     timestamp(0) NOT NULL, 
  USU_CREACION     number(10) NOT NULL, 
  FEC_MODIFICACION timestamp(0), 
  USU_MODIFICACION number(10), 
  ACTIVO           number(1) DEFAULT 1 NOT NULL, 
  PRIMARY KEY (EXP_DOC_ID));
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

-- -----------------------------------------------------------------------------
-- TABLA: TIPO NOTIFICACIÓN
-- -----------------------------------------------------------------------------

INSERT INTO TIPO_NOTIFICACION (TNF_ID,NOMBRE,VALOR,ACTIVO,QUIEN,CUANDO,QUIEN_MOD,CUANDO_MOD) VALUES ('200','NOTIFICACIÓN EXPEDIENTE USUARIO ASIGNADO','-1','1','3390',sysdate,'3390',sysdate);
INSERT INTO TIPO_NOTIFICACION (TNF_ID,NOMBRE,VALOR,ACTIVO,QUIEN,CUANDO,QUIEN_MOD,CUANDO_MOD) VALUES ('201','NOTIFICACIÓN EXPEDIENTE USUARIO ELIMINADO','-1','1','3390',sysdate,'3390',sysdate);
INSERT INTO TIPO_NOTIFICACION (TNF_ID,NOMBRE,VALOR,ACTIVO,QUIEN,CUANDO,QUIEN_MOD,CUANDO_MOD) VALUES ('203','NOTIFICACIÓN EXPEDIENTE APROBADO O RECHAZADO','-1','1','3390',sysdate,'3390',sysdate);
INSERT INTO TIPO_NOTIFICACION (TNF_ID,NOMBRE,VALOR,ACTIVO,QUIEN,CUANDO,QUIEN_MOD,CUANDO_MOD) VALUES ('204','NOTIFICACIÓN EXPEDIENTE DOCUMENTO INDEXADO','-1','1','3390',sysdate,'3390',sysdate);
INSERT INTO TIPO_NOTIFICACION (TNF_ID,NOMBRE,VALOR,ACTIVO,QUIEN,CUANDO,QUIEN_MOD,CUANDO_MOD) VALUES ('205','NOTIFICACIÓN EXPEDIENTE CERRADO','-1','1','3390',sysdate,'3390',sysdate);
INSERT INTO TIPO_NOTIFICACION (TNF_ID,NOMBRE,VALOR,ACTIVO,QUIEN,CUANDO,QUIEN_MOD,CUANDO_MOD) VALUES ('206','NOTIFICACIÓN EXPEDIENTE RE-ABIERTO','-1','1','3390',sysdate,'3390',sysdate);


-- -----------------------------------------------------------------------------
-- TABLA: WILDCARD_NOTIFICACION
-- -----------------------------------------------------------------------------

INSERT INTO WILDCARD_NOTIFICACION (WNF_ID,NOMBRE,VALOR) VALUES ('200','Nombre del Expediente','expediente.expNombre');
INSERT INTO WILDCARD_NOTIFICACION (WNF_ID,NOMBRE,VALOR) VALUES ('201','Usuario Asignado o eliminado','expUsuario.usuId.nombre');
INSERT INTO WILDCARD_NOTIFICACION (WNF_ID,NOMBRE,VALOR) VALUES ('202','Grado Usuario Asignado o eliminado','expUsuario.usuId.usuGrado.nombre');
INSERT INTO WILDCARD_NOTIFICACION (WNF_ID,NOMBRE,VALOR) VALUES ('203','Cargo Usuario Asignado o eliminado','expUsuario.carId.carNombre');

INSERT INTO WILDCARD_NOTIFICACION (WNF_ID,NOMBRE,VALOR) VALUES ('204','Jefe expediente','jefe.nombre');
INSERT INTO WILDCARD_NOTIFICACION (WNF_ID,NOMBRE,VALOR) VALUES ('205','Jefe Grado expediente','jefe.usuGrado.nombre');
INSERT INTO WILDCARD_NOTIFICACION (WNF_ID,NOMBRE,VALOR) VALUES ('213','Jefe Grado sigla expediente','jefe.usuGrado.id');
INSERT INTO WILDCARD_NOTIFICACION (WNF_ID,NOMBRE,VALOR) VALUES ('214','Jefe Grado sigla expediente','jefe.usuCargoPrincipalId.carNombre');

INSERT INTO WILDCARD_NOTIFICACION (WNF_ID,NOMBRE,VALOR) VALUES ('206','Expediente Estado (Aprobado ó Rechazado)','estado');

INSERT INTO WILDCARD_NOTIFICACION (WNF_ID,NOMBRE,VALOR) VALUES ('207','Asunto Documento','documento.asunto');
INSERT INTO WILDCARD_NOTIFICACION (WNF_ID,NOMBRE,VALOR) VALUES ('208','Radicado Documento','documento.radicado');
INSERT INTO WILDCARD_NOTIFICACION (WNF_ID,NOMBRE,VALOR) VALUES ('209','Usuario Eleabora Documento','documento.elabora.nombre');

INSERT INTO WILDCARD_NOTIFICACION (WNF_ID,NOMBRE,VALOR) VALUES ('210','Usuario creador expediente','usuarioCreador.nombre');
INSERT INTO WILDCARD_NOTIFICACION (WNF_ID,NOMBRE,VALOR) VALUES ('211','Usuario creador Grado nombre Expediente','usuarioCreador.usuGrado.nombre');
INSERT INTO WILDCARD_NOTIFICACION (WNF_ID,NOMBRE,VALOR) VALUES ('212','Usuario creador Grado Sigla Expediente','usuarioCreador.usuGrado.id');
INSERT INTO WILDCARD_NOTIFICACION (WNF_ID,NOMBRE,VALOR) VALUES ('215','Usuario creador Cargo Principal','usuarioCreador.usuCargoPrincipalId.carNombre');


-- -----------------------------------------------------------------------------
-- TABLA: WILDCARD_TIPO_NOTIFICACION
-- -----------------------------------------------------------------------------

INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('200','200');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('200','201');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('200','202');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('200','203');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('200','204');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('200','205');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('200','210');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('200','211');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('200','212');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('200','213');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('200','214');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('200','215');

INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('201','200');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('201','201');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('201','202');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('201','203');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('201','204');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('201','205');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('201','210');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('201','211');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('201','212');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('201','213');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('201','214');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('201','215');


INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('203','200');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('203','201');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('203','202');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('203','203');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('203','204');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('203','205');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('203','206');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('203','210');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('203','211');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('203','212');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('203','213');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('203','214');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('203','215');


INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('204','200');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('204','201');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('204','202');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('204','203');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('204','204');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('204','205');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('204','207');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('204','208');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('204','209');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('204','210');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('204','211');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('204','212');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('204','213');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('204','214');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('204','215');


INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('205','200');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('205','201');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('205','202');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('205','203');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('205','204');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('205','205');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('205','210');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('205','211');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('205','212');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('205','213');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('205','214');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('205','215');


INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('206','200');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('206','201');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('206','202');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('206','203');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('206','204');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('206','205');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('206','210');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('206','211');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('206','212');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('206','213');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('206','214');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('206','215');


-- -----------------------------------------------------------------------------
-- DESACTIVACIÓN NOTIFICACIONES EXPEDIENTES
-- -----------------------------------------------------------------------------
declare
    cursor c_usuario is
    select *
    from Usuario
    where activo = 1;
    
    cursor c_tip_notificacion is    
    select *
    from TIPO_NOTIFICACION
    where valor > -2
    and activo = 1
    AND TNF_ID IN (200,201,202,203,204,205,206);
    aux_total  NUMBER := 0;
begin
    for auxc_usuario in c_usuario loop
        aux_total := aux_total + 1;
        for aux_c_tip_notificacion in c_tip_notificacion loop
            begin
                INSERT INTO NOTIFICACION_X_USUARIO VALUES(NOTIFICACION_X_USUARIO_SEQ.NEXTVAL, auxc_usuario.usu_id, aux_c_tip_notificacion.tnf_id);
            exception when others then
                DBMS_OUTPUT.PUT_LINE(' Error desactivando la notificación '||aux_c_tip_notificacion.nombre||'.'||'al usuario '||auxc_usuario.usu_grado||' '||auxc_usuario.usu_nombre);
            end;
        end loop;
        DBMS_OUTPUT.PUT_LINE('Desactivando notificaciones al usuario '||auxc_usuario.usu_grado||' '||auxc_usuario.usu_nombre);
    end loop;
    DBMS_OUTPUT.PUT_LINE('TOTAL DE USUARIOS: '||aux_total);
end;
/

-- -----------------------------------------------------------------------------
-- DATOS DE PLANTILLAS DE NUEVAS NOTIFICACIONES
-- -----------------------------------------------------------------------------
DECLARE
    CURSOR C_NOT IS
    select '200' TNF_ID,
           '3' CLA_ID,
           '<!-- NOTIFICACIÓN USUARIO ASIGNADO A EXPEDIENTE --> 
                        <br>
                                    <p style="margin-top: 20px;">Cordial Saludo,</p>
                    <p>Señor(a) ${(jefe.usuGrado.nombre)!""}<br>
                                    <b>${(jefe.nombre?upper_case)!""}</b><br>
                                    ${(jefe.usuCargoPrincipalId.carNombre?capitalize)!""}</p>
                                     <br>
    
                                        <p style="text-align: justify;">Le informamos que el (la) Señor(a) 
    ${(usuarioCreador.usuGrado.id)!""}. ${(usuarioCreador.nombre)!""} ha ingresado al expediente <b>${(expediente.expNombre)!""}</b> al (la) Señor (a)  ${(expUsuario.usuId.usuGrado.nombre)!""}  ${(expUsuario.usuId.nombre)!""}  ${(expUsuario.carId.carNombre)!""} como nuevo participante. 
                                        </p><br>
                                        Recuerde ingresar a <b>SICDI</b> para <b>Aprobar</b> o <b>Rechazar</b> la solicitud del expediente.
                                       
                                        <div>
                                        <p>En caso de haber recibido este mensaje por error, le solicitamos comunicarse con el equipo de soporte <b>SICDI</b> a la linea (057)350 653 14 74
                                        o al correo electronico soporte.sicdi@imi.mil.co</p>
                                        
                                       <p>La Información contenida en este correo es para uso exclusivo del destinatario y puede ser confidencial. En caso de recibir este correo por error, 
                                            por favor no imprima, copie, reenvíe o divulgue de manera total o parcial este mensaje. Borre este correo y todas las copias y avise al remitente. Gracias.</p>
                                        
                                        <p>Atentamente,</p>
                                        <br>Equipo de Soporte
                                        <p><b>Sistema Clasificado de Documentos de Inteligencia Militar</b></p>
                                    </td>
                                </tr>
                            </table>
                    </td>
                    <td><td>
                </tr>
                <!-- END MAIN CONTENT AREA -->
            </table>' CUERPO,
            'Nuevo documento' ASUNTO
    from dual
    union
    select '201' TNF_ID,
           '3' CLA_ID,
           '<!-- NOTIFICACIÓN USUARIO ELIMINADO DE EXPEDIENTE --> 
                                        <br>
                                    <p style="margin-top: 20px;">Cordial Saludo,</p>
                    <p>Señor(a) ${(jefe.usuGrado.nombre)!""}<br>
                                    <b>${(jefe.nombre?upper_case)!""}</b><br>
                                    ${(jefe.usuCargoPrincipalId.carNombre?capitalize)!""}</p>
                                     <br>
                                    <p style="text-align: justify;">Le informamos que el (la) Señor(a) ${(usuarioCreador.usuGrado.id)!""}. ${(usuarioCreador.nombre)!""}, ha eliminado del expediente <b>${(expediente.expNombre)!""}</b>,  al (la) Señor (a) ${(expUsuario.usuId.usuGrado.nombre)!""}. ${(expUsuario.usuId.nombre)!""}  ${(expUsuario.carId.carNombre)!""}.</p>
                                        Recuerde que es su deber ingresar a <b>SICDI</b> para continuar con el flujo del expediente.
                                        <br>
                                        <br>
                                        <div>
                                        <p>En caso de haber recibido este mensaje por error, le solicitamos comunicarse con el equipo de soporte <b>SICDI</b> a la linea (057)350 653 14 74
                                        o al correo electronico soporte.sicdi@imi.mil.co</p>
                                        
                                        <p>La Información contenida en este correo es para uso exclusivo del destinatario y puede ser confidencial. En caso de recibir este correo por error, 
                                            por favor no imprima, copie, reenvíe,  divulgue de manera total o parcial este mensaje. Borre este correo y todas las copias y avise al remitente. Gracias.</p>
                                        
                                        <p>Atentamente,</p>
                                        <br>Equipo de Soporte
                                        <p><b>Sistema Clasificado de Documentos de Inteligencia Militar</b></p>
                                    </td>
                                </tr>
                            </table>
                    </td>
                    <td><td>
                </tr>
                <!-- END MAIN CONTENT AREA -->
            </table>' CUERPO,
            'Notificación expediente usuario eliminado' ASUNTO
    from dual
    union
    select '203' TNF_ID,
           '3' CLA_ID,
           '<!-- NOTIFICACIÓN USUARIO ASIGNADO A EXPEDIENTE --> 
                                        <br>
                                    <p style="margin-top: 20px;">Cordial Saludo,</p>
                    <p>Señor(a) ${(expediente.usuCreacion.usuGrado.nombre)!""}<br>
                                    <b>${(expediente.usuCreacion.nombre?upper_case)!""}</b><br>
                                          ${(usuarioCreador.usuCargoPrincipalId.carNombre)!""}</p>
                                     <br>
    
    </p>
                                        <p style="text-align: justify;">Le informamos que su requerimiento de indexación de nuevo participante al expediente <b>${(expediente.expNombre)!""} </b>ha sido<b> ${(estado)!""} </b>. 
                                        </p>
                                        Recuerde que es su deber ingresar a <b>SICDI</b> para continuar con el flujo del expediente.
                                        <br>
                                         <br>
                                        <div>
                                        <p>En caso de haber recibido este mensaje por error, le solicitamos comunicarse con el equipo de soporte <b>SICDI</b> a la linea (057)350 653 14 74
                                        o al correo electronico soporte.sicdi@imi.mil.co</p>
                                        
                                        <p>La Información contenida en este correo es para uso exclusivo del destinatario y puede ser confidencial. En caso de recibir este correo por error, 
                                            por favor no imprima, copie, reenvíe, divulgue de manera total o parcial este mensaje. Borre este correo y todas las copias y avise al remitente. Gracias.</p>
                                        
                                        <p>Atentamente,</p>
                                        <br>Equipo de Soporte
                                        <p><b>Sistema Clasificado de Documentos de Inteligencia Militar</b></p>
                                    </td>
                                </tr>
                            </table>
                    </td>
                    <td><td>
                </tr>
                <!-- END MAIN CONTENT AREA -->
            </table>' CUERPO,
            'Notificación de estado expediente' ASUNTO
    from dual
    union
    select '204' TNF_ID,
           '3' CLA_ID,
           '<!-- NOTIFICACIÓN DOCUMENTO INDEXADO A EXPEDIENTE --> 
                                        <br>
                                        <p style="margin-top: 20px;">Cordial Saludo,</p>
                                        <p>Señor Usuario</p>
                                        <p style="text-align: justify;">Le informamos que el (la) Señor(a)<b></b> ha indexado el documento <b>${(documento.asunto)!""}</b> radicado No <b>${(documento.radicado)!""}</b>al  expediente <b>${(expediente.expNombre)!""}</b>.  
                                        </p>
                                        Recuerde que es su deber ingresar a <b>SICDI</b> para continuar con el flujo del expediente.
                                        <br>${(jefe.usuCargoPrincipalId.carNombre)!""}
                                         <br>${(usuarioCreador.usuCargoPrincipalId.carNombre)!""}
                                         <br>${(jefe.usuGrado.id)!""}
                                         <br>${(usuarioCreador.nombre)!""}
                                         <br>${(usuarioCreador.usuGrado.nombre)!""}
                                         <br>${(usuarioCreador.usuGrado.id)!""}
                                         <br>${(expediente.expNombre)!""}
                                         <br>${(expUsuario.usuId.nombre)!""}
                                          <br>${(expUsuario.usuId.usuGrado.nombre)!""}
                                         <br>${(expUsuario.carId.carNombre)!""}
                                         <br>${(jefe.nombre)!""}
                                         <br>${(jefe.usuGrado.nombre)!""}
                                         <br>${(documento.asunto)!""}
                                         <br>${(documento.radicado)!""}
                                         <br>${(documento.elabora.nombre)!""}
                                         <br>
                                         <br>
                                         <br>
    
    
                                        <div>
                                        <p>En caso de haber recibido este mensaje por error, le solicitamos comunicarse con el equipo de soporte <b>SICDI</b> a la linea (057)350 653 14 74
                                        o al correo electronico soporte.sicdi@imi.mil.co</p>
                                        
                                        <p>La Información contenida en este correo es para uso exclusivo del destinatario y puede ser confidencial. En caso de recibir este correo por error, 
                                            por favor no imprima, copie, reenvíe, divulgue de manera total o parcial este mensaje. Borre este correo y todas las copias y avise al remitente. Gracias.</p>
                                        
                                        <p>Atentamente,</p>
                                        <br>Equipo de Soporte
                                        <p><b>Sistema Clasificado de Documentos de Inteligencia Militar</b></p>
                                    </td>
                                </tr>
                            </table>
                    </td>
                    <td><td>
                </tr>
                <!-- END MAIN CONTENT AREA -->
            </table>' CUERPO,
            'Notificación documento indexado a un expediente' ASUNTO
    from dual
    union
    select '205' TNF_ID,
           '3' CLA_ID,
           '<!-- NOTIFICACIÓN USUARIO ASIGNADO A EXPEDIENTE --> 
                    <br>
                                    <p style="margin-top: 20px;">Cordial Saludo,</p>
                    <p>Señor(a) ${(expediente.usuCreacion.usuGrado.nombre)!""}<br>
                                    <b>${(expediente.usuCreacion.nombre?upper_case)!""}</b><br>
                                           ${(usuarioCreador.usuCargoPrincipalId.carNombre?capitalize)!""}</p>
                                     <br>
                                        <p style="text-align: justify;">Le informamos que el (la) Señor(a)
                                       ${(jefe.usuGrado.nombre)!""}. <b>${(jefe.nombre)!""}</b> ha cerrado el expediente 
                                       <b> ${(expediente.expNombre)!""}, </b> si requiere efectuar más acciones sobre el 
                                        expediente, este debe ser reabierto por el jefe de la dependencia. 
                                        </p>
                                         <br>
                                         <br>
                                        <div>
                                        <p>En caso de haber recibido este mensaje por error, le solicitamos comunicarse con el equipo de soporte <b>SICDI</b> a la linea (057)350 653 14 74
                                        o al correo electronico soporte.sicdi@imi.mil.co</p>
                                        
                                       <p>La Información contenida en este correo es para uso exclusivo del destinatario y puede ser confidencial. En caso de recibir este correo por error, 
                                            por favor no imprima, copie, reenvíe, divulgue de manera total o parcial este mensaje. Borre este correo y todas las copias y avise al remitente. Gracias.</p>
                                        
                                        <p>Atentamente,</p>
                                        <br>Equipo de Soporte
                                        <p><b>Sistema Clasificado de Documentos de Inteligencia Militar</b></p>
                                    </td>
                                </tr>
                            </table>
                    </td>
                    <td><td>
                </tr>
                <!-- END MAIN CONTENT AREA -->
            </table>' CUERPO,
            'Expediente Cerrado' ASUNTO
    from dual
    union
    select '206' TNF_ID,
           '3' CLA_ID,
           '<!-- NOTIFICACIÓN USUARIO ASIGNADO A EXPEDIENTE --> 
                    <br>
                                    <p style="margin-top: 20px;">Cordial Saludo,</p>
                    <p>Señor(a) ${(expediente.usuCreacion.usuGrado.nombre)!""}<br>
                                    <b>${(expediente.usuCreacion.nombre?upper_case)!""}</b><br>
                                     ${(usuarioCreador.usuCargoPrincipalId.carNombre?capitalize)!""}</p>
                                     <br>
                                        <p style="text-align: justify;">Le informamos que el (la) Señor(a) 
                                       ${(jefe.usuGrado.nombre)!""}. <b>${(jefe.nombre)!""}</b> ha reabierto el expediente 
                                       <b>${(expediente.expNombre)!""}. </b></p>
                                        <br>
                                         <br>
                                        <div>
                                        <p>En caso de haber recibido este mensaje por error, le solicitamos comunicarse con el equipo de soporte <b>SICDI</b> a la linea (057)350 653 14 74
                                        o al correo electronico soporte.sicdi@imi.mil.co</p>
                                        
                                        <p>La Información contenida en este correo es para uso exclusivo del destinatario y puede ser confidencial. En caso de recibir este correo por error, 
                                            por favor no imprima, copie, reenvíe, divulgue de manera total o parcial este mensaje. Borre este correo y todas las copias y avise al remitente. Gracias.</p>
                                        
                                        <p>Atentamente,</p>
                                        <br>Equipo de Soporte
                                        <p><b>Sistema Clasificado de Documentos de Inteligencia Militar</b></p>
                                    </td>
                                </tr>
                            </table>
                    </td>
                    <td><td>
                </tr>
                <!-- END MAIN CONTENT AREA -->
            </table>' CUERPO,
            'Expediente Reabierto' ASUNTO
    from dual;
    aux_count   NUMBER;
BEGIN
    FOR AUX_C_NOT IN C_NOT LOOP
        SELECT COUNT(1) INTO aux_count FROM NOTIFICACION WHERE TNF_ID = AUX_C_NOT.TNF_ID AND ACTIVO = 1;
        IF aux_count = 0 THEN
            Insert into NOTIFICACION (NTF_ID, TNF_ID, CLA_ID, CUERPO, ASUNTO, ACTIVO, QUIEN, CUANDO) VALUES
            (NOTIFICACION_SEQ.NEXTVAL, AUX_C_NOT.TNF_ID, AUX_C_NOT.CLA_ID, AUX_C_NOT.CUERPO, AUX_C_NOT.ASUNTO, 1, 3390, SYSDATE);
        END IF;
    END LOOP;
END;
/

Insert into PAR_NOMBRE_EXPEDIENTE (PAR_ID,PAR_NOMBRE,USU_CREACION,FEC_CREACION) values (seq_PAR_NOMBRE_EXPEDIENTE.NEXTVAL,'INVESTIGACIÓN DISCIPLINARIOS','3390',SYSDATE);
Insert into PAR_NOMBRE_EXPEDIENTE (PAR_ID,PAR_NOMBRE,USU_CREACION,FEC_CREACION) values (seq_PAR_NOMBRE_EXPEDIENTE.NEXTVAL,'PROCESO DISCIPLINARIOS','3390',SYSDATE);
Insert into PAR_NOMBRE_EXPEDIENTE (PAR_ID,PAR_NOMBRE,USU_CREACION,FEC_CREACION) values (seq_PAR_NOMBRE_EXPEDIENTE.NEXTVAL,'INDAGACIÓN DISCIPLINARIA','3390',SYSDATE);
Insert into PAR_NOMBRE_EXPEDIENTE (PAR_ID,PAR_NOMBRE,USU_CREACION,FEC_CREACION) values (seq_PAR_NOMBRE_EXPEDIENTE.NEXTVAL,'CONTRATOS','3390',SYSDATE);
Insert into PAR_NOMBRE_EXPEDIENTE (PAR_ID,PAR_NOMBRE,USU_CREACION,FEC_CREACION) values (seq_PAR_NOMBRE_EXPEDIENTE.NEXTVAL,'INVESTIGACIÓN ADMINISTRATIVA','3390',SYSDATE);

COMMIT;