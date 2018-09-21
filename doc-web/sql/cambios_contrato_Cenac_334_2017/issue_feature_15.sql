--
-- Author:  samuel.delgado@controltechcg.com
-- Created: 14/08/2018 Issue #15 (SICDI-Controltech) feature-6
--

-- -----------------------------------------------------------------------------
-- FUNCION: 
-- -----------------------------------------------------------------------------

CREATE OR REPLACE FUNCTION DOC.FN_LINEA_MANDO_DEPENDENCIA(
    P_DEP_ID IN DEPENDENCIA.DEP_ID%TYPE,
    P_TIPO_SALIDA IN VARCHAR2 
)
RETURN VARCHAR2 IS
    ANS VARCHAR2(4000);
BEGIN
    ANS := '';
    IF P_TIPO_SALIDA = 'LINEA_MANDO' THEN
        FOR P_DEP_SIGLA IN (SELECT DEP_SIGLA INTO ANS
                  FROM DEPENDENCIA WHERE DEP_SIGLA IS NOT NULL
                  START WITH DEP_ID = P_DEP_ID
                  CONNECT BY DEP_ID = PRIOR DEP_PADRE
                  ORDER BY LEVEL DESC, DEP_SIGLA)
        LOOP
                ANS := ANS|| '-' || P_DEP_SIGLA.DEP_SIGLA;
        END LOOP;
        ANS := ANS || '-';
    ELSIF P_TIPO_SALIDA = 'SIGLA_LARGA_DE' THEN
        FOR P_DEP_SIGLA IN (SELECT DEP_SIGLA, DEP_NOMBRE, 
              DEP_IND_ENVIO_DOCUMENTOS, DEP_PADRE
              FROM DEPENDENCIA
              START WITH DEP_ID = P_DEP_ID
              CONNECT BY DEP_ID = PRIOR DEP_PADRE
              ORDER BY LEVEL ASC, DEP_SIGLA)
        LOOP
            IF P_DEP_SIGLA.DEP_IND_ENVIO_DOCUMENTOS = 1 OR P_DEP_SIGLA.DEP_PADRE IS NULL THEN
                ANS := P_DEP_SIGLA.DEP_NOMBRE;
                EXIT;
            END IF;
        END LOOP;
    END IF;
    RETURN ANS;
END FN_LINEA_MANDO_DEPENDENCIA;