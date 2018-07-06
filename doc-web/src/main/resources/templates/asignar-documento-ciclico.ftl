<!-- 2017-03-02 jgarcia@controltechcg.com Issue #149: Corrección de manejo de separador de miles -->
<#setting number_format="computer">

<#assign pageTitle = transicion.nombre />
<#include "header.ftl" />
<#include "gen-arbol-dependencias.ftl">


<div class="container-fluid">
	<div class="card">
		<div class="card-header">
			<h5><b>Asunto del Documento:</b><#if documento?? > ${documento.asunto}</#if></h5>
			<h5 style="margin-botton:20px;"><#if documento?? ><b>Grado de clasificación: <span style="color:red;">${documento.clasificacion.nombre}</span></b></#if> </h5>
			<h6>${transicion.nombre} - Selección de usuario<b></b></h6>
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
									<@listDependencias dependencias_arbol did/>
								<#else>									
									<@listDependencias dependencias_arbol/>
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
    <script src="/js/jstree.min.js"></script>
    <script src="/js/app/gen-arbol.js"></script>

    <script type="text/javascript">
        validarArbol("#arbol_list_dependenciasj",true,true);
    </script>
</#assign>

<#include "footer.ftl" />