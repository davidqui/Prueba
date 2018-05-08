<#if !fechaInicio??>
    <#assign fechaInicio = "" />
</#if>
<#if !fechaFin??>
    <#assign fechaFin = "" />
</#if>
<#if !asignado??>
    <#assign asignado = "" />
</#if>
<#if !radicado??>
    <#assign radicado = "" />
</#if>
<#if !destinatario??>
    <#assign destinatario = "" />
</#if>
<#if !clasificacion??>
    <#assign clasificacion = "" />
</#if>
<#if !asunto??>
    <#assign asunto = "" />
</#if>
<#if !dependenciaDestino??>
    <#assign dependenciaDestino = "" />
</#if>
<#if !dependenciaOrigen??>
    <#assign dependenciaOrigen = "" />
</#if>
<#if !term??>
    <#assign term = "" />
</#if>
<#if !clasificacionNombre??>
    <#assign clasificacionNombre = "" />
</#if>
<#if !dependenciaOrigenDescripcion??>
    <#assign dependenciaOrigenDescripcion = "" />
</#if>
<#if !dependenciaDestinoDescripcion??>
    <#assign dependenciaDestinoDescripcion = "" />
</#if>
<#--
    2018-05-08 jgarcia@controltechcg.com Issue #160 (SICDI-Controltech) feature-160:
    Asignación de valor por defecto para la Firma UUID.
-->
<#if !firmaUUID??>
    <#assign firmaUUID = "" />
</#if>