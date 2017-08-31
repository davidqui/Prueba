-- 2017-02-09 jgarcia@controltechcg.com Issue #138: Reiniciar el estado del Issue.

-- UPDATE PROCESO_TRANSICION SET ACTIVO = 1 WHERE PTR_NOMBRE = 'Dar respuesta';

-- 2017-02-06 jgarcia@controltechcg.com Issue #123: Nuevo campo CIUDAD en la tabla de DEPENDENCIA.

-- ALTER TABLE DEPENDENCIA ADD (CIUDAD VARCHAR2(64));

-- 2017-02-06 jgarcia@controltechcg.com Issue #123: Nuevo campos PDF_TEXTO para WILDCARDS de generación de documento (DEPENDENCIA.CIUDAD).

-- ALTER TABLE DOCUMENTO_PDF ADD (PDF_TEXTO82 VARCHAR2(4000), PDF_TEXTO83 VARCHAR2(4000));