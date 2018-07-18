<#setting number_format="computer">

<#assign pageTitle = "Reasignar" />

<#if !unidadId??>
    <#assign unidadId = "" />
</#if>
<#include "bandeja-header.ftl" />
<#include "gen-arbol-dependencias.ftl">

<form action="/documento/reasignar-ciclico?pin=${pin}" method="POST">
<#assign hayUnidades = unidades?? && (unidades?size > 0) />

    <div class="card">
        <div class="card-header">
            <h5><b>Asunto del Documento:</b><#if documento?? > ${documento.asunto}</#if></h5>
            <h5 style="margin-botton:20px;"><#if documento?? ><b>Grado de clasificación: <span style="color:red;">${documento.clasificacion.nombre}</span></b></#if> </h5>
            <h6>Selección de Unidad<b></b></h6>
            <p>Seleccione la unidad a la cual será reasignada la siguiente actividad en el proceso del documento.</p>
        </div>
        
        <div class="card-block">
        	<#if hayUnidades >
	            <div class="row">
                        <div class="col-md-7">
                            <div id="arbol_list_dependenciasj">
                                <@listDependencias dependencias=unidades href=false/>
                            </div>
                        </div>
                    </div>
                    <input type="hidden" name="depId" id="depId" value="${unidadId}"/>
                    <br/>
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
		    	<button type="submit" class="btn btn-success">Reasignar</button>
		    </#if>
		    <a href="/proceso/instancia?pin=${pin}" class="btn">Cancelar</a>
        </div>
    </div>
</form>

<#assign deferredJS>
    <script src="/js/jstree.min.js"></script>
    <script src="/js/app/gen-arbol.js"></script>
    
    <script type="text/javascript">
      validarArbol("#arbol_list_dependenciasj",false,true);
    </script>
    
</#assign>
<#include "bandeja-footer.ftl" />
<script src="/js/app/gen-arbol-reasignar-ciclico.js"></script>