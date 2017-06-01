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
      <div class="alert ${flashClass} text-xs-center">
        <p><strong>${flashMsg}</strong></p>
      </div>
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
        	<a href=${path} class="nav-link <#if activePill == ap >active</#if>">${label}</a>
        </li>
    </#if>
</#macro>