<!-- 2017-03-02 jgarcia@controltechcg.com Issue #149: Corrección de manejo de separador de miles -->
<#setting number_format="computer">

<#assign pageTitle = "Seleccionar usuario" />
<#include "header.ftl" />
<div class="container-fluid">
	<div class="card">
		<div class="card-header">
			<h4>Asignar funcionario para dar respuesta</h4>
			<p>Seleccione el usuario de su dependencia que debe dar respuesta a este documento.</p>
			<p>
				<#if pid??>
					<a href="?pin=${documento.instancia.id}&tid=${tid}&did=${pid}" class="btn btn-secondary btn-sm">Regresar</a>
				<#else>
					<#if did??>
						<a href="?pin=${documento.instancia.id}&tid=${tid}" class="btn btn-secondary btn-sm">Regresar</a>
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
					<a href="?pin=${documento.instancia.id}&tid=${tid}&depId=${x.id}">Subdependencias...</a>
			    </label>
			    <br/>
			    <br/>
			</#list>
		</div>
		</#if>
		<form action="?pin=${documento.instancia.id}&tid=${tid}" method="POST">
		<div class="card-block">
			<#list usuarios as x>
			    <label class="c-input c-radio">
			        <input type="radio" name="usuario" value="${x.id}">
			        <span class="c-indicator"></span>
			        ${x}
			    </label>
			    <br/>
			    <br/>
			</#list>
		</div>
		<div class="card-header">
		    <button type="submit" class="btn btn-success">Asignar</button>
		    <a href="/proceso/instancia?pin=${documento.instancia.id}" class="btn btn-link">Cancelar</a>
		</div>
		</form>
	</div>
</div>
<#include "footer.ftl" />
