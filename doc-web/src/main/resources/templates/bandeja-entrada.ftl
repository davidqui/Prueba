<#if !pageTitle??>
  <#assign pageTitle = "Bandeja de entrada" />
</#if>
<#include "bandeja-header.ftl">
<#--
    2017-04-07 jgarcia@controltechcg.com Issue #37 (SIGDI-Controltech): Importación de template de funciones de documento. 
 -->
<#include "lib/documento_functions.ftl" />

<#if error??> 
    <div class="jumbotron">
        <h2 class="display-1">Algo anda mal...</h2>
        <p class="lead">No se puede construir la bandeja debido a un problema interno del sistema. Intente nuevamente por favor.</p>
    </div>
<#else>
    <#if !documentos?? || documentos?size == 0 >
        <div class="jumbotron">
        <h2 class="display-1">No hay más documentos</h2>
        <p class="lead">En este momento no existen documentos en esta bandeja</p>
        </div>
    <#else>
        <#--
            2017-10-17 edison.gonzalez@controltechcg.com Issue #132 (SICDI-Controltech feature-132:
            Ajuste visual de informacion en tabla.
        -->
        <table class="table table-striped table-bordered">
            <thead>
                <tr>
                    <td style="font-weight:bold; text-align: center;">ASUNTO</td>
                    <td style="font-weight:bold; text-align: center;">RADICADO</td>
                    <td style="font-weight:bold; text-align: center;">FECHA CREACIÓN</td>
                    <td style="font-weight:bold; text-align: center;">PLAZO</td>
                    <td style="font-weight:bold; text-align: center;">ASIGNADO A</td>
                    <td style="font-weight:bold; text-align: center;">ASIGNADO POR</td>
                </tr>
            </thead>
            <tbody>
                <#list documentos as x>
                    <tr>
                        <td style="text-align: center;">
                            <strong><a href="/proceso/instancia?pin=${x.instancia.id}">${(x.asunto)!"&lt;Sin asunto&gt;"}</a></strong>
                        </td>
                        <td style="text-align: center;">
                            <#if (x.radicado)??>
                                ${x.radicado}
                            </#if>
                        </td>    
                        <td style="text-align: center;">
                            ${x.cuando?string('yyyy-MM-dd hh:mm a')}
                        </td>    
                        <td style="text-align: center;">
                            <#if (x.plazo)?? >
                                <span class="label label-${x.semaforo}">${x.plazo?string('yyyy-MM-dd')}</span>
                            </#if>
                        </td>
                        <td style="text-align: center;">
                            ${(usuarioService.mostrarInformacionBasica(x.instancia.asignado))!"&lt;No asignado&gt;"}
                        </td>
                        <td style="text-align: center;">
                            <#if (x.usuarioUltimaAccion)?? >
                                ${usuarioService.mostrarInformacionBasica(x.usuarioUltimaAccion)}
                            <#else> 
                                ${usuarioService.mostrarInformacionBasica(x.instancia.asignado)} 
                            </#if>
                        </td>
                    </tr>
                </#list>
            </tbody>
        </table>
        <#--
            2017-10-17 edison.gonzalez@controltechcg.com Issue #132 (SICDI-Controltech feature-132:
            Se agrega items de visualizacion para la paginacion.
        -->
        <#if totalPages gt 0>
            <center>
                <div class="row">
                    <div class="col-sm-5">
                        <div class="dataTables_info">${labelInformacion}</div>
                    </div>
                    <div class="col-sm-7">
                        <ul class="dataTables_paginate">

                            <#if pageIndex gt 1>
                                <li class="page-item"><a href="/bandeja/entrada?pageIndex=1" class="page-link"><<</a></li>
                                <li class="page-item"><a href="/bandeja/entrada?pageIndex=${pageIndex - 1}" class="page-link"><</a></li>
                            <#else>
                                <li class="page-item disabled"><a class="page-link"><<</a></li>
                                <li class="page-item disabled"><a class="page-link"><</a></li>
                            </#if>

                            <li class="page-item"><a href="/bandeja/entrada?pageIndex=${pageIndex}" class="page-link">${pageIndex}</a></li>

                            <#if pageIndex lt (totalPages)>
                                <li class="page-item"><a href="/bandeja/entrada?pageIndex=${pageIndex + 1}" class="page-link">></a></li>
                                <li class="page-item"><a href="/bandeja/entrada?pageIndex=${totalPages}" class="page-link">>></a></li>
                            <#else>
                                <li class="page-item disabled"><a class="page-link">></a></li>
                                <li class="page-item disabled"><a class="page-link">>></a></li>
                            </#if>
                        </ul>
                    </div>
                </div>
            </center>
        </#if>
    </#if>
</#if>
<#include "bandeja-footer.ftl">