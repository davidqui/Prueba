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
            <a href="/transferencia-archivo/listar?tipoTransferencia=ORIGEN" onclick="loading(event);" class="<#if tipoTransferencia == 'ORIGEN'>btn btn-primary btn-sm<#else>btn btn-secondary btn-sm</#if>" id="btn-realizado" style="width: 150px; margin-left:10px;">Realizadas</a>
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
                    <th>Número Radicado</th>
                    <th>Fecha de creación</th>
                    <#if tipoTransferencia == "ORIGEN">
                        <th>Usuario receptor</th>
                    </#if>
                    <#if tipoTransferencia == "DESTINO">
                        <th>Usuario origen</th>
                    </#if>
                    <th>Numero de documentos</th>
                </tr>
            </thead>
            <tbody>
                <#list transferencias as ta>
                    <tr>
                        <td>${ta.numeroRadicado!""}</td>
                        <td>${ta.fechaCreacion?string('yyyy-MM-dd')}</td>
                        <#if tipoTransferencia == "ORIGEN">
                            <td>${ta.destinoUsuario!""}</td>
                        </#if>
                        <#if tipoTransferencia == "DESTINO">
                            <td>${ta.origenUsuario!""}</td>
                        </#if>
                        <td>${ta.numeroDocumentos!"0"}</td>
                    </tr>
                </#list>
            </tbody>
        </table>
        
        <#if totalPages gt 0>
            <@printBar url="/transferencia-archivo/listar" params={"tipoTransferencia": tipoTransferencia!"ORIGEN"} metodo="get"/>
        </#if>
    </#if>
</div>
<#include "footer.ftl" />