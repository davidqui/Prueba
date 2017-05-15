<#--
    2017-04-07 jgarcia@controltechcg.com Issue #37 (SIGDI-Controltech): Función que define si una transacción corresponde a "Anular"  
-->
<#function isTransaccionAnular transaccion >
	<#local id = transaccion.id />
	<#return (id == 104 || id == 105 || id == 121 || id == 122) />
</#function>