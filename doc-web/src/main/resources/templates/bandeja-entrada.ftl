<#if !pageTitle??>
  <#assign pageTitle = "Bandeja de entrada" />
</#if>
<#include "bandeja-header.ftl">

<#--
    2017-04-07 jgarcia@controltechcg.com Issue #37 (SIGDI-Controltech): Importación de template de funciones de documento. 
 -->
<#include "lib/documento_functions.ftl" />

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
      			  		<#--
      					<div class="col-sm-1">
			      			<div>
			      				<a href="?action=quitar&pin=${x.instancia.id}" class="btn btn-sm btn-danger">Quitar</a>
			      			</div>			      			
	    				</div>
	    				-->
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
                    <strong>Asignado por: </strong><#if (x.usuarioUltimaAccion)?? > ${usuarioService.mostrarInformacionBasica(x.usuarioUltimaAccion)} <#else> ${usuarioService.mostrarInformacionBasica(x.instancia.asignado)} </#if>
                    </div>
					<#--
						2017-05-15 jgarcia@controltechcg.com Issue #78 (SICDI-Controltech) feature-78:
						Corrección presentación acciones en las bandejas.
					-->        					
                </div>
            <div class="row">	
                <div class="col-sm-4">
	            			<#--
	            				2017-05-11 jgarcia@controltechcg.com Issue #77 (SICDI-Controltech):
	            				Validación para determinar si se deben presentar transiciones para los documentos. 
	            			-->
	            			<#assign transiciones = x.instancia.transiciones() />
	            			<#if x.presentarTransiciones() >
					            <#if transiciones?? && transiciones?size &gt; 0 >
                    <strong>Acciones:</strong>
									<#list transiciones as t>
										<#-- 
						        		    2017-04-07 jgarcia@controltechcg.com Issue #37 (SIGDI-Controltech): Modificación en el template de documento para validar
						        		    si el documento presentado corresponde a la respuesta de un documento relacionado en estado en construcción. En caso de
						        		    ser así, se valida si la transacción a presentar corresponde a "Anular", para no presentarla dentro de las opciones al
						        		    usuario en sesión.
						        		    
						        		    2017-05-18 jgarcia@controltechcg.com Issue #87 (SICDI-Controltech) feature-87:
						        		    Modificación en la bandeja de entrada y template de documento para validar si el documento presentado corresponde a la
						        		    respuesta de un documento relacionado en estado de revisión por jefe de dependencia. En caso de ser así, se valida si la
						        		    transición a presentar corresponde a "Anular", para no presentarla dentro de las opciones alusuario en sesión.
						        		    
						        		    2017-06-12 jgarcia@controltechcg.com Issue #93 (SICDI-Controltech) feature-93:
											Ajuste en bandejas para presentación de transiciones de anulación de documentos respuesta.
						        		-->
						        		<#if !((x.documentoRespuestaEnConstruccion() || x.documentoRespuestaEnRevisionJefeDependencia()) && isTransaccionAnular(t)) >
						        			<#if !isTransicionAnularRespuesta(t) >
                    <a href="${t.replace(x.instancia)}">${t.nombre}...&nbsp;&nbsp;&nbsp;</a>
						                    </#if>
				        				</#if>				        				
					             	</#list>
					            </#if>
					        <#--
					        	2017-06-12 jgarcia@controltechcg.com Issue #93 (SICDI-Controltech feature-93:
	        		    		Modificación para evaluar la presentación de transiciones de anulación en documentos respuesta en construcción.
					        -->
				        	<#elseif transiciones?? >
                    <strong>Acciones:</strong>
				        		<#list transiciones as transicion >				        		
				        			<#if (x.documentoRespuestaEnConstruccion() || x.documentoRespuestaEnRevisionJefeDependencia()) && isTransicionAnularRespuesta(transicion) >
                    <a href="${transicion.replace(x.instancia)}">${transicion.nombre}...&nbsp;&nbsp;&nbsp;</a>
				        			</#if>
				        		</#list>					            
				            </#if>
                    </div>
                </div>
            </div>
        </div>
    </div>
      </#list>
    <#--
        2017-10-17 edison.gonzalez@controltechcg.com Issue #132 (SICDI-Controltech feature-132:
        Se agrega items de visualizacion para la paginacion.
    -->
    <center>
            <ul class="pagination">
                <#if totalPages gt 0>
                    <li class="page-item"><a href="/bandeja/entrada?pageIndex=1" class="page-link"><<</a></li>
                </#if>
                <#if pageIndex gt 1>
                    <li class="page-item"><a href="/bandeja/entrada?pageIndex=${pageIndex - 1}" class="page-link"><</a></li>
                </#if>
                <#if totalPages gt 0>
                    <li class="page-item"><a href="/bandeja/entrada?pageIndex=${pageIndex}" class="page-link">${pageIndex}</a></li>
                </#if>
                <#if pageIndex lt (totalPages)>
                    <li class="page-item"><a href="/bandeja/entrada?pageIndex=${pageIndex + 1}" class="page-link">></a></li>
                </#if>
                <#if totalPages gt 0>
                    <li class="page-item"><a href="/bandeja/entrada?pageIndex=${totalPages}" class="page-link">>></a></li>
                </#if>
                <#if totalPages gt 0>
                    <strong>&emsp;&emsp;${labelInformacion}</strong>
                </#if>
            </ul>
    </center>
	</#if>
</#if>
<#include "bandeja-footer.ftl">