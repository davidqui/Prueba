<#assign pageTitle = "Procesos disponibles" />
<#assign pageSubTitle = "Selección de proceso" />
<#include "header.ftl" />
<div class="container-fluid">
    <h1>Dar Respuesta - Registrar Documentos </h1>
    <h2>Selección de Proceso de Respuesta</h2>
    <p>
    Seleccione el proceso por el cual desea dar respuesta para el documento "${documento.asunto}".
    </p>
    <p>
    <span class="label label-danger">IMPORTANTE</span>Es importante determinar cuál es el proceso que se va a usar ya que una vez se inicie, este no se puede cambiar.
    </p>
    
    <div class="card-deck" style="margin: 0 auto; float: none; margin-bottom: 10px;">
    	<#list procesos as proceso>
    		<div class="card" style="width: 450px">
				<img class="card-img-top" src="/css/img/${proceso.imagen!"noimg.png"}" alt="" height="188" width="188">
    			<div class="card-block">
      				<h4 class="card-title">${proceso.nombre}</h4>
      				<p class="card-text">${proceso.descripcion!""}</p>
    			</div>    			
    			<div class="card-footer">
      				<a href="/documento/dar-respuesta-ciclico?pin=${pin}&proResp=${proceso.id}" class="btn btn-primary">Seleccionar</a>
    			</div>
    		</div>
    	</#list>
    </div>
</div>

<#include "footer.ftl" />