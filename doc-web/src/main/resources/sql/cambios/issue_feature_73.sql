-- ----------------------------------------------------------------------
-- 2017-05-19 jgarcia@controltechcg.com Issue #73 (SICDI-Controntech)
-- feature-73
-- Modificación de transiciones y estados del flujo para el proceso de
-- radicación de documentos.
-- ----------------------------------------------------------------------

-- PTR_ID = 123
INSERT INTO PROCESO_TRANSICION
	(PTR_ID, PTT_ID, PTR_DEFINICION, PTR_NOMBRE, PES_ID_INICIAL,
    PES_ID_FINAL, CUANDO, QUIEN, CUANDO_MOD, QUIEN_MOD, ACTIVO)
VALUES
  	(123, 1, '/documento/asignar-documento-ciclico?pin={instancia.id}', 'Asignar documento', 46, 
  	46, SYSDATE, 442, SYSDATE, 442, 1)
;

-- UPDATE PROCESO_TRANSICION SET ACTIVO = 0 WHERE PTR_ID = 123;

-- PTR_ID = 124
INSERT INTO PROCESO_TRANSICION
	(PTR_ID, PTT_ID, PTR_DEFINICION, PTR_NOMBRE, PES_ID_INICIAL,
    PES_ID_FINAL, CUANDO, QUIEN, CUANDO_MOD, QUIEN_MOD, ACTIVO)
VALUES
  	(124, 1, '/documento/dar-respuesta-ciclico?pin={instancia.id}', 'Dar Respuesta', 46, 
  	46, SYSDATE, 442, SYSDATE, 442, 1)
;

-- UPDATE PROCESO_TRANSICION SET ACTIVO = 0 WHERE PTR_ID = 124;