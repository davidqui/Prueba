<#--
    2018-05-15 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
    feature-162.
-->
<#include "documento-acta-config.ftl">

<div class="col-md-8">    
    <#if estadoModo == "CARGA_ACTA_DIGITAL">
    <table class="table table-sm">    	
        <tbody>
            <tr><th>Asunto</th><td>${documento.asunto}</td></tr>
            <tr><th>Lugar</th><td>${documento.actaLugar}</td></tr>
            <tr><th>Fecha de elaboración</th><td>${yyyymmdd.format(documento.actaFechaElaboracion)}</td></tr>
            <tr class="table-danger"><th>Nivel de clasificación</th><td>${documento.clasificacion.nombre}</td></tr>
            <tr><th>TRD</th><td>${documento.trd.codigo} - ${documento.trd.nombre}</td></tr>
            <tr><th>Número de folios</th><td>${documento.numeroFolios}</td></tr>
        </tbody>            
    </table>
    
    <div class="alert alert-info" role="alert">
        <h3>Número de radicado</h3>
        <h5>${documento.radicado}</h5>
    </div>
    
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
                <button id="trx_${transicion.id}" class="btn ${getTransicionStyle(transicion)} btn-sm" type="button" onclick="processTransition(this, '${transicion.replace(procesoInstancia)}')">
                    ${transicion.nombre}
                </button>
                </#list>
            </#if>
        </nav>

    </form> <#-- Cierra formulario principal -->
    </#if>

    <#-- Observaciones -->       	
    <@presentarObservaciones documentoObservaciones utilController estadoModo "observacion" />    
</div>

<div class="col-md-4">
    <@presentarInformacionProcesoInstancia procesoInstancia documento />

    <#-- Adjuntos -->    
    <@presentarCargaAdjuntos documento procesoInstancia utilController estadoModo "CARGA_ACTA_DIGITAL" tipologias "archivo" />
    <br />
</div>

<#include "bandeja-footer.ftl" />
