<#assign pageTitle = "Expedientes"/>
<#include "bandeja-header.ftl">

<div class="container-fluid">
	<div>
	<span>
		<a href="/prestamo/list-exp-prestados" class="btn btn-success btn-sm bd-popover" role="button" data-toggle="popover" data-trigger="hover" data-placement="right" title="Prestamos" data-content="Pulse visualizar el listado de expedientes prestados">Prestamos</a>
                <a href="/expediente/crear" class="btn btn-success btn-sm bd-popover float-right" role="button" data-toggle="popover" data-trigger="hover" data-placement="left" title="crear" data-content="Pulse para crear un nuevo expediente" style="float:right;" onclick="loading(event);">Nuevo Expediente</a>
        </span>
	</div>
	</br>
	
	<#if expedientes?size = 0 >
 		<div class="jumbotron">
    		<h1 class="display-1">No hay registros</h1>
    		<p class="lead">En este momento no existen expedientes disponibles en el archivo local</p>
 	   </div>
	<#else>
    <table class="table">
        <thead>
            <tr>
                <th>Nombre</th>
                <th>Fecha</th>
                <th>Dependencia</th>
                <th>TRD</th>
                <th>Estado</th>
                <th>Acciones</th>
            </tr>
        </thead>
        <tbody>
            <#list expedientes as exp>
                <tr>
                   <td><a href="/expediente/contenido?eid=${exp.id}" onclick="loading(event);">${exp.nombre}</a></td>
                    <td nowrap>${exp.cuando?string('yyyy-MM-dd')}</td>
                    <td>${exp.dependencia.nombre}</td>
                    <#if exp.trd??>
                    <td>${exp.trd.nombre}</td>
                    <#else>
                    <td></td>
                    </#if>
                    <td>${exp.estado}</td>
                    <#if exp.estado??&&exp.estado.nombre=="Cerrado">
            			<td nowrap><a id="archivarE" href="#" onclick="archiveEx(${exp.id});" tabindex="0" class="btn btn-sm btn-archivar bd-popover" role="button" data-toggle="popover" data-trigger="hover" data-placement="left" title="Archivar Expediente" data-content="Pulse para transferir al Archivo Central">A</a></td>
           			<#elseif exp.estado??&&exp.estado.nombre=="Abierto">
                        <td nowrap><a id="cerrarE" href="#"  onclick="cerrarExp(${exp.id});" tabindex="0" class="btn btn-sm btn-cerrarEx bd-popover" role="button" data-toggle="popover" data-trigger="hover" data-placement="left" title="Cerrar Expediente" data-content="Pulse para cerrar el expediente">C</a></td>
   				    </#if>
                 </tr>
            </#list>
        </tbody>
    </table>
  </#if>
</div>

<script type="text/javascript">
      function cerrarExp(data) {
          $.post("/expediente/cerrar",{
    		
        		eid: data },
        		function(data) {
        			location.reload(true);
        						
        		});       	
		}

	   function archiveEx(data) {
            				
        	$.post("/expediente/archivar",{
    		
        		eid: data },
            	function(data) {
			
				  location.reload(true);
			});       	
	  }
</script>

<#include "bandeja-footer.ftl">
