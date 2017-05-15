<#assign pageTitle = "Informes" />
<#include "header.ftl" />

<div class="container-fluid">

<h1>${pageTitle}</h1>

<table class="table">
    <thead>
        <tr>
            <th>Nombre</th>
            <th>Descripción</th>
        </tr>
    </thead>
    <tbody>
    <#list informes as x>
        <tr>
        <td><a href="/informes/informe?id=${x.id}">${x.nombre}</a></td>
        <td>${x.descripcion}</td>
        </tr>
    </#list>
    </tbody>
</table>

</div>

<#include "footer.ftl" />