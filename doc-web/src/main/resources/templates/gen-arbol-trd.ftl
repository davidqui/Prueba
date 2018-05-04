<#--
    2018-05-04 edison.gonzalez@controltechcg.com Issue #157 (SICDI-Controltech) 
    feature-157: Macro para visualizar el arbol de trds.
-->

<#macro listTrds trds>
    <ul>		 
        <#list trds as d>	
            <#assign tamanio = d.subs?size>
            <#if d.subs?has_content >
                <li data-jstree='{ "id" : ${d.id} , "opened" : false }'><a>${d.codigo} - ${d.nombre} prueba1</a>
                    <@listTrds d.subs/>
                </li>
            <#else>
                <li data-jstree='{ "id" : ${d.id} , "opened" : false }'><a > ${d.codigo} - ${d.nombre}prueba3</a></li>
            </#if>			
        </#list>
    </ul>
</#macro>