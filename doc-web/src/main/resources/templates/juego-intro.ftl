<#assign pageTitle = "Capacitaciones">
<#assign activePill = "none">
<#include "header.ftl">
        <div class="col-md-4 col-lg-3">
          <div class="card">
            <div class="card-header">
              Capacitaciones
            </div>
            <div class="card-block">
              <ul class="nav nav-pills nav-stacked">
              	<#if juegos?? >
                <#list juegos as j>
                <li class="nav-item">
                  <a href="/capacitacion-juego/juego?jid=${j.id}" class="nav-link <#if juego?? &&j.id == juego.id>active</#if>">${j.nombre}</a>
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
    <p>Este es modulo le permite a los usuarios aprender de una forma didáctica sobre un tema en específico. 
    Cada capacitación consiste en un juego de preguntas donde el usuario tendrá que elegir entre cuatro opciones de respuesta. 
     Para llegar al final de cada nivel, deberá responder todas las preguntas sin equivocarse.</p>
     <p>El usuario contará con ayudas, las cuales le permitirán avanzar en el juego en caso de necesitarlas</p>
    <h6> Ayudas</h6>
    <p>Usted contará con 2 ayudas:</p>
    <span><div id="ayuda1" class="btn"></div> Descarta 2 de las respuestas.</span>
    <p><div id="ayuda2" class="btn"></div> Le muestra el porcentaje de selección de otros usuarios del juego para cada respuesta.</p>
    <p>Por cada nivel, sólo podrá usar las ayudas una única vez. </p>
    <div class="alert alert-info" role="alert">
  		<strong>!Jugar!</strong> Para iniciar, seleccione un tema de capacitación del menú
	</div>
    
  
</div>
<#include "admin-footer.ftl">