<#assign pageTitle = "Administraciòn Logs" />
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
		        	<td><label for="usuario">Login</label></td>
		        	<td><input type="text" class="form-control" id="login" name="usuario" /><td>
		        	<td><button type="submit" class="btn btn-success btn-sm">Consulta el log del usuario</button></td>	 
		       </tr> 
	       </table>	       
	    </fieldset>    
    </form>
    
    <br />
    Total ${list?size}
    
        <br />
        <!-- 2017-02-24 jgarcia@controltechcg.com Issue #111: Se amplía el campo de búsqueda a 150 caracteres. -->
        Buscador un registro en la tabla de actual <input id="txt_tabla_logs" maxlength="150" size="30" class="form-control" type="text">    
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
