<#setting number_format="computer">
<#assign pageTitle = "Pregunta" />
<#assign deferredJS = "" />
<#import "spring.ftl" as spring />
<#include "admin-header.ftl">
<#include "util-macros.ftl" />

<div class="container">
    <div class="row">
        <h1 class="cus-h1-page-title">Crear ${pageTitle} -> <b>${preguntasCrear.pregunta?capitalize}</b></h1>
        <form action="/admin/respuesta/crear" method="POST" >
            <fieldset class="form-group">
                <label for="nombre">Texto Respuesta(*)</label>
                <input type="text" class="form-control" id="textoRespuesta" name="textoRespuesta" value="${(respuesta.textoRespuesta)!""}" style="text-transform:uppercase"/>
                <label for="temaCapacitacion">Correcta(*)</label>
                <input type="checkbox" class="form-control" id="correcta" name="correcta" value="${(respuesta.correcta)!""}"/>
                <label for="temaCapacitacion">Pregunta(*)</label>
                <input type="hidden" class="form-control" id="pregunta" name="pregunta" value="${(pregunta.id)!""}"/>
                </fieldset>

            <div class="m-y">
                <button id="btnGuardar" type="submit" class="btn btn-primary">Guardar</button>
                <a href="/admin/respuesta/list/${preguntasCrear.id}" class="btn btn-secondary">Cancelar</a>
                </div>
            </form>
        </div>
    </div>
<#include "footer.ftl">