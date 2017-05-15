<#assign pageTitle = "Prestamos"/>
<#include "archivo-documentos-tabs.ftl">
<#include "util-macros.ftl" />

</br>
    
    <#if !docArchivados??>
    <div class="jumbotron">
        <h1 class="display-1">No hay registros</h1>
        <p class="lead">En este momento no existen documentos archivados.</p>
    </div>
    <#else>
    <p>A continuación se muesta la lista de los documentos que se encuentran archivados. </p>
    <br>
    <table class="table">
        <thead>
            <tr>
            	
                <th>Documento (Asunto)</th>
                <th>Expediente</th>
                <th>Serie (TRD)</th>
                <th>Acciones</th>
            </tr>
        </thead>
        <tbody>
            <#list docArchivados as docsArc>
            <tr>
                <td><a href="/documento?pin=${docsArc.instancia.id}&archivoHeader='archivoHeader'">${docsArc.asunto}</a></td>
                <td nowrap>${docsArc.expediente.nombre}</td>
                <#if docsArc.trd ??>
                <td>${docsArc.trd.nombre}</td>
                </#if>
                <#if !docsArc.prestado?? || docsArc.prestado==false>
                	<td nowrap><a id="ver-prestamo" href="#" onclick="location.href='#';" tabindex="0" class="btn btn-sm btn-success bd-popover" role="button"data-toggle="popover" data-trigger="hover" data-placement="left" title="Documento pestado" data-content="Pulse para visualizar y/o modificar el prestamo de este documento">P</a></td>
                <#else>
                	<td nowrap><a id="ver-prestamo" href="#" onclick="location.href='#';" tabindex="0" class="btn btn-sm btn-danger bd-popover" role="button"data-toggle="popover" data-trigger="hover" data-placement="left" title="Documento pestado" data-content="Pulse para visualizar y/o modificar el prestamo de este documento">P</a></td>	
                </#if>
                
            </tr>
            </#list>
        </tbody>
    </table>
    </#if>
</div>
<#include "bandeja-footer.ftl">