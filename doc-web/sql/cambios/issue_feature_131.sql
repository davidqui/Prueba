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
        select 'GR' grado, 100000 orden from dual union
        select 'MG' grado, 95000 orden from dual union
        select 'BG' grado, 94000 orden from dual union
        select 'CR' grado, 93000 orden from dual union
        select 'TC' grado, 92000 orden from dual union
        select 'MY' grado, 91000 orden from dual union
        select 'CT' grado, 90000 orden from dual union
        select 'TE' grado, 89000 orden from dual union
        select 'ST' grado, 88000 orden from dual union
        select 'SMC' grado, 87000 orden from dual union
        select 'SM' grado,  86000 orden from dual union
        select 'SP' grado,  85000 orden from dual union
        select 'SV' grado,  84000 orden from dual union
        select 'SS' grado,  83000 orden from dual union
        select 'CP' grado,  82000 orden from dual union
        select 'CS' grado,  81000 orden from dual union
        select 'C3' grado,  80000 orden from dual union
        select 'DG' grado,  79000 orden from dual union
        select 'SLP' grado, 78000 orden from dual union
        select 'SLR' grado, 77000 orden from dual union
        select 'SLC' grado, 76000 orden from dual union
        select 'SLB' grado, 75000 orden from dual union
        select 'PD4' grado, 74000 orden from dual union
        select 'AI22' grado,73000 orden from dual union
        select 'AI21' grado,72000 orden from dual union
        select 'AI20' grado,71000 orden from dual union
        select 'AI19' grado,70000 orden from dual union
        select 'AI18' grado,69000 orden from dual union
        select 'AI17' grado,68000 orden from dual union
        select 'AI16' grado,67000 orden from dual union
        select 'AI15' grado,66000 orden from dual union
        select 'AI14' grado,65000 orden from dual union
        select 'AI13' grado,64000 orden from dual union
        select 'AI12' grado,63000 orden from dual union
        select 'AI11' grado,62000 orden from dual union
        select 'AI10' grado,61000 orden from dual union
        select 'AI09' grado,60000 orden from dual union
        select 'AI08' grado,59000 orden from dual;
        
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

update grado set GRA_PESO_ORDEN = 0 where GRA_PESO_ORDEN is null;
commit;