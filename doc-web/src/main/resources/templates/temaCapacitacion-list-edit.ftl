<#setting number_format="computer">
<#assign pageTitle = "Editar Tema de Capacitacón" />
<#assign deferredJS = "" />
<#import "spring.ftl" as spring />
<#include "admin-header.ftl">
<#include "util-macros.ftl" />

<div class="container">
    <div class="row">
        <h1 class="cus-h1-page-title">${pageTitle}</h1>
        <form action="/admin/temaCapacitacion/actualizar" method="POST" enctype="multipart/form-data" >
            <input type="hidden" id="id" name="id" value="${tematica.id}" />
            <fieldset class="form-group">
                <label for="nombre">Tema (*)</label>
                <input type="text" class="form-control" id="tema" name="tema" value="${(temaCapacitacion.tema)!""}" />
                </fieldset>
            <div class="row">
                <div class="col-xs-8">
                    <select id="doc-obs-defecto-select" name="doc-obs-defecto-select" class="form-control input-sm" onchange="setObservacionDefecto(this, 'justificacion')">
                        <option value="">Clasificación (*)</option>
                        <#list clasificacions as clasificacion >
                        <option value="${clasificacion.Id}">${clasificacion.nombre}</option>
                        </#list>
                        </select>
                    </div>
                </div>
            <div class="m-y">
                <button id="btnGuardar" type="submit" class="btn btn-primary">Guardar</button>
                <a href="/admin/temaCapacitacion" class="btn btn-secondary">Cancelar</a>
                </div>
            </form>
        </div>
    </div>
<#include "footer.ftl">