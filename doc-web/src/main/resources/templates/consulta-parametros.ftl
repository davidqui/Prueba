<#assign pageTitle = "Parámetros de busqueda" />

<#assign deferredJS = "" />

<#import "spring.ftl" as spring />
<#include "header.ftl" />
<#include "consulta-parametros-util.ftl"/>

<div class="container-fluid">
    <h4>Consulta de documentos</h4>
<div class="container-fluid">
<form method="POST" action="/consulta/parametros">

    <!--2017-02-13 jgarcia@controltechcg.com #78: Alinear el formulario de la búsqueda avanzada -->
    <!--2017-02-13 jgarcia@controltechcg.com #142: Reordenamiento de los campos del formulario para que no se crucen entre ellos. -->
    <!--2017-10-27 edison.gonzalez@controltechcg.com #136: Ajuste visual y paginación de los resultados. -->

    <div style="border-radius: 10px; margin-bottom: 10px; padding: 10px;">
        <fieldset>
            <legend>Parámetros de filtro</legend>

            <div class="form-group row">
                <label for="fechaInicio"  class="col-sm-1 col-form-label text-xs-right">Fecha Inicial</label>
                <div class="form-inline col-sm-2 input-group">
                    <input type="text" id="fechaInicio" name="fechaInicio" class="form-control datepicker" value="${fechaInicio}"/>
                </div>
                <label for="fechaFin" class="col-sm-1 col-form-label text-xs-right">Fecha Final</label>
                <div class="form-inline col-sm-2 input-group">
                    <input type="text" name="fechaFin" id="fechaFin" class="form-control datepicker" value="${fechaFin}"/>
                </div>
                <label for="asignado" class="col-sm-1 col-form-label text-xs-right">Enviado por</label>
                <div class="col-sm-2">
                    <input type="text" name="asignado" class="form-control" value="${asignado}"/>
                </div>
                <label for="asunto" class="col-sm-1 col-form-label text-xs-right">Asunto</label>
                <div class="col-sm-2">
                    <input type="text" name="asunto" class="form-control" value="${asunto}"/>
                </div>
            </div>
            <div class="form-group row">
                <label for="radicado" class="col-sm-1 col-form-label text-xs-right">Radicado</label>
                <div class="col-sm-2">
                    <input type="text" name="radicado" class="form-control" value="${radicado}"/>
                </div>
                <label for="destinatario" class="col-sm-1 col-form-label text-xs-right">Destinatario</label>
                <div class="col-sm-2">
                    <input type="text" name="destinatario" class="form-control" value="${destinatario}"/>
                </div>
                <label for="clasificacion" class="col-sm-1 col-form-label text-xs-right">Clasificación</label>
                <input type="hidden" name="clasificacion" id="clasificacion" value="${clasificacion}"/>
                <input type="hidden" name="clasificacionNombre" id="clasificacionNombre" value="${clasificacionNombre}"/>
                <div class="col-sm-2">
                    <select class="form-control" id="clasificacionSelect" name="clasificacionSelect">
                        <#if clasificaciones??>
                            <option value=""></option>
                            <#list clasificaciones as cla>
                                <#if clasificacion?has_content && clasificacion == cla.id>
                                    <option value="${clasificacion}" selected="selected">${clasificacionNombre}</option>
                                <#else> 
                                    <option value="${cla.id}">${cla.nombre}</option>
                                </#if>
                            </#list>
                        </#if>
                    </select>
                </div>
            </div>
            <div class="form-group row">
                <label for="origen" class="col-sm-1 col-form-label text-xs-right">Dependencia origen</label>
                <input type="hidden" name="dependenciaOrigen" id="dependenciaOrigen" value="${dependenciaOrigen}"/>
                <input type="hidden" name="dependenciaOrigenDescripcion" id="dependenciaOrigenDescripcion" value="${dependenciaOrigenDescripcion}"/>
                <div class="col-sm-11">
                    <div class="input-group">
                        <div class="form-control" id="dependenciaOrigenNombre">Por favor seleccione una dependencia origen...</div>
                        <span class="input-group-btn">
                            <button class="btn btn-primary" type="button" data-toggle="modal" data-target="#dependenciaOrigenModal">
                                <span class="hidden-md-down">Seleccionar</span><span class="hidden-lg-up">S</span>
                            </button>
                            <button class="btn" type="button" id="dependenciaOrigenLimpiar">
                                <span>Limpiar</span>
                            </button>
                        </span>
                    </div>
                </div>
            </div>
            <div class="form-group row">
                <!--2017-02-14 jgarcia@controltechcg.com Issue #105: Se cambia de posición el campo de búsqueda "Dependencia destino", para que la selección de las 
                        fechas se vea completa. -->
                <!-- 2017-02-13 jgarcia@controltechcg.com Issue #77 »»»»»»»»»» -->
                <label for="destinatario" class="col-sm-1 col-form-label text-xs-right">Dependencia destino</label>
                <input type="hidden" name="dependenciaDestino" id="dependenciaDestino" value="${dependenciaDestino}"/>
                <input type="hidden" name="dependenciaDestinoDescripcion" id="dependenciaDestinoDescripcion" value="${dependenciaDestinoDescripcion}"/>
                <div class="col-sm-11">
                    <div class="input-group">
                        <div class="form-control" id="dependenciaDestinoNombre">Por favor seleccione una dependencia destino...</div>
                        <span class="input-group-btn">
                            <button class="btn btn-primary" type="button" data-toggle="modal" data-target="#dependenciaDestinoModal">
                                <span class="hidden-md-down">Seleccionar</span>
                            </button>
                            <button class="btn" type="button" id="dependenciaDestinoLimpiar" >
                                <span >Limpiar</span>
                            </button>
                                
                            </div>
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
    </div>
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
                        $("#dependenciaDestinoDescripcion").val(nombre);
                        $("#dependenciaDestinoModal").modal('hide');
                    });
                });
            };
                
            function loadDependencia(e) {
                $("#dependenciaDestinoModalLabel").text("Dependencias");
                $("#dependenciaDestinoModal .modal-body").empty();
                    
                $.getJSON("/dependencias/dependencias.json", function(deps) {
                    console.log('inicio loadDependencia javascript 1 getJSON');
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
                        $("#dependenciaDestinoDescripcion").val(nombre);
                        $("#dependenciaDestinoModal").modal('hide');
                    });
                });
            };
            $('#dependenciaDestinoModal').on('show.bs.modal', loadDependencia);    
        </script>
                
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

                    $("#dependenciaOrigenModal .modal-body .mark-dep").click(function(){
                        var id = $(this).attr("id").substring(3);
                        loadSubDependencia(id);
                    });
                    
                    $("#dependenciaOrigenModal .modal-body .mark-sel").click(function(e) {
                        var id = $(this).attr("id").substring(3);
                        var nombre = $(this).attr("nombre");
                        $("#dependenciaOrigen").val(id);
                        $("#dependenciaOrigenNombre").text(nombre);
                        $("#dependenciaOrigenDescripcion").val(nombre);
                        $("#dependenciaOrigenModal").modal('hide');
                    });
                });
            };
                
            function loadDependencia(e) {
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
                        loadSubDependencia(id);
                    });
                        
                    $("#dependenciaOrigenModal .modal-body .mark-sel").click(function(e) {
                        id = $(this).attr("id").substring(3);
                        nombre = $(this).attr("nombre");
                        $("#dependenciaOrigen").val(id);
                        $("#dependenciaOrigenNombre").text(nombre);
                        $("#dependenciaOrigenDescripcion").val(nombre);
                        $("#dependenciaOrigenModal").modal('hide');
                    });
                });
            };
            $('#dependenciaOrigenModal').on('show.bs.modal', loadDependencia);    
        </script>
    </#assign>
    <#assign deferredJS = deferredJS + " " + deferredJSDepDestino>
	
	<!-- 2017-02-13 jgarcia@controltechcg.com Issue #77 «««««««««« -->	
    </form>
  
    <br><br>
