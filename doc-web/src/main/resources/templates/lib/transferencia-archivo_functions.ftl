<#--
    2017-08-29 jgarcia@controltechcg.com Issue #120 (SICDI-Controltech) feature-120:
    Funciones Freemarker para transferencia de archivo.
-->

<#function getUsuarioDescripcion usuario >
    <#return usuario.grado + " " + usuario.nombre + " - [" + usuario.clasificacion.nombre + "]" />
</#function>