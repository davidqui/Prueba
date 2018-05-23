create or replace FUNCTION "FN_PDF_RADIOGRAMA_MAIN" 
(
  P_DOC_ID IN DOCUMENTO.DOC_ID%TYPE,
  P_USU_ID_FIRMA IN DOCUMENTO.USU_ID_FIRMA%TYPE,
  P_DOC_RADICADO IN DOCUMENTO.DOC_RADICADO%TYPE
)
RETURN VARCHAR2 is
  v_code                    	NUMBER;
  v_errm                    	VARCHAR2(4000);
  v_documento               	DOCUMENTO%ROWTYPE;
  --v_clasificacion         	CLASIFICACION%ROWTYPE;
  V_CLASIFICACION_NOMBRE    	CLASIFICACION.CLA_NOMBRE%TYPE;
  V_DEP_ID_ELABORA          	USUARIO.DEP_ID%TYPE;
  V_SIGLA_CORTA_DE          	VARCHAR2(500);
  V_SIGLA_CORTA_PARA        	VARCHAR2(500);
  V_SIGLA_LARGA_DE          	VARCHAR2(1500);
  V_CONTADOR                	INTEGER;
  V_USU_VISTO_BUENO_1       	INTEGER;
  V_USU_VISTO_BUENO_2       	INTEGER;
  V_USU_VISTO_BUENO_3       	INTEGER;
  V_USU_VISTO_BUENO_4       	INTEGER;
  V_USU_VISTO_BUENO_5       	INTEGER;
  V_USU_VISTO_BUENO_6       	INTEGER;
  V_USU_GRADO_VoBo_1        	VARCHAR2(4000);
  V_USU_NOMBRE_VoBo_1       	VARCHAR2(4000);
  V_USU_CARGO_VoBo_1        	VARCHAR2(4000);
  V_USU_GRADO_VoBo_2        	VARCHAR2(4000);
  V_USU_NOMBRE_VoBo_2       	VARCHAR2(4000);
  V_USU_CARGO_VoBo_2        	VARCHAR2(4000);
  V_USU_GRADO_VoBo_3        	VARCHAR2(4000);
  V_USU_NOMBRE_VoBo_3       	VARCHAR2(4000);
  V_USU_CARGO_VoBo_3        	VARCHAR2(4000);
  V_USU_GRADO_VoBo_4        	VARCHAR2(4000);
  V_USU_NOMBRE_VoBo_4       	VARCHAR2(4000);
  V_USU_CARGO_VoBo_4        	VARCHAR2(4000);
  V_USU_GRADO_VoBo_5        	VARCHAR2(4000);
  V_USU_NOMBRE_VoBo_5       	VARCHAR2(4000);
  V_USU_CARGO_VoBo_5        	VARCHAR2(4000);
  V_USU_GRADO_VoBo_6        	VARCHAR2(4000);
  V_USU_NOMBRE_VoBo_6       	VARCHAR2(4000);
  V_USU_CARGO_VoBo_6        	VARCHAR2(4000);
  --V_USU_ID_FIRMA            	DOCUMENTO.USU_ID_FIRMA%TYPE;
  V_USU_GRADO_FIRMA         	GRADO.GRA_NOMBRE%TYPE;
  V_USU_NOMBRE_FIRMA        	USUARIO.USU_NOMBRE%TYPE;
  V_USU_CARGO_FIRMA         	CARGO.CAR_NOMBRE%TYPE;
  V_USU_TELEFONO            	USUARIO.USU_TELEFONO%TYPE;
  V_USU_EMAIL               	USUARIO.USU_EMAIL%TYPE;
  V_USU_ID_ELABORA          	DOCUMENTO.USU_ID_ELABORA%TYPE;
  V_USU_GRADO_ELABORA       	USUARIO.USU_GRADO%TYPE;
  V_USU_NOMBRE_ELABORA      	USUARIO.USU_NOMBRE%TYPE;
  V_USU_CARGO_ELABORA       	CARGO.CAR_NOMBRE%TYPE;
  V_USU_TEL_ELABORA         	USUARIO.USU_TELEFONO%TYPE;
  V_USU_ID_REVISA           	DOCUMENTO.USU_ID_APRUEBA%TYPE;
  V_USU_GRADO_REVISA        	USUARIO.USU_GRADO%TYPE;
  V_USU_NOMBRE_REVISA       	USUARIO.USU_NOMBRE%TYPE;
  V_USU_CARGO_REVISA        	CARGO.CAR_NOMBRE%TYPE;
  V_USU_ID_VoBo             	DOCUMENTO.USU_ID_VISTO_BUENO%TYPE;
  V_USU_GRADO_VoBo          	USUARIO.USU_GRADO%TYPE;
  V_USU_NOMBRE_VoBo         	USUARIO.USU_NOMBRE%TYPE;
  V_USU_CARGO_VoBo          	CARGO.CAR_NOMBRE%TYPE;
  V_TRD_CODIGO              	TRD.TRD_CODIGO%TYPE;
  V_TRD_NOMBRE              	TRD.TRD_NOMBRE%TYPE;
  V_CONTENIDO_PARTE1        	VARCHAR2(4000);
  --V_CONTENIDO_PARTE2      	VARCHAR(4000);
  V_PADRE_ORIGEN            	VARCHAR2(1000);
  V_DEPENCIAS_ARRIBA_ABAJO  	VARCHAR2(4000);
  V_DIRECCION               	VARCHAR(3900);
  --
  -- 2017-05-03 jgarcia@controltechcg.com Issue #66 (SICDI-Controlyech):
  -- Aumento del tamanyo de la variable V_ARCHIVOS a 4000.
  -- 
  V_ARCHIVOS                	VARCHAR2(4000);
  V_PRO_ID                  	PROCESO_INSTANCIA.PRO_ID%TYPE;
  V_TEXT_TITULO_PROCESO     	VARCHAR(50) := NULL;
  V_IMAGEN_FIRMA_EXT        	USUARIO.USU_IMAGEN_FIRMA_EXT%TYPE;
  V_PES_ID                  	PROCESO_INSTANCIA.PES_ID%TYPE;
  V_USU_ID_ASIGNADO         	PROCESO_INSTANCIA.USU_ID_ASIGNADO%TYPE;
  EXCEPTION_NO_IMG_FIRMA    	EXCEPTION;
  EXCEPTION_DOC_NO_ID_FIRMA    	EXCEPTION;
  V_DEP_ID                  	USUARIO.DEP_ID%TYPE;
  V_DEPENCIA_NOMBRE         	DEPENDENCIA.DEP_NOMBRE%TYPE;
  V_DEPENCIA_SIGLA_ELABORA  	DEPENDENCIA.DEP_SIGLA%TYPE;
  V_DEPENCIA_DIR_ELABORA    	DEPENDENCIA.DIRECCION%TYPE;
  V_JEFE_DEP_N_DESTINO        	USUARIO.USU_NOMBRE%TYPE;
  V_JEFE_DEP_G_DESTINO        	USUARIO.USU_GRADO%TYPE;

  --
  -- 2017-06-07 jgarcia@controltechcg.com Issue #101 (SICDI-Controltech)
  -- hotfix-101: Corrección para agregar la lógica en las funciones 
  -- FN_PDF_DOC_PREVIEW y FN_PDF_RADIOGRAMA_MAIN para establecer el valor de la 
  -- sigla del grado del usuario destino, correspondiente al campo 47 de la
  -- tabla DOCUMENTO_PDF.
  --
  V_JEFE_DEP_SG_DESTINO       USUARIO.USU_GRADO%TYPE;

  V_JEFE_DEP_DIR_DESTINO        DEPENDENCIA.DIRECCION%TYPE;
  V_USU_ID_JEFE           		DEPENDENCIA.USU_ID_JEFE%TYPE;
  V_DEPENCIA_DIR_DESTINO    	DEPENDENCIA.DIRECCION%TYPE;
  V_JEFE_DEP_CARGO_DESTINO      CARGO.CAR_NOMBRE%TYPE;
  V_DEPENCIA_U_ELABORA_NOMBRE	DEPENDENCIA.DEP_NOMBRE%TYPE;
  V_SIGLAS_DEP_ADICIONAL        VARCHAR2(4000);
  -- Issue #123
  V_DEPENDENCIA_CIUDAD_ELABORA  DEPENDENCIA.CIUDAD%TYPE;
  V_DEPENDENCIA_CIUDAD_DESTINO  DEPENDENCIA.CIUDAD%TYPE;

  /*
    2017-09-29 edison.gonzalez@controltechcg.com Issue #129 (SICDI-Controltech)
    feature-129: Se añade las variables para almacenar  la descripcion del 
    campo restriccion de difusion.
  */
  V_RES_DESCRIPCION             RESTRICCION_DIFUSION.RES_DESCRIPCION%TYPE;
