--
-- Author:  samuel.delgado@controltechcg.com
-- Created: 15/08/2018 Issue gogs #10 (SICDI-Controltech) feature-gogs-10
--

-- -----------------------------------------------------------------------------
-- TABLA: DESTINO_EXTERNO
-- -----------------------------------------------------------------------------

CREATE SEQUENCE DOC.DESTINO_EXTERNO_SEQ;

CREATE TABLE DOC.DESTINO_EXTERNO (
    ADE_ID              NUMBER(38)          NOT NULL,
    NOMBRE              VARCHAR2(128 CHAR)  NOT NULL,
    SIGLA               VARCHAR2(128 CHAR)   NOT NULL,
    QUIEN               NUMBER(38)          NOT NULL,
    ACTIVO              NUMBER(1)           NOT NULL,   
    CUANDO              DATE                NOT NULL,
    QUIEN_MOD           NUMBER(38)          NOT NULL,
    CUANDO_MOD          DATE                NOT NULL,
    TIPO                NUMBER(1),
    PRIMARY KEY (ADE_ID)
);

ALTER TABLE DOC.DESTINO_EXTERNO
ADD CONSTRAINT DEST_EXT_QUIEN_FK
FOREIGN KEY (QUIEN)
REFERENCES DOC.USUARIO (USU_ID);

ALTER TABLE DOC.DESTINO_EXTERNO
ADD CONSTRAINT DEST_EXT_QUIEN_MOD_FK
FOREIGN KEY (QUIEN_MOD)
REFERENCES DOC.USUARIO (USU_ID);

CREATE INDEX DESTINO_EXTERNO_IDX 
ON DOC.DESTINO_EXTERNO (ACTIVO);

COMMENT ON COLUMN DOC.DESTINO_EXTERNO.ADE_ID
IS 'Código asignado al destino externo.';
COMMENT ON COLUMN DOC.DESTINO_EXTERNO.NOMBRE
IS 'Nombre asignado al destino externo.';
COMMENT ON COLUMN DOC.DESTINO_EXTERNO.SIGLA
IS 'Sigla asignado al destino externo.';
COMMENT ON COLUMN DOC.DESTINO_EXTERNO.ACTIVO
IS 'Indicador de actividad (0=activo;1=inactivo).';
COMMENT ON COLUMN DOC.DESTINO_EXTERNO.QUIEN
IS 'ID del usuario creador del destino externo.';
COMMENT ON COLUMN DOC.DESTINO_EXTERNO.CUANDO
IS 'Fecha y hora de creación del  destino externo.';
COMMENT ON COLUMN DOC.DESTINO_EXTERNO.QUIEN_MOD
IS 'ID del último usuario que modificó el  destino externo.';
COMMENT ON COLUMN DOC.DESTINO_EXTERNO.CUANDO_MOD
IS 'Fecha y hora de la última modificación el destino externo.';

-- -----------------------------------------------------------------------------
-- TABLA: H_DESTINO_EXTERNO
-- -----------------------------------------------------------------------------

CREATE SEQUENCE DOC.H_DESTINO_EXTERNO_SEQ;

CREATE TABLE DOC.H_DESTINO_EXTERNO (
    HADE_ID             NUMBER(38)          NOT NULL,
    ADE_ID              NUMBER(38)          NOT NULL,
    NOMBRE              VARCHAR2(128 CHAR)  NOT NULL,
    SIGLA               VARCHAR2(128 CHAR)   NOT NULL,
    QUIEN               NUMBER(38)          NOT NULL,
    ACTIVO              NUMBER(1)           NOT NULL,   
    CUANDO              DATE                NOT NULL,
    QUIEN_MOD           NUMBER(38)          NOT NULL,
    CUANDO_MOD          DATE                NOT NULL,
    TIPO                NUMBER(1),
    PRIMARY KEY (HADE_ID)
);

-- -----------------------------------------------------------------------------
-- TABLA: TRG_REGISTRO_H_DESTINO_EXTERNO
-- -----------------------------------------------------------------------------
SET DEFINE OFF
CREATE OR REPLACE TRIGGER DOC.TRG_REGISTRO_H_DEST_EXT
AFTER INSERT OR UPDATE ON DOC.DESTINO_EXTERNO 
FOR EACH ROW
BEGIN
    INSERT INTO DOC.H_DESTINO_EXTERNO
    (   HADE_ID,
        ADE_ID,
        NOMBRE,
        SIGLA,
        ACTIVO,    
        QUIEN,     
        CUANDO,    
        QUIEN_MOD, 
        CUANDO_MOD,
        TIPO
    ) VALUES (
        DOC.H_DESTINO_EXTERNO_SEQ.NEXTVAL,
        :NEW.ADE_ID,
        :NEW.NOMBRE,
        :NEW.SIGLA,
        :NEW.ACTIVO,    
        :NEW.QUIEN,     
        :NEW.CUANDO,    
        :NEW.QUIEN_MOD, 
        :NEW.CUANDO_MOD,
        :NEW.TIPO
    );
END;
/

-- -----------------------------------------------------------------------------
-- TABLA: ROL
-- -----------------------------------------------------------------------------

INSERT INTO DOC.ROL 
    (ROL_ID, QUIEN, CUANDO, QUIEN_MOD, CUANDO_MOD, ACTIVO, ROL_NOMBRE) 
