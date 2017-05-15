<#assign pageTitle = "Documento"/>
<#include "header.ftl" />
<div class="col-md-4 col-lg-3">
  <#if (transiciones??) && (transiciones?size > 0) >
    <div class="card">
      <div class="card-header">
        Acciones
      </div>
      <div class="card-block">
          <ul class="nav nav-pills nav-stacked">
              <#list transiciones as t>
              <li class="nav-item"><a href="${t.replace(t.definicion, instancia)}" class="nav-link">${t.nombre}</a></li>
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
    <ul class="list-group list-group-flush">
        <#list hinstancias as h>
        <li class="list-group-item">
            <strong>${h.estado.nombre}</strong><br/><small>${h.cuandoMod!h.cuando}</small>
        </li>
        </#list>
    </ul>
  </div>
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
<div class="col-md-8 col-lg-9">