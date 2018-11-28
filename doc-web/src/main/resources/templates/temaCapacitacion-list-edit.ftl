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
                    <fieldset class="form-group">
                <label for="clasificacion">Clasificación (*)</label>
                <select id="clas-notificacion-form" name="clasificacion" class="form-control input-sm">
                    <option value="">--------</option>
                    <#list clasificacions as cla>
                    <#if cla.id?string == ((temaCapacitacion.clasificacion.id)!"")?string >
                        <option value="${cla.id}" selected="selected">${cla.nombre}</option>
                    <#else>
                        <option value="${cla.id}">${cla.nombre}</option>
                    </#if>
                    </#list>
                </select>
            </fieldset>
            <div class="m-y">
                <button id="btnGuardar" type="submit" class="btn btn-primary">Guardar</button>
                <a href="/admin/temaCapacitacion" class="btn btn-secondary">Cancelar</a>
                </div>
            </form>
        </div>
    </div>
<#include "footer.ftl">