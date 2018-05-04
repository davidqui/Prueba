<#--
    2018-05-04 edison.gonzalez@controltechcg.com Issue #157 (SICDI-Controltech) 
    feature-157: Macro para visualizar el arbol de trds.
-->

<#macro listTrds trds selected=0 href=true>
    <ul>		 
        <#list trds as d>	
            <#assign tamanio = d.subs?size>						
            <#if d.subs?has_content >
                <#if href>
                    <li data-jstree='{ "id" : ${d.id} , "opened" : false }'><a href="?pin=${pin!""}&tid=${tid!""}&did=${d.id}&mode=${mode!""}"><#if d.sigla?? > ${d.sigla} - </#if>${d.nombre} </a>				
                        <@listDependencias d.subs selected />
                    </li>
                <#else>
                    <li data-jstree='{ "id" : ${d.id} , "opened" : false }'><a href="">${d.codigo} - ${d.nombre} </a>				
                        <@listTrds d.subs selected false/>
                    </li>
                </#if>	
            <#else>
                <#if d.id == selected>
                    <li data-jstree='{ "id" : ${d.id} , "opened" : false }'><strong style="background-color:#A9F5F2; padding:5px;"> ${d.codigo} - ${d.nombre} </strong></li>
                <#else>
                    <#if href>
                        <li data-jstree='{ "id" : ${d.id} , "opened" : false }'><a href="?pin=${pin!""}&tid=${tid!""}&did=${d.id}&mode=${mode!""}"> ${d.codigo} - ${d.nombre} </a></li>
                    <#else>
                        <li data-jstree='{ "id" : ${d.id} , "opened" : false }'><a href=""> ${d.codigo} - ${d.nombre} </a></li>
                    </#if>	
                    
                </#if>		
            </#if>			
        </#list>
    </ul>
</#macro>