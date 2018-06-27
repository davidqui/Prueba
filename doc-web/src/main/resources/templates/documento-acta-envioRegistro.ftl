<#--
    2018-05-15 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
    feature-162.
-->
<#include "documento-acta-config.ftl">

<div class="col-md-8">    
    <#if estadoModo == "SOLO_CONSULTA">
    <@presentarInformacionRegistrada documento estadoModo />
    
    <@presentarUsuariosAsignados usuariosAsignados />
    
    <div class="alert alert-info" role="alert">
        <h3>Número de radicado</h3>
        <h5>${documento.radicado}</h5>
    </div>
    
    <form action="/documento-acta/cargar-acta-digital" method="POST" id="formdoc" enctype='multipart/form-data'>
        
        <nav class="navbar navbar-default navbar-fixed-bottom text-xs-center hermes-bottombar">
            
                <#list procesoInstancia.transiciones() as transicion >
                <button id="trx_${transicion.id}" class="btn ${getTransicionStyle(transicion)} btn-sm" type="button" onclick="processTransition(this, '${transicion.replace(procesoInstancia)}')">
                    ${transicion.nombre}
                </button>
                </#list>
            
        </nav>

    </form> <#-- Cierra formulario principal -->
    </#if>

    <#-- Observaciones -->       	
    <@presentarObservaciones documentoObservaciones utilController estadoModo "observacion" />    
</div>

<div class="col-md-4">
    <@presentarInformacionProcesoInstancia procesoInstancia documento />
    <@presentarSticker documento/>

    <#-- Adjuntos -->    
    <@presentarCargaAdjuntos documento procesoInstancia utilController estadoModo "CARGA_ACTA_DIGITAL" tipologias "archivo" />
    <br />
</div>

<#include "bandeja-footer.ftl" />
