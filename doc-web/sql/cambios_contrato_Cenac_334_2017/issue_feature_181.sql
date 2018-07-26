/**
 * Author:  egonzalezm
 * Created: 24/07/2018
 */

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
  EST_NOMBRE varchar2(500), 
  PRIMARY KEY (EXP_EST_ID), 
  CONSTRAINT EXP_ESTADO_UNI_1 
    UNIQUE (EST_NOMBRE));
COMMENT ON COLUMN EXPEDIENTE_ESTADO.EXP_EST_ID IS 'Identificador unico de la tabla';
COMMENT ON COLUMN EXPEDIENTE_ESTADO.EST_NOMBRE IS 'Nombre del estado en el que se puede encontrar un expediente';

-- -----------------------------------------------------------------------------
-- TABLA: EXPEDIENTE_TRANSICION
-- -----------------------------------------------------------------------------

CREATE SEQUENCE seq_EXPEDIENTE_TRANSICION;
CREATE TABLE EXPEDIENTE_TRANSICION (
  EXP_TRA_ID   number(10) NOT NULL, 
  USU_CREACION number(10) NOT NULL, 
  EXP_EST_ID   number(10) NOT NULL, 
  FEC_CREACION timestamp(0), 
  EXP_ID       number(10) NOT NULL, 
  DOC_ID       number(10) NOT NULL, 
  PRIMARY KEY (EXP_TRA_ID));
COMMENT ON COLUMN EXPEDIENTE_TRANSICION.EXP_TRA_ID IS 'Identificador unico de la tabla';
COMMENT ON COLUMN EXPEDIENTE_TRANSICION.USU_CREACION IS 'Identificador del usuario quien crea la transición';
COMMENT ON COLUMN EXPEDIENTE_TRANSICION.EXP_EST_ID IS 'Identificador del estado del expediente';
COMMENT ON COLUMN EXPEDIENTE_TRANSICION.FEC_CREACION IS 'Fecha de creación del registro';
COMMENT ON COLUMN EXPEDIENTE_TRANSICION.EXP_ID IS 'Identificador del expediente asociado a la transición.';
COMMENT ON COLUMN EXPEDIENTE_TRANSICION.DOC_ID IS 'Identificador del documento, asociado a la transición del expediente.';
ALTER TABLE EXPEDIENTE_TRANSICION ADD CONSTRAINT FK_TRA_DOCUMENTO FOREIGN KEY (DOC_ID) REFERENCES DOCUMENTO (DOC_ID);
ALTER TABLE EXPEDIENTE_TRANSICION ADD CONSTRAINT FK_TRA_ESTADO FOREIGN KEY (EXP_EST_ID) REFERENCES EXPEDIENTE_ESTADO (EXP_EST_ID);
ALTER TABLE EXPEDIENTE_TRANSICION ADD CONSTRAINT FK_TRA_EXPEDIENTE FOREIGN KEY (EXP_ID) REFERENCES EXPEDIENTE (EXP_ID);
ALTER TABLE EXPEDIENTE_TRANSICION ADD CONSTRAINT FK_TRA_USUARIO FOREIGN KEY (USU_CREACION) REFERENCES USUARIO (USU_ID);

-- -----------------------------------------------------------------------------
-- TABLA: EXPEDIENTE_TRANSICION
-- -----------------------------------------------------------------------------

CREATE SEQUENCE seq_EXPEDIENTE;
CREATE TABLE EXPEDIENTE (
  EXP_ID           number(10) NOT NULL, 
  DEP_ID           number(10) NOT NULL, 
  EXP_TIPO         number(10) NOT NULL, 
  TRD_ID_PRINCIPAL number(10) NOT NULL, 
  EXP_NOMBRE       varchar2(255) NOT NULL, 
  EXP_DESCRIPCION  varchar2(255) NOT NULL, 
  FEC_CREACION     timestamp(0) NOT NULL, 
  USU_CREACION     number(10) NOT NULL, 
  FEC_MODIFICACION timestamp(0), 
  USU_MODIFICACION number(10) NOT NULL, 
  IND_APROBACION   number(10), 
  IND_APROBADO     number(10), 
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
ALTER TABLE EXPEDIENTE ADD CONSTRAINT FK_EXP_DEPENDENCIA FOREIGN KEY (DEP_ID) REFERENCES DEPENDENCIA (DEP_ID);
ALTER TABLE EXPEDIENTE ADD CONSTRAINT FK_EXP_TRD FOREIGN KEY (TRD_ID_PRINCIPAL) REFERENCES TRD (TRD_ID);
ALTER TABLE EXPEDIENTE ADD CONSTRAINT FK_EXP_USU_CREADOR FOREIGN KEY (USU_CREACION) REFERENCES USUARIO (USU_ID);
ALTER TABLE EXPEDIENTE ADD CONSTRAINT FK_EXP_USU_MOD FOREIGN KEY (USU_MODIFICACION) REFERENCES USUARIO (USU_ID);

-- -----------------------------------------------------------------------------
-- TABLA: EXPEDIENTE_TRANSICION
-- -----------------------------------------------------------------------------

CREATE SEQUENCE seq_EXPEDIENTE_TRANSICION;
CREATE TABLE EXPEDIENTE_TRANSICION (
  EXP_TRA_ID       number(10) NOT NULL, 
  EXP_EST_ID       number(10) NOT NULL, 
  USU_CREACION     number(10), 
  FEC_MODIFICACIÓN number(10), 
  EXP_ID           number(10) NOT NULL, 
  DOC_ID           number(10), 
  PRIMARY KEY (EXP_TRA_ID));
ALTER TABLE EXPEDIENTE_TRANSICION ADD CONSTRAINT FK_EXPT_EXP FOREIGN KEY (EXP_ID) REFERENCES EXPEDIENTE (EXP_ID);
ALTER TABLE EXPEDIENTE_TRANSICION ADD CONSTRAINT FK_EXPT_EXPE FOREIGN KEY (EXP_EST_ID) REFERENCES EXPEDIENTE_ESTADO (EXP_EST_ID);

-- -----------------------------------------------------------------------------
-- TABLA: EXP_OBSERVACION
-- -----------------------------------------------------------------------------

CREATE SEQUENCE seq_EXP_OBSERVACION;
CREATE TABLE EXP_OBSERVACION (
  EXP_OBS_ID   number(10) NOT NULL, 
  FEC_CREACION number(10), 
  USU_ID       number(10), 
  EXP_ID       number(10) NOT NULL, 
  TEXTO        varchar2(255) NOT NULL, 
  PRIMARY KEY (EXP_OBS_ID));
ALTER TABLE EXP_OBSERVACION ADD CONSTRAINT FKEXP_OBSERV208356 FOREIGN KEY (EXP_ID) REFERENCES EXPEDIENTE (EXP_ID);

-- -----------------------------------------------------------------------------
-- TABLA: EXP_DOCUMENTO
-- -----------------------------------------------------------------------------



