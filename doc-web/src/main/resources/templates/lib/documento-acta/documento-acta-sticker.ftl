<#--
    2018-05-17 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
    feature-162: Macro para la presentación de visualización de
    información de la instancia del proceso del documento.

    procesoInstancia    Instancia del proceso.
    documento           Documento.
-->
<#macro presentarSticker documento>
    <div class="card">
        <div class="card-header">
            Sticker
        </div>
        <iframe src="/ofs/viewer?file=/ofs/download/${documento.sticker}" width="100%" height="230px"></iframe>
    </div>
</#macro>