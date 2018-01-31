-- -----------------------------------------------------------------------------
-- 2018-01-30 edison.gonzalez@controltechcg.com Feature #147 (SICDI-Controntech)
-- feature-147
-- Organizacion de las unidades jerarquicas.
-- -----------------------------------------------------------------------------

-- -----------------------------------------------------------------------------
-- TABLA: DEPENDENCIA
-- -----------------------------------------------------------------------------

--DEP_IND_ENVIO_DOCUMNETOS

ALTER TABLE DEPENDENCIA ADD DEP_IND_ENVIO_DOCUMENTOS NUMBER(1) DEFAULT 0;

CREATE INDEX DEPENDENCIA_IDX2 ON DEPENDENCIA(DEP_IND_ENVIO_DOCUMENTOS);

--Actualizacion de dependencias cuya sigla esta erronea

UPDATE DEPENDENCIA SET DEP_SIGLA = NULL WHERE DEP_ID = 715;
UPDATE DEPENDENCIA SET DEP_SIGLA = NULL WHERE DEP_ID = 382;
UPDATE DEPENDENCIA SET DEP_SIGLA = NULL WHERE DEP_ID = 391;
UPDATE DEPENDENCIA SET DEP_SIGLA = NULL WHERE DEP_ID = 380;
UPDATE DEPENDENCIA SET DEP_SIGLA = NULL WHERE DEP_ID = 379;
UPDATE DEPENDENCIA SET DEP_SIGLA = NULL WHERE DEP_ID = 392;
UPDATE DEPENDENCIA SET DEP_SIGLA = NULL WHERE DEP_ID = 395;
UPDATE DEPENDENCIA SET DEP_SIGLA = NULL WHERE DEP_ID = 735;
UPDATE DEPENDENCIA SET DEP_SIGLA = NULL WHERE DEP_ID = 759;
COMMIT;
/

declare
    cursor c_dependencias is 
        select 'CAIMI' dep_sigla
        from dual
        union
        select 'CACIM' dep_sigla
        from dual
        union
        select 'CEDE2' dep_sigla
        from dual
        union
        select 'CI3MO1' dep_sigla
        from dual
        union
        select 'CI3MO2' dep_sigla
        from dual
        union
        select 'CI3MO3' dep_sigla
        from dual
        union
        select 'CI3MO4' dep_sigla
        from dual
        union
        select 'CI3MO5' dep_sigla
        from dual
        union
        select 'CI3MO6' dep_sigla
        from dual
        union
        select 'CI3MO7' dep_sigla
        from dual
        union
        select 'CI3MO8' dep_sigla
        from dual
        union
        select 'BAIMI1' dep_sigla
        from dual
        union
        select 'BAIMI2' dep_sigla
        from dual
        union
        select 'BAIMI3' dep_sigla
        from dual
        union
        select 'BAIMI4' dep_sigla
        from dual
        union
        select 'BAIMI5' dep_sigla
        from dual
        union
        select 'BAIMI6' dep_sigla
        from dual
        union
        select 'BAIMI7' dep_sigla
        from dual
        union
        select 'BAIMI8' dep_sigla
        from dual
        union
        select 'BAGCI' dep_sigla
        from dual
        union
        select 'BASIM' dep_sigla
        from dual
        union
        select 'BAIME1' dep_sigla
        from dual
        union
        select 'BAIME2' dep_sigla
        from dual
        union
        select 'BAIME3' dep_sigla
        from dual
        union
        select 'BAIME4' dep_sigla
        from dual
        union
        select 'BAIME5' dep_sigla
        from dual
        union
        select 'BINGE' dep_sigla
        from dual
        union
        select 'BIGAM' dep_sigla
        from dual
        union
        select 'BACIB' dep_sigla
        from dual
        union
        select 'BAIDI' dep_sigla
        from dual
        union
        select 'ESICI' dep_sigla
        from dual
        union
        select 'BRIMI1' dep_sigla
        from dual
        union
        select 'BRIMI2' dep_sigla
        from dual
        union
        select 'BINSE' dep_sigla
        from dual
        union
        select 'CENAC' dep_sigla
        from dual
        union
        select 'BRCIM2' dep_sigla
        from dual
        union
        select 'BRCIM1' dep_sigla
        from dual
        union
        select 'BASMI1' dep_sigla
        from dual
        union
        select 'BASMI2' dep_sigla
        from dual
        union
        select 'BASMI3' dep_sigla
        from dual
        union
        select 'BASMI4' dep_sigla
        from dual
        union
        select 'BASMI5' dep_sigla
        from dual
        union
        select 'BASEC5' dep_sigla
        from dual
        union
        select 'BAGOP' dep_sigla
        from dual
        union
        select 'BACCE' dep_sigla
        from dual
        union
        select 'BACIM1' dep_sigla
        from dual
        union
        select 'BACIM2' dep_sigla
        from dual
        union
        select 'BACIM3' dep_sigla
        from dual
        union
        select 'BACIM4' dep_sigla
        from dual
        union
        select 'BACIM5' dep_sigla
        from dual
        union
        select 'BACIM6' dep_sigla
        from dual
        union
        select 'BACIM7' dep_sigla
        from dual
        union
        select 'BACIM8' dep_sigla
        from dual
        union
        select 'BACIM9' dep_sigla
        from dual
        union
        select 'BASCI' dep_sigla
        from dual
        union
        select 'BACSI' dep_sigla
        from dual
        union
        select 'BACIF5' dep_sigla
        from dual
        union
        select 'BASEC1' dep_sigla
        from dual
        union
        select 'BASEC2' dep_sigla
        from dual
        union
        select 'BASEC3' dep_sigla
        from dual
        union
        select 'BASEC4' dep_sigla
        from dual;
begin
    for aux_c_dependencias in c_dependencias loop
        update dependencia
        set DEP_IND_ENVIO_DOCUMENTOS = 1
        where dep_sigla = aux_c_dependencias.dep_sigla;
    end loop;
    COMMIT;
end;
/

update DEPENDENCIA
set dep_padre = dep_padre_organico;
COMMIT;
/