<#assign pageTitle = "Prestamos"/>
<#assign deferredJS = "" />
<#include "bandeja-header.ftl">
<#import "spring.ftl" as spring />

<link href='/css/jquery-ui-1.10.2.css' rel='stylesheet' type='text/css'>
<script src="/jquery/jquery-ui-1.10.2.js"></script>

<div class="container-fluid">
<h3>Solicitud Prestamo de Documento</h3>
  <form name="prestamoSolicitud" action="/archivo/solicitud/send-doc" method="POST" id="formPrestamoSolicitud">
   <@spring.bind "prestamoSolicitud.id" />
   <input type="hidden" name="${spring.status.expression}" id="${spring.status.expression}" value="${(prestamoSolicitud.id)!""}" />
   
   <fieldset class="form-group">
     <label for="fechaSolicitud">Fecha de solicitud del prestamo</label>
     <@spring.bind "prestamoSolicitud.fechaSolicitud" />
     <input class="form-control" readonly id="${spring.status.expression}" name="${spring.status.expression}" value="<#if prestamoSolicitud.fechaSolicitud??>${yyyymmdd.format(prestamoSolicitud.fechaSolicitud)}</#if>"/>
     <div class="error">
      <@spring.showErrors "<br>"/>
    </div>
  </fieldset>
  
  <fieldset class="form-group">
   <label for="trd">Documento</label>
   <@spring.bind "prestamoSolicitud.documento" />
   <input type="hidden" name="${spring.status.expression}" id="documento" value="${(prestamoSolicitud.documento.id)!""}"></input>
   <div class="row">
    <div class="col-lg-12">
      <div class="input-group">
       <div class="form-control" id="docAsunto">${(prestamoSolicitud.documento.asunto)!"Por favor seleccione el documento "}</div>
       <span class="input-group-btn">
         <button class="btn btn-primary" type="button" data-toggle="modal" data-target="#docModal">
          Seleccionar
        </button>
      </span>
    </div>
  </div>
</div>
<div class="error">
 <@spring.showErrors "<br>"/>
</div>
</fieldset>
<!-- documentoModal -->
<div class="modal fade" id="docModal" tabindex="-1" role="dialog" aria-labelledby="expModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-lg" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar">
         <span aria-hidden="true">&times;</span>
         <span class="sr-only">Cerrar</span>
       </button>
       <h4 class="modal-title" id="myModalLabel">Bienvenido al Archivo del Ejercito Nacional de Colombia</h4>
       <p>Busqueda de documentos</p>
     </div>
     <div class="modal-body">
      <div class="text-center">
       <a href="#" id="modDoc" onclick='byAsuntoChoice();'>Buscar por Asunto</a>
       <br/>
       <a href="#" id="modExp"onclick='byExpedienteChoice();'>Buscar por nombre de expediente </a>
       <br/>
       <a href="#" onclick='byDateChoice();'>Buscar por fecha de creación </a>
     </div>
   </div>
 </div>
</div>
</div>

<fieldset class="form-group">
 <label for="usuarioSolicita" >Usuario que solicita</label>
 <input class="form-control" readonly value="<#if prestamoSolicitud.usuarioSolicita??>${(prestamoSolicitud.usuarioSolicita.nombre)}</#if>"/>
 <@spring.bind "prestamoSolicitud.usuarioSolicita" />
 <input type="hidden" class="form-control" name="${spring.status.expression}" value="${(prestamoSolicitud.usuarioSolicita.id)!""}"/>
 <div class="error">
  <@spring.showErrors "<br>"/>
</div>
</fieldset>

<fieldset class="form-group">
 <label for="fechaDevolucion">Fecha devolución (*)</label>
 <@spring.bind "prestamoSolicitud.fechaDevolucion" />
 <input class="form-control datepicker" id="${spring.status.expression}" name="${spring.status.expression}" value="<#if prestamoSolicitud.fechaDevolucion??>${yyyymmdd.format(prestamoSolicitud.fechaDevolucion)}</#if>"/>
 <div class="error">
   <@spring.showErrors "<br>"/>
 </div>
</fieldset>
<p>
  <button type="submit" class="btn btn-secondary btn-sm">Enviar solicitud</button>
