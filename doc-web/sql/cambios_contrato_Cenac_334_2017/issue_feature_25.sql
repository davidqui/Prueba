-- 2018-10-17 dquijanor@imi.mil.co Issue_feature_25 (SICDI-GETDE).
-- 2018-10-17 aherreram@imi.mil.co Issue_feature_25 (SICDI-GETDE).

-- -----------------------------------------------------------------------------
-- TABLA: TEMA_CAPACITACION
-- -----------------------------------------------------------------------------
CREATE TABLE DOC.TEMA_CAPACITACION (
    ID              NUMBER(5)           NOT NULL,
    CLASIFICACION   NUMBER(5)           NULL,
    TEMA            VARCHAR2(255)       NOT NULL,
    ACTIVO          NUMBER(5)           NOT NULL,
    QUIEN           NUMBER(38)          NOT NULL,
    CUANDO          DATE                NOT NULL,
    QUIEN_MOD       NUMBER(38)          NULL,
    CUANDO_MOD      DATE                NULL,
    PRIMARY KEY (ID)
);
CREATE INDEX TEMA_CAPACITACION_ACTIVO_IDX ON DOC.TEMA_CAPACITACION(ACTIVO);
CREATE SEQUENCE DOC.SQ_TEMA_CAPACITACION;

ALTER TABLE DOC.TEMA_CAPACITACION ADD CONSTRAINT TEMA_CAPACITACION_QUIEN_FK FOREIGN KEY ("QUIEN")REFERENCES DOC.USUARIO ("USU_ID") ENABLE;
ALTER TABLE DOC.TEMA_CAPACITACION ADD CONSTRAINT TEMA_CAPACITACION_QUIEN_MOD_FK FOREIGN KEY ("QUIEN_MOD")REFERENCES DOC.USUARIO ("USU_ID") ENABLE;
ALTER TABLE DOC.TEMA_CAPACITACION ADD CONSTRAINT TEMA_CAPACITACION_CLASIFICACION_FK FOREIGN KEY ("CLASIFICACION")REFERENCES DOC.CLASIFICACION ("CLA_ID") ENABLE;

COMMENT ON TABLE DOC.TEMA_CAPACITACION IS 'Registro de las Temas de Capacitacion que se relacionan con el funcionamiento de SICDI.';
COMMENT ON COLUMN DOC.TEMA_CAPACITACION.CLASIFICACION IS 'Grado o Nivel de acceso a la informacion dentro del sistema';
COMMENT ON COLUMN DOC.TEMA_CAPACITACION.TEMA IS 'Tema Especifico del que trata la capacitacion';
COMMENT ON COLUMN DOC.TEMA_CAPACITACION.ACTIVO IS 'Estado (1=activo ; 0=inactivo).';
COMMENT ON COLUMN DOC.TEMA_CAPACITACION.QUIEN IS 'Id usuario que crea el registro.';
COMMENT ON COLUMN DOC.TEMA_CAPACITACION.CUANDO IS 'Fecha y Hora en la que se crea el registro.';
COMMENT ON COLUMN DOC.TEMA_CAPACITACION.QUIEN_MOD IS 'Id usuario que modifica el registro.';
COMMENT ON COLUMN DOC.TEMA_CAPACITACION.CUANDO_MOD IS 'Fecha y hora de modificacion del registro.';

-- -----------------------------------------------------------------------------
-- TABLA: CAPACITACION
-- -----------------------------------------------------------------------------
CREATE TABLE DOC.CAPACITACION (
    ID                      NUMBER(5)          NOT NULL,
    TEMA_CAPACITACION       NUMBER(5)           NOT NULL,
    USUARIO                 NUMBER(5)           NOT NULL,
    NOTA_OBTENIDA           VARCHAR2(255)       NULL,
    RESULTADO               VARCHAR2(255)       NULL,
    NUMERO_CERTIFICADO      NUMBER(999999999)   NULL,
    UBICACION_CERTIFICADO   VARCHAR2(800)       NULL,
    CUANDO      DATE            NOT NULL,
    PRIMARY KEY (ID)
);

CREATE SEQUENCE DOC.SQ_CAPACITACION;

ALTER TABLE DOC.CAPACITACION ADD CONSTRAINT CAPACITACION_TEMA_CAPACITACION_FK FOREIGN KEY (TEMA_CAPACITACION) REFERENCES DOC.TEMA_CAPACITACION (ID);
ALTER TABLE DOC.CAPACITACION ADD CONSTRAINT CAPACITACION_USUARIO_FK FOREIGN KEY (USUARIO) REFERENCES DOC.USUARIO (USU_ID) ENABLE;


