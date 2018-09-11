<#setting number_format="computer">
<#if !pageTitle??>
    <#assign pageTitle = 'Resultados' />
</#if>
<#include "util-macros.ftl" />
<#include "gen-paginacion.ftl">

<#if term?has_content>
<div style=" border-radius: 10px; margin-bottom: 10px; padding: 10px;">
    <fieldset>
        <legend>Parámetros de filtro</legend>
        <#--
            2018-05-08 jgarcia@controltechcg.com Issue #160 (SICDI-Controltech)
            feature-160: Mejora en presentación de tabla de parámetros de filtro
            de búsqueda.
        -->
        <div class="table-responsive">
            <table class="table table-bordered">
                <thead>
                    <tr>
                        <th style="font-weight:bold; text-align: center; vertical-align: middle;">Busqueda General</th>
                        </tr>
                    </thead>
                <tbody>
                    <tr>
                        <td style="text-align: center; vertical-align: middle;">${term}</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </fieldset>
    </div>
</#if>

<#if !documentos?? || documentos?size == 0 >
<div class="jumbotron">
    <p class="lead">No se encontraron documentos</p>
    </div>
<#else>
<div >
    <table class="table">
        <thead>
            <tr>
                <td style="font-weight:bold; text-align: center; vertical-align: middle; ">FECHA ÚLTIMA MODIFICACIÓN</td>
                <td style="font-weight:bold; text-align: center; vertical-align: middle; ">ASUNTO</td>
                <td style="font-weight:bold; text-align: center; vertical-align: middle; ">UNIDAD ORIGEN</td>
                <td style="font-weight:bold; text-align: center; vertical-align: middle; ">UNIDAD DESTINO</td>
                <td style="font-weight:bold; text-align: center; vertical-align: middle; ">ESTADO</td>
                <td style="font-weight:bold; text-align: center; vertical-align: middle; ">USUARIO ASIGNADO</td>
                <td style="font-weight:bold; text-align: center; vertical-align: middle; ">ENVIADO POR</td>
                <td style="font-weight:bold; text-align: center; vertical-align: middle; ">ELABORA</td>
                <td style="font-weight:bold; text-align: center; vertical-align: middle; ">REVISÓ</td>
                <td style="font-weight:bold; text-align: center; vertical-align: middle; ">VISTO BUENO</td>
                <td style="font-weight:bold; text-align: center; vertical-align: middle; ">FIRMA</td>
                <td style="font-weight:bold; text-align: center; vertical-align: middle; ">CLASIFICACIÓN</td>
                <td style="font-weight:bold; text-align: center; vertical-align: middle; ">RADICADO</td>
                </tr>
            </thead>
        <tbody>
                <#list documentos as x>
            <tr>
                <td style="text-align: center; vertical-align: middle;">
                            ${(x.cuandoMod?string('yyyy-MM-dd'))!""}
                    <br>
                            ${(x.cuandoMod?string('hh:mm a'))!""} 
                    </td>
                <td style="text-align: center; vertical-align: middle;word-wrap:break-word;">
                    <#if x.perteneceDocumento>
                        <strong><a href="/proceso/instancia?pin=${x.idInstancia}">${(x.asunto)!"&lt;Sin asunto&gt;"}</a></strong>
                    <#else> 
                        <span>${(x.asunto)!"&lt;Sin asunto&gt;"}</option>
                    </#if>
                    </td>
                <td style="text-align: center; vertical-align: middle;">
                            ${x.unidadOrigen}
                    </td>
                <td style="text-align: center; vertical-align: middle;">
                            ${x.unidadDestino!""}
                    </td>
                <td style="text-align: center; vertical-align: middle;">
                            ${x.nombreEstado}
                    </td>
                <td style="text-align: center; vertical-align: middle;">
                            ${x.nombreUsuarioAsignado}
                    </td>
                <td style="text-align: center; vertical-align: middle;">
                            ${x.nombreUsuarioEnviado!"&lt;Nadie&gt;"}
                    </td>
                <td style="text-align: center; vertical-align: middle;">
                            ${x.nombreUsuarioElabora!"&lt;Nadie&gt;"}
                    </td>
                <td style="text-align: center; vertical-align: middle;">
                            ${x.nombreUsuarioReviso!"&lt;Nadie&gt;"}
                    </td>
                <td style="text-align: center; vertical-align: middle;">
                            ${x.nombreUsuarioVbueno!"&lt;Nadie&gt;"}
                    </td>
                <td style="text-align: center; vertical-align: middle;">
                            ${x.nombreUsuarioFirma!"&lt;Nadie&gt;"}
                    </td>
                <td style="text-align: center; vertical-align: middle;">
                            ${x.nombreClasificacion!"&lt;No aplica&gt;"}
                    </td>
                <td style="text-align: center; vertical-align: middle;">
                            ${x.numeroRadicado!"&lt;Sin radicado&gt;"}
                    </td>
                </tr>    
                </#list>
            </tbody>
        </table>
        <#--
            2017-11-01 edison.gonzalez@controltechcg.com Issue #132 (SICDI-Controltech feature-132:
            Se agrega items de visualizacion para la paginacion. 
        -->
        <#if totalPages gt 0>
            <@printBar url="/consulta/parametros" params={"asignado":asignado,
                "asunto": asunto,"fechaInicio":fechaInicio,"fechaFin":fechaFin,
                "radicado":radicado,"destinatario":destinatario,"clasificacion":clasificacion,
                "dependenciaDestino":dependenciaDestino,"dependenciaOrigen":dependenciaOrigen, 
                "term":term,"clasificacionNombre":clasificacionNombre,
                "dependenciaOrigenDescripcion":dependenciaOrigenDescripcion,
                "dependenciaDestinoDescripcion":dependenciaDestinoDescripcion, 
                "tipoProceso":tipoProceso, "buscarTodo":(buscarTodo?string("true","false"))!"",
                "destinoExterno":destinoExterno, "tipoBusqueda":tipoBusqueda!"" }metodo="post"/>
        </#if>
    </div>
</#if>