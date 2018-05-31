<#setting number_format="computer">
<#assign pageTitle = "Notificaciones" />
<#-- <#assign mode = notificacion.mode!"" /> -->
<#assign deferredJS = "" />
<#import "spring.ftl" as spring />
<#include "admin-header.ftl">
<#include "util-macros.ftl" />

<div class="container">
    <div class="row">
        <h1 class="cus-h1-page-title">${pageTitle}</h1>
        <form action="/admin/notificacion/actualizar" method="POST" enctype="multipart/form-data" >
            <fieldset class="form-group">
                <label for="trd">Tipo de Notificación</label>
                <select id="tipo-notificacion-form" name="tipoNotificacion" class="form-control input-sm" onchange="cambioTipoNotificacion(${(notificacion.tipoNotificacion.id)!""})">
                    <option value="">--------</option>
                    <#list tiposNotificaciones as tipoNotificacion>
                        <#if tipoNotificacion.id?string == ((notificacion.tipoNotificacion.id)!"")?string >
                            <option value="${tipoNotificacion.id}" selected="selected">${tipoNotificacion.nombre}</option>
                        <#else>
                            <option value="${tipoNotificacion.id}">${tipoNotificacion.nombre}</option>
                        </#if>
                    </#list>
                </select>
            </fieldset>
            <fieldset class="form-group">
                <label for="textoObservacion">Clasificación</label>
                <select id="clas-notificacion-form" name="clasificacion" class="form-control input-sm">
                    <option value="">--------</option>
                    <#list clasificaciones as cla>
                    <#if cla.id?string == ((notificacion.clasificacion.id)!"")?string >
                        <option value="${cla.id}" selected="selected">${cla.nombre}</option>
                    <#else>
                        <option value="${cla.id}">${cla.nombre}</option>
                    </#if>
                    </#list>
                </select>
            </fieldset>
            <label for="textoObservacion">Template</label>
            <#if notificacion.tipoNotificacion??>
                <fieldset class="form-group">
                    <div style="display: flex;">
                        <textarea id="text-area-notificacion" class="form-control" name="template" rows="20" style="width: 70%;">${(notificacion.template)!""}</textarea>
                        <div class="list-group notificacion-wildcard-selector" style="width: 30%; max-height: 480px; overflow: hidden; overflow-y: auto;">
                            <#list notificacion.tipoNotificacion.wildCards as wildcard>
                                <a class="list-group-item list-group-item-action" href="javascript:addTextArea('${wildcard.valor}');">${wildcard.nombre}</a>
                            </#list>
                        </div>
                    </div>
                </fieldset>
            </#if>
            <div class="m-y">
                <button id="btnGuardar" type="submit" class="btn btn-primary">Guardar</button>
                <a href="/admin/notificacion" class="btn btn-secondary">Cancelar</a>
            </div>
        </form>
        <script>
            function addTextArea(value) {
                var textarea = document.getElementById("text-area-notificacion")
                var val = textarea.value;
                var part1 = val.substring(0, val.slice(0, textarea.selectionStart).length);
                var part2 = val.substring(val.slice(0, textarea.selectionStart).length, val.length);
                textarea.value = part1+"$"+"{"+value+"}" + part2;
            }
                
            function cambioTipoNotificacion(change) {
                if (change) {
                    var r = confirm("Si cambia el tipo de notificación se borrara el texto del témplate.");
                    if (r == true) {
                        redirectToCreate();
                    }else{
                        document.getElementById("tipo-notificacion-form").value = change;
                    }
                }else{
                   redirectToCreate();
                }
            }
            
            function redirectToCreate(){
                var v_tipo = document.getElementById("tipo-notificacion-form").value;
                var v_clas = document.getElementById("clas-notificacion-form").value;
                
                if (v_tipo) {
                    window.location.href = 
                    '/admin/notificacion/create?tipoNotificacion='+v_tipo+
                    '&clasificacion='+v_clas;    
                }
            }
        </script>
    </div>
</div>
<#include "footer.ftl">