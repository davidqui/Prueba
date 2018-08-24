<#setting number_format="computer">
<#assign pageTitle = "Observacion Transferencia Archivo" />
<#-- <#assign mode = observacionDefecto.mode!"" /> -->
<#assign deferredJS = "" />
<#import "spring.ftl" as spring />
<#include "admin-header.ftl">
<#include "util-macros.ftl" />

<div class="container">
    <div class="row">
        <h1 class="cus-h1-page-title">Modificar ${pageTitle}</h1>
        <form action="/admin/transjustificaciondefecto/actualizar" method="POST" enctype="multipart/form-data" >
            <input type="hidden" id="tjdId" name="tjdId" value="${justificacionDefecto.tjdId}" />
            <fieldset class="form-group">
                <label for="textoObservacion">Texto</label>
                <input type="text" class="form-control" id="textoObservacion" name="textoObservacion" value="${(justificacionDefecto.textoObservacion)!""}" style="text-transform:uppercase"/>
            </fieldset>
            
            <div class="m-y">
                <button id="btnGuardar" type="submit" class="btn btn-primary">Guardar</button>
                <a href="/admin/transjustificaciondefecto" class="btn btn-secondary">Cancelar</a>
                </div>
            </form>
        </div>
    </div>
<#include "footer.ftl">