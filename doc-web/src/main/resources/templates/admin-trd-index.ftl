<#assign pageTitle = "Tablas de retención documental">
<#assign activePill = "trd">
<#include "admin-header.ftl">

<p><a href="trd/create" class="btn btn-success btn-sm">Nueva tabla</a></p>

<table class="table table-hover">
  <thead class="thead-inverse">
    <tr>
      <th>Nombre</th>
      <th>Activa</th>
    </tr>
  </thead>
  <tbody>
      <tr>
        <td nowrap>
          <div class="btn-group btn-group-sm">
            <button type="button" class="btn btn-link dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
              Tabla según resolución 4576 de 2014
            </button>
            <div class="dropdown-menu cus-normal-text">
              <a href="#" class="dropdown-item">Modificar</a>
              <a href="#" class="dropdown-item">Establecer como activa</a>
              <a href="#" class="dropdown-item">Descargar</a>
              <div class="dropdown-divider"></div>
              <a href="#" class="dropdown-item">Cargar</a>
            </div>
          </div>
        </td>
        <td>Sí</td>
      </tr>
  </tbody>
</table>

<#include "admin-footer.ftl">