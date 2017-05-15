<#assign pageTitle = "Contenido " + expediente.nombre />
<#include "admin-header.ftl">

<div class="container-fluid">
    <h1 class="cus-h1-page-title">${pageTitle}</h1>
    <p>
    <a href="/admin/expediente" class="btn btn-link btn-sm">Regresar</a>
    </p>

    <ol>
    <#list documentos as doc>
        <li>
            <a href="/documento?pin=${doc.instancia.id}">${doc.asunto}</a>
            <#if doc.adjuntos?? && (doc.adjuntos?size > 0) >
                <ol>
                <#list doc.adjuntos as adj>
                    <li><a href="/documento/adjunto?doc=${doc.id}&dad=${adj.id}">${adj.tipologia.nombre} - ${adj.original}</a>
                </#list>
                </ol>
            </#if>
        </li>
    </#list>
    </ol>
</div>

<#include "admin-footer.ftl">
