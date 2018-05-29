--
-- Author:  samuel.delgado@controltechcg.com
-- Created: 29/05/2018 Issue #169 (SICDI-Controltech) feature-169
--

-- -----------------------------------------------------------------------------
-- TABLA: WILDCARD_NOTIFICACION
-- -----------------------------------------------------------------------------

CREATE TABLE WILDCARD_NOTIFICACION(
    WNF_ID              NUMBER(38)          NOT NULL,
    NOMBRE              VARCHAR2(32 CHAR)   NOT NULL,
    VALOR               VARCHAR2(32 CHAR)   NOT NULL,
    PRIMARY KEY (WNF_ID)
)

COMMENT ON TABLE WILDCARD_NOTIFICACION
IS 'Información y parametrización de variables para agregar en el cuerpo de la notificación';

COMMENT ON COLUMN WILDCARD_NOTIFICACION.WNF_ID
IS 'Código asignado a la variable del cuerpo de la notificación.';
COMMENT ON COLUMN WILDCARD_NOTIFICACION.NOMBRE
IS 'Nombre asignado a la variable del cuerpo de la notificación.';
COMMENT ON COLUMN WILDCARD_NOTIFICACION.VALOR
IS 'Valor asignado a la variable del cuerpo de la notificación.';

INSERT INTO WILDCARD_NOTIFICACION (WNF_ID, NOMBRE, VALOR) VALUES (10, 'Nombre Usuario', '$(usuario.nombre)');
INSERT INTO WILDCARD_NOTIFICACION (WNF_ID, NOMBRE, VALOR) VALUES (20, 'Clasificación Usuario', '$(usuario.usuGrado)');

-- -----------------------------------------------------------------------------
-- TABLA: TIPO_NOTIFICACION
-- -----------------------------------------------------------------------------

CREATE TABLE TIPO_NOTIFICACION(
    TNF_ID              NUMBER(38)          NOT NULL,
    NOMBRE              VARCHAR2(32 CHAR)   NOT NULL,
    VALOR               NUMBER(38)          NOT NULL,
    ACTIVO              NUMBER(1)           NOT NULL,    
    QUIEN               NUMBER(38)          NOT NULL,
    CUANDO              TIMESTAMP           NOT NULL,
    QUIEN_MOD           NUMBER(38)          NULL,
    CUANDO_MOD          TIMESTAMP           NULL,
    PRIMARY KEY (TNF_ID)
)

COMMENT ON TABLE TIPO_NOTIFICACION
IS 'Información y parametrización de notificaciones utilizadas en el sistema.';

COMMENT ON COLUMN TIPO_NOTIFICACION.NTF_ID
IS 'Código asignado al tipo de notificación.';
COMMENT ON COLUMN TIPO_NOTIFICACION.NOMBRE
IS 'Nombre asignado al tipo de notificación.';
COMMENT ON COLUMN TIPO_NOTIFICACION.VALOR
IS 'Valor asignado al tipo de notificación.';
COMMENT ON COLUMN TIPO_NOTIFICACION.ACTIVO
IS 'Indicador de actividad (0=activo;1=inactivo).';
COMMENT ON COLUMN TIPO_NOTIFICACION.QUIEN
IS 'ID del usuario creador del tipo de notificación.';
COMMENT ON COLUMN TIPO_NOTIFICACION.CUANDO
IS 'Fecha y hora de creación del tipo de notificación.';
COMMENT ON COLUMN TIPO_NOTIFICACION.QUIEN_MOD
IS 'ID del último usuario que modificó el tipo de notificación.';
COMMENT ON COLUMN TIPO_NOTIFICACION.CUANDO_MOD
IS 'Fecha y hora de la última modificación del tipo de notificación.';

ALTER TABLE TIPO_NOTIFICACION
ADD CONSTRAINT TIPO_NOTIFICACION_QUIEN_FK
FOREIGN KEY (QUIEN)
REFERENCES USUARIO (USU_ID);

ALTER TABLE TIPO_NOTIFICACION
ADD CONSTRAINT TIPO_NOTIFICACION_QUIEN_MOD_FK
FOREIGN KEY (QUIEN_MOD)
REFERENCES USUARIO (USU_ID);

-- -----------------------------------------------------------------------------
-- TABLA: WILDCARD_TIPO_NOTIFICACION
-- -----------------------------------------------------------------------------

