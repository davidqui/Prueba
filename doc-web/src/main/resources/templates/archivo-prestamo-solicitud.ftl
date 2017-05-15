<#assign pageTitle = "Prestamos"/>
<#assign deferredJS = "" />
<#include "bandeja-header.ftl">
<#import "spring.ftl" as spring />

<link href='/css/jquery-ui-1.10.2.css' rel='stylesheet' type='text/css'>
<script src="/jquery/jquery-ui-1.10.2.js"></script>

<div class="container-fluid">
    <h3>Solicitud Prestamo de Expediente</h3>
    <form name="prestamoSolicitud" action="/archivo/solicitud/send" method="POST" id="formPrestamoSolicitud">
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
     <label for="trd">Expediente</label>
     <@spring.bind "prestamoSolicitud.expediente" />
     <input type="hidden" name="${spring.status.expression}" id="expediente" value="${(prestamoSolicitud.expediente.id)!""}"></input>
     <div class="row">
        <div class="col-lg-12">
          <div class="input-group">
             <div class="form-control" id="expNombre">${(prestamoSolicitud.expediente.nombre)!"Por favor seleccione el expediente "}</div>
             <span class="input-group-btn">
               <button class="btn btn-primary" type="button" data-toggle="modal" data-target="#expModal">
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
<!-- expedienteModal -->
<div class="modal fade" id="expModal" tabindex="-1" role="dialog" aria-labelledby="expModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-lg" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar">
           <span aria-hidden="true">&times;</span>
           <span class="sr-only">Cerrar</span>
       </button>
       <h4 class="modal-title" id="myModalLabel">Bienvenido al Archivo del Ejercito Nacional de Colombia</h4>
       <p>Busqueda de expedientes</p>
   </div>
   <div class="modal-body">
      <div class="text-center">
         <a href="#" id="modDoc" onclick='byNameChoice();'>Buscar por Nombre</a>
         <br/>
         <a href="#" id="modExp"onclick='byCodeChoice();'>Buscar por código </a>
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

<!-- byNameModal -->
<div class="modal fade" id="byNameModal" tabindex="-1" role="dialog" aria-labelledby="byNameModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-lg" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar">
           <span aria-hidden="true">&times;</span>
           <span class="sr-only">Cerrar</span>
       </button>
       <h4 class="modal-title" class="myModalLabel">Archivo del Ejercito Nacional de Colombia</h4>
       <p>Búsqueda de expedientes por nombre</p>
   </div>
   <div class="modal-body">
     <div class="input-group">
       <input id="nameTerm" type="text" class="form-control" placeholder="Ingrese un término que coincida parcialmente con el nombre del expediente " aria-describedby="basic-addon2">
       <span class="input-group-addon" onclick='searchByName();'><a href="#" class="btn-sm" type="button">Buscar</a></span>
       <div class="results">
       </div>
   </div>
