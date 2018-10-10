<#assign pageTitle = "Manual de Usuario SICDI">
<#include "manual-opciones.ftl">
<link href="/css/manual.css" rel="stylesheet" type="text/css"/>
<div class="container-fluid">
    <div class="row">
        <div class="col-sm-9">
            <#if !recursos?? || recursos?size == 0>
                <div class="alert alert-warning" role="alert">
                    <strong>Disculpanos!</strong> Estamos trabajando para ofrecerte el mejor contenido. 
                  </div>
            <#else>
            <div class="card text-center">
                <#list recursos as recurso>
                <#if recurso.tipo!="application/pdf">
                <div class="card-header">
                    <h2><b>${recurso.tematica.nombre?capitalize}</b></h2>
                    <h3 id="titulovideo">${recurso.nombre?capitalize}</h3>
                    </div>
                <div class="card-body">
                    <video id="myVideo" name="videoprincipal" src="/admin/recursoMultimedia/descargar/${recurso.id}" width="100%" height="592"  controls autoplay>
                        Tu navegador no implementa el elemento <code>video</code>.
                        </video>
                    </div>
                <div class="card-footer text-muted">
                    <p id="descripcion" style="text-align: justify;"><b style="margin-right:2px">Descripción:</b> ${recurso.descripcion?capitalize}</p>
                    <#break>
                    </#if>
                    </#list>
                    </div>
                
                </div>
           <!--Fin card-->
            </div>
        <!--Fin columna 1 Video Principla-->
        <div class="col-sm-3">
            <div class="card text-center">
              <#list recursos as recurso>
                <#if recurso.tipo!="application/pdf">
                <div class="card-header">
                    <h6>${recurso.nombre?capitalize}</h6>
                    </div>
                <div class="card-body padre-videominiatura" onclick="myFunction('/admin/recursoMultimedia/descargar/${recurso.id}', '${recurso.nombre?capitalize}', '${recurso.descripcion?capitalize}')">
                <div id="reproducir" class="hover-video"><img src="/img/Play5.png" style="width: 25%;"></div>
                
                <div id="videominiatura">
                    
                    <video id="minvideo" name="listavideos" src="/admin/recursoMultimedia/descargar/${recurso.id}" width="100%" height="100%" preload="preload">
                        Tu navegador no implementa el elemento <code>video</code>
                        </video>
                    </div>
                    </div>
             
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
    <div class="cards-split-container cards-split-container2">
        
        <div class="container" id="pick2">
            <#list recursos as recurso>
                <#if recurso.tipo=="application/pdf">    
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
                </#list>
            </div>
        <!--Fin container pick-->
       </div>
    <!--Fin container card-->

    
    </div>
        <!--Fin columna card-->
    </div>
    <!--Fin fila 2-->
    </div>
<!--Fin container-fluid-->
</#if>
<!--Fin if principal para cuando aun no hay contenido tematico-->
    <script> 
    function myFunction(url,nombreRecurso,nombreDescripcion) {
        $("#titulovideo").text(nombreRecurso);
        $("#descripcion").text(nombreDescripcion);
        document.getElementById("myVideo").src = url;
        document.getElementById("myVideo").load();
    } 
    </script>
<#include "admin-footer.ftl">
