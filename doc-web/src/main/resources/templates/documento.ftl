<#-- 2017-04-25 jgarcia@controltechcg.com Issue #53 #54 (SICDI-Controltech): Se asigna formato numerico para manejo de los miles -->
<#setting number_format="computer">

<#assign pageTitle = documento.asunto!"Documento" />

<#assign instancia = documento.instancia />
<#assign mode = documento.mode />
<#assign deferredJS = "" />

<#assign headScripts>
<script src="/js/eventos_documento.js"></script>
<script src="/js/app/funciones-documento.js"></script>
<script src="/js/tinymce.min.js"></script>
</#assign>

<#import "spring.ftl" as spring />
<#if archivoHeader??>	
	<#include "archivo-header.ftl">
<#else>
	<#include "bandeja-header.ftl" />
</#if>

<#--
    2017-04-07 jgarcia@controltechcg.com Issue #37 (SIGDI-Controltech): Importación de template de funciones de documento. 
 -->
<#include "lib/documento_functions.ftl" />
<#include "gen-arbol-dependencias.ftl">
<#include "gen-arbol-trd.ftl">

<div class="col-md-8">
    <#if relacionado??>
    <strong>Documento relacionado:</strong> <a target="_blank" href="/documento?pin=${relacionado.instancia.id}">${relacionado.asunto!"&lt;Sin asunto&gt;"}</a>
    </#if>
    <table class="table table-sm">    	
        <#if mode.expediente_view && documento.expediente?? >
        <tr><th>Expediente</th><td>${documento.expediente.expNombre}</td></tr>
        </#if> 
        <#if mode.radicado_view && documento.radicado?? >
        <tr><th>Radicado</th><td>${documento.radicado}</td></tr>
        </#if> 
        <#if !mode.radicadoOrfeo_edit && mode.radicadoOrfeo_view && documento.radicadoOrfeo?? >
        <tr><th>Radicado de otro ORFEO</th><td>${(documento.radicadoOrfeo)!""}</td></tr>
        </#if> 
        <#if !mode.trd_edit && mode.trd_view >
        <tr><th>TRD</th><td>${(documento.trd)!""}</td></tr>
        </#if> 
        <#if !mode.destinatario_edit && mode.destinatario_view >
            <#if (documento.dependenciaDestino)?? >
        <tr><th>Dependencia destino</th><td>${(documento.dependenciaDestino)!""}</td></tr>
            </#if>
            <#if (documento.destinatarioNombre)?? >
        <tr><th>Nombre destinatario</th><td>${(documento.destinatarioNombre)!""}</td></tr>
            </#if>
            <#if (documento.destinatarioTitulo)?? >
        <tr><th>Título destinatario</th><td>${(documento.destinatarioTitulo)!""}</td></tr>
            </#if>
            <#if (documento.destinatarioDireccion)?? >
        <tr><th>Dirección destinatario</th><td>${(documento.destinatarioDireccion)!""}</td></tr>
            </#if>
        </#if> 
        <#if !mode.remitente_edit && mode.remitente_view >
            <#if (documento.dependenciaRemitente)?? >
        <tr><th>Remintente</th><td>${(documento.dependenciaRemitente)!""}</td></tr>
            </#if>
            <#if (documento.remitenteNombre)?? >
        <tr><th>Nombre remitente</th><td>${(documento.remitenteNombre)!""}</td></tr>
            </#if>
            <#if (documento.remitenteTitulo)?? >
        <tr><th>Título remitente</th><td>${(documento.remitenteTitulo)!""}</td></tr>
            </#if>
            <#if (documento.remitenteDireccion)?? >
        <tr><th>Dirección remitente</th><td>${(documento.remitenteDireccion)!""}</td></tr>
            </#if>
        </#if> 
        <#if !mode.numeroOficio_edit && mode.numeroOficio_view && documento.numeroOficio?? >
        <tr><th>Número de oficio</th><td>${documento.numeroOficio!""}</td></tr>
        </#if>
        <#if !mode.numeroBolsa_edit && mode.numeroBolsa_view && documento.numeroBolsa?? >
        <tr><th>Número de bolsa de seguridad</th><td>${documento.numeroBolsa!""}</td></tr>
        </#if>
        <#if !mode.fechaOficio_edit && mode.fechaOficio_view && documento.fechaOficio?? >
        <tr><th>Fecha del oficio</th><td><#if documento.fechaOficio?? >${yyyymmdd.format(documento.fechaOficio)}<#else>&lt;No definido&gt;</#if></td></tr>
        </#if>
        <#if !mode.numeroFolios_edit && mode.numeroFolios_view && documento.numeroFolios?? >
        <tr><th>Número de folios</th><td>${documento.numeroFolios!""}</td></tr>
        </#if>
        <#if !mode.plazo_edit && mode.plazo_view && documento.plazo??  >
        <tr><th>Plazo</th><td><#if documento.plazo?? >${yyyymmdd.format(documento.plazo)}<#else>&lt;No definido&gt;</#if></td></tr>
        </#if>
        <#if !mode.clasificacion_edit && mode.clasificacion_view && documento.clasificacion?? >
        <tr class="table-danger"><th>Clasificación</th><td>${(documento.clasificacion.nombre)!""}</td></tr>
        </#if>
        <#if !mode.restriccionDifusion_edit && mode.restriccionDifusion_view && documento.restriccionDifusion?? >
        <tr><th>Restricción de difusión</th><td>${(documento.restriccionDifusion.resDescripcion)!""}</td></tr>
        </#if>
        <#if !mode.gradoExterno_edit && mode.gradoExterno_view && documento.gradoExterno?? >
        <tr><th>Grado</th><td>${(documento.gradoExterno)!""}</td></tr>
        </#if>
        <#if !mode.marcaAguaExterno_edit && mode.marcaAguaExterno_view && documento.marcaAguaExterno?? >
        <tr><th>Marca de agua</th><td>${(documento.marcaAguaExterno?upper_case)!""}</td></tr>
        </#if>
        </table>

    <form action="/documento/savepin?pin=${instancia.id}" method="POST" id="formdoc" enctype='multipart/form-data'>
            <@spring.bind "documento.id" />
        <input type="hidden" name="${spring.status.expression}" id="${spring.status.expression}" value="${(documento.id)!""}" />            
    <!--
        Radicado ORFEO
        -->
        <!--
        <#if mode.radicadoOrfeo_edit >
            <fieldset class="form-group">
                <label for="trd">Radicado otro ORFEO</label>
                <@spring.bind "documento.radicadoOrfeo" />
                <div class="row">
                  <div class="col-lg-12">
                    <div class="input-group">
                      <@spring.formInput "documento.radicadoOrfeo" 'class="form-control"' />
                      <span class="input-group-btn">
                        <button class="btn btn-primary" type="button" data-toggle="modal" data-target="#radicadoOrfeoModal">
                            <span class="hidden-md-down">Consultar</span><span class="hidden-lg-up">C</span>
                        </button>
                      </span>
                    </div>
                  </div>
                </div>
                <div class="error">
                    <@spring.showErrors "<br>"/>
                </div>
            </fieldset>
            <!-- radicadoOrfeoModal -->
        <div class="modal fade" id="radicadoOrfeoModal" tabindex="-1" role="dialog" aria-labelledby="radicadoOrfeoModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar">
                            <span aria-hidden="true">&times;</span>
                            <span class="sr-only">Cerrar</span>
                            </button>
                        <h4 class="modal-title" id="myModalLabel">Búsqueda del documento en otro ORFEO</h4>
                        </div>
                    <div class="modal-body">
                        <div class="alert alert-danger">
                            <p class="lead">Esta funcionalidad no se encuentra habilitada en el momento</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
		</#if>


        <!--
        TRD-->
        <#if mode.trd_edit >
        <fieldset class="form-group">
            <label for="trd">Tabla de retención documental</label>
                <@spring.bind "documento.trd" />
            <input type="hidden" name="${spring.status.expression}" id="trd" value="${(documento.trd.id)!""}" />
            <div class="row">
                <div class="col-lg-12">
                    <div class="input-group">
                        <div class="form-control" id="trdNombre">${(documento.trd)!"Por favor seleccione una subserie..."}</div>
                        <span class="input-group-btn">
                            <button class="btn btn-primary" type="button" data-toggle="modal" data-target="#trdModalArbol">
                                <span class="hidden-md-down">Seleccionar</span><span class="hidden-lg-up">S</span>
                                </button>
                            </span>
                        </div>
                    </div>
                </div>
            <div class="error">
                    <@spring.showErrors "<br>"/>
                </div>
            </fieldset>
            <!-- trdModal -->
        <div class="modal fade" id="trdModal" tabindex="-1" role="dialog" aria-labelledby="trdModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar">
                            <span aria-hidden="true">&times;</span>
                            <span class="sr-only">Cerrar</span>
                            </button>
                        <h4 class="modal-title" id="myModalLabel">Selección de serie</h4>
                        </div>
                    <div class="modal-body">
                        </div>
                    </div>
                </div>
            </div>
        
            <#--
                2018-05-07 edison.gonzalez@controltechcg.com Issue #157 (SIGDI-Controltech): Convertir la selección
                de la TRD en arbol. 
             -->
            <div class="modal fade" id="trdModalArbol" tabindex="-1" role="dialog" aria-labelledby="trdModalArbolLabel" aria-hidden="true">
                <div class="modal-dialog modal-lg" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar">
                                <span aria-hidden="true">&times;</span>
                                <span class="sr-only">Cerrar</span>
                            </button>
                            <h4 class="modal-title" id="trdModalLabel">Selección de tabla de retención documental</h4>
                        </div>
                        <div class="modal-body">
                            <div class="card">
                                <div class="card-block">
                                    <div class="row">
                                        <div class="col-md-7">
                                            <div id="arbol_list_trd">
                                                <@listTrds trds=trds/>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="clearfix"></div>
                            </div>
                            <br /><br />
                        </div>
                    </div>
                </div>
            </div>
            <#assign deferredJSTrd>
                <!--<script type="text/javascript" src="/js/app/trd-modal.js"></script>-->
            </#assign>
            <#assign deferredJS = deferredJS + " " + deferredJSTrd>
        </#if>
    
        <!--
    2017-09-28 edison.gonzalez@controltechcg.com Feature #129 (SICDI-Controltech) feature-129
    gradoExterno
        -->
        <#if (instancia.proceso.id == procesoExternoId || instancia.proceso.id == procesoRegistroDocumentos) && mode.gradoExterno_edit >
        <fieldset class="form-group">
            <label for="gradoExterno">Grado</label>
                <@spring.formInput "documento.gradoExterno" 'class="form-control"' />
            <div class="error">
                    <@spring.showErrors "<br>"/>
                </div>
            </fieldset>
        </#if>
        
        <!--Destinatario texto-->
        <#if ((instancia.variablesAsMap['doc.destinatario.mode'])!"") == 'texto' && mode.destinatario_edit>
        <fieldset class="form-group">
            <label for="destinatarioNombre">Nombre del destinatario (*)</label>
                <@spring.formInput "documento.destinatarioNombre" 'class="form-control"' />
            <div class="error">
                    <@spring.showErrors "<br>"/>
                </div>
            </fieldset>
        <fieldset class="form-group">
            <label for="destinatarioTitulo">Título o cargo del destinatario (*)</label>
                <@spring.formInput "documento.destinatarioTitulo" 'class="form-control"' />
            <div class="error">
                    <@spring.showErrors "<br>"/>
                </div>
            </fieldset>
        <fieldset class="form-group">
            <label for="destinatarioDireccion">Dirección del destinatario (*)</label>
                <@spring.formTextarea "documento.destinatarioDireccion" 'class="form-control"' />
            <div class="error">
                    <@spring.showErrors "<br>"/>
                </div>
            </fieldset>
        </#if>
        
        <!--
            2018-09-03 samuel.delgado@controltechcg.com Feature #129 (SICDI-Controltech) feature-gogs-10
            se cambia el campo de marca de agua por el administrable de destinos externos.
        -->
        <#if instancia.proceso.id == procesoExternoId && mode.destinoExterno_edit >
        <fieldset class="form-group">
            <label for="destinosExternos">Destino Externo(*)</label>
            <@spring.bind "documento.destinoExterno" />
            <select class="selectpicker" id="${spring.status.expression}" name="${spring.status.expression}" data-live-search="true">
                    <#if destinosExternos??>
                <option value=""></option>
                        <#list destinosExternos as dte>
                        <#if dte.id?string == ((documento.destinoExterno.id)!"")?string >
                <option value="${dte.id}" selected="selected">${dte.sigla} - ${dte.nombre}</option>
                        <#else>
                <option value="${dte.id}">${dte.sigla} - ${dte.nombre}</option>
                        </#if>
                        </#list>
                    </#if>
                </select>
            <div class="error">
                    <@spring.showErrors "<br>"/>
                </div>
            </fieldset>
        </#if>
        
        <!--Dependencia destino-->
        <#if ((instancia.variablesAsMap['doc.destinatario.mode'])!"") != 'texto' && mode.destinatario_view && mode.destinatario_edit >
        <fieldset class="form-group">
            <label for="depDestino">Dependencia destino</label>
                <@spring.bind "documento.dependenciaDestino" />
            <input type="hidden" name="${spring.status.expression}" id="dependenciaDestino" value="${(documento.dependenciaDestino.id)!""}" />
            <div class="row">
                <div class="col-lg-12">
                    <div class="input-group">
                        <div class="form-control" id="dependenciaDestinoNombre">${(documento.dependenciaDestino)!"Por favor seleccione una dependencia destino..."}</div>
                        <span class="input-group-btn">
                            <button class="btn btn-primary" type="button" data-toggle="modal" data-target="#dependenciaDestinoModalArbol">
                                <span class="hidden-md-down">Seleccionar</span><span class="hidden-lg-up">S</span>
                                </button>
                            </span>
                        </div>
                    </div>
                </div>
            <div class="error">
                    <@spring.showErrors "<br>"/>
                </div>
            </fieldset>     
        
        <div class="modal fade" id="dependenciaDestinoModalArbol" tabindex="-1" role="dialog" aria-labelledby="dependenciaDestinoModalArbolLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar">
                                    <span aria-hidden="true">&times;</span>
                                    <span class="sr-only">Cerrar</span>
                                    </button>
                        <h4 class="modal-title" id="dependenciaDestinoModalLabel">Selección de dependencia destino</h4>
                    </div>
                    <div class="modal-body">

                        <div class="card">
                            <div class="card-block">
                                <div class="row">
                                    <div class="col-md-7">
                                        <div id="arbol_list_dependenciasj">
                                            <#if did??>
                                                <@listDependencias dependencias=dependencias selected=did href=false/>
                                                <#else>
                                                    <@listDependencias dependencias=dependencias href=false/>
                                            </#if>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="clearfix"></div>
                        </div>
                        <br /><br />
                    </div>
                </div>
            </div>
        </div>
        </#if>
            
        <!--Remitente texto-->
            
        <#if ((instancia.variablesAsMap['doc.remitente.mode'])!"") == 'texto' && mode.remitente_edit>
        <fieldset class="form-group">
            <label for="remitenteNombre">Nombre del remitente (*)</label>
                <@spring.formInput "documento.remitenteNombre" 'class="form-control"' />
            <div class="error">
                    <@spring.showErrors "<br>"/>
                </div>
            </fieldset>
        <fieldset class="form-group">
            <label for="remitenteTitulo">Título o cargo del remitente (*)</label>
                <@spring.formInput "documento.remitenteTitulo" 'class="form-control"' />
            <div class="error">
                    <@spring.showErrors "<br>"/>
                </div>
            </fieldset>
        <fieldset class="form-group">
            <label for="remitenteDireccion">Dirección del remitente (*)</label>
                <@spring.formTextarea "documento.remitenteDireccion" 'class="form-control"' />
            <div class="error">
                    <@spring.showErrors "<br>"/>
                </div>
            </fieldset>
        </#if>
        
        <!--
    2017-09-29 edison.gonzalez@controltechcg.com Feature #129 (SICDI-Controltech) feature-129
    Restriccion de difusion
        -->
        <#if mode.restriccionDifusion_edit >
        <fieldset class="form-group">
            <label for="restriccionDifusion">Restricción de difusión</label>
                <@spring.bind "documento.restriccionDifusion" />
            <select class="form-control" id="${spring.status.expression}" name="${spring.status.expression}">
                    <#if restriccionesDifusion??>
                <option value=""></option>
                        <#list restriccionesDifusion as res>
                        <#if res.id?string == ((documento.restriccionDifusion.id)!"")?string >
                <option value="${res.id}" selected="selected">${res.resDescripcion}</option>
                        <#else>
                <option value="${res.id}">${res.resDescripcion}</option>
                        </#if>
                        </#list>
                    </#if>
                </select>
            <small class="text-muted">Establecer la restricción en la difusión  del documento.</small>
            <div class="error">
                    <@spring.showErrors "<br>"/>
                </div>
            </fieldset>
        </#if>
        
        <!--Asunto-->
        <#if mode.asunto_edit>
        <fieldset class="form-group">
            <label for="asunto">Asunto (*)</label>
                <@spring.formInput "documento.asunto" 'class="form-control"' />
            <small class="text-muted">El asunto del documento es un texto corto que describe el documento, preferiblemente de manera única.</small>
            <div class="error">
                    <@spring.showErrors "<br>"/>
                </div>
            </fieldset>
        </#if>
        
        <!--Dependencia remitente-->
        <#if ((instancia.variablesAsMap['doc.remitente.mode'])!"") != 'texto' && mode.remitente_view && mode.remitente_edit >
        <fieldset class="form-group">
            <label for="depRemitente">Dependencia remitente</label>
                <@spring.bind "documento.dependenciaRemitente" />
            <input type="hidden" name="${spring.status.expression}" id="dependenciaRemitente" value="${(documento.dependenciaRemitente.id)!""}" />
            <div class="row">
                <div class="col-lg-12">
                    <div class="input-group">
                        <div class="form-control" id="dependenciaRemitenteNombre">${(documento.dependenciaRemitente)!"Por favor seleccione una dependencia remitente..."}</div>
                        <span class="input-group-btn">
                            <button class="btn btn-primary" type="button" data-toggle="modal" data-target="#dependenciaRemitenteModal">
                                <span class="hidden-md-down">Seleccionar</span><span class="hidden-lg-up">S</span>
                                </button>
                            </span>
                        </div>
                    </div>
                </div>
            <div class="error">
                    <@spring.showErrors "<br>"/>
                </div>
            </fieldset>
            <!-- dependenciaRemintenteModal -->
        <div class="modal fade" id="dependenciaRemintenteModal" tabindex="-1" role="dialog" aria-labelledby="dependenciaRemintenteModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar">
                            <span aria-hidden="true">&times;</span>
                            <span class="sr-only">Cerrar</span>
                            </button>
                        <h4 class="modal-title" id="dependenciaRemintenteModalLabel">Selección de dependencia remitente</h4>
                        </div>
                    <div class="modal-body">
                        </div>
                    </div>
                </div>
            </div>
            <#assign deferredJSDepRemintente>
        <script type="text/javascript">
            <!--
            var depPadreRemintente = -1;
            var depStackRemintente = new Array();

            function loadSubDependenciaRemintente(id) {
            if(depStackRemintente.length == 0) {
            depStackRemintente.push(id);
            } else if(depStackRemintente.length > 0 && depStackRemintente[depStackRemintente.length - 1] != id) {
            depStackRemintente.push(id);
            }
            var nombre = $(this).attr("nombre");

            $("#dependenciaRemintenteModal .modal-body").empty();
            $("#dependenciaRemintenteModalLabel").text(nombre);

            $.getJSON("/dependencias/dependencias.json?id=" + id, function(deps) {

            modalBody = $("#dependenciaRemintenteModal .modal-body");

            $("<a/>", {html: "Regresar", id:'btnRegresarRemintente', class:'btn btn-secondary btn-sm'}).appendTo(modalBody);
            if(id != null && id !== 'undefined') {
            $("#btnRegresarRemintente").click(function(){
            if(depStackRemintente.length > 1) {
            depStackRemintente.splice(1, depStackRemintente.length - 1);
            } else {
            depStackRemintente = new Array();
            }
            if(depStackRemintente.length > 0) {
            prev = depStackRemintente[depStackRemintente.length - 1];
            loadSubDependenciaRemintente(prev);
            } else {
            loadDependenciaRemintente();
            }
            });
            } else {
            $("#btnRegresarRemintente").click(loadDependenciaRemintente);
            }

            // Si no hay resultados
            if(deps.length == 0) {
            msg = $("<div/>", { html: "<br/><h6>No tiene dependencias subordinadas</h6>" });
            msg.appendTo(modalBody);
            }

            table = $("<table/>", { class: "table" });
            $.each(deps, function(i, item){
            tr = $("<tr/>");
            tr.appendTo(table);

            linkDiv = '<a href="#" nombre="' + item.nombre + '" id="dep' + item.id + '" class="btn btn-primary btn-sm mark-sel"><span class="hidden-md-down">Seleccionar</span><span class="hidden-lg-up">S</span></a> <strong>' + item.nombre + '</strong>&nbsp;&nbsp;&nbsp;<a href="#" id="dep' + item.id + '" nombre="' + item.nombre + '" class="mark-dep">Subdependencias...</a>';
            td = $("<td/>", { html: linkDiv });
            td.appendTo(tr);

            buttonDiv = '';
            td = $("<td/>", { html: buttonDiv });
            td.appendTo(tr);
            });

            table.appendTo(modalBody);                           

            $("#dependenciaRemintenteModal .modal-body .mark-dep").click(function(){
            var id = $(this).attr("id").substring(3);
            loadSubDependenciaRemintente(id);
            });
            $("#dependenciaRemintenteModal .modal-body .mark-sel").click(function(e) {
            var id = $(this).attr("id").substring(3);
            var nombre = $(this).attr("nombre");
            $("#dependenciaRemintente").val(id);
            $("#dependenciaRemintenteNombre").text(nombre);
            $("#dependenciaRemintenteModal").modal('hide');
            });
            });
            };

            function loadDependenciaRemintente(e) {
            $("#dependenciaRemintenteModalLabel").text("Dependencias");
            $("#dependenciaRemintenteModal .modal-body").empty();
            $.getJSON("/dependencias/dependencias.json", function(deps) {
            table = $("<table/>", { class: "table" });
            $.each(deps, function(i, item){
            tr = $("<tr/>");
            tr.appendTo(table);

            linkDiv = '<a href="#" nombre="' + item.nombre + '" id="dep' + item.id + '" class="btn btn-primary btn-sm mark-sel"><span class="hidden-md-down">Seleccionar</span><span class="hidden-lg-up">S</span></a> <strong>' + item.nombre + '</strong>&nbsp;&nbsp;&nbsp;<a href="#" id="dep' + item.id + '" nombre="'+item.nombre+'" class="mark-dep">Subdependencias...</a>';
            td = $("<td/>", { html: linkDiv });
            td.appendTo(tr);

            buttonDiv = '';
            td = $("<td/>", { html: buttonDiv });
            td.appendTo(tr);
            });
            modalBody = $("#dependenciaRemintenteModal .modal-body");
            table.appendTo(modalBody);

            $("#dependenciaRemintenteModal .modal-body .mark-dep").click(function(){
            var id = $(this).attr("id").substring(3);
            loadSubDependenciaRemintente(id);
            });
            $("#dependenciaRemintenteModal .modal-body .mark-sel").click(function(e) {
            id = $(this).attr("id").substring(3);
            nombre = $(this).attr("nombre");
            $("#dependenciaRemintente").val(id);
            $("#dependenciaRemintenteNombre").text(nombre);
            $("#dependenciaRemintenteModal").modal('hide');
            });
            });
            };
            $('#dependenciaRemintenteModal').on('show.bs.modal', loadDependenciaRemintente);
            -->
            </script>
            </#assign>
            <#assign deferredJS = deferredJS + " " + deferredJSDepRemintente>
        </#if>
    
        <!--Número de oficio y fecha de oficio-->
        <#if mode.numeroOficio_edit >
        <fieldset class="form-group">
            <label for="numeroOficio">Número de oficio (*)</label>
                <@spring.formInput "documento.numeroOficio" 'class="form-control"' />
            <div class="error">
                    <@spring.showErrors "<br>"/>
                </div>
            </fieldset>
        </#if>
        <#if mode.fechaOficio_edit >
        <fieldset class="form-group">
            <label for="fechaOficio">Fecha del oficio (*)</label>
                <@spring.bind "documento.fechaOficio" />
            <input class="form-control datepicker" id="${spring.status.expression}" name="${spring.status.expression}" value="<#if documento.fechaOficio??>${yyyymmdd.format(documento.fechaOficio)}</#if>"/>
            <div class="error">
                    <@spring.showErrors "<br>"/>
                </div>
            </fieldset>
        </#if>
        <!--


            Número de bolsa de seguridad
        -->
        <#if mode.numeroBolsa_edit >
        <fieldset class="form-group">
            <label for="numeroBolsa">Número de bolsa de seguridad</label>
                <@spring.formInput "documento.numeroBolsa" 'class="form-control"' />
            <div class="error">
                    <@spring.showErrors "<br>"/>
                </div>
            </fieldset>
        </#if>
        <!--


            Número de folios y plazo
        -->
        <#if mode.numeroFolios_edit >
        <fieldset class="form-group">
            <label for="numeroFolios">Número de folios (*)</label>
                <@spring.formInput "documento.numeroFolios" 'class="form-control"' />
            <div class="error">
                    <@spring.showErrors "<br>"/>
                </div>
            </fieldset>
        </#if>
        <#if mode.plazo_edit>
        <fieldset class="form-group">
            <label for="plazo">Plazo</label>
                <@spring.bind "documento.plazo" />
            <input type="text" class="form-control datepicker" id="${spring.status.expression}" name="${spring.status.expression}"  value="<#if documento.plazo??>${yyyymmdd.format(documento.plazo)}</#if>" />
            <small class="text-muted">Cuando la subserie de la TRD seleccionada establece un plazo, este campo será completado automáticamente por el sistema.</small>
            <div class="error">
                    <@spring.showErrors "<br>"/>
                </div>
            </fieldset>
        </#if>
        <!--


            Clasificación
        -->
        <#if mode.clasificacion_edit >
        <fieldset class="form-group">
            <label for="clasificacion">Nivel de clasificación (*)</label>
                <@spring.bind "documento.clasificacion" />
            <select class="form-control" id="${spring.status.expression}" name="${spring.status.expression}">
                    <#if clasificaciones??>
                <option value=""></option>
                        <#list clasificaciones as cla>
                        <#if cla.id?string == ((documento.clasificacion.id)!"")?string >
                <option value="${cla.id}" selected="selected">${cla.nombre}</option>
                        <#else>
                <option value="${cla.id}">${cla.nombre}</option>
                        </#if>
                        </#list>
                    </#if>
                </select>
            <div class="error">
                    <@spring.showErrors "<br>"/>
                </div>
            </fieldset>
        </#if>

        
        <!--
            Contenido
        -->
        <#if mode.plantilla_edit >
        <fieldset class="form-group">
            <fieldset class="form-group">
                <label for="file">Plantilla de Office Word (*.docx)</label>
			        <@spring.bind "documento.docx4jDocumento" />
			        <#if documento.docx4jDocumento?has_content>
                <a href="/documento/download/${documento.docx4jDocumento}">Descargar plantilla actual</a>
			        </#if>
                <input class="form-control" type="file" name="file" id="file">
                <div class="error">
                    	<@spring.showErrors "<br>"/>
                    </div>
                </fieldset>
            </fieldset>		
        </#if>

        <#-- <div class="error">
            <@spring.showErrors "<br>"/>
        </div> -->

		<#if documento.mostrarPreview() == "Y">
        <div class="card">
            <div class="card-header">
                Vista previa de la plantilla del documento.
                </div>
            <iframe src="/ofs/viewer?file=/ofs/downloaddocxtopdf/${documento.docx4jDocumento}/${documento.id}" width="100%" height="700px"></iframe>
            </div>   
	    </#if>                   
        <#if mode.contenido_view >
            <#if (documento.pdf)??>
        <div class="card">
            <div class="card-header">Contenido</div>
            <iframe src="/ofs/viewer?file=/ofs/download/${documento.pdf}" width="100%" height="700px"></iframe>                    
            </div>                   	               
            </#if>            
        </#if>
        
        <!--
            cargo
        -->
        <#if mode.cargoIdElabora_edit && cambiarIdCargoElabora!false>
        <fieldset class="form-group">
            <label for="cargoIdElabora" style="font-weight: bold;">Cargo con el cual se creará el documento (*)</label>
                <@spring.bind "documento.cargoIdElabora" />
            <div style="border: 2px solid;">
            <select class="form-control" id="${spring.status.expression}" name="${spring.status.expression}">
                    <#if cargosXusuario??>   
                        <#list cargosXusuario as cla>
                        <#if cla.id?string == ((documento.cargoIdElabora.id)!"")?string >
                            <option value="${cla.id}" selected="selected">${cla.nombre}</option>
                        <#else>
                            <option value="${cla.id}">${cla.nombre}</option>
                        </#if>
                        </#list>
                    </#if>
                </select>
                </div>
            
            <div class="error">
                    <@spring.showErrors "<br>"/>
                </div>
            </fieldset>
        </#if>
        
        <#if mode.cargoIdFirma_edit && cambiarIdCargoFirma!false>
            <fieldset class="form-group">
                <label for="cargoIdFirma" style="font-weight: bold;">Cargo con el cual se firmará el documento (*)</label>
                <div style="border: 2px solid;">
                <select class="form-control" id="cargoIdFirma" name="cargoIdFirma" onchange="onChangeDocumentoCargoFirma(this, '17')">
                    <#if cargosXusuario??>   
                        <#list cargosXusuario as cla>
                        <#if cla.id?string == ((documento.cargoIdFirma.id)!"")?string >
                            <option value="${cla.id}" selected="selected">${cla.nombre}</option>
                        <#else>
                            <option value="${cla.id}">${cla.nombre}</option>
                        </#if>
                        </#list>
                    </#if>    
                </select>
                    </div>
            </fieldset>
        </#if>
        <nav class="navbar navbar-default navbar-fixed-bottom text-xs-center hermes-bottombar">
        	<#--
        		2017-05-19 jgarcia@controltechcg.com Issue #73 (SICDI-Controltech) feature-73
        	-->
        	<#assign asignacionCiclica = mustApplyAsignacionCiclica(documento) />

            <div class="container">

        		<#if mode.guardar_view >
        			<#--
        				2017-02-15 jgarcia@controltechcg.com Issue #140: Se deshabilita el botón de guardar, tras dar el primer click por parte del usuario.        		    
        		            		    
        		        2017-04-21 jgarcia@controltechcg.com Issue #50 (SICDI-Controltech):
        		        Deshabilitar el botón Guardar del documento cuando se encuentre habilitada la posibilidad
        		        de extraer el documento de la bandeja de apoyo y consulta para el proceso de radicacion de documentos.
        		    -->        		    
        		    <#assign deshabilitarTransicionesPorExtraccionConsulta=documentoEnConsulta && puedeExtraerDeBandejaConsulta />
        		    <#if !deshabilitarTransicionesPorExtraccionConsulta>
        		    	<#--
        		    		2017-05-19 jgarcia@controltechcg.com Issue #73 (SICDI-Controltech) feature-73
        		    		Cambio de estilo del botón guardar cuando aplique la asignación cíclica.
        		    	-->
        		    	<#assign btnGuardarStyle = "" />
        		    	<#if !asignacionCiclica >
        		    		<#assign btnGuardarStyle = "btn-success" /> 
        		    	</#if>

        		    	<#--
        		    		2017-06-02 jgarcia@controltechcg.com Issue #100 (SICDI-Controltech) feature-73:
        		    		Corrección para activar el botón de la acción "Guardar" únicamente cuando se encuentre
        		    		en sesión el usuario asignado al documento. 
        		    	-->
        		    	<#if (usuariologueado.id == documento.instancia.asignado.id)>
                <!--#181 se agrega loader --> 
                <button id="guardar-doc-btn" type="submit" class="btn ${btnGuardarStyle} btn-sm" onclick="loading(event);">Guardar</button>

                <script type="text/javascript">
                    $(document).ready(function () {
                    $("#guardar-doc-btn").click(function() {
                    $(this).attr("disabled", "disabled");
                    $(this).text("Procesando...");

                    // 2017-02-17 jgarcia@controltechcg.com Issue #143: Corrección para funcionalidad en Google Chrome.

                    $(this).closest("form").submit();
                    });
                    });
                    </script>
	        			</#if>
					</#if>        			
        		</#if>


        <!-- 
             2017-02-02 jgarcia@controltechcg.com Issue #129: Variable que se 
             encuentra en el documento que en tiempo de validación que permite 
                 establecer si tiene errores para manejo en el formulario. -->
        		<#if !documento.withErrors >     		        	
        		    <#--
        		        2017-04-20 jgarcia@controltechcg.com Issue #50 (SICDI-Controltech):
        		        Manejo de acción de bandeja de consulta en el formulario de documento.
        		    -->    		        	
        		    <#if puedeEnviarABandejaConsulta>
        		    	<#--
                	    2017-04-27 jgarcia@controltechcg.com Issue #64 (SICDI-Controltech):
                	    Cambio del nombre del botón y del color de botón de Enviar a Consulta. 
		                -->
                <a href="/documento-consulta/enviar-apoyo-consulta?pin=${instancia.id}" class="btn btn-warning btn-sm">
		                	<#--
		                		2017-05-19 jgarcia@controltechcg.com Issue #73 (SICDI-Controltech) feature-73:
		                		Cambio de nombre del botón de la acción "Apoyo y Consulta" a "Archivar como Apoyo".
		                	-->
                    Archivar como Apoyo
                    </a>
        		    <#elseif puedeExtraerDeBandejaConsulta>
        		    	<#--
                	    2017-04-27 jgarcia@controltechcg.com Issue #64 (SICDI-Controltech):
                	    Cambio del nombre del botón y del color de botón de Enviar a Entrada. 
		                -->        		    
                <a href="/documento-consulta/extraer-apoyo-consulta?pin=${instancia.id}" class="btn btn-warning btn-sm">
                    Devolver a Bandeja de Entrada
                    </a>
        		    </#if>

        		    <#--
        		        2017-04-21 jgarcia@controltechcg.com Issue #50 (SICDI-Controltech):
        		        Deshabilitar las transiciones y otras acciones del documento cuando se encuentre habilitada la posibilidad
        		        de extraer el documento de la bandeja de apoyo y consulta.
        		    -->
        		    <#assign deshabilitarTransicionesPorExtraccionConsulta=documentoEnConsulta && puedeExtraerDeBandejaConsulta />
        		    <#if !deshabilitarTransicionesPorExtraccionConsulta>
        		    	<#--
        		    		2017-04-26 jgarcia@controltechcg.com Issue #57 (SICDI-Controltech):
        		    		Cambio en el método que valida si un documento puede presentar o no las transiciones.
        		    		
        		    		2017-06-12 jgarcia@controltechcg.com Issue #93 (SICDI-Controltech feature-93:
        		    		Modificación para evaluar la presentación de transiciones de anulación en documentos respuesta en construcción.
        		    	-->
        		    	<#assign transiciones = documento.instancia.transiciones() />          		        	        		        		        	    		        	    		       
		        		<#if documento.presentarTransiciones() >
				            <#if transiciones??>
				                <#list transiciones as transicion >
					        		<#-- 
					        		    2017-04-07 jgarcia@controltechcg.com Issue #37 (SIGDI-Controltech): Modificación en el template de documento para validar
					        		    si el documento presentado corresponde a la respuesta de un documento relacionado en estado en construcción. En caso de
					        		    ser así, se valida si la transacción a presentar corresponde a "Anular", para no presentarla dentro de las opciones al
					        		    usuario en sesión.
							        		    
					        		    2017-05-18 jgarcia@controltechcg.com Issue #87 (SICDI-Controltech) feature-87:
					        		    Modificación en la bandeja de entrada y template de documento para validar si el documento presentado corresponde a la
					        		    respuesta de un documento relacionado en estado de revisión por jefe de dependencia. En caso de ser así, se valida si la
					        		    transición a presentar corresponde a "Anular", para no presentarla dentro de las opciones alusuario en sesión.					        		    
					        		-->
					        		<#if !((documento.documentoRespuestaEnConstruccion() || documento.documentoRespuestaEnRevisionJefeDependencia()) 
					        			&& isTransaccionAnular(transicion)) >					        			
					        			<#--
        									2017-05-19 jgarcia@controltechcg.com Issue #73 (SICDI-Controltech) feature-73
        									
        									2017-06-12 jgarcia@controltechcg.com Issue #93 (SICDI-Controltech) feature-93
        								-->
        								<#if !isTransicionAnularRespuesta(transicion) >
                                                                                <#--
                                                                                2018-02-28 jgarcia@controltechcg.com Issue #151  (SICDI-Controltech) feature-151
                                                                                Se establecen las mismas condiciones para determinar la selección de cargo en tiempo de firma y envío
                                                                                validando además que la transición a presentar sea la correspondiente para establecer como parámetro
                                                                                el cargo a seleccionar por defecto.
                                                                                Este valor es cambiado en la acción de selección del selector de cargos.
                                                                                2018-04-24 edison.gonzalez@controltechcg.com Issue #156 (SICDI-Controltech) feature-156
                                                                                Se realiza la modificación de los componentes que controlan la transición de los documentos
                                                                                del tag <a> por el tag <button>.
                                                                                -->
                                                                                <!--#181 se agrega loader --> 
                                                                                <#if (mode.cargoIdFirma_edit && cambiarIdCargoFirma!false) && isTransicionFirmar(transicion)>
                                                                                    <button id="trx_${transicion.id}" class="btn ${getTransicionStyle(transicion)} btn-sm" type="button" onclick="loading(event); processTransition(this, '${transicion.replace(instancia)}&cargoIdFirma=${cargosXusuario?first.id}')">
                                                                                        ${transicion.nombre}
                                                                                    </button>
                                                                                <#else>
                                                                                    <button class="btn ${getTransicionStyle(transicion)} btn-sm" type="button" onclick="loading(event); processTransition(this, '${transicion.replace(instancia)}&cargoIdFirma=${cargosXusuario?first.id}')">
                                                                                        ${transicion.nombre}
                                                                                    </button>
                                                                                </#if>
                                                                            </#if>
                                                                        </#if>
				                </#list>
				            </#if>
				        <#--
				        	2017-06-12 jgarcia@controltechcg.com Issue #93 (SICDI-Controltech feature-93:
        		    		Modificación para evaluar la presentación de transiciones de anulación en documentos respuesta en construcción.
				        -->
				        <#elseif transiciones?? >
				        	<#list transiciones as transicion >				        		
				        		<#if (documento.documentoRespuestaEnConstruccion() || documento.documentoRespuestaEnRevisionJefeDependencia())
				        			&& isTransicionAnularRespuesta(transicion) >
                <a href="${transicion.replace(instancia)}" class="btn ${getTransicionStyle(transicion)} btn-sm">${transicion.nombre}</a>
				        		</#if>
				        	</#list>
				        </#if>
				        <#--
				        	2017-05-15 jgarcia@controltechcg.com Issue #81 (SICDI-Controltech):
		 					hotfix-81 -> Corrección para que los documentos del proceso externo
		 					no tengan acciones ni transiciones después de enviados.
				        -->  
			            <#if documento.activarAccionReasignacion() && usuariologueado?? && documento.elabora.id != usuariologueado.id >
			            	<#-- 
			            	    2017-03-13 jgarcia@controltechcg.com Issue #48 (SIGDI-Controltech): Validación para identificar si el documento se encuentra en estado 
			            	    Enviado para el proceso Interno, y el usuario en sesión corresponde al usuario que firma el documento, para evitar que se presente el 
			            	    botón de Reasignar.
			            	    
			            	    2017-06-01 jgarcia@controltechcg.com Issue #100 (SICDI-Controltech) feature-73: Modificación sobre la regla de negocio implementada
			            	    en el Issue #48, para que valide cuando el documento se encuentra en estado Enviado para el proceso Interno, solo presente la acción
			            	    de Reasignar el usuario en sesión corresponde al usuario asignado en la instancia del proceso. Complemento de la misma validación
			            	    para que aplique con el proceso de radicación de documentos, cuando el documento se encuentra en estado de revisión.
			            	-->
			            	<#if ((documento.esDocumentoRevisionRadicado() || documento.esDocumentoEnviadoInterno()) 
			            		&& (usuariologueado.id == documento.instancia.asignado.id)) >
			            		<#--
			            			2017-05-19 jgarcia@controltechcg.com Issue #73 (SICDI-Controltech) feature-73
			            		-->
                <a href="${getReasignarURL(documento)}" class="btn btn-danger btn-sm">
                    Reasignar
                    </a> 
			                </#if>
                
			            </#if>     
                                    <#-- 2018-08-02 samuel.delgado@controltechcg.com Issue #181 (SIGDI-Controltech): 
                                        Botón para enviar a expediente un documento ya finalizado -->
                                    <#if expedientesValidos?? && ((((documento.esDocumentoRevisionRadicado() || documento.esDocumentoEnviadoInterno()) 
			            		&& (usuariologueado.id == documento.instancia.asignado.id))) || (documento.aprueba?? && usuariologueado.id == documento.elabora.id)) && !documento.expediente??>
                                        <a type="button" class="btn btn-primary btn-sm" data-toggle="modal" data-target="#modal-enviar-expediente">
                                            Asignar Expediente
                                        </a>
                                    </#if>
	                </#if>
	            </#if>
                </div>
            </nav>
        </form>

    <!--
        Observaciones
    -->
    <div class="card m-y">                           		        
        <#if (documento.observaciones)??>
        <h5 class="card-title" style="padding: 16px;margin: 0;">Observaciones</h5>
        <div class="card-body" id="obsDiv" style="padding: 0 16px;max-height: 373px;overflow: hidden;overflow-y: auto;">
            <#list documento.observaciones as obs>
                <!--
                    2018-09-05 samuel.delgado@controltechcg.com hotfix gogs #11 (SICDI-Controltech)
                    hotfix-gogs-11.
                -->
                <#if (((documento.esDocumentoRevisionRadicado() || documento.esDocumentoEnviadoInterno()) 
                && (usuariologueado.id == documento.instancia.asignado.id))) >
                    <#if (obs.cuando >= fechaMinObservaciones)>
                    <hr/>
                    <strong>${utilController.nombre(obs.quien)}</strong>, <em> ${obs.cuando?string('yyyy-MM-dd hh:mm a:ss')}</em>
                    <p>${obs.texto}</p>
                    </#if>
                <#else>
                    <hr/>
                    <strong>${utilController.nombre(obs.quien)}</strong>, <em> ${obs.cuando?string('yyyy-MM-dd hh:mm a:ss')}</em>
                    <p>${obs.texto}</p>
                </#if>
	    </#list>
            </div>
            <#if mode.observaciones_edit>
        <div class="card-block cus-gray-bg">
            <form method="post" id="obsForm" >
                <fieldset class="form-group">
                    <textarea class="form-control" id="observacion" name="observacion"></textarea>
                </fieldset>
                <div class="row">
                    <div class="col-xs-4">
                        <a href="#" class="btn btn-secondary btn-sm" id="obsButton">Comentar</a>
                    </div>
                    <#--
                        2018-05-24 jgarcia@controltechcg.com Issue #172 (SICDI-Controltech)
                        feature-172: Selector para observaciones por defecto.
                    -->
                    <div class="col-xs-8">
                        <select id="doc-obs-defecto-select" name="doc-obs-defecto-select" class="form-control input-sm" onchange="setObservacionDefecto(this, 'observacion')">
                            <option value="">Lista de observaciones por defecto:</option>
                            <#list observacionesDefecto as observacionDefecto >
                            <option value="${observacionDefecto.id}">${observacionDefecto.textoObservacion}</option>
                            </#list>
                        </select>
                    </div>
                </div>
            </form>
        </#if>
        </div>
                <#assign deferredJSObs>
        <script type="text/javascript">
            $("#obsButton").click(function(event){
            event.preventDefault();

            var docObs = $.trim( $("#observacion").val() );

            if( docObs.length <= 0 ){

            alert("Debe ingresar una observación");

            }else{

            $.ajax({
            type: "POST",
            url: "/documento/observacion?doc=${documento.id}",
            data: $("#obsForm").serialize(),
            success: function(data) {
            var p = $("<p/>");
            p.html(data.texto);
            $("#obsDiv").prepend(p);
            $("#observacion").val('');
            var em = $("<em/>");
            em.text(data.cuando);
            $("#obsDiv").prepend(em);
            var strong = $("<strong/>");
            strong.text(data.quien + ", ");
            $("#obsDiv").prepend(strong);
            var hr = $("<hr/>");
            $("#obsDiv").prepend(hr);
            
            }
            });
            }
            });
            </script>
                </#assign>
                <#assign deferredJS = deferredJS + deferredJSObs />
            </#if>
        </div>

    </div>
