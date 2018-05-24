<#setting number_format="computer">
<#assign pageTitle = observacionDefecto.nombre!"Observación por defecto" />
<#-- <#assign mode = observacionDefecto.mode!"" /> -->
<#assign deferredJS = "" />
<#import "spring.ftl" as spring />
<#include "admin-header.ftl">
<#include "util-macros.ftl" />

<div class="container">
    <div class="row">
        <h1 class="cus-h1-page-title">${pageTitle}</h1>
	<@flash/>
        <form action="/admin/doc-observacion-defecto/actualizar" method="POST" enctype="multipart/form-data" >
            <input type="hidden" id="id" name="id" value="${observacionDefecto.id}" />
            <fieldset class="form-group">
                <label for="textoObservacion">Código</label>
                <input type="text" class="form-control" id="textoObservacion" name="textoObservacion" value="${(observacionDefecto.textoObservacion)!""}" />
            </fieldset>
            
            <div class="m-y">
                <button id="btnGuardar" type="submit" class="btn btn-primary">Guardar</button>
                <a href="/admin/doc-observacion-defecto" class="btn btn-secondary">Cancelar</a>
                </div>
            </form>
        </div>
    </div>
<#include "footer.ftl">