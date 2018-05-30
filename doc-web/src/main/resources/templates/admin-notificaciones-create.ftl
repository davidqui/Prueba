<#setting number_format="computer">
<#assign pageTitle = "Notificaciones" />
<#-- <#assign mode = observacionDefecto.mode!"" /> -->
<#assign deferredJS = "" />
<#import "spring.ftl" as spring />
<#include "admin-header.ftl">
<#include "util-macros.ftl" />

<div class="container">
    <div class="row">
        <h1 class="cus-h1-page-title">${pageTitle}</h1>
        <form action="/admin/doc-observacion-defecto/crear" method="POST" enctype="multipart/form-data" >
            <fieldset class="form-group">
                <label for="textoObservacion">Texto</label>
                <input type="text" class="form-control" id="textoObservacion" name="textoObservacion" value="${(observacionDefecto.textoObservacion)!""}"/>
            </fieldset>
            <fieldset class="form-group">
                <label for="trd">Tipo de Notificación</label>
                <select id="tipo-notificacion-form" name="selectedTipoNotificacion" class="form-control input-sm" >
                    <option value="">--------:</option>
                    <#list tipoNotificacion as tiposNotificaciones >
                    <option value="${tipoNotificacion.id}">${tipoNotificacion.nombre}</option>
                    </#list>
                </select>
            </fieldset>
            <fieldset class="form-group">
                <label for="textoObservacion">Clasificación</label>
                <select id="doc-obs-defecto-select" name="selectedClasificacion" class="form-control input-sm">
                    <option value="">--------:</option>
                    <#list clasificacion as clasificaciones >
                    <option value="${clasificacion.id}">${clasificacion.nombre}</option>
                    </#list>
                </select>
            </fieldset>
            <fieldset class="form-group">
                <label for="textoObservacion">Template</label>
                <textarea id="descripcion" class="form-control" name="notification.template" rows="10"></textarea>
            </fieldset>
            <div class="m-y">
                <button id="btnGuardar" type="submit" class="btn btn-primary">Guardar</button>
                <a href="/admin/doc-observacion-defecto" class="btn btn-secondary">Cancelar</a>
            </div>
        </form>
    </div>
</div>
<#include "footer.ftl">