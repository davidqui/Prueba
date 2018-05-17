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
<script src="/js/app/documento-acta-observaciones.js"></script>
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

<#include "lib/documento-acta/documento-acta-observaciones.ftl">

<#assign estadoModo = estadoModeMap[procesoInstancia.estado.id?string] />

<div class="col-md-8">    
    <#if estadoModo == "EDICION_INFORMACION">   
    <form action="/documento-acta/guardar" method="POST" id="formdoc" enctype='multipart/form-data'>
        <input type="hidden" name="docId" id="docId" value="${(documento.id)!""}" />
        <input type="hidden" name="pinId" id="pinId" value="${(procesoInstancia.id)!""}" />

        <#-- Asunto -->
        <div class="form-group">
            <#assign campo = "asunto" />
            <label for="${campo}">Asunto (*)</label>
            <input type="text" class="form-control" id="${campo}" name="${campo}" value="<#if documentoActaDTO?? >${documentoActaDTO.asunto}<#elseif documento.asunto?? >${documento.asunto}</#if>"/>
            <small class="text-muted">Describe el objeto de la reunión de manera clara y sucinta.</small>
            <div class="error">
                <#if logicValidation?? && logicValidation.containsError("${campo}") >
                    ${logicValidation.getError("${campo}").message}
                </#if>
            </div>
        </div>

        <#-- Lugar -->
        <div class="form-group">
            <#assign campo = "actaLugar" />
            <label for="${campo}">Lugar (*)</label>
            <input type="text" class="form-control" id="${campo}" name="${campo}" value="<#if documentoActaDTO?? >${documentoActaDTO.actaLugar}<#elseif documento.actaLugar?? >${documento.actaLugar}</#if>"/>
            <small class="text-muted">Determina la ciudad donde se llevó a cabo la actividad que motivo la elaboración del acta.</small>
            <div class="error">
                <#if logicValidation?? && logicValidation.containsError("${campo}") >
                    ${logicValidation.getError("${campo}").message}
                </#if>                
            </div>
        </div>

        <#-- Fecha -->
        <div class="form-group">
            <#assign campo = "actaFechaElaboracion" />
            <label for="${campo}">Fecha (*)</label>
            <input type="text" class="form-control datepicker" id="${campo}" name="${campo}"  value="<#if documentoActaDTO?? >${documentoActaDTO.actaFechaElaboracion}<#elseif documento.actaFechaElaboracion?? >${yyyymmdd.format(documento.actaFechaElaboracion)}</#if>" />
            <small class="text-muted">Determina la fecha donde se llevó a cabo la actividad que motivo la elaboración del acta.</small>
            <div class="error">
                <#if logicValidation?? && logicValidation.containsError("${campo}") >
                    ${logicValidation.getError("${campo}").message}
                </#if>                
            </div>
        </div>

        <#-- Nivel de clasificación -->
        <div class="form-group">
            <#assign campo = "clasificacion" />
            <label for="${campo}">Nivel de clasificación (*)</label>           
            <select class="form-control" id="${campo}" name="${campo}">
                <option value=""></option>
                <#if clasificaciones??>
                    <#list clasificaciones as clasificacion>
                        <option value="${clasificacion.id}" <#if (documentoActaDTO?? && (clasificacion.id?string == documentoActaDTO.clasificacion)) || (documento.clasificacion?? && (clasificacion.id == documento.clasificacion.id)) >selected="selected"</#if>>${clasificacion.nombre}</option>
                    </#list>
                </#if>
            </select>
            <small class="text-muted">Se asignará de acuerdo a lo establecido en el decreto 857 de 2014, según sea el caso: Ultrasecreto, Secreto, Confidencial o Restringido.</small>            
            <div class="error">
                <#if logicValidation?? && logicValidation.containsError("${campo}") >
                    ${logicValidation.getError("${campo}").message}
                </#if>                
            </div>
        </div>

        <#-- TRD de Acta -->
        <div class="form-group">
            <#assign campo = "trd" />
            <label for="${campo}">TRD de Acta (*)</label>           
            <select class="form-control" id="${campo}" name="${campo}">
                <option value=""></option>
                <#if subseriesTrdActas??>
                    <#list subseriesTrdActas as subseriesTrdActa>
                        <option value="${subseriesTrdActa.id}" <#if (documentoActaDTO?? && (subseriesTrdActa.id?string == documentoActaDTO.trd)) || (documento.trd?? && (subseriesTrdActa.id == documento.trd.id)) >selected="selected"</#if>>${subseriesTrdActa.codigo} - ${subseriesTrdActa.nombre}</option>
                    </#list>
                </#if>
            </select>
            <small class="text-muted">Código valor tabla de retención documental que corresponda al tipo de acta a registrar.</small>            
            <div class="error">
                <#if logicValidation?? && logicValidation.containsError("${campo}") >
                    ${logicValidation.getError("${campo}").message}
                </#if>                
            </div>
        </div>

        <#-- Número de folios -->
        <div class="form-group">
            <#assign campo = "numeroFolios" />
            <label for="${campo}">Número de folios (*)</label>
            <input type="number" class="form-control" id="${campo}" name="${campo}" value="<#if documentoActaDTO?? >${documentoActaDTO.numeroFolios}<#elseif documento.numeroFolios?? >${documento.numeroFolios}</#if>"/>
            <div class="error"></div>
            <small class="text-muted">Valor numérico equivalente al número de folios útiles que conforman el acta.</small>
            <div class="error">
                <#if logicValidation?? && logicValidation.containsError("${campo}") >
                    ${logicValidation.getError("${campo}").message}
                </#if>                
            </div>            
        </div>        

        <#-- Cargo -->
        <div class="form-group">
            <#assign campo = "cargoElabora" />
            <label for="${campo}" style="font-weight: bold;">Cargo (*)</label>
            <div style="border: 2px solid;">
                <select class="form-control" id="${campo}" name="${campo}">
                    <#if cargosUsuario??>
                        <#list cargosUsuario as cargoUsuario>
                            <option value="${cargoUsuario.id}" <#if (documentoActaDTO?? && (cargoUsuario.id?string == documentoActaDTO.cargoElabora)) || (documento.cargoIdElabora?? && (cargoUsuario.id == documento.cargoIdElabora.id)) >selected="selected"</#if>>${cargoUsuario.nombre}</option>
                        </#list>
                    </#if>
                </select>
            </div>    
            <small class="text-muted">Cargo con el cual se creará el acta.</small>            
            <div class="error">
                <#if logicValidation?? && logicValidation.containsError("${campo}") >
                    ${logicValidation.getError("${campo}").message}
                </#if>                
            </div>          
        </div>        

        <nav class="navbar navbar-default navbar-fixed-bottom text-xs-center hermes-bottombar">
            <button id="guardar-doc-btn" type="submit" class="btn btn-success btn-sm">Guardar</button>
            <#if (!logicValidation?? || (logicValidation?? && logicValidation.isAllOK())) && !documento.estadoTemporal?? >
                <#list procesoInstancia.transiciones() as transicion >
                    <#-- &cargoIdFirma=${cargosXusuario?first.id} -->
                <button id="trx_${transicion.id}" class="btn ${getTransicionStyle(transicion)} btn-sm" type="button" onclick="processTransition(this, '${transicion.replace(procesoInstancia)}')">
                    ${transicion.nombre}
                </button>
                </#list>
            </#if>
        </nav>

    </form> <#-- Cierra formulario principal -->
    </#if>

    <!--
        Observaciones
    -->       	
    <@presentarObservaciones documento utilController estadoModo "observacion" />    
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