<div class="col-md-4">
    <div class="card">
        <div class="card-header">
            <!--#181 se agrega loader --> 
            <a href="/proceso/instancia/detalle?pin=${instancia.id}" onclick="loading(event);">Proceso</a>
            </div>
        <div class="card-block">

        	<#-- 2017-02-22 jgarcia@controltechcg.com Issue #141: Presentación fecha de firma -->
        	<#--  
            	2017-03-17 jgarcia@controltechcg.com Issue #28 (SIGDI-Incidencias01): Cambio etiqueta presentación fecha creación
            	del documento. 
            -->
        	<#if (doc_cuando_firma??)>
            <strong>Fecha de creación documento:</strong> ${doc_cuando_firma?string('yyyy-MM-dd hh:mm a')}<br/>
        	<#elseif (documento.cuando??) >
            <strong>Fecha de creación documento:</strong> ${documento.cuando?string('yyyy-MM-dd hh:mm a')}<br/>
            <#else>
            <strong>Fecha de creación documento:</strong><br/>
            </#if>
            <#--  
            	2017-03-17 jgarcia@controltechcg.com Issue #28 (SIGDI-Incidencias01): Presentación fecha de última modificación. 
            -->
            <#if (documento.cuandoMod??)>
            <strong>Fecha última acción:</strong> ${documento.cuandoMod?string('yyyy-MM-dd hh:mm a')}<br/>
            <#else>
            <strong>Fecha última acción:</strong><br/>
            </#if>

            <strong>Proceso:</strong> ${instancia.proceso.nombre}<br/>
            <strong>Estado:</strong> ${instancia.estado.nombre}<br/>
            <strong>Usuario asignado:</strong> ${instancia.asignado}<br/>
            <strong>Enviado por:</strong> ${documento.usuarioUltimaAccion!"&lt;Nadie&gt;"}<br/>
            <strong>Elabora:</strong> ${documento.elabora!"&lt;Nadie&gt;"}<br/>
            <strong>Cargo elabora:</strong>
                <#if (documento.cargoIdElabora??)>
                    ${documento.cargoIdElabora.carNombre!"&lt;Ninguno&gt;"}
                <#else>
                    ${"&lt;Ninguno&gt;"}
                </#if>
            <br/>
            <strong>Revisó:</strong> ${documento.aprueba!"&lt;Nadie&gt;"}<br/>
            <strong>Visto bueno:</strong> ${documento.vistoBueno!"&lt;Nadie&gt;"}<br/>
            <strong>Firma:</strong> ${documento.firma!"&lt;Nadie&gt;"}<br/>
            <strong>Cargo firma:</strong>
                <#if (documento.cargoIdFirma??)>
                    ${documento.cargoIdFirma.carNombre!"&lt;Ninguno&gt;"}
                <#else>
                    ${"&lt;Ninguno&gt;"}
                </#if>
            <br/>
            </div>
        </div>

    <!--
        2018-04-11 jgarcia@controltechcg.com Issue #156 (SICDI-Controltech):
        Se reactiva la sección de multidestino, modificando los componentes de
        selección para utilizar el árbol de dependencias y la nueva entidad
        DependenciaCopiaMultidestino. (feature-156)
    -->
    <#if documento.mostrarMultidestino()>
    	<input type="hidden" id="idDocumentoDependenciaDestinoAdicionalModal" value="${documento.id}" />
    	
    	<div class="card">
	    <div class="card-header">
                Unidades o dependencias destino adicionales a enviar el documento
	        <#if mode.guardar_view >
		    <button class="btn btn-primary" type="button" data-toggle="modal" data-target="#dependenciaDestinoAdicionalModal">
		        <span class="hidden-md-down"> Adicionar </span><span class="hidden-lg-up">S</span>
		    </button>
		</#if>    
	    </div>
	    <div class="card-block pre-scrollable" >
		<#list documento.dependenciaCopiaMultidestinos as dependenciaCopiaMultidestino>
                    <#if dependenciaCopiaMultidestino.activo>
                        <strong>Dependencia:</strong>
                            <#if dependenciaCopiaMultidestino.dependenciaDestino.sigla?? >
                                ${ dependenciaCopiaMultidestino.dependenciaDestino.sigla} - 
                            </#if>
                            ${dependenciaCopiaMultidestino.dependenciaDestino}<br/>
                        <strong>Fecha:</strong> ${dependenciaCopiaMultidestino.cuandoMod?string('yyyy-MM-dd hh:mm a:ss')}<br/>
                        <strong>Modificado por:</strong> ${dependenciaCopiaMultidestino.quien}<br/>
                        <#if mode.guardar_view >
                            <a id="eliminarMultidestino" href="#" onclick="eliminarDocumentoDependenciaAdicional( '${dependenciaCopiaMultidestino.id}' );return false;" class="btn btn-sm btn-danger">Eliminar</a><br/>
                        </#if>
                        <hr/>
                    </#if>
	        </#list>
	    </div>
	</div>
        
        <div class="modal fade" id="dependenciaDestinoAdicionalModal" tabindex="-1" role="dialog" aria-labelledby="dependenciaDestinoModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar">
                            <span aria-hidden="true">&times;</span>
                            <span class="sr-only">Cerrar</span>
                        </button>
                        <h4 class="modal-title" id="dependenciaDestinoModalLabel">Selección de copia dependencia destino</h4>
                    </div>
                    <div class="modal-body">
                        <div class="card">
                            <div class="card-block">
                                <div class="row">
                                    <div class="col-md-7">
                                        <div id="arbol_list_dependenciasadi">
                                            <#if did??>
                                                <@listDependencias dependencias=dependencias selected=did href=false/>
                                                <#else>
                                                    <@listDependencias dependencias=dependencias href=false/>
                                            </#if>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="clearfix"></div>
                        </div>
                        <br /><br />
                    </div>
                </div>
            </div>
        </div>
    </#if>

    <#if (documento.vistosBuenos?size > 0) >
    <div class="card">
        <div class="card-header">
            Vistos buenos
            </div>
        <div class="card-block">
	            <#list documento.vistosBuenos as vb>                	                        
            <strong>Fecha:</strong> ${vb.fecha?string('yyyy-MM-dd hh:mm a:ss')}<br/>
            <strong>Usuario:</strong> ${vb}<br/>
            <hr/>
                </#list>
            </div>
        </div>
    </#if>