</br>
</div>
</div>
</div>
</div>
<!-- byCodeModal -->
<div class="modal fade" id="byCodeModal" tabindex="-1" role="dialog" aria-labelledby="byNameModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-lg" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar">
           <span aria-hidden="true">&times;</span>
           <span class="sr-only">Cerrar</span>
       </button>
       <h4 class="modal-title">Archivo del Ejercito Nacional de Colombia</h4>
       <p>Búsqueda de expedientes por código</p>
   </div>
   <div class="modal-body">
     <div class="input-group">
       <input id="codeTerm" type="text" class="form-control" placeholder="Ingrese un término que coincida parcialmente con el código del expediente " aria-describedby="basic-addon2">
       <span class="input-group-addon" onclick='searchByCode();'><a href="#" class="btn-sm" type="button">Buscar</a></span>
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
       <p>Búsqueda de expedientes por fecha de creación</p>
       <p>El sistema devolverá los expedientes creados entre las fechas ingresadas a continuación. </p>
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
  function byNameChoice() {

     $("#byNameModal .list-group").empty();     
     $("#expModal").modal('hide');
     $("#byNameModal").modal('show');


 }
 function byCodeChoice() {

  $("#byCodeModal .list-group").empty();      
  $("#expModal").modal('hide');
  $("#byCodeModal").modal('show');
  
  
}
function byDateChoice() {

  $("#byDateModal .list-group").empty();      
  $("#expModal").modal('hide');
  $("#byDateModal").modal('show');
  
  
}
function searchByName() {


   $.get("/archivo/solicitud/searchByName",{

     term: $('#nameTerm').val(),},

     function(expedientes) {
     
     
     var expedientesList = [];
     
     if (!jQuery.isEmptyObject(expedientes)){

        
        $.each(expedientes, function(i, item){
        
         $( "#info" ).empty();	

          var n=item.nombre;
			
          expedientesList.push('<div class="list-group">');
          expedientesList.push('<a href="#" class="list-group-item"');
          expedientesList.push('onclick="returnSelected(');
          expedientesList.push(item.id);
          expedientesList.push(",'");
          expedientesList.push(item.nombre);
          expedientesList.push("'");
          expedientesList.push(')"');

          expedientesList.push('><p id="expN" hidden>'+item.nombre+'</p><h6 class="list-group-item-heading">Código: '+item.codigo+'</h6>');
          expedientesList.push('<p class="list-group-item-text"> Nombre: ');
          expedientesList.push(item.nombre);
          expedientesList.push('</p></a></div>');
      });
        $("<div/>", { html: expedientesList.join("") }).appendTo("#byNameModal .modal-body");
      }else{
      	
      	
      	expedientesList.push('<div class="list-group">');
      	expedientesList.push('<div id="info" class="alert alert-info" role="alert">No se encoentraron resultados.</div>');
      	expedientesList.push('</div>');
      	$("<div/>", { html: expedientesList.join("") }).appendTo("#byNameModal .modal-body");
      	
      }
    });
}
function searchByCode() {

  if($('#codeTerm').val()==''){

     $("#byCodeModal").modal('hide');
     $( "#dialog" ).dialog();

 }
 
 
 $.get("/archivo/solicitud/searchByCode",{

     term: $('#codeTerm').val(),},

     function(expedientes) {

        var expedientesList = [];
        if (!jQuery.isEmptyObject(expedientes)){
        
          $.each(expedientes, function(i, item){
          
		  
		  $( ".list-group" ).empty();
		  
	      var n=item.nombre;
			

          expedientesList.push('<div class="list-group">');
          expedientesList.push('<a href="#" class="list-group-item"');
          expedientesList.push('onclick="returnSelected2(');
          expedientesList.push(item.id);
          expedientesList.push(",'");
          expedientesList.push(item.nombre);
          expedientesList.push("'");
          expedientesList.push(",'");
          expedientesList.push(item.estado.nombre);
          expedientesList.push("'");
          expedientesList.push(')"');

          expedientesList.push('><p id="expC" hidden>'+item.nombre+'</p><h6 class="list-group-item-heading">Código: '+item.codigo+'</h6>');
          expedientesList.push('<p class="list-group-item-text"> Nombre: ');
          expedientesList.push(item.nombre);
          expedientesList.push('</p> ');
          expedientesList.push('<p id="exes" hidden>'+item.estado.nombre+'</p><p class="list-group-item-text"> Estado: ');
          expedientesList.push(item.estado.nombre);
          expedientesList.push('</p></a><div/>');
      	});
        $("<div/>", { html: expedientesList.join("") }).appendTo("#byCodeModal .modal-body");
        
        }else{
      	
      	  
      	  expedientesList.push('<div class="list-group">');
      	  expedientesList.push('<div id="info" class="alert alert-info" role="alert">No se encontraron resultados.</div>');
      	  expedientesList.push('</div>');
      	  $("<div/>", { html: expedientesList.join("") }).appendTo("#byCodeModal .modal-body");
      	
      }
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



function returnSelected(id,name){

  $('#expediente').val(id);
  $('#expNombre').text(name);
  $('#byNameModal').modal("hide");
}
function returnSelected2(id,name,estado){

  $('#byCodeModal').modal("hide");
  
  if(estado=='Prestado'){
     $( "#prestadoDialog" ).dialog();
 }else{
     $('#expediente').val(id);
     $('#expNombre').text(name);
     $('#byCodeModal').modal("hide");
 }
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
