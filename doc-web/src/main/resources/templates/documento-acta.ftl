<#--
    2018-05-15 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
    feature-162.
-->
<#setting number_format="computer">

<#assign pageTitle = documento.asunto!"Acta" />
<#assign deferredJS = "" />

<#assign headScripts>
<script src="/js/eventos_documento.js"></script>
<script src="/js/app/funciones-documento.js"></script>
<script src="/js/tinymce.min.js"></script>
</#assign>

<#if archivoHeader??>	
    <#include "archivo-header.ftl">
<#else>
    <#include "bandeja-header.ftl" />
</#if>

<#include "lib/documento_functions.ftl" />
<#include "gen-arbol-dependencias.ftl">
<#include "gen-arbol-trd.ftl">

<#assign estadoModo = estadoModeMap[procesoInstancia.estado.id?string] />

<div class="col-md-8">    
    <#if estadoModo == "SOLO_CONSULTA" || estadoModo == "CARGA_ACTA_DIGITAL">
    <table class="table table-sm">    	
        <#-- TODO: Poblar tabla con información de solo consulta. -->
        </table>
    </#if>

    <#if estadoModo == "EDICION_INFORMACION" || estadoModo == "CARGA_ACTA_DIGITAL">   
    <form action="/documento-acta/guardar" method="POST" id="formdoc" enctype='multipart/form-data'>
        <input type="hidden" name="docId" id="docId" value="${(documento.id)!""}" />
        <input type="hidden" name="pinId" id="pinId" value="${(procesoInstancia.id)!""}" />

        <#-- Asunto -->
        <div class="form-group">
            <label for="asunto">Asunto (*)</label>
            <input type="text" class="form-control" id="asunto" name="asunto" value="<#if documentoActaDTO?? >${documentoActaDTO.asunto}<#elseif documento.asunto?? >${documento.asunto}</#if>"/>
            <small class="text-muted">Describe el objeto de la reunión de manera clara y sucinta.</small>
            <div class="error">
                <#if logicValidation?? && logicValidation.containsError("asunto") >
                    ${logicValidation.getError("asunto").message}
                </#if>
                </div>
            </div>

        <#-- Lugar -->
        <div class="form-group">
            <label for="actaLugar">Lugar (*)</label>
            <input type="text" class="form-control" id="actaLugar" name="actaLugar" value="<#if documentoActaDTO?? >${documentoActaDTO.actaLugar}<#elseif documento.actaLugar?? >${documento.actaLugar}</#if>"/>
            <small class="text-muted">Determina la ciudad donde se llevó a cabo la actividad que motivo la elaboración del acta.</small>
            <div class="error">
                <#if logicValidation?? && logicValidation.containsError("actaLugar") >
                    ${logicValidation.getError("actaLugar").message}
                </#if>                
                </div>
            </div>

        <#-- Fecha -->
        <div class="form-group">
            <label for="actaFechaElaboracion">Fecha (*)</label>
            <input type="text" class="form-control datepicker" id="actaFechaElaboracion" name="actaFechaElaboracion"  value="<#if documentoActaDTO?? >${documentoActaDTO.actaFechaElaboracion}<#elseif documento.actaFechaElaboracion?? >${yyyymmdd.format(documento.actaFechaElaboracion)}</#if>" />
            <small class="text-muted">Determina la fecha donde se llevó a cabo la actividad que motivo la elaboración del acta.</small>
            <div class="error">
                <#if logicValidation?? && logicValidation.containsError("actaFechaElaboracion") >
                    ${logicValidation.getError("actaFechaElaboracion").message}
                </#if>                
                </div>
            </div>

        <#-- Nivel de clasificación -->
        <div class="form-group">
            <label for="clasificacion">Nivel de clasificación (*)</label>           
            <select class="form-control" id="clasificacion" name="clasificacion">
                <option value=""></option>
                <#if clasificaciones??>
                    <#list clasificaciones as clasificacion>
                <option value="${clasificacion.id}" <#if documento.clasificacion?? && (clasificacion.id == documento.clasificacion.id) >selected="selected"</#if>>${clasificacion.nombre}</option>
                    </#list>
                </#if>
                </select>
            <small class="text-muted">Se asignará de acuerdo a lo establecido en el decreto 857 de 2014, según sea el caso: Ultrasecreto, Secreto, Confidencial o Restringido.</small>            
            <div class="error">
                <#if logicValidation?? && logicValidation.containsError("clasificacion") >
                    ${logicValidation.getError("clasificacion").message}
                </#if>                
                </div>
            </div>

        <#-- TRD de Acta -->
        <div class="form-group">
            <label for="trd">TRD de Acta (*)</label>           
            <select class="form-control" id="trd" name="trd">
                <option value=""></option>
                <#if subseriesTrdActas??>
                    <#list subseriesTrdActas as subseriesTrdActa>
                <option value="${subseriesTrdActa.id}" <#if documento.trd?? && (subseriesTrdActa.id == documento.trd.id) >selected="selected"</#if>>${subseriesTrdActa.codigo} - ${subseriesTrdActa.nombre}</option>
                    </#list>
                </#if>
                </select>
            <small class="text-muted">Código valor tabla de retención documental que corresponda al tipo de acta a registrar.</small>            
            <div class="error">
                <#if logicValidation?? && logicValidation.containsError("trd") >
                    ${logicValidation.getError("trd").message}
                </#if>                
                </div>
            </div>

        <#-- Número de folios -->
        <div class="form-group">
            <label for="numeroFolios">Número de folios (*)</label>
            <input type="number" class="form-control" id="numeroFolios" name="numeroFolios" value="<#if documentoActaDTO?? >${documentoActaDTO.numeroFolios}<#elseif documento.numeroFolios?? >${documento.numeroFolios}</#if>"/>
            <div class="error"></div>
            <small class="text-muted">Valor numérico equivalente al número de folios útiles que conforman el acta.</small>
            <div class="error">
                <#if logicValidation?? && logicValidation.containsError("numeroFolios") >
                    ${logicValidation.getError("numeroFolios").message}
                </#if>                
                </div>            
            </div>        

        <nav class="navbar navbar-default navbar-fixed-bottom text-xs-center hermes-bottombar">
            <button id="guardar-doc-btn" type="submit" class="btn btn-success btn-sm">Guardar</button>
            </nav>

        </form> <#-- Cierra formulario principal -->
    </#if>

    <!--
        Observaciones
    -->       	
    <div class="card m-y">                           		        
	<#if (documento.observaciones)??>
        <div class="card-block" id="obsDiv">
            <h5>Observaciones</h5>
            <#list documento.observaciones as observacion>
            <hr/>
            <strong>${utilController.nombre(observacion.quien)}</strong>, <em> ${observacion.cuando?string('yyyy-MM-dd hh:mm a:ss')}</em>
            <p>${observacion.texto}</p>
	    </#list>
            </div>
	</#if>

        <#if estadoModo == "EDICION_INFORMACION" || estadoModo == "CARGA_ACTA_DIGITAL">
        <div class="card-block cus-gray-bg">
            <form method="post" id="obsForm" >
                <fieldset class="form-group">
                    <textarea class="form-control" id="observacion" name="observacion"></textarea>
                    </fieldset>
                <a href="#" class="btn btn-secondary btn-sm" id="obsButton">Comentar</a>
                </form>
            </div>
        </#if>
        </div>
    </div>

