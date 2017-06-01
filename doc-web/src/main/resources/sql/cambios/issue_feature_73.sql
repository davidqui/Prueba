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
  	(123, 1, '', 'Asignar documento', 46, 
  	46, SYSDATE, 442, SYSDATE, 442, 1)
;

UPDATE PROCESO_TRANSICION 
	SET PTR_DEFINICION = '/documento/asignar-documento-ciclico?pin={instancia.id}&tid={transicion.id}' 
	WHERE PTR_ID = 123
;

-- UPDATE PROCESO_TRANSICION SET ACTIVO = 0 WHERE PTR_ID = 123;

-- PTR_ID = 124
INSERT INTO PROCESO_TRANSICION
	(PTR_ID, PTT_ID, PTR_DEFINICION, PTR_NOMBRE, PES_ID_INICIAL,
    PES_ID_FINAL, CUANDO, QUIEN, CUANDO_MOD, QUIEN_MOD, ACTIVO)
VALUES
  	(124, 1, '', 'Dar Respuesta', 46, 
  	52, SYSDATE, 442, SYSDATE, 442, 1)
;

UPDATE PROCESO_TRANSICION 
	SET PTR_DEFINICION = '/documento/dar-respuesta-ciclico?pin={instancia.id}&tid={transicion.id}' 
	WHERE PTR_ID = 124
;

-- UPDATE PROCESO_TRANSICION SET ACTIVO = 0 WHERE PTR_ID = 124;

-- ----------------------------------------------------------------------
-- 2017-05-19 jgarcia@controltechcg.com Issue #73 (SICDI-Controntech)
-- feature-73
-- Modificación de transiciones y estados del flujo para el proceso de
-- generación de documentos internos.
-- ----------------------------------------------------------------------

-- PTR_ID = 125
INSERT INTO PROCESO_TRANSICION
	(PTR_ID, PTT_ID, PTR_DEFINICION, PTR_NOMBRE, PES_ID_INICIAL,
    PES_ID_FINAL, CUANDO, QUIEN, CUANDO_MOD, QUIEN_MOD, ACTIVO)
VALUES
  	(125, 1, '', 'Asignar documento', 49, 
  	49, SYSDATE, 442, SYSDATE, 442, 1)
;

UPDATE PROCESO_TRANSICION 
	SET PTR_DEFINICION = '/documento/asignar-documento-ciclico?pin={instancia.id}&tid={transicion.id}' 
	WHERE PTR_ID = 125
;

-- UPDATE PROCESO_TRANSICION SET ACTIVO = 0 WHERE PTR_ID = 125;

-- PTR_ID = 126
INSERT INTO PROCESO_TRANSICION
	(PTR_ID, PTT_ID, PTR_DEFINICION, PTR_NOMBRE, PES_ID_INICIAL,
    PES_ID_FINAL, CUANDO, QUIEN, CUANDO_MOD, QUIEN_MOD, ACTIVO)
VALUES
  	(126, 1, '', 'Dar Respuesta', 49, 
  	52, SYSDATE, 442, SYSDATE, 442, 1)
;

UPDATE PROCESO_TRANSICION 
	SET PTR_DEFINICION = '/documento/dar-respuesta-ciclico?pin={instancia.id}&tid={transicion.id}' 
	WHERE PTR_ID = 126
;

-- UPDATE PROCESO_TRANSICION SET ACTIVO = 0 WHERE PTR_ID = 126;


-- ----------------------------------------------------------------------
-- 2017-05-22 jgarcia@controltechcg.com Issue #73 (SICDI-Controntech)
-- feature-73
-- Desactivación de transiciones que serán reemplazadas con la 
-- implementación cíclica.
-- ----------------------------------------------------------------------

-- Radicación de documentos.
UPDATE PROCESO_TRANSICION SET ACTIVO = 0 WHERE PTR_ID IN (8, 6, 103);

-- Generación interna de documentos.
UPDATE PROCESO_TRANSICION SET ACTIVO = 0 WHERE PTR_ID IN (90);

-- UPDATE PROCESO_TRANSICION SET ACTIVO = 1 WHERE PTR_ID IN (90, 8, 6, 103);

-- ----------------------------------------------------------------------
-- 2017-06-01 jgarcia@controltechcg.com Issue #73 (SICDI-Controntech)
-- feature-73
-- Cambio de nombre en estado de recepción del proceso de radicación
-- de documentos.
-- ----------------------------------------------------------------------

UPDATE PROCESO_ESTADO SET PES_NOMBRE = 'En Revisión' WHERE PES_ID = 46;

-- UPDATE PROCESO_ESTADO SET PES_NOMBRE = 'Revisión Jefe Jefatura' WHERE PES_ID = 46;