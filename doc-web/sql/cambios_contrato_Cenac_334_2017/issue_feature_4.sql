--
-- Author:  edison.gonzalez@controltechcg.com
-- Created: 21/08/2018 Issue #4 (SICDI-Controltech) feature-4
--

-- -----------------------------------------------------------------------------
-- BORRADO
-- -----------------------------------------------------------------------------
DROP SEQUENCE DOC.SEQ_TRANS_JUS_DEFECTO;
DROP TABLE DOC.TRANS_JUSTIFICACION_DEFECTO CASCADE CONSTRAINTS;
DROP TABLE DOC.TRANSFERENCIA_ESTADO CASCADE CONSTRAINTS;
DROP SEQUENCE DOC.seq_TRANSFERENCIA_TRANSICION;
DROP TABLE DOC.TRANSFERENCIA_TRANSICION CASCADE CONSTRAINTS;
DROP SEQUENCE DOC.seq_TRANSFERENCIA_OBSERVACION;
DROP TABLE DOC.TRANSFERENCIA_OBSERVACION CASCADE CONSTRAINTS;
DROP SEQUENCE DOC.seq_TRANS_EXP_DETALLE;
DROP TABLE DOC.TRANS_EXPEDIENTE_DETALLE CASCADE CONSTRAINTS;

-- -----------------------------------------------------------------------------
-- TRANSFERENCIA_ARCHIVO
-- -----------------------------------------------------------------------------

ALTER TABLE DOC.TRANSFERENCIA_ARCHIVO ADD JUSTIFICACION 	VARCHAR2(4000);
ALTER TABLE DOC.TRANSFERENCIA_ARCHIVO ADD USUARIO_ASIGNADO 	NUMBER(1);
ALTER TABLE DOC.TRANSFERENCIA_ARCHIVO ADD IND_APROBADO          NUMBER(1);
ALTER TABLE DOC.TRANSFERENCIA_ARCHIVO ADD DOC_ID 		VARCHAR2(32);
ALTER TABLE DOC.TRANSFERENCIA_ARCHIVO ADD FUID   		VARCHAR2(32);

ALTER TABLE DOC.TRANSFERENCIA_ARCHIVO MODIFY TIPO_TRANSFERENCIA NULL;
ALTER TABLE DOC.TRANSFERENCIA_ARCHIVO MODIFY NUM_DOCUMENTOS     NULL;
ALTER TABLE DOC.TRANSFERENCIA_ARCHIVO MODIFY USU_DESTINO_CARGO  NULL;

COMMENT ON COLUMN DOC.TRANSFERENCIA_ARCHIVO.JUSTIFICACION       IS 'Justificación de la transferencia.';
COMMENT ON COLUMN DOC.TRANSFERENCIA_ARCHIVO.USUARIO_ASIGNADO    IS 'Indica el usuario en el que se encuentra la transferencia  0: Usuario emisor; 1: Usuario receptor; 2: Usuario autorizador';
COMMENT ON COLUMN DOC.TRANSFERENCIA_ARCHIVO.IND_APROBADO        IS 'Verifica si la transferencia se encuentra aprobada o no: 0: No aprobada; 1: Aprobada';
COMMENT ON COLUMN DOC.TRANSFERENCIA_ARCHIVO.DOC_ID              IS 'Identificador del documento del acta asociada a la transferencia.';
COMMENT ON COLUMN DOC.TRANSFERENCIA_ARCHIVO.FUID                IS 'Identificador del documento del Fuid.';

UPDATE DOC.TRANSFERENCIA_ARCHIVO SET JUSTIFICACION = 'Sin Justificación';
UPDATE DOC.TRANSFERENCIA_ARCHIVO SET USUARIO_ASIGNADO = 2;
UPDATE DOC.TRANSFERENCIA_ARCHIVO SET IND_APROBADO = 1;
UPDATE DOC.TRANSFERENCIA_ARCHIVO SET ACTIVO = 0;

ALTER TABLE DOC.TRANSFERENCIA_ARCHIVO MODIFY JUSTIFICACION 	NOT NULL;
ALTER TABLE DOC.TRANSFERENCIA_ARCHIVO MODIFY USUARIO_ASIGNADO   DEFAULT 0 NOT NULL;
ALTER TABLE DOC.TRANSFERENCIA_ARCHIVO MODIFY IND_APROBADO       DEFAULT 0 NOT NULL;

