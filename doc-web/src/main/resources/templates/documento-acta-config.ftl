<#--
    2018-05-18 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
    feature-162: Archivo con la configuración inicial para los templates
    del proceso de Registro de Actas.
-->
<#setting number_format="computer">

<#assign pageTitle = documento.asunto!"Acta" />
<#assign deferredJS = "" />

<#assign headScripts>
<script src="/js/eventos_documento.js"></script>
<script src="/js/app/funciones-documento.js"></script>
<script src="/js/app/documento-acta-observaciones.js"></script>
<script src="/js/app/documento-acta.js"></script>
<script src="/js/app/documento-observaciones.js"></script>
<script src="/js/tinymce.min.js"></script>
</#assign>

<#if archivoHeader??>	
    <#include "archivo-header.ftl">
<#else>
    <#include "bandeja-header.ftl" />
</#if>

<#include "lib/documento_functions.ftl" />
<#include "gen-arbol-dependencias.ftl">
<#include "gen-arbol-trd.ftl">

<#include "lib/documento-acta/documento-acta-observaciones.ftl">
<#include "lib/documento-acta/documento-acta-info-proceso.ftl">
<#include "lib/documento-acta/documento-acta-carga-adjuntos.ftl">
<#include "lib/documento-acta/documento-acta-presentacion.ftl">

<#assign estadoModo = estadoModeMap[procesoInstancia.estado.id?string] />