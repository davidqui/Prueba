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
 	<#return procesoID != estadoID />
 </#function>