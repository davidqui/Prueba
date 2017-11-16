create or replace FUNCTION "FN_GENERA_NUM_RADICADO" (
    p_dep_id    NUMBER,
    p_rad_id    NUMBER
) return varchar2 as
    v_ano     varchar2(4);
    v_dep     DEPENDENCIA.DEP_CODIGO%TYPE;
    v_seqn    varchar2(32);
    v_seq     varchar2(21);
    v_dig     varchar2(1);
    v_rad     varchar2(32);
    v_select  varchar2(100);
begin
    select to_char(sysdate, 'YYYY')
    into v_ano
    from dual;
 
    select dep_codigo
    into v_dep
    from dependencia
    where dep_id = p_dep_id;
    
    -- 2017-05-05 jgarcia@controltechcg.com Issue #75 (SICDI-Controltech):
    -- Temporalmente se aumenta la presentación del número de documento
    -- radicado a 5 caracteres.
    -- 2017-07-14 jgarcia@controltechcg.com Issue #76 (SICDI-Controltech):
    -- Aplicación temporal a 7 caracteres, pendiente desarrollo reinicio.
    -- 2017-11-14 edison.gonzalez@controltechcg.com Issue #138 (SICDI-Controltech):
    -- Se realiza la independencia de la secuencia del radicado, dependiendo del tipo
    -- de radicado.

    select a.rad_indicativo, b.seq_nombre
    into v_dig, v_seqn
    from RADICACION a,
         SECUENCIA_RADICACION b
    where b.seq_id = a.secuencia
    and a.rad_id = p_rad_id;

    v_select := 'select lpad(to_char('||v_seqn||'.nextval), 7, ''0'') from dual';
    execute immediate v_select into v_seq;

    -- 2017-11-14 edison.gonzalez@controltechcg.com Issue #138 (SICDI-Controltech):
    -- Se realiza la parametrizacion del indicativo segun el id del proceso.
  
    v_rad := v_ano || '-' || v_dep || '-' || v_seq || '-' || v_dig;
 
    return v_rad;
end FN_GENERA_NUM_RADICADO;