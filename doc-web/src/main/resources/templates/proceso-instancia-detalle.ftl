<#-- 2017-03-24 jgarcia@controltechcg.com Issue #19 (SIGDI-Incidencias01) -->
<#setting number_format="computer">

<#assign pageTitle = "Procesos disponibles" />
<#assign pageSubTitle = "Selección de proceso" />
<#include "header.ftl" />
<div class="container-fluid">
<div class="col-md-4 col-lg-3">
  <#if (transiciones??) && (transiciones?size > 0) >
    <div class="card">
      <div class="card-header">
        Acciones
      </div>
      <div class="card-block">
          <ul class="nav nav-pills nav-stacked">
              <#list transiciones as t>
              <!--#181 se agrega loader --> 
              <li class="nav-item"><a href="${t.replace(t.definicion, instancia)}" class="nav-link" onclick="loading();">${t.nombre}</a></li>
              </#list>
          </ul>
      </div>
    </div>
  </#if>
  <div class="card">
    <div class="card-header">
      Proceso
    </div>
    <div class="card-block">
        <p>
            <span class="label label-default">P</span>&nbsp;${instancia.proceso.nombre}<br/>
            <span class="label label-primary">E</span>&nbsp;${instancia.estado.nombre}<br/>
            <span class="label label-success">A</span>&nbsp;${(instancia.asignado.nombre)!""}<br/>
        </p>
    </div>
  </div>
  <div class="card">
    <div class="card-header">
      Transiciones
    </div>
    <#--
    	Se modifica el proceso de presentación del histórico de la instancia
		del proceso, para únicamente mostrar aquellos registros que
		corresponden a transiciones entre estados.
    -->
    <#-- <#assign hinstancias = instancia.historia() /> -->
    <#assign hinstancias = historiasTransiciones />
    <#if (hinstancias??) && (hinstancias?size > 0) >
    <ul class="list-group list-group-flush">
        <#list hinstancias as h>
        <li class="list-group-item">
        	<#-- 2017-03-24 jgarcia@controltechcg.com Issue #19 (SIGDI-Incidencias01): Modificación para la presentación 
        	     de la información de quien realizó la operación y quien quedó asignado. -->
            <strong>${h.estado.nombre}</strong>
            <br/>
            <small>${h.cuandoMod!h.cuando}</small>
            <br/>
            <ul>
            	<li><i><b>Realizó</b></i>: ${quienModMap[h.quienMod?string]}</li>
            	<li><i><b>Asignado</b></i>: ${h.asignado}</li>
            </ul>
        </li>
        </#list>
    </ul>
    </#if>
  </div>
</div>
<div class="col-md-8 col-lg-9">
	<h1 class="cus-h1-page-title">Detalle de instancia</h1>
	<p>
		<a href="/proceso/instancia?pin=${instancia.id}">${instancia.id}</a>
	</p>
	  <div class="card">
	    <div class="card-header">
	        Variables
	    </div>
	    <ul class="list-group list-group-flush">
	        <#list instancia.variables as v>
	    		<li class="list-group-item">
	        		<strong>${v.key} = ${v.value}</strong><br/><small><#if v.cuandoMod??>${v.cuandoMod?datetime}<#else>${v.cuando?datetime}</#if></small>
	    		</li>
		    </#list>
	    </ul>
	  </div>
</div>

<#include "footer.ftl" />
