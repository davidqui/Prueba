<#assign pageTitle = "Prestamos"/>
<#include "expediente-prestamo-tabs.ftl">
<#include "util-macros.ftl" />

</br>
    
    <#if !docPrestados??>
    <div class="jumbotron">
        <h1 class="display-1">No hay registros</h1>
        <p class="lead">En este momento no existen documentos prestados en el archivo</p>
    </div>
    <#else>
    <br>
    <table class="table">
        <thead>
            <tr>
            	
                <th>Documento (Asunto)</th>
                <th>Fecha prestamo</th>
                <th>Fecha devolución</th>
                <th>Solicitante</th>
                <th>Dependencia</th>
                <th>Funcionario</th>
                <th>Acciones</th>
            </tr>
        </thead>
        <tbody>
            <#list docPrestados as docsPre>
            <tr>

                <td><a href="/documento?pin=${docsPre.documento.instancia.id}">${docsPre.documento.asunto}</a></td>
                <td nowrap>${docsPre.prestamo.fechaPrestamo?string('yyyy-MM-dd')}</td>
                <td nowrap>${docsPre.prestamo.fechaDevolucion?string('yyyy-MM-dd')}</td>
                <td>${docsPre.prestamo.usuarioSolicita}</td>
                <td>${docsPre.prestamo.usuarioSolicita.dependencia.nombre}</td>
                <td>${docsPre.prestamo.funcionarioPresta.nombre}</td>
                <td nowrap><a id="ver-prestamo" href="#" onclick="location.href='/prestamo/update-prestamo?eid=${docsPre.documento.expediente}';" tabindex="0" class="btn btn-sm btn-danger bd-popover" role="button"data-toggle="popover" data-trigger="hover" data-placement="left" title="Documento pestado" data-content="Pulse para visualizar y/o modificar el prestamo de este documento">P</a></td>
                
            </tr>
            </#list>
        </tbody>
    </table>
    </#if>
</div>
<#include "bandeja-footer.ftl">