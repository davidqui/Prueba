<#--
    2018-05-18 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
    feature-162: Macro para la presentación del componente de presentación
    y carga de archivos adjuntos para el acta.

    documento           Documento.
    procesoInstancia    Instancia del proceso.
    utilController      Controlador utilitario.
    estadoModo          Estado del acta.
    estadoModoCarga     Estado del acta en el cual es válido permitir carga de adjuntos.
    tipologias          Lista de tipologías de adjuntos.
    inputFileID         ID del campo para la carga de archivo.
-->
<#macro presentarCargaAdjuntos documento procesoInstancia utilController estadoModo estadoModoCarga tipologias inputFileID >
<div class="card">
    <#if (documento.adjuntos?size > 0) >
    <div class="card-block">
        <h5 class="m-b">Adjuntos actuales</h5>
        <#list documento.adjuntos as adjunto >
            <#if adjunto.activo>
            <hr/>
            <strong>${adjunto.tipologia.nombre}</strong><br/>
            <em>Subido el ${yyyymmdd.format(adjunto.cuando)} por ${utilController.nombre(adjunto.quien)}</em>
            <#if estadoModo == estadoModoCarga >
            <a href="#" onclick="eliminarArchivoAdjunto('${adjunto.original}', '${adjunto.id}', '${procesoInstancia.id}');">Eliminar</a><br/>
            </#if>
            <a href="#" onclick="visualizar('/ofs/viewer?file=/ofs/download/${adjunto.contenido}')">
                <img src="/ofs/download/tmb/${adjunto.contenido}" />
                <br/>
                ${adjunto.original}
            </a>
            <br/>
            </#if>
        </#list>
            
        <script type="text/javascript">
            function eliminarArchivoAdjunto(adjuntoNombreOriginal, adjuntoID, procesoInstanciaID) {
                var result = confirm("¿Está seguro de eliminar el archivo adjunto " + adjuntoNombreOriginal + "?");
                if(!result){
                    return;
                }
                    
                $.ajax({
                    method: "DELETE",
                    url: "/documento-acta/adjunto/" + adjuntoID +  "/" + procesoInstanciaID + "/eliminar"
                }).done(function() {
                    console.log("Adjunto eliminado: " + adjuntoID + ", " + procesoInstanciaID);
                }).fail(function() {
                    console.log("Error eliminando adjunto: " + adjuntoID + ", " + procesoInstanciaID);
                }).always(function(){
                    location.reload();
                });
            }    
        </script>
    </div>
    <#elseif estadoModo != estadoModoCarga >
    <div class="alert" role="alert">No hay archivos adjuntos.</div>
    </#if>

    <#if estadoModo == estadoModoCarga >
    <div class="card-block cus-gray-bg">
        <form action="/documento-acta/adjunto/${documento.id}/guardar" method="post" enctype="multipart/form-data">
            <fieldset class="form-group">
                <label for="destinatario"><strong>Tipología y archivo</strong></label>
                </br>
                <span style="font-style: italic">Únicamente se permiten cargar archivos en formato PDF, JPG y PNG.</span>                    
                <select class="form-control" id="tipologia" name="tipologia">
                <#if tipologias??>
                    <option value=""></option>
                    <#list tipologias as tipologia>
                    <option value="${tipologia.id}">${tipologia.nombre}</option>
                    </#list>
                </#if>
                    </select>
                <input type="file" class="form-control" id="${inputFileID}" name="${inputFileID}"/>
            </fieldset>
            <button type="submit" class="btn btn-secondary btn-sm">Subir</button>
        </form>
    </div>
    </#if>    
</div>

<@presentarVisualizadorPDF />

</#macro>

<#macro presentarVisualizadorPDF >
<div class="modal fade" id="visualizador">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                    <span class="sr-only">Close</span>
                </button>
            </div>        
            <iframe id="iframeVisor" width="100%" height="600px"></iframe>
        </div>
    </div>
</div>

<script type="text/javascript">
    function visualizar(url) {
        var frame = $("#visualizador iframe");
        frame.attr('src', url);
        $('#visualizador').width('100%');
        $('#visualizador .modal-dialog').width('95%');
        h = $(window).height();
        $('#visualizador').height(h + 'px');        
        $('#visualizador').modal('show');
        $('#visualizador iframe').height((h - 125) + 'px');
    }    
</script>
</#macro>