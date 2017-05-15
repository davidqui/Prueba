 <#assign pageTitle = "Archivo Central Documentos"/>
  <#include "archivo-header.ftl">
 <#include "util-macros.ftl" />


 <div class="container-fluid">
   <h3>Documentos del Archivo Central</h3>
   <ul class="nav nav-tabs">
      <li class="nav-item">
        <a href="/archivo/list-archivados-doc" class="nav-link <#if activeNav??&& activeNav== 'archivados'>active</#if> " >Archivados</a>
    </li>
    <li class="nav-item">
    	<a href="/prestamo/list-prestamos-doc" class="nav-link <#if activeNav??&& activeNav== 'prestados'>active</#if>">Prestados</a>
     </li>
</ul>



