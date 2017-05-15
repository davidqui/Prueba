<#assign pageTitle = "Tablas básicas (cambiar)">
<#assign activePill = "cambiar">
<#include "admin-header.ftl">


<table class="table">
	<thead class="thead-inverse">
		<tr>
			<th>Nombre</th>
		</tr>
	</thead>
	<tbody>
		<#list list as x>
			<tr>
				<td><a href="editor/${x.name}">${x.label}</a></td>
			</tr>
		</#list>
	</tbody>
</table>

<#include "admin-footer.ftl">