BEGIN

  DBMS_OUTPUT.PUT_LINE('Inicio proceso main');

    DELETE FROM TMP_ARBOL_DEPEN WHERE DOC_ID = P_DOC_ID;
    DBMS_OUTPUT.PUT_LINE('Borrando en: TMP_ARBOL_DEPEN');

    SELECT * INTO v_documento FROM DOCUMENTO WHERE DOC_ID = P_DOC_ID;
    DBMS_OUTPUT.PUT_LINE('Consulto OK documento');

    SELECT PRO_ID,PES_ID,USU_ID_ASIGNADO INTO V_PRO_ID,V_PES_ID,V_USU_ID_ASIGNADO FROM PROCESO_INSTANCIA pi INNER JOIN DOCUMENTO doc ON pi.PIN_ID=doc.PIN_ID WHERE doc.DOC_ID=P_DOC_ID;
    DBMS_OUTPUT.PUT_LINE('Consulta OK PROCESO_INSTANCIA.PRO_ID : ' || V_PRO_ID);

    IF V_PRO_ID = 8 THEN
      V_TEXT_TITULO_PROCESO := 'RADIOGRAMA';
      -- SELECT CLA_NOMBRE INTO V_CLASIFICACION_NOMBRE FROM CLASIFICACION WHERE CLA_ID = v_documento.CLA_ID;
      DBMS_OUTPUT.PUT_LINE('Consulta OK CLASIFICACION.CLA_NOMBRE : ' || V_CLASIFICACION_NOMBRE);
    END IF;

    --
    -- 2017-03-08 jgarcia@controltechcg.com Issue #1: Se modifica la función
    -- para que en todos los procesos aparezca la clasificación del documento.
    --
    SELECT CLA_NOMBRE INTO V_CLASIFICACION_NOMBRE FROM CLASIFICACION WHERE CLA_ID = v_documento.CLA_ID;

    IF v_documento.DEP_ID_DES IS NOT NULL THEN
      -- Issue #123
      SELECT DIRECCION,USU_ID_JEFE,CIUDAD INTO V_JEFE_DEP_DIR_DESTINO, V_USU_ID_JEFE, V_DEPENDENCIA_CIUDAD_DESTINO FROM DEPENDENCIA WHERE DEP_ID = v_documento.DEP_ID_DES;
      IF V_USU_ID_JEFE IS NOT NULL THEN
        SELECT USU_NOMBRE, (SELECT CAR_NOMBRE FROM CARGO WHERE CAR_ID = USU_CARGO_PRINCIPAL_ID) INTO V_JEFE_DEP_N_DESTINO,V_JEFE_DEP_CARGO_DESTINO FROM USUARIO WHERE USU_ID = V_USU_ID_JEFE;

          --
          -- 2017-06-07 jgarcia@controltechcg.com Issue #101 (SICDI-Controltech)
          -- hotfix-101: Corrección para agregar la lógica en las funciones 
          -- FN_PDF_DOC_PREVIEW y FN_PDF_RADIOGRAMA_MAIN para establecer el valor de la 
          -- sigla del grado del usuario destino, correspondiente al campo 47 de la
          -- tabla DOCUMENTO_PDF.
          --        
        SELECT GRA_NOMBRE, GRA_ID INTO V_JEFE_DEP_G_DESTINO, V_JEFE_DEP_SG_DESTINO FROM GRADO g INNER JOIN USUARIO u ON g.GRA_ID = u.USU_GRADO WHERE u.USU_ID = V_USU_ID_JEFE;
      END IF;
    END IF;

    --CONSULTAMOS EL USUARIO QUIEN ELABORA EL DOCUMENTO
   V_USU_ID_ELABORA := v_documento.USU_ID_ELABORA;
   DBMS_OUTPUT.PUT_LINE('Consulta OK DOCUMENTO.USU_ID_ELABORA : '|| V_USU_ID_ELABORA);

   SELECT USU_GRADO,USU_NOMBRE,USU_TELEFONO INTO V_USU_GRADO_ELABORA,V_USU_NOMBRE_ELABORA,V_USU_TEL_ELABORA FROM USUARIO WHERE USU_ID = V_USU_ID_ELABORA;
   SELECT CAR_NOMBRE INTO V_USU_CARGO_ELABORA FROM CARGO WHERE CAR_ID = v_documento.CARGO_ID_ELABORA;
   DBMS_OUTPUT.PUT_LINE('Consulta OK ELABORA DOCUMENTO.USU_ID_ELABORA : '|| V_USU_ID_ELABORA);
   DBMS_OUTPUT.PUT_LINE('Consulta OK ELABORA DOCUMENTO.USU_ID_ELABORA : '|| V_USU_ID_ELABORA);
   DBMS_OUTPUT.PUT_LINE('Consulta OK ELABORA USUARIO.CARGO : '|| V_USU_CARGO_ELABORA);

   --CONSULTAMOS LA DEPENDENCIA DEL USUARIO
   SELECT DEP_ID INTO V_DEP_ID_ELABORA FROM USUARIO WHERE USU_ID = V_USU_ID_ELABORA;
   DBMS_OUTPUT.PUT_LINE('Consulta OK USUARIO.DEP_ID : '|| V_DEP_ID_ELABORA);
   IF V_DEP_ID_ELABORA IS NOT NULL THEN
      -- Issue #123
      SELECT DEP_SIGLA,DIRECCION,DEP_NOMBRE,CIUDAD INTO V_DEPENCIA_SIGLA_ELABORA,V_DEPENCIA_DIR_ELABORA, V_DEPENCIA_U_ELABORA_NOMBRE, V_DEPENDENCIA_CIUDAD_ELABORA FROM DEPENDENCIA WHERE DEP_ID = V_DEP_ID_ELABORA;
   END IF;

   --USUARIO ASIGANDO - QUIEN FIRMA
   --V_USU_ID_FIRMA := v_documento.USU_ID_FIRMA;
   IF P_USU_ID_FIRMA IS NULL THEN
      DBMS_OUTPUT.PUT_LINE('ERROR::EXCEPTION_DOC_NO_ID_FIRMA');
      RAISE EXCEPTION_DOC_NO_ID_FIRMA;
   END IF;
   DBMS_OUTPUT.PUT_LINE('Consulta OK DOCUMENTO.USU_ID_FIRMA : '|| P_USU_ID_FIRMA);

   SELECT USU_GRADO, USU_IMAGEN_FIRMA_EXT,USU_NOMBRE, DEP_ID,USU_TELEFONO,USU_EMAIL INTO V_USU_GRADO_FIRMA, V_IMAGEN_FIRMA_EXT,V_USU_NOMBRE_FIRMA,V_DEP_ID,V_USU_TELEFONO,V_USU_EMAIL FROM USUARIO WHERE USU_ID = P_USU_ID_FIRMA;
   SELECT CAR_NOMBRE INTO V_USU_CARGO_FIRMA FROM CARGO WHERE CAR_ID = v_documento.CARGO_ID_FIRMA;
   --GFRADO LARGO
   SELECT GRA_NOMBRE INTO V_USU_GRADO_FIRMA FROM GRADO g INNER JOIN USUARIO u ON g.GRA_ID = u.USU_GRADO WHERE u.USU_ID = P_USU_ID_FIRMA;
   IF V_IMAGEN_FIRMA_EXT IS NULL THEN
      RAISE EXCEPTION_NO_IMG_FIRMA;
   END IF;
   IF V_DEP_ID IS NOT NULL THEN
      SELECT DEP_NOMBRE INTO V_DEPENCIA_NOMBRE FROM DEPENDENCIA WHERE DEP_ID = V_DEP_ID;
   END IF;

   DBMS_OUTPUT.PUT_LINE('Consulta OK FIRMA USUARIO.IMAGEN_FIRMA_EXT : '|| V_IMAGEN_FIRMA_EXT);
   DBMS_OUTPUT.PUT_LINE('Consulta OK FIRMA USUARIO.USU_GRADO : '|| V_USU_GRADO_FIRMA);
   DBMS_OUTPUT.PUT_LINE('Consulta OK FIRMA USUARIO.USU_NOMBRE : '|| V_USU_NOMBRE_FIRMA);
   DBMS_OUTPUT.PUT_LINE('Consulta OK FIRMA USUARIO.CARGO : '|| V_USU_CARGO_FIRMA);

   --CONSULTAMOS EL USUARIO QUIEN REVISA EL DOCUMENTO
   V_USU_ID_REVISA := v_documento.USU_ID_APRUEBA;
   DBMS_OUTPUT.PUT_LINE('Consulta OK DOCUMENTO.USU_ID_APRUEBA : '|| V_USU_ID_REVISA);

   IF V_USU_ID_REVISA IS NOT NULL THEN
     SELECT USU_GRADO,USU_NOMBRE,(SELECT CAR_NOMBRE FROM CARGO WHERE CAR_ID = USU_CARGO_PRINCIPAL_ID) INTO V_USU_GRADO_REVISA,V_USU_NOMBRE_REVISA,V_USU_CARGO_REVISA FROM USUARIO WHERE USU_ID = V_USU_ID_REVISA;
     --SELECT USU_NOMBRE INTO V_USU_NOMBRE_REVISA FROM USUARIO WHERE USU_ID = V_USU_ID_REVISA;
     --SELECT USU_CARGO INTO V_USU_CARGO_REVISA FROM USUARIO WHERE USU_ID = V_USU_ID_REVISA;
     DBMS_OUTPUT.PUT_LINE('Consulta OK REVISA USUARIO.USU_GRADO : '|| V_USU_GRADO_REVISA);
     DBMS_OUTPUT.PUT_LINE('Consulta OK REVISA USUARIO.USU_NOMBRE : '|| V_USU_NOMBRE_REVISA);
     DBMS_OUTPUT.PUT_LINE('Consulta OK REVISA USUARIO.CARGO : '|| V_USU_CARGO_REVISA);
   END IF;

   --CONSULTAMOS EL USUARIO VoBo
   V_USU_ID_VoBo := v_documento.USU_ID_VISTO_BUENO;
   DBMS_OUTPUT.PUT_LINE('Consulta OK VoBo DOCUMENTO.USU_ID_VISTO_BUENO : '|| V_USU_ID_VoBo);

   IF V_USU_ID_VoBo IS NOT NULL THEN
     SELECT USU_GRADO,USU_NOMBRE,(SELECT CAR_NOMBRE FROM CARGO WHERE CAR_ID = USU_CARGO_PRINCIPAL_ID) INTO V_USU_GRADO_VoBo,V_USU_NOMBRE_VoBo,V_USU_CARGO_VoBo FROM USUARIO WHERE USU_ID = V_USU_ID_VoBo;
     DBMS_OUTPUT.PUT_LINE('Consulta OK VoB USUARIO.USU_GRADO : '|| V_USU_GRADO_VoBo);
     DBMS_OUTPUT.PUT_LINE('Consulta OK VoB USUARIO.USU_NOMBRE : '|| V_USU_NOMBRE_VoBo);
     DBMS_OUTPUT.PUT_LINE('Consulta OK VoB USUARIO.CARGO : '|| V_USU_CARGO_VoBo);
   END IF;

   --CONSULTAMOS LA TRD CODIGO
   SELECT TRD_CODIGO,TRD_NOMBRE INTO V_TRD_CODIGO,V_TRD_NOMBRE FROM TRD WHERE TRD_ID = v_documento.TRD_ID;
   DBMS_OUTPUT.PUT_LINE('Consulta OK TRD.TRD_CODIGO : '|| V_TRD_CODIGO);

    --CARGAR ARBOL
    PROC_PDF_ARBOL_DEPENDE(P_DOC_ID, V_DEP_ID_ELABORA);
    DBMS_OUTPUT.PUT_LINE('Consulta OK PROC_PDF_ARBOL_DEPENDE : ');

    V_SIGLA_CORTA_DE := FN_PDF_GET_SIGLA( P_DOC_ID, 'ULTIMO' );
    DBMS_OUTPUT.PUT_LINE('Consulta OK FN_PDF_GET_SIGLA : ' || V_SIGLA_CORTA_DE);

    /*FOR rec IN (SELECT D.DEP_SIGLA FROM DEPENDENCIA D INNER JOIN DOCUMENTO_DEP_DESTINO DDD ON D.DEP_ID = DDD.DEP_ID WHERE DDD.DOC_ID = P_DOC_ID AND D.DEP_SIGLA IS NOT NULL ORDER BY D.DEP_SIGLA)
       LOOP
          V_SIGLAS_DEP_ADICIONAL := ( V_SIGLAS_DEP_ADICIONAL || '-' || rec.DEP_SIGLA );
    END LOOP;*/

    IF v_documento.DEP_ID_DES IS NOT NULL THEN
      SELECT DEP_SIGLA INTO V_SIGLA_CORTA_PARA FROM DEPENDENCIA WHERE DEP_ID = v_documento.DEP_ID_DES;
      /*IF V_SIGLAS_DEP_ADICIONAL IS NOT NULL THEN
        V_SIGLAS_DEP_ADICIONAL := V_SIGLA_CORTA_PARA || V_SIGLAS_DEP_ADICIONAL;
        V_SIGLA_CORTA_PARA := 'UNIDADES';
      ELSE*/
        V_SIGLAS_DEP_ADICIONAL := V_SIGLA_CORTA_PARA;
      --END IF;
      DBMS_OUTPUT.PUT_LINE('Consulta OK DEPENDENCIA.DEP_SIGLA : ' || V_SIGLA_CORTA_PARA);
    END IF;

    V_DEPENCIAS_ARRIBA_ABAJO := FN_PDF_GET_SIGLA( P_DOC_ID, 'RECURSIVO_ARRIBA_ABAJO' );
    DBMS_OUTPUT.PUT_LINE('Procesa OK FN_PDF_GET_SIGLA( P_DOC_ID ) : ' || P_DOC_ID || ' CON SALIDA: ' || V_DEPENCIAS_ARRIBA_ABAJO || ' JUNTO V_SIGLA_CORTA_DE: ' || V_SIGLA_CORTA_DE);

    IF V_SIGLA_CORTA_DE = 'CAIMI' THEN
      V_SIGLA_LARGA_DE := 'COMANDO DE APOYO DE COMBATE DE INTELIGENCIA MILITAR';
      V_PADRE_ORIGEN := 'JEMOP';
     -- V_DIRECCION := '<p>Patria, Honor, Lealtad<br />&quot;Dios en todas nuestras actuaciones&quot;<br />Fe en la causa<br />Carrera 8 a No. 101 - 33<br />Conmutador 6004900 Extensi¿n 2080, Fax 6004901<br />Cob.caimi@imi.mil.co';
    ELSIF V_SIGLA_CORTA_DE = 'CACIM' THEN
      V_SIGLA_LARGA_DE := 'COMANDO DE APOYO DE COMBATE DE CONTRAINTELIGENCIA MILITAR';
      V_PADRE_ORIGEN := 'JEMOP';
      --V_DIRECCION := '<p>Patria, Honor, Lealtad<br />&quot;Dios en todas nuestras actuaciones&quot;<br />Fe en la causa<br />Carrera 8 a No. 101 - 33<br />Conmutador 6004900 Extensi¿n 2080, Fax 6004901<br />jecim@ejercito.mil.co';
    ELSIF V_SIGLA_CORTA_DE = 'CEDE2' THEN
      V_SIGLA_LARGA_DE := 'DEPARTAMENTO DE INTELIGENCIA Y CONTRAINTELIGENCIA';
      V_PADRE_ORIGEN := 'JEMPP';
     -- V_DIRECCION := '<p>Patria, Honor, Lealtad<br />&quot;Dios en todas nuestras actuaciones&quot;<br />Fe en la causa<br />Entrada Principal Carrera 54 No 26-25, Oficina 304<br />Conmutador 2209200 MK 0117212<br />Criptograf¿a.E2@imi.mil.co';
    ELSIF V_SIGLA_CORTA_DE = 'CI3MO1' THEN
      V_SIGLA_LARGA_DE := 'CENTRO INTEGRADO INFORMACION INTELIEGENCIA MILITAR OPERACIONAL No.1';
      V_PADRE_ORIGEN := 'JEMOP-CAIMI-CI3ME-';
    ELSIF V_SIGLA_CORTA_DE = 'CI3MO2' THEN
      V_SIGLA_LARGA_DE := 'CENTRO INTEGRADO INFORMACION INTELIEGENCIA MILITAR OPERACIONAL No.2';
      V_PADRE_ORIGEN := 'JEMOP-CAIMI-CI3ME-';
    ELSIF V_SIGLA_CORTA_DE = 'CI3MO3' THEN
      V_SIGLA_LARGA_DE := 'CENTRO INTEGRADO INFORMACION INTELIEGENCIA MILITAR OPERACIONAL No.3';
      V_PADRE_ORIGEN := 'JEMOP-CAIMI-CI3ME-';
    ELSIF V_SIGLA_CORTA_DE = 'CI3MO4' THEN
      V_SIGLA_LARGA_DE := 'CENTRO INTEGRADO INFORMACION INTELIEGENCIA MILITAR OPERACIONAL No.4';
      V_PADRE_ORIGEN := 'JEMOP-CAIMI-CI3ME-';
    ELSIF V_SIGLA_CORTA_DE = 'CI3MO5' THEN
      V_SIGLA_LARGA_DE := 'CENTRO INTEGRADO INFORMACION INTELIEGENCIA MILITAR OPERACIONAL No.5';
      V_PADRE_ORIGEN := 'JEMOP-CAIMI-CI3ME-';
    ELSIF V_SIGLA_CORTA_DE = 'CI3MO6' THEN
      V_SIGLA_LARGA_DE := 'CENTRO INTEGRADO INFORMACION INTELIEGENCIA MILITAR OPERACIONAL No.6';
      V_PADRE_ORIGEN := 'JEMOP-CAIMI-CI3ME-';
    ELSIF V_SIGLA_CORTA_DE = 'CI3MO7' THEN
      V_SIGLA_LARGA_DE := 'CENTRO INTEGRADO INFORMACION INTELIEGENCIA MILITAR OPERACIONAL No.7';
      V_PADRE_ORIGEN := 'JEMOP-CAIMI-CI3ME-';
    ELSIF V_SIGLA_CORTA_DE = 'CI3MO8' THEN
      V_SIGLA_LARGA_DE := 'CENTRO INTEGRADO INFORMACION INTELIEGENCIA MILITAR OPERACIONAL No.8';
      V_PADRE_ORIGEN := 'JEMOP-CAIMI-CI3ME-';
    ELSIF V_SIGLA_CORTA_DE = 'BAIMI1' THEN
      V_SIGLA_LARGA_DE := 'BATALLÓN DE INTELIGENCIA MILITAR No. 1';
      V_PADRE_ORIGEN := 'JEMOP-CAIMI-BRIMI2-';
    ELSIF V_SIGLA_CORTA_DE = 'BAIMI2' THEN
      V_SIGLA_LARGA_DE := 'BATALLÓN DE INTELIGENCIA MILITAR No. 2';
      V_PADRE_ORIGEN := 'JEMOP-CAIMI-BRIMI2-';
    ELSIF V_SIGLA_CORTA_DE = 'BAIMI3' THEN
      V_SIGLA_LARGA_DE := 'BATALLÓN DE INTELIGENCIA MILITAR No. 3';
      V_PADRE_ORIGEN := 'JEMOP-CAIMI-BRIMI2-';
    ELSIF V_SIGLA_CORTA_DE = 'BAIMI4' THEN
      V_SIGLA_LARGA_DE := 'BATALLÓN DE INTELIGENCIA MILITAR No. 4';
      V_PADRE_ORIGEN := 'JEMOP-CAIMI-BRIMI2-';
    ELSIF V_SIGLA_CORTA_DE = 'BAIMI5' THEN
      V_SIGLA_LARGA_DE := 'BATALLÓN DE INTELIGENCIA MILITAR No. 5';
      V_PADRE_ORIGEN := 'JEMOP-CAIMI-BRIMI2-';
    ELSIF V_SIGLA_CORTA_DE = 'BAIMI6' THEN
      V_SIGLA_LARGA_DE := 'BATALLÓN DE INTELIGENCIA MILITAR No. 6';
      V_PADRE_ORIGEN := 'JEMOP-CAIMI-BRIMI2-';
    ELSIF V_SIGLA_CORTA_DE = 'BAIMI7' THEN
      V_SIGLA_LARGA_DE := 'BATALLÓN DE INTELIGENCIA MILITAR No. 7';
      V_PADRE_ORIGEN := 'JEMOP-CAIMI-BRIMI2-';
    ELSIF V_SIGLA_CORTA_DE = 'BAIMI8' THEN
      V_SIGLA_LARGA_DE := 'BATALLÓN DE INTELIGENCIA MILITAR No. 8';
      V_PADRE_ORIGEN := 'JEMOP-CAIMI-BRIMI2-';
    ELSIF V_SIGLA_CORTA_DE = 'BAGCI' THEN
      V_SIGLA_LARGA_DE := 'BATALLÓN DE GESTIÓN DEL CONOCIMIENTO DE INTELIGENCIA MILITAR';
      V_PADRE_ORIGEN := 'JEMOP-CAIMI-BRIMI2-';
    ELSIF V_SIGLA_CORTA_DE = 'BASIM' THEN
      V_SIGLA_LARGA_DE := 'BATALLÓN DE APOYO DE SERVICIOS PARA LA INTELIGENCIA MILITAR';
      V_PADRE_ORIGEN := 'JEMOP-CAIMI-';
    ELSIF V_SIGLA_CORTA_DE = 'BAIME1' THEN
      V_SIGLA_LARGA_DE := 'BATALLÓN DE INTELIGENCIA MILITAR ESTRATÉGICO No.1';
      V_PADRE_ORIGEN := 'JEMOP-CAIMI-BRIMI1-';
    ELSIF V_SIGLA_CORTA_DE = 'BAIME2' THEN
      V_SIGLA_LARGA_DE := 'BATALLÓN DE INTELIGENCIA MILITAR ESTRATÉGICO No.2';
      V_PADRE_ORIGEN := 'JEMOP-CAIMI-BRIMI1-';
    ELSIF V_SIGLA_CORTA_DE = 'BAIME3' THEN
      V_SIGLA_LARGA_DE := 'BATALLÓN DE INTELIGENCIA MILITAR ESTRATÉGICO No.3';
      V_PADRE_ORIGEN := 'JEMOP-CAIMI-BRIMI1-';
    ELSIF V_SIGLA_CORTA_DE = 'BAIME4' THEN
      V_SIGLA_LARGA_DE := 'BATALLÓN DE INTELIGENCIA MILITAR ESTRATÉGICO No.4';
      V_PADRE_ORIGEN := 'JEMOP-CAIMI-BRIMI1-';
    ELSIF V_SIGLA_CORTA_DE = 'BAIME5' THEN
      V_SIGLA_LARGA_DE := 'BATALLÓN DE INTELIGENCIA MILITAR ESTRATÉGICO No.5';
      V_PADRE_ORIGEN := 'JEMOP-CAIMI-BRIMI1-';
    ELSIF V_SIGLA_CORTA_DE = 'BINGE' THEN
      V_SIGLA_LARGA_DE := 'BATALLÓN DE INTELIGENCIA DE GUERRA ELECTRÓNICA';
      V_PADRE_ORIGEN := 'JEMOP-CAIMI-BRIMI1-';
    ELSIF V_SIGLA_CORTA_DE = 'BIGAM' THEN
      V_SIGLA_LARGA_DE := 'BATALLÓN DE INTELIGENCIA DE GUERRA ELECTRÓNICA ALTA MOVILIDAD';
      V_PADRE_ORIGEN := 'JEMOP-CAIMI-BRIMI1-';
    ELSIF V_SIGLA_CORTA_DE = 'BACIB' THEN
      V_SIGLA_LARGA_DE := 'BATALLÓN DE CIBERINTELIGENCIA';
      V_PADRE_ORIGEN := 'JEMOP-CAIMI-BRIMI1-';
    ELSIF V_SIGLA_CORTA_DE = 'BAIDI' THEN
      V_SIGLA_LARGA_DE := 'BATALLÓN DE INVESTIGACIÓN DESARROLLO E INNOVACIÓN';
      V_PADRE_ORIGEN := 'JEMOP-CAIMI-BRIMI1-';
    ELSIF V_SIGLA_CORTA_DE = 'ESICI' THEN
      V_SIGLA_LARGA_DE := 'ESCUELA DE INTELIGENCIA Y CONTRAINTELIGENCIA';
      V_PADRE_ORIGEN := 'JEMGF-CEDOC-CEMIL-';
    ELSIF V_SIGLA_CORTA_DE = 'BRIMI1' THEN
      V_SIGLA_LARGA_DE := 'BRIGADA DE INTELIGENCIA MILITAR No.1';
      V_PADRE_ORIGEN := 'JEMOP-CAIMI-';
    ELSIF V_SIGLA_CORTA_DE = 'BRIMI2' THEN
      V_SIGLA_LARGA_DE := 'BRIGADA DE INTELIGENCIA MILITAR No.2';
      V_PADRE_ORIGEN := 'JEMOP-CAIMI-';
    ELSIF V_SIGLA_CORTA_DE = 'BINSE' THEN
      V_SIGLA_LARGA_DE := 'BATALLÓN DE INTELIGENCIA DE SEÑALES';
      V_PADRE_ORIGEN := 'JEMOP-CAIMI-BRIMI2-';
    ELSIF V_SIGLA_CORTA_DE = 'CENAC' THEN
      V_SIGLA_LARGA_DE := 'CENTRAL ADMINISTRATIVA Y CONTABLE ESPECIALIZADA DE INTELIGENCIA';
      V_PADRE_ORIGEN := 'JEMGF-COLOG-DIADQ-CADCO-';
    ELSIF V_SIGLA_CORTA_DE = 'BRCIM2' THEN
      V_SIGLA_LARGA_DE := 'BRIGADA DE CONTRAINTELIGENCIA MILITAR No. 2';
      V_PADRE_ORIGEN := 'JEMOP-CACIM-';
    ELSIF V_SIGLA_CORTA_DE = 'BRCIM1' THEN
      V_SIGLA_LARGA_DE := 'BRIGADA DE CONTRAINTELIGENCIA MILITAR NO. 1';
      V_PADRE_ORIGEN := 'JEMOP-CACIM-';
    ELSIF V_SIGLA_CORTA_DE = 'BASMI1' THEN
      V_SIGLA_LARGA_DE := 'BATALLÓN DE SEGURIDAD MILITAR No 1';
      V_PADRE_ORIGEN := 'JEMOP-CACIM-BRCIM2-';
    ELSIF V_SIGLA_CORTA_DE = 'BASMI2' THEN
      V_SIGLA_LARGA_DE := 'BATALLÓN DE SEGURIDAD MILITAR No 2';
      V_PADRE_ORIGEN := 'JEMOP-CACIM-BRCIM2-';
    ELSIF V_SIGLA_CORTA_DE = 'BASMI3' THEN
      V_SIGLA_LARGA_DE := 'BATALLÓN DE SEGURIDAD MILITAR No 3';
      V_PADRE_ORIGEN := 'JEMOP-CACIM-BRCIM2-';
    ELSIF V_SIGLA_CORTA_DE = 'BASMI4' THEN
      V_SIGLA_LARGA_DE := 'BATALLÓN DE SEGURIDAD MILITAR No 4';
      V_PADRE_ORIGEN := 'JEMOP-CACIM-BRCIM2-';
    ELSIF V_SIGLA_CORTA_DE = 'BASMI5' THEN
      V_SIGLA_LARGA_DE := 'BATALLÓN DE SEGURIDAD MILITAR No 5';
      V_PADRE_ORIGEN := 'JEMOP-CACIM-BRCIM2-';
    /*
    2017-11-8 edison.gonzalez@controltechcg.com Issue #137 (SICDI-Controltech)
    Issue-137: Ajuste del nombre de la dependencia BASEC POR BASEC5.
    */
    ELSIF V_SIGLA_CORTA_DE = 'BASEC5' THEN
      V_SIGLA_LARGA_DE := 'BATALLÓN DE SEGURIDAD CIVIL No 5';
      V_PADRE_ORIGEN := 'JEMOP-CACIM-BRCIM2-';
    ELSIF V_SIGLA_CORTA_DE = 'BAGOP' THEN
      V_SIGLA_LARGA_DE := 'BATALLÓN DE GESTIÓN OPERACIONAL DE INTELIGENCIA Y CONTRAINTELIGENCIA';
      V_PADRE_ORIGEN := 'JEMOP-CACIM-BRCIM2';
    ELSIF V_SIGLA_CORTA_DE = 'BACCE' THEN
      V_SIGLA_LARGA_DE := 'BATALLÓN DE CREDIBILIDAD Y CONFIABILIDAD DEL EJÉRCITO';
      V_PADRE_ORIGEN := 'JEMOP-CACIM-BRCIM1-';
    ELSIF V_SIGLA_CORTA_DE = 'BACIM1' THEN
      V_SIGLA_LARGA_DE := 'BATALLÓN DE CONTRAINTELIGENCIA MILITAR No 1';
      V_PADRE_ORIGEN := 'JEMOP-CACIM-BRCIM1-';
    ELSIF V_SIGLA_CORTA_DE = 'BACIM2' THEN
      V_SIGLA_LARGA_DE := 'BATALLÓN DE CONTRAINTELIGENCIA MILITAR No 2';
      V_PADRE_ORIGEN := 'JEMOP-CACIM-BRCIM1-';
    ELSIF V_SIGLA_CORTA_DE = 'BACIM3' THEN
      V_SIGLA_LARGA_DE := 'BATALLÓN DE CONTRAINTELIGENCIA MILITAR No 3';
      V_PADRE_ORIGEN := 'JEMOP-CACIM-BRCIM1-';
    ELSIF V_SIGLA_CORTA_DE = 'BACIM4' THEN
      V_SIGLA_LARGA_DE := 'BATALLÓN DE CONTRAINTELIGENCIA MILITAR No 4';
      V_PADRE_ORIGEN := 'JEMOP-CACIM-BRCIM1-';
    ELSIF V_SIGLA_CORTA_DE = 'BACIM5' THEN
      V_SIGLA_LARGA_DE := 'BATALLÓN DE CONTRAINTELIGENCIA MILITAR No 5';
      V_PADRE_ORIGEN := 'JEMOP-CACIM-BRCIM1-';
    ELSIF V_SIGLA_CORTA_DE = 'BACIM6' THEN
      V_SIGLA_LARGA_DE := 'BATALLÓN DE CONTRAINTELIGENCIA MILITAR No 6';
      V_PADRE_ORIGEN := 'JEMOP-CACIM-BRCIM1-';
    ELSIF V_SIGLA_CORTA_DE = 'BACIM7' THEN
      V_SIGLA_LARGA_DE := 'BATALLÓN DE CONTRAINTELIGENCIA MILITAR No 7';
      V_PADRE_ORIGEN := 'JEMOP-CACIM-BRCIM1-';
    ELSIF V_SIGLA_CORTA_DE = 'BACIM8' THEN
      V_SIGLA_LARGA_DE := 'BATALLÓN DE CONTRAINTELIGENCIA MILITAR No 8';
      V_PADRE_ORIGEN := 'JEMOP-CACIM-BRCIM1-';
    ELSIF V_SIGLA_CORTA_DE = 'BACIM9' THEN
      V_SIGLA_LARGA_DE := 'BATALLÓN DE CONTRAINTELIGENCIA MILITAR No 9';
      V_PADRE_ORIGEN := 'JEMOP-CACIM-BRCIM1-';
    ELSIF V_SIGLA_CORTA_DE = 'BASCI' THEN
      V_SIGLA_LARGA_DE := 'BATALLÓN DE APOYO DE SERVICIOS PARA LA CONTRAINTELIGENCIA MILITAR';
      V_PADRE_ORIGEN := 'JEMOP-CACIM-';
    ELSIF V_SIGLA_CORTA_DE = 'BACSI' THEN
      V_SIGLA_LARGA_DE := 'BATALLON DE CONTRAINTELIGENCIA DE SEGURIDAD DE LA INFORMACIÓN';
      V_PADRE_ORIGEN := 'JEMOP-CACIM-BRCIM2-';
    ELSIF V_SIGLA_CORTA_DE = 'BACIF5' THEN
      V_SIGLA_LARGA_DE := 'BATALLON DE CONTRAINTELIGENCIA DE FRONTERAS N0. 5';
      V_PADRE_ORIGEN := 'JEMOP-CACIM-BRCIM1-';
    /*
    2017-11-8 edison.gonzalez@controltechcg.com Issue #137 (SICDI-Controltech)
    Issue-137: Se añaden las lineas de mando de las dependencias (BASEC1,BASEC2,
    BASEC3,BASEC4).
    */
    ELSIF V_SIGLA_CORTA_DE = 'BASEC1' THEN
      V_SIGLA_LARGA_DE := 'BATALLÓN DE SEGURIDAD CIVIL No 1';
      V_PADRE_ORIGEN := 'JEMOP-CACIM-BRCIM2-';
    ELSIF V_SIGLA_CORTA_DE = 'BASEC2' THEN
      V_SIGLA_LARGA_DE := 'BATALLÓN DE SEGURIDAD CIVIL No 2';
      V_PADRE_ORIGEN := 'JEMOP-CACIM-BRCIM2-';
    ELSIF V_SIGLA_CORTA_DE = 'BASEC3' THEN
      V_SIGLA_LARGA_DE := 'BATALLÓN DE SEGURIDAD CIVIL No 3';
      V_PADRE_ORIGEN := 'JEMOP-CACIM-BRCIM2-';
    ELSIF V_SIGLA_CORTA_DE = 'BASEC4' THEN
      V_SIGLA_LARGA_DE := 'BATALLÓN DE SEGURIDAD CIVIL No 4';
      V_PADRE_ORIGEN := 'JEMOP-CACIM-BRCIM2-';
    ELSIF V_SIGLA_CORTA_DE = 'DIV08' THEN
      V_SIGLA_LARGA_DE := 'OCTAVA DIVISIÓN DEL EJÉRCITO NACIONAL';
      V_PADRE_ORIGEN := 'JEMOP';
    ELSE
      V_SIGLA_LARGA_DE := '';
      V_PADRE_ORIGEN := '';
    END IF;

    /*
    2017-09-29 edison.gonzalez@controltechcg.com Issue #129 (SICDI-Controltech)
    feature-129: Se añade las variables para almacenar la descripcion del campo 
    restriccion de difusion.
    */
    IF v_documento.RESTRICCION_DIFUSION IS NOT NULL THEN
        SELECT RES_DESCRIPCION
        INTO V_RES_DESCRIPCION
        FROM RESTRICCION_DIFUSION
        WHERE RES_ID = v_documento.RESTRICCION_DIFUSION;
    END IF;

    V_CONTADOR := 1;
    FOR rec IN (select USU_ID_VISTO_BUENO from DOCUMENTO_USU_VISTOS_BUENOS where DOC_ID = P_DOC_ID and rownum <= 6 order by CUANDO ASC )
       LOOP
          IF V_CONTADOR = 1 THEN
            V_USU_VISTO_BUENO_1 := rec.USU_ID_VISTO_BUENO;
          ELSIF V_CONTADOR = 2 THEN
            V_USU_VISTO_BUENO_2 := rec.USU_ID_VISTO_BUENO;
          ELSIF V_CONTADOR = 3 THEN
            V_USU_VISTO_BUENO_3 := rec.USU_ID_VISTO_BUENO;
          ELSIF V_CONTADOR = 4 THEN
            V_USU_VISTO_BUENO_4 := rec.USU_ID_VISTO_BUENO;
          ELSIF V_CONTADOR = 5 THEN
            V_USU_VISTO_BUENO_5 := rec.USU_ID_VISTO_BUENO;
          ELSIF V_CONTADOR = 6 THEN
            V_USU_VISTO_BUENO_6 := rec.USU_ID_VISTO_BUENO;
          END IF;
          V_CONTADOR := V_CONTADOR + 1;
    END LOOP;

    IF V_USU_VISTO_BUENO_1 IS NOT NULL THEN
        SELECT USU_GRADO,USU_NOMBRE,(SELECT CAR_NOMBRE FROM CARGO WHERE CAR_ID = USU_CARGO_PRINCIPAL_ID) INTO V_USU_GRADO_VoBo_1,V_USU_NOMBRE_VoBo_1,V_USU_CARGO_VoBo_1 FROM USUARIO WHERE USU_ID = V_USU_VISTO_BUENO_1;
    END IF;
    IF V_USU_VISTO_BUENO_2 IS NOT NULL THEN
        SELECT USU_GRADO,USU_NOMBRE,(SELECT CAR_NOMBRE FROM CARGO WHERE CAR_ID = USU_CARGO_PRINCIPAL_ID) INTO V_USU_GRADO_VoBo_2,V_USU_NOMBRE_VoBo_2,V_USU_CARGO_VoBo_2 FROM USUARIO WHERE USU_ID = V_USU_VISTO_BUENO_2;
    END IF;
    IF V_USU_VISTO_BUENO_3 IS NOT NULL THEN
        SELECT USU_GRADO,USU_NOMBRE,(SELECT CAR_NOMBRE FROM CARGO WHERE CAR_ID = USU_CARGO_PRINCIPAL_ID) INTO V_USU_GRADO_VoBo_3,V_USU_NOMBRE_VoBo_3,V_USU_CARGO_VoBo_3 FROM USUARIO WHERE USU_ID = V_USU_VISTO_BUENO_3;
    END IF;
    IF V_USU_VISTO_BUENO_4 IS NOT NULL THEN
        SELECT USU_GRADO,USU_NOMBRE,(SELECT CAR_NOMBRE FROM CARGO WHERE CAR_ID = USU_CARGO_PRINCIPAL_ID) INTO V_USU_GRADO_VoBo_4,V_USU_NOMBRE_VoBo_4,V_USU_CARGO_VoBo_4 FROM USUARIO WHERE USU_ID = V_USU_VISTO_BUENO_4;
    END IF;
    IF V_USU_VISTO_BUENO_5 IS NOT NULL THEN
        SELECT USU_GRADO,USU_NOMBRE,(SELECT CAR_NOMBRE FROM CARGO WHERE CAR_ID = USU_CARGO_PRINCIPAL_ID) INTO V_USU_GRADO_VoBo_5,V_USU_NOMBRE_VoBo_5,V_USU_CARGO_VoBo_5 FROM USUARIO WHERE USU_ID = V_USU_VISTO_BUENO_5;
    END IF;
    IF V_USU_VISTO_BUENO_6 IS NOT NULL THEN
        SELECT USU_GRADO,USU_NOMBRE,(SELECT CAR_NOMBRE FROM CARGO WHERE CAR_ID = USU_CARGO_PRINCIPAL_ID) INTO V_USU_GRADO_VoBo_6,V_USU_NOMBRE_VoBo_6,V_USU_CARGO_VoBo_6 FROM USUARIO WHERE USU_ID = V_USU_VISTO_BUENO_6;
    END IF;

   V_CONTENIDO_PARTE1 := REPLACE(' MDN-CGFM-COEJC-SECEJ-' || V_PADRE_ORIGEN || '-' || V_DEPENCIAS_ARRIBA_ABAJO, '--','-');
  --V_CONTENIDO_PARTE2 := V_TRD_CODIGO || 'X ' || TO_CHAR(v_documento.DOC_CONTENIDO) || 'X (' || V_USU_GRADO_FIRMA || ' ' || V_USU_NOMBRE_FIRMA || ' ' || V_USU_CARGO_FIRMA || ')';

    FOR rec IN (SELECT DAD_ORIGINAL FROM DOCUMENTO_ADJUNTO WHERE DOC_ID = P_DOC_ID AND ACTIVO = 1) LOOP
        V_ARCHIVOS := ( V_ARCHIVOS || rec.DAD_ORIGINAL || ',' );
    END LOOP;

    DELETE FROM DOCUMENTO_PDF WHERE DOCPDF_ID = P_DOC_ID;
    INSERT INTO DOCUMENTO_PDF (DOCPDF_ID) VALUES (P_DOC_ID);
    UPDATE
      DOCUMENTO_PDF
    SET
      PASO       = 'FIRMA',
      PDF_TEXTO1 = V_CLASIFICACION_NOMBRE,
      PDF_TEXTO2 = V_SIGLA_LARGA_DE,
      PDF_TEXTO3 = P_DOC_RADICADO,
      PDF_TEXTO4 = REPLACE(P_DOC_RADICADO,'-',''),
      PDF_TEXTO5 = TO_CHAR(sysdate,'DD') || ' de ' || INITCAP( TRIM(TO_CHAR(sysdate,'MONTH','nls_date_language=SPANISH'))) || ' de ' || TO_CHAR(sysdate,'YYYY'),
      PDF_TEXTO6 = V_SIGLA_CORTA_DE,
      PDF_TEXTO6_2 = V_SIGLA_CORTA_PARA,
      PDF_TEXTO7 = V_TEXT_TITULO_PROCESO,
      PDF_TEXTO8 = '<b>' || V_CONTENIDO_PARTE1 || V_TRD_CODIGO || '</b>',
      PDF_TEXTO9 = V_CONTENIDO_PARTE1 || P_DOC_RADICADO,
      PDF_TEXTO10 = NULL,--V_CONTENIDO_PARTE2,
      PDF_TEXTO11 = NULL,--'<b>' || V_CONTENIDO_PARTE1 || V_TRD_CODIGO || '</b>' || 'X ' || v_documento.DOC_CONTENIDO || 'X ' || '<b>' || V_USU_GRADO_FIRMA || ' - ' || V_USU_NOMBRE_FIRMA || ' - ' || V_USU_CARGO_FIRMA || '</b>' || ' Quien firma X',
      PDF_TEXTO12 = V_USU_ID_ELABORA,
      PDF_TEXTO13 = V_USU_GRADO_ELABORA,
      PDF_TEXTO14 = V_USU_NOMBRE_ELABORA,
      PDF_TEXTO15 = V_USU_CARGO_ELABORA,
      PDF_TEXTO16 = P_USU_ID_FIRMA,
      PDF_TEXTO17 = V_USU_GRADO_FIRMA,
      PDF_TEXTO18 = V_USU_NOMBRE_FIRMA,
      PDF_TEXTO19 = V_USU_CARGO_FIRMA,
      PDF_TEXTO20 = V_USU_ID_REVISA,
      PDF_TEXTO21 = V_USU_GRADO_REVISA,
      PDF_TEXTO22 = V_USU_NOMBRE_REVISA,
      PDF_TEXTO23 = V_USU_CARGO_REVISA,
      PDF_TEXTO24 = V_DIRECCION,
      PDF_TEXTO25 = V_ARCHIVOS,
      PDF_TEXTO26 = v_documento.DOC_DEST_NOMBRE,
      PDF_TEXTO27 = v_documento.DOC_DEST_TITULO,
      PDF_TEXTO28 = v_documento.DOC_DEST_DIRECCION,
      PDF_TEXTO29 = v_documento.DOC_ASUNTO,
      PDF_TEXTO30 = SUBSTR(V_IMAGEN_FIRMA_EXT,1,1) || '/' || SUBSTR(V_IMAGEN_FIRMA_EXT,2,1) || '/' || SUBSTR(V_IMAGEN_FIRMA_EXT,3,1) || '/' || SUBSTR(V_IMAGEN_FIRMA_EXT,4,1) || '/' || V_IMAGEN_FIRMA_EXT,
      PDF_TEXTO31 = V_USU_ID_VoBo,
      PDF_TEXTO32 = V_USU_GRADO_VoBo,
      PDF_TEXTO33 = V_USU_NOMBRE_VoBo,
      PDF_TEXTO34 = V_USU_CARGO_VoBo,
      PDF_TEXTO35 = V_DEPENCIAS_ARRIBA_ABAJO,
      PDF_TEXTO36 = V_TRD_CODIGO,
      PDF_TEXTO37 = V_DEPENCIA_NOMBRE,
      PDF_TEXTO38 = V_PADRE_ORIGEN,
      PDF_TEXTO40 = V_DEPENCIA_SIGLA_ELABORA,
      PDF_TEXTO42 = V_TRD_NOMBRE,
      PDF_TEXTO43 = V_SIGLA_CORTA_DE,
      PDF_TEXTO44 = V_DEPENCIA_DIR_ELABORA,
      PDF_TEXTO46 = V_JEFE_DEP_G_DESTINO,

      -- 2017-06-07 jgarcia@controltechcg.com Issue #101 (SICDI-Controltech) hotfix-101:
      PDF_TEXTO47 = V_JEFE_DEP_SG_DESTINO,

      PDF_TEXTO48 = V_JEFE_DEP_N_DESTINO,
      PDF_TEXTO49 = V_JEFE_DEP_DIR_DESTINO,
      PDF_TEXTO50 = v_documento.DOC_ASUNTO,
      PDF_TEXTO51 = v_documento.DOC_DEST_TITULO,
      PDF_TEXTO52 = v_documento.DOC_DEST_NOMBRE,
      PDF_TEXTO53 = v_documento.DOC_DEST_DIRECCION,
      PDF_TEXTO55 = V_USU_TEL_ELABORA,
      PDF_TEXTO56 = V_JEFE_DEP_CARGO_DESTINO,
      PDF_TEXTO57 = V_CONTENIDO_PARTE1,
      PDF_TEXTO58 = V_DEPENCIA_U_ELABORA_NOMBRE,
      PDF_TEXTO59 = V_SIGLAS_DEP_ADICIONAL,
      PDF_TEXTO60 = V_USU_TELEFONO,
      PDF_TEXTO61 = V_USU_EMAIL,
      PDF_TEXTO64 = V_USU_GRADO_VoBo_1,
      PDF_TEXTO65 = V_USU_NOMBRE_VoBo_1,
      PDF_TEXTO66 = V_USU_CARGO_VoBo_1,
      PDF_TEXTO67 = V_USU_GRADO_VoBo_2,
      PDF_TEXTO68 = V_USU_NOMBRE_VoBo_2,
      PDF_TEXTO69 = V_USU_CARGO_VoBo_2,
      PDF_TEXTO70 = V_USU_GRADO_VoBo_3,
      PDF_TEXTO71 = V_USU_NOMBRE_VoBo_3,
      PDF_TEXTO72 = V_USU_CARGO_VoBo_3,
      PDF_TEXTO73 = V_USU_GRADO_VoBo_4,
      PDF_TEXTO74 = V_USU_NOMBRE_VoBo_4,
      PDF_TEXTO75 = V_USU_CARGO_VoBo_4,
      PDF_TEXTO76 = V_USU_GRADO_VoBo_5,
      PDF_TEXTO77 = V_USU_NOMBRE_VoBo_5,
      PDF_TEXTO78 = V_USU_CARGO_VoBo_5,
      PDF_TEXTO79 = V_USU_GRADO_VoBo_6,
      PDF_TEXTO80 = V_USU_NOMBRE_VoBo_6,
      PDF_TEXTO81 = V_USU_CARGO_VoBo_6,
      -- Issue #123
      PDF_TEXTO82 = V_DEPENDENCIA_CIUDAD_ELABORA,
      PDF_TEXTO83 = V_DEPENDENCIA_CIUDAD_DESTINO,
      -- Issue #129
      PDF_TEXTO84 = v_documento.GRADO_EXTERNO,
      PDF_TEXTO85 = v_documento.MARCA_AGUA_EXTERNO,
      PDF_TEXTO86 = V_RES_DESCRIPCION

    WHERE DOCPDF_ID = P_DOC_ID;

    commit;

    RETURN 'OK';

  EXCEPTION
    WHEN EXCEPTION_DOC_NO_ID_FIRMA THEN
        --DEJAMOS EL REGISTRO EN SU ESTADO ANTERIOR QUE DEBE SER: 58         Pendiente de firma
        UPDATE PROCESO_INSTANCIA SET PES_ID = V_PES_ID, USU_ID_ASIGNADO = V_USU_ID_ASIGNADO WHERE PIN_ID = v_documento.PIN_ID;
        commit;
        RETURN 'ERROR: Este documento no tiene persona que firma el documento';
    WHEN EXCEPTION_NO_IMG_FIRMA THEN
        --DEJAMOS EL REGISTRO EN SU ESTADO ANTERIOR QUE DEBE SER: 58         Pendiente de firma
        UPDATE PROCESO_INSTANCIA SET PES_ID = V_PES_ID, USU_ID_ASIGNADO = V_USU_ID_ASIGNADO WHERE PIN_ID = v_documento.PIN_ID;
        commit;
        RETURN 'ERROR: La persona (' || V_USU_NOMBRE_FIRMA || ') quien firma este documento no tiene firma cargada.';
    WHEN OTHERS THEN
      v_code := SQLCODE;
      v_errm := SUBSTR(SQLERRM, 1, 3500);
      DBMS_OUTPUT.PUT_LINE ('Error code ' || v_code || ': ' || v_errm);

      --INSERT INTO errors VALUES (P_DOC_ID, v_code, v_errm, SYSTIMESTAMP);
      --DEJAMOS EL REGISTRO EN SU ESTADO ANTERIOR QUE DEBE SER: 58           Pendiente de firma
      UPDATE PROCESO_INSTANCIA SET PES_ID = V_PES_ID, USU_ID_ASIGNADO = V_USU_ID_ASIGNADO WHERE PIN_ID = v_documento.PIN_ID;
      commit;
      RETURN 'ERROR: ' || v_code || ', ' || v_errm;

END FN_PDF_RADIOGRAMA_MAIN;
