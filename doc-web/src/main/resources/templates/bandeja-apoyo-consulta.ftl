<#if !pageTitle??>
    <#assign pageTitle = "Bandeja de consulta" />
</#if>
<#include "bandeja-header.ftl">

<#if error??> 
  <div class="jumbotron">
    <h2 class="display-1">Algo anda mal...</h2>
    <p class="lead">No se puede construir la bandeja debido a un problema interno del sistema. Intente nuevamente por favor.</p>
  </div>
<#else>
    <#--
            2017-07-05 jgarcia@controltechcg.com Issue #115 (SICDI-Controltech) feature-115:
            Adición de formulario con fitro de fecha en bandejas diferentes a la de entrada.
     -->
    <#if fechaInicial??>
            <#assign fechaInicialValor = fechaInicial?string["yyyy-MM-dd"] />
    <#else>
            <#assign fechaInicialValor = "" />
    </#if>

    <#if fechaFinal??>
            <#assign fechaFinalValor = fechaFinal?string["yyyy-MM-dd"] />
    <#else>
            <#assign fechaFinalValor = "" />
    </#if>
	  
    <form action="/bandeja/apoyo-consulta" method="POST" class="form-inline">
        <div class="form-group">
            <label for="fechaInicial">Fecha Inicial</label>
            <input class="form-control datepicker" id="fechaInicial" name="fechaInicial" value="${fechaInicialValor}" />
        </div>
        <div class="form-group">
            <label>Fecha Final</label>
            <input class="form-control datepicker" id="fechaFinal" name="fechaFinal" value="${fechaFinalValor}" />
        </div>
        <button type="submit" class="btn btn-success">Buscar</button>
    </form>
  	
    <#--
            2017-07-10 jgarcia@controltechcg.com Issue #115 (SICDI-Controltech) feature-115:
            Modificación presentación de mensaje de resultados, para hacer referencia al rango de fechas.
    -->
    <#if !documentos?? || documentos?size == 0 >
        <div class="jumbotron">
            <h2 class="display-1">No hay documentos para el rango.</h2>
            <p class="lead">No existen documentos en esta bandeja para el rango de fechas seleccionado entre ${fechaInicialValor} y ${fechaFinalValor}.</p>
        </div>
    <#else>
	<#--
            2017-10-23 edison.gonzalez@controltechcg.com Issue #132 (SICDI-Controltech feature-132:
            Ajuste visual de informacion en tabla.
        -->
        </br>
        <table class="table table-striped table-bordered" style="table-layout: fixed;">
            <thead>
                <tr>
                    <td style="font-weight:bold; text-align: center; vertical-align: middle; width : 10%">FECHA CREACIÓN</td>
                    <td style="font-weight:bold; text-align: center; vertical-align: middle;">ASUNTO</td>
                    <td style="font-weight:bold; text-align: center; vertical-align: middle; width : 10%">RADICADO</td>
                    <td style="font-weight:bold; text-align: center; vertical-align: middle; width : 5%">UNIDAD DESTINO</td>
                    <td style="font-weight:bold; text-align: center; vertical-align: middle;">ASIGNADO A</td>
                    <td style="font-weight:bold; text-align: center; vertical-align: middle; width : 5%">UNIDAD ORIGEN</td>
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
                        <td style="text-align: center">
                            <strong><a href="/proceso/instancia?pin=${x.instancia.id}">${(x.asunto)!"&lt;Sin asunto&gt;"}</a></strong>
                        </td>
                        <td style="text-align: center; vertical-align: middle; width : 5%">
                            <#if (x.radicado)??>
                                ${x.radicado}
                            </#if>
                        </td>
                        <td style="text-align: center; vertical-align: middle; width : 5%">
                            <#--
                                2017-10-24 edison.gonzalez@controltechcg.com Issue #132 (SICDI-Controltech) feature-78: Presentar información
                                de la unidad del usuario destino.
                            -->
                            ${usuarioService.mostrarInformacionUnidad(x.instancia.asignado)}
                        </td>
                        <td style="text-align: center">
                            <#if (x.instancia.asignado)??>
                                <#--
                                    2017-05-15 jgarcia@controltechcg.com Issue #78 (SICDI-Controltech) feature-78:
                                    Presentar información básica de los usuarios asignadores y asignados en las
                                    bandejas del sistema.
                                -->
                                ${(usuarioService.mostrarInformacionBasicaBandejas(x.instancia.asignado))!"&lt;No asignado&gt;"}
                            </#if>
                        </td>
                        <td style="text-align: center; vertical-align: middle; width : 5%">
                            <#--
                                2017-10-24 edison.gonzalez@controltechcg.com Issue #132 
                                (SICDI-Controltech) feature-78: Presentar información
                                de la unidad del usuario asignado.
                            -->
                            <#if (x.usuarioUltimaAccion)?? >
                                ${usuarioService.mostrarInformacionUnidad(x.usuarioUltimaAccion)}
                            <#else> 
                                ${usuarioService.mostrarInformacionUnidad(x.instancia.asignado)}
                            </#if>
                        </td>
                        <td style="text-align: center">
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
    </#if>
</#if>
<#include "bandeja-footer.ftl">