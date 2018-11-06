<#setting number_format="computer">
<#assign pageTitle = "Respuesta" />
<#-- <#assign mode = nombreExpediente.mode!"" /> -->
<#assign deferredJS = "" />
<#import "spring.ftl" as spring />
<#include "admin-header.ftl">
<#include "util-macros.ftl" />

<div class="container">
    <div class="row">
        <h1 class="cus-h1-page-title">Editar ${pageTitle} de:   <em><b>${(preguntasEditar.pregunta?capitalize)!""}</b></em></h1>
        <form action="/admin/respuesta/actualizar" method="POST"  >
            <input type="hidden" id="pregunta" name="pregunta" value="${preguntasEditar.id}" />
            <input type="hidden" id="id" name="id" value="${respuesta.id}" />
            <fieldset class="form-group">
                <label for="nombre">Texto Respuesta(*)</label>
                <input type="text" class="form-control" id="textoRespuesta" name="textoRespuesta" value="${(respuesta.textoRespuesta)!""}" style="text-transform:uppercase"/>
            </fieldset>
            <fieldset class="form-group">
                <label for="nombre">Correcta(*)</label>
                <input type="checkbox" class="form-control" id="correcta" name="correcta" value="${(respuesta.correcta?c)!""}" <#if respuesta.correcta == true>checked</#if>/>
            </fieldset>
            <div class="m-y">
                <button id="btnGuardar" type="submit" class="btn btn-primary">Guardar</button>
                <a href="/admin/respuesta/list/${preguntasEditar.id}" class="btn btn-secondary">Cancelar</a>
                </div>
            </form>
        </div>
    </div>
<#include "footer.ftl">