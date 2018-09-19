--
-- Author:  samuel.delgado@controltechcg.com
-- Created: 14/08/2018 Issue #15 (SICDI-Controltech) feature-6
--

-- -----------------------------------------------------------------------------
-- FUNCION: 
-- -----------------------------------------------------------------------------

CREATE OR REPLACE FUNCTION DOC.FN_LINEA_MANDO_DEPENDENCIA(
    P_DEP_ID IN DEPENDENCIA.DEP_ID%TYPE
)
RETURN VARCHAR2 IS
    V_DEP_SIGLAS VARCHAR2(4000);
BEGIN
    V_DEP_SIGLAS := '';
    FOR P_DEP_SIGLA IN (SELECT DEP_SIGLA INTO V_DEP_SIGLAS
              FROM DEPENDENCIA WHERE DEP_SIGLA IS NOT NULL
              START WITH DEP_ID = P_DEP_ID
              CONNECT BY DEP_ID = PRIOR DEP_PADRE
              ORDER BY LEVEL DESC, DEP_SIGLA)
    LOOP
            V_DEP_SIGLAS := V_DEP_SIGLAS|| '-' || P_DEP_SIGLA.DEP_SIGLA;
    END LOOP;

    RETURN V_DEP_SIGLAS || '-';
END FN_LINEA_MANDO_DEPENDENCIA;