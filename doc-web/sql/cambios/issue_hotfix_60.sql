-- ----------------------------------------------------------------------
-- 2017-06-22 jgarcia@controltechcg.com Issue #60 (SICDI-Controntech)
-- hotfix-60
-- Desactivación de roles de pruebas ADMIN_ARCHIVO_CENTRAL y 
-- ARCHIVO_CENTRAL
-- ----------------------------------------------------------------------

UPDATE ROL SET ACTIVO = 0 WHERE ROL_ID IN ('ADMIN_ARCHIVO_CENTRAL', 'ARCHIVO_CENTRAL');

UPDATE PERFIL_ROL SET ACTIVO = 0 WHERE ROL_ID IN ('ADMIN_ARCHIVO_CENTRAL', 'ARCHIVO_CENTRAL');

-- Consulta de verificacion #1

SELECT DISTINCT PERFIL.PER_ID,
  PERFIL.PER_NOMBRE
FROM PERFIL_ROL
JOIN PERFIL
ON (PERFIL.PER_ID      = PERFIL_ROL.PER_ID)
WHERE PERFIL.ACTIVO    = 1
AND PERFIL_ROL.ROL_ID IN ('ADMIN_ARCHIVO_CENTRAL', 'ARCHIVO_CENTRAL')
ORDER BY PERFIL.PER_NOMBRE ;

-- Consulta de verificacion #2

SELECT USUARIO.USU_LOGIN, PERFIL.PER_NOMBRE
FROM USUARIO
JOIN PERFIL
ON (PERFIL.PER_ID    = USUARIO.PER_ID)
WHERE USUARIO.ACTIVO = 1
AND PERFIL.ACTIVO    = 1
AND PERFIL.PER_ID   IN (2,81) ;