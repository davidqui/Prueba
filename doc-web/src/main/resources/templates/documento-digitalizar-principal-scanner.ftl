<#assign pageTitle = documento.asunto!"Documento" />

<#include "bandeja-header.ftl" />

<h5 class="m-y">Digitalización del documento principal</h5>

<p class="m-y">
    El documento principal es el oficio que se encuentra al inicio del documento que se está radicando.
    Los otros folios corresponden a un anexo que podrá digitalizar en el siguiente paso. La digitalización
    se puede hacer de dos maneras que se describen a continuación.
</p>

<#assign uploadUrl>${baseUrl}/ofs/upload-stage?ref=${documento.id}<#if stage??>&stageId=${stage.id}</#if></#assign>

<script type="text/javascript" src="/js/deployJava.js"></script>
<script type="text/javascript">
    <!--
    var attributes = { 
        id : 'hermesScanApplet',
        code : 'com.laamware.hermes.scan.ScanPageApplet',  
        width : 1, 
        height : 1,
        archive : '${baseUrl}/java/hermes-scan-0.0.1-SNAPSHOT-jar-with-dependencies.jar'
    } ;
    var parameters = { 
        uploadUrl: '${uploadUrl}',
        cookie: '${cookie}'
    } ;
    deployJava.runApplet(attributes, parameters, '1.6');
    -->
</script>

<#assign deferredJS>
    <script type="text/javascript">
    <!--
    function getDeviceNames() {
        var devNames = hermesScanApplet.getDeviceNames();
        var i;
        for(i = 0; i < devNames.length; i++) {
            var link = $('<a/>');
            link.attr('href', "#");
            link.attr('onclick', 'hermesScanApplet.acquire("' + devNames[i] + '")');
            link.attr("class", "nav-link");
            link.text(devNames[i]);
            var li = $('<li/>');
            li.attr("class", "nav-item");
            link.appendTo(li);
            li.appendTo($("#deviceList"));
        }
    }

    $(document).ready(function(){
        getDeviceNames();
    });
    -->    
    </script>
</#assign>

<div class="row m-y">
    <div class="col-md-9">
        <div class="card">
            <div class="card-header">
                Páginas
            </div>
            <div class="card-block">
                <#if stage?? && (stage.partes)?? >
                    <div class="m-y">
                        <a href="/documento/attach?pin=${documento.instancia.id}&tid=${tid}" class="btn btn-primary">Terminar</a>
                    </div>
                    <#list stage.partes?split(":") as file>
                        <img src="/ofs/download/tmb/${file}" height="250" />
                    </#list>
                <#else>
                    <p>El documento no tiene páginas aún. Seleccione el dispositivo con el que desea realizar la captura de la imagen</p>
                </#if>
            </div>
        </div>
    </div>
    <div class="col-md-3">
        <div class="card">
            <div class="card-header">
                Dispositivos
            </div>
            <div class="card-block">
              <ul class="nav nav-pills nav-stacked" id="deviceList">
              </ul>
            </div>
        </div>
    </div>
</div>

<#include "bandeja-footer.ftl" />