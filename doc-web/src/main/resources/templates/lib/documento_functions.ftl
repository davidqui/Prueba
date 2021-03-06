<#--
    2017-04-07 jgarcia@controltechcg.com Issue #37 (SIGDI-Controltech): Función que define si una transacción corresponde a "Anular"  
-->
<#function isTransaccionAnular transaccion >
	<#local id = transaccion.id />
	<#return (id == 104 || id == 105 || id == 121 || id == 122) />
</#function>

<#--
	2017-05-19 jgarcia@controltechcg.com Issue #73 (SICDI-Controltech) feature-73:
	Funcionalidad que permite determinar en los templates de documentos si el
	documento en sesión se encuentra en el proceso y estado para aplicar la
	asignación cíclica. 
 -->

 <#function mustApplyAsignacionCiclica documento >
 	<#local procesoID = documento.instancia.proceso.id />
 	<#local estadoID = documento.instancia.estado.id />
 	<#if procesoID == 9 && estadoID == 46 >
 		<#return (1 == 1) />
 	<#elseif procesoID == 8 && estadoID == 49 >
 		<#return (1 == 1) />
 	<#else>
 		<#return (1 != 1) />
 	</#if> 	
 </#function>
 
 <#--
 	 2017-05-19 jgarcia@controltechcg.com Issue #73 (SICDI-Controltech) feature-73:
 	 Funcionalidad que permite determinar el estilo de presentación de los botones
 	 de las transiciones a presentar en la pantalla de documento.
 -->
 
 <#function getTransicionStyle transicion >
 	<#local id = transicion.id />
 	
 	<#switch id >
 		<#case 123 >
 		<#case 125 >
 		 	<#-- Asignar documento -->
 			<#return "btn-success" />
 		<#default>
 			<#return "btn-primary" />
 	</#switch>
 </#function>
 
 <#--
 	 2017-05-19 jgarcia@controltechcg.com Issue #73 (SICDI-Controltech) feature-73:
 	 Funcionalidad que establece la URL de la acción "Reasignar" dependiendo si esta
 	 debe aplicarse para la asignación cíclica o no. 
 -->
 <#function getReasignarURL documento >
 	<#if mustApplyAsignacionCiclica(documento) >
 		<#return "/documento/reasignar-ciclico?pin=" + documento.instancia.id />
 	<#else>
 		<#return "/documento/reasignar?pin=" + documento.instancia.id />
 	</#if>
 </#function>
 
 <#--
 	 2017-06-12 jgarcia@controltechcg.com Issue #93 (SICDI-Controltech) feature-93:
 	 Funcionalidad que indica si una transición corresponde a la anulación de un
 	 documento respuesta. 
  -->
  <#function isTransicionAnularRespuesta transaccion >
	<#local id = transaccion.id />
	<#return (id == 127) />
</#function>

<#--
    2018-03-01 edison.gonzalez@controltechcg.com Issue #151 (SICDI-Controltech) feature-151:
    Funcionalidad que indica si una transición corresponde a la firma del documento. 
  -->
<#function isTransicionFirmar transaccion >
	<#local id = transaccion.id />
	<#return (id == 17) />
</#function>