<#assign pageTitle = "Nueva tabla de retención documental">
<#assign activePill = "trd">
<#include "admin-header.ftl">

<div class="container">
  <div class="row">
    <form method="POST">
      <fieldset class="form-group">
        <label for="nombre">Nombre de la tabla</label>
        <input type="text" class="form-control" id="nombre"/>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
      </fieldset>
      <p>
        <button type="submit" class="btn btn-success">Crear</button>
        <a href="../trd" class="btn btn-secondary">Cancelar</a>
      </p>
    </form>
  </div>
</div>

<#include "admin-footer.ftl">
