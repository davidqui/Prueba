<#--
    2018-01-29 edison.gonzalez@controltechcg.com Issue #147 (SICDI-Controltech) 
    feature-147: Macro para visualizar el arbol de dependencias.
-->

<#macro listDependencias dependencias selected=0>
	<ul>		 
		<#list dependencias as d>	
			<#assign tamanio = d.subs?size>						
			<#if d.subs?has_content >
				<li data-jstree='{ "id" : ${d.id} , "opened" : false }'><a href=""><#if d.sigla?? > ${d.sigla} - </#if>${d.nombre} </a>				
					<@listDependencias d.subs selected />
				</li>	
			<#else>
				<#if d.id == selected>
					<li data-jstree='{ "id" : ${d.id} , "opened" : false }'><strong style="background-color:#A9F5F2; padding:5px;"> <#if d.sigla?? > ${d.sigla} - </#if> ${d.nombre} </strong></li>
				<#else>
					<li data-jstree='{ "id" : ${d.id} , "opened" : false }'><a href=""> <#if d.sigla?? > ${d.sigla} - </#if> ${d.nombre} </a></li>
				</#if>		
			</#if>			
		</#list>
	</ul>
</#macro>