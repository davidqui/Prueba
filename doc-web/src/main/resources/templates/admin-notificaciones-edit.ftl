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
            <input type="hidden" id="id" name="id" value="${notificacion.id}" />
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
                        <div style="width: 70%;">
                            <input type="text" class="form-control" id="asunto" name="asunto" value="${(notificacion.asunto)!""}" placeholder="Asunto" disabled/>
                            <textarea id="text-area-notificacion" class="form-control" name="template" rows="20" placeholder="Template" disabled>${(notificacion.template)!""}</textarea>
                        </div>
                        
                        <div class="list-group notificacion-wildcard-selector" style="width: 30%; max-height: 524px; overflow: hidden; overflow-y: auto;">
                            <#list notificacion.tipoNotificacion.wildCards as wildcard>
                                <a class="list-group-item list-group-item-action" href="javascript:addTextArea('${wildcard.valor}');">${wildcard.nombre}</a>
                            </#list>
                        </div>
                    </div>
                </fieldset>
            </#if>
            <div class="m-y">
                <button id="btnEnviar" type="button" class="btn btn-default" data-toggle="modal" data-target="#sendTestModal" data-whatever="@${notificacion.id}" style="margin-right: 30px;">Enviar Test</button>
                <button id="btnGuardar" type="submit" class="btn btn-primary" style="display:none;">Guardar</button>
                <button id="btnHedicion" type="button" class="btn btn-primary" data-toggle="modal" data-target="#enviarTest">Habilitar Edición</button>
                <a href="/admin/notificacion" class="btn btn-secondary">Cancelar</a>
            </div>
        </form>
        <script type="text/javascript">
            function addTextArea(value) {
                var textarea = document.getElementById("text-area-notificacion");
                if(!textarea.disabled){
                    var val = textarea.value;
                    var part1 = val.substring(0, val.slice(0, textarea.selectionStart).length);
                    var part2 = val.substring(val.slice(0, textarea.selectionStart).length, val.length);
                    textarea.value = part1+"$"+"{("+value+")!\"\"}" + part2;
                }
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
            
            function habilitarEdicion(){
                var textarea = document.getElementById("text-area-notificacion");
                var input = document.getElementById("asunto");
                var senTest = document.getElementById("btnEnviar");
                
                var btnGuardar = document.getElementById("btnGuardar");
                var btnEditar = document.getElementById("btnHedicion");
                
                textarea.disabled = false;
                input.disabled = false;
                senTest.disabled = true;
                
                btnGuardar.style.display = "initial";
                btnEditar.style.display = "none";
            }
        </script>
    </div>
</div>

<!-- Modal Enviar test-->
<div class="modal fade" id="sendTestModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="exampleModalLabel">Test email</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close" style="position:absolute;right: 10px;top: 5px;">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <form action="/admin/notificacion/testmail" method="GET">
        <div class="modal-body">
        <div class="form-group" style="display:none">
          <label for="recipient-name" class="col-form-label">Recipient:</label>
          <input type="text" class="form-control" id="id" name="id" value="${notificacion.id}">
        </div>
        <div class="form-group">
          <label for="recipient-name" class="col-form-label">Enviar a:</label>
          <input type="text" class="form-control" id="mail" name="mail" placeholder="mail@mail.com">
        </div>
        </div>
        <div class="modal-footer">
          <button type="submit" class="btn btn-primary">Enviar Prueba</button>
        </div>
      </form>
    </div>
  </div>
</div>

<!-- Modal Aviso-->
<div class="modal fade" id="enviarTest" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="exampleModalLongTitle" style="color:red;">Advertencia</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close" style="position:absolute;right: 10px;top: 5px;">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
        Señor Usuario:
Tenga en cuenta que para editar el contenido de esta plantilla, usted debe tener conocimientos básicos sobre HTML5 y CCS; además debe tener en cuenta que los cambios realizados afectaran las notificaciones globales del sistema que dependan de esta plantilla. 
</br>
Los cambios realizados serán objeto de auditoria por parte del sistema, generando con ello que se establezca claramente quien ha generado modificaciones sobre esta plantilla. 
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancelar</button>
        <button type="button" class="btn btn-primary" onclick="habilitarEdicion()" data-dismiss="modal">Aceptar</button>
      </div>
    </div>
  </div>
</div>
<#include "footer.ftl">