<!--
    Adjuntos
    -->
    <#if mode.adjuntos_view >
    <div class="card">
            <#if (documento.adjuntos?size > 0) >
        <div class="card-block">
            <h5 class="m-b">Adjuntos actuales</h5>
                    <#list documento.adjuntos as adj>
                    	<#if adj.activo>
            <hr/>
            <strong>${adj.tipologia.nombre}</strong><br/>
            <em>Subido el ${yyyymmdd.format(adj.cuando)} por ${utilController.nombre(adj.quien)}</em>
            <a href="/documento/adjunto/${adj.id}/eliminar?pin=${instancia.id}" onclick="return confirm('¿Está seguro que desea eliminar el archivo ${adj.tipologia.nombre}?');">Eliminar</a><br/>
            <a href="#" onclick="visualizar('/ofs/viewer?file=/ofs/download/${adj.contenido}')">
                <img src="/ofs/download/tmb/${adj.contenido}" /><br/>
	                            ${adj.original}
                </a><br/>
	                    </#if>
                    </#list>
            </div>
            </#if>
            <#if mode.adjuntos_edit>
        <div class="card-block cus-gray-bg">
            <form action="/documento/adjunto?doc=${documento.id}" method="post" enctype="multipart/form-data">
                <fieldset class="form-group">
                    <label for="destinatario"><strong>Tipología y archivo</strong></label>
                            <#--
                                2017-04-17 jgarcia@controltechcg.com Issue #46 (SICDI-Controltech): Mensaje que indica los formatos permitidos para carga como adjuntos.
                            -->
                    </br>
                    <span style="font-style: italic">Únicamente se permiten cargar archivos en formato PDF, JPG y PNG.</span>
                    <select class="form-control" id="tipologia" name="tipologia">
                                <#if tipologias??>
                        <option value=""></option>
                                    <#list tipologias as tip>
                        <option value="${tip.id}">${tip.nombre}</option>
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
    </#if>

