<#assign pageTitle = "TRANSFERENCIA DE ARCHIVOS" />
<#assign deferredJS = "" />

<#import "spring.ftl" as spring />
<#include "header.ftl" />
<#include "lib/transferencia-archivo_functions.ftl" />

<div class="container-fluid">
    <h4>${pageTitle}</h4>

    <div class="container-fluid">
        <form action="procesar" method="POST">

            Confirma la aplicación y procesamiento de la siguiente transferencia de archivo?

            <table class="table table-striped">
                <tbody>
                    <tr>
                        <td><strong>Usuario origen:</strong></td>
                        <td>${getUsuarioDescripcion(origenUsuario)}</td>
                        </tr>    
                    <tr>
                        <td><strong>Usuario destino:</strong></td>
                        <td>${getUsuarioDescripcion(destinoUsuario)}</td>
                        </tr>    
                    <tr>
                        <td><strong>Tipo de transferencia:</strong></td>
                        <td>${getTipoDescripcion(tipoTransferencia)}</td>
                        </tr>    
                    <#if tipoTransferencia == "T" >
                    <tr>
                        <td><strong><i>Fecha aplicación transferencia seleccionada:</strong></i></td>
                        <td><i>TODO</i></td>
                        </tr>                        
                    <tr>
                        <td><strong><i>Usuario origen transferencia seleccionada:</strong></i></td>
                        <td><i>TODO</i></td>
                        </tr>                        
                    </#if>
                    <tr>
                        <td><strong>Número de documentos:</strong></td>
                        <td>TODO</td>
                        </tr> 

                    </tbody>
                </table>

            <div class="text-center">
                <button id="btn-ok" name="btn-ok" type="submit" class="btn btn-success btn-lg">Aceptar</button>
                <button id="btn-cancel" name="btn-cancel" type="submit" class="btn btn-lg">Cancelar</button>
                </div>

            <input type="hidden" id="origenUsuario" name="origenUsuario" value="442"/>
            <input type="hidden" id="destinoUsuario" name="destinoUsuario" value="1147"/>
            <input type="hidden" id="tipoTransferencia" name="tipoTransferencia" value="T"/>

            </form>
        </div>

    </div>
<#include "footer.ftl" />
