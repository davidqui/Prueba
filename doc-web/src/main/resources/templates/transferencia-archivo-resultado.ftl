<#setting number_format="computer">

<#assign pageTitle = "TRANSFERENCIA DE ARCHIVOS" />
<#assign deferredJS = "" />

<#import "spring.ftl" as spring />
<#include "header.ftl" />
<#include "lib/transferencia-archivo_functions.ftl" />

<div class="container-fluid">
    <h4>${pageTitle}</h4>

    <div class="container-fluid">

        Se ha realizado la siguiente transferencia de archivos

        <table class="table table-striped">
            <tbody>
                <tr>
                    <td><strong>Usuario origen:</strong></td>
                    <td>${getUsuarioDescripcion(nuevatransferencia.origenUsuario)}</td>
                    </tr>    
                <tr>
                    <td><strong>Usuario destino:</strong></td>
                    <td>${getUsuarioDescripcion(nuevatransferencia.destinoUsuario)}</td>
                    </tr>    
                <tr>
                    <td><strong>Tipo de transferencia:</strong></td>
                    <td>${getTipoDescripcion(nuevatransferencia.tipo)}</td>
                    </tr>    
                    <#if nuevatransferencia.tipo == "P" >
                <tr>
                    <td><strong><i>Fecha aplicación transferencia seleccionada:</strong></i></td>
                    <td><i>${transferenciaAnterior.fechaAprobacion?string('yyyy-MM-dd hh:mm:ss a')}</i></td>
                    </tr>                        
                <tr>
                    <td><strong><i>Usuario origen transferencia seleccionada:</strong></i></td>
                    <td><i>${getUsuarioDescripcion(transferenciaAnterior.origenUsuario)}</i></td>
                    </tr>                         
                    </#if>
                <tr>
                    <td><strong>Número de documentos:</strong></td>
                    <td>${nuevatransferencia.numeroDocumentos}</td>
                    </tr> 
                <#if nuevatransferencia.actaOFS?? >
                <tr>
                    <td colspan="2"><strong><a href="/ofs/download/${nuevatransferencia.actaOFS}" >Descargar Acta</a></strong></td>
                    </tr>
                </#if>
                </tbody>
            </table>

        <div class="text-center">
            <a href="/transferencia-archivo/crear" class="btn btn-secondary btn-lg">Cerrar</a>
            </div>
        </div>
    </div>
<#include "footer.ftl" />