ALTER TABLE DOC.TRANSFERENCIA_ARCHIVO ADD CONSTRAINT TAR_DOC_ID_ACTA_FK FOREIGN KEY (DOC_ID) REFERENCES DOC.DOCUMENTO (DOC_ID);

CREATE INDEX "DOC"."TRANSFERENCIA_ARCHIVO_ACTA" ON "DOC"."TRANSFERENCIA_ARCHIVO" ("DOC_ID");
CREATE INDEX "DOC"."TRANSFERENCIA_ARCHIVO_FUID" ON "DOC"."TRANSFERENCIA_ARCHIVO" ("FUID");

-- -----------------------------------------------------------------------------
-- TRANSFERENCIA_ARCHIVO_DETALLE
-- -----------------------------------------------------------------------------

ALTER TABLE DOC.TRANSFERENCIA_ARCHIVO_DETALLE ADD IND_REALIZADO     NUMBER(1);
ALTER TABLE DOC.TRANSFERENCIA_ARCHIVO_DETALLE ADD ACTIVO            NUMBER(1);

UPDATE DOC.TRANSFERENCIA_ARCHIVO_DETALLE SET IND_REALIZADO = 1;
UPDATE DOC.TRANSFERENCIA_ARCHIVO_DETALLE SET ACTIVO = 0;

ALTER TABLE DOC.TRANSFERENCIA_ARCHIVO_DETALLE MODIFY IND_REALIZADO  DEFAULT 0 NOT NULL;
ALTER TABLE DOC.TRANSFERENCIA_ARCHIVO_DETALLE MODIFY ACTIVO         DEFAULT 1 NOT NULL;

COMMENT ON COLUMN DOC.TRANSFERENCIA_ARCHIVO_DETALLE.IND_REALIZADO	IS 'Indica si el documento se transfirio: 1=Si; 0=No;';
COMMENT ON COLUMN DOC.TRANSFERENCIA_ARCHIVO_DETALLE.ACTIVO       	IS 'Indica si el documento se encuentra disponible para la transferencia: 1=Si; 0=No;';

ALTER TABLE DOC.TRANSFERENCIA_ARCHIVO_DETALLE ADD CONSTRAINT TAD_DOC_ID_FK FOREIGN KEY (DOC_ID) REFERENCES DOC.DOCUMENTO (DOC_ID);

-- -----------------------------------------------------------------------------
-- TRANS_JUSTIFICACION_DEFECTO
-- -----------------------------------------------------------------------------

CREATE SEQUENCE DOC.SEQ_TRANS_JUS_DEFECTO;
CREATE TABLE DOC.TRANS_JUSTIFICACION_DEFECTO (
  TJD_ID            number(10) NOT NULL, 
  TEXTO_OBSERVACION varchar2(1000) NOT NULL, 
  ACTIVO            number(1) NOT NULL, 
  QUIEN             number(38) NOT NULL, 
  CUANDO            timestamp NOT NULL, 
  QUIEN_MOD         number(38), 
  CUANDO_MOD        timestamp, 
  PRIMARY KEY (TJD_ID));
  
ALTER TABLE DOC.TRANS_JUSTIFICACION_DEFECTO ADD CONSTRAINT FK_TJD_USUARIO_CREADOR FOREIGN KEY (QUIEN) REFERENCES DOC.USUARIO (USU_ID);
ALTER TABLE DOC.TRANS_JUSTIFICACION_DEFECTO ADD CONSTRAINT FK_TJD_USUARIO_MODIFICA FOREIGN KEY (QUIEN_MOD) REFERENCES DOC.USUARIO (USU_ID);

COMMENT ON COLUMN DOC.TRANS_JUSTIFICACION_DEFECTO.TJD_ID 			IS 'Identificador de la tabla.';
COMMENT ON COLUMN DOC.TRANS_JUSTIFICACION_DEFECTO.TEXTO_OBSERVACION		IS 'Texto de la observación.';
COMMENT ON COLUMN DOC.TRANS_JUSTIFICACION_DEFECTO.ACTIVO 			IS 'Indicador de actividad (0=activo;1=inactivo).';
COMMENT ON COLUMN DOC.TRANS_JUSTIFICACION_DEFECTO.QUIEN                         IS 'ID del usuario creador de la observación.';
COMMENT ON COLUMN DOC.TRANS_JUSTIFICACION_DEFECTO.CUANDO			IS 'Fecha y hora de creación de la observación.';
COMMENT ON COLUMN DOC.TRANS_JUSTIFICACION_DEFECTO.QUIEN_MOD			IS 'ID del último usuario que modificó la observación.';
COMMENT ON COLUMN DOC.TRANS_JUSTIFICACION_DEFECTO.CUANDO_MOD                    IS 'Fecha y hora de la última modificación de la observación.';

