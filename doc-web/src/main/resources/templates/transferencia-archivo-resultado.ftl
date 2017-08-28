<#assign pageTitle = "TRANSFERENCIA DE ARCHIVOS" />
<#assign deferredJS = "" />

<#import "spring.ftl" as spring />
<#include "header.ftl" />

<div class="container-fluid">
    <h4>${pageTitle}</h4>

    <div class="container-fluid">
        <form action="crear" method="GET">

            <div class="text-center">
                <button id="btn-ok" type="submit" class="btn btn-success btn-lg">Cerrar</button>
                </div>

            </form>
        </div>

    </div>
<#include "footer.ftl" />