CREATE TABLE WILDCARD_TIPO_NOTIFICACION(
    TNF_ID               NUMBER(38)          NOT NULL,
    WNF_ID               NUMBER(38)          NOT NULL,
    PRIMARY KEY (TNF_ID, WNF_ID)
)

COMMENT ON TABLE WILDCARD_TIPO_NOTIFICACION
IS 'Tabla intermedia entre TIPO_NOTIFICACION y WILDCARD_NOTIFICACION.';

COMMENT ON COLUMN WILDCARD_TIPO_NOTIFICACION.TNF_ID
IS 'ID de el tipo de notificación.';
COMMENT ON COLUMN WILDCARD_TIPO_NOTIFICACION.WNF_ID
IS 'ID de la variable para el cuerpo de la notificación';

ALTER TABLE WILDCARD_TIPO_NOTIFICACION
ADD CONSTRAINT WILDCARD_TIPO_NTF_TIPO_NTF_FK
FOREIGN KEY (TNF_ID)
REFERENCES TIPO_NOTIFICACION (TNF_ID);

ALTER TABLE WILDCARD_TIPO_NOTIFICACION
ADD CONSTRAINT WILDCARD_TIPO_NTF_WILD_NTF_FK
FOREIGN KEY (WNF_ID)
REFERENCES WILDCARD_NOTIFICACION (WNF_ID);


-- -----------------------------------------------------------------------------
-- TABLA: NOTIFICACION
-- -----------------------------------------------------------------------------

CREATE TABLE NOTIFICACION(
    NTF_ID              NUMBER(38)          NOT NULL,
    TNF_ID              NUMBER(38)          NOT NULL,
    CLA_ID              NUMBER(38)          NOT NULL,
    CUERPO              BLOB                NOT NULL,
    ACTIVO              NUMBER(1)           NOT NULL,
    QUIEN               NUMBER(38)          NOT NULL,
    CUANDO              TIMESTAMP           NOT NULL,
    QUIEN_MOD           NUMBER(38)          NULL,
    CUANDO_MOD          TIMESTAMP           NULL,
    PRIMARY KEY (NTF_ID)
)

COMMENT ON TABLE NOTIFICACION
IS 'Información y parametrización de notificaciones utilizadas en el sistema.';

COMMENT ON COLUMN NOTIFICACION.NTF_ID
IS 'Código asignado a la notificación.';
COMMENT ON COLUMN NOTIFICACION.TNF_ID
IS 'ID de el tipo de notificación';
COMMENT ON COLUMN NOTIFICACION.CLA_ID
IS 'ID de el nivel de clasificaón de la notificación';
COMMENT ON COLUMN NOTIFICACION.CUERPO
IS 'Es el segmento de codigo ftl del template de la notificación';
COMMENT ON COLUMN NOTIFICACION.ACTIVO
IS 'Indicador de actividad (0=activo;1=inactivo).';
COMMENT ON COLUMN NOTIFICACION.QUIEN
IS 'ID del usuario creador de la notificación.';
COMMENT ON COLUMN NOTIFICACION.CUANDO
IS 'Fecha y hora de creación de la notificación.';
COMMENT ON COLUMN NOTIFICACION.QUIEN_MOD
IS 'ID del último usuario que modificó la notificación.';
COMMENT ON COLUMN NOTIFICACION.CUANDO_MOD
IS 'Fecha y hora de la última modificación de la notificación.';

ALTER TABLE NOTIFICACION
ADD CONSTRAINT NOTIFICACION_QUIEN_FK
FOREIGN KEY (QUIEN)
REFERENCES USUARIO (USU_ID);

ALTER TABLE NOTIFICACION
ADD CONSTRAINT NOTIFICACION_QUIEN_MOD_FK
FOREIGN KEY (QUIEN_MOD)
REFERENCES USUARIO (USU_ID);

ALTER TABLE NOTIFICACION
ADD CONSTRAINT NOTIFICACION_TNF_ID_FK
FOREIGN KEY (TNF_ID)
REFERENCES TIPO_NOTIFICACION (USU_ID);

CREATE INDEX NOTIFICACION_ACTIVO_IDX 
ON NOTIFICACION (ACTIVO);

-- -----------------------------------------------------------------------------
-- TABLA: H_NOTIFICACION
-- -----------------------------------------------------------------------------