<!--
    Formatos
    -->
    <!-- Visualiza los formatos seleccionados -->
    <#if mode.formatos_view >
    <div class="card">
    		<#if (documento.formatos?size > 0) >
        <div class="card-block">
            <h5 class="m-b">Formatos actuales</h5>
                    <#list documento.formatos as form>
            <hr/>
            <em>Subido el ${yyyymmdd.format(form.cuando)} por ${utilController.nombre(from.quien)}</em><br/>
            <a href="#" onclick="visualizar('/ofs/viewer?file=/ofs/download/${from.contenido}')">
                <img src="/ofs/download/tmb/${from.contenido}" /><br/>
                            ${from.original}
                </a><br/>
                    </#list>
            </div>
            </#if>

            <#if mode.formatos_edit>
        <div class="card-block cus-gray-bg">
            <form action="">
                <fieldset class="form-group">
                    <label>Plantillas disponibles ( .docx )</label>   
                    <div class="card-block">
                            	<#list plantillas as pla>

                        <strong>${pla.nombre}</strong>
                        ||
							            <#if pla.docx4jDocumento?has_content>		                                    
                        <a href="/documento/download/${pla.docx4jDocumento}"> Descargar </a> <br/>
		                                <#else>
                        <label style="color:#d9534f;">
                            SIN PLANTILLA. 
                            </label>
                        <br/>
		                                </#if>
		                        </#list>
                        </div>
                    </fieldset>
                </form>
            </div>
            </#if>
        <br />

