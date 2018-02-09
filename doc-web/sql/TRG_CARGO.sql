-- 
-- 2018-02-09 edison.gonzalez@controltechcg.com Issue #149 (SICDI-Controltechcg).
-- Creación del trigger para guardar los cambios que se han registrado en la columna
-- CAR_NOMBRE de la tabla CARGO.

CREATE OR REPLACE TRIGGER CARGO_TRG AFTER UPDATE ON CARGO
FOR EACH ROW
BEGIN
    IF :old.CAR_NOMBRE != :new.CAR_NOMBRE THEN
        begin
            INSERT INTO cargo_historico (
                car_his_id,
                car_id,
                car_nombre_anterior,
                car_his_fec_mod,
                car_his_usu_mod
            ) VALUES (
                CARGO_HISTORICO_SEQ.nextval,
                :new.CAR_ID,
                :old.CAR_NOMBRE,
                :new.CUANDO_MOD,
                :new.quien_mod
            );        
        exception when others then
            NULL;
        end;
    END IF;
END;
/