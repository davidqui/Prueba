<#assign pageTitle = "Informes" />
<#include "gen-macros.ftl">
<#include "util-macros.ftl" />
<#if templatePrefix??>
    <#include templatePrefix + "-header.ftl">
<#else>
    <#include "header.ftl">
</#if>

<div class="container-fluid">
    <h1 class="cus-h1-page-title">Reportes</h1>
    
    <br />
    
    <form action="/admin/informes/init" method="GET" id="forinfo_list">
    
    
    <fieldset class="form-group">
	    <label for="seleccion_tipo_reporte">Seleccione el tipo de reporte a general</label>    		                   
	    <select id="seleccion_tipo_reporte" class="form-control" name="seleccion_tipo_reporte">	    	
	    	<option value="" ></option>        
	    	
	    	<#if seleccionTipoReporte == 'REPORTE_POR_FECHAS' >
	       		<option value="REPORTE_POR_FECHAS" selected="selected">Reporte por fechas</option>
	       	<#else>
                <option value="REPORTE_POR_FECHAS">Reporte por fechas</option>
            </#if>
            
            <#if seleccionTipoReporte == 'REPORTE_POR_CLASIFICACION_DEL_DOCUMENTO' >
	       		<option value="REPORTE_POR_CLASIFICACION_DEL_DOCUMENTO" selected="selected">Reporte por clasificación del documento</option>
	       	<#else>
                <option value="REPORTE_POR_CLASIFICACION_DEL_DOCUMENTO">Reporte por clasificación del documento</option>
            </#if>
            
            <#if seleccionTipoReporte == 'REPORTE_POR_ASUNTO' >
	       		<option value="REPORTE_POR_ASUNTO" selected="selected">Reporte por asunto</option>
	       	<#else>
                <option value="REPORTE_POR_ASUNTO">Reporte por asunto</option>
            </#if>
	    	
	       	<#if seleccionTipoReporte == 'REPORTE_POR_USUARIO' >
	       		<option value="REPORTE_POR_USUARIO" selected="selected">Reporte por usuario</option>
	       	<#else>
                <option value="REPORTE_POR_USUARIO">Reporte por usuario</option>
            </#if>
            
            <#if seleccionTipoReporte == 'REPORTE_POR_DESTINATARIO' >
	       		<option value="REPORTE_POR_DESTINATARIO" selected="selected">Reporte por destinatario</option>
	       	<#else>
                <option value="REPORTE_POR_DESTINATARIO">Reporte por destinatario</option>
            </#if>	   
	       	
	    </select>
    </fieldset>	
    <fieldset class="form-group" id="fsfinicio">
	    <table width="100%">
			<tr>
	        	<td>
	        		<fieldset class="form-group">
				        <label for="finicio">Fecha inicio</label>
				        <input type="text" class="form-control datepicker" id="finicio" name="finicio" value="${finicio}"/>        
				    </fieldset>
	        	</td>
	        	<td>
	        		<fieldset class="form-group">
				        <label for="ffin">Fecha fin</label>
				        <input type="text" class="form-control datepicker" id="ffin" name="ffin" value="${ffin}"/>        
				    </fieldset>
	        	<td>        		
	       </tr> 
	   </table>   
   </fieldset>	
   <fieldset class="form-group" id="fsusuario">
        <label for="usuario">Usuario</label>
        <input type="text" class="form-control" id="usuario" name="usuario" value="${usuario}" />      
    </fieldset>
    <fieldset class="form-group" id="fsasunto">
        <label for="asunto">Asunto</label>
        <input type="text" class="form-control" id="asunto" name="asunto" value="${asunto}"/>      
    </fieldset>
    <fieldset class="form-group" id="fsnivelclasifica">
        <label for="nivelclasifica">Nivel de clasificación</label>
        <select class="form-control" id="nivelclasifica" name="nivelclasifica">
            <#if clasificaciones_informe??>
                <option value=""></option>
                <#list clasificaciones_informe as cla>
                	<#if '${cla.id}' == '${nivelclasifica}'>
	                	<option selected="selected" value="${cla.id}">${cla.nombre}</option>
	                <#else>
	                	<option value="${cla.id}">${cla.nombre}</option>	
	        		</#if>        	
                </#list>
            </#if>
        </select>
    </fieldset>
    <fieldset class="form-group" id="fsdestinatario">
        <label for="asunto">Destinatario</label>
        <input type="text" class="form-control" id="destinatario" name="destinatario" value="${destinatario}"/>      
    </fieldset>
    
    
    
    <button type="submit" name="btn_consultar_datos" class="btn btn-success btn-sm">Consultar datos</button></td>   
  	
  	
  	
    <#if !documentos?? || documentos?size == 0 >
    	<br /><br />
	  <div class="jumbotron">
	    <p class="lead">No se encontraron documentos para los filtros de búsqueda seleccionados</p>
	  </div>
	<#else>
	  <br /><br />	  	  
	  
      <#list documentos as x>      	
      	<div class="card">
      		<div class="card-block">
      			<div class="container-fluid">
      				<div class="row">
      					<div class="col-sm-<#if (x.radicado)??>7<#else>11</#if>">
      						<strong><a target="_blank" href="/proceso/instancia?pin=${x.instancia.id}">${(x.asunto)!"&lt;Sin asunto&gt;"}</a></strong>
      					</div>
		      			<#if (x.radicado)??>
	      					<div class="col-sm-4">
				      			<div>
				      				<strong>Radicado: </strong>${x.radicado}
				      			</div>
		    				</div>
      			  		</#if>      			  		
					</div>
      				<div class="row">
      					<div class="col-sm-4">
      						<strong>Fecha de creación:</strong>&nbsp;${x.cuando?string('yyyy-MM-dd hh:mm a')}<#if (x.plazo)?? > <strong>Plazo:&nbsp;</strong><span class="label label-${x.semaforo}">${x.plazo?string('yyyy-MM-dd')}</span></#if> 
      					</div>
      					<div class="col-sm-4">
      						<#if (x.instancia.asignado)??><strong>Asignado a: </strong>${(x.instancia.asignado)!"&lt;No asignado&gt;"}</#if>
      					</div>
      					<div class="col-sm-4">
      						<strong>Asignado por: </strong><#if (x.usuarioUltimaAccion)?? > ${x.usuarioUltimaAccion} </#if>
      					</div>	            		
					</div>
				</div>
      		</div>
      	</div>
      </#list>
      <button type="submit" name="btn_generar_reporte" class="btn btn-success btn-sm">Generar Reporte</button></td>
	</#if>
</div>

<script>

    $(document).ready(function() {
	$("#seleccion_tipo_reporte").change(function() {
		$(this).find("option:selected").each(function() {
		
			$("#fsfinicio").hide();
			$("#ffin").hide();
			$("#fsusuario").hide();
			$("#fsasunto").hide();
			$("#fsnivelclasifica").hide();
			$("#fsdestinatario").hide();
			
			var valor = $(this).attr("value");	
					
			if ( valor == "REPORTE_POR_FECHAS" ) {
				$("#fsfinicio").show();
				$("#ffin").show();
				$("#fsusuario").show();				
			} else if( valor == "REPORTE_POR_ASUNTO" ){
				$("#fsasunto").show();
			}else if( valor == "REPORTE_POR_CLASIFICACION_DEL_DOCUMENTO" ){
				$("#fsnivelclasifica").show();
			}else if( valor == "REPORTE_POR_USUARIO" ){
				$("#fsusuario").show();							
			}else if( valor == "REPORTE_POR_DESTINATARIO" ){
				$("#fsdestinatario").show	();						
			}				
		});		
		
	}).change();
});
</script>

<#include "footer.ftl">
