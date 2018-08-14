<#--
    2018-05-21 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
    feature-162.
-->
<#include "documento-acta-config.ftl">

<div class="col-md-8">    
    <#if estadoModo == "SOLO_CONSULTA">
    <@presentarInformacionRegistrada documento estadoModo />
    
    <@presentarUsuariosAsignados usuariosAsignadosConsulta />

        <#if documento.pdf?? >
        <div class="card">
            <div class="card-header">Acta digitalizada</div>
            <iframe src="/ofs/viewer?file=/ofs/download/${documento.pdf}" width="100%" height="700px"></iframe>                    
        </div>                   	               
        </#if>
    </#if>

    <#-- Observaciones -->       	
    <@presentarObservaciones documentoObservaciones utilController estadoModo "observacion" />    
</div>

<div class="col-md-4">
    <@presentarInformacionProcesoInstancia procesoInstancia documento />
    <#-- Agregar a expediente -->    
    <#if expedientesValidos??>
        <a type="button" class="btn btn-primary btn-sm" data-toggle="modal" data-target="#modal-enviar-expediente" style="width: 100%;margin: 10px 0;">
            Asignar Expediente
        </a>
    </#if>
    
    <#-- Adjuntos -->    
    <@presentarCargaAdjuntos documento procesoInstancia utilController estadoModo "EDICION_INFORMACION" tipologias "archivo" />
    <br />
</div>

<!-- modal seleccionar expediente -->
<div class="modal fade bd-example-modal-lg" id="modal-enviar-expediente" tabindex="-1" role="dialog" aria-labelledby="exampleModalLongTitle" aria-hidden="true">
  <div class="modal-dialog modal-lg" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="exampleModalLongTitle">Seleccionar Expediente</h5>
      </div>
      <div class="modal-body" style="max-height: 650px; overflow: hidden; overflow-y: auto;">
        <#if expedientesValidos?? && expedientesValidos?size != 0>
            <div class="list-group">
                <input type="hidden" id="expedienteDestino" name="expedienteDestino" value="" />
                <#list expedientesValidos as pExpediente>
                    <button id="expediente-${(pExpediente.expId)!""}" onclick="seleccionarExpediente(${(pExpediente.expId)!""})"
                            class="list-group-item list-group-item-action flex-column align-items-start expediente-list">
                        <div class="d-flex w-100 justify-content-between">
                          <h5 class="mb-1">${(pExpediente.expNombre)!""}</h5>
                          <small>${(pExpediente.fecCreacion)!""}</small>
                        </div>
                        <p class="mb-1">
                            <#if pExpediente.expDescripcion?length &lt; 255>
                            ${pExpediente.expDescripcion}
                            <#else>
                            ${pExpediente.expDescripcion?substring(0,249)} ...
                            </#if>
                        </p>
                        <small>${(pExpediente.depId.nombre)!""}</small>
                    </button>
                </#list>
            </div>
        <#else>
           <p>No tiene expedientes validos para este documento.</p>
        </#if>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancelar</button>
        <button type="button" id="submit-button"
        class="btn btn-primary" 
        onclick="submitSeleccionarExpediente('${procesoInstancia.id}')"
        style="display:none;"
        >
        Enviar a expediente
        </button>
      </div>
    </div>
  </div>
</div>

<script src="/js/app/enviar-expediente-documento.js"></script>

<#include "bandeja-footer.ftl" />
