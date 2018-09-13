-- 
-- 2018-08-30 jcespedeso@imi.mil.co Issue_feature_9 (SICDI-GETDE).
-- 2018-08-30 dquijanor@imi.mil.co Issue_feature_9 (SICDI-GETDE).
-- 2018-08-30 aherreram@imi.mil.co Issue_feature_9 (SICDI-GETDE).
-- -----------------------------------------------------------------------------
-- TABLA: TEMATICA
-- -----------------------------------------------------------------------------
CREATE TABLE TEMATICA (
    ID          NUMBER(5)       NOT NULL,
    NOMBRE      VARCHAR2(150)   NOT NULL,
    DESCRIPCION VARCHAR2(1000)  NOT NULL,
    ACTIVO      NUMBER(5)       NOT NULL,
    QUIEN       NUMBER(38)      NOT NULL,
    CUANDO      DATE            NOT NULL,
    QUIEN_MOD   NUMBER(38)      NOT NULL,
    CUANDO_MOD  DATE            NOT NULL,
    PRIMARY KEY (ID)
);
CREATE INDEX TEMATICA_ACTIVO_IDX ON TEMATICA(ACTIVO);
CREATE SEQUENCE SQ_TEMATICA;

ALTER TABLE TEMATICA ADD CONSTRAINT "TEMATICA_QUIEN_FK" FOREIGN KEY ("QUIEN")REFERENCES "DOC"."USUARIO" ("USU_ID") ENABLE;
ALTER TABLE TEMATICA ADD CONSTRAINT "TEMATICA_QUIEN_MOD_FK" FOREIGN KEY ("QUIEN_MOD")REFERENCES "DOC"."USUARIO" ("USU_ID") ENABLE;

COMMENT ON TABLE TEMATICA 
IS 'Registro de las Tematicas que se relacionan con el funcionamiento de SICDI.';
COMMENT ON COLUMN TEMATICA.NOMBRE IS 'Nombre especifico de la tematica.';
COMMENT ON COLUMN TEMATICA.DESCRIPCION IS 'Información puntual sobre la tematica a crear.';
COMMENT ON COLUMN TEMATICA.ACTIVO IS 'Estado (1=activo ; 0=inactivo).';
COMMENT ON COLUMN TEMATICA.QUIEN IS 'Id usuario que crea el registro.';
COMMENT ON COLUMN TEMATICA.CUANDO IS 'Fecha y Hora en la que se crea el registro.';
COMMENT ON COLUMN TEMATICA.QUIEN_MOD IS 'Id usuario que modifica el registro.';
COMMENT ON COLUMN TEMATICA.CUANDO_MOD IS 'Fecha y hora de modificacion del registro.';

-- -----------------------------------------------------------------------------
-- TABLA: RECURSO_MULTIMEDIA
-- -----------------------------------------------------------------------------
CREATE TABLE RECURSO_MULTIMEDIA (
    ID          NUMBER(5)       NOT NULL,
    TEMATICA    NUMBER(5)       NOT NULL,
    NOMBRE      VARCHAR2(255)   NOT NULL,
    DESCRIPCION VARCHAR2(1000)   NOT NULL,
    ACTIVO      NUMBER(5)       NOT NULL,
    FUENTE      VARCHAR2(255)   NOT NULL,
    PESO_ORDEN  NUMBER(38),
    TIPO        VARCHAR2(60)    NOT NULL,
    UBICACION   VARCHAR2(1000)  NOT NULL,
    QUIEN       NUMBER(38)      NOT NULL,
    CUANDO      DATE            NOT NULL,
    QUIEN_MOD   NUMBER(38)      NOT NULL,
    CUANDO_MOD  DATE            NOT NULL,
    NOMBRE_ARCHIVO_ORIGINAL VARCHAR2(255) NOT NULL, 
    NOMBRE_ARCHIVO_FINAL VARCHAR2(255) NOT NULL,
    PRIMARY KEY (ID)
);

CREATE SEQUENCE SQ_RECURSO_MULTIMEDIA;

ALTER TABLE RECURSO_MULTIMEDIA ADD CONSTRAINT RECURSO_MULTIMEDIA_TEMATICA_FK FOREIGN KEY (TEMATICA) REFERENCES TEMATICA (ID);
ALTER TABLE RECURSO_MULTIMEDIA ADD CONSTRAINT RECURSO_MULTIMEDIA_QUIEN_FK FOREIGN KEY (QUIEN) REFERENCES DOC.USUARIO (USU_ID) ENABLE;
ALTER TABLE RECURSO_MULTIMEDIA ADD CONSTRAINT RECUR_MULTIMED_QUIEN_MOD_FK FOREIGN KEY (QUIEN_MOD) REFERENCES DOC.USUARIO (USU_ID) ENABLE;


COMMENT ON TABLE RECURSO_MULTIMEDIA IS 'Registro de cada recurso multimedia que se relaciona con las Tematicas de SICDI.';
COMMENT ON COLUMN RECURSO_MULTIMEDIA.TEMATICA IS 'Identificador de relacion con la tabla tematica.';
COMMENT ON COLUMN RECURSO_MULTIMEDIA.NOMBRE IS 'Nombre especifico del recurso multimedia.';
COMMENT ON COLUMN RECURSO_MULTIMEDIA.DESCRIPCION IS 'Información puntual sobre el recurso multimedia a crear.';
COMMENT ON COLUMN RECURSO_MULTIMEDIA.ACTIVO IS 'Estado (1=activo ; 0=inactivo).';
COMMENT ON COLUMN RECURSO_MULTIMEDIA.FUENTE IS 'Origen de creación del recurso multimedia.';
COMMENT ON COLUMN RECURSO_MULTIMEDIA.PESO_ORDEN IS 'Valor numerico que clasifica la fuente multimedia.';
COMMENT ON COLUMN RECURSO_MULTIMEDIA.TIPO IS 'ContenType del archivo cargado.';
COMMENT ON COLUMN RECURSO_MULTIMEDIA.UBICACION IS 'Url de ubicacion del archivo.';
COMMENT ON COLUMN RECURSO_MULTIMEDIA.QUIEN IS 'Id usuario que crea el registro.';
COMMENT ON COLUMN RECURSO_MULTIMEDIA.CUANDO IS 'Fecha y Hora en la que se crea el registro.';
COMMENT ON COLUMN RECURSO_MULTIMEDIA.QUIEN_MOD IS 'Id usuario que modifica el registro.';
COMMENT ON COLUMN RECURSO_MULTIMEDIA.CUANDO_MOD IS 'Fecha y hora de modificacion del registro.';

-- -----------------------------------------------------------------------------
-- TABLA: ROL
-- -----------------------------------------------------------------------------

INSERT INTO ROL 
    (ROL_ID, QUIEN, CUANDO, QUIEN_MOD, CUANDO_MOD, ACTIVO, ROL_NOMBRE) 
VALUES 
    ('ADMIN_TEMATICA',3390, SYSDATE,3390,SYSDATE,1,'Roles Administración Tematica');


INSERT INTO ROL 
    (ROL_ID, QUIEN, CUANDO, QUIEN_MOD, CUANDO_MOD, ACTIVO, ROL_NOMBRE) 
VALUES 
    ('ADMIN_RECURSO_MULTIMEDIA',3390, SYSDATE,3390,SYSDATE,1,'Roles Administración Recursos Multimedia');




COMMIT;



