<#assign pageTitle = (informe.nombre)!"Informe" />
<#include "header.ftl" />

<div class="container-fluid">

<h2>Informe: <span class="text-muted">${pageTitle}</span></h2>
<br/>
    <div class="col-md-3">
        <div class="card">
            <div class="card-block">
                <h5 class="card-title">Parámetros del informe</h5>
                ${params.render()}
            </div>
        </div>
    </div>
    <div class="col-md-9">
        <#if tabla??>
            ${tabla.render()}
        <#else>
            <div class="jumbotron">
                <h4>No hay resultados</h4>
                <p>En este momento no se encuentran resultados para los criterios o no ha ejecutado el informe</p>
            </div>
        </#if>
    </div>



</div>

<#include "footer.ftl" />