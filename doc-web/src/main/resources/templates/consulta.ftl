<#if !pageTitle??>
  <#assign pageTitle = 'Resultados' />
</#if>
<#include "util-macros.ftl" />
<#include "header.ftl">

<br>
<span><a href="/consulta/parametros"><strong> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;&lt; &nbsp;REGRESAR</strong></a></span> 
<br><br>
<#if !documentos?? || documentos?size == 0 >
  <div class="jumbotron">

    <p class="lead">No se encontraron documentos</p>
  </div>
<#else>
<div class="col-md-8 col-lg-9">
  <table class="table">
    <thead class="cus-thead">
      <tr>
        <th><strong>${(totalResultados)}</strong></th>
      </tr>
    </thead>
    <tbody>
      <ul>
		      <#list documentos as x>
		        <li>
			          <div class="card-block">
				          	<span><a href="/proceso/instancia?pin=${x.instancia.id}"><strong>${(x.asunto)!"&lt;Sin asunto&gt;"}</strong></a></span>
							<!-- 2017-02-13 jgarcia@controltechcg.com Issue #78 Se cambia por Fecha Última Modificacion   -->
							<!-- <strong>Fecha de creación:</strong> ${x.instancia.proceso.cuando?string('yyyy-MM-dd hh:mm a')} -->
				            <strong>Fecha Última Modificación:</strong> ${x.cuandoMod?string('yyyy-MM-dd hh:mm a')} 
				            <strong>Proceso:</strong> ${x.instancia.proceso.nombre} 
				            <strong>Estado:</strong> ${x.instancia.estado.nombre}  
				            <strong>Usuario asignado:</strong> ${x.instancia.asignado.nombre}  
				            <strong>Enviado por:</strong> ${x.usuarioUltimaAccion!"&lt;Nadie&gt;"}  
				            <strong>Elabora:</strong> ${x.elabora!"&lt;Nadie&gt;"} 
				            <strong>Revisó:</strong> ${x.aprueba!"&lt;Nadie&gt;"}  
				            <strong>Visto bueno:</strong> ${x.vistoBueno!"&lt;Nadie&gt;"}  
				            <strong>Firma:</strong> ${x.firma!"&lt;Nadie&gt;"} 
				            <strong>Clasificación:</strong> ${x.clasificacion!"&lt;No aplica&gt;"} 
				            <strong>Radicado:</strong> ${x.radicado!"&lt;Sin radicado&gt;"}     
				        </div>
		          	</li>	          		          
		      	</#list>
		      </ul>
    </tbody>
  </table>
  </div>
</#if>
<#include "bandeja-footer.ftl">