COMMENT ON TABLE DOC.CAPACITACION IS 'Registro de cada capacitacion que se relaciona con lOS Temas de Capacitacion de SICDI.';
COMMENT ON COLUMN DOC.CAPACITACION.ID IS 'Identificador de relacion con la tabla Tema de Capacitacion.';
COMMENT ON COLUMN DOC.CAPACITACION.TEMA_CAPACITACION IS 'Identificador de relacion con la tabla Tema de Capacitacion.';
COMMENT ON COLUMN DOC.CAPACITACION.USUARIO IS 'Identificador de relacion con la tabla Usuario.';
COMMENT ON COLUMN DOC.CAPACITACION.NOTA_OBTENIDA IS 'Campo en el cual se verá reflejada la Nota obtenida por el usuario.';
COMMENT ON COLUMN DOC.CAPACITACION.RESULTADO IS 'Descripcion gramatica del resultado de la nota obtenida.';
COMMENT ON COLUMN DOC.CAPACITACION.NUMERO_CERTIFICADO IS 'Codigo generado por el sistema como comprovante de que aprobó la capacitación.';
COMMENT ON COLUMN DOC.CAPACITACION.UBICACION_CERTIFICADO IS 'Url de ubicacion del archivo que contiene el Codigo de Certificado.';
COMMENT ON COLUMN DOC.CAPACITACION.CUANDO IS 'Fecha y Hora en la que se crea el registro.';

-- -----------------------------------------------------------------------------
-- TABLA: RESULTADO_CAPACITACION
-- -----------------------------------------------------------------------------
CREATE TABLE DOC.RESULTADO_CAPACITACION (
    ID                      NUMBER(5)           NOT NULL,
    CAPACITACION            NUMBER(5)           NOT NULL,
    PREGUNTA                NUMBER(5)           NULL,
    CORRECTA                NUMBER(5)           NULL,
    PRIMARY KEY (ID)
);

CREATE SEQUENCE DOC.SQ_RESULTADO_CAPACITACION;

ALTER TABLE DOC.RESULTADO_CAPACITACION ADD CONSTRAINT RESULTADO_CAPACITACION_CAPACITACION_FK FOREIGN KEY (CAPACITACION) REFERENCES DOC.CAPACITACION (ID);


COMMENT ON TABLE DOC.RESULTADO_CAPACITACION IS 'Registro del resultado obtenido de cada capacitacion que se relaciona con los Temas de Capacitacion de SICDI.';
COMMENT ON COLUMN DOC.RESULTADO_CAPACITACION.ID IS 'Identificador de relacion de la tabla Resultado de Capacitacion.';
COMMENT ON COLUMN DOC.RESULTADO_CAPACITACION.CAPACITACION IS 'Identificador de relacion con la tabla Capacitacion.';
COMMENT ON COLUMN DOC.RESULTADO_CAPACITACION.PREGUNTA IS 'Identificador de relacion con la tabla Pregunta.';
COMMENT ON COLUMN DOC.RESULTADO_CAPACITACION.CORRECTA IS 'Identificador logico de estado (1=correcto ; 0=incorrecto).';
-- -----------------------------------------------------------------------------
-- TABLA: PREGUNTA
-- -----------------------------------------------------------------------------
CREATE TABLE DOC.PREGUNTA (
    ID                      NUMBER(5)           NOT NULL,
    TEMA_CAPACITACION       NUMBER(5)           NOT NULL,
    PREGUNTA                VARCHAR2(1000)      NOT NULL,
    ACTIVO                  NUMBER(5)           NULL,
    QUIEN                   NUMBER(38)          NOT NULL,
    CUANDO                  DATE                NOT NULL,
    QUIEN_MOD               NUMBER(38)          NULL,
    CUANDO_MOD              DATE                NULL,
    PRIMARY KEY (ID)
);

CREATE SEQUENCE DOC.SQ_PREGUNTA;

ALTER TABLE DOC.PREGUNTA ADD CONSTRAINT PREGUNTA_TEMA_CAPACITACION_FK FOREIGN KEY (TEMA_CAPACITACION) REFERENCES DOC.TEMA_CAPACITACION (ID);
ALTER TABLE DOC.PREGUNTA ADD CONSTRAINT PREGUNTA_QUIEN_FK FOREIGN KEY ("QUIEN")REFERENCES DOC.USUARIO ("USU_ID") ENABLE;
ALTER TABLE DOC.PREGUNTA ADD CONSTRAINT PREGUNTA_QUIEN_MOD_FK FOREIGN KEY ("QUIEN_MOD")REFERENCES DOC.USUARIO ("USU_ID") ENABLE;


