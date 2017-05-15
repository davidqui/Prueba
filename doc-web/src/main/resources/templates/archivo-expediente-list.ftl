<#include "archivo-expedientes-tabs.ftl">
<#include "util-macros.ftl" />

<div class="container-fluid"> 
<#if !expArchivados??>
 <div class="jumbotron">
    <h1 class="display-1">No hay registros</h1>
    <p class="lead">En este momento no existen expedientes en el archivo</p>
  </div>
<#else>
    <table class="table">
        <thead>
            <tr>
            	
                <th>Nombre</th>
                <th>Fecha transferencia</th>
                <th>Usuario transferencia</th>
                <th>Dependencia</th>
                <th>Estado</th>
                <th>Acciones</th>
            </tr>
        </thead>
        <tbody>
            <#list expArchivados as exp>
                <tr>
                
                    <td><a href="/prestamo/contenido?eid=${exp.expediente.id}">${exp.expediente.nombre}</a></td>
                    <#if exp.fechaTransferencia??>
                    	<td nowrap>${exp.fechaTransferencia?string('yyyy-MM-dd')}</td>
                    <#else>
                    	<td></td>
                    </#if>
                    <#if exp.usuarioTransferencia??>
                    	<td>${exp.usuarioTransferencia.nombre}</td>
                    <#else>
                    	<td></td>
                    </#if>
                    <td>${exp.expediente.dependencia.nombre}</td>
                    <td>${exp.estado.nombre}</td>
                    <#if exp.estado??&&exp.estado.nombre=="Archivado">
                   		<td nowrap><a id="prestar" href="#" onclick="location.href='/prestamo/form?eid=${exp.expediente.id}&depid=0&usuid=0';" tabindex="0" class="btn btn-sm btn-success bd-popover" role="button" data-toggle="popover" data-trigger="hover" data-placement="left" title="Prestar Expediente" data-content="Pulse para realizar prestamo de este expediente">P</a></td>
                     <#elseif exp.estado??&&exp.estado.nombre=="Prestado">
           		   		<td nowrap><a id="ver-prestamo" href="#" onclick="location.href='/prestamo/update-prestamo?eid=${exp.expediente.id}';" tabindex="0" class="btn btn-sm btn-danger bd-popover" role="button" data-toggle="popover" data-trigger="hover" data-placement="left" title="Expediente pestado" data-content="Pulse para visualizar o  modificar el prestamo de este expediente">P</a></td>
                   </#if>
               </tr>
            </#list>
        </tbody>
    </table>
 </#if>
</div>
<#include "bandeja-footer.ftl">
