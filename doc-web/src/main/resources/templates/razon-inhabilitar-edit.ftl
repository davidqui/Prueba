<#setting number_format="computer">
<#assign pageTitle = "Observación por defecto" />
<#-- <#assign mode = razonInhabilitar.mode!"" /> -->
<#assign deferredJS = "" />
<#import "spring.ftl" as spring />
<#include "admin-header.ftl">
<#include "util-macros.ftl" />

<div class="container">
    <div class="row">
        <h1 class="cus-h1-page-title">${pageTitle}</h1>
        <form action="/admin/razon-inhabilitar/actualizar" method="POST" enctype="multipart/form-data" >
            <input type="hidden" id="id" name="id" value="${razonInhabilitar.id}" />
            <fieldset class="form-group">
                <label for="textoRazon">Texto</label>
                <input type="text" class="form-control" id="textoRazon" name="textoRazon" value="${(razonInhabilitar.textoRazon)!""}" />
            </fieldset>
            
            <div class="m-y">
                <button id="btnGuardar" type="submit" class="btn btn-primary">Guardar</button>
                <a href="/admin/razon-inhabilitar" class="btn btn-secondary">Cancelar</a>
                </div>
            </form>
        </div>
    </div>
<#include "footer.ftl">