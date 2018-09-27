<!-- AQUI_ES_LO_DEL_MIL http://freemarker.org/docs/ref_directive_setting.html -->
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
        <a href="/usuarios/nuevo" class="btn btn-success btn-sm">Crear nuevo</a>
    </p>


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
        Buscador general
        <form method="GET" style="display: flex;">
            <input type="hidden" name="all" value="${all?string("true", "false")}">
            <input name="filtro" maxlength="150" size="30" class="form-control" type="text" style="width: 80%" value="${(filtro)!""}">    
            <button type="submit" class="btn btn-primary" onclick="loading(event);" style="width: 20%">
                <span class="hidden-md-down"><img class="card-img-top" src="/img/search.svg" alt=""> Buscar</span><span class="hidden-lg-up"><img class="card-img-top" src="/img/search.svg" alt=""></span>
                </button>
        </form>
        <table class="table" id="tabla_usuarios">
            <thead class="thead-inverse">
                <tr>
                <#list descriptor.listProperties() as p>
                    <#if !p.label?contains('Cargo') && !p.label?contains('Dominio')>
                        <th>${p.label}</th>
                    </#if>
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
                    <#if !p.label?contains('Cargo') && !p.label?contains('Dominio')>
                    <td class="${vclasses}">
                        <#if p.type == "Boolean">
                            <#assign pvalue = p.value(x)!false />
                            <div>${pvalue?string("X", "")}</div>
                        <#else>
                            <div>${p.value(x)!""}</div>
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
                    </#if>
                </#list>
                	<td nowrap="nowrap">
                        <#if x.activo?? && x.activo == true >
                            <a href=${"/usuarios/editar?id=" + x.id} class="btn btn-sm btn-warning" title="Modificar">M</a>
                            <!-- AQUI_ES_LO_DEL_MIL http://freemarker.org/docs/ref_builtins_number.html#ref_builtin_c -->
                            <a href=${"/usuarios/eliminar?id=" + x.id?c} class="btn btn-sm btn-danger" title="Eliminar" onclick="return confirm('¿Está seguro que desea eliminar el registro?');">E</a>
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
        <#if totalPages gt 0>
            <@printBar url="/usuarios" params={"filtro": filtro!"", "all": all?string("true", "false") } metodo="get"/>
        </#if>
    </#if>

</div>

<script>
         
    function buscarEnTabla(inputVal, idTabla) {
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
	                var regExp = new RegExp(inputVal, 'i');
	                if (regExp.test($(td).text()))
	                {   
	                	if( inputVal != null && inputVal != "" && inputVal.length > 0 ){    	                	   
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
        
        $('#txt_tabla_usuarios').keyup(function(){                 
                buscarEnTabla( $(this).val(), '#tabla_usuarios' );
        });
        
    });             
</script>


<#if templatePrefix??>
    <#include templatePrefix + "-footer.ftl">
<#else>
    <#include "footer.ftl">
</#if>
