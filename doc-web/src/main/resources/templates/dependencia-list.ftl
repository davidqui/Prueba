<!-- 2017-02-15 jgarcia@controltechcg.com Issue #147: Se asigna formato numerico para manejo de los miles -->
<#setting number_format="computer">

<#assign pageTitle = descriptor.label />
<#include "gen-macros.ftl">
<#if templatePrefix??>
    <#include templatePrefix + "-header.ftl">
<#else>
    <#include "admin-header.ftl">
</#if>

<div class="container-fluid">
    <h1 class="cus-h1-page-title">${pageTitle}</h1>
    <#if descriptor.description??>
    <p class="lead">${descriptor.description}</p>
    </#if>
    <p>
        <#if returnUrl??><a href="${returnUrl}" class="btn btn-secondary btn-sm">Regresar</a></#if>
        <a href="/dependencias/create" class="btn btn-success btn-sm">Crear nuevo</a>
        </p>

    Total ${list?size}

    <#if descriptor.properties?size == 0>
    <div class="jumbotron">
        <h1>No hay propiedades configuradas</h1>
        <p>Por alguna razón la lista de propiedades está vacía. Por lo general es automático el reconocimiento de propiedades a menos que todas las propiedades estén marcadas como @Transient.</p>
        </div>
    <#else>
        <#if all?? && all>
    <a class="btn btn-link" href="?all=false">No mostrar eliminados</a>
        <#else>
    <a class="btn btn-link" href="?all=true">Mostrar eliminados</a>
        </#if>
    <br />
    <!-- 2017-02-24 jgarcia@controltechcg.com Issue #111: Se amplía el campo de búsqueda a 150 caracteres. -->
    Buscador general <input id="txt_tabla_dependencias" maxlength="150" size="30" class="form-control" type="text">            
    <table class="table" id="tabla_dependencias">
        <thead class="thead-inverse">
            <tr>
                <#list descriptor.listProperties() as p>
                <th>${p.label}</th>
                </#list>
                <th>Acciones</th>
                </tr>
            </thead>
        <tbody>
            <#list list as x>
            <tr>
                <#assign first = true />
                <#list descriptor.listProperties() as p>
                    <#assign vclasses = "" />
                    <#if !x.activo?? || x.activo == false >
                        <#assign vclasses = "cus-deleted" />
                    </#if>
                <td class="${vclasses}">
                        <#if p.type == "Boolean">
                            <#assign pvalue = p.value(x)!false />
                    <div>${pvalue?string("X", "")}</div>
                        <#else>
                        	<#if p.name == "padre">
                    <div>${controller.nombreDependenciaPadre((p.value(x)!"")?string)!""}</div>
                            <#else>
                    <div>${p.value(x)!""}</div>
                            </#if>
                        </#if>
                        <#if first && descriptor.actions?size != 0 >
                        <div>
                                <#list descriptor.actions as a>
                                    <@accion a x />
                                </#list>
                        </div>
                        </#if>
                        <#assign first = false />
                    </td>
                </#list>
                <td nowrap="nowrap">
                        <#if x.activo?? && x.activo == true >
                    <a href=${"/dependencias/edit?id=" + x.idString} class="btn btn-sm btn-warning" title="Modificar">M</a>
                    <a href=${"/dependencias/delete?id=" + x.idString} class="btn btn-sm btn-danger" title="Eliminar" onclick="return confirm('¿Está seguro que desea eliminar el registro?');">E</a>
                        </#if>
                        <#if x.cuando?? >
                            <#if x.cuandoMod?? >
                    <a tabindex="0" class="btn btn-sm btn-primary bd-popover" role="button" data-toggle="popover" data-trigger="hover" data-placement="left" title="Historial" data-content="Creado por: ${x.quien} el día ${x.cuando}, Modificado por: ${x.quienMod!"Nadie"} el día ${x.cuandoMod!"Ninguno"}">H</a>
                            <#else>
                    <a tabindex="0" class="btn btn-sm btn-primary bd-popover" role="button" data-toggle="popover" data-trigger="hover" data-placement="left" title="Historial" data-content="Creado por: ${x.quien} el día ${x.cuando}">H</a>
                            </#if>
                        </#if>
                    </td>
                </tr>
            </#list>
            </tbody>
        </table>
    </#if>

    </div>

<script>
         
    function buscarEnTabla(inputVal, idTabla) {
        // 2017-02-06 jgarcia@controltechcg.com Issue #111: Corrección del valor de entrada por formulario 
        // para tener la expresión regular necesaria que cumpla las condiciones de búsqueda cuando esta
        // se realiza con valores que contienen paréntesis.
        var fixedInputVal = inputVal.replace("(", "\\(").replace(")", "\\)");
    	
            var table = $(idTabla);
            table.find('tr').each(function (index, fila)
            {
                var celdas = $(fila).find('td');
                if (celdas.length > 0)
                {
                    celdas.each(function (index, td)
                    {
                        $(td).removeAttr('style');	                
                    });	            
                }
	        
	        
                if (celdas.length > 0)
                {
                    var encontro = false;
                    celdas.each(function (index, td)
                    { 
                        // Issue #111
                        var regExp = new RegExp(fixedInputVal, 'i');
                        if (regExp.test($(td).text()))
                        {   
                                // Issue #111
                                if( fixedInputVal != null && fixedInputVal != "" && fixedInputVal.length > 0 ){    	                	   
                                        $(td).css("background-color","yellow");              
                                }
                            encontro = true;
                            return false;
                        }
                    });
                    if (encontro === true) {
                        $(fila).show();
                    } else {
                        $(fila).hide();
                    }
                }
        });
        }
	     
    $(function () {
        
        $('#txt_tabla_dependencias').keyup(function(){                 
                buscarEnTabla( $(this).val(), '#tabla_dependencias' );
        });
        
    });             
    </script>

<#if templatePrefix??>
    <#include templatePrefix + "-footer.ftl">
<#else>
    <#include "footer.ftl">
</#if>
