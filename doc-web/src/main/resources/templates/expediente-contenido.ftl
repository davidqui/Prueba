<#assign pageTitle = "Contenido " + expediente.nombre />
<#include "bandeja-header.ftl">
<#import "spring.ftl" as spring />

<div class="container-fluid">
	<#assign expid=eid />

    <table class="table">
    <thead>
        <tr>
            <th colspan="2">Nombre</th>
            <th>Fecha</th>
            <th>Serie,Subserie / Tipología</th>
            <th>Nivel Seguridad</th>
            <th>Trabajado Por</th>
            <th>Página</th>
            <th>Descripción</th>
            <th>Acciones</th>
        </tr>
    </thead>
    <#list documentos as doc>
        <tr>
            <td colspan="2"><a href="/documento?pin=${doc.instancia.id}">${doc.asunto}</a></td>
            <td nowrap>${doc.cuando?string('yyyy-MM-dd')}</td>
            <td nowrap><#if doc.trd??> ${doc.trd.nombre}</#if></td>
            <td nowrap>${doc.clasificacion.nombre}</td>
            <td nowrap>${utilController.nombre(doc.quien)}</td>
            <#if doc.paginas??>
            <td nowrap>${doc.paginas}</td>
            <#else>
            <td></td>
            </#if>
            <#if doc.descripcion?? >
            <td nowrap> <a tabindex="0" class="btn btn-sm btn-info bd-popover" role="button" data-toggle="popover" data-trigger="hover" data-placement="left" title="Descripción" data-content="${doc.descripcion}">D</a></td>
            <#else>
            <td></td>
            </#if>
            <td nowrap><a id="modDocExp" href="#" class="btn btn-sm btn-warning bd-popover" role="button" data-toggle="popover" data-trigger="hover" data-placement="left" title="Modificar documento" data-content="Puse para modificar el asunto y/o la descripción del documento" ' onclick='showModalUpdateDoc("${doc.id}");'>M</a></td>
            <td nowrap><a id="modMoveDoc" href="#" tabindex="0" class="btn btn-sm btn-success bd-popover" role="button" data-toggle="popover" data-trigger="hover" data-placement="left" title="Mover Documento" data-content="Pulse para movel el documentoa otro expediente." onclick='moveDoc("${doc.id}");'>MD</a></td>
        </tr>
        <#if doc.adjuntos?? && (doc.adjuntos?size > 0) >
            <#list doc.adjuntos as adj>
                <tr>
                    <td></td>
                    <td>
                        <a href="/documento/adjunto?doc=${doc.id}&dad=${adj.id}">${adj.tipologia.nombre} - ${adj.original}</a>
                    </td>
                    <td nowrap>${adj.cuando}</td>                    
            		<td>${adj.tipologia.nombre}</td>
            		<td nowrap>${doc.clasificacion.nombre}</td>
            		<td nowrap>${utilController.nombre(adj.quien)}</td>
            		<td nowrap></td>
                </tr>
            </#list>
        </#if>
    </#list>
    </table>
</div>

<#--Modal que muestra el nombre y la descripción del documento para modificar -->
<div id="modalUpdateDoc" class="modal fade">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
        <h4 class="modal-title">Modificar</h4>
        
      </div>
            
      <div class="modal-body">
      	<p>A continuación ingrese el nuevo nombre y descripción para el adjunto</p>
      	<span id="docId" style="display: none;"></span>
        <div class="form-group">
    		<label for="exampleInputEmail1">Nombre</label>
    		<input type="text" class="form-control" id="nombreDoc">
    		<label for="exampleInputEmail1">Descripción</label>
    		<textarea id="descripcion" class="form-control" rows="3"> </textarea>
  		</div>
      </div>
     
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cerrar</button>
        <button id="saveNameDoc" type="button" class="btn btn-primary">Guardar</button>
      </div>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
</div><!--Fin /.modal -->


<script>
<!--

function addIdDoc(data) {

	$('#modalMenuMod').modal("show");
	$('#docId').text(data);
	
				
}


-->
<#--Dispara el modal que permite realizar modificaciones sobre el nombre y descripción del documento  -->
function showModalUpdateDoc(docid) {

	$('#docId').text(docid);
	$('#modalUpdateDoc').modal("show");
	
			  		
	$.get('/documento/get-info', {
	 			
			docId : docid,
			}, function(data) {
			
			var asunto;
			var desc;
			for(i in data) {
			
				asunto=i;
				desc=data[i]
			}
			$('#nombreDoc').val(asunto);
			$('#descripcion').val(desc);
				

			});
					
}
<#--Lleva al controlador los datos modificados asunto y descripción del documento -->
$('#saveNameDoc').click(
			function() {
			
		
		 $.post("/documento/update-info",{
    		
        	docId: $('#docId').text(),
        	asunto:$('#nombreDoc').val(),
			desc:$('#descripcion').val() },
			
			function(data) {
			
			$('#modalUpdateDoc').modal("show");
			window.location.reload();
			
			
	  	});    	
});
<#--Muestra el listado de expedientes a los que se puede mover el documento -->

function moveDoc(data) {

	$('#docId').text(data);
	
	window.location = "/expediente/expedientes-dependencia?did="+data+"&eid="+${expid};
	
					
}


</script>
<#include "bandeja-footer.ftl">
