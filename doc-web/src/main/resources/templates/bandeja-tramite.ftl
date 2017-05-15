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
	<#if !documentos?? || documentos?size == 0 >
	  <div class="jumbotron">
	    <h2 class="display-1">No hay más documentos</h2>
	    <p class="lead">En este momento no existen documentos en esta bandeja</p>
	  </div>
	<#else>
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
      						<#if (x.instancia.asignado)??><strong>Asignado a: </strong>${(x.instancia.asignado)!"&lt;No asignado&gt;"}</#if>
      					</div>
      					<div class="col-sm-4">
      						<strong>Asignado por: </strong><#if (x.usuarioUltimaAccion)?? > ${x.usuarioUltimaAccion} </#if>
      					</div>
	            		<div class="col-sm-4">
				            <#assign transiciones = x.instancia.transiciones() />
				            <#if transiciones?? && transiciones?size &gt; 0 >
				            		<strong>Acciones:</strong>
									<#list transiciones as t>
				                  		<a href="${t.replace(x.instancia)}">${t.nombre}...&nbsp;&nbsp;&nbsp;</a>
					             	</#list>
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