VALUES 
    ('ADMIN_DESTINO_EXTERNO',3390, SYSDATE,3390,SYSDATE,1,'Administrar destinos externos');

COMMIT;

-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
-- FUNCION: SUSTITUIR Y AGREGAR LAS MARCAS EXISTENTES EN LOS DOCUMENTOS.
-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------

ALTER TABLE DOC.DOCUMENTO ADD ADE_ID NUMBER(38) NULL;
/

DECLARE
    CURSOR C_DOCUMENTOS IS
        select DOC.documento.*
        from DOC.documento
        JOIN DOC.PROCESO_INSTANCIA ON (DOC.documento.PIN_ID = DOC.PROCESO_INSTANCIA.PIN_ID AND DOC.PROCESO_INSTANCIA.PRO_ID = 41)
        where MARCA_AGUA_EXTERNO is not null
        AND ADE_ID is null;
    aux_count NUMBER := 0;
    local_variable number(38);
    local_sequence number(38);
BEGIN
    FOR AUX_C_DOCUMENTOS IN C_DOCUMENTOS LOOP
        BEGIN
          SELECT DOC.DESTINO_EXTERNO.ADE_ID INTO local_variable 
          FROM DOC.DESTINO_EXTERNO where DOC.DESTINO_EXTERNO.SIGLA = TRIM(UPPER(AUX_C_DOCUMENTOS.MARCA_AGUA_EXTERNO));
          UPDATE DOC.DOCUMENTO SET ADE_ID = local_variable WHERE DOC.DOCUMENTO.DOC_ID = AUX_C_DOCUMENTOS.DOC_ID;
          exception
          when no_data_found then
            local_sequence := DESTINO_EXTERNO_SEQ.NEXTVAL;
            INSERT INTO DOC.DESTINO_EXTERNO VALUES(local_sequence, TRIM(UPPER(AUX_C_DOCUMENTOS.MARCA_AGUA_EXTERNO)), TRIM(UPPER(AUX_C_DOCUMENTOS.MARCA_AGUA_EXTERNO)), 3390, 0, SYSDATE, 3390, SYSDATE);
            UPDATE DOC.DOCUMENTO SET  ADE_ID = local_sequence WHERE DOC.DOCUMENTO.DOC_ID = AUX_C_DOCUMENTOS.DOC_ID;
        END;
    END LOOP;
    COMMIT;
END;
/

-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
-- MEJORAS A LA CONSULTA
-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------

DELETE FROM DOC.S_INSTANCIA_USUARIO
WHERE NOT EXISTS(
    SELECT 1
    FROM DOC.USUARIO
    WHERE S_INSTANCIA_USUARIO.USU_ID = DOC.USUARIO.USU_ID
);

DELETE FROM DOC.S_INSTANCIA_USUARIO
WHERE NOT EXISTS(
    SELECT 1
    FROM DOC.PROCESO_INSTANCIA
    WHERE DOC.S_INSTANCIA_USUARIO.PIN_ID = DOC.PROCESO_INSTANCIA.PIN_ID
);

ALTER TABLE DOC.S_INSTANCIA_USUARIO
ADD CONSTRAINT S_INSTANCIA_USUARIO_FK
FOREIGN KEY (USU_ID)
REFERENCES DOC.USUARIO (USU_ID);


ALTER TABLE DOC.S_INSTANCIA_USUARIO
ADD CONSTRAINT S_INSTANCIA_PINSTANCIA_FK
FOREIGN KEY (PIN_ID)
REFERENCES DOC.PROCESO_INSTANCIA (PIN_ID);

ALTER TABLE DOC.DOCUMENTO ADD CONSTRAINT DOCUMENTO_USUFIRMA_FK FOREIGN KEY (USU_ID_FIRMA) REFERENCES DOC.USUARIO(USU_ID);

ALTER TABLE DOC.DOCUMENTO ADD CONSTRAINT DOCUMENTO_USUELABORA_FK FOREIGN KEY (USU_ID_ELABORA) REFERENCES DOC.USUARIO(USU_ID);

ALTER TABLE DOC.DOCUMENTO ADD CONSTRAINT DOCUMENTO_USUVISTOBUENO_FK FOREIGN KEY (USU_ID_VISTO_BUENO) REFERENCES DOC.USUARIO(USU_ID);

ALTER TABLE DOC.DOCUMENTO ADD CONSTRAINT DOCUMENTO_USUAPRUEBA_FK FOREIGN KEY (USU_ID_APRUEBA) REFERENCES DOC.USUARIO(USU_ID);

ALTER TABLE DOC.DOCUMENTO ADD CONSTRAINT DOCUMENTO_QUIEN_FK FOREIGN KEY (QUIEN) REFERENCES DOC.USUARIO(USU_ID);

ALTER TABLE DOC.DOCUMENTO ADD CONSTRAINT DOCUMENTO_QUIENMOD_FK FOREIGN KEY (QUIEN_MOD) REFERENCES DOC.USUARIO(USU_ID);

CREATE INDEX "DOC"."DOCUMENTO_QUIEN_INDEX" ON "DOC"."DOCUMENTO" ("QUIEN");

CREATE INDEX "DOC"."DOCUMENTO_QUIENMOD_INDEX" ON "DOC"."DOCUMENTO" ("QUIEN_MOD");

