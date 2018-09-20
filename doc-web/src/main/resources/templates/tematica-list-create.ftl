<#setting number_format="computer">
<#assign pageTitle = "Nombre de la Tematica" />
<#-- <#assign mode = nombreExpediente.mode!"" /> -->
<#assign deferredJS = "" />
<#import "spring.ftl" as spring />
<#include "admin-header.ftl">
<#include "util-macros.ftl" />

<div class="container">
    <div class="row">
        <h1 class="cus-h1-page-title">${pageTitle}</h1>
        <form action="/admin/tematica/crear" method="POST" enctype="multipart/form-data" >
            <fieldset class="form-group">
                <label for="nombre">Nombre(*)</label>
                <input type="text" class="form-control" id="nombre" name="nombre" value="${(tematica.nombre)!""}" style="text-transform:uppercase"/>
                <label for="descripcion">Descripcion(*)</label>
                <textarea type="text" class="form-control" id="descripcion" name="descripcion" value="${(tematica.descripcion)!""}" style="text-transform:uppercase"/></textarea>
            </fieldset>
            <div class="m-y">
                <button id="btnGuardar" type="submit" class="btn btn-primary">Guardar</button>
                <a href="/admin/tematica" class="btn btn-secondary">Cancelar</a>
                </div>
            </form>
        </div>
    </div>
<#include "footer.ftl">