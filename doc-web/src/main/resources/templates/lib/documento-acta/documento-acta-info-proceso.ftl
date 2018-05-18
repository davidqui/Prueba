<#--
    2018-05-17 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
    feature-162: Macro para la presentación de visualización de
    información de la instancia del proceso del documento.
-->
<#macro presentarInformacionProcesoInstancia procesoInstancia documento >
    <div class="card">
        <div class="card-header">
            <a href="/proceso/instancia/detalle?pin=${procesoInstancia.id}" target = "_blank">Proceso</a>
        </div>
        <div class="card-block">
            <table>
                <tbody>
                    <tr><th>Fecha de creación:</th><td>${documento.cuando?string('yyyy-MM-dd hh:mm a')}</td></tr>
                    <tr><th>Fecha última acción:</th><td>${documento.cuandoMod?string('yyyy-MM-dd hh:mm a')}</td></tr>
                    <tr><th>Proceso:</th><td>${procesoInstancia.proceso.nombre}</td></tr>
                    <tr><th>Estado:</th><td>${procesoInstancia.estado.nombre}</td></tr>
                    <tr><th>Usuario asignado:</th><td>${procesoInstancia.asignado}</td></tr>
                    <#if documento.cargoIdElabora ?? >
                    <tr><th>Cargo:</th><td>${documento.cargoIdElabora.carNombre}</td></tr>
                    </#if>
                    <tr><th>Número de radicación:</th><td>${documento.radicado!""}</td></tr>
                </tbody>
            </table>
        </div>
    </div>
</#macro>