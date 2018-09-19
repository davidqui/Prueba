<#setting number_format="computer">
<#assign pageTitle = "Editar Recurso Multimedia" />
<#-- <#assign mode = nombreExpediente.mode!"" /> -->
<#assign deferredJS = "" />
<#import "spring.ftl" as spring />
<#include "admin-header.ftl">
<#include "util-macros.ftl" />

<div class="container">
    <div class="row">
        <h1 class="cus-h1-page-title">${pageTitle} -> ${(recursoMultimedia.nombre)!""}</h1>
        <form action="/admin/recursoMultimedia/actualizar" method="POST" enctype="multipart/form-data" >
            <input type="hidden" id="id" name="id" value="${recursoMultimedia.id}" />
            <fieldset class="form-group">
                <label for="nombre">Nombre(*)</label>
                <input type="text" class="form-control" id="nombre" name="nombre" value="${(recursoMultimedia.nombre)!""}" style="text-transform:uppercase"/>
                <label for="descripcion">Descripcion(*)</label>
                <textarea  class="form-control" id="descripcion" name="descripcion" value="${(recursoMultimedia.descripcion)!""}" style="text-transform:uppercase">${(recursoMultimedia.descripcion)!""}</textarea>
                <!--<input type="text" class="form-control" id="descripcion" name="descripcion" value="${(recursoMultimedia.descripcion)!""}" style="text-transform:uppercase"/>-->
                <label for="fuente">Fuente(*)</label>
                <input type="text" class="form-control" id="fuente" name="fuente" value="${(recursoMultimedia.fuente)!""}" style="text-transform:uppercase"/>
                <label for="pesoOrden">Peso Orden</label>
                <input type="number" class="form-control" id="pesoOrden" name="pesoOrden" value="${(recursoMultimedia.pesoOrden)!""}" min="1" max="100"/>
                <!--<label for="pesoOrden">Peso Orden</label>-->
                <input type="hidden" class="form-control" id="tematica" name="tematica" value="${(tematicasCrear.id)!""}"/>
                <label for="fuente">Archivo:</label>
                <input type="text" class="form-control" id="" name="" value="${(recursoMultimedia.nombreArchivoOriginal)!""}" disabled/>
            </fieldset>
            <div class="m-y">
                <button id="btnGuardar" type="submit" class="btn btn-primary">Guardar</button>
                <a href="/admin/recursoMultimedia/list/${tematicasEditar.id}" class="btn btn-secondary">Cancelar</a>
                </div>
            </form>
        </div>
    </div>
<#include "footer.ftl">