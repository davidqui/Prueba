--
-- Author:  samuel.delgado@controltechcg.com
-- Created: 15/08/2018 Issue gogs #7 (SICDI-Controltech) feature-gogs-7
--


-- -----------------------------------------------------------------------------
-- TABLA: RAZON_INHABILITAR
-- -----------------------------------------------------------------------------

CREATE SEQUENCE RAZON_INHABILITAR_SEQ;

CREATE TABLE RAZON_INHABILITAR (
    RAZ_ID              NUMBER(38)  NOT NULL,
    TEXTO_RAZON         VARCHAR2(64 CHAR)   NOT NULL,
    ACTIVO              NUMBER(1)           NOT NULL,
    QUIEN               NUMBER(38)          NOT NULL,
    CUANDO              DATE                NOT NULL,
    QUIEN_MOD           NUMBER(38)          NOT NULL,
    CUANDO_MOD          DATE                NOT NULL,
    PRIMARY KEY (RAZ_ID)
);

ALTER TABLE RAZON_INHABILITAR
ADD CONSTRAINT RAZON_INHABILITAR_QUIEN_FK
FOREIGN KEY (QUIEN)
REFERENCES USUARIO (USU_ID);

ALTER TABLE RAZON_INHABILITAR
ADD CONSTRAINT RAZON_INHABILITAR_QUIEN_MOD_FK
FOREIGN KEY (QUIEN_MOD)
REFERENCES USUARIO (USU_ID);

CREATE INDEX RAZON_INHABILITAR_ACTIVO_IDX 
ON RAZON_INHABILITAR (ACTIVO);

-- -----------------------------------------------------------------------------
-- TABLA: USUARIO
-- -----------------------------------------------------------------------------

ALTER TABLE USUARIO ADD (
    USU_ACTIVO NUMBER(1,0) DEFAULT 1 NOT NULL, 
    RAZ_ID NUMBER(38) NULL
);

ALTER TABLE USUARIO
ADD CONSTRAINT USUARIO_RAZ_ID_FK
FOREIGN KEY (RAZ_ID)
REFERENCES RAZON_INHABILITAR (RAZ_ID);

-- -----------------------------------------------------------------------------
-- TABLA: ROL
-- -----------------------------------------------------------------------------

INSERT INTO ROL 
    (ROL_ID, QUIEN, CUANDO, QUIEN_MOD, CUANDO_MOD, ACTIVO, ROL_NOMBRE) 
VALUES 
    ('ADMIN_RAZON_INHABILITAR', 3390, SYSDATE, 3390, SYSDATE, 1, 'Administrar Razones de inhabilitar usuario')
;