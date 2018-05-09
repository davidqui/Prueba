<#--
    2018-05-08 jgarcia@controltechcg.com Issue #160 (SICDI-Controltech)
    feature-160.
-->
<#assign charset="UTF-8">
<#assign pageTitle = "VALIDAR DOCUMENTO" />
<#include "header.ftl" />
<!DOCTYPE html>
<div class="container-fluid">
    <h4>${pageTitle}</h4>

    <div class="container-fluid">
        <form action="/file-validation/validate" method="post" enctype="multipart/form-data">
            <fieldset class="form-group">
                <label for="archivo_validar">Archivo a validar: (*.pdf)</label>
                <input type="file" id="archivo_validar" name="archivo_validar" class="form-control"/>
            </fieldset>
            <fieldset class="form-group">
                <label for="doc_firma_envio_uuid">UUID Firma Documento SICDI:</label>
                <input type="text" id="doc_firma_envio_uuid" name="doc_firma_envio_uuid" class="form-control"/>
            </fieldset>
            
            <button type="submit" class="btn btn-success btn-sm">Enviar</button>
        </form>
    </div>
    <#if valid?? >
        Es: ${valid?c}!!
    </#if>
</div>
<#include "footer.ftl" />