</p>

</div>

<!-- byAsuntoModal -->
<div class="modal fade" id="byAsuntoModal" tabindex="-1" role="dialog" aria-labelledby="byAsuntoModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-lg" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar">
         <span aria-hidden="true">&times;</span>
         <span class="sr-only">Cerrar</span>
       </button>
       <h4 class="modal-title" class="myModalLabel">Archivo del Ejercito Nacional de Colombia</h4>
       <p>Búsqueda de documento por asunto</p>
     </div>
     <div class="modal-body">
       <div class="input-group">
         <input id="nameTerm" type="text" class="form-control" placeholder="Ingrese un término que coincida parcialmente con el asunto del documento " aria-describedby="basic-addon2">
         <span class="input-group-addon" onclick='searchByAsunto();'><a href="#" class="btn-sm" type="button">Buscar</a></span>
         <div class="results">
         </div>
       </div>
     </br>
   </div>
 </div>
</div>
</div>
<!-- byExpedienteModal -->
<div class="modal fade" id="byExpedienteModal" tabindex="-1" role="dialog" aria-labelledby="byExpedienteModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-lg" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar">
         <span aria-hidden="true">&times;</span>
         <span class="sr-only">Cerrar</span>
       </button>
       <h4 class="modal-title">Archivo del Ejercito Nacional de Colombia</h4>
       <p>Búsqueda de documentos por nombre de expediente</p>
     </div>
     <div class="modal-body">
       <div class="input-group">
         <input id="expTerm" type="text" class="form-control" placeholder="Ingrese un término que coincida parcialmente con nombre del documento " aria-describedby="basic-addon2">
         <span class="input-group-addon" onclick='searchByExpediente();'><a href="#" class="btn-sm" type="button">Buscar</a></span>
         <div class="results">
         </div>
       </div>
     </br>
   </div>
 </div>
</div>
</div>
<div id="dialog" title="" style="display: none;">
 <p>Debe ingresar el código del expediente.</p>
</div>
<div id="prestadoDialog" title="" style="display: none;">
 <p>El expediente se encuentra prestado, intente de nuevo más tarde.</p>
</div>

<!-- byDateModal -->
<div class="modal fade" id="byDateModal" tabindex="-1" role="dialog" aria-labelledby="byDateModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-lg" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar">
         <span aria-hidden="true">&times;</span>
         <span class="sr-only">Cerrar</span>
       </button>
       <h4 class="modal-title">Archivo del Ejercito Nacional de Colombia</h4>
       <p>Búsqueda de documentos por fecha de creación</p>
       <p>El sistema devolverá los documentos creados entre las fechas ingresadas a continuación. </p>
     </div>
     <div class="modal-body">
       <fieldset class="form-group">
         <label for="fechaInicial">Fecha inicial</label>
         <input id="startDate" class="form-control datepicker"/>
       </fieldset>
       <fieldset class="form-group">
         <label for="fechaFinal">Fecha fin</label>
         <input id="endDate" class="form-control datepicker"/>
       </fieldset>
     </br>
     <span class="input-group-addon" onclick='searchByDate();'><a href="#" class="btn-sm" type="button">Buscar</a></span>
     <div class="results">
     </div>
   </div>
 </div>
</div>
</div>
<div id="dialog" title="" style="display: none;">
 <p>Debe ingresar el código del expediente.</p>
</div>
<div id="prestadoDialog" title="" style="display: none;">
 <p>El expediente se encuentra prestado, intente de nuevo más tarde.</p>
