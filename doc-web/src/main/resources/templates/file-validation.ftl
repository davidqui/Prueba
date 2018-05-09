<#--
    2018-05-08 jgarcia@controltechcg.com Issue #160 (SICDI-Controltech)
    feature-160.
-->
<#setting number_format="computer">

<#assign pageTitle = "Validar Documento PDF por UUID de Firma y Envío." />
<#include "gen-macros.ftl">
<#if templatePrefix??>
    <#include templatePrefix + "-header.ftl">
<#else>
    <#include "admin-header.ftl">
</#if>

<#--
    2018-05-09 jgarcia@controltechcg.com Issue #160 (SICDI-Controltech)
    feature-160: Texto a presentar, indicado en correo "TEXTO PDF" (Wed, 09 May 2018 13:55:22 -0500)
-->
<#assign formAlertText = "En cumplimiento del Decreto 2609 de 2012 Art26 se garantiza que un documento electrónico generado por primera vez en su forma definitiva no sea modificado a lo largo de todo su ciclo de vida, desde su producción hasta su conservación."/>


<div class="container-fluid">
    <h1 class="cus-h1-page-title">${pageTitle}</h1>

    <div class="alert alert-info" role="alert">${formAlertText}</div>
    
    <form action="/admin/file-validation" method="post" enctype="multipart/form-data">
        <fieldset class="form-group">
            <label for="archivo_validar">Archivo a validar: (*.pdf)</label>
            <input type="file" id="archivo_validar" name="archivo_validar" class="form-control"/>
            </fieldset>
        <fieldset class="form-group">
            <label for="doc_firma_envio_uuid">UUID Firma Documento SICDI:</label>
            <input type="text" id="doc_firma_envio_uuid" name="doc_firma_envio_uuid" class="form-control" value="${doc_firma_envio_uuid?if_exists}"/>
            </fieldset>

        <button type="submit" class="btn btn-success btn-sm">Enviar</button>
        </form>

    <#if fileValidation?? && fileValidation.valid >
    <hr></hr>
    <table class="table table-bordered">
        <tr><th scope="row">Asunto:</th><td>${fileValidation.asuntoDocumento}</td></tr> 
        <tr><th scope="row">Unidad origen:</th><td>${fileValidation.unidadOrigen}</td></tr> 
        <#if fileValidation.unidadDestino?? >
        <tr><th scope="row">Unidad destino:</th><td>${fileValidation.unidadDestino}</td></tr> 
        </#if>
        <tr><th scope="row">Usuario creador:</th><td>${fileValidation.nombreUsuarioCreador}</td></tr> 
        <tr><th scope="row">Fecha de creación:</th><td>${fileValidation.fechaCreacion?string["yyyy-MM-dd, HH:mm"]}</td></tr> 
        <tr><th scope="row">Usuario firma y envío:</th><td>${fileValidation.nombreUsuarioFirmaYEnvio}</td></tr> 
        <tr><th scope="row">Fecha firma y envío:</th><td>${fileValidation.fechaFirmaEnvio?string["yyyy-MM-dd, HH:mm"]}</td></tr> 
        <tr><th scope="row">Clasificación:</th><td>${fileValidation.nombreClasificacion}</td></tr> 
        <tr><th scope="row">Número de radicado:</th><td>${fileValidation.numeroRadicado}</td></tr> 
        </table>
    </#if>

<#if templatePrefix??>
    <#include templatePrefix + "-footer.ftl">
<#else>
    <#include "footer.ftl">
</#if>