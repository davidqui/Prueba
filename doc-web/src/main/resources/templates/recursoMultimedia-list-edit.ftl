<#setting number_format="computer">
<#assign pageTitle = "Editar Recurso Multimedia" />
<#-- <#assign mode = nombreExpediente.mode!"" /> -->
<#assign deferredJS = "" />
<#import "spring.ftl" as spring />
<#include "admin-header.ftl">
<#include "util-macros.ftl" />

<div class="container">
    <div class="row">
        <h1 class="cus-h1-page-title">${pageTitle}</h1>
        <form action="/admin/recursoMultimedia/actualizar" method="POST" enctype="multipart/form-data" >
            <input type="hidden" id="id" name="id" value="${recursoMultimedia.id}" />
            <fieldset class="form-group">
                <label for="nombre">Nombre</label>
                <input type="text" class="form-control" id="nombre" name="nombre" value="${(recursoMultimedia.nombre)!""}" style="text-transform:uppercase"/>
                <label for="descripcion">Descripcion</label>
                <input type="text" class="form-control" id="descripcion" name="descripcion" value="${(recursoMultimedia.descripcion)!""}" style="text-transform:uppercase"/>
                <label for="fuente">Fuente</label>
                <input type="text" class="form-control" id="fuente" name="fuente" value="${(recursoMultimedia.fuente)!""}" style="text-transform:uppercase"/>
                <label for="fuente">Peso Orden</label>
                <input type="text" class="form-control" id="fuente" name="fuente" value="${(recursoMultimedia.pesoOrden)!""}"/>
            </fieldset>
            <div class="m-y">
                <button id="btnGuardar" type="submit" class="btn btn-primary">Guardar</button>
                <a href="/admin/recursoMultimedia" class="btn btn-secondary">Cancelar</a>
                </div>
            </form>
        </div>
    </div>
<#include "footer.ftl">