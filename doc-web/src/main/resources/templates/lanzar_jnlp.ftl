<#assign deferredJS = "" />

<#assign headScripts>
  <script type="text/javascript" src="/js/deployJava.js"></script>
</#assign>

<#if archivoHeader??>	
	<#include "archivo-header.ftl">
<#else>
	<#include "bandeja-header.ftl" />
</#if>

<div class="col-md-8">
    <#if relacionado??>
        <strong>Documento relacionado:</strong> <a target="_blank" href="/documento?pin=${relacionado.instancia.id}">${relacionado.asunto!"&lt;Sin asunto&gt;"}</a>
    </#if>
    <table class="table table-sm">    	
        <#if mode.expediente_view && documento.expediente?? >
            <tr><th>Expediente</th><td>${documento.expediente.nombre}</td></tr>
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


            TRD
        -->
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
                        <button class="btn btn-primary" type="button" data-toggle="modal" data-target="#trdModal">
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
            <#assign deferredJSTrd>
                <script type="text/javascript">
                <!--
                    function subserie(e) {
                                var id = $(this).attr("id").substring(5);
                                var nombre = $(this).text();

                                $("#trdModal .modal-body").empty();
                                $("#myModalLabel").text("Selección de subserie");

                                $.getJSON("/trd/subseries.json?serieId=" + id, function(subseries) {
                                    var subseriesList = [];
                                    $.each(subseries, function(i, item){
                                        subseriesList.push('<div><a href="#" id="serie' + item.id + '" class="mark-serie">' + item.codigo + " - " + item.nombre + '</a></div>');
                                    });

                                    $("<h5/>", { html: nombre }).appendTo("#trdModal .modal-body");
                                    $("<a/>", {html: "Regresar", id:'btnRegresar'}).appendTo("#trdModal .modal-body");
                                    $("<div/>", {html: subseriesList.join("") }).appendTo("#trdModal .modal-body");
                                   
                                   $("#btnRegresar").click(serie);
                                   

                                    
                                   $("#trdModal .modal-body .mark-serie").click(function(e) {
                                        var id = $(this).attr("id").substring(5);
                                        var nombre = $(this).text();
                                        
                                        $("#trd").val(id);
                                        $("#trdNombre").text(nombre);
                                        $("#trdModal").modal('hide');
                                    });
                                });
                    };

                    function serie(e) {
                        $("#trdModal .modal-body").empty();
                        $("#myModalLabel").text("Selección de serie");
                        $.getJSON("/trd/series.json", function(series) {
                            var divTagList = [];
                            $.each(series, function(i, item){
                                divTagList.push('<div><a href="#" id="serie' + item.id + '" class="mark-serie">' + item.codigo + " - " + item.nombre + '</a></div>');
                            });
                            $("<div/>", { html: divTagList.join("") }).appendTo("#trdModal .modal-body");

                            $("#trdModal .modal-body .mark-serie").click(subserie);
                        });
                    };
                    $('#trdModal').on('show.bs.modal', serie);
                -->
                </script>
            </#assign>
            <#assign deferredJS = deferredJS + " " + deferredJSTrd>
        </#if>
		<!--
		
		
			Destinatario texto
		-->
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
                <label for="destinatarioDireccion">Dirección del destinatario</label>
                <@spring.formTextarea "documento.destinatarioDireccion" 'class="form-control"' />
                <div class="error">
                    <@spring.showErrors "<br>"/>
                </div>
            </fieldset>
        </#if>
		<!--
		
		
			Dependencia destino
		-->
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
                        <button class="btn btn-primary" type="button" data-toggle="modal" data-target="#dependenciaDestinoModal">
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
            
            <!-- dependenciaDestinoModal -->            
            <div class="modal fade" id="dependenciaDestinoModal" tabindex="-1" role="dialog" aria-labelledby="dependenciaDestinoModalLabel" aria-hidden="true">
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
                  </div>
                </div>
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
                  </div>
                </div>
              </div>
            </div>
            <#assign deferredJSDepDestino>
                <script type="text/javascript">
                <!--
                var depPadre = -1;
                var depStack = new Array();
                
                function loadSubDependencia(id) {
                	if(depStack.length == 0) {
                		depStack.push(id);
                	} else if(depStack.length > 0 && depStack[depStack.length - 1] != id) {
                		depStack.push(id);
                	}
                    var nombre = $(this).attr("nombre");

                    $("#dependenciaDestinoModal .modal-body").empty();
                    $("#dependenciaDestinoModalLabel").text(nombre);

                    $.getJSON("/dependencias/dependencias.json?id=" + id, function(deps) {

                        modalBody = $("#dependenciaDestinoModal .modal-body");

                        $("<a/>", {html: "Regresar", id:'btnRegresar', class:'btn btn-secondary btn-sm'}).appendTo(modalBody);
                        if(id != null && id !== 'undefined') {
                        	$("#btnRegresar").click(function(){
                        		if(depStack.length > 1) {
                        			depStack.splice(1, depStack.length - 1);
                        		} else {
                        			depStack = new Array();
                        		}
                        		if(depStack.length > 0) {
                        			prev = depStack[depStack.length - 1];
                        			loadSubDependencia(prev);
                        		} else {
                        			loadDependencia();
                        		}
                        	});
                        } else {
                        	$("#btnRegresar").click(loadDependencia);
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

                        $("#dependenciaDestinoModal .modal-body .mark-dep").click(function(){
                        	var id = $(this).attr("id").substring(3);
                        	loadSubDependencia(id);
                        });
                        $("#dependenciaDestinoModal .modal-body .mark-sel").click(function(e) {                        	                        
                            var id = $(this).attr("id").substring(3);
                            var nombre = $(this).attr("nombre");
                            $("#dependenciaDestino").val(id);
                            $("#dependenciaDestinoNombre").text(nombre);
                            $("#dependenciaDestinoModal").modal('hide');
                        });
                    });
                };
                
                function loadDependencia(e) {
                    $("#dependenciaDestinoModalLabel").text("Dependencias");
                    $("#dependenciaDestinoModal .modal-body").empty();
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
                        modalBody = $("#dependenciaDestinoModal .modal-body");
                        table.appendTo(modalBody);

                        $("#dependenciaDestinoModal .modal-body .mark-dep").click(function(){
                        	var id = $(this).attr("id").substring(3);
                        	loadSubDependencia(id);
                        });
                        $("#dependenciaDestinoModal .modal-body .mark-sel").click(function(e) {
                            id = $(this).attr("id").substring(3);
                            nombre = $(this).attr("nombre");
                            $("#dependenciaDestino").val(id);
                            $("#dependenciaDestinoNombre").text(nombre);
                            $("#dependenciaDestinoModal").modal('hide');
                        });
                    });
                };
                $('#dependenciaDestinoModal').on('show.bs.modal', loadDependencia);
                -->
                </script>
                
                <script type="text/javascript">
                <!--
                var depPadre = -1;
                var depStack = new Array();
                
                function loadSubDependenciaAdicional(id) {
                	if(depStack.length == 0) {
                		depStack.push(id);
                	} else if(depStack.length > 0 && depStack[depStack.length - 1] != id) {
                		depStack.push(id);
                	}
                    var nombre = $(this).attr("nombre");

                    $("#dependenciaDestinoAdicionalModal .modal-body").empty();
                    $("#dependenciaDestinoModalLabel").text(nombre);

                    $.getJSON("/dependencias/dependencias.json?id=" + id, function(deps) {

                        modalBody = $("#dependenciaDestinoAdicionalModal .modal-body");

                        $("<a/>", {html: "Regresar", id:'btnRegresar', class:'btn btn-secondary btn-sm'}).appendTo(modalBody);
                        if(id != null && id !== 'undefined') {
                        	$("#btnRegresar").click(function(){
                        		if(depStack.length > 1) {
                        			depStack.splice(1, depStack.length - 1);
                        		} else {
                        			depStack = new Array();
                        		}
                        		if(depStack.length > 0) {
                        			prev = depStack[depStack.length - 1];
                        			loadSubDependenciaAdicional(prev);
                        		} else {
                        			loadDependenciaAdicional();
                        		}
                        	});
                        } else {
                        	$("#btnRegresar").click(loadDependenciaAdicional);
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

                        $("#dependenciaDestinoAdicionalModal .modal-body .mark-dep").click(function(){
                        	var id = $(this).attr("id").substring(3);
                        	loadSubDependenciaAdicional(id);
                        });
                        $("#dependenciaDestinoAdicionalModal .modal-body .mark-sel").click(function(e) {
                        
                        	var idDocumento = $("#idDocumentoDependenciaDestinoAdicionalModal").val();
                            var idDependencia = $(this).attr("id").substring(3);                                                       
							
                            $.ajax("/documento/dependenciAdicionalDocumento/"+idDependencia+"/"+idDocumento)
	                        	.done(function() {          	                        		 	
								  	window.location.reload(true);
								})
								.error( function(){
									window.location.reload(true);
								}); 
                            $("#dependenciaDestinoAdicionalModal").modal('hide');	   
							                                               
                        });
                    });
                };
                
                function loadDependenciaAdicional(e) {
                    $("#dependenciaDestinoModalLabel").text("Dependencias");
                    $("#dependenciaDestinoAdicionalModal .modal-body").empty();
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
                        modalBody = $("#dependenciaDestinoAdicionalModal .modal-body");
                        table.appendTo(modalBody);

                        $("#dependenciaDestinoAdicionalModal .modal-body .mark-dep").click(function(){
                        	var id = $(this).attr("id").substring(3);
                        	loadSubDependenciaAdicional(id);
                        });
                        $("#dependenciaDestinoAdicionalModal .modal-body .mark-sel").click(function(e) {                        
                        
                            var idDocumento = $("#idDocumentoDependenciaDestinoAdicionalModal").val();
                            var idDependencia = $(this).attr("id").substring(3);                                                       
                            
                            $.ajax("/documento/dependenciAdicionalDocumento/"+idDependencia+"/"+idDocumento)
	                        	.done(function() {          	                        		 	
								  	window.location.reload(true);
								})
								.error( function(){
									window.location.reload(true);
								});                            
                            $("#dependenciaDestinoAdicionalModal").modal('hide');                            
                        });
                    });
                };
                $('#dependenciaDestinoAdicionalModal').on('show.bs.modal', loadDependenciaAdicional);
                -->
                </script>
                
                
            </#assign>
            <#assign deferredJS = deferredJS + " " + deferredJSDepDestino>
        </#if>
        <!--


            Asunto
        -->
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
		<!--
		
		
			Remitente texto
		-->
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
                <label for="remitenteDireccion">Dirección del remitente</label>
                <@spring.formTextarea "documento.remitenteDireccion" 'class="form-control"' />
                <div class="error">
                    <@spring.showErrors "<br>"/>
                </div>
            </fieldset>
        </#if>
		<!--
		
		
			Dependencia remitente
		-->
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
        <!--


            Número de oficio y fecha de oficio
        -->
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


            Expediente
        -->
        <#if mode.expediente_edit >
            <fieldset class="form-group">
                <label for="expediente">Expediente</label>
                <@spring.bind "documento.expediente" />
                <select class="form-control" id="expediente" name="${spring.status.expression}">
                    <#if expedientes??>
                        <option value=""></option>
                        <#list expedientes as exp>
                        <#if spring.status.value?? && exp == spring.status.value >
                            <option value="${exp.id}" selected="selected">${exp.nombre}</option>
                        <#else>
                            <option value="${exp.id}">${exp.nombre}</option>
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
        <nav class="navbar navbar-default navbar-fixed-bottom text-xs-center hermes-bottombar">
        	<div class="container">
        		<#if mode.guardar_view >
        			<button type="submit" class="btn btn-success btn-sm">Guardar</button>
        		</#if>
        		<#if documento.consultaEstadoAplicado() == "" >
	        		<#assign transiciones = documento.instancia.transiciones() />
		            <#if transiciones??>
		                <#list transiciones as x>
		                    <a href="${x.replace(instancia)}" class="btn btn-primary btn-sm">${x.nombre}</a>
		                </#list>
		            </#if>
		        </#if>    
	            <#if documento.mostrarBtnReasignacion() == "Y" && usuariologueado?? && documento.elabora.id != usuariologueado.id >
	                <a href="/documento/reasignar?pin=${instancia.id}" class="btn btn-danger btn-sm">
	                    Reasignar
	                </a>
	            </#if>
            </div>
        </nav>
    </form>
	
    <!--
		Observaciones
	-->       	
        <div class="card m-y">                           		        
	            <#if (documento.observaciones)??>
	                <div class="card-block" id="obsDiv">
	                    <h5>Observaciones</h5>
	                    <#list documento.observaciones as obs>
	                        <hr/>
	                        <strong>${utilController.nombre(obs.quien)}</strong>, <em> ${obs.cuando?string('yyyy-MM-dd hh:mm a:ss')}</em>
	                        <p>${obs.texto}??</p>
	                    </#list>
	                </div>
	        	</#if>
            <#if mode.observaciones_edit>
                <div class="card-block cus-gray-bg">
                    <form method="post" id="obsForm" >
                        <fieldset class="form-group">
                            <textarea class="form-control" id="observacion" name="observacion"></textarea>
                        </fieldset>
                        <a href="#" class="btn btn-secondary btn-sm" id="obsButton">Comentar</a>
                    </form>
                </div>
                <#assign deferredJSObs>
                    <script type="text/javascript">
                    <!--
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
	                                    var hr = $("<hr/>");
	                                    hr.appendTo("#obsDiv");
	                                    var strong = $("<strong/>");
	                                    strong.text(data.quien + ", ");
	                                    strong.appendTo("#obsDiv");
	                                    var em = $("<em/>");
	                                    em.text(data.cuando);
	                                    em.appendTo("#obsDiv");
	                                    var p = $("<p/>");
	                                    p.html(data.texto);
	                                    p.appendTo("#obsDiv");
	                                    $("#observacion").val('');
	                                }
	                            });
                            }
                        });
                    -->
                    </script>
                </#assign>
                <#assign deferredJS = deferredJS + deferredJSObs />
            </#if>
        </div>
    