-- -----------------------------------------------------------------------------
-- TRANSFERENCIA_ESTADO
-- -----------------------------------------------------------------------------

CREATE TABLE DOC.TRANSFERENCIA_ESTADO (
  TRA_EST_ID     number(10) NOT NULL, 
  TRA_EST_NOMBRE varchar2(255) NOT NULL, 
  PRIMARY KEY (TRA_EST_ID), 
  CONSTRAINT TRA_ESTADO_UNI_1 
    UNIQUE (TRA_EST_NOMBRE));
	
COMMENT ON COLUMN DOC.TRANSFERENCIA_ESTADO.TRA_EST_ID 		IS 'Identificador de la tabla.';
COMMENT ON COLUMN DOC.TRANSFERENCIA_ESTADO.TRA_EST_NOMBRE	IS 'Descripcion del estado de la transferencia.';

INSERT INTO DOC.TRANSFERENCIA_ESTADO(TRA_EST_ID, TRA_EST_NOMBRE) VALUES (10, 'En construcción');
INSERT INTO DOC.TRANSFERENCIA_ESTADO(TRA_EST_ID, TRA_EST_NOMBRE) VALUES (20, 'Pendiente de aceptación');
INSERT INTO DOC.TRANSFERENCIA_ESTADO(TRA_EST_ID, TRA_EST_NOMBRE) VALUES (30, 'Pendiente de autorizacion');
INSERT INTO DOC.TRANSFERENCIA_ESTADO(TRA_EST_ID, TRA_EST_NOMBRE) VALUES (40, 'Aprobado');
INSERT INTO DOC.TRANSFERENCIA_ESTADO(TRA_EST_ID, TRA_EST_NOMBRE) VALUES (50, 'Rechazado');
INSERT INTO DOC.TRANSFERENCIA_ESTADO(TRA_EST_ID, TRA_EST_NOMBRE) VALUES (60, 'Transferido');
INSERT INTO DOC.TRANSFERENCIA_ESTADO(TRA_EST_ID, TRA_EST_NOMBRE) VALUES (70, 'Anulado');

-- -----------------------------------------------------------------------------
-- TRANSFERENCIA_TRANSICION
-- -----------------------------------------------------------------------------

CREATE SEQUENCE DOC.seq_TRANSFERENCIA_TRANSICION;
CREATE TABLE DOC.TRANSFERENCIA_TRANSICION (
  TTRA_ID      number(10) NOT NULL, 
  USU_CREACION number(38) NOT NULL, 
  TRA_EST_ID   number(10) NOT NULL, 
  TAR_ID       number(38) NOT NULL, 
  FEC_CREACION timestamp  NOT NULL, 
  PRIMARY KEY (TTRA_ID));

COMMENT ON COLUMN DOC.TRANSFERENCIA_TRANSICION.TTRA_ID 		IS 'Identificador de la tabla.';
COMMENT ON COLUMN DOC.TRANSFERENCIA_TRANSICION.USU_CREACION		IS 'Identificador del usuario quien crea la transición.';
COMMENT ON COLUMN DOC.TRANSFERENCIA_TRANSICION.TRA_EST_ID		IS 'Identificador del estado de la transición.';
COMMENT ON COLUMN DOC.TRANSFERENCIA_TRANSICION.TAR_ID		IS 'Identificador de la transferencia de archivo.';
COMMENT ON COLUMN DOC.TRANSFERENCIA_TRANSICION.FEC_CREACION		IS 'Fecha de creación del registro.';

ALTER TABLE DOC.TRANSFERENCIA_TRANSICION ADD CONSTRAINT FK_TRANS_TRANSFERENCIA FOREIGN KEY (TAR_ID) REFERENCES DOC.TRANSFERENCIA_ARCHIVO (TAR_ID);
ALTER TABLE DOC.TRANSFERENCIA_TRANSICION ADD CONSTRAINT FK_TRANSICION_ESTADO FOREIGN KEY (TRA_EST_ID) REFERENCES DOC.TRANSFERENCIA_ESTADO (TRA_EST_ID);
ALTER TABLE DOC.TRANSFERENCIA_TRANSICION ADD CONSTRAINT FK_TRANSICION_USUARIO FOREIGN KEY (USU_CREACION) REFERENCES DOC.USUARIO (USU_ID);

