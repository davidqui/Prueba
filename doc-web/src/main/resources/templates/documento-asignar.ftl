<!-- 2017-03-02 jgarcia@controltechcg.com Issue #149: Corrección de manejo de separador de miles -->
<#setting number_format="computer">

<#assign pageTitle = "Selección de asignación" />

<#include "header.ftl" />
<#include "gen-arbol-dependencias.ftl">

<div class="container-fluid">
	<#if mode == 'nomode'>
		<div class="card">
			<div class="card-header">
				<h4>Error de configuración</h4>
				<p>La variable <strong>mode</strong> no se ha establecido</p>
			</div>
			<div class="card-block">
				<p>La manera correcta de invocar este resurso es con la variable <strong>mode</strong> con alguno de los siguientes valores</p>
				<ul>
					<li><strong>mijefe</strong>. Asigna el documento automáticamente al jefe de la dependencia del usuario que realiza la operación (el que oprime el botón).</li>
					<li><strong>usuario</strong>. Indica que se debe seleccionar un usuario correspondiente a la dependencia a la que está asignada el documento.</li>
				</ul>
			</div>
		</div>
	</#if>
	<#if mode == 'usuario'>
			
		<div class="card">
			<div class="card-header">
			<h3><b><#if documento?? >${documento.asunto}</#if></b></h3>
			<h5><#if documento?? >Nivel de clasifición del documento: ${documento.clasificacion.nombre} </#if> </h5>
			<h6>Selección de usuario</h6>
                        <p>Seleccione el usuario que se encuentra en su misma dependencia a quien será asignada a siguiente actividad en el proceso.</p>
			</div>			
			<form action="?pin=${pin}&tid=${tid}&mode=${mode}" method="POST">
			<div class="card-block">
                            <div class="row">
                                <div class="col-md-7">
                                    <div id="arbol_list_dependenciasj">
                                        <#if did??>
                                            <@listDependencias dependencias did />
                                            <#else>
                                                <@listDependencias dependencias />
                                        </#if>
                                    </div>
                                </div>
                                <div class="col-md-5">
                                    <h5>Usuarios</h5>
                                    <#if usuarios??>
                                        <#list usuarios as u>
                                            <div>
                                                <#if u.restriccionDocumentoNivelAcceso== true>
                                                    <label style="color:#FF0000">								
                                                                                                        ${u} ${u.mensajeNivelAcceso}
                                                                                                    </label>
                                                    <#else>
                                                        <label class="c-input c-radio" style="color:#5cb85c">
                                                                                                        <input type="radio" name="uid" value="${u.id}"><span class="c-indicator"></span>${u} ${u.mensajeNivelAcceso}
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
			<br /><br />
			<nav class="navbar navbar-default navbar-fixed-bottom text-xs-center hermes-bottombar">
			    <button type="submit" class="btn btn-success">Asignar</button>
			    <a href="/proceso/instancia?pin=${pin}" class="btn btn-link">Cancelar</a>
			</div>
			</form>
		</div>
	</#if>
	<div id="event_result" />
</div>   

<#assign deferredJS>  
  <!-- 5 include the minified jstree source -->
    <script src="/js/jstree.min.js"></script>
    <script src="/js/app/gen-arbol.js"></script>

    <script type="text/javascript">
        validarArbol("#arbol_list_dependenciasj");
    </script>
</#assign>

<#include "footer.ftl" />
