<#assign pageTitle = "Selección de expediente" />
<#include "header.ftl" />

<div class="container-fluid">
	<div class="card">
		<div class="card-header">
			<h4>Seleccione el expediente, ¡es opcional!</h4>
			<p>Un documento puede asociarse a un expediente en particular. Aquí podra seleccionar el expediente al que desea que el documento quede asociado.</p>
		</div>
		<div class="card-block">
			<form action="">
	            <fieldset class="form-group">
	                <label for="expId">Expediente</label>
	                <select class="form-control" id="expId" name="expId">
	                    <#if expedientes??>
	                        <option value="0"></option>
	                        <#list expedientes as exp>
	                            <option value="${exp.id}">${exp.nombre}</option>
	                        </#list>
	                    </#if>
	                </select>
	            </fieldset>
	            <div>
		            <a href="#" class="btn btn-success" onclick="redirect();">Continuar</a>
		            <a href="${cancelUrl}" class="btn btn-secondary">Cancelar</a>
                    <script type="text/javascript">
	                    function redirect() {
	                    	url = "${returnUrl}&expId=" + $("#expId").val();
	                    	$(location).attr("href", url);
	                    }
					</script>
	            </div>
			</form>
		</div>
	</div>
</div>
<#include "footer.ftl" />