-- -----------------------------------------------------------------------------
-- TRANSFERENCIA_OBSERVACION
-- -----------------------------------------------------------------------------

CREATE SEQUENCE DOC.seq_TRANSFERENCIA_OBSERVACION;
CREATE TABLE DOC.TRANSFERENCIA_OBSERVACION (
  TRA_OBS_ID      number(10) NOT NULL, 
  FEC_CREACION    timestamp  NOT NULL, 
  USU_ID          number(38) NOT NULL, 
  TAR_ID          number(38) NOT NULL, 
  TRA_OBSERVACION varchar2(500), 
  PRIMARY KEY (TRA_OBS_ID));

COMMENT ON COLUMN DOC.TRANSFERENCIA_OBSERVACION.TRA_OBS_ID 		IS 'Identificador de la tabla.';
COMMENT ON COLUMN DOC.TRANSFERENCIA_OBSERVACION.FEC_CREACION	IS 'Fecha de creación del registro.';
COMMENT ON COLUMN DOC.TRANSFERENCIA_OBSERVACION.USU_ID		IS 'Identificador del usuario quien crea la observación.';
COMMENT ON COLUMN DOC.TRANSFERENCIA_OBSERVACION.TAR_ID		IS 'Identificador de la transferencia de archivo.';
COMMENT ON COLUMN DOC.TRANSFERENCIA_OBSERVACION.TRA_OBSERVACION	IS 'Descripción de la observación';

ALTER TABLE DOC.TRANSFERENCIA_OBSERVACION ADD CONSTRAINT FK_OBS_TRANSFERENCIA FOREIGN KEY (TAR_ID) REFERENCES DOC.TRANSFERENCIA_ARCHIVO (TAR_ID);
ALTER TABLE DOC.TRANSFERENCIA_OBSERVACION ADD CONSTRAINT FK_OBS_USUARIO FOREIGN KEY (USU_ID) REFERENCES DOC.USUARIO (USU_ID);

-- -----------------------------------------------------------------------------
-- TRANS_EXPEDIENTE_DETALLE
-- -----------------------------------------------------------------------------

CREATE SEQUENCE DOC.seq_TRANS_EXP_DETALLE;
CREATE TABLE DOC.TRANS_EXPEDIENTE_DETALLE (
  TRA_EXP_ID        number(10) NOT NULL, 
  TAR_ID            number(38) NOT NULL, 
  EXP_ID            number(10) NOT NULL, 
  ANTERIOR_QUIEN    number(38) NOT NULL, 
  ANTERIOR_DEP_ID   number(38) NOT NULL, 
  NUEVO_DEP_ID      number(38) NOT NULL, 
  NUEVO_QUIEN       number(38) NOT NULL, 
  FEC_TRANSFERENCIA timestamp, 
  IND_REALIZADO     number(1) DEFAULT 0 NOT NULL, 
  ACTIVO            number(1) DEFAULT 1 NOT NULL,
  PRIMARY KEY (TRA_EXP_ID));

COMMENT ON COLUMN DOC.TRANS_EXPEDIENTE_DETALLE.TRA_EXP_ID 		IS 'Identificador de la tabla.';
COMMENT ON COLUMN DOC.TRANS_EXPEDIENTE_DETALLE.TAR_ID               IS 'Identificador de la transferencia de archivo.';
COMMENT ON COLUMN DOC.TRANS_EXPEDIENTE_DETALLE.EXP_ID		IS 'Identificador del expediente.';
COMMENT ON COLUMN DOC.TRANS_EXPEDIENTE_DETALLE.ANTERIOR_QUIEN	IS 'Identificador del usuario administrador anterior del expediente';
COMMENT ON COLUMN DOC.TRANS_EXPEDIENTE_DETALLE.ANTERIOR_DEP_ID	IS 'Identificador de la dependencia del usuario administrador anterior del expediente';
COMMENT ON COLUMN DOC.TRANS_EXPEDIENTE_DETALLE.NUEVO_DEP_ID		IS 'Identificador de la dependencia del usuario administrador nuevo del expediente';
COMMENT ON COLUMN DOC.TRANS_EXPEDIENTE_DETALLE.NUEVO_QUIEN          IS 'Identificador del usuario administrador nuevo del expediente';
COMMENT ON COLUMN DOC.TRANS_EXPEDIENTE_DETALLE.FEC_TRANSFERENCIA	IS 'Fecha de creación del registro.';
COMMENT ON COLUMN DOC.TRANS_EXPEDIENTE_DETALLE.IND_REALIZADO	IS 'Indica si el expediente se transfirio: 1=Si; 0=No;';
COMMENT ON COLUMN DOC.TRANS_EXPEDIENTE_DETALLE.ACTIVO       	IS 'Indica si el expediente se encuentra disponible para la transferencia: 1=Si; 0=No;';