<!--Permite subir el formato -->
            <#if mode.formatos_edit>
        <div class="card-block cus-gray-bg">
            <form action="">
                <fieldset class="form-group">
                    <label for="destinatario">Formato</label>

                    <input type="hidden" id="trd2" />
                    <div class="input-group">

                        <div class="input-group-btn">
                            <button class="btn btn-primary" type="button" onclick='showModalSeries();'>
                                Descargar formato
                                </button>
                            </div>
                        </div>
                    </fieldset>
                </form>
            </div>
            </#if>
        <!-- trdModal2 -->
        <div class="modal fade" id="trdFormModal" tabindex="-1" role="dialog" aria-labelledby="trdModalLabel2" aria-hidden="true">
            <div class="modal-dialog modal-lg" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar">
                            <span aria-hidden="true">&times;</span>
                            <span class="sr-only">Cerrar</span>
                            </button>
                        <h4 class="modal-title" id="myModalLabel2">Selección de serie</h4>
                        <div class="seriesList"></div>
                        </div>
                    <div class="modal-body">
                        </div>
                    </div>
                </div>
            </div>


        </div>
    </#if>

<!--
    Sticker
    -->
    <#if mode.sticker_view && documento.sticker??>
    <div class="card">
        <div class="card-header">
            Sticker
            </div>
        <iframe src="/ofs/viewer?file=/ofs/download/${documento.sticker}" width="100%" height="230px"></iframe>
        </div>
    </#if>

    </div>

