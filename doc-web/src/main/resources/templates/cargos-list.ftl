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
        <a href="/admin/cargos/create" class="btn btn-success btn-sm">Crear nuevo</a>
    </p>

    Total ${list?size}

    <#if descriptor.properties?size == 0>
        <div class="jumbotron">
            <h1>No hay propiedades configuradas</h1>
            <p>Por alguna razón la lista de propiedades está vacía. Por lo general es automático el reconocimiento de propiedades a menos que todas las propiedades estén marcadas como @Transient.</p>
        </div>
    <#else>
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
                        <#if x.carIndLdap?? && x.carIndLdap == false >
                            <a href=${"/admin/cargos/edit?id=" + x.id} class="btn btn-sm btn-warning" title="Modificar">M</a>
                        </#if>
                        <#if x.cuando?? >
                            <#if x.cuandoMod?? >
                                <a tabindex="0" class="btn btn-sm btn-primary bd-popover" role="button" data-toggle="popover" data-trigger="hover" data-placement="left" title="Historial" data-content="Creado por: ${x.quien!"Sistema"} el día ${x.cuando}, Modificado por: ${x.quienMod!"Nadie"} el día ${x.cuandoMod!"Ninguno"}">H</a>
                            <#else>
                                <a tabindex="0" class="btn btn-sm btn-primary bd-popover" role="button" data-toggle="popover" data-trigger="hover" data-placement="left" title="Historial" data-content="Creado por: ${x.quien} el día ${x.cuando}">H</a>
                            </#if>
                        </#if>
                	</td>
                </tr>
            </#list>
            </tbody>
        </table>
        <#if totalPages gt 0>
            <@printBar url="/admin/cargos" params={"filtro": filtro!""} metodo="get"/>
        </#if>
    </#if>

</div>

<#if templatePrefix??>
    <#include templatePrefix + "-footer.ftl">
<#else>
    <#include "footer.ftl">
</#if>