ALTER TABLE DOC.TRANS_EXPEDIENTE_DETALLE ADD CONSTRAINT FK_TED_DEP_ANTERIOR FOREIGN KEY (ANTERIOR_DEP_ID) REFERENCES DOC.DEPENDENCIA (DEP_ID);
ALTER TABLE DOC.TRANS_EXPEDIENTE_DETALLE ADD CONSTRAINT FK_TED_DEP_NUEVO FOREIGN KEY (NUEVO_DEP_ID) REFERENCES DOC.DEPENDENCIA (DEP_ID);
ALTER TABLE DOC.TRANS_EXPEDIENTE_DETALLE ADD CONSTRAINT FK_TED_EXPEDIENTE FOREIGN KEY (EXP_ID) REFERENCES DOC.EXPEDIENTE (EXP_ID);
ALTER TABLE DOC.TRANS_EXPEDIENTE_DETALLE ADD CONSTRAINT FK_TED_TAR FOREIGN KEY (TAR_ID) REFERENCES DOC.TRANSFERENCIA_ARCHIVO (TAR_ID);
ALTER TABLE DOC.TRANS_EXPEDIENTE_DETALLE ADD CONSTRAINT FK_TED_USU_ANTERIOR FOREIGN KEY (ANTERIOR_QUIEN) REFERENCES DOC.USUARIO (USU_ID);
ALTER TABLE DOC.TRANS_EXPEDIENTE_DETALLE ADD CONSTRAINT FK_TED_USU_NUEVO FOREIGN KEY (NUEVO_QUIEN) REFERENCES DOC.USUARIO (USU_ID);

-- -----------------------------------------------------------------------------
-- TABLA: ROL
-- -----------------------------------------------------------------------------

INSERT INTO DOC.ROL 
    (ROL_ID, QUIEN, CUANDO, QUIEN_MOD, CUANDO_MOD, ACTIVO, ROL_NOMBRE) 
VALUES 
    ('ADMIN_TRANS_JUSTIF_DEFECTO',3390, SYSDATE,3390,SYSDATE,1,'Observacion Transferencia de Archivo');

-- -----------------------------------------------------------------------------
-- TABLA: TIPO NOTIFICACIÓN
-- -----------------------------------------------------------------------------

INSERT INTO DOC.TIPO_NOTIFICACION (TNF_ID,NOMBRE,VALOR,ACTIVO,QUIEN,CUANDO,QUIEN_MOD,CUANDO_MOD) VALUES ('400','NOTIFICACIÓN NUEVA TRANSFERENCIA','-1','1','3390',sysdate,'3390',sysdate);
INSERT INTO DOC.TIPO_NOTIFICACION (TNF_ID,NOMBRE,VALOR,ACTIVO,QUIEN,CUANDO,QUIEN_MOD,CUANDO_MOD) VALUES ('401','NOTIFICACIÓN TRANSFERENCIA RECIBIDA','-1','1','3390',sysdate,'3390',sysdate);
INSERT INTO DOC.TIPO_NOTIFICACION (TNF_ID,NOMBRE,VALOR,ACTIVO,QUIEN,CUANDO,QUIEN_MOD,CUANDO_MOD) VALUES ('402','NOTIFICACIÓN TRANSFERENCIA RECHAZADA','-1','1','3390',sysdate,'3390',sysdate);
INSERT INTO DOC.TIPO_NOTIFICACION (TNF_ID,NOMBRE,VALOR,ACTIVO,QUIEN,CUANDO,QUIEN_MOD,CUANDO_MOD) VALUES ('403','NOTIFICACIÓN TRANSFERENCIA AUTORIZADA','-1','1','3390',sysdate,'3390',sysdate);


-- -----------------------------------------------------------------------------
-- TABLA: WILDCARD_NOTIFICACION
-- -----------------------------------------------------------------------------

