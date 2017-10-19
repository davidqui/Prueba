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
                            <#--
                                2017-05-15 jgarcia@controltechcg.com Issue #78 (SICDI-Controltech) feature-78:
                                Presentar información básica de los usuarios asignadores y asignados en las
                                bandejas del sistema.
                            -->
                            <#if (x.instancia.asignado)??>
                                ${(usuarioService.mostrarInformacionBasica(x.instancia.asignado))!"&lt;No asignado&gt;"}
                            </#if>
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
            <@printBar "/bandeja/entrada"/>
        </#if>
    </#if>
</#if>
<#include "bandeja-footer.ftl">