COMMENT ON TABLE DOC.PREGUNTA IS 'Registro de las preguntas de cada Tema de Capacitación de SICDI.';
COMMENT ON COLUMN DOC.PREGUNTA.ID IS 'Identificador de relacion de la tabla Resultado de Capacitacion.';
COMMENT ON COLUMN DOC.PREGUNTA.TEMA_CAPACITACION IS 'Identificador de relacion con la tabla Tema Capacitacion.';
COMMENT ON COLUMN DOC.PREGUNTA.PREGUNTA IS 'Texto del Enunciado de la Pregunta a formular.';
COMMENT ON COLUMN DOC.PREGUNTA.ACTIVO IS 'Estado (1=activo ; 0=inactivo).';
COMMENT ON COLUMN DOC.PREGUNTA.QUIEN IS 'Id usuario que crea el registro.';
COMMENT ON COLUMN DOC.PREGUNTA.CUANDO IS 'Fecha y Hora en la que se crea el registro.';
COMMENT ON COLUMN DOC.PREGUNTA.QUIEN_MOD IS 'Id usuario que modifica el registro.';
COMMENT ON COLUMN DOC.PREGUNTA.CUANDO_MOD IS 'Fecha y hora de modificacion del registro.';
-- -----------------------------------------------------------------------------
-- TABLA: RESPUESTA
-- -----------------------------------------------------------------------------
CREATE TABLE DOC.RESPUESTA (
    ID                      NUMBER(5)           NOT NULL,
    PREGUNTA                NUMBER(5)           NOT NULL,
    TEXTO_RESPUESTA         VARCHAR2(1000)      NOT NULL,
    CORRECTA                NUMBER(5)           NOT NULL,
    ACTIVO                  NUMBER(5)           NULL,
    QUIEN                   NUMBER(38)          NOT NULL,
    CUANDO                  DATE                NOT NULL,
    QUIEN_MOD               NUMBER(38)          NULL,
    CUANDO_MOD              DATE                NULL,
    PRIMARY KEY (ID)
);

CREATE SEQUENCE DOC.SQ_RESPUESTA;

ALTER TABLE DOC.RESPUESTA ADD CONSTRAINT RESPUESTA_PREGUNTA_FK FOREIGN KEY (PREGUNTA) REFERENCES DOC.PREGUNTA (ID);
ALTER TABLE DOC.RESPUESTA ADD CONSTRAINT RESPUESTA_QUIEN_FK FOREIGN KEY ("QUIEN")REFERENCES DOC.USUARIO ("USU_ID") ENABLE;
ALTER TABLE DOC.RESPUESTA ADD CONSTRAINT RESPUESTA_QUIEN_MOD_FK FOREIGN KEY ("QUIEN_MOD")REFERENCES DOC.USUARIO ("USU_ID") ENABLE;


COMMENT ON TABLE DOC.RESPUESTA IS 'Registro de las Respuestas de cada Pregunta en un Tema de Capacitación de SICDI.';
COMMENT ON COLUMN DOC.RESPUESTA.ID IS 'Identificador de relacion de la tabla Resultado de Capacitacion.';
COMMENT ON COLUMN DOC.RESPUESTA.PREGUNTA IS 'Identificador de relacion con la tabla Pregunta.';
COMMENT ON COLUMN DOC.RESPUESTA.TEXTO_RESPUESTA IS 'Texto del Enunciado de la Respuesta a formular.';
COMMENT ON COLUMN DOC.RESPUESTA.ACTIVO IS 'Estado (1=activo ; 0=inactivo).';
COMMENT ON COLUMN DOC.RESPUESTA.QUIEN IS 'Id usuario que crea el registro.';
COMMENT ON COLUMN DOC.RESPUESTA.CUANDO IS 'Fecha y Hora en la que se crea el registro.';
COMMENT ON COLUMN DOC.RESPUESTA.QUIEN_MOD IS 'Id usuario que modifica el registro.';
COMMENT ON COLUMN DOC.RESPUESTA.CUANDO_MOD IS 'Fecha y hora de modificacion del registro.';
-- -----------------------------------------------------------------------------
-- TABLA: ROL
-- -----------------------------------------------------------------------------

INSERT INTO DOC.ROL 
    (ROL_ID, QUIEN, CUANDO, QUIEN_MOD, CUANDO_MOD, ACTIVO, ROL_NOMBRE) 
VALUES 
    ('ADMIN_TEMA_CAPACITACION',3390, SYSDATE,3390,SYSDATE,1,'Roles Administración Tema de Capacitacion');

COMMIT;