INSERT INTO DOC.WILDCARD_NOTIFICACION (WNF_ID,NOMBRE,VALOR) VALUES ('450','Nombre usuario Emisor','usuOrigen.nombre');
INSERT INTO DOC.WILDCARD_NOTIFICACION (WNF_ID,NOMBRE,VALOR) VALUES ('451','Sigla Grado usuario Emisor','usuOrigen.usuGrado.id');
INSERT INTO DOC.WILDCARD_NOTIFICACION (WNF_ID,NOMBRE,VALOR) VALUES ('452','Cargo usuario Emisor','usuOrigenCargo.carNombre');
INSERT INTO DOC.WILDCARD_NOTIFICACION (WNF_ID,NOMBRE,VALOR) VALUES ('453','Nombre usuario Receptor','usuDestino.nombre');
INSERT INTO DOC.WILDCARD_NOTIFICACION (WNF_ID,NOMBRE,VALOR) VALUES ('454','Sigla Grado usuario Receptor','usuDestino.usuGrado.id');
INSERT INTO DOC.WILDCARD_NOTIFICACION (WNF_ID,NOMBRE,VALOR) VALUES ('455','Justificación transferencia','transferencia.justificacion');
INSERT INTO DOC.WILDCARD_NOTIFICACION (WNF_ID,NOMBRE,VALOR) VALUES ('456','Cargo usuario Emisor','usuDestinoCargo.carNombre');
INSERT INTO DOC.WILDCARD_NOTIFICACION (WNF_ID,NOMBRE,VALOR) VALUES ('457','Nombre jefe dependencia origen','jefeOrigen.nombre');
INSERT INTO DOC.WILDCARD_NOTIFICACION (WNF_ID,NOMBRE,VALOR) VALUES ('458','Sigla Grado jefe dependencia origen','jefeOrigen.usuGrado.id');
INSERT INTO DOC.WILDCARD_NOTIFICACION (WNF_ID,NOMBRE,VALOR) VALUES ('459','Motivo rechazo transferencia','motRechazo');
INSERT INTO DOC.WILDCARD_NOTIFICACION (WNF_ID,NOMBRE,VALOR) VALUES ('460','Nombre usuario correo destino','usuario.nombre');
INSERT INTO DOC.WILDCARD_NOTIFICACION (WNF_ID,NOMBRE,VALOR) VALUES ('461','Sigla Grado usuario correo destino','usuario.usuGrado.id');
INSERT INTO DOC.WILDCARD_NOTIFICACION (WNF_ID,NOMBRE,VALOR) VALUES ('462','Cargo usuario correo destino','usuario.usuCargoPrincipalId.carNombre');
INSERT INTO DOC.WILDCARD_NOTIFICACION (WNF_ID,NOMBRE,VALOR) VALUES ('463','Nombre usuario rechaza','usuRechaza.nombre');
INSERT INTO DOC.WILDCARD_NOTIFICACION (WNF_ID,NOMBRE,VALOR) VALUES ('464','Nombre usuario rechaza','usuRechaza.usuGrado.id');
INSERT INTO DOC.WILDCARD_NOTIFICACION (WNF_ID,NOMBRE,VALOR) VALUES ('465','Nombre usuario rechaza','usuRechaza.usuCargoPrincipalId.carNombre');

-- -----------------------------------------------------------------------------
-- TABLA: 
-- -----------------------------------------------------------------------------

INSERT INTO DOC.WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('400','450');
INSERT INTO DOC.WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('400','451');
INSERT INTO DOC.WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('400','452');
INSERT INTO DOC.WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('400','453');
INSERT INTO DOC.WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('400','455');
INSERT INTO DOC.WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('400','456');
INSERT INTO DOC.WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('400','457');
INSERT INTO DOC.WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('400','458');
INSERT INTO DOC.WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('400','460');
INSERT INTO DOC.WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('400','461');
INSERT INTO DOC.WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('400','462');

INSERT INTO DOC.WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('401','450');
INSERT INTO DOC.WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('401','451');
INSERT INTO DOC.WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('401','452');
INSERT INTO DOC.WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('401','453');
INSERT INTO DOC.WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('401','455');
INSERT INTO DOC.WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('401','456');
INSERT INTO DOC.WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('401','457');
INSERT INTO DOC.WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('401','458');
INSERT INTO DOC.WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('401','460');
INSERT INTO DOC.WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('401','461');
INSERT INTO DOC.WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('401','462');

