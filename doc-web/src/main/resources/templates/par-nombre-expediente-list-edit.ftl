<#setting number_format="computer">
<#assign pageTitle = "Nombre por defecto" />
<#-- <#assign mode = nombreExpediente.mode!"" /> -->
<#assign deferredJS = "" />
<#import "spring.ftl" as spring />
<#include "admin-header.ftl">
<#include "util-macros.ftl" />

<div class="container">
    <div class="row">
        <h1 class="cus-h1-page-title">${pageTitle}</h1>
        <form action="/admin/parnombrexpediente/actualizar" method="POST" enctype="multipart/form-data" >
            <input type="hidden" id="parId" name="parId" value="${nombreExpediente.parId}" />
            <fieldset class="form-group">
                <label for="parNombre">Texto</label>
                <input type="text" class="form-control" id="parNombre" name="parNombre" value="${(nombreExpediente.parNombre)!""}" />
            </fieldset>
            <div class="m-y">
                <button id="btnGuardar" type="submit" class="btn btn-primary">Guardar</button>
                <a href="/admin/parnombrexpediente" class="btn btn-secondary">Cancelar</a>
                </div>
            </form>
        </div>
    </div>
<#include "footer.ftl">