<#setting number_format="computer">
<#assign pageTitle = "TRANSFERENCIA DE ARCHIVOS Y EXPEDIENTES" />
<#assign deferredJS = "" />
<#include "header.ftl" />
<#include "gen-paginacion.ftl">

<div class="container-fluid">
    <div>
        <ol class="breadcrumb">
            <li class="active">Inicio</li>
        </ol>
    </div>
    <div>
        <span>
            <a href="#" class="btn btn-success btn-sm bd-popover float-right" role="button" data-toggle="popover" data-trigger="hover" data-placement="right" title="crear" data-content="Pulse para crear una nueva transferencia" style="float:left;">Nueva Transferencia de Archivos y Expedientes en Gestión</a>
        </span>
    </div>
    </br>
    
    <#if traArchivosRecibidos?size = 0 >
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
                    <th>Número Radicado</th>
                    <th>Fecha de creación</th>
                    <th>Usuario receptor</th>
                    <th>Numero de documentos</th>
                </tr>
            </thead>
            <tbody>
                <#list traArchivosRecibidos as ta>
                    <tr>
                        <td>${ta.numeroRadicado!""}</td>
                        <td>${ta.fechaCreacion?string('yyyy-MM-dd')}</td>
                        <td>${ta.destinoUsuario!""}</td>
                        <td>${ta.numeroDocumentos!"0"}</td>
                    </tr>
                </#list>
            </tbody>
        </table>
        
        <#if totalPages gt 0>
            <@printBar url="/transferencia-archivo/listar" params=null metodo="get"/>
        </#if>
    </#if>
</div>
<#include "footer.ftl" />