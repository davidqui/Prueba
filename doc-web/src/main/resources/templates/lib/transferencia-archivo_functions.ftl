<#--
    2017-08-29 jgarcia@controltechcg.com Issue #120 (SICDI-Controltech) feature-120:
    Funciones Freemarker para transferencia de archivo.
-->
<#--
    2017-11-10 edison.gonzalez@controltechcg.com Issue #131 (SICDI-Controltech) 
    feature-131: Cambio en la entidad usuario, se coloca llave foranea el grado.
-->
<#function getUsuarioDescripcion usuario >
    <#return usuario.usuGrado.id + " " + usuario.nombre + " - [" + usuario.clasificacion.nombre + "]" />
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