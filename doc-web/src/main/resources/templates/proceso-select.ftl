<#assign pageTitle = "Procesos disponibles" />
<#assign pageSubTitle = "Selección de proceso" />
<#include "header.ftl" />
<#--
    2018-05-11 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
    feature-162: Mejora de presentación.
-->
<div class="container">
    <h1 class="cus-h1-page-title">Selección de proceso</h1>
    <p class="m-y">
        El proceso gobierna todo el flujo de trabajo mediante el cual se gestionan los documentos. Es importante 
        determinar cuál es el proceso que se va a usar ya que una vez se inicie, este no se puede cambiar.
        </p>

    <div class="card-deck-wrapper">
        <div class="card-deck" style="padding-bottom: 50px;">
            <#--
                2018-05-11 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
                feature-162: Cambio de nombre de la variable a proceso.
            -->
      <#list procesos as proceso>
            <div class="card center-block" style="width: 250px; padding-bottom: 50px;">

                <div class="card-block" style="height: 150px; text-align: left;">
                    <h4 class="card-title">${proceso.nombre}</h4>
                    </div>

                <div class="card-block" style="height: 220px;">
                    <img class="card-img-top" src="/css/img/${proceso.imagen!"noimg.png"}" alt="" style="width: 200px; height:170px;">
                </div>

                <div class="card-block" style="vertical-align: text-bottom; text-align: justify;">
                    <p class="card-text">${proceso.descripcion!""}</p>                    
                    </div>

                <div class="card-footer" style="position: absolute; bottom: 0; width:100%; text-align: center;">
                    <!--#181 se agrega loader --> 
                    <a href="/proceso/instancia/nueva?proId=${proceso.id}" class="btn btn-primary" onclick="loading();">Seleccionar</a>
                    </div>

                </div>
      </#list>
            </div> <!-- card-deck -->
        </div> <!-- card-deck-wrapper -->
    </div> <!-- container -->

<#include "footer.ftl" />