</div>
    ${dependenciaDestino}
<#include "consulta.ftl" />

</div>
<script type="text/javascript">
    var depOri = "${dependenciaOrigenDescripcion}";
    if(depOri == null || depOri.length == 0){
        $("#dependenciaOrigenNombre").text("Por favor seleccione una dependencia origen...");
    }else{
        $("#dependenciaOrigenNombre").text(depOri);
    }
    
    var depDes = "${dependenciaDestinoDescripcion}";
    if(depDes == null || depDes.length == 0){
        $("#dependenciaDestinoNombre").text("Por favor seleccione una dependencia destino...");
    }else{
        $("#dependenciaDestinoNombre").text(depDes);
    }
        
    
    $('#fechaInicio').prop('readonly', true);
    $('#fechaFin').prop('readonly', true);

    $('#fechaInicio').each(function() {
        var buton = "<div onclick='document.getElementById(\""+ $(this).attr("id")+ "\").value = \"\"' class=\"input-group-addon btn\" >Limpiar</div>";
        $(this).parent().append(buton);
    });
    $('#fechaFin').each(function() {
        var buton = "<div onclick='document.getElementById(\""+ $(this).attr("id")+ "\").value = \"\"' class=\"input-group-addon btn\" >Limpiar</div>";
        $(this).parent().append(buton);
    }); 
        
    $('#clasificacionSelect').change(function () {
        $("#clasificacion").val($('#clasificacionSelect').val());
        $("#clasificacionNombre").val($("#clasificacionSelect option:selected").text());
    });
        
    $('#dependenciaOrigenLimpiar').on('click', function() {
        $("#dependenciaOrigen").val("");
        $("#dependenciaOrigenNombre").text("Por favor seleccione una dependencia origen...");
        $("#dependenciaOrigenDescripcion").val("");
    });
        
    $('#dependenciaDestinoLimpiar').on('click', function() {
      $("#dependenciaDestino").val("");
      $("#dependenciaDestinoNombre").text("Por favor seleccione una dependencia destino...");
      $("#dependenciaDestinoDescripcion").val("");   
    });
</script>
<#include "footer.ftl" />