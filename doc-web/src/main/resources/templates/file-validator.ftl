<#--
    2018-05-08 jgarcia@controltechcg.com Issue #160 (SICDI-Controltech)
    feature-160.
-->
<#assign charset="UTF-8">
<#assign title="Validar Documento">
<!DOCTYPE html>
<html>
    <head>
        <title>${title}</title>
        <meta charset="${charset}">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        </head>
    <body>
        <form action="/file-validation/validate" method="post" enctype="multipart/form-data">
            Archivo a validar:<input type="file" id="archivo_validar" name="archivo_validar"/>
            UUID Firma Documento SICDI:<input type="text" id="doc_firma_envio_uuid" name="doc_firma_envio_uuid" />
            <button type="submit">Enviar</button>
            </form>
        </body>
    
    <#if valid?? >
        Es: ${valid?c}!!
    </#if>
    </html>
