<!-- 2017-03-02 jgarcia@controltechcg.com Issue #149: Corrección de manejo de separador de miles -->
<#setting number_format="computer">

<#assign pageTitle = "Seleccionar usuario" />
<#include "header.ftl" />

<#macro listDependencias dependencias selected=0>
	<ul>		 
		<#list dependencias as d>									
			<#if d.subs?has_content >
				<li data-jstree='{ "opened" : false }'><a href="?pin=${pin}&tid=${tid}&did=${d.id}"><#if d.sigla?? > ${d.sigla} - </#if>${d.nombre} </a>				
					<@listDependencias d.subs selected />
				</li>	
			<#else>
				<#if d.id == selected>
					<li data-jstree='{ "opened" : false }'><strong style="background-color:#A9F5F2; padding:5px;"> <#if d.sigla?? > ${d.sigla} - </#if> ${d.nombre} </strong></li>
				<#else>
					<li data-jstree='{ "opened" : false }'><a href="?pin=${pin}&tid=${tid}&did=${d.id}"> <#if d.sigla?? > ${d.sigla} - </#if> ${d.nombre} </a></li>
				</#if>		
			</#if>			
		</#list>
	</ul>
</#macro>

<div class="container-fluid">
	<div class="card">
		<div class="card-header">
			<h4>Selección de usuario</h4>
			<p>Seleccione el usuario que se encuentra en su misma dependencia a quien será asignada a siguiente actividad en el proceso.</p>
			<p>
				<#if pid??>
					<a href="?pin=${pin}&tid=${tid}&did=${pid}" class="btn btn-secondary btn-sm">Regresar</a>
				<#else>
					<#if did??>
						<a href="?pin=${pin}&tid=${tid}" class="btn btn-secondary btn-sm">Regresar</a>
					</#if>
				</#if>
			</p>
		</div>
		<#if dependencias??>
		<div class="card-block">
			<#list dependencias as x>
			    <label class="c-input c-radio">
			        <input type="radio" name="depId" value="${x.id}">
			        <span class="c-indicator"></span>
			        ${x}
					<a href="/documento/asignar-cualquiera-dependencia?pin=${pin}&tid=${tid}&depId=${x.id}">Subdependencias...</a>
			    </label>
			    <br/>
			    <br/>
			</#list>
		</div>
		</#if>
		<#if usuarios??>
		<#--
			2017-03-16 jgarcia@controltechcg.com Issue #16 (SIGDI-Incidencias01): Opción de confirmación en asignación del documento.  
		 -->
		<form id="doc_asignar_cualquiera_form" action="?pin=${pin}&tid=${tid}" method="GET">
		<div class="card-block">
			
			<input type="hidden" name="pin" value="${pin}"/>
			<input type="hidden" name="tid" value="${tid}"/>
			
			<div class="container-fluid>
					<div class="row">
						<div class="col-md-7">							
							<div id="arbol_list_dependenciasj">
								<#if did?? >
									<@listDependencias dependencias_arbol did />
								<#else>									
									<@listDependencias dependencias_arbol />
								</#if>
							</div>
						</div>
						
						
						<div class="col-md-5">
							<h5>Usuarios</h5>
							<#if lista_usuarios??>
								<#list lista_usuarios as u>
								<div>
									<#if u.restriccionDocumentoNivelAcceso == true >
										<label style="color:#FF0000">								
									        ${u} ${u.mensajeNivelAcceso}
									    </label> 
							        <#else>
							        	<label class="c-input c-radio" style="color:#5cb85c">
									        <input type="radio" name="usuId" value="${u.id}"><span class="c-indicator"></span>${u} ${u.mensajeNivelAcceso}
									    </label>
							        </#if>
								</div>
								</#list>
							</#if>
						</div>
						
						
						
					</div>
				</div>
				<div class="clearfix"></div>
			
		</div>
		<div class="card-header">
		    <button type="submit" class="btn btn-success">Asignar</button>
		    <a href="/proceso/instancia?pin=${pin}" class="btn btn-link">Cancelar</a>
		</div>
		</form>
		</#if>
	</div>
</div>

<#assign deferredJS>  
  
  <!-- 4 include the jQuery library -->
  <script src="/js/jquery.min.js.1.12.1.js"></script>
  <!-- 5 include the minified jstree source -->
  <script src="/js/jstree.min.js"></script>
  
  <script>
  		var getUrlParameter = function getUrlParameter(sParam) {
		    var sPageURL = decodeURIComponent(window.location.search.substring(1)),
		        sURLVariables = sPageURL.split('&'),
		        sParameterName,
		        i;
		
		    for (i = 0; i < sURLVariables.length; i++) {
		        sParameterName = sURLVariables[i].split('=');
		
		        if (sParameterName[0] === sParam) {
		            return sParameterName[1] === undefined ? true : sParameterName[1];
		        }
		    }
		};
		
		$("#arbol_list_dependenciasj").jstree().bind('ready.jstree', function (event, data) {
			var idS = getUrlParameter('idseleccionado');  
			if( idS != undefined && idS != null ){
				data.instance._open_to( idS );	
			} 
		  	
		});
		  		
  		$('#arbol_list_dependenciasj')  			
  			.on("select_node.jstree", function (e, data) {
                    var newLoc = data.instance.get_node(data.node, true).children('a').attr('href');
                    var id = data.instance.get_node(data.node, true).attr('id');                    
                    if(window.location.href != newLoc){                                  	  	
                        document.location = newLoc + ("&idseleccionado="+id);
                    }
                })	  		
		  .jstree();
		  
		// 2017-03-16 jgarcia@controltechcg.com Issue #16 (SIGDI-Incidencias01): Opción de confirmación en asignación del documento.
		
		// $('#doc_asignar_cualquiera_form').submit(function(){
		//	 return confirm('Confirma la asignación a realizar?');
		// });
  </script>
</#assign>

<#include "footer.ftl" />
