<#setting number_format="computer">
<#assign pageTitle = "TRANSFERENCIA DE ARCHIVOS Y EXPEDIENTES" />
<#assign deferredJS = "" />
<#include "header.ftl" />
<#include "gen-paginacion.ftl">
<style>
    .selection-tr{
        cursor:pointer;
    }
    
    .selection-tr:hover{
        background-color: #e8e8e8;
    }
    
</style>
<div class="container-fluid">
    <div>
        <ol class="breadcrumb">
            <li class="active">Inicio</li>
        </ol>
    </div>
    <div>
        <span>
            <a href="/transferencia-archivo/crearTransferenciaGestion" onclick="loading(event);" class="btn btn-success btn-sm bd-popover float-right" role="button" data-toggle="popover" data-trigger="hover" data-placement="right" title="crear" data-content="Pulse para crear una nueva transferencia" style="float:left;">Nueva Transferencia de Archivos y Expedientes en Gestión</a>
            <a href="/transferencia-archivo/listar?tipoTransferencia=PROCESO" onclick="loading(event);" class="<#if tipoTransferencia == 'PROCESO'>btn btn-primary btn-sm<#else>btn btn-secondary btn-sm</#if>" id="btn-proceso" style="width: 150px; margin-left:10px;">En proceso</a>
            <a href="/transferencia-archivo/listar?tipoTransferencia=ORIGEN" onclick="loading(event);" class="<#if tipoTransferencia == 'ORIGEN'>btn btn-primary btn-sm<#else>btn btn-secondary btn-sm</#if>" id="btn-realizado" style="width: 150px;">Realizadas</a>
            <a href="/transferencia-archivo/listar?tipoTransferencia=DESTINO" onclick="loading(event);" class="<#if tipoTransferencia == 'DESTINO'>btn btn-primary btn-sm<#else>btn btn-secondary btn-sm</#if>" id="btn-recibido" style="width: 150px;">Recibidas</a>
        </span>
    </div>
    </br>
    
    <#if transferencias?size = 0 >
        </br>
        </br>
        <div class="jumbotron">
            <h1 class="display-1">No hay registros</h1>
        </div>
    <#else>
        </br>              
        <table class="table">
            <thead>
                <tr>
                    <th>Fecha de creación</th>
                    <#if tipoTransferencia == "DESTINO" || tipoTransferencia == "PROCESO">
                        <th>Usuario origen</th>
                    </#if>
                    <#if tipoTransferencia == "ORIGEN" || tipoTransferencia == "PROCESO">
                        <th>Usuario receptor</th>
                    </#if>
                    <th>Justificación</th>
                    <th>Numero de documentos</th>
                    <th>Numero de expedientes</th>
                    <#if tipoTransferencia == "PROCESO">
                        <th>Usuario Asignado</th>
                        <th>Ultimo estado</th>
                    </#if>
                </tr>
            </thead>
            <tbody>
                <#list transferencias as ta>
                    <tr class="selection-tr" onclick="location.href = '/transferencia-archivo/resumen/${ta.tarId}';">
                        <td>${ta.fechaCreacion?string('yyyy-MM-dd')}</td>
                        <#if tipoTransferencia == "DESTINO" || tipoTransferencia == "PROCESO">
                            <td>${ta.usuNomOrigen!""}</td>
                        </#if>
                        <#if tipoTransferencia == "ORIGEN" || tipoTransferencia == "PROCESO">
                            <td>${ta.usuNomDestino!""}</td>
                        </#if>
                        <td>${ta.justificacion!""}</td>
                        <td>${ta.numDocumentos!"0"}</td>
                        <td>${ta.numExpedientes!"0"}</td>
                        <#if tipoTransferencia == "PROCESO">
                            <td>${ta.usuAsignado!"0"}</td>
                            <td>${ta.ultEstado!"0"}</td>
                        </#if>
                    </tr>
                </#list>
            </tbody>
        </table>
        
        <#if totalPages gt 0>
            <@printBar url="/transferencia-archivo/listar" params={"tipoTransferencia": tipoTransferencia!"PROCESO"} metodo="get"/>
        </#if>
    </#if>
</div>
<#include "footer.ftl" />