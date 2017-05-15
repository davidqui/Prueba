<#assign pageTitle = "Procesos">
<#assign activePill = "proceso">
<#include "header.ftl">
<div class="container-fluid">
<div class="jumbotron cus-juego-jumbotron">
 <#if flashMsg??>
	<div class="alert alert-warning">
   		${flashMsg}
	</div>
</#if>
 <h6 class="juego-titles">Juego Capacitación</h6>
   <div class="row"> 	
		<div class="col-md-6">
			<div class="panel-default">
  				<span class="juego-titles">Nivel:&nbsp;</span>${nivel.nombre}
  					<table class="table">
    				 	<#list puntuacion as i>
        					<tr>
        					  <#if i==numPregunta>
          						<td style="background-color:#94ce18; color:#fff;">${i}</td>
          					  	<td style="background-color:#94ce18; color:#fff;">${i*10}<span>&nbsp;Pts</span></td>
          					  <#else>
          					    <td><span class="juego-titles">${i}</span></td>
          					  	<td> ${i*10}<span class="juego-titles">&nbsp;Pts</span></td>
          					  </#if>
         					</tr>
      					</#list>
  					</table>
   			</div>
   		</div>
   		<div class="col-md-6">
    		<div>
    			<p class="juego-titles">Niveles</p>
    	  			<#list niveles as n>
    				 	<#if n.nombre==nivel.nombre>
    				 	  <span style="background-color:#5bc0de; color:#f;">
          						${n.nombre}&nbsp;|
         				  </span>
         				<#else>
         				  <span>
          					${n.nombre}&nbsp;|
         				  </span>
         				</#if>
      			    </#list>
  			</div>
  			<div class="referencia">
  				<#list puntuacion as i>
  					<#if i==numPregunta&& i==1>
  						<h3><span class="label label-pill label-info">Primera pregunta por 10 puntos</span></h3>
  					</#if>
  					<#if i==numPregunta&& i==2>
  						<h3><span class="label label-pill label-info">Segunda pregunta por 20 puntos</span></h3>
  					</#if>
  					<#if i==numPregunta&& i==3>
  						<h3><span class="label label-pill label-info">Tercera pregunta por 30 puntos</span></h3>
  					</#if>
  					</#list>
  					
  			</div>
  			</br>
  			</br>
  			<a href="/capacitacion-juego/pregunta?jjuId=${jjuId}&numPregunta=${numPregunta}&jNivelId=${nivel.id}" class="btn btn-success">¡Jugar!</a>
  		</div>
  		
  	</div>
 
</div>
</div>
<#include "admin-footer.ftl">