</div>
<div class="col-md-4">
    <div class="card">
        <div class="card-header">
            <a href="/proceso/instancia/detalle?pin=${instancia.id}">Proceso</a>
        </div>
        <div class="card-block">
        	<#if (documento.cuando??) >
            	<strong>Fecha de creación:</strong> ${documento.cuando?string('yyyy-MM-dd hh:mm a')}<br/>
            <#else>
            	<strong>Fecha de creación:<br/>
            </#if>
            <strong>Proceso:</strong> ${instancia.proceso.nombre}<br/>
            <strong>Estado:</strong> ${instancia.estado.nombre}<br/>
            <strong>Usuario asignado:</strong> ${instancia.asignado.nombre}<br/>
            <strong>Enviado por:</strong> ${documento.usuarioUltimaAccion!"&lt;Nadie&gt;"}<br/>
            <strong>Elabora:</strong> ${documento.elabora!"&lt;Nadie&gt;"}<br/>
            <strong>Revisó:</strong> ${documento.aprueba!"&lt;Nadie&gt;"}<br/>
            <strong>Visto bueno:</strong> ${documento.vistoBueno!"&lt;Nadie&gt;"}<br/>
            <strong>Firma:</strong> ${documento.firma!"&lt;Nadie&gt;"}<br/>
        </div>
    </div>
    
    
    
    
    <#if documento.mostrarCopiaDependecia() == "S" >     
    
    	<input type="hidden" id="idDocumentoDependenciaDestinoAdicionalModal" value="${documento.id}" />
    	
    	<div class="card">
	        <div class="card-header">
	            Copia dependencias
	            <#if mode.guardar_view >
		            <button class="btn btn-primary" type="button" data-toggle="modal" data-target="#dependenciaDestinoAdicionalModal">
		                <span class="hidden-md-down"> Adicionar </span><span class="hidden-lg-up">S</span>
		            </button>
		        </#if>    
	        </div>
	        <div class="card-block">
		            <#list documento.documentoDependenciaDestinos as dependenciaDocumentoAdicional> 
		            		<strong>Dependencia:</strong> ${dependenciaDocumentoAdicional.dependencia}<br/>
	                        <strong>Fecha:</strong> ${dependenciaDocumentoAdicional.cuandoInserta?string('yyyy-MM-dd hh:mm a:ss')}<br/>
	                        <strong>Modificado por:</strong> ${dependenciaDocumentoAdicional.elabora}<br/>
	                        <#if mode.guardar_view >
	                        	<a href="#" onclick="eliminarDocumentoDependenciaAdicional( '${dependenciaDocumentoAdicional.id}' );return false;" class="btn btn-sm btn-danger">Eliminar</a><br/>
	                        </#if>	
	                        <hr/>
	                </#list>
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
                            <label for="destinatario">Tipología y archivo</label>
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

	function eliminarDocumentoDependenciaAdicional( id ){
      	
  		$.ajax("/documento/eliminarDependenciAdicionalDocumento/"+id)
        	.done(function() {          	                        		 	
			  	window.location.reload(true);
			})
			.error( function(){
				window.location.reload(true);
			});
										
  	}
      
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

<#include "bandeja-footer.ftl" />
