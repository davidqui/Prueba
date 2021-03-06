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
                <#--
                    2018-05-09 jgarcia@controltechcg.com Issue #160 (SICDI-Controltech)
                    feature-160: Presentación del nombre del rol, en vez del código
                    del rol.
                -->
                <#if controller.has(rol.id, perfil.roles)>
                <input name="role" type="checkbox" value="${rol.id}" checked="checked">${rol.nombre}
                <#else>
                <input name="role" type="checkbox" value="${rol.id}">${rol.nombre}
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
