-- ----------------------------------------------------------------------
-- 2017-06-12 jgarcia@controltechcg.com Issue #93 (SICDI-Controntech)
-- feature-93
-- Creación de transición para anulación de respuesta para proceso 
-- cíclico.
-- ----------------------------------------------------------------------

-- PTR_ID = 127
INSERT INTO PROCESO_TRANSICION
	(PTR_ID, PTT_ID, PTR_DEFINICION, PTR_NOMBRE, PES_ID_INICIAL,
    PES_ID_FINAL, CUANDO, QUIEN, CUANDO_MOD, QUIEN_MOD, ACTIVO)
VALUES
  	(127, 1, '', 'Anular Respuesta', 61, 
  	83, SYSDATE, 442, SYSDATE, 442, 1)
;

UPDATE PROCESO_TRANSICION 
	SET PTR_DEFINICION = '/documento/anular-respuesta-documento-ciclico?pin={instancia.id}&tid={transicion.id}' 
	WHERE PTR_ID = 127
;