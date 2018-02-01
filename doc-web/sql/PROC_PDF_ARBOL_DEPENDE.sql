-- ------------------------------------------------------------ 
-- 2018-02-01 edison.gonzalez@controltechcg.com Issue #147 (SICDI-Controltech)
-- issue-147: Version inicial de la funcion.
-- ------------------------------------------------------------
create or replace PROCEDURE PROC_PDF_ARBOL_DEPENDE 
(
  P_DOC_ID IN DOCUMENTO.DOC_ID%TYPE,   
  P_DEP_ID IN DEPENDENCIA.DEP_ID%TYPE
) IS
  V_DEP_PADRE DEPENDENCIA.DEP_PADRE%TYPE;
  V_DEP_SIGLA DEPENDENCIA.DEP_SIGLA%TYPE;
BEGIN
  
  IF P_DEP_ID IS NOT NULL THEN
  
    SELECT DEP_PADRE INTO V_DEP_PADRE FROM DEPENDENCIA WHERE DEP_ID = P_DEP_ID;
    SELECT DEP_SIGLA INTO V_DEP_SIGLA FROM DEPENDENCIA WHERE DEP_ID = P_DEP_ID;      
    
    IF V_DEP_SIGLA IS NOT NULL THEN   
      INSERT INTO TMP_ARBOL_DEPEN VALUES (P_DOC_ID,CURRENT_TIMESTAMP,V_DEP_SIGLA);
    END IF;  
      
    IF V_DEP_PADRE IS NOT NULL THEN      
      PROC_PDF_ARBOL_DEPENDE( P_DOC_ID, V_DEP_PADRE );
    END IF;    
  END IF;
  
END PROC_PDF_ARBOL_DEPENDE;