<!-- 

    Visor de PDF
-->

<div class="modal fade" id="visualizador">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                    <span class="sr-only">Close</span>
                    </button>
                </div>        
            <iframe id="iframeVisor" width="100%" height="600px"></iframe>
          </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
      </div><!-- /.modal -->


<!-- modal seleccionar expediente -->
<div class="modal fade bd-example-modal-lg" id="modal-enviar-expediente" tabindex="-1" role="dialog" aria-labelledby="exampleModalLongTitle" aria-hidden="true">
  <div class="modal-dialog modal-lg" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="exampleModalLongTitle">Seleccionar Expediente</h5>
      </div>
      <div class="modal-body" style="max-height: 650px; overflow: hidden; overflow-y: auto;">
        <#if expedientesValidos?? && expedientesValidos?size != 0>
            <input class="form-control" type="text" id="buscardor-expediente" onkeyup="buscarEnLista()" placeholder="buscar" title="Esctriba el nombre del expediente" style="margin-bottom: 10px;">
            <div class="list-group" id="lista-expedientes">
                <input type="hidden" id="expedienteDestino" name="expedienteDestino" value="" />
                <#list expedientesValidos as pExpediente>
                    <button id="expediente-${(pExpediente.expId)!""}" onclick="seleccionarExpediente(${(pExpediente.expId)!""})"
                            class="list-group-item list-group-item-action flex-column align-items-start expediente-list">
                        <div class="d-flex w-100 justify-content-between">
                          <h5 class="mb-1">${(pExpediente.expNombre)!""}</h5>
                          <small>${(pExpediente.fecCreacion)!""}</small>
                        </div>
                        <p class="mb-1">
                            <#if pExpediente.expDescripcion?length &lt; 255>
                            ${pExpediente.expDescripcion}
                            <#else>
                            ${pExpediente.expDescripcion?substring(0,249)} ...
                            </#if>
                        </p>
                        <small>${(pExpediente.depId.nombre)!""}</small>
                    </button>
                </#list>
            </div>
        <#else>
           <p>No tiene expedientes validos para este documento.</p>
        </#if>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancelar</button>
        <button type="button" id="submit-button"
        class="btn btn-primary" 
        onclick="submitSeleccionarExpediente('${instancia.id}')"
        style="display:none;"
        >
        Enviar a expediente
        </button>
      </div>
    </div>
  </div>