INSERT INTO DOC.WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('402','450');
INSERT INTO DOC.WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('402','451');
INSERT INTO DOC.WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('402','452');
INSERT INTO DOC.WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('402','453');
INSERT INTO DOC.WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('402','455');
INSERT INTO DOC.WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('402','456');
INSERT INTO DOC.WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('402','457');
INSERT INTO DOC.WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('402','458');
INSERT INTO DOC.WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('402','459');
INSERT INTO DOC.WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('402','460');
INSERT INTO DOC.WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('402','461');
INSERT INTO DOC.WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('402','462');
INSERT INTO DOC.WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('402','463');
INSERT INTO DOC.WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('402','464');
INSERT INTO DOC.WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('402','465');


INSERT INTO DOC.WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('403','450');
INSERT INTO DOC.WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('403','451');
INSERT INTO DOC.WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('403','452');
INSERT INTO DOC.WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('403','453');
INSERT INTO DOC.WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('403','455');
INSERT INTO DOC.WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('403','456');
INSERT INTO DOC.WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('403','457');
INSERT INTO DOC.WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('403','458');
INSERT INTO DOC.WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('403','460');
INSERT INTO DOC.WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('403','461');
INSERT INTO DOC.WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('403','462');

-- -----------------------------------------------------------------------------
-- TABLA: DOCUMENTO
--        BLOQUE DE PL/SQL QUE PERMITE CENTRALIZAR LA FECHA DE RADICACIÓN DEL
--        DOCUMENTO, INDEPENDIENTEMENTE DEL PROCESO.
-- -----------------------------------------------------------------------------

ALTER TABLE DOC.DOCUMENTO ADD DOC_FEC_RADICADO TIMESTAMP;
ALTER TABLE DOC.DOCUMENTO MODIFY ACTA_DESCRIPCION VARCHAR2(4000);

COMMENT ON COLUMN DOC.DOCUMENTO.DOC_FEC_RADICADO    IS 'Permite identificar la fecha en que se realizó la radicación de un documento, independientemente del proceso.';

SET SERVEROUTPUT ON;
DECLARE
    CURSOR C_RADICADO IS
        select a.*, b.pro_id
        from DOC.documento a,
             DOC.proceso_instancia b
        where b.pin_id = a.pin_id
        and b.pro_id = 9
        and doc_asunto is not null
        and doc_fec_radicado is null;
    
    aux_count_radicacion    NUMBER := 0;
BEGIN
    FOR AUX_C_RADICADO IN C_RADICADO LOOP         
            aux_count_radicacion := aux_count_radicacion + 1;
            UPDATE DOC.DOCUMENTO SET doc_fec_radicado = AUX_C_RADICADO.CUANDO WHERE DOC_ID = AUX_C_RADICADO.DOC_ID;
    END LOOP;
    DBMS_OUTPUT.PUT_LINE('radicacion= '||aux_count_radicacion);
    commit;
END;
/

DECLARE
    CURSOR C_INT_EXTERNO IS
       select a.pin_id, a.doc_id, max(c.cuando) cuando_firma
        from DOC.documento a
        join DOC.proceso_instancia b on (b.pin_id = a.pin_id and b.pro_id = 8 and b.pes_id != 83)
        join DOC.DOCUMENTO_USU_FIRMA c on ( c.doc_id = a.doc_id and c.cuando is not null)
        where a.doc_asunto is not null
        and doc_fec_radicado is null
        group by a.pin_id, a.doc_id, a.cuando;

    aux_count_int_externo   NUMBER := 0;
BEGIN     
    FOR AUX_C_INT_EXTERNO IN C_INT_EXTERNO LOOP   
        aux_count_int_externo := aux_count_int_externo + 1;
        UPDATE DOC.DOCUMENTO SET doc_fec_radicado = AUX_C_INT_EXTERNO.cuando_firma WHERE DOC_ID = AUX_C_INT_EXTERNO.DOC_ID;
    END LOOP;
    DBMS_OUTPUT.PUT_LINE('interno= '||aux_count_int_externo);
    commit;
END;
/

DECLARE
    CURSOR C_INT_EXTERNO IS
       select a.pin_id, a.doc_id, max(c.cuando) cuando_firma
        from DOC.documento a
        join DOC.proceso_instancia b on (b.pin_id = a.pin_id and b.pro_id = 41 and b.pes_id != 83)
        join DOC.DOCUMENTO_USU_FIRMA c on ( c.doc_id = a.doc_id and c.cuando is not null)
        where a.doc_asunto is not null
        and doc_fec_radicado is null
        group by a.pin_id, a.doc_id, a.cuando;

    aux_count_int_externo   NUMBER := 0;
