<#--
    2018-05-21 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
    feature-162.
-->
<#include "documento-acta-config.ftl">

<div class="col-md-8">    
    <#if estadoModo == "SOLO_CONSULTA">
    <table class="table table-sm">    	
        <tbody>
            <tr><th>Asunto</th><td>${documento.asunto}</td></tr>
            <tr><th>Lugar</th><td>${documento.actaLugar}</td></tr>
            <tr><th>Fecha de elaboración</th><td>${yyyymmdd.format(documento.actaFechaElaboracion)}</td></tr>
            <tr class="table-danger"><th>Nivel de clasificación</th><td>${documento.clasificacion.nombre}</td></tr>
            <tr><th>TRD</th><td>${documento.trd.codigo} - ${documento.trd.nombre}</td></tr>
            <tr><th>Número de folios</th><td>${documento.numeroFolios}</td></tr>
            <tr class="table-info"><th>Número de radicado</th><td>${documento.radicado}</td></tr>            
        </tbody>            
    </table>

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
