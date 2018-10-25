<#setting number_format="computer">
<#assign pageTitle = "Editar Tema de Capacitacón" />
<#assign deferredJS = "" />
<#import "spring.ftl" as spring />
<#include "admin-header.ftl">
<#include "util-macros.ftl" />

<div class="container">
    <div class="row">
        <h1 class="cus-h1-page-title">${pageTitle}</h1>
        <form action="/admin/temaCapacitacion/actualizar" method="POST" enctype="multipart/form-data" >
            <input type="hidden" id="id" name="id" value="${temaCapacitacion.id}" />
            <fieldset class="form-group">
                <label for="nombre">Tema (*)</label>
                <input type="text" class="form-control" id="tema" name="tema" value="${(temaCapacitacion.tema)!""}" />
                </fieldset>
            <div class="row">
                <div class="col-xs-8">
                    <select type="text" class="form-control" id="clasificacion" name="clasificacion" value="${(temaCapacitacion.clasificacion)!"-- Seleccione una Clasificación --"}"/>
                 <option value="">-- Seleccione una Clasificación --</option>
                    <#list clasificacions as clasificacion>
                    <option value="${clasificacion.id}">${clasificacion.nombre}</option>
                    </#list>
                    </select>
                    </div>
                </div>
            <div class="m-y">
                <button id="btnGuardar" type="submit" class="btn btn-primary">Guardar</button>
                <a href="/admin/temaCapacitacion" class="btn btn-secondary">Cancelar</a>
                </div>
            </form>
        </div>
    </div>
<#include "footer.ftl">