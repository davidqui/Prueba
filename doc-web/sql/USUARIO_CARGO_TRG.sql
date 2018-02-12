-- 
-- 2018-02-09 edison.gonzalez@controltechcg.com Issue #149 (SICDI-Controltechcg).
-- Creación del trigger para guardar los cambios que se han registrado en las columnas
-- de cargo de la tabla USUARIO.

set SERVEROUTPUT ON

CREATE OR REPLACE TRIGGER USUARIO_CARGO_TRG AFTER INSERT OR UPDATE ON USUARIO
FOR EACH ROW
BEGIN
    IF (nvl(:old.USU_CARGO_PRINCIPAL_ID,-1) != nvl(:new.USU_CARGO_PRINCIPAL_ID,-1)) or  (nvl(:old.USU_CARGO1_ID,-1) != nvl(:new.USU_CARGO1_ID,-1)) or
    (nvl(:old.USU_CARGO2_ID,-1) != nvl(:new.USU_CARGO2_ID,-1)) or (nvl(:old.USU_CARGO3_ID,-1) != nvl(:new.USU_CARGO3_ID,-1)) or 
    (nvl(:old.USU_CARGO4_ID,-1) != nvl(:new.USU_CARGO4_ID,-1)) or (nvl(:old.USU_CARGO5_ID,-1) != nvl(:new.USU_CARGO5_ID,-1)) or
    (nvl(:old.USU_CARGO6_ID,-1) != nvl(:new.USU_CARGO6_ID,-1)) or (nvl(:old.USU_CARGO7_ID,-1) != nvl(:new.USU_CARGO7_ID,-1)) or
    (nvl(:old.USU_CARGO8_ID,-1) != nvl(:new.USU_CARGO8_ID,-1)) or (nvl(:old.USU_CARGO9_ID,-1) != nvl(:new.USU_CARGO9_ID,-1)) or 
    (nvl(:old.USU_CARGO9_ID,-1) != nvl(:new.USU_CARGO9_ID,-1)) THEN
        begin
            INSERT INTO USUARIO_CARGO_HISTORICO (
                USU_CAR_HIS_ID,
                USU_ID,
                CARGO_PRINCIPAL_ANTERIOR,
                CARGO_PRINCIPAL_NUEVO,
                CARGO1_ID_ANTERIOR,
                CARGO1_ID_NUEVO,
                CARGO2_ID_ANTERIOR,
                CARGO2_ID_NUEVO,
                CARGO3_ID_ANTERIOR,
                CARGO3_ID_NUEVO,
                CARGO4_ID_ANTERIOR,
                CARGO4_ID_NUEVO,
                CARGO5_ID_ANTERIOR,
                CARGO5_ID_NUEVO,
                CARGO6_ID_ANTERIOR,
                CARGO6_ID_NUEVO,
                CARGO7_ID_ANTERIOR,
                CARGO7_ID_NUEVO,
                CARGO8_ID_ANTERIOR,
                CARGO8_ID_NUEVO,
                CARGO9_ID_ANTERIOR,
                CARGO9_ID_NUEVO,
                CARGO10_ID_ANTERIOR,
                CARGO10_ID_NUEVO,                
                CAR_HIS_FEC_MOD,
                USU_CAR_HIS_USU_MOD
            ) VALUES (
                USUARIO_CARGO_HISTORICO_SEQ.nextval,
                :new.usu_id,
                :old.USU_CARGO_PRINCIPAL_ID,
                :new.USU_CARGO_PRINCIPAL_ID,
                :old.USU_CARGO1_ID,
                :new.USU_CARGO1_ID,
                :old.USU_CARGO2_ID,
                :new.USU_CARGO2_ID,
                :old.USU_CARGO3_ID,
                :new.USU_CARGO3_ID,
                :old.USU_CARGO4_ID,
                :new.USU_CARGO4_ID,
                :old.USU_CARGO5_ID,
                :new.USU_CARGO5_ID,
                :old.USU_CARGO6_ID,
                :new.USU_CARGO6_ID,
                :old.USU_CARGO7_ID,
                :new.USU_CARGO7_ID,
                :old.USU_CARGO8_ID,
                :new.USU_CARGO8_ID,
                :old.USU_CARGO9_ID,
                :new.USU_CARGO9_ID,
                :old.USU_CARGO10_ID,
                :new.USU_CARGO10_ID,
                :new.CUANDO_MOD,
                :new.QUIEN_MOD
            );       
        exception when others then
            NULL;
        end;
    END IF;
END;
/