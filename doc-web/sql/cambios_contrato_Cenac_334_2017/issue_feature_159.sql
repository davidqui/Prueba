--
-- Author:  jgarcia@controltechcg.com
-- Created: 2/05/2018 Issue #159 (SICDI-Controltech) feature-159
--

-- -----------------------------------------------------------------------------
-- TABLA: DOMINIO
-- -----------------------------------------------------------------------------

CREATE TABLE DOMINIO (
    DOM_CODIGO              VARCHAR2(8)     NOT NULL,
    NOMBRE                  VARCHAR2(64)    NOT NULL,
    DESCRIPCION             VARCHAR2(256)   NULL,
    QUIEN                   NUMBER(38)      NOT NULL,
    CUANDO                  TIMESTAMP       NOT NULL,
    ACTIVO                  NUMBER(1)       NOT NULL,
    QUIEN_MOD               NUMBER(38)      NOT NULL,
    CUANDO_MOD              TIMESTAMP       NOT NULL,
    DOM_PRV_VER_LINK_OWA    NUMBER(1)       NOT NULL,
    PRIMARY KEY (DOM_CODIGO)
);

COMMENT ON TABLE DOMINIO
IS 'Información y parametrización de dominios utilizados en el sistema.';

COMMENT ON COLUMN DOMINIO.DOM_CODIGO
IS 'Código asignado al dominio.';
COMMENT ON COLUMN DOMINIO.NOMBRE
IS 'Nombre.';
COMMENT ON COLUMN DOMINIO.DESCRIPCION
IS 'Descripción.';
COMMENT ON COLUMN DOMINIO.QUIEN
IS 'ID del usuario creador del dominio.';
COMMENT ON COLUMN DOMINIO.CUANDO
IS 'Fecha y hora de creación del dominio.';
COMMENT ON COLUMN DOMINIO.ACTIVO
IS 'Indicador de actividad (0=activo;1=inactivo).';
COMMENT ON COLUMN DOMINIO.QUIEN_MOD
IS 'ID del último usuario que modificó el dominio.';
COMMENT ON COLUMN DOMINIO.CUANDO_MOD
IS 'Fecha y hora de la última modificación del dominio.';
COMMENT ON COLUMN DOMINIO.DOM_PRV_VER_LINK_OWA
IS 'Indicador de privilegio (temporal) para visualizar el Link a OWA.';

ALTER TABLE DOMINIO
ADD CONSTRAINT DOMINIO_QUIEN_FK
FOREIGN KEY (QUIEN)
REFERENCES USUARIO (USU_ID);

ALTER TABLE DOMINIO
ADD CONSTRAINT DOMINIO_QUIEN_MOD_FK
FOREIGN KEY (QUIEN_MOD)
REFERENCES USUARIO (USU_ID);

CREATE INDEX DOMINIO_ACTIVO_IDX 
ON DOMINIO (ACTIVO);

CREATE INDEX DOMINIO_PRV_VER_LINK_OWA_IDX 
ON DOMINIO (DOM_PRV_VER_LINK_OWA);

INSERT INTO DOMINIO 
(DOM_CODIGO, NOMBRE, DESCRIPCION, QUIEN, CUANDO, ACTIVO, QUIEN_MOD, CUANDO_MOD, DOM_PRV_VER_LINK_OWA)
VALUES
('10', 'CAIMI', '', 3390, SYSDATE, 1, 3390, SYSDATE, 1)
;

-- -----------------------------------------------------------------------------
-- TABLA: USUARIO (MODIFICACIONES)
-- -----------------------------------------------------------------------------

ALTER TABLE USUARIO ADD (DOM_CODIGO VARCHAR2(8));

ALTER TABLE USUARIO
ADD CONSTRAINT USUARIO_DOM_CODIGO_FK
FOREIGN KEY (DOM_CODIGO)
REFERENCES DOMINIO (DOM_CODIGO);

UPDATE USUARIO SET DOM_CODIGO = '10';

ALTER TABLE USUARIO MODIFY (DOM_CODIGO VARCHAR2(8) NOT NULL);

-- -----------------------------------------------------------------------------
-- TABLA: H_DOMINIO
-- -----------------------------------------------------------------------------

CREATE TABLE H_DOMINIO (
    HDOM_ID                 NUMBER(38)      NOT NULL,
    DOM_CODIGO              VARCHAR2(8)     NOT NULL,
    NOMBRE                  VARCHAR2(64)    NOT NULL,
    DESCRIPCION             VARCHAR2(256)   NULL,
    QUIEN                   NUMBER(38)      NOT NULL,
    CUANDO                  TIMESTAMP       NOT NULL,
    ACTIVO                  NUMBER(1)       NOT NULL,
    QUIEN_MOD               NUMBER(38)      NOT NULL,
    CUANDO_MOD              TIMESTAMP       NOT NULL,
    DOM_PRV_VER_LINK_OWA    NUMBER(1)       NOT NULL,
    PRIMARY KEY (HDOM_ID)
);

COMMENT ON TABLE H_DOMINIO
IS 'Información y parametrización histórica de dominios.';