CREATE TABLE H_NOTIFICACION(
    HNT_ID                NUMBER(38)          NOT NULL,
    TNF_ID               NUMBER(38)          NOT NULL,
    CLA_ID              NUMBER(38)          NOT NULL,
    CUERPO              TEXT                NOT NULL,
    ACTIVO              NUMBER(1)           NOT NULL,
    QUIEN               NUMBER(38)          NOT NULL,
    CUANDO              TIMESTAMP           NOT NULL,
    QUIEN_MOD           NUMBER(38)          NULL,
    CUANDO_MOD          TIMESTAMP           NULL,
    PRIMARY KEY (HNT_ID)
)

COMMENT ON TABLE H_NOTIFICACION
IS 'Información y parametrización histórica de notificaciones en el sistema.';

COMMENT ON COLUMN H_NOTIFICACION.HNT_ID
IS 'Código asignado a la notificación.';
COMMENT ON COLUMN H_NOTIFICACION.TNF_ID
IS 'ID de el tipo de notificación';
COMMENT ON COLUMN NOTIFICACION.CLA_ID
IS 'ID de el nivel de clasificaón de la notificación';
COMMENT ON COLUMN H_NOTIFICACION.CUERPO
IS 'Es el segmento de codigo ftl del template de la notificación';
COMMENT ON COLUMN H_NOTIFICACION.ACTIVO
IS 'Indicador de actividad (0=activo;1=inactivo).';
COMMENT ON COLUMN H_NOTIFICACION.QUIEN
IS 'ID del usuario creador de la notificación.';
COMMENT ON COLUMN H_NOTIFICACION.CUANDO
IS 'Fecha y hora de creación de la notificación.';
COMMENT ON COLUMN H_NOTIFICACION.QUIEN_MOD
IS 'ID del último usuario que modificó la notificación.';
COMMENT ON COLUMN H_NOTIFICACION.CUANDO_MOD
IS 'Fecha y hora de la última modificación de la notificación.';

CREATE SEQUENCE H_NOTIFICACION_SEQ;

ALTER TABLE H_NOTIFICACION
ADD CONSTRAINT H_NOTIFICACION_QUIEN_FK
FOREIGN KEY (QUIEN)
REFERENCES USUARIO (USU_ID);

ALTER TABLE H_NOTIFICACION
ADD CONSTRAINT H_NOTIFICACION_QUIEN_MOD_FK
FOREIGN KEY (QUIEN_MOD)
REFERENCES USUARIO (USU_ID);

ALTER TABLE H_NOTIFICACION
ADD CONSTRAINT H_NOTIFICACION_TNF_ID_FK
FOREIGN KEY (TNF_ID)
REFERENCES TIPO_NOTIFICACION (USU_ID);

CREATE INDEX H_NOTIFICACION_ACTIVO_IDX 
ON H_NOTIFICACION (ACTIVO);

-- -----------------------------------------------------------------------------
-- TRIGGER: TRG_REGISTRO_H_NOTIFICACION
-- -----------------------------------------------------------------------------

CREATE OR REPLACE TRIGGER 'TRG_REGISTRO_H_NOTIFICACION' 
AFTER INSERT OR UPDATE ON H_NOTIFICACION 
FOR EACH ROW
BEGIN
    INSERT INTO H_NOTIFICACION
    (
        HNT_ID,      
        TNF_ID,
        CLA_ID,     
        CUERPO,    
        ACTIVO,    
        QUIEN,     
        CUANDO,    
        QUIEN_MOD, 
        CUANDO_MOD
    ) VALUES (
        H_NOTIFICACION_SEQ.NEXTVAL,      
        :NEW.TNF_ID,
        :NEW.CLA_ID,
        :NEW.CUERPO,    
        :NEW.ACTIVO,    
        :NEW.QUIEN,     
        :NEW.CUANDO,    
        :NEW.QUIEN_MOD, 
        :NEW.CUANDO_MOD
    );
END;

insert into ROL (ROL_ID, QUIEN, CUANDO, QUIEN_MOD, CUANDO_MOD, ACTIVO, ROL_NOMBRE) values ('ADMIN_NOTIFICACIONES',3390, sysdate,3390,sysdate,1,'Administrar notificaciones');
COMMIT;