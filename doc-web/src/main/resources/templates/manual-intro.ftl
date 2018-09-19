<#assign pageTitle = "Manual de Usuario SICDI">
<#assign activePill = "none">
<#include "header.ftl">
        <div class="col-md-4 col-lg-3">
          <div class="card">
            <div class="card-header">
              Temario Manual de Usuario
            </div>
            <div class="card-block">
              <ul class="nav nav-pills nav-stacked">
              	<#if tematicas?? >
                <#list tematicas as tematica>
                <li class="nav-item">
                  <a href="/manual/multimedia/${tematica.id}" class="nav-link" >${tematica.nombre?capitalize}</a>
                </li>
                </#list>
				</#if>
              </ul>
            </div>
          </div>
        </div>
        <div class="col-md-8 col-lg-9">
        
<div class="container-fluid">
    <h1 class="cus-h1-page-title">${pageTitle}</h1>
    <p>Bienvenido señor usuario, al <ins>Manual de Usuario Final del <b>Sistema Integrado de Información Clasificada de Inteligencia Militar - SICDI</b></ins>, 
        esta es una herramienta de autoaprendizaje en la que usted podrá validar la correcta utilización de todas 
        las funcionalidades del sistema, esto le facilitará su interacción con el aplicativo, haciendo más eficiente su utilización. 
    </p>
    
    <div class="card bg-light text-dark">
         <div class="card-header">Recursos Multimedia Mas Recientes:
             </div>
         <div class="card-body">
             
             </div>
        
        </div>
    <div class="alert alert-info" role="alert">
  		<strong>Para iniciar, seleccione un tema de su interes del menú izquierdo...</strong> 
	</div>
    
  
</div>
<#include "admin-footer.ftl">