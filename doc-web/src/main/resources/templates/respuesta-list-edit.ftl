<#setting number_format="computer">
<#assign pageTitle = "Editar Pregunta" />
<#-- <#assign mode = nombreExpediente.mode!"" /> -->
<#assign deferredJS = "" />
<#import "spring.ftl" as spring />
<#include "admin-header.ftl">
<#include "util-macros.ftl" />

<div class="container">
    <div class="row">
        <h1 class="cus-h1-page-title">Editar ${pageTitle} -> ${(temaCapacitacionCrear.tema?capitalize)!""}</h1>
        <form action="/admin/pregunta/actualizar" method="POST"  >
            <input type="hidden" id="id" name="id" value="${pregunta.id}" />
            <fieldset class="form-group">
                <label for="nombre">Pregunta(*)</label>
                <input type="text" class="form-control" id="pregunta" name="pregunta" value="${(pregunta.pregunta)!""}" style="text-transform:uppercase"/>
            </fieldset>
            <div class="m-y">
                <button id="btnGuardar" type="submit" class="btn btn-primary">Guardar</button>
                <a href="/admin/pregunta/list/${temaCapacitacionEditar.id}" class="btn btn-secondary">Cancelar</a>
                </div>
            </form>
        </div>
    </div>
<#include "footer.ftl">