BEGIN     
    FOR AUX_C_INT_EXTERNO IN C_INT_EXTERNO LOOP   
        aux_count_int_externo := aux_count_int_externo + 1;
        UPDATE DOC.DOCUMENTO SET doc_fec_radicado = AUX_C_INT_EXTERNO.cuando_firma WHERE DOC_ID = AUX_C_INT_EXTERNO.DOC_ID;
    END LOOP;
    DBMS_OUTPUT.PUT_LINE('externo= '||aux_count_int_externo);
    commit;
END;
/

DECLARE
    CURSOR C_ACTAS IS
        select a.*, b.pro_id
        from DOC.documento a,
             DOC.proceso_instancia b
        where b.pin_id = a.pin_id
        and b.pro_id = 100
        and doc_asunto is not null
        AND ACTA_FECHA_ELABORACION IS NOT NULL
        and doc_fec_radicado is null;
    
    aux_count_acta          NUMBER := 0;
BEGIN
    FOR AUX_C_ACTAS IN C_ACTAS LOOP   
        aux_count_acta := aux_count_acta + 1;
        UPDATE DOC.DOCUMENTO SET doc_fec_radicado = AUX_C_ACTAS.ACTA_FECHA_ELABORACION WHERE DOC_ID = AUX_C_ACTAS.DOC_ID;
    END LOOP;
    DBMS_OUTPUT.PUT_LINE('actas = '||aux_count_acta);
    commit;
END;
/

-- -----------------------------------------------------------------------------
-- TABLA: PLANTILLA_FUID_GESTION
-- -----------------------------------------------------------------------------

CREATE TABLE DOC.PLANTILLA_FUID_GESTION (
    PFG_ID          NUMBER(*,0)         NOT NULL,
    ACTIVO          NUMBER(1,0)         NOT NULL,
    NOMBRE_ARCHIVO  VARCHAR2(512 CHAR)  NOT NULL,
    TAMANYO_ARCHIVO NUMBER(*,0)         NOT NULL,
    FIRMA_MD5       VARCHAR2(32 CHAR)   NOT NULL,
    CODIGO_OFS      VARCHAR2(32 CHAR)   NOT NULL,
    QUIEN           NUMBER(*,0)         NOT NULL,
    CUANDO          DATE                NOT NULL,
    PRIMARY KEY (PFG_ID)
);

ALTER TABLE DOC.PLANTILLA_FUID_GESTION ADD CONSTRAINT PFG_QUIEN_FK FOREIGN KEY (QUIEN) REFERENCES DOC.USUARIO (USU_ID);

CREATE SEQUENCE DOC.PLANTILLA_FUID_GESTION_SEQ;

-- -----------------------------------------------------------------------------
-- CAMBIOS A DATOS EXISTENTES
-- -----------------------------------------------------------------------------

INSERT INTO DOC.ROL (
    ROL_ID,
    QUIEN,
    CUANDO,
    QUIEN_MOD,
    CUANDO_MOD,
    ACTIVO,
    ROL_NOMBRE
) VALUES (
    'ADMIN_FUID_GESTION',
    3390,
    SYSDATE,
    3390,
    SYSDATE,
    1,
    'Plantilla Fuid de gestión'
);

COMMIT;


-- -----------------------------------------------------------------------------
-- AJUSTE A DATOS DOCUMENTO_DEPENDENCIA
-- -----------------------------------------------------------------------------
declare
    cursor c_repetidos is
        select doc_id, max(dcdp_id) aux_dcdp_id
        from documento_dependencia
        where activo = 1
        group by doc_id
        having count(1) > 1
        order by doc_id;
begin
    for aux_c_repetidos in c_repetidos loop
        update documento_dependencia set activo = 0 where doc_id = aux_c_repetidos.doc_id and dcdp_id != aux_c_repetidos.aux_dcdp_id;
    end loop;
    commit;
end;
/

declare
    cursor c_sincargo is
        select distinct a.quien, b.usu_cargo_principal_id
        from documento_dependencia a,
             usuario b
        where a.activo = 1
        and a.cargo_id is null
        and a.quien = b.usu_id
        and b.usu_cargo_principal_id is not null;
begin
    for aux_c_sincargo in c_sincargo loop
        update documento_dependencia set cargo_id = aux_c_sincargo.usu_cargo_principal_id where quien = aux_c_sincargo.quien;
    end loop;
    commit;
end;
/