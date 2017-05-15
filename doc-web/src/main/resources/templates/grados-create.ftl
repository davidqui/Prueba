<#assign pageTitle = grado.nombre!"Grado" />
<#assign mode = grado.mode!"" />
<#assign deferredJS = "" />
<#import "spring.ftl" as spring />
<#include "admin-header.ftl">
<#include "util-macros.ftl" />

<div class="container">
  <div class="row">
    <h1 class="cus-h1-page-title">${pageTitle}</h1>
	<@flash/>
    <form action="/grados/save" method="POST" enctype="multipart/form-data" >
      
      <fieldset class="form-group">
        <label for="id">Sigla</label>
        <#if grado.id??>
        	<input type="text" class="form-control" id="id" name="id" value="${grado.id}" readonly="readonly"/><br />
       	<#else>
       		<input type="text" class="form-control" id="id" name="id" /><br />
        </#if>
      </fieldset>
            
      <fieldset class="form-group">
         <label for="nombre">Nombre</label>
        <input type="text" class="form-control" id="nombre" name="nombre" value="${(grado.nombre)!""}"/>
      </fieldset>

      <div class="m-y">
      	<button id="btnGuardar" type="submit" class="btn btn-primary">Guardar</button>
      </div>
      
    </form>

  </div>
</div>




<#include "footer.ftl">