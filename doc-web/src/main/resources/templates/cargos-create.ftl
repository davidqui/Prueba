<#setting number_format="computer">
<#assign pageTitle = cargo.nombre!"Cargo" />
<#assign mode = cargo.mode!"" />
<#assign deferredJS = "" />
<#import "spring.ftl" as spring />
<#include "admin-header.ftl">
<#include "util-macros.ftl" />

<div class="container">
    <div class="row">
        <h1 class="cus-h1-page-title">${pageTitle}</h1>
	<@flash/>
        <form action="/admin/cargos/save" method="POST" enctype="multipart/form-data" >
            <#if cargo.id??>
                <input type="hidden" name="id" id="id" value="${cargo.id}" />
	    </#if>
            <fieldset class="form-group">
                <label for="id">Nombre</label>
                <input type="text" class="form-control" id="carNombre" name="carNombre" value="${(cargo.carNombre)!""}"/>
                </fieldset>

            <div class="m-y">
                <button id="btnGuardar" type="submit" class="btn btn-primary">Guardar</button>
                <a href="/admin/cargos" class="btn btn-secondary">Cancelar</a>
                </div>
            </form>
        </div>
    </div>
<#include "footer.ftl">