</div>


<script type="text/javascript">
    <!--
    function visualizar(url) {
    var frame = $("#visualizador iframe");
    frame.attr('src', url);
    $('#visualizador').width('100%');
    $('#visualizador .modal-dialog').width('95%');
    h = $(window).height();
    $('#visualizador').height(h + 'px');        
    $('#visualizador').modal('show');
    $('#visualizador iframe').height((h - 125) + 'px');
    }
    -->
    </script>
<script type="text/javascript">
    <!--
    function showModalSeries() {

    $("#trdFormModal .seriesList").empty();                            
    $('#trdFormModal').modal("show");
    $("#myModalLabel2").text("Selección de serie");
    $.getJSON("/trd/series.json", function(series) {
    var divTagList = [];
    $.each(series, function(i, item){

    divTagList.push('<div><a href="#" id="serie2' + item.id + '" class="mark-serie2"');
    divTagList.push(' '); 
    divTagList.push('onclick="showModalSubserie(');  
    divTagList.push(item.id);
    divTagList.push(')"');
    divTagList.push('>' + item.codigo + " - " + item.nombre + '</a></div>');
    });
    $("<div/>", { html: divTagList.join("") }).appendTo("#trdFormModal .seriesList");

    $("#trdFormModal .seriesList .mark-serie");
    });

    };


    function showModalSubserie(e) {

    var id = e;
    var nombre = $(this).text();
    $("#trdFormModal .seriesList").empty();
    $('#trdFormModal').modal("show");
    $("#myModalLabel2").text("Selección de subserie");

    $.getJSON("/trd/subseries.json?serieId=" + id, function(subseries) {
    var subseriesList = [];
    $.each(subseries, function(i, item){
    subseriesList.push('<div><a href="#" id="serie2' + item.id + '" class="mark-serie2">' + item.codigo + " - " + item.nombre + '</a></div>');
    });

    $("<h5/>", { html: nombre }).appendTo("#trdFormModal .seriesList");
    $("<a/>", {html: "Regresar", id:'btnRegresar'}).appendTo("#trdFormModal .seriesList");
    $("<div/>", {html: subseriesList.join("") }).appendTo("#trdFormModal .seriesList");

    $("#btnRegresar").click(showModalSeries);

    $("#trdFormModal .seriesList .mark-serie2").click(function(e) {

    $("#trdFormModal .seriesList").empty(); 
    $("#myModalLabel2").text("Selección de formato");
    var id = $(this).attr("id").substring(6);
    var nombre = $(this).text();

    $("#trd2").val(id);
    $("#trdNombre2").text(nombre);

    $.get('/formato/get-list', {



    subId : id,
    }, function(formatos) {

    var formatosList = [];
    $.each(formatos, function(i, item){

    formatosList.push('<div><a href="/formato/download/' + item.contenido + '">' + item.original + " - " + item.descripcion + '</a></div>');
    formatosList.push('<p id="formContent" hidden>');
    formatosList.push(item.contenido);
    formatosList.push(' </p>');
    });


    $("<a/>", {html: "Regresar", id:'btnRegresar'}).appendTo("#trdFormModal .seriesList");
    $("<div/>", {html: formatosList.join("") }).appendTo("#trdFormModal .seriesList");
    $("#btnRegresar").click(showModalSeries);

    });

    });
    });

    };
    -->
    </script>
