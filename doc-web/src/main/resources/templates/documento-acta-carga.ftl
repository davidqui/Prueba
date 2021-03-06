<#--
    2018-05-15 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
    feature-162.
-->
<#include "documento-acta-config.ftl">

<div class="col-md-8">
    <#assign transicion_validar = 155>
    <#assign transicion_generar_sticker = 157>
    <#if estadoModo == "CARGA_ACTA_DIGITAL">
        <@presentarInformacionRegistrada documento estadoModo />

        <@presentarUsuariosAsignados usuariosAsignados />

        <div class="alert alert-info" role="alert">
            <h3>Número de radicado</h3>
            <h5>${documento.radicado}</h5>
        </div>

        <#if usuarioRegistro?? && usuarioRegistro.id == usuarioSesion.id>
            <form action="/documento-acta/cargar-acta-digital" method="POST" id="formdoc" enctype='multipart/form-data'>
                <input type="hidden" id="pin" name="pin" value="${procesoInstancia.id}" />

                <fieldset class="form-group">
                    <label for="archivo">Archivo (*)</label>
                    <input type="file" class="form-control" id="archivo" name="archivo"/>
                </fieldset>

                <#if documento.pdf?? >
                <div class="card">
                    <div class="card-header">Acta digitalizada</div>
                    <iframe src="/ofs/viewer?file=/ofs/download/${documento.pdf}" width="100%" height="700px"></iframe>                    
                </div>                   	               
                </#if>

                <nav class="navbar navbar-default navbar-fixed-bottom text-xs-center hermes-bottombar">
                    <button id="guardar-doc-btn" type="submit" class="btn btn-success btn-sm">Cargar Archivo</button>
                    <#if documento.pdf?? >
                        <#list procesoInstancia.transiciones() as transicion >
                        <#if sticker?? && transicion.id == transicion_validar || !sticker?? && transicion.id == transicion_generar_sticker>
                            <button id="trx_${transicion.id}" class="btn ${getTransicionStyle(transicion)} btn-sm" type="button" onclick="processTransition(this, '${transicion.replace(procesoInstancia)}')">
                                ${transicion.nombre}
                            </button>
                        </#if>
                        </#list>
                    </#if>
                </nav>

            </form> <#-- Cierra formulario principal -->
        </#if>
    </#if>

    <#-- Observaciones -->       	
    <@presentarObservaciones documentoObservaciones utilController estadoModo "observacion" />    
</div>

<div class="col-md-4">
    <@presentarInformacionProcesoInstancia procesoInstancia documento />
    
    <#if sticker??>
        <@presentarSticker documento/>
    </#if>
    
    <#-- Adjuntos --> 
    <@presentarCargaAdjuntos documento procesoInstancia utilController estadoModo "CARGA_ACTA_DIGITAL" tipologias "archivo" />
    <br />
</div>

<#include "bandeja-footer.ftl" />
