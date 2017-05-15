<#assign pageTitle = "Expedientes"/>
<#include "header.ftl">
<#import "spring.ftl" as spring />
<div class="container-fluid">
<h3>Expedientes</h3>
<div class="alert alert-info" role="alert">
	<p class="alert-info">Seleccione el expediente de la lista al cual quiere mover el documento.</p>
</div>

<#if expedientes??>
    <table class="table">
        <thead class="cus-thead">
            <tr>
                <th>Nombre</th>
                <th>Fecha</th>
                <th>TRD</th>
                <th>Mover</th>
            </tr>
        </thead>
        <tbody>
            <#list expedientes as exp>
                <tr>
                    <td>${exp.nombre}</td>
                    <td nowrap>${exp.cuando?string('yyyy-MM-dd')}</td>
                    <#if exp.trd??>
                    <td>${exp.trd.nombre}</td>
                    <#else>
                    <td></td>
                    </#if>															
                    <td><input type="radio" name="depId" value="${exp.id}" onchange='listExpedientes(${exp.id})'></td>
				</tr>
            </#list>
        </tbody>
    </table>
    <#else>
         <p>${expMessage}</p>
</#if>
 <p>
    <a id="goBack" href="/expediente/contenido?eid=${eid}" class="btn btn-secondary">Regresar</a>
 </p>
</div>
<div id="alert-modal" class="modal fade" tabindex="-1" role="dialog">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title">Solicitud de confirmación</h4>
      </div>
      <div class="modal-body">
        <p class="alert alert-warning">¿Está seguro de mover el documento?</p>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
        <button id="move" type="button" class="btn btn-primary">Aceptar</button>
      </div>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
<script>
<!--

var expid=-1;

function listExpedientes(data) {
	
	expid=data;
	$('#alert-modal').modal("show");

}

$('#move').click(function() {

	window.location = "/documento/mover?docid=${docId}&expid="+expid;

});
	
-->
</script>
<#include "bandeja-footer.ftl">