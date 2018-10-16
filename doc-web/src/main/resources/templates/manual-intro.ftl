<#assign pageTitle = "Manual de Usuario SICDI">
<#assign activePill = "none">
<#include "header.ftl">
<link href="/css/manual.css" rel="stylesheet" type="text/css"/>
        <div class="col-md-4 col-lg-3">
          <div class="card">
            <div class="card-header">
              <a href="/manual/intro" class="nav-link"><img src="/img/menu.svg" style="margin-right:2px;"><b>Temario Manual de Usuario</b></a>
            </div>
            <div class="card-block">
              <ul class="nav nav-pills nav-stacked">
              	<#if tematicas?? >
                <#list tematicas as tematica>
                <li class="nav-item">
                  <a href="/manual/multimedia/${tematica.id}" class="nav-link" ><img src="/img/book-open.svg" style="height: 14px; margin-right:2px;"> ${tematica.nombre?capitalize}</a>
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
    
    <p>Bienvenido señor usuario, al <ins>Manual de Usuario Final</ins> del <b>Sistema Clasificado de documentos de Inteligencia Militar - SICDI</b>, 
        esta es una herramienta de autoaprendizaje en la que usted podrá validar la correcta utilización de todas 
        las funcionalidades del sistema, esto le facilitará su interacción con el aplicativo, haciendo más eficiente su utilización. 
    </p>
    <div class="alert alert-info" role="alert">
        <form class="form-search content-search navbar-form" action="/manual/busqueda" method="GET">
            <div class="input-group">
                <input placeholder="Buscar Recurso Multimedia" name="findTokens" id="findTokens" class="form-control form-text" type="search" size="15" maxlength="128" />
                <span class="input-group-btn">
                    <button type="submit" class="btn btn-primary">Buscar</button>
                    </span>
                </div>
            </form>
        </div>
    
    <!--Fin alerta-->
    <div class="card bg-light text-dark">
         <div class="card-body">
             
        <div class="row">
            <div class="col-sm-9">
        <#if !recursos?? || recursos?size == 0>
                <div class="alert alert-warning" role="alert">
                    <strong>Disculpanos!</strong> Estamos trabajando para ofrecerte el mejor contenido. 
                  </div>
            <#else>
            
            <div class="alert alert-success" role="alert">
  		<strong>¡Enterate de los mas reciente que SICDI tiene para ti!</strong> 
                </div>
                
            <div class="card text-center">
               
                <#list recursos as recurso>
                
                <#if recurso.tipo!="application/pdf">
                <div class="card-header">
                    <h2 id="tematica"><b>${recurso.tematica.nombre?capitalize}</b></h2>
                    <h3 id="titulovideo">${recurso.nombre?capitalize}</h3>
                    </div>
                <div class="card-body">
                    <video id="myVideo" name="videoprincipal" src="/admin/recursoMultimedia/descargar/${recurso.id}" width="100%" height="592" controls>
                        Tu navegador no implementa el elemento <code>video</code>.
                        </video>
                    </div>
                    <!--Fin card body-->
                    <div class="card-footer text-muted">
                    <h6><b style="margin-right:2px">Descripción:</b></h6><p id="descripcion" style="text-align: justify;">${recurso.descripcion?capitalize}</p>
                    <#break>
                    </#if>
                    </#list>
                    </div>
                    <!--Fin card footer-->
                </div>
            
            <div class="cards-split-container cards-split-container2">
        
        <div class="container" id="pick2">
            <#assign pdf = 0>
            <#list recursos as recurso>
            <#if recurso.tipo=="application/pdf">  
            <#assign pdf = pdf+1>
            <#if pdf<=5>
            <div class="card card-contenido text-center">
                <div class="card-header  white-space: initial;">
                    <p style="white-space:Normal; margin-bottom: 0rem;">${recurso.nombre?capitalize}</p>
                    </div>
                <div class="card-body">
                    <img src="/ofs/download/tmb-static/${recurso.ubicacion}" alt="${recurso.nombre}" width="204" height="174">
                    </div>
                <div class="card-footer text-muted">
                    <a href="/admin/recursoMultimedia/descargar/${recurso.id}" target="_blank" class="btn btn-primary">Ver</a>
                  </div>
                </div>
                <!--Fin card-->
                </#if>
                </#if>
                </#list>
            </div>
        <!--Fin container pick-->
       </div>
    <!--Fin container card-->
            
            </div>
        <!--Fin columna 1 Video Principal-->
        <div class="col-sm-3">
            <#assign video = 0>
            <div class="card text-center">
              <#list recursos as recurso>
                <#if recurso.tipo!="application/pdf">
                <#assign video = video+1>
                <#if video<=5>
                <div class="card-header">
                    <h6>${recurso.nombre?capitalize}</h6>
                    </div>
                <div class="card-body padre-videominiatura" onclick="myFunction('/admin/recursoMultimedia/descargar/${recurso.id}', '${recurso.nombre?capitalize}', '${recurso.descripcion?capitalize}','${recurso.tematica.nombre?capitalize}')">
                <div id="reproducir" class="hover-video"><img src="/img/Play5.png" style="width: 25%;"></div>
                
                <div id="videominiatura">
                    
                    <video id="${recurso.id}" name="listavideos" src="/admin/recursoMultimedia/descargar/${recurso.id}" width="100%" height="100%">
                        Tu navegador no implementa el elemento <code>video</code>
                        </video>
                    </div>
                    </div>
                  </#if>
                  </#if>
                        </#list>
                </div>
            <!--fin card-->
            </div>
        <!--Fin columna 1 Preview Videos-->
    </div>
    <!--Fin Fila 1-->

    <div class="row">
        <div class="col-sm-9">
    

    
    </div>
        <!--Fin columna card-->
    </div>
    <!--Fin fila 2-->
    </div>
  
             </div>
             <!--Fin card body principal-->
        
        </div>
        <!--Fin card principal-->
   </#if>
    <!--Fin if principal para cuando aun no hay contenido tematico-->
    
  
</div>
<!--Fin container principal-->
<script> 
    function myFunction(url,nombreRecurso, nombreDescripcion, nombreTematica) {
        $("#tematica").text(nombreTematica);
        $("#titulovideo").text(nombreRecurso);
        $("#descripcion").text(nombreDescripcion);
        document.getElementById("myVideo").src = url;
        document.getElementById("myVideo").load();
    } 
    </script>
<#include "admin-footer.ftl">