<script>

    function cargarHtmlAplantilla() {    	    	
    $("#contenido").val( tinyMCE.get('id_text_area_html').getContent() );
    //CKEDITOR.replace( 'contenido' );
    location.reload();
    }
    tinymce.init(
    { 
    selector:'#id_text_area_html',
    readonly : 1,
    menubar:false,
    statusbar:false,
    toolbar:false
    });
    </script>

<#assign deferredJSDependencias>
    <script src="/js/jstree.min.js"></script>
    <script src="/js/app/gen-arbol.js"></script>

    <script type="text/javascript">
        validarArbol("#arbol_list_dependenciasj",false);
    </script>
    <script type="text/javascript">
        validarArbol("#arbol_list_dependenciasadi",false);
    </script>
    <script type="text/javascript">
        validarArbol("#arbol_list_trd",false);
    </script>
    <script src="/js/app/documento.js"></script>
    <#--
        2018-05-31 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
        feature-162: Separación del selector de observaciones por defecto.
    -->
    <script src="/js/app/documento-observaciones.js"></script>
    <#--
        2018-08-03 samuel.delgado@controltechcg.com Issue #181 (SICDI-Controltech)
        feature-162: js para enviar documento a expediente.
    -->
    <script src="/js/app/enviar-expediente-documento.js"></script>
    
</#assign>
<#assign deferredJS = deferredJS + " " + deferredJSDependencias>
<#include "bandeja-footer.ftl" />
