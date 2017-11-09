<#setting number_format="computer">
<#if !pageTitle??>
    <#assign pageTitle = "Bandeja en trámite" />
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
	2017-07-10 jgarcia@controltechcg.com Issue #115 (SICDI-Controltech) feature-115:
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
	  
    <form action="/bandeja/entramite" method="POST" class="form-inline">
        <label for="fechaInicial">Fecha Inicial</label>
        <div class="form-inline input-group">
            <input class="form-control datepicker" id="fechaInicial" name="fechaInicial" value="${fechaInicialValor}" />
        </div>
        <label>Fecha Final</label>
        <div class="form-inline input-group">
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
            2017-10-20 edison.gonzalez@controltechcg.com Issue #132 (SICDI-Controltech feature-132:
            Ajuste visual de informacion en tabla.
        -->
        </br>
        <table class="table table-striped table-responsive" style="table-layout: fixed;">
            <thead>
                <tr>
                    <td style="font-weight:bold; text-align: center; vertical-align: middle; width : 10%">FECHA CREACIÓN</td>
                    <td style="font-weight:bold; text-align: center; vertical-align: middle">ASUNTO</td>
                    <td style="font-weight:bold; text-align: center; vertical-align: middle; width : 10%">RADICADO</td>
                    <td style="font-weight:bold; text-align: center; vertical-align: middle; width : 5%">UNIDAD DESTINO</td>
                    <td style="font-weight:bold; text-align: center; vertical-align: middle">ASIGNADO A</td>
                    <td style="font-weight:bold; text-align: center; vertical-align: middle; width : 5%">UNIDAD ORIGEN</td>
                    <td style="font-weight:bold; text-align: center; vertical-align: middle">ASIGNADO POR</td>
                    <td style="font-weight:bold; text-align: center; vertical-align: middle; width : 10%">PLAZO</td>
                </tr>
            </thead>
            <tbody>	
                <#list documentos as x>
                    <tr>
                        <td style="text-align: center; vertical-align: middle; width : 10%">
                            ${x.cuando?string('yyyy-MM-dd hh:mm a')}
                        </td>
                        <td style="text-align: center; vertical-align: middle; middle;word-wrap:break-word;">
                            <strong><a href="/proceso/instancia?pin=${x.instancia.id}">${(x.asunto)!"&lt;Sin asunto&gt;"}</a></strong>
                        </td>
                        <td style="text-align: center; vertical-align: middle; width : 10%">
                            <#if (x.radicado)??>
                                ${x.radicado}
                            </#if>
                        </td>
                        <td style="text-align: center; vertical-align: middle; width : 5%">
                            <#--
                                2017-10-25 edison.gonzalez@controltechcg.com Issue #132 
                                (SICDI-Controltech) feature-78: Presentar información
                                de la unidad del usuario asignado.
                            -->
                            <#if (x.instancia.asignado)?? >
                                ${usuarioService.mostrarInformacionUnidad(x.instancia.asignado)}
                            </#if>
                        </td>
		      	<td style="text-align: center; vertical-align: middle;">
                            <#--
                                2017-05-15 jgarcia@controltechcg.com Issue #78 (SICDI-Controltech) feature-78:
                                Presentar información básica de los usuarios asignadores y asignados en las
                                bandejas del sistema.
                            -->
                            <#if (x.instancia.asignado)??>
                                ${(usuarioService.mostrarInformacionBasicaBandejas(x.instancia.asignado))!"&lt;No asignado&gt;"}
                            </#if>
                        </td>
                        <td style="text-align: center; vertical-align: middle; width : 5%">
                            <#--
                                2017-10-24 edison.gonzalez@controltechcg.com Issue #132 
                                (SICDI-Controltech) feature-78: Presentar información
                                de la unidad del usuario quien creo el documento.
                            -->
                            <#if (x.elabora)?? >
                                ${usuarioService.mostrarInformacionUnidad(x.elabora)}
                            </#if>
                        </td>
                        <td style="text-align: center; vertical-align: middle;">
                            <#if (x.usuarioUltimaAccion)?? > 
                                ${usuarioService.mostrarInformacionBasicaBandejas(x.usuarioUltimaAccion)} 
                            </#if>
                        </td>
                        <td style="text-align: center; vertical-align: middle; width : 10%">
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
            2017-10-20 edison.gonzalez@controltechcg.com Issue #132 (SICDI-Controltech feature-132:
            Se agrega items de visualizacion para la paginacion.
        -->
        <#if totalPages gt 0>
            <@printBar url="/bandeja/entramite" params={"fechaInicial": fechaInicialValor, "fechaFinal": fechaFinalValor} metodo="get"/>
        </#if>
    </#if>
</#if>
<script type="text/javascript">
    $(document).ready(function() {
        $('#fechaInicial').prop('readonly', true);
        $('#fechaFinal').prop('readonly', true);

        $('#fechaInicial').each(function() {
            var buton = "<div onclick='document.getElementById(\""+ $(this).attr("id")+ "\").value = \"\"' class=\"input-group-addon btn btn-warning\" >Limpiar</div>";
            $(this).parent().append(buton);
        });
        $('#fechaFinal').each(function() {
            var buton = "<div onclick='document.getElementById(\""+ $(this).attr("id")+ "\").value = \"\"' class=\"input-group-addon btn btn-warning\" >Limpiar</div>";
            $(this).parent().append(buton);
        });
    });
</script>
<#include "bandeja-footer.ftl">