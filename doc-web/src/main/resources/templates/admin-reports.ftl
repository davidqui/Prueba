<#assign pageTitle = "Informes, consultas y estadísticas">
<#assign activePill = "reports">
<#include "admin-header.ftl">

<#if !list??>
  <div class="jumbotron">
    <h1 class="display-1">No hay registros</h1>
    <p class="lead">En este momento no existen registros</p>
  </div>
<#else>
  <table class="table">
    <thead class="thead-inverse">
      <tr>
        <th>#</th>
        <th>Fecha</th>
        <th>Asunto</th>
        <th>Destinatario</th>
        <th>Estado</th>
        <th>Tipo</th>
      </tr>
    </thead>
    <tbody>
      <#list list as x>
        <tr>
          <td>${x.DOC_ID}</td>
          <td nowrap>${x.DOC_FECHA_RADICACION?string('yyyy-MM-dd')}</td>
          <td>${x.DOC_ASUNTO}</td>
          <td>${x.DEST_NOMBRE}</td>
          <td>${x.EST_NOMBRE}</td>
          <td>${x.TIPO_RAD_NOMBRE}</td>
        </tr>
      </#list>
    </tbody>
  </table>
</#if>

<#include "admin-footer.ftl">