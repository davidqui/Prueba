  <#include "bandeja-header.ftl">
 <#include "util-macros.ftl" />


 <div class="container-fluid">
   <h3>Expedientes  y Documentos en calidad de prestamo</h3>
   <div>
	<span>
		<a href="/archivo/solicitud/form?uid=${uid}" class="btn btn-success btn-sm bd-popover" role="button" data-toggle="popover" data-trigger="hover" data-placement="right" title="Archivo Central" data-content="Pulse para realizar una solicitud de prestamo al Archivo Central">Solicitar prestamo de expedientes</a>
	</span>
	<span>
		<a href="/archivo/solicitud/prestamo-solicitud-doc?uid=${uid}" class="btn btn-primary btn-sm bd-popover" role="button" data-toggle="popover" data-trigger="hover" data-placement="right" title="Archivo Central" data-content="Pulse para realizar una solicitud de prestamo de un documento al Archivo Central">Solicitar prestamo de documentos</a>
	</span>
	</div>
	</br>
   
   <ul class="nav nav-tabs">
      <li class="nav-item">
        <a href="/prestamo/list-exp-prestados" class="nav-link <#if activeNav??&& activeNav== 'expedientes'>active</#if> " >Expedientes</a>
    </li>
    <li class="nav-item">
    	<a href="/prestamo/list-doc-prestados" class="nav-link <#if activeNav??&& activeNav== 'documentos'>active</#if>">Documentos</a>
     </li>
</ul>



