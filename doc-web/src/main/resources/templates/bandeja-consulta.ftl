<#if !pageTitle??>
  <#assign pageTitle = "Consulta de documentos" />
</#if>
<#include "bandeja-header.ftl">

<div class="card">
	<div class="card-header">
		Criterios de búsqueda
	</div>
	<div class="card-block">
		<form method="GET" action="">
		  <fieldset class="form-group">
		    <label for="asunto">Asunto</label>
		    <input type="text" class="form-control" id="asunto">
		  </fieldset>
  		</form>
	</div>
</div>

<#include "bandeja-footer.ftl">