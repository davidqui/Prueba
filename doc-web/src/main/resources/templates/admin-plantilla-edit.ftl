<#assign pageTitle = plantilla.nombre!"Plantilla" />
<#assign activePill = "plantilla" />
<#assign deferredJS = "" />
<#import "spring.ftl" as spring />
<#include "admin-header.ftl">
<#include "util-macros.ftl" />

<div class="container">
    <h1 class="cus-h1-page-title">${pageTitle}</h1>
    <form action="/admin/plantilla/save" method="POST" enctype="multipart/form-data" >
    <#if plantilla.id??>
    	<input type="hidden" name="id" id="id" value="${plantilla.id}" />
    </#if>
	    
	    <@spring.bind "plantilla.nombre" />
      <fieldset class="form-group">
        <label for="nombre">Nombre</label>
        <input type="text" class="form-control" id="nombre" name="nombre" value="${(plantilla.nombre)!""}"/><br />
      </fieldset>
	    <div class="error">
	        <@spring.showErrors "<br>"/>
	    </div>

	<div class="card">
		
	<fieldset class="form-group">	
		<label for="file">
			Plantilla de Office Word (*.docx) 
				<#if plantilla.plantillaTienePlantilla()> 
					<a href="/documento/download/${plantilla.docx4jDocumento}">
						Descargar la plantilla WORD actual para esta plantilla
					</a> 
				<#else> 
					<label style="color:#d9534f;">
						(Debe cargar la firma.) 
					</label> 
				</#if>  
		</label>
        <input class="form-control" type="file" name="file" id="file">
  	</fieldset>
	   
	 <#if plantilla.id??>
	 	<br />
	 	<table class="table">
	<thead class="thead-inverse">
		<tr>
			<th>Clave</th>
    		<th>Valor</th>
		</tr>
	</thead>
	<tbody>
		<tr>
    			<td>sigdi_param_documento_asunto</td>
    			<td>Asunto del documento</td>
    		</tr>
    		<tr>
    			<td>sigdi_param_documento_trd</td>
    			<td>TRD del documento</td>
    		</tr>
	</tbody>
</table>
    </#if>
                 
		
	 </div>
	 
       <div class="m-y">
        	<button id="btnGuardar" type="submit" class="btn btn-primary">Guardar</button>
        	<a href="/admin/plantilla" class="btn btn-secondary">Cancelar</a>
        </div>
      
    </form>

</div>

<#include "footer.ftl">