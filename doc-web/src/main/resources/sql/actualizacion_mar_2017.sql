-- 2017-03-27 jgarcia@controltechcg.com Issue #25 (SIGDI-Controltech): Modificación del nombre de la primer botón "Dar respuesta".
-- ###EJECUTADO PRODUCCION### 2017-04-03

-- UPDATE PROCESO_TRANSICION SET PTR_NOMBRE = 'Asignar para respuesta' WHERE PTR_ID = 90;
-- UPDATE PROCESO_TRANSICION SET PTR_NOMBRE = 'Asignar para respuesta' WHERE PTR_ID = 6;

-- 2017-03-08 jgarcia@controltechcg.com Issue #10 (SIGDI-Incidencias01): Ampliación el campo "DOC_DEST_TITULO" de la tabla "DOCUMENTO" de 32 a 128 caracteres.
-- ###EJECUTADO PRODUCCION###

-- ALTER TABLE DOCUMENTO MODIFY DOC_DEST_TITULO VARCHAR2(128 CHAR);