<#setting number_format="computer">

<#assign pageTitle = "Reasignar" />

<#include "bandeja-header.ftl" />

<form action="/documento/reasignar-ciclico?pin=${pin}" method="POST">
<#assign hayUnidades = unidades?? && (unidades?size > 0) />

    <div class="card">
        <div class="card-header">
            <h4>Selección de Unidad</h4>
            <p>Seleccione la unidad a la cual será reasignada la siguiente actividad en el proceso del documento "${documento.asunto}".</p>
        </div>
        
        <div class="card-block">
        	<#if hayUnidades >
	            <#list unidades as unidad >
	                <label class="c-input c-radio">
	                    <input type="radio" name="depId" value="${unidad.id}">
	                    <span class="c-indicator"></span>
	                    ${unidad}
	                </label>
	                <br/>
	            </#list>
            <div class="m-y">
                <fieldset class="form-group">
                    <textarea name="observacion" placeholder="Escriba un comentario para el nuevo asignado..." class="form-control"></textarea>
                </fieldset>
            </div>
            <#else>
            	No hay Unidades para seleccionar.
            </#if>
        </div>
        
        <div class="card-footer">
	        <#if hayUnidades >
		    	<button type="submit" class="btn btn-danger">Reasignar</button>
		    </#if>
		    <a href="/proceso/instancia?pin=${pin}" class="btn">Cancelar</a>
        </div>
    </div>
</form>

<#include "bandeja-footer.ftl" />