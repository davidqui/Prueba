<#assign pageTitle = "Capacitaciones">
<#assign activePill = "none">
<#include "header.ftl">
        <div class="col-md-4 col-lg-3">
          <div class="card">
            <div class="card-header">
              <b>Capacitaciones</b>
            </div>
            <div class="card-block">
              <ul class="nav nav-pills nav-stacked">
              	<#if temas?? >
                <#list temas as tema>
                  
                <li class="nav-item" >
                  <a href="/capacitacion/juego" style="white-space: initial; justific" class="nav-link"><b>${tema.tema?capitalize} &nbsp;&nbsp;<#if !tema.clasificacion??><em>&nbsp;&nbsp; SIN CLASIFICACION </em><#else><em>${tema.clasificacion}</em></b></#if></a> 
                 
                </li>
                </#list>
				</#if>
              </ul>
            </div>
          </div>
        </div>
        <div class="col-md-8 col-lg-9">
          <h1 class="cus-h1-page-title">${pageTitle}</h1>

<div class="container-fluid">
    <p><b><em>Bienvenido al Modulo de capacitación</em></b><br>
    Este es modulo le permite a los usuarios aprender de una forma didáctica sobre un tema en específico.<br> 
    Cada capacitación consiste en un juego de preguntas donde el usuario tendrá que elegir entre cuatro opciones de respuesta. 
     Para llegar al final de cada nivel, deberá responder todas las preguntas sin equivocarse.</p>
     <p>El usuario contará con ayudas, las cuales le permitirán avanzar en el juego en caso de necesitarlas</p>
    <h6> Ayudas</h6>   <b>jejejejeje  Ningunaaaaaaaaa!!!!  &nbsp;&nbsp; ¯\_(ツ)_/¯</b>
    <br>
    <br>
<!--    <p>Usted contará con 2 ayudas:</p>
    <span><div id="ayuda1" class="btn"></div> Descarta 2 de las respuestas.</span>
    <p><div id="ayuda2" class="btn"></div> Le muestra el porcentaje de selección de otros usuarios del juego para cada respuesta.</p>
    <p>Por cada nivel, sólo podrá usar las ayudas una única vez. </p>-->
    <div class="alert alert-info" role="alert">
  		<strong>!Jugar!</strong> Para iniciar, seleccione un tema de capacitación del menú
	</div>
    
  
</div>
<#include "admin-footer.ftl">