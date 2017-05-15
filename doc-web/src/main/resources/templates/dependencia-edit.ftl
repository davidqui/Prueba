<!-- 2017-02-15 jgarcia@controltechcg.com Issue #147: Se asigna formato numerico para manejo de los miles -->
<#setting number_format="computer">

<#assign pageTitle = descriptor.label />
<#include "gen-macros.ftl">
<#include "util-macros.ftl" />
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
	<@flash/>
    <form action="/dependencias/save" method="POST" enctype="multipart/form-data" onsubmit="return validacion()">
                
        <#if entity.id??>
	    	<input type="hidden" name="id" id="id" value="${entity.idString}" />
	    </#if>
        <#list descriptor.createProperties() as p>
	        <#if p.name == 'padre' >
	        	<fieldset class="form-group">
	        	<label for="${p.name}">${p.label}</label>
		        <select class="form-control" name="${p.name}" id="${p.name}">	                	        
		        	<option value=""></option>
		            <#list dependencias as dep>	                    
		                    <#if dep.idString == ((entity.idPadreString)!"")?string>
		                        <option value="${dep.idString}" selected="selected">${dep.nombre}</option>
		             		<#else>
		            			<option value="${dep.idString}">${dep.nombre}</option>
		             		</#if>
		             </#list>
		        </select>
		        </fieldset>
	        <#else>
		        <fieldset class="form-group">
		            <label for="${p.name}">${p.label}</label>
		            <@inputedit p entity />
		        </fieldset>
		    </#if>    
        </#list>
    <p>
        <button type="submit" class="btn btn-success">Guardar</button> 
        <a href="/dependencias" class="btn btn-secondary">Cancelar</a>
    </p>
    </form>

</div>

<script type="text/javascript">
        
        function validacion(){
            
            var codigo = $.trim( $("#codigo").val() );
            if( codigo.length <= 0 ){
            
            	alert("Debe ingresar el código de la dependencia");
            	return false;
            }
            
            var nombre = $.trim( $("#nombre").val() );
            if( nombre.length <= 0 ){
            
            	alert("Debe ingresar el nombre de la dependencia");
            	return false;
            }                        
            
            return true;
        }
     </script>

<#if templatePrefix??>
    <#include templatePrefix + "-footer.ftl">
<#else>
    <#include "footer.ftl">
</#if>
