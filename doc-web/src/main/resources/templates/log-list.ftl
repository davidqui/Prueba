<#assign pageTitle = "Administración Logs" />
<#include "gen-macros.ftl">
<#if templatePrefix??>
    <#include templatePrefix + "-header.ftl">
<#else>
    <#include "admin-header.ftl">
</#if>

<div class="container-fluid">

	<#if list?size = 0 >
    	<p class="alert alert-warning">No se encontraron registros para el usuario ingresado</p>
    </#if>
    
	
    <h1 class="cus-h1-page-title">${pageTitle}</h1>
    <p>
    
	<form action="/admin/logs/usuario" method="GET" enctype="multipart/form-data" >
		<fieldset class="form-group">
			<table>
				<tr>
		        	<td style="padding-right: 16px;"><label for="usuario">Login</label></td>
		        	<td><input type="text" class="form-control" id="login" name="usuario" value="${(usuario)!""}"/><td>
		        	<td><button type="submit" class="btn btn-success btn-sm">Consulta el log del usuario</button></td>	 
		       </tr> 
	       </table>	       
	    </fieldset>
            <div style="display: flex;">
                <input name="filtro" maxlength="150" size="30" class="form-control" type="text" style="width: 80%" value="${(filtro)!""}">    
                <button type="submit" class="btn btn-primary" onclick="loading(event);" style="width: 20%">
                    <span class="hidden-md-down"><img class="card-img-top" src="/img/search.svg" alt=""> Buscar</span><span class="hidden-lg-up"><img class="card-img-top" src="/img/search.svg" alt=""></span>
                </button>
            </div>
    </form>
    
    <br />
    Total ${list?size}
    
        <br />
        <!-- 2017-02-24 jgarcia@controltechcg.com Issue #111: Se amplía el campo de búsqueda a 150 caracteres. -->
        <table class="table" id="tabla_logs">
            <thead class="thead-inverse">
                <tr>
	                <#list descriptor.listProperties() as p>
	                    <th>${p.label}</th>
	                </#list>                    
                </tr>
            </thead>
            <tbody>
            <#list list as x>
                <tr>                
	                <#list descriptor.listProperties() as p>
	                    <#assign vclasses = "" />                    
	                    <td class="${vclasses}">
	                        <div>${p.value(x)!""}</div>
	                    </td>
	                </#list>                	
                </tr>
            </#list>
            </tbody>
        </table>
        <#if  totalPages?? && totalPages gt 0>
            <@printBar url="/admin/logs/usuario" params={"filtro": filtro!"", "usuario": usuario!"" } metodo="get"/>
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
        
        $('#txt_tabla_logs').keyup(function(){                 
                buscarEnTabla( $(this).val(), '#tabla_logs' );
        });
        
    });             
</script>

<#if templatePrefix??>
    <#include templatePrefix + "-footer.ftl">
<#else>
    <#include "footer.ftl">
</#if>
