-- -----------------------------------------------------------------------------
-- 2018-05-22 jgarcia@controltechcg.com Issue #171 (SICDI-Controltech)
-- hotfix-171: Sentencia SQL que construye instrucciones de actualización
-- sobre la tabla DOCUMENTO cuando no tiene valor en el campo CARGO_ID_ELABORA.
-- -----------------------------------------------------------------------------
SELECT
    'UPDATE DOCUMENTO SET CARGO_ID_ELABORA = '
     || DOCUMENTO_USU_ELABORA_MAX.CARGO_ID_ELABORA
     || ' WHERE DOCUMENTO.DOC_ID = '''
     || DOCUMENTO.DOC_ID
     || ''';' AS "UPDATE"
FROM
    DOCUMENTO
    JOIN (
        SELECT
            DOC_ID,
            USU_ID_ELABORA,
            CARGO_ID_ELABORA,
            MAX(CUANDO)
        FROM
            DOCUMENTO_USU_ELABORA
        WHERE
            CARGO_ID_ELABORA IS NOT NULL
        GROUP BY
            DOC_ID,
            USU_ID_ELABORA,
            CARGO_ID_ELABORA
    ) DOCUMENTO_USU_ELABORA_MAX ON (
            DOCUMENTO_USU_ELABORA_MAX.DOC_ID = DOCUMENTO.DOC_ID
        AND
            DOCUMENTO_USU_ELABORA_MAX.USU_ID_ELABORA = DOCUMENTO.USU_ID_ELABORA
    )
WHERE
        DOCUMENTO.DOC_ASUNTO IS NOT NULL
    AND
        DOCUMENTO.CARGO_ID_ELABORA IS NULL;