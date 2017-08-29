<#--
    2017-08-29 jgarcia@controltechcg.com Issue #120 (SICDI-Controltech) feature-120:
    Funciones Freemarker para transferencia de archivo.
-->

<#function getUsuarioDescripcion usuario >
    <#return usuario.grado + " " + usuario.nombre + " - [" + usuario.clasificacion.nombre + "]" />
</#function>

<#function getTipoDescripcion tipo >
    <#if tipo == "T" >
        <#return "Total" />
    <#elseif tipo == "P">
        <#return "Parcial (Recibida previamente)" />
    <#else>
        <#return "TIPO NO VÁLIDO: " + tipo />
    </#if>
</#function>