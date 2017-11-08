<#setting number_format="computer">
<#if !pageTitle??>
    <#assign pageTitle = 'Resultados' />
</#if>
<#include "util-macros.ftl" />
<#include "bandeja-header.ftl">
<#include "consulta-parametros-util.ftl"/>
<#include "gen-paginacion.ftl">

<br>
<span><a href="/consulta/parametros"><strong> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;&lt; &nbsp;REGRESAR</strong></a></span>
<br><br>
<div style=" border-radius: 10px; margin-bottom: 10px; padding: 10px;">
    <fieldset>
        <legend>Parámetros de filtro</legend>
        <div class="table-responsive" style="width:75%">
            <table class="table table-bordered">
                <thead>
                    <tr>
                        <#if term?has_content>
                        <td style="font-weight:bold; text-align: center; vertical-align: middle;">Busqueda General</td>
                        <#else>
                            <#if fechaInicio?has_content>
                        <td style="font-weight:bold; text-align: center; vertical-align: middle;">Fecha Inicial</td>
                            </#if>
                            <#if fechaFin?has_content>
                        <td style="font-weight:bold; text-align: center; vertical-align: middle;">Fecha Final</td>
                            </#if> 
                            <#if asignado?has_content>
                        <td style="font-weight:bold; text-align: center; vertical-align: middle;">Asignado a</td>
                            </#if> 
                            <#if asunto?has_content>
                        <td style="font-weight:bold; text-align: center; vertical-align: middle;">Asunto</td>
                            </#if> 
                            <#if radicado?has_content>
                        <td style="font-weight:bold; text-align: center; vertical-align: middle;">Radicado</td>
                            </#if>
                            <#if destinatario?has_content>
                        <td style="font-weight:bold; text-align: center; vertical-align: middle;">Destinatario</td>
                            </#if>
                            <#if clasificacion?has_content>
                        <td style="font-weight:bold; text-align: center; vertical-align: middle;">Clasificación</td>
                            </#if>
                            <#if dependenciaOrigen?has_content>
                        <td style="font-weight:bold; text-align: center; vertical-align: middle;">Dependencia Origen</td>
                            </#if>
                            <#if dependenciaDestino?has_content>
                        <td style="font-weight:bold; text-align: center; vertical-align: middle;">Dependencia destino</td>
                            </#if>
                        </#if> 
                        </tr>
                    </thead>
                <tbody>
                    <tr>
                        <#if term?has_content>
                            <td style="text-align: center; vertical-align: middle;">${term}</td>
                        <#else>
                            <#if fechaInicio?has_content >
                                <td style="text-align: center; vertical-align: middle;">${fechaInicio}</td>
                            </#if> 
                            <#if fechaFin?has_content >
                                <td style="text-align: center; vertical-align: middle;">${fechaFin}</td>
                            </#if> 
                            <#if asignado?has_content >
                                <td style="text-align: center; vertical-align: middle;">${asignado}</td>
                            </#if>
                            <#if asunto?has_content >
                                <td style="text-align: center; vertical-align: middle;">${asunto}</td>
                            </#if>
                            <#if radicado?has_content >
                                <td style="text-align: center; vertical-align: middle;">${radicado}</td>
                            </#if>
                            <#if destinatario?has_content >
                                <td style="text-align: center; vertical-align: middle;">${destinatario}</td>
                            </#if>
                            <#if clasificacion?has_content >
                                <td style="text-align: center; vertical-align: middle;">${clasificacionNombre}</td>
                            </#if>
                            <#if dependenciaDestino?has_content >
                                <td style="text-align: center; vertical-align: middle;">${dependenciaDestinoDescripcion}</td>
                            </#if>
                            <#if dependenciaOrigen?has_content >
                                <td style="text-align: center; vertical-align: middle;">${dependenciaOrigenDescripcion}</td>
                            </#if>
                        </#if>
                    </tr>
                </tbody>
            </table>
        </div>
    </fieldset>
</div>

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
                    <td style="font-weight:bold; text-align: center; vertical-align: middle; ">PROCESO</td>
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
                            ${x.cuandoMod?string('yyyy-MM-dd')}
                            <br>
                            ${x.cuandoMod?string('hh:mm a')} 
                        </td>
                        <td style="text-align: center; vertical-align: middle;word-wrap:break-word;">
                            <strong><a href="/proceso/instancia?pin=${x.idInstancia}">${(x.asunto)!"&lt;Sin asunto&gt;"}</a></strong>
                        </td>
                        <td style="text-align: center; vertical-align: middle;">
                            ${x.nombreProceso}
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
            <@printBar url="/consulta/parametros" params={"asignado":asignado,"asunto": asunto,"fechaInicio":fechaInicio,"fechaFin":fechaFin,"radicado":radicado,"destinatario":destinatario,"clasificacion":clasificacion, "dependenciaDestino":dependenciaDestino,"dependenciaOrigen":dependenciaOrigen,"term":term,"clasificacionNombre":clasificacionNombre,"dependenciaOrigenDescripcion":dependenciaOrigenDescripcion,"dependenciaDestinoDescripcion":dependenciaDestinoDescripcion}/>
        </#if>
    </div>
</#if>
<#include "bandeja-footer.ftl">