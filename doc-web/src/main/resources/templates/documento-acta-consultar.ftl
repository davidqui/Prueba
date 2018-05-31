<#--
    2018-05-21 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
    feature-162.
-->
<#include "documento-acta-config.ftl">

<div class="col-md-8">    
    <#if estadoModo == "SOLO_CONSULTA">
    <@presentarInformacionRegistrada documento estadoModo />
    
    <@presentarUsuariosAsignados usuariosAsignados />

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

    <#-- Adjuntos -->    
    <@presentarCargaAdjuntos documento procesoInstancia utilController estadoModo "EDICION_INFORMACION" tipologias "archivo" />
    <br />
</div>

<#include "bandeja-footer.ftl" />
