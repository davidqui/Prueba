<#assign pageTitle = "Prestamo de expediente"/>
<#assign deferredJS = "" />
<#include "util-macros.ftl" />
<#include "archivo-header.ftl">
<#import "spring.ftl" as spring />

<link href='/css/jquery-ui-1.10.2.css' rel='stylesheet' type='text/css'>
<script src="/jquery/jquery-ui-1.10.2.js"></script>

<div class="container-fluid">
<form name="prestamo" action="/prestamo/save-prestamo?eid=${expediente.id}" method="POST" id="formprestamo">
	<@spring.bind "prestamo.id" />
	<input type="hidden" name="${spring.status.expression}" id="${spring.status.expression}" value="${(prestamo.id)!""}" />
	<p hidden id="eid"></p>
	<fieldset class="form-group">
       <label for="prestamo.codigo">Codigo prestamo (*)</label>
       <@spring.bind "prestamo.codigo" />
       <input class="form-control" name="${spring.status.expression}" value="${(prestamo.codigo)!""}"/>
       <div class="error">
            <@spring.showErrors "<br>"/>
       </div>
    </fieldset>
	<fieldset class="form-group">
	   <label for="codigoExpediente">Código del expediente</label>
       <input type="text" class="form-control" value="${expediente.codigo}" readonly/>
    </fieldset>
    <fieldset class="form-group">
    	<label for="funcionarioPresta" >Funcionario que presta el expediente</label>
    	<input class="form-control" readonly value="<#if prestamo.funcionarioPresta??>${(prestamo.funcionarioPresta.nombre)}</#if>"/>
    	<@spring.bind "prestamo.funcionarioPresta" />
    	<input type="hidden" class="form-control" name="${spring.status.expression}" value="${(prestamo.funcionarioPresta.id)!""}"/>
    	<div class="error">
            <@spring.showErrors "<br>"/>
       </div>
    </fieldset>
    <#if prestamo.dependencia??>
    <fieldset class="form-group">
    	<label for="dependencia" >Dependencia del usuario solicitante (*)</label>
    	<input class="form-control" readonly value="<#if prestamo.dependencia??>${(prestamo.dependencia.nombre)}</#if>"/>
    	<@spring.bind "prestamo.dependencia" />
    	<input type="hidden" class="form-control" name="${spring.status.expression}" value="${(prestamo.dependencia.id)!""}"/>
    	<div class="error">
            <@spring.showErrors "<br>"/>
       </div>
    </fieldset>
    <#else>
    <fieldset class="form-group">
       <label for="dependencia">Dependencia del usuario solicitante (*)</label>
       <@spring.bind "prestamo.dependencia" />
          <select class="form-control" id="dependencia" name="${spring.status.expression}" onchange="myFunction()">
           <#if dependencias??>
                <option value=""></option>
                <#list dependencias as dep>
               		<option id=depId" value="${dep.id}">${dep.nombre}</option>
                </#list>
                
            </#if>
            
            <#if dependencias??>
            	<#if dependencia??>
			        <option value="${dependencia.id}" selected="selected">${dependencia.nombre}</option>
			    </#if>
			<#else>
		            <option value=""></option>
                	<#list dependencias as dep>
               			<option id=depId" value="${dep.id}">${dep.nombre}</option>
                	</#list>
		    </#if>
        </select>
        <div class="error">
          <@spring.showErrors "<br>"/>
        </div>
    </fieldset>
    </#if>
    <#if prestamo.usuarioSolicita??>
    <fieldset class="form-group">
    	<label for="usuarioSolicita" >Usuario que solicita</label>
    	<input class="form-control" readonly value="<#if prestamo.usuarioSolicita??>${(prestamo.usuarioSolicita.nombre)}</#if>"/>
    	<@spring.bind "prestamo.usuarioSolicita" />
    	<input type="hidden" class="form-control" name="${spring.status.expression}" value="${(prestamo.usuarioSolicita.id)!""}"/>
    	<div class="error">
            <@spring.showErrors "<br>"/>
       </div>
    </fieldset>
    <#else>
    <fieldset class="form-group">
       <label for="usuarioSolicita">Usuario solicitante (*)</label>
       <input type="text" class="form-control" name="${spring.status.expression}" id="usuarioSolicita" value= "<#if prestamo.usuarioSolicita??>${prestamo.usuarioSolicita.nombre}</#if>"></input>
       <@spring.bind "prestamo.usuarioSolicita" />
       <input type="hidden" class="form-control" id="usuId" name="${spring.status.expression}" value="${(prestamo.usuarioSolicita.id)!""}"/>
       <p hidden id="idDep"></p>
       <input type="hidden" name="${spring.status.expression}" value="${spring.status.expression}" id="usuId">
       <small class="text-muted">El usuario se autocompletará, digite mínimo las 3 primeras letras del usuario para ver las coincidencias.</small>
       <div class="error">
          <@spring.showErrors "<br>"/>
       </div>
    </fieldset>
    </#if>
    <fieldset class="form-group">
       <label for="fechaPrestamo">Fecha del prestamo</label>
       <@spring.bind "prestamo.fechaPrestamo" />
       <input class="form-control" readonly id="${spring.status.expression}" name="${spring.status.expression}" value="<#if prestamo.fechaPrestamo??>${yyyymmdd.format(prestamo.fechaPrestamo)}</#if>"/>
       <div class="error">
     	<@spring.showErrors "<br>"/>
       </div>
    </fieldset>
    <fieldset class="form-group">
       <label for="fechaDevolucion">Fecha de devolución(*)</label>
       <@spring.bind "prestamo.fechaDevolucion" />
       <input class="form-control datepicker" id="${spring.status.expression}" name="${spring.status.expression}" value="<#if prestamo.fechaDevolucion??>${yyyymmdd.format(prestamo.fechaDevolucion)}</#if>"/>
       <div class="error">
       <@spring.showErrors "<br>"/>
       </div>
    </fieldset>
    <p>
      <button type="submit" class="btn btn-secondary btn-sm">Enviar expediente</button>
    </p>
	
</div>
<script type="text/javascript">
	var usuariosList;
	function myFunction() {
    	var x = document.getElementById("dependencia").value;
    	document.getElementById("idDep").innerHTML = x;
	}
	
	
	$("#usuarioSolicita").autocomplete({
		minLength:3,
		source: function(request, response){
			
			$.get('/prestamo/usuarios?depId='+$("#idDep").text(), request,
				function(listUsuarios){
				 
				 usuariosList = listUsuarios;
				 response($.map(listUsuarios, function(item) {
				 
				 $('#usuId').val(item.id);
				 	
                    return {
                        label: item.nombre ,
                        value: item.nombre
                        
                    }
                }));
				
			});	
		},
      	change: function( event, ui ) {
      	}
     });	
     
</script>

<#include "bandeja-footer.ftl">
