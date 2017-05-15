<#assign pageTitle = "Roles perfil " + perfil.nombre />
<#include "admin-header.ftl">

<div class="container-fluid">
    <h1 class="cus-h1-page-title">${pageTitle}</h1>
    <p>
    <a href="/admin/profiles" class="btn btn-link btn-sm">Regresar</a>
    </p>

    <form method="POST">
        <#list roles as rol>
            <div class="checkbox">
              <label>
                <#if controller.has(rol.id, perfil.roles)>
                    <input name="role" type="checkbox" value="${rol.id}" checked="checked">${rol.id}
                <#else>
                    <input name="role" type="checkbox" value="${rol.id}">${rol.id}
                </#if>
              </label>
            </div>
        </#list>
        <div class="m-y">
            <button type="submit" class="btn btn-primary">Guardar</button>
        </div>
    </form>

</div>

<#include "admin-footer.ftl">
