<!-- 2017-02-15 jgarcia@controltechcg.com Issue #147: Se asigna formato numerico para manejo de los miles -->
<#setting number_format="computer">

<#assign pageTitle = descriptor.label />
<#include "gen-macros.ftl">
<#if templatePrefix??>
    <#include templatePrefix + "-header.ftl">
<#else>
    <#include "header.ftl">
</#if>

<div class="container-fluid">
    <h1 class="cus-h1-page-title">${pageTitle} (Modificar)</h1>
    <#if descriptor.description??>
        <p class="lead">${descriptor.description}</p>
    </#if>

    <form action="<@savepath />" method="POST" enctype="multipart/form-data">
        <@hiddenids descriptor entity />
        <#list descriptor.createProperties() as p>
        <fieldset class="form-group">
            <label for="${p.name}">${p.label}</label>
            <@inputedit p entity />
        </fieldset>
        </#list>
    <p>
        <button type="submit" class="btn btn-success">Guardar</button> 
        <a href="<@listpath />" class="btn btn-secondary">Cancelar</a>
    </p>
    </form>

</div>

<#if templatePrefix??>
    <#include templatePrefix + "-footer.ftl">
<#else>
    <#include "footer.ftl">
</#if>
