--
-- Author:  samuel.delgado@controltechcg.com
-- Created: 15/08/2018 Issue gogs #10 (SICDI-Controltech) feature-gogs-10
--

-- -----------------------------------------------------------------------------
-- TABLA: DESTINO_EXTERNO
-- -----------------------------------------------------------------------------

CREATE SEQUENCE DESTINO_EXTERNO_SEQ;

CREATE TABLE DESTINO_EXTERNO (
    ADE_ID              NUMBER(38)          NOT NULL,
    NOMBRE              VARCHAR2(128 CHAR)  NOT NULL,
    SIGLA               VARCHAR2(32 CHAR)   NOT NULL,
    QUIEN               NUMBER(38)          NOT NULL,
    ACTIVO              NUMBER(1)           NOT NULL,   
    CUANDO              DATE                NOT NULL,
    QUIEN_MOD           NUMBER(38)          NOT NULL,
    CUANDO_MOD          DATE                NOT NULL,
    PRIMARY KEY (ADE_ID)
);

ALTER TABLE DESTINO_EXTERNO
ADD CONSTRAINT DEST_EXT_QUIEN_FK
FOREIGN KEY (QUIEN)
REFERENCES USUARIO (USU_ID);

ALTER TABLE DESTINO_EXTERNO
ADD CONSTRAINT DEST_EXT_QUIEN_MOD_FK
FOREIGN KEY (QUIEN_MOD)
REFERENCES USUARIO (USU_ID);

CREATE INDEX DESTINO_EXTERNO_IDX 
ON DESTINO_EXTERNO (ACTIVO);

COMMENT ON COLUMN DESTINO_EXTERNO.ADE_ID
IS 'Código asignado al destino externo.';
COMMENT ON COLUMN DESTINO_EXTERNO.NOMBRE
IS 'Nombre asignado al destino externo.';
COMMENT ON COLUMN DESTINO_EXTERNO.SIGLA
IS 'Sigla asignado al destino externo.';
COMMENT ON COLUMN DESTINO_EXTERNO.ACTIVO
IS 'Indicador de actividad (0=activo;1=inactivo).';
COMMENT ON COLUMN DESTINO_EXTERNO.QUIEN
IS 'ID del usuario creador del destino externo.';
COMMENT ON COLUMN DESTINO_EXTERNO.CUANDO
IS 'Fecha y hora de creación del  destino externo.';
COMMENT ON COLUMN DESTINO_EXTERNO.QUIEN_MOD
IS 'ID del último usuario que modificó el  destino externo.';
COMMENT ON COLUMN DESTINO_EXTERNO.CUANDO_MOD
IS 'Fecha y hora de la última modificación el destino externo.';

-- -----------------------------------------------------------------------------
-- TABLA: H_DESTINO_EXTERNO
-- -----------------------------------------------------------------------------

CREATE SEQUENCE H_DESTINO_EXTERNO_SEQ;

CREATE TABLE H_DESTINO_EXTERNO (
    HADE_ID             NUMBER(38)          NOT NULL,
    ADE_ID              NUMBER(38)          NOT NULL,
    NOMBRE              VARCHAR2(128 CHAR)  NOT NULL,
    SIGLA               VARCHAR2(32 CHAR)   NOT NULL,
    QUIEN               NUMBER(38)          NOT NULL,
    ACTIVO              NUMBER(1)           NOT NULL,   
    CUANDO              DATE                NOT NULL,
    QUIEN_MOD           NUMBER(38)          NOT NULL,
    CUANDO_MOD          DATE                NOT NULL,
    PRIMARY KEY (HADE_ID)
);

-- -----------------------------------------------------------------------------
-- TABLA: TRG_REGISTRO_H_DESTINO_EXTERNO
-- -----------------------------------------------------------------------------

CREATE OR REPLACE TRIGGER TRG_REGISTRO_H_DEST_EXT
AFTER INSERT OR UPDATE ON DESTINO_EXTERNO 
FOR EACH ROW
BEGIN
    INSERT INTO H_DESTINO_EXTERNO
    (   HADE_ID,
        ADE_ID,
        NOMBRE,
        SIGLA,
        ACTIVO,    
        QUIEN,     
        CUANDO,    
        QUIEN_MOD, 
        CUANDO_MOD
    ) VALUES (
        H_DESTINO_EXTERNO_SEQ.NEXTVAL,
        :NEW.ADE_ID,
        :NEW.NOMBRE,
        :NEW.SIGLA,
        :NEW.ACTIVO,    
        :NEW.QUIEN,     
        :NEW.CUANDO,    
        :NEW.QUIEN_MOD, 
        :NEW.CUANDO_MOD
    );
END;

-- -----------------------------------------------------------------------------
-- TABLA: ROL
-- -----------------------------------------------------------------------------

INSERT INTO ROL 
    (ROL_ID, QUIEN, CUANDO, QUIEN_MOD, CUANDO_MOD, ACTIVO, ROL_NOMBRE) 
VALUES 
    ('ADMIN_DESTINO_EXTERNO',3390, SYSDATE,3390,SYSDATE,1,'Administrar destinos externos')
;

COMMIT;


-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
-- FUNCION: SUSTITUIR Y AGREGAR LAS MARCAS EXISTENTES EN LOS DOCUMENTOS.
-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------


ALTER TABLE DOCUMENTO ADD ADE_ID NUMBER(38) NULL

DECLARE
    CURSOR C_DOCUMENTOS IS
        SELECT DOCUMENTO.* 
        FROM DOCUMENTO, PROCESO_INSTANCIA 
        WHERE PROCESO_INSTANCIA.PRO_ID = 41
        AND MARCA_AGUA_EXTERNO is not null;
    aux_count NUMBER := 0;
    local_variable number(38);
    local_sequence number(38);
BEGIN
    FOR AUX_C_DOCUMENTOS IN C_DOCUMENTOS LOOP
    BEGIN
      SELECT DESTINO_EXTERNO.ADE_ID INTO local_variable 
      FROM DESTINO_EXTERNO where DESTINO_EXTERNO.SIGLA = UPPER(AUX_C_DOCUMENTOS.MARCA_AGUA_EXTERNO) AND ACTIVO = 1;
      exception
      when no_data_found then
        local_variable := null;
    END;
    IF (local_variable IS NULL)
    THEN
        local_sequence := DESTINO_EXTERNO_SEQ.NEXTVAL;
        INSERT INTO DESTINO_EXTERNO VALUES(local_sequence, UPPER(AUX_C_DOCUMENTOS.DOC_DEST_NOMBRE), UPPER(AUX_C_DOCUMENTOS.MARCA_AGUA_EXTERNO), 3390, 1, SYSDATE, 3390, SYSDATE);
        UPDATE DOCUMENTO SET  ADE_ID = local_sequence WHERE DOCUMENTO.DOC_ID = AUX_C_DOCUMENTOS.DOC_ID;
    ELSE
        UPDATE DOCUMENTO SET  ADE_ID = local_variable WHERE DOCUMENTO.DOC_ID = AUX_C_DOCUMENTOS.DOC_ID;
    END IF;
    END LOOP;
    COMMIT;
END;