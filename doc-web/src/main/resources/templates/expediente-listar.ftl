<#setting number_format="computer">
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
    </div>
    <div class="container-fluid" style="margin-top: 30px;">
        <form action="/expediente/listarExpedientes?all=true" method="GET" class="form-inline">
            <div class="form-group row">
                <input type="text" class="form-control" id="filtro" name="filtro" value="${filtro!""}" style="width:400px" placeholder="FILTRO POR EL NOMBRE DE EXPEDIENTE"/>
                <button type="submit" class="btn btn-default">Buscar</button>
                </div>
            <input type="hidden" name="all" value="true">
            </form>
        </div>
        
        <#if all?? && all>
    <a class="btn btn-link" href="/expediente/listarExpedientes?all=false&filtro=${filtro!""}">No mostrar expedientes cerrados</a>
        <#else>
    <a class="btn btn-link" href="/expediente/listarExpedientes?all=true&filtro=${filtro!""}">Mostrar expedientes cerrados</a>
        </#if>

    <#if expedientes?size = 0 >
    </br>
    </br>
    <div class="jumbotron">
        <h1 class="display-1">No hay registros</h1>
        </div>
    <#else>
    </br>
    </br>
    </br>
    <table class="table">
        <thead>
            <tr>
                <th></th>
                <th>TRD principal</th>
                <th>Nombre</th>
                <th>Fecha</th>
                <th>Dependencia</th>
                <th>Acción</th>
                </tr>
            </thead>
        <tbody>
                <#list expedientes as exp>
                    <#assign vclasses = "" />
                    <#if !exp.indCerrado?? || exp.indCerrado == true >
                        <#assign vclasses = "text-danger" />
                    </#if>
                    <tr <#if exp.indJefeDependencia && exp.indUsuarioAsignado == 1>style="background-color: #f1d1d1;"</#if>>
                        <td>
                            <img class="svg" src="/img/folder.svg" alt=""/>
                            </td>
                        <td class="${vclasses}">${exp.trdNomIdPrincipal!""}</td>
                        <td class="${vclasses}"><a href="/expediente/listarDocumentos?expId=${exp.expId}">${exp.expNombre!""}</a></td>
                        <td class="${vclasses}">${exp.fecCreacion?string('yyyy-MM-dd')}</td>
                        <td class="${vclasses}">${exp.depNombre!""}</td>
                        <td>
                            <a title="Ver detalle" href="/expediente/administrarExpediente?expId=${exp.expId}">
                                <img class="card-img-top" src="/img/eye.svg" alt="">
                                </a>
                                    <#if exp.indUsuarioAsignado == 1 && exp.indJefeDependencia>
                            <a class="btn btn-success btn-sm" onclick="mostrarCambiosPendientes(${exp.expId})" data-toggle="modal" href="#enviarJefeModal" style="float: right;">
                                Aprobar
                                </a>
                                    </#if>
                            </td>
                    </tr>
                </#list>
            </tbody>
        </table>

        <#if totalPages gt 0>
            <@printBar url="/expediente/listarExpedientes" params={"filtro": filtro!"", "all": all?string("true", "false") } metodo="get"/>
        </#if>

<!-- Modal aprobar cambios por jefe de dependencia -->
    <div class="modal fade" id="enviarJefeModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">Aprobación</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close" style="position: absolute; right: 7px; top: 5px;">
                        <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                <div class="modal-body">
                    Aqui se listaran los cambios pendientes para su aprobación. Recuerde diligenciar la observación:
                    <div class="card-block cus-gray-bg">
                        <form method="post" id="obsForm">
                            <fieldset class="form-group">
                                <textarea class="form-control" id="observacion2" name="observacion2"></textarea>
                                </fieldset>
                            </form>
                        </div>
                    <div class="modal-contenido" style="height: 400px; overflow-y: scroll;">
                        </div>
                    </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" onclick="aprobarOrechazarCambios(0)">Rechazar</button>
                    <button type="button" class="btn btn-primary" onclick="aprobarOrechazarCambios(1)" >Aprobar</button>
                    </div>
                </div>
            </div>
        </div>
  </#if>
    </div>

<script src="/js/app/expediente-administrar.js"></script>
<#include "bandeja-footer.ftl">
