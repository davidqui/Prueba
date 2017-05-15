<#assign pageTitle = documento.asunto!"Documento" />

<#include "bandeja-header.ftl" />

<h5 class="m-y">Selección del documento principal</h5>

<p class="m-y">
    El documento principal es el oficio que se encuentra al inicio del documento que se está radicando.
</p>

<div class="row m-y">
    <div class="col-md-12">
        <#if files?? >
            <div class="card">
                <div class="card-header">
                    Archivos en la unidad virtual
                </div>
                <div class="card-block">
                        <#list files as file>
                            <a href="/documento/digitalizar-principal-net?pin=${documento.instancia.id}&file=${file.name}">${file.name}</a>
                        </#list>
                </div>
            </div>
        <#else>
            <div class="jumbotron">
                <h1>La unidad no está conectada</h1>
                <p>La unidad virtual no se encuentra conectada en este computador. Recuerde que no puede tener la unidad conectada a dos computadores al tiempo y que esta funcionalidad sólo aplica para los sistemas operativos Windows que se encuentren configurados dentro del dominio de autenticación de Windows.
            </div>
        </#if>
    </div>
</div>

<#include "bandeja-footer.ftl" />