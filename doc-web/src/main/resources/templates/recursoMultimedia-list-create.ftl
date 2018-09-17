<#setting number_format="computer">
<#assign pageTitle = "Recurso Multimedia" />
<#assign deferredJS = "" />
<#import "spring.ftl" as spring />
<#include "admin-header.ftl">
<#include "util-macros.ftl" />

<div class="container">
    <div class="row">
        <h1 class="cus-h1-page-title">Crear ${pageTitle} -> <b>${tematicasCrear.nombre?capitalize}</b></h1>
        <form action="/admin/recursoMultimedia/crear" method="POST" enctype="multipart/form-data" >
            <fieldset class="form-group">
                <label for="nombre">Nombre(*)</label>
                <input type="text" class="form-control" id="nombre" name="nombre" value="${(recursoMultimedia.nombre)!""}" style="text-transform:uppercase"/>
                <label for="descripcion">Descripcion(*)</label>
                <input type="text" class="form-control" id="descripcion" name="descripcion" value="${(recursoMultimedia.descripcion)!""}" style="text-transform:uppercase"/>
                <label for="fuente">Fuente(*)</label>
                <input type="text" class="form-control" id="fuente" name="fuente" value="${(recursoMultimedia.fuente)!""}" style="text-transform:uppercase"/>
                <label for="pesoOrden">Peso Orden</label>
                <input type="number" class="form-control" id="pesoOrden" name="pesoOrden" value="${(recursoMultimedia.pesoOrden)!""}" min="1" max="100"/>
                <!--<label for="pesoOrden">Peso Orden</label>-->
                <input type="hidden" class="form-control" id="tematica" name="tematica" value="${(tematicasCrear.id)!""}"/>
                <label for="fuente">Cargar Archivo(*)</label>
                <input type="file"  name="archivo" class="form-control-file" id="files" required>
                
                </fieldset>

            <div class="m-y">
                <button id="btnGuardar" type="submit" class="btn btn-primary">Guardar</button>
                <a href="/admin/recursoMultimedia/list/${tematicasCrear.id}" class="btn btn-secondary">Cancelar</a>
                </div>
            </form>
        </div>
    </div>
<#include "footer.ftl">