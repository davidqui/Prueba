-- -----------------------------------------------------------------------------
-- 2017-08-25 jgarcia@controltechcg.com Issue #120 (SICDI-Controntech)
-- feature-120
-- Creación de Entidades correspondientes a Transferencia de Archivo.
-- -----------------------------------------------------------------------------

-- -----------------------------------------------------------------------------
-- TABLA: TRANSFERENCIA_ARCHIVO
-- -----------------------------------------------------------------------------

CREATE TABLE TRANSFERENCIA_ARCHIVO (
    TAR_ID              NUMBER(*,0)         NOT NULL,
    ACTIVO              NUMBER(1,0)         NOT NULL,
    ESTADO              VARCHAR2(1 CHAR)    NOT NULL,
    CREADOR_USU_ID      NUMBER(*,0)         NOT NULL,
    CREADOR_DEP_ID      NUMBER(*,0)         NOT NULL,
    CREADOR_GRA_ID      VARCHAR2(16 CHAR)   NOT NULL,
    CREADOR_USU_CARGO   VARCHAR2(500 BYTE)  NOT NULL,
    FECHA_CREACION      DATE                NOT NULL,
    ORIGEN_USU_ID       NUMBER(*,0)         NOT NULL,
    ORIGEN_DEP_ID       NUMBER(*,0)         NOT NULL,
    ORIGEN_CLA_ID       NUMBER(*,0)         NOT NULL,
    ORIGEN_GRA_ID       VARCHAR2(16 CHAR)   NOT NULL,
    ORIGEN_USU_CARGO    VARCHAR2(500 BYTE)  NOT NULL,
    DESTINO_USU_ID      NUMBER(*,0)         NOT NULL,
    DESTINO_DEP_ID      NUMBER(*,0)         NOT NULL,
    DESTINO_CLA_ID      NUMBER(*,0)         NOT NULL,
    DESTINO_GRA_ID      VARCHAR2(16 CHAR)   NOT NULL,
    DESTINO_USU_CARGO   VARCHAR2(500 BYTE)  NOT NULL,
    NUM_DOCUMENTOS      NUMBER(8,0)         NOT NULL,
    FECHA_APROBACION    DATE                NULL,
    ACTA_OFS            VARCHAR2(32 CHAR)   NULL,
    PRIMARY KEY (TAR_ID)
);

-- INDEX

CREATE INDEX TAR_ACTIVO_IDX ON TRANSFERENCIA_ARCHIVO (ACTIVO);
CREATE INDEX TAR_ESTADO_IDX ON TRANSFERENCIA_ARCHIVO (ESTADO);

-- FOREIGN KEY

ALTER TABLE TRANSFERENCIA_ARCHIVO ADD CONSTRAINT TAR_CREADOR_USU_ID_FK
FOREIGN KEY (CREADOR_USU_ID) REFERENCES USUARIO (USU_ID);

ALTER TABLE TRANSFERENCIA_ARCHIVO ADD CONSTRAINT TAR_CREADOR_DEP_ID_FK
FOREIGN KEY (CREADOR_DEP_ID) REFERENCES DEPENDENCIA (DEP_ID);

ALTER TABLE TRANSFERENCIA_ARCHIVO ADD CONSTRAINT TAR_CREADOR_GRA_ID_FK
FOREIGN KEY (CREADOR_GRA_ID) REFERENCES GRADO (GRA_ID);

ALTER TABLE TRANSFERENCIA_ARCHIVO ADD CONSTRAINT TAR_ORIGEN_USU_ID_FK
FOREIGN KEY (ORIGEN_USU_ID) REFERENCES USUARIO (USU_ID);

ALTER TABLE TRANSFERENCIA_ARCHIVO ADD CONSTRAINT TAR_ORIGEN_DEP_ID_FK
FOREIGN KEY (ORIGEN_DEP_ID) REFERENCES DEPENDENCIA (DEP_ID);

ALTER TABLE TRANSFERENCIA_ARCHIVO ADD CONSTRAINT TAR_ORIGEN_GRA_ID_FK
FOREIGN KEY (ORIGEN_GRA_ID) REFERENCES GRADO (GRA_ID);

ALTER TABLE TRANSFERENCIA_ARCHIVO ADD CONSTRAINT TAR_ORIGEN_CLA_ID_FK
FOREIGN KEY (ORIGEN_CLA_ID) REFERENCES CLASIFICACION (CLA_ID);

ALTER TABLE TRANSFERENCIA_ARCHIVO ADD CONSTRAINT TAR_DESTINO_USU_ID_FK
FOREIGN KEY (DESTINO_USU_ID) REFERENCES USUARIO (USU_ID);

ALTER TABLE TRANSFERENCIA_ARCHIVO ADD CONSTRAINT TAR_DESTINO_DEP_ID_FK
FOREIGN KEY (DESTINO_DEP_ID) REFERENCES DEPENDENCIA (DEP_ID);

