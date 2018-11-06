<#setting number_format="computer">
<#assign pageTitle = "Pregunta" />
<#-- <#assign mode = nombreExpediente.mode!"" /> -->
<#import "spring.ftl" as spring />
<#include "admin-header.ftl">
<#include "util-macros.ftl" />

<div class="container">
    <div class="row">
        <h1 class="cus-h1-page-title">Editar ${pageTitle} de:    <b>${(temaCapacitacionEditar.tema?capitalize)!""}</b></h1>
        <form action="/admin/pregunta/actualizar" method="POST"  >
            <input type="hidden" id="id" name="id" value="${pregunta.id}" />
            <fieldset class="form-group">
                <label for="pregunta">Pregunta(*)</label>
                <input type="text" class="form-control" id="pregunta" name="pregunta" value="${(pregunta.pregunta)!""}" style="text-transform:uppercase"/>
                 <input type="hidden" class="form-control" id="temaCapacitacion" name="temaCapacitacion" value="${(temaCapacitacionEditar.id)!""}"/>
            </fieldset>
            <div class="m-y">
                <button id="btnGuardar" type="submit" class="btn btn-primary">Guardar</button>
                <a href="/admin/pregunta/list/${temaCapacitacionEditar.id}" class="btn btn-secondary">Cancelar</a>
                </div>
            </form>
        </div>
    </div>
<#include "footer.ftl">