<!-- 2017-03-02 jgarcia@controltechcg.com Issue #149: Corrección de manejo de separador de miles -->
<#setting number_format="computer">

<#assign pageTitle = transicion.nombre />
<#include "header.ftl" />

<#macro listDependencias dependencias selected=0>
	<ul>		 
		<#list dependencias as dependencia >									
			<#if dependencia.subs?has_content >
				<li data-jstree='{ "opened" : false }'><a href="?pin=${pin}&tid=${tid}&did=${dependencia.id}"><#if dependencia.sigla?? > ${dependencia.sigla} - </#if>${dependencia.nombre} </a>				
					<@listDependencias dependencia.subs selected />
				</li>	
			<#else>
				<#if dependencia.id == selected>
					<li data-jstree='{ "opened" : false }'><strong style="background-color:#A9F5F2; padding:5px;"> <#if dependencia.sigla?? > ${dependencia.sigla} - </#if> ${dependencia.nombre} </strong></li>
				<#else>
					<li data-jstree='{ "opened" : false }'><a href="?pin=${pin}&tid=${tid}&did=${dependencia.id}"> <#if dependencia.sigla?? > ${dependencia.sigla} - </#if> ${dependencia.nombre} </a></li>
				</#if>		
			</#if>			
		</#list>
	</ul>
</#macro>

<div class="container-fluid">
	<div class="card">
		<div class="card-header">
			<h4>${transicion.nombre} - Selección de usuario</h4>
			<p>Seleccione el usuario dentro de su misma unidad a quien será asignado la siguiente actividad en el proceso del documento "${documento.asunto}".</p>
		</div>

		<#if usuarios??>
		<form id="asignar-documento-ciclico-form" action="?pin=${pin}&tid=${tid}" method="GET">
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
								<#list lista_usuarios as usuario >
								<div>
									<#if usuario.restriccionDocumentoNivelAcceso == true >
										<label style="color:#FF0000">								
									        ${usuario} ${usuario.mensajeNivelAcceso}
									    </label> 
							        <#else>
							        	<label class="c-input c-radio" style="color:#5cb85c">
									        <input type="radio" name="usuId" value="${usuario.id}"><span class="c-indicator"></span>${usuario} ${usuario.mensajeNivelAcceso}
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
		
			<div class="card-footer">
			    <button type="submit" class="btn btn-success">${transicion.nombre}</button>
			    <a href="/proceso/instancia?pin=${pin}" class="btn">Cancelar</a>
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
  </script>
</#assign>

<#include "footer.ftl" />
