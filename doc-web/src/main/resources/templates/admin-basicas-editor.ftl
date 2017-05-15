<#assign pageTitle = descriptor.label>
<#assign activePill = descriptor.name>
<#include "admin-header.ftl">

<p>
<a class="btn btn-primary" href="../new/${tableName}">Crear registro en la tabla</a>
</p>
<#if errors?? && errors?size != 0>
	<div class="alert alert-danger">
		<h3>¡Datos no válidos en el filtro!</h3>
		<ul>
			<#list errors as x>
				<li>${x}</li>
			</#list>
		</ul>
	</div>
</#if>
<form method="get">
	<p>
		Total de registros encontrados: ${list?size}
	</p>
	<table class="table">
		<thead>
			<tr>
				<th><input type="submit" value="Filtrar" /></th>
				<#list descriptor.columns as x>
					<th class="${x.cssClass}"><input type="text" id="Q_${x.name}"
						name="Q_${x.name}" class="input-block-level" /></th>
					<#if "DATE" == x.type>
						<script type="text/javascript">
						<!--
							$(document).ready(function() {
								// Configura el control de fecha
								$("#Q_${x.name}").datepicker({
									dateFormat : 'yy-mm-dd',
									changeMonth : true,
									changeYear : true
								});
							});
						//-->
						</script>
					</#if>
				</#list>
				<th></th>
			</tr>
			<tr>
				<th></th>
				<#list descriptor.columns as x>
					<#assign url = orderUrlPrefix/>
					<#if orderUrlPrefix?contains("?")>
						<#assign url>${url}&amp;ORDER=${x.name}</#assign>
					<#else>
						<#assign url>${url}?ORDER=${x.name}</#assign>
					</#if>
					<th><a href="${url}">${x.label}</a></th>
				</#list>
				<th></th>
			</tr>
		</thead>
		<tbody>
			<#list list as x>
				<tr>
					<td>
						<a href="../editar/${tableName}/${x[descriptor.id.name]}" class="btn btn-primary btn-sm">Modificar</a>
					</td>
					<#list descriptor.columns as k>
						<td class="${k.cssClass}">
							<#if k.type == "DATE">
								<#if x[k.name] == null>
									NULL
								<#else>
									${x[k.name]}
								</#if>
							<#else>
								${x[k.name]}
							</#if>
						</td>
					</#list>
					<td class="bas-action">
						<a href="../borrar/${tableName}/${x[descriptor.id.name]}" class="btn btn-danger btn-sm"
						onclick="return confirm('¿Está seguro que quiere eliminar el registro seleccionado?');">Borrar</a>
					</td>
				</tr>
			</#list>
		</tbody>
	</table>
</form>
<#if list??><#else>
	<#if list?size == 0>
		<div class="alert alert-info">
			<h3>¡No hay resultados para el filtro!</h3>
			<p>Con la información puesta en el filtro el sistema no ha
				encontrado coincidencias, intente de nuevo con otros datos de filtro.</p>
		</div>
	</#if>
</#if>

<#include "admin-footer.ftl">
