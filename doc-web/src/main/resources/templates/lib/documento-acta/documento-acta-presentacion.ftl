<#--
    2018-05-31 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
    feature-162. Funciones para presentación de información del acta.
-->

<#--
    Presenta la información de usuarios asignados al acta.

    usuariosAsignadosConsulta Lista de usuarios asignados al acta.
-->
<#macro presentarUsuariosAsignados usuariosAsignadosConsulta >
    <#if (usuariosAsignadosConsulta?size > 0) >
<table class="table table-bordered table-sm" >
    <thead>
        <tr>
            <th>Usuario</th>
            <th>Cargo</th>
            <th>Clasificación</th>
            </tr>
        </thead>
    <tbody>
            <#list usuariosAsignadosConsulta as usuarioAsignado >
        <tr>
            <td>${usuarioAsignado.usuario.usuGrado}. ${usuarioAsignado.usuario.nombre}</td>
            <td>${usuarioAsignado.cargo.carNombre}</td>
            <td>${usuarioAsignado.usuario.clasificacion.nombre}</td>
            </tr>        
            </#list>
        </tbody>
    </table>
    </#if>
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
        <#--
            2018-05-07 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
            feature-162: "Lugar" cambia por "Lugar donde se suscribió el acta" y
            "Fecha" cambia por "Fecha de suscripción del acta".
        -->
        <tr><th>Lugar donde se suscribió el acta</th><td>${documento.actaLugar}</td></tr>
        <tr><th>Fecha de suscripción del acta</th><td>${yyyymmdd.format(documento.actaFechaElaboracion)}</td></tr>
        <tr class="table-danger"><th>Nivel de clasificación</th><td>${documento.clasificacion.nombre}</td></tr>
        <tr><th>TRD</th><td>${documento.trd.codigo} - ${documento.trd.nombre}</td></tr>
        <tr><th>Número de folios</th><td>${documento.numeroFolios}</td></tr>
        <tr><th>Descripción</th><td>${documento.actaDescripcion}</td></tr>
        <#if estadoModo != "CARGA_ACTA_DIGITAL" && documento.radicado?? >
        <tr><th>Número de radicación</th><td>${documento.radicado}</td></tr>
        </#if>
        </tbody>            
    </table>
</#macro>
