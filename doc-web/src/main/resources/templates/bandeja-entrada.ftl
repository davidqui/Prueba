<#setting number_format="computer">
<#if !pageTitle??>
  <#assign pageTitle = "Bandeja de entrada" />
</#if>
<#include "bandeja-header.ftl">
<#include "gen-paginacion.ftl">
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
        <table class="table table-striped table-bordered" style="table-layout: fixed;">
            <thead>
                <tr>
                    <td style="font-weight:bold; text-align: center; vertical-align: middle; width : 10%">FECHA CREACIÓN</td>
                    <td style="font-weight:bold; text-align: center; vertical-align: middle;">ASUNTO</td>
                    <td style="font-weight:bold; text-align: center; vertical-align: middle;width : 10%">RADICADO</td>
                    <td style="font-weight:bold; text-align: center; vertical-align: middle; width : 10%">UNIDAD ORIGEN</td>
                    <td style="font-weight:bold; text-align: center; vertical-align: middle;">ASIGNADO POR</td>
                    <td style="font-weight:bold; text-align: center; vertical-align: middle; width : 5%">PLAZO</td>
                </tr>
            </thead>
            <tbody>
                <#list documentos as x>
                    <tr>
                        <td style="text-align: center; vertical-align: middle; width : 10%">
                            ${x.cuando?string('yyyy-MM-dd hh:mm a')}
                        </td>
                        <td style="text-align: center; vertical-align: middle;word-wrap:break-word;">
                            <strong><a href="/proceso/instancia?pin=${x.instancia.id}">${(x.asunto)!"&lt;Sin asunto&gt;"}</a></strong>
                        </td>
                        
                        <td style="text-align: center; vertical-align: middle; width : 10%">
                            <#if (x.radicado)??>
                                ${x.radicado}
                            </#if>
                        </td>
                        <td style="text-align: center; vertical-align: middle; width : 10%">
                            <#--
                                2017-10-24 edison.gonzalez@controltechcg.com Issue #132 (SICDI-Controltech) feature-78: Presentar información
                                de la unidad del usuario destino.
                            -->
                            <#if (x.usuarioUltimaAccion)?? >
                                ${usuarioService.mostrarInformacionUnidad(x.usuarioUltimaAccion)}
                            <#else> 
                                ${usuarioService.mostrarInformacionUnidad(x.instancia.asignado)} 
                            </#if>
                        </td>
                        <td style="text-align: center; vertical-align: middle;">
                            <#if (x.usuarioUltimaAccion)?? >
                                ${usuarioService.mostrarInformacionBasicaBandejas(x.usuarioUltimaAccion)}
                            <#else> 
                                ${usuarioService.mostrarInformacionBasicaBandejas(x.instancia.asignado)} 
                            </#if>
                        </td>
                        <td style="text-align: center; vertical-align: middle; width : 5%">
                            <#if (x.plazo)?? >
                                <span class="label label-${x.semaforo}">
                                    ${x.plazo?string('yyyy-MM-dd')}
                                </span>
                            <#else>
                                <span class="label label-success">
                                    Sin plazo
                                </span>
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
            <@printBar url="/bandeja/entrada" />
        </#if>
    </#if>
</#if>
<#include "bandeja-footer.ftl">