</div>
<script type="text/javascript">
  var usuariosList;
  function byAsuntoChoice() {
   
   $("#byAsuntoModal .list-group").empty();     
   $("#docModal").modal('hide');
   $("#byAsuntoModal").modal('show');
   
   
 }
 function byExpedienteChoice() {
   
  $("#byExpedienteModal .list-group").empty();      
  $("#docModal").modal('hide');
  $("#byExpedienteModal").modal('show');
  
  
}
function byDateChoice() {
  
  $("#byDateModal .list-group").empty();      
  $("#expModal").modal('hide');
  $("#byDateModal").modal('show');
  
  
}
function searchByAsunto() {
 
 
 $.get("/archivo/solicitud/searchByAsunto",{
  
   term: $('#nameTerm').val(),},
   
   function(documentos) {
     
    var documentosList = [];
    $.each(documentos, function(i, item){
     
           
      documentosList.push('<div class="list-group">');
      documentosList.push('<a href="#" class="list-group-item"');
      documentosList.push('onclick="returnSelected(');
      documentosList.push("'");
      documentosList.push(item.id);
      documentosList.push("','");
      documentosList.push(item.asunto);
      documentosList.push("'");
      documentosList.push(')"');
      documentosList.push('<p class="list-group-item-text"><h6 class="list-group-item-heading"> Asunto: ');
      documentosList.push(item.asunto);
      documentosList.push('</h6><p hidden id="expN">'+item.asunto+'</p>Expediente: '+item.expediente.nombre);
      
      documentosList.push('</p></a></div>');
    });
    $("<div/>", { html: documentosList.join("") }).appendTo("#byAsuntoModal .modal-body");
  });
}
function searchByExpediente() {
  
  
$.get("/archivo/solicitud/searchByExpediente",{
  
   term: $('#expTerm').val(),},
   
   function(documentos) {
     
    var documentosList = [];
    $.each(documentos, function(i, item){
     
      var n=item.nombre;
      
      
      documentosList.push('<div class="list-group">');
      documentosList.push('<a href="#" class="list-group-item"');
      documentosList.push('onclick="returnSelected2(');
      documentosList.push("'");
      documentosList.push(item.id);
      documentosList.push("','");
      documentosList.push(item.asunto);
      documentosList.push("'");
      documentosList.push(')"');
      documentosList.push('<p class="list-group-item-text"><h6 class="list-group-item-heading"> Asunto: ');
      documentosList.push(item.asunto);
      documentosList.push('</h6><p hidden id="expN">'+item.asunto+'</p>Expediente: '+item.expediente.nombre);
      
      documentosList.push('</p></a></div>');
    });
    $("<div/>", { html: documentosList.join("") }).appendTo("#byExpedienteModal .modal-body");
  });
}
function searchByDate() {

  var d1 = new Date();
  d1=$('#startDate').val();
  var firstDate=d1.toString();
  
  var d2 = new Date();
  var d2 = $('#endDate').val();
  var secondDate = d2.toString();
  
  
  $.get("/archivo/solicitud/searchByDate",{

    
   firstDate: firstDate,
   secondDate: secondDate,},
   
   function(expedientes) {
   
   
     
    var expedientesList = [];
    $.each(expedientes, function( key, value ){
    
    obj = JSON.parse(text);
    
      expedientesList.push('<div class="list-group">');
      expedientesList.push('<a href="#" class="list-group-item"');
      expedientesList.push('onclick="returnSelected3(');
      expedientesList.push(expedientes.id);
      expedientesList.push(",'");
      expedientesList.push(expedientes.nombre);
      expedientesList.push("'");
      expedientesList.push(')"');
      
      expedientesList.push('><p id="expN">'+expedientes.nombre+'</p><h6 class="list-group-item-heading">Código: '+expedientes.codigo+'</h6>');
      expedientesList.push('<p class="list-group-item-text"> Nombre: ');
      expedientesList.push(expedientes.nombre);
      expedientesList.push('</p></a></div>');
    });
    $("<div/>", { html: expedientesList.join("") }).appendTo("#byDateModal .modal-body");
  });
}



function returnSelected(id,asunto){
  
  $('#documento').val(id);
  $('#docAsunto').text(asunto);
  $('#byAsuntoModal').modal("hide");
}
function returnSelected2(id,asunto){
 
  
  
  
   $('#documento').val(id);
   $('#docAsunto').text(asunto);
   $('#byExpedienteModal').modal("hide");

}
function returnSelected3(id,name,estado){
  
  $('#byDateModal').modal("hide");
  
  if(estado=='Prestado'){
    $( "#prestadoDialog" ).dialog();
  }else{
    $('#expediente').val(id);
    $('#expNombre').text(name);
    $('#byDateModal').modal("hide");
  }
}



</script>

<#include "bandeja-footer.ftl">
