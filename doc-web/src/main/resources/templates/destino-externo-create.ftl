<#setting number_format="computer">
<#assign pageTitle = "Observacion por defecto" />
<#-- <#assign mode = observacionDefecto.mode!"" /> -->
<#assign deferredJS = "" />
<#import "spring.ftl" as spring />
<#include "admin-header.ftl">
<#include "util-macros.ftl" />

<div class="container">
    <div class="row">
        <h1 class="cus-h1-page-title">${pageTitle}</h1>
        <form action="/admin/destino-externo/crear" method="POST" enctype="multipart/form-data" >
            <fieldset class="form-group">
                <label for="textoObservacion">Texto</label>
                <input type="text" class="form-control" id="nombre" name="nombre" value="${(destinoExterno.nombre)!""}" style="text-transform: uppercase;"/>
            </fieldset>
            <fieldset class="form-group">
                <label for="textoObservacion">Sigla</label>
                <input type="text" class="form-control" id="sigla" name="sigla" value="${(destinoExterno.sigla)!""}" style="text-transform: uppercase;"/>
            </fieldset>
            
            <div class="m-y">
                <button id="btnGuardar" type="submit" class="btn btn-primary">Guardar</button>
                <a href="/admin/destino-externo" class="btn btn-secondary">Cancelar</a>
                </div>
            </form>
        </div>
    </div>
<#include "footer.ftl">