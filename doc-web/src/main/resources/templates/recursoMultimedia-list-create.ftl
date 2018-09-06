<#setting number_format="computer">
<#assign pageTitle = "Recurso Multimedia" />
<#-- <#assign mode = observacionDefecto.mode!"" /> -->
<#assign deferredJS = "" />
<#import "spring.ftl" as spring />
<#include "admin-header.ftl">
<#include "util-macros.ftl" />

<div class="container">
    <div class="row">
        <h1 class="cus-h1-page-title">Crear ${pageTitle}</h1>
        <form action="/admin/recursoMultimedia/crear" method="POST" enctype="multipart/form-data" >
            <fieldset class="form-group">
                <label for="nombre">Nombre</label>
                <input type="text" class="form-control" id="nombre" name="nombre" value="${(recursoMultimedia.nombre)!""}" style="text-transform:uppercase"/>
                <label for="descripcion">Descripcion</label>
                <input type="text" class="form-control" id="descripcion" name="descripcion" value="${(recursoMultimedia.descripcion)!""}" style="text-transform:uppercase"/>
                <label for="fuente">Fuente</label>
                <input type="text" class="form-control" id="fuente" name="fuente" value="${(recursoMultimedia.fuente)!""}" style="text-transform:uppercase"/>
                <label for="pesoOrden">Peso Orden</label>
                <input type="num" class="form-control" id="pesoOrden" name="pesoOrden" value="${(recursoMultimedia.pesoOrden)!""}"/>
                <fieldset class="form-group">          
                <@spring.bind "recursoMultimedia.tematica" />
                    <label for="${spring.status.expression}">Temática</label><br>
                    <select class="selectpicker" id="${spring.status.expression}" name="${spring.status.expression}" data-live-search="true">
                <#if tematicas??>
                        <option value=""></option>
                    <#list tematicas as temat>
	            <#if temat.id?string == ((recursoMultimedia.tematica.id)!"")?string >
                        <option value="${temat.id}" selected="selected">${temat.nombre}</option>
                    <#else>
                        <option value="${temat.id}">${temat.nombre}</option>
	            </#if>
                    </#list>
                     </#if>
                        </select>
                    </fieldset>
                <label for="fuente">Cargar Archivo</label>
                <input type="file"  name="archivo" class="form-control-file" id="files">
                
                </fieldset>
            
            <div class="m-y">
                <button id="btnGuardar" type="submit" class="btn btn-primary">Guardar</button>
                <a href="/admin/recursoMultimedia" class="btn btn-secondary">Cancelar</a>
                </div>
            </form>
        </div>
    </div>
<#include "footer.ftl">