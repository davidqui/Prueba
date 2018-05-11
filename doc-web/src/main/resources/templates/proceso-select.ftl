<#assign pageTitle = "Procesos disponibles" />
<#assign pageSubTitle = "Selección de proceso" />
<#include "header.ftl" />
<div class="container-fluid">
    <h1 class="cus-h1-page-title">Selección de proceso</h1>
    <p class="m-y">
        El proceso gobierna todo el flujo de trabajo mediante el cual se gestionan los documentos. Es importante 
        determinar cuál es el proceso que se va a usar ya que una vez se inicie, este no se puede cambiar.
        </p>
    <div class="card-deck-wrapper">
        <div class="card-deck">
            <#--
                2018-05-11 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
                feature-162: Cambio de nombre de la variable a proceso.
            -->
      <#list procesos as proceso>
            <div class="card center-block" style="width: 250px">
                <div class="card-block">
                    <h4 class="card-title">${proceso.nombre}</h4>
                    </div>
                <img class="card-img-top" src="/css/img/${proceso.imagen!"noimg.png"}" alt="">
                <div class="card-block">
                    <p class="card-text">${proceso.descripcion!""}</p>
                    <a href="/proceso/instancia/nueva?proId=${proceso.id}" class="btn btn-primary">Seleccionar</a>
                    </div>
                </div>
      </#list>
            </div>
        </div>
    </div>

<#include "footer.ftl" />
