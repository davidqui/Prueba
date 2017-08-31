create or replace FUNCTION       "FN_DOCUMENTO_RADICADO" (p_dep_id number) return varchar2 as
  v_ano varchar2(4);
  v_dep DEPENDENCIA.DEP_CODIGO%TYPE;
  v_seq varchar2(21);
  v_dig varchar2(1);
  v_rad varchar2(32);
begin
 
  select to_char(sysdate, 'YYYY')
    into v_ano
    from dual;
 
  select dep_codigo
    into v_dep
    from dependencia
   where dep_id = p_dep_id;
  --
  -- 2017-05-05 jgarcia@controltechcg.com Issue #75 (SICDI-Controltech):
  -- Temporalmente se aumenta la presentación del número de documento
  -- radicado a 5 caracteres.
  -- 2017-07-14 jgarcia@controltechcg.com Issue #76 (SICDI-Controltech):
  -- Aplicación temporal a 7 caracteres, pendiente desarrollo reinicio.
  --
  select lpad(to_char(DOCUMENTO_RADICADO.nextval), 7, '0')
    into v_seq
    from dual;
 
  v_dig := 2;
 
  v_rad := v_ano || '-' || v_dep || '-' || v_seq || '-' || v_dig;
 
  return v_rad;
end fn_documento_radicado;