COMMENT ON COLUMN H_DOMINIO.HDOM_ID
IS 'ID consecutivo del histórico del dominio.';
COMMENT ON COLUMN H_DOMINIO.DOM_CODIGO
IS 'Código asignado al dominio.';
COMMENT ON COLUMN H_DOMINIO.NOMBRE
IS 'Nombre.';
COMMENT ON COLUMN H_DOMINIO.DESCRIPCION
IS 'Descripción.';
COMMENT ON COLUMN H_DOMINIO.QUIEN
IS 'ID del usuario creador del dominio.';
COMMENT ON COLUMN H_DOMINIO.CUANDO
IS 'Fecha y hora de creación del dominio.';
COMMENT ON COLUMN H_DOMINIO.ACTIVO
IS 'Indicador de actividad (0=activo;1=inactivo).';
COMMENT ON COLUMN H_DOMINIO.QUIEN_MOD
IS 'ID del último usuario que modificó el dominio.';
COMMENT ON COLUMN H_DOMINIO.CUANDO_MOD
IS 'Fecha y hora de la última modificación del dominio.';
COMMENT ON COLUMN H_DOMINIO.DOM_PRV_VER_LINK_OWA
IS 'Indicador de privilegio (temporal) para visualizar el Link a OWA.';

CREATE SEQUENCE H_DOMINIO_SEQ;

ALTER TABLE H_DOMINIO
ADD CONSTRAINT H_DOMINIO_DOM_CODIGO_FK
FOREIGN KEY (DOM_CODIGO)
REFERENCES DOMINIO (DOM_CODIGO);

ALTER TABLE H_DOMINIO
ADD CONSTRAINT H_DOMINIO_QUIEN_FK
FOREIGN KEY (QUIEN)
REFERENCES USUARIO (USU_ID);

ALTER TABLE H_DOMINIO
ADD CONSTRAINT H_DOMINIO_QUIEN_MOD_FK
FOREIGN KEY (QUIEN_MOD)
REFERENCES USUARIO (USU_ID);

CREATE INDEX H_DOMINIO_ACTIVO_IDX 
ON H_DOMINIO (ACTIVO);

CREATE INDEX H_DOMINIO_PRV_VER_LINK_OWA_IDX 
ON H_DOMINIO (DOM_PRV_VER_LINK_OWA);

-- -----------------------------------------------------------------------------
-- TABLA: LINK_OWA_REGISTRO_USO
-- -----------------------------------------------------------------------------

CREATE TABLE LINK_OWA_REGISTRO_USO (
    LOWA_ID     NUMBER(38)      NOT NULL,
    QUIEN       NUMBER(38)      NOT NULL,
    CUANDO      TIMESTAMP       NOT NULL,
    DOM_CODIGO  VARCHAR2(8)     NOT NULL,
    LINK_URL    VARCHAR2(256)   NOT NULL,
    PRIMARY KEY (LOWA_ID)
);

COMMENT ON TABLE LINK_OWA_REGISTRO_USO
IS 'Traza auditable de usuarios que utilizaron el link a OWA a través de SICDI.';

COMMENT ON COLUMN LINK_OWA_REGISTRO_USO.LOWA_ID
IS 'ID consecutivo del histórico del registro.';
COMMENT ON COLUMN LINK_OWA_REGISTRO_USO.QUIEN
IS 'ID del usuario registrado.';
COMMENT ON COLUMN LINK_OWA_REGISTRO_USO.CUANDO
IS 'Fecha y hora del registro.';
COMMENT ON COLUMN LINK_OWA_REGISTRO_USO.DOM_CODIGO
IS 'Código del dominio del usuario.';
COMMENT ON COLUMN LINK_OWA_REGISTRO_USO.LINK_URL
IS 'URL a la cual se realizó la redirección.';

CREATE SEQUENCE LINK_OWA_REGISTRO_USO_SQ;

ALTER TABLE LINK_OWA_REGISTRO_USO
ADD CONSTRAINT LOWA_REGISTRO_QUIEN_FK
FOREIGN KEY (QUIEN)
REFERENCES USUARIO (USU_ID);

ALTER TABLE LINK_OWA_REGISTRO_USO
ADD CONSTRAINT LOWA_REGISTRO_DOM_CODIGO_FK
FOREIGN KEY (DOM_CODIGO)
REFERENCES DOMINIO (DOM_CODIGO);

CREATE INDEX LOWA_REGISTRO_USO_LINK_URL_IDX 
ON LINK_OWA_REGISTRO_USO (LINK_URL);

-- -----------------------------------------------------------------------------
-- TABLA: TRG_REGISTRO_H_DOMINIO
-- -----------------------------------------------------------------------------

CREATE OR REPLACE TRIGGER "TRG_REGISTRO_H_DOMINIO" 
AFTER INSERT OR UPDATE ON DOMINIO 
FOR EACH ROW
BEGIN
  INSERT INTO H_DOMINIO 
  (
    HDOM_ID,
    DOM_CODIGO,
    NOMBRE,
    DESCRIPCION,
    QUIEN,
    CUANDO,
    ACTIVO,
    QUIEN_MOD,
    CUANDO_MOD,
    DOM_PRV_VER_LINK_OWA
  ) VALUES (
    H_DOMINIO_SEQ.NEXTVAL,
    :NEW.DOM_CODIGO,
    :NEW.NOMBRE,
    :NEW.DESCRIPCION,
    :NEW.QUIEN,
    :NEW.CUANDO,
    :NEW.ACTIVO,
    :NEW.QUIEN_MOD,
    :NEW.CUANDO_MOD,
    :NEW.DOM_PRV_VER_LINK_OWA
  );
END;