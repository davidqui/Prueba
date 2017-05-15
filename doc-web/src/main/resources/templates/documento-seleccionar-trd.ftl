<#assign pageTitle = documento.asunto!"Documento" />

<#import "spring.ftl" as spring />

<#include "bandeja-header.ftl" />

<h1>TRD del documento de respuesta</h1>

<p>Seleccione el tipo de documento que va a crear según la Tabla de Retención Documental. 
Esta selección establece la plantilla que se usará para el documento</p>

<form action="/documento/crear-respuesta?pin=${instancia.id}&tid=${tid}" method="POST">
    <#list trds as x>
        <#if x.serie??>
            <label class="c-input c-radio">
                <input type="radio" name="trd" value="${x.id}">
                <span class="c-indicator"></span>
                ${x.codigo} ${x.nombre} <#if x.tipoDocumento??><span class="label label-info">${(x.tipoDocumento.nombre)!""}</span></#if>
            </label>
        <br/>
        <#else>
            <br/>
            <h4>${x.codigo} ${x.nombre}</h4>
        </#if>
    </#list>
    <br/>
    <br/>
    <button type="submit" class="btn btn-success">Seleccionar</button>
    <a href="/documento?pin=${instancia.id}" class="btn btn-link">Cancelar</a>
</form>

<#include "bandeja-footer.ftl" />