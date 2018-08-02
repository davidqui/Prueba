<#setting number_format="computer">
<#assign pageTitle = descriptor.label />
<#include "gen-macros.ftl">
<#if templatePrefix??>
    <#include templatePrefix + "-header.ftl">
<#else>
    <#include "admin-header.ftl">
</#if>

<div class="container-fluid">
    <h1 class="cus-h1-page-title">${pageTitle}</h1>
    <#if descriptor.description??>
    <p class="lead">${descriptor.description}</p>
    </#if>
    <p>
        <#if returnUrl??><a href="${returnUrl}" class="btn btn-secondary btn-sm">Regresar</a></#if>
        <a href="/admin/parnombrexpediente/create" class="btn btn-success btn-sm">Crear nuevo</a>
        </p>

    Total ${list?size}
    <table class="table">
        <thead class="thead-inverse">
            <tr>
                <#list descriptor.listProperties() as p>
                <th>${p.label}</th>
                </#list>
                <th>Acciones</th>
                </tr>
            </thead>
        <tbody>
            <#list list as x>
            <tr>
                <#assign first = true />
                <#list descriptor.listProperties() as p>
                    <#assign vclasses = "" />
                <td class="${vclasses}">
                        <#if p.type == "Boolean">
                            <#assign pvalue = p.value(x)!false />
                    <div>${pvalue?string("X", "")}</div>
                        <#else>
                    <div>${p.value(x)!""}</div>
                        </#if>
                        <#if first && descriptor.actions?size != 0 >
                    <div>
                                <#list descriptor.actions as a>
                                    <@accion a x />
                                </#list>
                        </div>
                        </#if>
                        <#assign first = false />
                    </td>
                </#list>
                <td nowrap="nowrap">
                    <a href="/admin/parnombrexpediente/edit?id=${x.parId}" class="btn btn-sm btn-warning" title="Modificar">M</a>
                        <#if x.fecCreacion?? >
                            <#if x.fecModificacion?? >
                    <a tabindex="0" class="btn btn-sm btn-primary bd-popover" role="button" data-toggle="popover" data-trigger="hover" data-placement="left" title="Historial" data-content="Creado por: ${x.usuCreacion!"Sistema"} el día ${x.fecModificacion?string('dd.MM.yyyy HH:mm:ss')}, Modificado por: ${x.usuModificacion!"Nadie"} el día ${x.fecModificacion?string('dd.MM.yyyy HH:mm:ss')!"Ninguno"}">H</a>
                            <#else>
                    <a tabindex="0" class="btn btn-sm btn-primary bd-popover" role="button" data-toggle="popover" data-trigger="hover" data-placement="left" title="Historial" data-content="Creado por: ${x.usuCreacion} el día ${x.fecModificacion?string('dd.MM.yyyy HH:mm:ss')}">H</a>
                            </#if>
                        </#if>
                    </td>
                </tr>
            </#list>
            </tbody>
        </table>

    </div>

<#if templatePrefix??>
    <#include templatePrefix + "-footer.ftl">
<#else>
    <#include "footer.ftl">
</#if>