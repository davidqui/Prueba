<#assign pageTitle = "Prestamos"/>
<#include "archivo-expedientes-tabs.ftl">
<#include "util-macros.ftl" />

</br>
    
    <#if !exPrestados??>
    <div class="jumbotron">
        <h1 class="display-1">No hay registros</h1>
        <p class="lead">En este momento no existen expedientes prestados en el archivo</p>
    </div>
    <#else>
    <p>A continuación se muesta la lista de los expedientes que se encuentran prestados. </p>
    <br>
    <table class="table">
        <thead>
            <tr>
            	
                <th>Nombre Expediente</th>
                <th>Cógido Expediente</th>
                <th>Fecha prestamo</th>
                <th>Fecha devolución</th>
                <th>Dependencia</th>
                <th>Funcionario</th>
                <th>Acciones</th>
            </tr>
        </thead>
        <tbody>
            <#list exPrestados as exPre>
            <tr>

                <td><a href="/prestamo/contenido?eid=${exPre.expediente.id}">${exPre.expediente.nombre}</a></td>
                <td nowrap>${exPre.expediente.codigo}</td>
                <td nowrap>${exPre.prestamo.fechaPrestamo?string('yyyy-MM-dd')}</td>
                <td nowrap>${exPre.prestamo.fechaDevolucion?string('yyyy-MM-dd')}</td>
                <td>${exPre.prestamo.usuarioSolicita.dependencia.nombre}</td>
                <td>${exPre.prestamo.usuarioSolicita}</td>
                
                <td nowrap><a id="ver-prestamo" href="#" onclick="location.href='/prestamo/update-prestamo?eid=${exPre.expediente.id}';" tabindex="0" class="btn btn-sm btn-danger bd-popover" role="button"data-toggle="popover" data-trigger="hover" data-placement="left" title="Documento pestado" data-content="Pulse para visualizar y/o modificar el prestamo de este documento">P</a></td>
                
            </tr>
            </#list>
        </tbody>
    </table>
    </#if>
</div>
<#include "bandeja-footer.ftl">