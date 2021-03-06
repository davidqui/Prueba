<#--
    2018-05-17 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
    feature-162: Macro para la presentación de campo y visualización de
    observaciones de documentos.

    documentoObservaciones  Lista de observaciones del documento.
    utilController          Controlador utilitario.
    estadoModo              Estado del acta.
    idAreaObservacion       ID para el área de texto de la observación.
-->
<#macro presentarObservaciones documentoObservaciones utilController estadoModo idAreaObservacion >
<div class="card m-y">                           		        
    <#if (documentoObservaciones)??>
    <div class="card-block" id="obsDiv">
        <h5>Observaciones</h5>
        <#list documentoObservaciones as observacion >
        <hr/>
        <strong>${utilController.nombre(observacion.quien)}</strong>, <em> ${observacion.cuando?string('yyyy-MM-dd hh:mm:ss a')}</em>
        <p>${observacion.texto}</p>
        </#list>
    </div>
    </#if>

    <#if estadoModo != "SOLO_CONSULTA" >
    <div class="card-block cus-gray-bg">        
        <fieldset class="form-group">
            <textarea class="form-control" id="${idAreaObservacion}" name="${idAreaObservacion}"></textarea>
        </fieldset>
        <div class="row">
            <div class="col-xs-4">
                <#assign obsButtonID = "obs-button" />
                <button class="btn btn-secondary btn-sm" id="${obsButtonID}" name="${obsButtonID}" onclick="enviarComentario('${idAreaObservacion}', '${documento.id}')">Comentar</button>
                <span id="msg-enviar-comentario"></span>
            </div>
            <div class="col-xs-8">
                <select id="doc-obs-defecto-select" name="doc-obs-defecto-select" class="form-control input-sm" onchange="setObservacionDefecto(this, 'observacion')">
                    <option value="">Lista de observaciones por defecto:</option>
                    <#list observacionesDefecto as observacionDefecto >
                    <option value="${observacionDefecto.id}">${observacionDefecto.textoObservacion}</option>
                    </#list>
                </select>
            </div>
        </div>
    </div>
    </#if>
</div>
</#macro>