ALTER TABLE TRANSFERENCIA_ARCHIVO ADD CONSTRAINT TAR_DESTINO_GRA_ID_FK
FOREIGN KEY (DESTINO_GRA_ID) REFERENCES GRADO (GRA_ID);

ALTER TABLE TRANSFERENCIA_ARCHIVO ADD CONSTRAINT TAR_DESTINO_CLA_ID_FK
FOREIGN KEY (DESTINO_CLA_ID) REFERENCES CLASIFICACION (CLA_ID);

-- SEQUENCE

CREATE SEQUENCE TRANSFERENCIA_ARCHIVO_SEQ;

-- -----------------------------------------------------------------------------
-- TABLA: TRANSFERENCIA_ARCHIVO_DETALLE
-- -----------------------------------------------------------------------------

CREATE TABLE TRANSFERENCIA_ARCHIVO_DETALLE (
    TAD_ID          NUMBER(*,0)         NOT NULL,
    TAR_ID          NUMBER(*,0)         NOT NULL,
    DCDP_ID         NUMBER(*,0)         NOT NULL,
    DOC_ID          VARCHAR2(32 CHAR)   NOT NULL,
    ANTERIOR_DEP_ID NUMBER(*,0)         NOT NULL,
    ANTERIOR_QUIEN  NUMBER(*,0)         NOT NULL,
    ANTERIOR_CUANDO DATE                NOT NULL,
    NUEVO_DEP_ID    NUMBER(*,0)         NOT NULL,
    NUEVO_QUIEN     NUMBER(*,0)         NOT NULL,
    NUEVO_CUANDO    DATE                NOT NULL,
    PRIMARY KEY (TAD_ID)
);

-- INDEX

CREATE INDEX TAD_DOC_ID_IDX ON TRANSFERENCIA_ARCHIVO_DETALLE (DOC_ID);

-- FOREIGN KEY

ALTER TABLE TRANSFERENCIA_ARCHIVO_DETALLE ADD CONSTRAINT TAD_TAR_ID_FK
FOREIGN KEY (TAR_ID) REFERENCES TRANSFERENCIA_ARCHIVO (TAR_ID);

ALTER TABLE TRANSFERENCIA_ARCHIVO_DETALLE ADD CONSTRAINT TAD_DCDP_ID_FK
FOREIGN KEY (DCDP_ID) REFERENCES DOCUMENTO_DEPENDENCIA (DCDP_ID);

ALTER TABLE TRANSFERENCIA_ARCHIVO_DETALLE ADD CONSTRAINT TAD_ANTERIOR_DEP_ID_FK
FOREIGN KEY (ANTERIOR_DEP_ID) REFERENCES DEPENDENCIA (DEP_ID);

ALTER TABLE TRANSFERENCIA_ARCHIVO_DETALLE ADD CONSTRAINT TAD_ANTERIOR_QUIEN_FK
FOREIGN KEY (ANTERIOR_QUIEN) REFERENCES USUARIO (USU_ID);

ALTER TABLE TRANSFERENCIA_ARCHIVO_DETALLE ADD CONSTRAINT TAD_NUEVO_DEP_ID_FK
FOREIGN KEY (NUEVO_DEP_ID) REFERENCES DEPENDENCIA (DEP_ID);

ALTER TABLE TRANSFERENCIA_ARCHIVO_DETALLE ADD CONSTRAINT TAD_NUEVO_QUIEN_FK
FOREIGN KEY (NUEVO_QUIEN) REFERENCES USUARIO (USU_ID);

-- SEQUENCE

CREATE SEQUENCE TRANSFERENCIA_ARCHIVO_DET_SEQ;

-- -----------------------------------------------------------------------------
-- TABLA: PLANTILLA_TRANSF_ARCHIVO
-- -----------------------------------------------------------------------------

CREATE TABLE PLANTILLA_TRANSF_ARCHIVO (
    PTA_ID          NUMBER(*,0)         NOT NULL,
    ACTIVO          NUMBER(1,0)         NOT NULL,
    NOMBRE_ARCHIVO  VARCHAR2(512 CHAR)  NOT NULL,
    TAMANYO_ARCHIVO NUMBER(*,0)         NOT NULL,
    FIRMA_MD5       VARCHAR2(32 CHAR)   NOT NULL,
    CODIGO_OFS      VARCHAR2(32 CHAR)   NOT NULL,
    QUIEN           NUMBER(*,0)         NOT NULL,
    CUANDO          DATE                NOT NULL,
    PRIMARY KEY (PTA_ID)
);

-- INDEX

CREATE INDEX PTA_ACTIVO_IDX     ON PLANTILLA_TRANSF_ARCHIVO (ACTIVO);

-- FOREIGN KEY

ALTER TABLE PLANTILLA_TRANSF_ARCHIVO ADD CONSTRAINT PTA_QUIEN_FK
FOREIGN KEY (QUIEN) REFERENCES USUARIO (USU_ID);

-- SEQUENCE

CREATE SEQUENCE PLANTILLA_TRANSF_ARCHIVO_SEQ;