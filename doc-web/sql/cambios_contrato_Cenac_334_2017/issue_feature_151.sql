-- -----------------------------------------------------------------------------
-- 2018-02-22 edison.gonzalez@controltechcg.com Feature #149 (SICDI-Controntech)
-- feature-151
-- Implementacion de multiples cargos.
-- -----------------------------------------------------------------------------

-- -----------------------------------------------------------------------------
-- TABLA: DOCUMENTO
-- -----------------------------------------------------------------------------

ALTER TABLE DOC.DOCUMENTO add CARGO_ID_ELABORA NUMBER;
ALTER TABLE DOC.DOCUMENTO add CARGO_ID_FIRMA NUMBER;

COMMENT ON COLUMN DOC.DOCUMENTO.CARGO_ID_ELABORA    IS 'Identificador del cargo del usuario quien elabora el documento';
COMMENT ON COLUMN DOC.DOCUMENTO.CARGO_ID_FIRMA      IS 'Descripción del cargo del usuario quien firma el documento.';

ALTER TABLE DOC.DOCUMENTO ADD CONSTRAINT DOCUMENTO_FK1 FOREIGN KEY (CARGO_ID_ELABORA) REFERENCES CARGO (CAR_ID);
ALTER TABLE DOC.DOCUMENTO ADD CONSTRAINT DOCUMENTO_FK2 FOREIGN KEY (CARGO_ID_FIRMA) REFERENCES CARGO (CAR_ID);

CREATE INDEX DOCUMENTO_IDX_8 ON DOC.DOCUMENTO(CARGO_ID_ELABORA);
CREATE INDEX DOCUMENTO_IDX_9 ON DOC.DOCUMENTO(CARGO_ID_FIRMA);

-- -----------------------------------------------------------------------------
-- TABLA: DOCUMENTO_DEPENDENCIA
-- -----------------------------------------------------------------------------

ALTER TABLE DOC.DOCUMENTO_DEPENDENCIA add CARGO_ID NUMBER;

COMMENT ON COLUMN DOC.DOCUMENTO_DEPENDENCIA.CARGO_ID    IS 'Identificador del cargo del usuario quien tiene el documento en el archivo';

ALTER TABLE DOC.DOCUMENTO_DEPENDENCIA ADD CONSTRAINT DOCUMENTO_DEPENDENCIA_FK2 FOREIGN KEY (CARGO_ID) REFERENCES CARGO (CAR_ID);

CREATE INDEX DOCUMENTO_DEPENDENCIA_IDX_1 ON DOC.DOCUMENTO_DEPENDENCIA(CARGO_ID);

-- -----------------------------------------------------------------------------
-- TABLA: TRANSFERENCIA_ARCHIVO
-- -----------------------------------------------------------------------------

ALTER TABLE DOC.TRANSFERENCIA_ARCHIVO add USU_CREADOR_CARGO NUMBER;
ALTER TABLE DOC.TRANSFERENCIA_ARCHIVO add USU_ORIGEN_CARGO NUMBER;
ALTER TABLE DOC.TRANSFERENCIA_ARCHIVO add USU_DESTINO_CARGO NUMBER;

COMMENT ON COLUMN DOC.TRANSFERENCIA_ARCHIVO.USU_CREADOR_CARGO   IS 'Identificador del cargo del usuario quien crea la transferencia del archivo';
COMMENT ON COLUMN DOC.TRANSFERENCIA_ARCHIVO.USU_ORIGEN_CARGO    IS 'Identificador del cargo del usuario origen de la transferencia del archivo';
COMMENT ON COLUMN DOC.TRANSFERENCIA_ARCHIVO.USU_DESTINO_CARGO   IS 'Identificador del cargo del usuario destino a quien va la transferencia del archivo';

ALTER TABLE DOC.TRANSFERENCIA_ARCHIVO ADD CONSTRAINT TRANSFERENCIA_ARCHIVO_FK1 FOREIGN KEY (USU_CREADOR_CARGO) REFERENCES CARGO (CAR_ID);
ALTER TABLE DOC.TRANSFERENCIA_ARCHIVO ADD CONSTRAINT TRANSFERENCIA_ARCHIVO_FK2 FOREIGN KEY (USU_ORIGEN_CARGO) REFERENCES CARGO (CAR_ID);
ALTER TABLE DOC.TRANSFERENCIA_ARCHIVO ADD CONSTRAINT TRANSFERENCIA_ARCHIVO_FK3 FOREIGN KEY (USU_DESTINO_CARGO) REFERENCES CARGO (CAR_ID);

CREATE INDEX TRANSFERENCIA_ARCHIVO_IDX_1 ON DOC.TRANSFERENCIA_ARCHIVO(USU_CREADOR_CARGO);
CREATE INDEX TRANSFERENCIA_ARCHIVO_IDX_2 ON DOC.TRANSFERENCIA_ARCHIVO(USU_ORIGEN_CARGO);
CREATE INDEX TRANSFERENCIA_ARCHIVO_IDX_3 ON DOC.TRANSFERENCIA_ARCHIVO(USU_DESTINO_CARGO);

ALTER TABLE TRANSFERENCIA_ARCHIVO MODIFY CREADOR_USU_CARGO NULL;
ALTER TABLE TRANSFERENCIA_ARCHIVO MODIFY ORIGEN_USU_CARGO NULL;
ALTER TABLE TRANSFERENCIA_ARCHIVO MODIFY DESTINO_USU_CARGO NULL;

-- -----------------------------------------------------------------------------
-- TABLA: DOCUMENTO_USU_ELABORA
-- -----------------------------------------------------------------------------

ALTER TABLE DOC.DOCUMENTO_USU_ELABORA add CARGO_ID_ELABORA NUMBER;

COMMENT ON COLUMN DOC.DOCUMENTO_USU_ELABORA.CARGO_ID_ELABORA    IS 'Identificador del cargo del usuario quien elabora el documento';

ALTER TABLE DOC.DOCUMENTO_USU_ELABORA ADD CONSTRAINT DOCUMENTO_USU_ELABORA_FK1 FOREIGN KEY (CARGO_ID_ELABORA) REFERENCES CARGO (CAR_ID);

CREATE INDEX DOCUMENTO_USU_ELABORA_IDX_1 ON DOC.DOCUMENTO_USU_ELABORA(CARGO_ID_ELABORA);

-- -----------------------------------------------------------------------------
-- TABLA: DOCUMENTO_USU_FIRMA
-- -----------------------------------------------------------------------------

ALTER TABLE DOC.DOCUMENTO_USU_FIRMA add CARGO_ID_FIRMA NUMBER;

COMMENT ON COLUMN DOC.DOCUMENTO_USU_FIRMA.CARGO_ID_FIRMA   IS 'Identificador del cargo del usuario quien firma el documento';

ALTER TABLE DOC.DOCUMENTO_USU_FIRMA ADD CONSTRAINT DOCUMENTO_USU_FIRMA_FK1 FOREIGN KEY (CARGO_ID_FIRMA) REFERENCES CARGO (CAR_ID);

CREATE INDEX DOCUMENTO_USU_FIRMA_IDX_1 ON DOC.DOCUMENTO_USU_FIRMA(CARGO_ID_FIRMA);

-- -----------------------------------------------------------------------------
-- TABLA: USUARIO
-- -----------------------------------------------------------------------------

ALTER TABLE USUARIO DROP COLUMN USU_CARGO;