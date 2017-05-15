<#assign pageTitle = "Subseries " + dependencia.nombre />
<#include "admin-header.ftl">

<div class="container-fluid">
    <h1 class="cus-h1-page-title">${pageTitle}</h1>
    <p>
    <a href="/dependencias" class="btn btn-link btn-sm">Regresar</a>
    </p>

    <form method="POST">
        <#list trds as trd>
            <div class="checkbox">
              <label>
              <ul>
                <#if controller.has(trd.id, dependencia.trds)>
                   <li><input name="trd" type="checkbox" value="${trd.id}" checked="checked">${trd.nombre}</li>
                <#else>
                    <li><input name="trd" type="checkbox" value="${trd.id}">${trd.nombre}</li>
                </#if>
              </ul>
              </label>
            </div>
        </#list>
        <br /><br />
        <nav class="navbar navbar-default navbar-fixed-bottom text-xs-center hermes-bottombar">
            <button type="submit" class="btn btn-primary">Guardar</button>
        </div>
    </form>

</div>

<#include "admin-footer.ftl">