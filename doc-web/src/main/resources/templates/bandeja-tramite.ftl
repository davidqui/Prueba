<#if !pageTitle??>
  <#assign pageTitle = "Bandeja en trámite" />
</#if>
<#include "bandeja-header.ftl">

<#if error??> 
  <div class="jumbotron">
    <h2 class="display-1">Algo anda mal...</h2>
    <p class="lead">No se puede construir la bandeja debido a un problema interno del sistema. Intente nuevamente por favor.</p>
  </div>
<#else>
	<#--
		2017-07-10 jgarcia@controltechcg.com Issue #115 (SICDI-Controltech) feature-115:
		Adición de formulario con fitro de fecha en bandejas diferentes a la de entrada.	 
	 -->
	<#if fechaInicial??>
		<#assign fechaInicialValor = fechaInicial?string["yyyy-MM-dd"] />
	<#else>
		<#assign fechaInicialValor = "" />
	</#if>
	
	<#if fechaFinal??>
		<#assign fechaFinalValor = fechaFinal?string["yyyy-MM-dd"] />
	<#else>
		<#assign fechaFinalValor = "" />
	</#if>
	  
  	<form action="/bandeja/entramite" method="GET" class="form-inline">
  		<div class="form-group">
  			<label for="fechaInicial">Fecha Inicial</label>
  			<input class="form-control datepicker" id="fechaInicial" name="fechaInicial" value="${fechaInicialValor}" />
  		</div>
  		<div class="form-group">
  			<label>Fecha Final</label>
  			<input class="form-control datepicker" id="fechaFinal" name="fechaFinal" value="${fechaFinalValor}" />
  		</div>
  		<button type="submit" class="btn btn-success">Buscar</button>
  	</form>
  	
  	<#--
  		2017-07-10 jgarcia@controltechcg.com Issue #115 (SICDI-Controltech) feature-115:
  		Modificación presentación de mensaje de resultados, para hacer referencia al rango de fechas.
  	--> 	
	<#if !documentos?? || documentos?size == 0 >
	  <div class="jumbotron">
	    <h2 class="display-1">No hay documentos para el rango.</h2>
	    <p class="lead">No existen documentos en esta bandeja para el rango de fechas seleccionado entre ${fechaInicialValor} y ${fechaFinalValor}.</p>
	  </div>
	<#else>
		<div style="margin-top:10px; margin-bottom:10px;">
			<span class="label label-default" style="font-size:12px;">
				${documentos?size} documentos entre ${fechaInicialValor} y ${fechaFinalValor}.
			</span>
		</div>
			
      <#list documentos as x>
      	<div class="card">
      		<div class="card-block">
      			<div class="container-fluid">
      				<div class="row">
      					<div class="col-sm-<#if (x.radicado)??>7<#else>11</#if>">
      						<strong><a href="/proceso/instancia?pin=${x.instancia.id}">${(x.asunto)!"&lt;Sin asunto&gt;"}</a></strong>
      					</div>
		      			<#if (x.radicado)??>
	      					<div class="col-sm-4">
				      			<div>
				      				<strong>Radicado: </strong>${x.radicado}
				      			</div>
		    				</div>
      			  		</#if>
      					<div class="col-sm-1">
	    				</div>
					</div>
      				<div class="row">
      					<div class="col-sm-4">
      						<strong>Fecha de creación:</strong>&nbsp;${x.cuando?string('yyyy-MM-dd hh:mm a')}<#if (x.plazo)?? > <strong>Plazo:&nbsp;</strong><span class="label label-${x.semaforo}">${x.plazo?string('yyyy-MM-dd')}</span></#if> 
      					</div>
      					<div class="col-sm-4">
      						<#--
      							2017-05-15 jgarcia@controltechcg.com Issue #78 (SICDI-Controltech) feature-78:
      							Presentar información básica de los usuarios asignadores y asignados en las
      							bandejas del sistema.
      						-->         					
      						<#if (x.instancia.asignado)??><strong>Asignado a: </strong>${(usuarioService.mostrarInformacionBasica(x.instancia.asignado))!"&lt;No asignado&gt;"}</#if>
      					</div>
      					<div class="col-sm-4">
      						<strong>Asignado por: </strong><#if (x.usuarioUltimaAccion)?? > ${usuarioService.mostrarInformacionBasica(x.usuarioUltimaAccion)} </#if>
      					</div>
					<#--
						2017-05-15 jgarcia@controltechcg.com Issue #78 (SICDI-Controltech) feature-78:
						Corrección presentación acciones en las bandejas.
					-->        					
      				</div>
      				<div class="row">	      					
	            		<div class="col-sm-4">
	            			<#--
	            				2017-05-15 jgarcia@controltechcg.com Issue #81 (SICDI-Controltech):
	            				hotfix-81 -> Validación para determinar si se deben presentar transiciones para los documentos en la bandeja de enviados
	            				y en trámite.
	            			-->
	            			<#if x.presentarTransiciones() >
					            <#assign transiciones = x.instancia.transiciones() />
					            <#if transiciones?? && transiciones?size &gt; 0 >
					            		<strong>Acciones:</strong>
										<#list transiciones as t>
					                  		<a href="${t.replace(x.instancia)}">${t.nombre}...&nbsp;&nbsp;&nbsp;</a>
						             	</#list>
					            </#if>
					        </#if>
						</div>
					</div>
				</div>
      		</div>
      	</div>
      </#list>
	</#if>
</#if>
<#include "bandeja-footer.ftl">