-- -----------------------------------------------------------------------------
-- 2017-10-05 edison.gonzalez@controltechcg.com Feature #131 (SICDI-Controntech)
-- feature-131
-- peso del grado.
-- -----------------------------------------------------------------------------

-- -----------------------------------------------------------------------------
-- 2017-10-05 edison.gonzalez@controltechcg.com Feature #131 (SICDI-Controntech)
-- feature-131
-- Alteracion de tabla GRADO para agregar el nuevo campo del peso.
-- -----------------------------------------------------------------------------

-- -----------------------------------------------------------------------------
-- ALTERACION DE LA TABLA: GRADO
-- -----------------------------------------------------------------------------
alter table GRADO add GRA_PESO_ORDEN NUMBER;

-- INDEX

CREATE INDEX GRADO_PESO_IDX ON GRADO(GRA_PESO_ORDEN);

-- DATOS

declare
    existe  number;
    cursor c_grado is
        select 'OPS' grado, 1000  orden from dual union
        select 'AI' grado, 2000 orden from dual union
        select 'SLB' grado, 3000 orden from dual union
        select 'SLC' grado, 4000 orden from dual union
        select 'SLR' grado, 5000 orden from dual union
        select 'SLP' grado, 6000 orden from dual union
        select 'DG' grado, 7000 orden from dual union
        select 'C3' grado, 8000 orden from dual union
        select 'CS' grado, 9000 orden from dual union
        select 'CP' grado, 10000 orden from dual union
        select 'SS' grado, 11000 orden from dual union
        select 'SV' grado, 12000 orden from dual union
        select 'SP' grado, 13000 orden from dual union
        select 'SM' grado, 14000 orden from dual union
        select 'SMC' grado, 15000 orden from dual union
        select 'SMCE' grado, 16000 orden from dual union
        select 'ST' grado, 17000 orden from dual union
        select 'TE' grado, 18000 orden from dual union
        select 'CT' grado, 19000 orden from dual union
        select 'MY' grado, 20000 orden from dual union
        select 'TC' grado, 21000 orden from dual union
        select 'CR' grado, 22000 orden from dual union
        select 'BG' grado, 23000 orden from dual union
        select 'MG' grado, 24000 orden from dual union
        select 'TG' grado, 25000 orden from dual union
        select 'GR' grado, 26000 orden from dual;
begin
    for aux_c_grado in c_grado loop
        if aux_c_grado.grado like 'AI%' then
            update grado set GRA_PESO_ORDEN = aux_c_grado.orden where GRA_ID like aux_c_grado.grado;
        else
            update grado set GRA_PESO_ORDEN = aux_c_grado.orden where GRA_ID = aux_c_grado.grado;
        end if;
        commit;
    end loop;
end;
/

update grado set GRA_PESO_ORDEN = 0 where ACTIVO = 0;
update grado set GRA_PESO_ORDEN = 0 where GRA_PESO_ORDEN is null;
commit;