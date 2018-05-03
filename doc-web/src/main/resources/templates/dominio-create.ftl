<#setting number_format="computer">
<#assign pageTitle = dominio.nombre!"Dominio" />
<#assign mode = dominio.mode!"" />
<#assign deferredJS = "" />
<#import "spring.ftl" as spring />
<#include "admin-header.ftl">
<#include "util-macros.ftl" />

<div class="container">
    <div class="row">
        <h1 class="cus-h1-page-title">${pageTitle}</h1>
	<@flash/>
        <form action="/admin/dominio/crear" method="POST" enctype="multipart/form-data" >
            <fieldset class="form-group">
                <label for="codigo">Código</label>
                <input type="text" class="form-control" id="codigo" name="codigo" value="${(dominio.codigo)!""}"/>
                </fieldset>

            <fieldset class="form-group">
                <label for="nombre">Nombre</label>
                <input type="text" class="form-control" id="nombre" name="carNombre" value="${(dominio.nombre)!""}"/>
                </fieldset>
            
            <fieldset class="form-group">
                <label for="descripcion">Descripcion</label>
                <input type="text" class="form-control" id="descripcion" name="descripcion" value="${(dominio.descripcion)!""}"/>
                </fieldset>
            
            <fieldset class="form-group">
                <label for="visualizaLinkOWA">Permitir link OWA</label>
                <input class="form-control" type="checkbox" name="visualizaLinkOWA" id="${dominio.visualizaLinkOWA!"false"}" />
            </fieldset>

            <div class="m-y">
                <button id="btnGuardar" type="submit" class="btn btn-primary">Guardar</button>
                <a href="/admin/dominio" class="btn btn-secondary">Cancelar</a>
                </div>
            </form>
        </div>
    </div>
<#include "footer.ftl">