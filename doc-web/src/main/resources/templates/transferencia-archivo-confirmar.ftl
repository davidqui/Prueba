<#assign pageTitle = "TRANSFERENCIA DE ARCHIVOS" />
<#assign deferredJS = "" />

<#import "spring.ftl" as spring />
<#include "header.ftl" />

<div class="container-fluid">
    <h4>${pageTitle}</h4>

    <div class="container-fluid">
        <form action="procesar" method="POST">

            <div class="text-center">
                <button id="btn-ok" type="submit" class="btn btn-success btn-lg">Aceptar</button>
                <button id="btn-cancel" type="submit" class="btn btn-lg">Cancelar</button>
                </div>

            <input type="hidden" id="origenUsuario" name="origenUsuario" value="442"/>
            <input type="hidden" id="destinoUsuario" name="destinoUsuario" value="1147"/>
            <input type="hidden" id="tipoTransferencia" name="tipoTransferencia" value="T"/>

            </form>
        </div>

    </div>
<#include "footer.ftl" />
