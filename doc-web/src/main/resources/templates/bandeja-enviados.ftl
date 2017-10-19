<#setting number_format="computer">
<#if !pageTitle??>
  <#assign pageTitle = "Bandeja de enviados" />
</#if>
<#include "bandeja-header.ftl">
<#include "gen-paginacion.ftl">

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
	  
    <form action="/bandeja/enviados" method="POST" class="form-inline">
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
        </br>
        <table class="table table-striped table-bordered">
            <thead>
                <tr>
                    <td style="font-weight:bold; text-align: center;">ASUNTO</td>
                    <td style="font-weight:bold; text-align: center;">RADICADO</td>
                    <td style="font-weight:bold; text-align: center;">FECHA ENVIO</td>
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
                            <#if (x.textoAsignado)??>
                                <#-- 2017-02-06 jgarcia@controltechcg.com Issue #118 Presentación de jefes de dependencias adicionales a un documento. -->
                                <#-- <#if (x.instancia.asignado)??><strong>Env: </strong>${(x.instancia.asignado)!"&lt;No asignado&gt;"}</#if> -->
                                ${(x.textoAsignado)!"&lt;No asignado&gt;"}
                            </#if>
                        </td>
                        <td style="text-align: center;">
                            <#--
                                2017-05-15 jgarcia@controltechcg.com Issue #78 (SICDI-Controltech) feature-78:
                                Presentar información básica de los usuarios asignadores y asignados en las
                                bandejas del sistema.
                            -->
                            <#if (x.usuarioUltimaAccion)?? >
                                ${usuarioService.mostrarInformacionBasica(x.usuarioUltimaAccion)}
                            </#if>
                        </td>
                    </tr>
                </#list>
            </tbody>
        </table>

        <#if totalPages gt 0>
            <@printBar "/bandeja/enviados" "&fechaInicial=${fechaInicialValor}&fechaFinal=${fechaFinalValor}"/>
            
        </#if>
    </#if>
</#if>
<#include "bandeja-footer.ftl">