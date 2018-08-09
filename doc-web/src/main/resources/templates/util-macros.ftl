<#assign objectConstructor = "freemarker.template.utility.ObjectConstructor"?new()>
<#assign yyyymmdd = objectConstructor("java.text.SimpleDateFormat","yyyy-MM-dd")>

<#macro flash>

	<#--
		2017-06-01 jgarcia@controltechcg.com Issue #99 (SICDI-Controltech) hotfix-99:
		Validador para determinar la funcionalidad de los mensajes FLASH. 
		(Código comentado).
	-->
	<#--
		<#if FLASH_SUCCESS??>
			${FLASH_SUCCESS}
		<#else>
			NADA!!!
		</#if>
	 -->

    <#if FLASH_ERROR?? || FLASH_SUCCESS?? || FLASH_INFO?? || FLASH_WARN?? || postError?? || postSuccess?? >
      <#if FLASH_ERROR??>
        <#assign flashClass = "alert-danger" />
        <#assign flashMsg = FLASH_ERROR />
      </#if>
      <#if postError??>
        <#assign flashClass = "alert-danger" />
        <#assign flashMsg = postError />
      </#if>
      <#if postSuccess??>
        <#assign flashClass = "alert-success" />
        <#assign flashMsg = postSuccess />
      </#if>
      <#if FLASH_SUCCESS?? >
        <#assign flashClass = "alert-success" />
        <#assign flashMsg = FLASH_SUCCESS />
      </#if>
      <#if FLASH_INFO?? >
        <#assign flashClass = "alert-info" />
        <#assign flashMsg = FLASH_INFO />
      </#if>
      <#if FLASH_WARN?? >
        <#assign flashClass = "alert-warn" />
        <#assign flashMsg = FLASH_WARN />
      </#if>
        <#--
		2018-04-25 jgarcia@controltechcg.com Issue #156 (SICDI-Controltech) hotfix-156:
		Modificación de la presentación de los mensajes, para los usuarios 
                asignados en un documento multidestino. 
		(Código comentado).
	-->
        <#if flashMsg?contains("com.laamware.ejercito.doc.web.dto.FlashAttributeValue")>
            <div class="alert ${flashClass}">
                <strong>
                    <p class="text-center">${flashMsg.mainMessage}</p>

                    <script type="text/javascript">
                        function toogleFlashAltMessages(obj){
                            var objText = $(obj).text();
                            if($("#flash-alt-messages-list").is(":visible")){
                                objText = objText.replace('Ocultar', 'Ver');
                            } else {
                                objText = objText.replace('Ver', 'Ocultar');
                            }
                            $(obj).text(objText);

                            $("#flash-alt-messages-list").toggle();
                        }
                    </script>
                    
                    <#if flashMsg.multidestino>
                        <p class="text-left"><#if flashMsg.multidestino> (<a href="#" onclick="toogleFlashAltMessages(this); return false;">Ver ${flashMsg.numRecords} Registros</a>)</#if></p>

                        <div id="flash-alt-messages-list" class="text-left" <#if flashMsg.multidestino>style="display: none;"</#if>>
                            ${flashMsg.altMessageMultidestino}
                        </div>
                    </#if>
                </strong>
            </div>
        <#else>
            <div class="alert ${flashClass} text-xs-center">
                <p><strong>${flashMsg}</strong></p>
            </div>
        </#if>

    </#if>
</#macro>

<#macro authorize roles>
  <#if utilController??>
    <#if utilController.isAuthorized(roles)>
      <#nested>
    </#if>
  </#if>
</#macro>


<#macro link_admin role path ap label >
	<#if utilController.isAuthorized(role)>
<li class="nav-item">
    <!--#181 se agrega loader --> 
    <a href=${path} class="nav-link <#if activePill == ap >active</#if>" onclick="loading()">${label}</a>
    </li>
    </#if>
</#macro>