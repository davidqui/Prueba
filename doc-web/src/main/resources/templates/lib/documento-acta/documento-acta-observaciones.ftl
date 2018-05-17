<!--
    2018-05-17 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
    feature-162: Macro para la presentación de campo y visualización de
    observaciones de documentos.
-->
<#macro presentarObservaciones documento utilController estadoModo idAreaObservacion >
    <div class="card m-y">                           		        
        <#if (documento.observaciones)??>
        <div class="card-block" id="obsDiv">
            <h5>Observaciones</h5>
            <#list documento.observaciones as observacion >
            <hr/>
            <strong>${utilController.nombre(observacion.quien)}</strong>, <em> ${observacion.cuando?string('yyyy-MM-dd hh:mm a:ss')}</em>
            <p>${observacion.texto}</p>
            </#list>
        </div>
        </#if>

        <#if estadoModo == "EDICION_INFORMACION" || estadoModo == "CARGA_ACTA_DIGITAL">
        <div class="card-block cus-gray-bg">
            <fieldset class="form-group">
                <textarea class="form-control" id="${idAreaObservacion}" name="${idAreaObservacion}"></textarea>
            </fieldset>
            <#assign obsButtonID = "obs-button" />
            <button class="btn btn-secondary btn-sm" id="${obsButtonID}" name="${obsButtonID}" onclick="enviarComentario('${idAreaObservacion}', '${documento.id}')">Comentar</button>
            <span id="msg-enviar-comentario"></span>
        </div>
        </#if>
    </div>
</#macro>