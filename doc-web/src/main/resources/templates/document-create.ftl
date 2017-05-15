<#assign pageTitle = "Nuevo documento">

<#assign headScripts>
  <script src="/ckeditor/ckeditor.js"></script>
</#assign>

<#include "header.ftl">
<div class="container">
  <div class="row">
    <h1 class="cus-h1-page-title">${pageTitle}</h1>

    <form>
      <fieldset class="form-group">
        <label for="asunto">Asunto del documento</label>
        <input type="text" class="form-control" id="asunto"/>
      </fieldset>
      <fieldset class="form-group">
        <label for="descripcion">Cuerpo del documento</label>
        <textarea class="form-control" id="descripcion"></textarea>
        <script type="text/javascript">
          CKEDITOR.replace('descripcion');
        </script>
      </fieldset>
    </form>

  </div>
</div>
<#include "footer.ftl">