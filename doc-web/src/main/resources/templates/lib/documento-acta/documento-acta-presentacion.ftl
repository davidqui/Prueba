<#--
    2018-05-31 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
    feature-162. Funciones para presentación de información del acta.
-->

<#--
    Presenta la información de usuarios asignados al acta.

    usuariosAsignados Lista de usuarios asignados al acta.
-->
<#macro presentarUsuariosAsignados usuariosAsignados >
<table class="table table-bordered table-sm" >
    <thead>
        <tr>
            <th>Usuario</th>
            <th>Cargo</th>
        </tr>
    </thead>
    <tbody>
        <#list usuariosAsignados as usuarioAsignado >
        <tr>
            <td>${usuarioAsignado.usuario.usuGrado}. ${usuarioAsignado.usuario.nombre}</td>
            <td>${usuarioAsignado.cargo.carNombre}</td>
        </tr>        
        </#list>
    </tbody>
</table>
</#macro>

<#--
    Presenta la información básica registrada sobre el acta.

    documento   Documento acta.
    estadoModo  Modo del proceso documental.
-->
<#macro presentarInformacionRegistrada documento estadoModo >
<table class="table table-sm">    	
    <tbody>
        <tr><th>Asunto</th><td>${documento.asunto}</td></tr>
        <tr><th>Lugar</th><td>${documento.actaLugar}</td></tr>
        <tr><th>Fecha de elaboración</th><td>${yyyymmdd.format(documento.actaFechaElaboracion)}</td></tr>
        <tr class="table-danger"><th>Nivel de clasificación</th><td>${documento.clasificacion.nombre}</td></tr>
        <tr><th>TRD</th><td>${documento.trd.codigo} - ${documento.trd.nombre}</td></tr>
        <tr><th>Número de folios</th><td>${documento.numeroFolios}</td></tr>
        <#if estadoModo != "CARGA_ACTA_DIGITAL" && documento.radicado?? >
        <tr><th>Número de radicación</th><td>${documento.radicado}</td></tr>
        </#if>
    </tbody>            
</table>
</#macro>
