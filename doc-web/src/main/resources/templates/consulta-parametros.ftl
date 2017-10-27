<#assign pageTitle = "Parámetros de busqueda" />
<#assign deferredJS = "" />
<#assign deferredJSOrigen = "" />
<#import "spring.ftl" as spring />
<#include "header.ftl" />

<div class="container-fluid">
    <h4>Consulta de documentos</h4>

    <div class="container-fluid">
        <form method="GET">

        <!-- 	2017-02-13 jgarcia@controltechcg.com #78: Alinear el formulario de la búsqueda avanzada -->
        <!-- 	2017-02-13 jgarcia@controltechcg.com #142: Reordenamiento de los campos del formulario para que no se crucen entre ellos. -->
        <!-- 	2017-10-27 edison.gonzalez@controltechcg.com #136: Ajuste visual y paginación de los resultados. -->

            <div style="border: solid 2px #CEE3F6; border-radius: 10px; margin-bottom: 10px; padding: 10px;">
                <fieldset>
                    <legend>Parámetros de filtro</legend>

                    <div class="form-group row">
                        <label for="fechaInicio"  class="col-sm-1 col-form-label text-xs-right">Fecha Inicial</label>
                        <div class="form-inline col-sm-2 input-group">
                            <input type="text" id="fechaInicio" name="fechaInicio" class="form-control datepicker" />
                        </div>
                        <label for="fechaFin" class="col-sm-1 col-form-label text-xs-right">Fecha Final</label>
                        <div class="form-inline col-sm-2 input-group">
                            <input type="text" name="fechaFin" id="fechaFin" class="form-control datepicker" />
                        </div>
                        <label for="asignado" class="col-sm-1 col-form-label text-xs-right">Asignado a</label>
                        <div class="col-sm-2">
                            <input type="text" name="asignado" class="form-control" />
                        </div>
                        <label for="asunto" class="col-sm-1 col-form-label text-xs-right">Asunto</label>
                        <div class="col-sm-2">
                            <input type="text" name="asunto" class="form-control" />
                        </div>
                    </div>
                    <div class="form-group row">
                        <label for="radicado" class="col-sm-1 col-form-label text-xs-right">Radicado</label>
                        <div class="col-sm-2">
                            <input type="text" name="radicado" class="form-control" />
                        </div>
                        <label for="destinatario" class="col-sm-1 col-form-label text-xs-right">Destinatario</label>
                        <div class="col-sm-2">
                            <input type="text" name="destinatario" class="form-control" />
                        </div>
                        <label for="clasificacion" class="col-sm-1 col-form-label text-xs-right">Clasificación</label>
                        <div class="col-sm-2">
                            <select class="form-control" id="clasificacion" name="clasificacion">
                                <#if clasificaciones??>
                                    <option value=""></option>
                                    <#list clasificaciones as cla>
                                        <option value="${cla.id}">${cla.nombre}</option>
                                    </#list>
                                </#if>
                            </select>
                        </div>
                    </div>
                    <div class="form-group row">
                        <!--2017-02-14 jgarcia@controltechcg.com Issue #105: Se cambia de posición el campo de búsqueda "Dependencia destino", para que la selección de las 
                                fechas se vea completa. -->
                        <!-- 2017-02-13 jgarcia@controltechcg.com Issue #77 »»»»»»»»»» -->
                        <label for="destinatario" class="col-sm-1 col-form-label text-xs-right">Dependencia destino</label>
                        <input type="hidden" name="dependenciaDestino" id="dependenciaDestino" value="" />
                        <div class="col-sm-11">
                            <div class="input-group">
                                <div class="form-control" id="dependenciaDestinoNombre">Por favor seleccione una dependencia destino...</div>
                                <span class="input-group-btn">
                                    <button class="btn btn-primary" type="button" data-toggle="modal" data-target="#dependenciaDestinoModal">
                                        <span class="hidden-md-down">Seleccionar</span><span class="hidden-lg-up">S</span>
                                    </button>
                                </span>
                            </div>
                        </div>
                    </div>
                    <div class="form-group row">
                        <!--2017-02-14 jgarcia@controltechcg.com Issue #105: Se cambia de posición el campo de búsqueda "Dependencia origen", para que la selección de las 
                                fechas se vea completa. -->
                        <!-- 2017-02-13 jgarcia@controltechcg.com Issue #77 »»»»»»»»»» -->
                        <label for="destinatario" class="col-sm-1 col-form-label text-xs-right">Dependencia origen</label>
                        <input type="hidden" name="dependenciaOrigen" id="dependenciaOrigen" value="" />
                        <div class="col-sm-11">
                            <div class="input-group">
                                <div class="form-control" id="dependenciaOrigenNombre">Por favor seleccione una dependencia origen...</div>
                                <span class="input-group-btn">
                                    <button class="btn btn-primary" type="button" data-toggle="modal" data-target="#dependenciaOrigenModal">
                                        <span class="hidden-md-down">Seleccionar</span><span class="hidden-lg-up">S</span>
                                    </button>
                                </span>
                            </div>
                        </div>
                    </div>
                    <div class="text-center">
                        <button type="submit" class="btn btn-success btn-lg">Buscar</button>
                    </div>
                </fieldset>
            </div>



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
                
                <!-- dependenciaOrigenModal -->            
                <div class="modal fade" id="dependenciaOrigenModal" tabindex="-1" role="dialog" aria-labelledby="dependenciaOrigenModalLabel" aria-hidden="true">
                    <div class="modal-dialog modal-lg" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar">
                                    <span aria-hidden="true">&times;</span>
                                    <span class="sr-only">Cerrar</span>
                                </button>
                                <h4 class="modal-title" id="dependenciaOrigenModalLabel">Selección de dependencia origen</h4>
                            </div>
                        <div class="modal-body">
                        </div>
                    </div>
                </div>
                <div class="modal fade" id="dependenciaOrigenAdicionalModal" tabindex="-1" role="dialog" aria-labelledby="dependenciaOrigenModalLabel" aria-hidden="true">
                    <div class="modal-dialog modal-lg" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar">
                                    <span aria-hidden="true">&times;</span>
                                    <span class="sr-only">Cerrar</span>
                                </button>
                                <h4 class="modal-title" id="dependenciaOrigenModalLabel">Selección de copia dependencia origen</h4>
                            </div>
                            <div class="modal-body">
                            </div>
                        </div>
                    </div>
                </div>
                    
                <#assign deferredJSDepOrigen>
                    <script type="text/javascript">

                    var depPadre = -1;
                    var depStack = new Array();

                    function loadSubDependenciaOrigen(id) {

                            if(depStack.length == 0) {
                                    depStack.push(id);
                            } else if(depStack.length > 0 && depStack[depStack.length - 1] != id) {
                                    depStack.push(id);
                            }
                        var nombre = $(this).attr("nombre");

                        $("#dependenciaOrigenModal .modal-body").empty();
                        $("#dependenciaOrigenModalLabel").text(nombre);

                        $.getJSON("/dependencias/dependencias.json?id=" + id, function(deps) {

                            modalBody = $("#dependenciaOrigenModal .modal-body");

                            $("<a/>", {html: "Regresar", id:'btnRegresar', class:'btn btn-secondary btn-sm'}).appendTo(modalBody);
                            if(id != null && id !== 'undefined') {
                                    $("#btnRegresar").click(function(){

                                            if(depStack.length > 1) {
                                                    // 2017-02-13 jgarcia@controltechcg.com Issue #68
                                                    // Se corrige el manejo del splice() en el arreglo de pila de dependencias.

                                                    depStack.splice(depStack.length - 1, 1);
                                            } else {
                                                    depStack = new Array();
                                            }

                                            if(depStack.length > 0) {
                                                    prev = depStack[depStack.length - 1];

                                                    loadSubDependenciaOrigen(prev);
                                            } else {
                                                    loadDependenciaOrigen();
                                            }
                                    });
                            } else {
                                    $("#btnRegresar").click(loadDependenciaOrigen);
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

                            $("#dependenciaOrigenModal .modal-body .mark-dep").click(function(){
                                    var id = $(this).attr("id").substring(3);
                                    loadSubDependenciaOrigen(id);
                            });
                            $("#dependenciaOrigenModal .modal-body .mark-sel").click(function(e) {                        	                        
                                var id = $(this).attr("id").substring(3);
                                var nombre = $(this).attr("nombre");
                                $("#dependenciaOrigen").val(id);
                                $("#dependenciaOrigenNombre").text(nombre);
                                $("#dependenciaOrigenModal").modal('hide');
                            });
                        });
                    };

                    function loadDependenciaOrigen(e) {

                        $("#dependenciaOrigenModalLabel").text("Dependencias");
                        $("#dependenciaOrigenModal .modal-body").empty();
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
                            modalBody = $("#dependenciaOrigenModal .modal-body");
                            table.appendTo(modalBody);

                            $("#dependenciaOrigenModal .modal-body .mark-dep").click(function(){
                                    var id = $(this).attr("id").substring(3);
                                    loadSubDependenciaOrigen(id);
                            });
                            $("#dependenciaOrigenModal .modal-body .mark-sel").click(function(e) {
                                id = $(this).attr("id").substring(3);
                                nombre = $(this).attr("nombre");
                                $("#dependenciaOrigen").val(id);
                                $("#dependenciaOrigenNombre").text(nombre);
                                $("#dependenciaOrigenModal").modal('hide');
                            });
                        });
                    };
                    $('#dependenciaOrigenModal').on('show.bs.modal', loadDependenciaOrigen);

                    </script>

                    <script type="text/javascript">

                    var depPadre = -1;
                    var depStack = new Array();

                    function loadSubDependenciaOrigenAdicional(id) {
                            if(depStack.length == 0) {
                                    depStack.push(id);
                            } else if(depStack.length > 0 && depStack[depStack.length - 1] != id) {
                                    depStack.push(id);
                            }
                        var nombre = $(this).attr("nombre");

                        $("#dependenciaOrigenAdicionalModal .modal-body").empty();
                        $("#dependenciaOrigenModalLabel").text(nombre);

                        $.getJSON("/dependencias/dependencias.json?id=" + id, function(deps) {

                            modalBody = $("#dependenciaOrigenAdicionalModal .modal-body");

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
                                                    loadSubDependenciaOrigenAdicional(prev);
                                            } else {
                                                    loadDependenciaOrigenAdicional();
                                            }
                                    });
                            } else {
                                    $("#btnRegresar").click(loadDependenciaOrigenAdicional);
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

                            $("#dependenciaOrigenAdicionalModal .modal-body .mark-dep").click(function(){
                                    var id = $(this).attr("id").substring(3);
                                    loadSubDependenciaOrigenAdicional(id);
                            });
                            $("#dependenciaOrigenAdicionalModal .modal-body .mark-sel").click(function(e) {

                                    var idDocumento = $("#idDocumentodependenciaOrigenAdicionalModal").val();
                                var idDependencia = $(this).attr("id").substring(3);                                                       

                                $.ajax("/documento/dependenciAdicionalDocumento/"+idDependencia+"/"+idDocumento)
                                            .done(function() {          	                        		 	
                                                                            window.location.reload(true);
                                                                    })
                                                                    .error( function(){
                                                                            window.location.reload(true);
                                                                    }); 
                                $("#dependenciaOrigenAdicionalModal").modal('hide');	   

                            });
                        });
                    };

                    function loadDependenciaOrigenAdicional(e) {
                        $("#dependenciaOrigenModalLabel").text("Dependencias");
                        $("#dependenciaOrigenAdicionalModal .modal-body").empty();
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
                            modalBody = $("#dependenciaOrigenAdicionalModal .modal-body");
                            table.appendTo(modalBody);

                            $("#dependenciaOrigenAdicionalModal .modal-body .mark-dep").click(function(){
                                    var id = $(this).attr("id").substring(3);
                                    loadSubDependenciaOrigenAdicional(id);
                            });
                            $("#dependenciaOrigenAdicionalModal .modal-body .mark-sel").click(function(e) {                        

                                var idDocumento = $("#idDocumentodependenciaOrigenAdicionalModal").val();
                                var idDependencia = $(this).attr("id").substring(3);                                                       

                                $.ajax("/documento/dependenciAdicionalDocumento/"+idDependencia+"/"+idDocumento)
                                            .done(function() {          	                        		 	
                                                                            window.location.reload(true);
                                                                    })
                                                                    .error( function(){
                                                                            window.location.reload(true);
                                                                    });                            
                                $("#dependenciaOrigenAdicionalModal").modal('hide');                            
                            });
                        });
                    };
                    $('#dependenciaOrigenAdicionalModal').on('show.bs.modal', loadDependenciaOrigenAdicional);

                    </script>


                </#assign>
                <#assign deferredJSOrigen = deferredJSOrigen + " " + deferredJSDepOrigen>
                
                <#assign deferredJSDepDestino>
                    <script type="text/javascript">

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
                                                    // 2017-02-13 jgarcia@controltechcg.com Issue #68
                                                    // Se corrige el manejo del splice() en el arreglo de pila de dependencias.

                                                    depStack.splice(depStack.length - 1, 1);
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

                    </script>

                    <script type="text/javascript">

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

                    </script>


                </#assign>
                <#assign deferredJS = deferredJS + " " + deferredJSDepDestino>

            <!-- 2017-02-13 jgarcia@controltechcg.com Issue #77 «««««««««« -->	
        </form>
    </div>
</div>
<#include "footer.ftl" />
<!-- 	2017-10-27 edison.gonzalez@controltechcg.com #136: Ajuste visual de las fechas de filtro. -->
<script type="text/javascript">
    $(document).ready(function() {
        $('#fechaInicio').prop('readonly', true);
        $('#fechaFin').prop('readonly', true);

        $('#fechaInicio').each(function() {
            var buton = "<div onclick='document.getElementById(\""+ $(this).attr("id")+ "\").value = \"\"' class=\"input-group-addon btn btn-warning\" >Limpiar</div>";
            $(this).parent().append(buton);
        });
        $('#fechaFin').each(function() {
            var buton = "<div onclick='document.getElementById(\""+ $(this).attr("id")+ "\").value = \"\"' class=\"input-group-addon btn btn-warning\" >Limpiar</div>";
            $(this).parent().append(buton);
        });
    });
</script>
