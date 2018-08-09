<#assign pageTitle = "Expedientes"/>
<#include "bandeja-header.ftl">
<#include "gen-paginacion.ftl">

<div class="container-fluid">
    <div>
        <ol class="breadcrumb">
                <li class="active">Inicio</li>
        </ol>
    </div>
    <div>
        <span>
            <a href="/expediente/crear" class="btn btn-success btn-sm bd-popover float-right" role="button" data-toggle="popover" data-trigger="hover" data-placement="right" title="crear" data-content="Pulse para crear un nuevo expediente" style="float:left;">Nuevo Expediente</a>
        </span>
    </div>
    </br>
	
    <#if expedientes?size = 0 >
        </br>
        </br>
        <div class="jumbotron">
            <h1 class="display-1">No hay registros</h1>
       </div>
    <#else>
        </br>
        </br>
        <table class="table">
            <thead>
                <tr>
                <th></th>
                    <th>Nombre</th>
                    <th>Fecha</th>
                    <th>Dependencia</th>
                    <th>TRD principal</th>
                    <th>Acción</th>
                </tr>
            </thead>
            <tbody>
                <#list expedientes as exp>
                    <tr>
                        <td>
                            <img class="svg" src="/img/folder.svg" alt=""/>
                        </td>
                        <td><a href="/expediente/listarDocumentos?expId=${exp.expId}">${exp.expNombre!""}</a></td>
                        <td nowrap>${exp.fecCreacion?string('yyyy-MM-dd')}</td>
                        <td>${exp.depNombre!""}</td>
                        <td>${exp.trdNomIdPrincipal!""}</td>
                        <td>
                            <#if exp.indUsuarioAsignado == 0>
                                <a title="Ver detalle" href="/expediente/administrarExpediente?expId=${exp.expId}">
                                    <img class="card-img-top" src="/img/eye.svg" alt="">
                                </a>
                            </#if>
                            <#if exp.indUsuarioAsignado == 1>
                                <a title="El expediente se encuentra con cambios sin aprobar. Ver detalle." href="/expediente/administrarExpediente?expId=${exp.expId}">
                                    <img class="card-img-top" src="/img/alert-circle.svg" alt="">
                                </a>
                            </#if>
                        </td>
                 </tr>
            </#list>
        </tbody>
    </table>
        
        <#if totalPages gt 0>
            <@printBar url="/expediente/listarExpedientes" params=null metodo="get"/>
        </#if>
  </#if>
</div>
<#include "bandeja-footer.ftl">
