<#assign pageTitle = "Solicitudes de prestamo"/>
<#include "util-macros.ftl" />
<#include "archivo-header.ftl">

<div class="container-fluid">
<h3>Solicitudes de prestamo</h3>
</br>
<#if !solicitudes??>
 <div class="jumbotron">
    <h1 class="display-1">No hay registros</h1>
    <p class="lead">En este momento no existen solicitudes de prestamo pendientes.</p>
  </div>
<#else>
    <table class="table">
        <thead>
            <tr>
            	<th>Tipo</th>
            	<th>Nombre</th>
                <th>Dependencia</th>
                <th>Funcionario</th>
                <th>Grado</th>
                <th>Fecha solicitud</th>
                <th>Fecha devolución</th>
                <th>Acciones</th>
            </tr>
        </thead>
        <tbody>
            <#list solicitudes as sol>
                <tr>
                	<#if sol.expediente??>
                	<td>E</td>
                	<#elseif sol.documento??>
                	<td>E</td>
                	</#if>
                	<#if sol.expediente??><td><a href="/expediente/contenido?eid=${sol.expediente.id}">${sol.expediente.nombre}</a></td></#if>
                	<#if sol.documento??><td><a href="/expediente/contenido?eid=${sol.documento.id}">${sol.documento.asunto}</a></td></#if>
                    <td>${sol.dependencia}</td>
                    <td>${sol.usuarioSolicita.nombre}</td>
                    <#if sol.usuarioSolicita.grado??>
                    	<td>${sol.usuarioSolicita.grado}</td>
                    <#else>
                     	<td></td>
                    </#if>
                    <td>${sol.fechaSolicitud?string('yyyy-MM-dd')}</td>
                    <#if sol.fechaDevolucion??>
                    	<td>${sol.fechaDevolucion?string('yyyy-MM-dd')}</td>
                    <#else>
                    	<td></td>
                    </#if>
                    <#if sol.expediente??><td nowrap><a id="prestar" href="#" onclick="location.href='/prestamo/form?eid=${sol.expediente.id}&depid=${sol.dependencia.id}&usuid=${sol.usuarioSolicita.id}';" tabindex="0" class="btn btn-sm btn-success bd-popover" role="button" data-toggle="popover" data-trigger="hover" data-placement="left" title="Prestar Expediente" data-content="Pulse para realizar prestamo de este expediente">P</a></td></#if>
               		<#if sol.documento??><td nowrap><a id="prestarDoc" href="#" onclick="location.href='/prestamo/prestamo-doc?docid=${sol.documento.id}&depid=${sol.dependencia.id}&usuid=${sol.usuarioSolicita.id}';"  tabindex="0" class="btn btn-sm btn-success bd-popover" role="button" data-toggle="popover" data-trigger="hover" data-placement="left" title="Prestar Documento" data-content="Pulse para realizar prestamo de este documento">P</a></td></#if>
               </tr>
            </#list>
        </tbody>
    </table>
 </#if>
</div>
<#include "bandeja-footer.ftl">
