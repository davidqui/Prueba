<#assign pageTitle = "Plantillas" />
<#assign activePill = "plantilla" />
<#include "admin-header.ftl">

<div class="container-fluid">
    <h1 class="cus-h1-page-title">${pageTitle}</h1>
    <p>
        <a href="/admin/plantilla/new" class="btn btn-success btn-sm">Crear nuevo</a>
    </p>

    Total ${list?size}

    <#if all?? && all>
        <a class="btn btn-link" href="?all=false">No mostrar eliminados</a>
    <#else>
        <a class="btn btn-link" href="?all=true">Mostrar eliminados</a>
    </#if>
    <table class="table">
        <thead class="thead-inverse">
            <tr>
            	<th>Nombre</th>
                <th>Acciones</th>
            </tr>
        </thead>
        <tbody>
        <#list list as x>
            <#assign vclasses = "" />
            <#if !x.activo?? || x.activo == false >
                <#assign vclasses = "cus-deleted" />
            </#if>
            <tr>
            	<td class="${vclasses}">${x.nombre!"Sin descripción"}</td>
            	<td nowrap="nowrap">
                    <#if x.activo?? && x.activo == true >
                        <a href=${"/admin/plantilla/edit?id=" + x.id} class="btn btn-sm btn-warning" title="Modificar">M</a>
                        <a href=${"/admin/plantilla/delete?id=" + x.id} class="btn btn-sm btn-danger" title="Eliminar" onclick="return confirm('¿Está seguro que desea eliminar el registro?');">E</a>
                    </#if>
                    <#if x.cuando?? >
                        <#if x.cuandoMod?? >
                            <a tabindex="0" class="btn btn-sm btn-primary bd-popover" role="button" data-toggle="popover" data-trigger="hover" data-placement="left" title="Historial" data-content="Creado por: ${x.quien} el día ${x.cuando}, Modificado por: ${x.quienMod!"Nadie"} el día ${x.cuandoMod!"Ninguno"}">H</a>
                        <#else>
                            <a tabindex="0" class="btn btn-sm btn-primary bd-popover" role="button" data-toggle="popover" data-trigger="hover" data-placement="left" title="Historial" data-content="Creado por: ${x.quien} el día ${x.cuando}">H</a>
                        </#if>
                    </#if>
            	</td>
            </tr>
        </#list>
        </tbody>
    </table>
</div>

<#include "footer.ftl">