<div class="col-md-4">
    <div class="card">
        <div class="card-header">
            <a href="/proceso/instancia/detalle?pin=${procesoInstancia.id}">Proceso</a>
            </div>
        <div class="card-block">
            <#-- TODO: Información de la instancia del proceso -->
            </div>
        </div>

    <#-- Adjuntos -->    
    <div class="card">
        <#if (documento.adjuntos?size > 0) >
        <div class="card-block">
            <h5 class="m-b">Adjuntos actuales</h5>
            <#list documento.adjuntos as adjunto >
                <#if adjunto.activo>
            <hr/>
            <strong>${adjunto.tipologia.nombre}</strong><br/>
            <em>Subido el ${yyyymmdd.format(adjunto.cuando)} por ${utilController.nombre(adjunto.quien)}</em>
            <a href="/documento/adjunto/${adjunto.id}/eliminar?pin=${procesoInstancia.id}" onclick="return confirm('¿Está seguro que desea eliminar el archivo ${adjunto.tipologia.nombre}?');">Eliminar</a><br/>
            <a href="#" onclick="visualizar('/ofs/viewer?file=/ofs/download/${adjunto.contenido}')">
                <img src="/ofs/download/tmb/${adjunto.contenido}" />
                <br/>
                        ${adjunto.original}
                </a>
            <br/>
	        </#if>
            </#list>
            </div>
        </#if>

        <#if estadoModo == "EDICION_INFORMACION" >
        <div class="card-block cus-gray-bg">
            <form action="/documento/adjunto?doc=${documento.id}" method="post" enctype="multipart/form-data">
                <fieldset class="form-group">
                    <label for="destinatario"><strong>Tipología y archivo</strong></label>
                    </br>
                    <span style="font-style: italic">Únicamente se permiten cargar archivos en formato PDF, JPG y PNG.</span>                    
                    <select class="form-control" id="tipologia" name="tipologia">
                    <#if tipologias??>
                        <option value=""></option>
                        <#list tipologias as tipologia>
                        <option value="${tipologia.id}">${tipologia.nombre}</option>
                        </#list>
                    </#if>
                        </select>
                    <input type="file" class="form-control" id="archivo" name="archivo"/>
                    </fieldset>
                <button type="submit" class="btn btn-secondary btn-sm">Subir</button>
                </form>
            </div>
        </#if>
        </div>   
    <br />
    </div>

<#include "bandeja-footer.ftl" />
