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
      <#list procesos as proc>
          <div class="card center-block" style="width: 250px">
            <div class="card-block">
              <h4 class="card-title">${proc.nombre}</h4>
            </div>
            <img class="card-img-top" src="/css/img/${proc.imagen!"noimg.png"}" alt="">
            <div class="card-block">
              <p class="card-text">${proc.descripcion!""}</p>
              <a href="/proceso/instancia/nueva?proId=${proc.id}" class="btn btn-primary">Seleccionar</a>
            </div>
          </div>
      </#list>
      </div>
    </div>
</div>

<#include "footer.ftl" />
