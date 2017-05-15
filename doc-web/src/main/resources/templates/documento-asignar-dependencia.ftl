<!-- 2017-03-02 jgarcia@controltechcg.com Issue #149: Corrección de manejo de separador de miles -->
<#setting number_format="computer">

<#assign pageTitle = "Seleccionar dependencia" />
<#include "header.ftl" />
<div class="container-fluid">
	<div class="card">
		<div class="card-header">
			<h4>Selección de dependencia</h4>
			<p>Seleccione la dependencia o siga la jerarquía con el enlace de subdependencias</p>
			<p>
				<#if pid??>
					<a href="/documento/asignar-jefe-dependencia?pin=${pin}&tid=${tid}&did=${pid}" class="btn btn-secondary btn-sm">Regresar</a>
				<#else>
					<#if did??>
						<a href="/documento/asignar-jefe-dependencia?pin=${pin}&tid=${tid}" class="btn btn-secondary btn-sm">Regresar</a>
					</#if>
				</#if>
			</p>
		</div>
		<form action="/documento/asignar-jefe-dependencia?pin=${pin}&tid=${tid}" method="POST">
		<div class="card-block">
			<#list dependencias as x>
			    <label class="c-input c-radio">
			        <input type="radio" name="depId" value="${x.id}">
			        <span class="c-indicator"></span>
			        ${x}
					<a href="/documento/asignar-jefe-dependencia?pin=${pin}&tid=${tid}&did=${x.id}">Subdependencias...</a>
			    </label>
			    <br/>
			    <br/>
			</#list>
		</div>
		<div class="card-header">
		    <button type="submit" class="btn btn-success">Asignar</button>
		    <a href="/proceso/instancia?pin=${pin}" class="btn btn-link">Cancelar</a>
		</div>
		</form>
	</div>
</div>
<#include "footer.ftl" />