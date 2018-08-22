<#assign pageTitle = plantilla.nombre!"Plantilla" />
<#assign activePill = "plantilla" />
<#assign deferredJS = "" />
<#import "spring.ftl" as spring />
<#include "admin-header.ftl">
<#include "util-macros.ftl" />

<div class="container">
    <h1 class="cus-h1-page-title">${pageTitle}</h1>
    <p style="padding: 16px;
    border: 5px solid#2e73a1;
    font-size: 16px;">Mensaje:
    </br>
    Señor Usuario recuerde tener en cuenta los siguientes aspectos ante la <u>creación</u> o <u>actualización</u> de plantillas:
    </br>
    1.	El marcador que contiene el nombre de identificación de la plantilla no puede tener espacios, debe ser único para el conjunto de plantillas activas en el sistema y obedecer al formato <span style="color:red;">nombre_NombreUnicoPlantilla</span>.
    <span style="color:blue;"> Ejemplo: nombre_informeOperacional </span>
    </br>
    2.	El marcador que contiene la versión de la plantilla no puede tener espacios y debe obedecer al formato <span style="color:red;">version_DiaMesAño</span> en números.
    <span style="color:blue;">Ejemplo: version_09082018</span>
    </br>
    </br>
    <span style="color:red;">
        <b>Advertencia:</b> Cuando actualice el formato de alguna de las plantillas debe cambiar obligatoriamente el marcador de versión, de lo contrario el sistema permitirá la utilización de plantillas desactualizadas. 
    </span>
    </p>
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
			<th>Wildcards Requeridos</th>
		</tr>
	</thead>
	<tbody>
            <#if wildCardsPlantilla??>
                <#list wildCardsPlantilla as wildcard >
                    <tr>
                        <td>${wildcard.texto}</td>
                    </tr>
                </#list>
            </#if>
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