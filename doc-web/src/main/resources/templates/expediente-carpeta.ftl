<#assign pageTitle = "Carpetas"/>
<#include "bandeja-header.ftl">

<div class="container-fluid">
    <#if series?? && (series?size > 0) >
        <table class="table">
        <thead>
            <tr>
            	<th>Código</th>
                <th>Nombre</th>
            </tr>
        </thead>
        <tbody>
        	<#list series as s>
            	<tr>
                	<td nowrap>${s.codigo}</td>
                	<td><a href="carpeta?ser=${s.id}">${s.nombre}</a></td> 
                </tr>
            </#list>            
        </tbody>
        </table>
    </#if>
    <#if subseries?? && (subseries?size > 0) >
    
        <h5>${serie.codigo}. ${serie.nombre}</h5>
        <table class="table">
        <thead>
            <tr>
                <th>Código</th>
                <th>Nombre</th>
            </tr>
        </thead>
        <tbody>
            <#list subseries as x>
                <tr>
                    <td nowrap="">${x.codigo}</td>
                    
                    <#if x.documentos?? && (x.documentos?size > 0) >                		
                		<td><a href="carpeta?sub=${x.id}">${x.nombre} ( ${x.documentos?size} ) </a></td>
                	<#else>
                		<td><a href="carpeta?sub=${x.id}">${x.nombre} ( 0 )</a></td>
                	</#if> 
                    
                    
                </tr>
            </#list>            
        </tbody>
        </table>
    </#if>
    <#if docs?? >
        <h5>${subserie.codigo}. ${subserie.nombre}</h5>
        <#if (docs?size > 0) >
            <table class="table">
            <thead>
                <tr>
                    <th>Radicado</th>
                    <th>Asunto</th>
                    <th>Fecha</th>
                </tr>
            </thead>
            <tbody>
                <#list docs as x>
                    <tr>
                        <td nowrap>${x.radicado!"&lt;Sin radicado&gt;"}</td>
                        <td><a href="/documento?pin=${x.instancia.id}">${x.asunto!"&lt;Sin asunto&gt;"}</a></td>
                        <td nowrap>${x.cuando?string('yyyy-MM-dd')}</td>
                    </tr>
                </#list>            
            </tbody>
            </table>
        </#if>
        <#if (docs?size == 0) >
            <div class="jumbotron">
                <h3>No hay documentos</h3>
                <p>En este momento no hay documentos relacionados a esta carpeta</p>
            </div>
        </#if>
    </#if>
</div>

<#include "bandeja-footer.ftl">
