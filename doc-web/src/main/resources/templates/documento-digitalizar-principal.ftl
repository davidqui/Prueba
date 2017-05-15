<#assign pageTitle = documento.asunto!"Documento" />

<#include "bandeja-header.ftl" />

<h5 class="m-y">Digitalización del documento principal</h5>

<p class="m-y">
    El documento principal es el oficio que se encuentra al inicio del documento que se está radicando.
    Los otros folios corresponden a un anexo que podrá digitalizar en el siguiente paso. La digitalización
    se puede hacer de dos maneras que se describen a continuación.
</p>

<div class="col-md-12 m-y">
    
    <div class="col-md-4">
        <div class="card">
            <div class="card-block">
                <h4 class="card-title">Escaner <div style="color: #0275d8;">( Código de seguridad: ${documento.codigoValidaScanner} )</div> </h4>
                <p><strong><span id="deviceName"></span></strong></p>
            </div>
            <#if windowsOS?? && windowsOS>
            <div class="text-center">
                <img class="card-img-top" src="/css/img/scanner.jpg" alt="Escaner"/>
            </div>
            <div class="card-block">
                <p class="card-text">
                    Las imágenes se capturan mediante dispositivos conectados a su computador, ya sean estas
                    imágenes fotos o páginas escaneadas. Se recomienda escanear con una resolución de 300dpi
                    para obtener mejores resultados en el proceso de extracción de información textual al que
                    será sometida la imagen. Todas las imágenes serán consolidadas en un único PDF.
                </p>                               
                <a href="/java/doc-web-scanner.jar" class="btn btn-primary" download>Descargar scanner </a>               
            </div>
            <#else>
            <div class="text-center">
                <img class="card-img-top" src="/css/img/no-scanner.jpg" alt="No escaner"/>
            </div>
            <div class="card-block">
                <p class="card-text">
                    Lo sentimos, esta operación sólo funciona si su sistema operativo es Windows.
                </p>
            </div>
            </#if>
        </div>
    </div>
<!--
    <div class="col-md-4">
        <div class="card">
          <div class="card-block">
            <h4 class="card-title">Unidad virtual</h4>
          </div>
          <div class="text-center">
            <img class="card-img-top" src="/css/img/net-drive.png" alt="Subir archivo"/>
          </div>
          <div class="card-block">
            <p class="card-text">
                El documento se toma de la unidad virtual del usuario.
            </p>
            <a href="/documento/digitalizar-principal-net?pin=${documento.instancia.id}&tid=${tid}" class="btn btn-primary" id="acquire">Seleccionar archivo</a>
          </div>
        </div>
    </div>
-->
    <div class="col-md-4">
        <div class="card">
          <div class="card-block">
            <h4 class="card-title">Subir archivos</h4>
          </div>
          <div class="text-center">
            <img class="card-img-top" src="/css/img/uploadfile.png" alt="Subir archivo"/>
          </div>
          <div class="card-block">
            <p class="card-text">
                El documento se sube al servidor desde un archivo de su computador. El archivo que suba será procesado por el servidor para obtener la mayor cantidad de información textual. El formato del archivo debe ser PDF.
            </p>
            <a href="/documento/digitalizar-principal-file?pin=${documento.instancia.id}&tid=${tid}" class="btn btn-primary" id="acquire">Subir archivo</a>
          </div>
        </div>
    </div>

</div>

<script>

	$(function() {
	  function update() {
	  	$.ajax({
            type: "GET",
            url: "/documento/validarcarguedigitacion?pin=${documento.instancia.id}",            
            success: function(data) {
                if( data == "DIGITALIZACION" ){
                	window.location.reload(true);
                }
            }
        });		        
	  }
	  setInterval(update, 4000);
	  update();
	});

</script>

<#include "bandeja-footer.ftl" />