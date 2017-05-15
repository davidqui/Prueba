<#assign pageTitle = documento.asunto!"Documento" />

<#assign instancia = documento.instancia />
<#assign mode = documento.mode />
<#assign deferredJS = "" />


<div class="col-md-8">
    <#if relacionado??>
        <strong>Documento relacionado:</strong> <a target="_blank" href="/documento?pin=${relacionado.instancia.id}">${relacionado.asunto!"&lt;Sin asunto&gt;"}</a>
    </#if>
    <table class="table table-sm">    	
        <#if mode.expediente_view && documento.expediente?? >
            <tr><th>Expediente</th><td>${documento.expediente.nombre}</td></tr>
        </#if> 
        <#if mode.radicado_view && documento.radicado?? >
            <tr><th>Radicado</th><td>${documento.radicado}</td></tr>
        </#if> 
        <#if !mode.radicadoOrfeo_edit && mode.radicadoOrfeo_view && documento.radicadoOrfeo?? >
            <tr><th>Radicado de otro ORFEO</th><td>${(documento.radicadoOrfeo)!""}</td></tr>
        </#if> 
        <#if !mode.trd_edit && mode.trd_view >
            <tr><th>TRD</th><td>${(documento.trd)!""}</td></tr>
        </#if> 
        <#if !mode.destinatario_edit && mode.destinatario_view >
            <#if (documento.dependenciaDestino)?? >
	            <tr><th>Dependencia destino</th><td>${(documento.dependenciaDestino)!""}</td></tr>
            </#if>
            <#if (documento.destinatarioNombre)?? >
            	<tr><th>Nombre destinatario</th><td>${(documento.destinatarioNombre)!""}</td></tr>
            </#if>
            <#if (documento.destinatarioTitulo)?? >
    	        <tr><th>Título destinatario</th><td>${(documento.destinatarioTitulo)!""}</td></tr>
            </#if>
            <#if (documento.destinatarioDireccion)?? >
	            <tr><th>Dirección destinatario</th><td>${(documento.destinatarioDireccion)!""}</td></tr>
            </#if>
        </#if> 
        <#if !mode.remitente_edit && mode.remitente_view >
            <#if (documento.dependenciaRemitente)?? >
            	<tr><th>Remintente</th><td>${(documento.dependenciaRemitente)!""}</td></tr>
            </#if>
            <#if (documento.remitenteNombre)?? >
        	    <tr><th>Nombre remitente</th><td>${(documento.remitenteNombre)!""}</td></tr>
            </#if>
            <#if (documento.remitenteTitulo)?? >
    	        <tr><th>Título remitente</th><td>${(documento.remitenteTitulo)!""}</td></tr>
            </#if>
            <#if (documento.remitenteDireccion)?? >
	            <tr><th>Dirección remitente</th><td>${(documento.remitenteDireccion)!""}</td></tr>
            </#if>
        </#if> 
        <#if !mode.numeroOficio_edit && mode.numeroOficio_view && documento.numeroOficio?? >
            <tr><th>Número de oficio</th><td>${documento.numeroOficio!""}</td></tr>
        </#if>
        <#if !mode.numeroBolsa_edit && mode.numeroBolsa_view && documento.numeroBolsa?? >
            <tr><th>Número de bolsa de seguridad</th><td>${documento.numeroBolsa!""}</td></tr>
        </#if>
        <#if !mode.fechaOficio_edit && mode.fechaOficio_view && documento.fechaOficio?? >
            <tr><th>Fecha del oficio</th><td><#if documento.fechaOficio?? >${yyyymmdd.format(documento.fechaOficio)}<#else>&lt;No definido&gt;</#if></td></tr>
        </#if>
        <#if !mode.numeroFolios_edit && mode.numeroFolios_view && documento.numeroFolios?? >
            <tr><th>Número de folios</th><td>${documento.numeroFolios!""}</td></tr>
        </#if>
        <#if !mode.plazo_edit && mode.plazo_view && documento.plazo??  >
            <tr><th>Plazo</th><td><#if documento.plazo?? >${yyyymmdd.format(documento.plazo)}<#else>&lt;No definido&gt;</#if></td></tr>
        </#if>
        <#if !mode.clasificacion_edit && mode.clasificacion_view && documento.clasificacion?? >
            <tr class="table-danger"><th>Clasificación</th><td>${(documento.clasificacion.nombre)!""}</td></tr>
        </#if>
    </table>
    	
       
        <#if mode.expediente_edit >
            <fieldset class="form-group">
                <label for="expediente">Expediente</label>
                <@spring.bind "documento.expediente" />
                <select class="form-control" id="expediente" name="${spring.status.expression}">
                    <#if expedientes??>
                        <option value=""></option>
                        <#list expedientes as exp>
                        <#if spring.status.value?? && exp == spring.status.value >
                            <option value="${exp.id}" selected="selected">${exp.nombre}</option>
                        <#else>
                            <option value="${exp.id}">${exp.nombre}</option>
                        </#if>
                        </#list>
                    </#if>
                </select>
                <div class="error">
                    <@spring.showErrors "<br>"/>
                </div>
            </fieldset>
        </#if>
       
        <#if mode.plantilla_edit >
            <fieldset class="form-group">
                <fieldset class="form-group">
			        <label for="file">Plantilla de Office Word (*.docx)</label>
			        <#if documento.docx4jDocumento?has_content>
			        	<a href="/documento/download/${documento.docx4jDocumento}">Descargar plantilla actual</a>
			        </#if>	
			        <input class="form-control" type="file" name="file" id="file">
			  	</fieldset>			                    
                <div class="error">
                    <@spring.showErrors "<br>"/>
                </div>
            </fieldset>		
        </#if>
        <div class="error">
            <@spring.showErrors "<br>"/>
        </div>

		
    </form>
	
    <!--
		Observaciones
	-->
    <#if mode.observaciones_view && documento.mostrarPreview() == "Y">        	
        <div class="card m-y">                           		        
	            <#if (documento.observaciones)??>
	                <div class="card-block" id="obsDiv">
	                    <h5>Observaciones</h5>
	                    <#list documento.observaciones as obs>
	                        <hr/>
	                        <strong>${utilController.nombre(obs.quien)}</strong>, <em>${obs.cuando}</em>
	                        <p>${obs.texto}</p>
	                    </#list>
	                </div>
	        	</#if>
            <#if mode.observaciones_edit>
                <div class="card-block cus-gray-bg">
                    <form method="post" id="obsForm" >
                        <fieldset class="form-group">
                            <textarea class="form-control" id="observacion" name="observacion"></textarea>
                        </fieldset>
                        <a href="#" class="btn btn-secondary btn-sm" id="obsButton">Comentar</a>
                    </form>
                </div>
                <#assign deferredJSObs>
                    <script type="text/javascript">
                    <!--
                        $("#obsButton").click(function(event){
                            event.preventDefault();
                            $.ajax({
                                type: "POST",
                                url: "/documento/observacion?doc=${documento.id}",
                                data: $("#obsForm").serialize(),
                                success: function(data) {
                                    var hr = $("<hr/>");
                                    hr.appendTo("#obsDiv");
                                    var strong = $("<strong/>");
                                    strong.text(data.quien + ", ");
                                    strong.appendTo("#obsDiv");
                                    var em = $("<em/>");
                                    em.text(data.cuando);
                                    em.appendTo("#obsDiv");
                                    var p = $("<p/>");
                                    p.html(data.texto);
                                    p.appendTo("#obsDiv");
                                    $("#observacion").val('');
                                }
                            });
                        });
                    -->
                    </script>
                </#assign>
                <#assign deferredJS = deferredJS + deferredJSObs />
            </#if>
        </div>
    </#if>
</div>
<div class="col-md-4">
    <div class="card">
        <div class="card-header">
            <a href="/proceso/instancia/detalle?pin=${instancia.id}">Proceso</a>
        </div>
        <div class="card-block">
            <strong>Fecha de creación:</strong> ${instancia.proceso.cuando?string('yyyy-MM-dd hh:mm a')}<br/>
            <strong>Proceso:</strong> ${instancia.proceso.nombre}<br/>
            <strong>Estado:</strong> ${instancia.estado.nombre}<br/>
            <strong>Usuario asignado:</strong> ${instancia.asignado.nombre}<br/>
            <strong>Enviado por:</strong> ${documento.usuarioUltimaAccion!"&lt;Nadie&gt;"}<br/>
            <strong>Elabora:</strong> ${documento.elabora!"&lt;Nadie&gt;"}<br/>
            <strong>Revisó:</strong> ${documento.aprueba!"&lt;Nadie&gt;"}<br/>
            <strong>Visto bueno:</strong> ${documento.vistoBueno!"&lt;Nadie&gt;"}<br/>
            <strong>Firma:</strong> ${documento.firma!"&lt;Nadie&gt;"}<br/>
        </div>
    </div>
    
    <#if (documento.vistosBuenos?size > 0) >
	    <div class="card">
	        <div class="card-header">
	            Vistos buenos
	        </div>
	        <div class="card-block">
	            <#list documento.vistosBuenos as vb>                	                        
                        <strong>Fecha:</strong> ${vb.fecha?string('yyyy-MM-dd hh:mm a:ss')}<br/>
                        <strong>Usuario:</strong> ${vb}<br/>
                        <hr/>
                </#list>
	        </div>
	    </div>
    </#if>
    
    <!--
        Adjuntos
    -->
    <#if mode.adjuntos_view >
        <div class="card">
            <#if (documento.adjuntos?size > 0) >
                <div class="card-block">
                    <h5 class="m-b">Adjuntos actuales</h5>
                    <#list documento.adjuntos as adj>
                    	<#if adj.activo>
	                        <hr/>
	                        <strong>${adj.tipologia.nombre}</strong><br/>
	                        <em>Subido el ${yyyymmdd.format(adj.cuando)} por ${utilController.nombre(adj.quien)}</em>
	                        <a href="/documento/adjunto/${adj.id}/eliminar?pin=${instancia.id}" onclick="return confirm('¿Está seguro que desea eliminar el archivo ${adj.tipologia.nombre}?');">Eliminar</a><br/>
	                        <a href="#" onclick="visualizar('/ofs/viewer?file=/ofs/download/${adj.contenido}')">
	                            <img src="/ofs/download/tmb/${adj.contenido}" /><br/>
	                            ${adj.original}
	                        </a><br/>
	                    </#if>
                    </#list>
                </div>
            </#if>
            <#if mode.adjuntos_edit>
                <div class="card-block cus-gray-bg">
                    <form action="/documento/adjunto?doc=${documento.id}" method="post" enctype="multipart/form-data">
                        <fieldset class="form-group">
                            <label for="destinatario">Tipología y archivo</label>
                            <select class="form-control" id="tipologia" name="tipologia">
                                <#if tipologias??>
                                    <option value=""></option>
                                    <#list tipologias as tip>
                                        <option value="${tip.id}">${tip.nombre}</option>
                                    </#list>
                                </#if>
                            </select>
                            <input type="file" class="form-control" id="archivo" name="archivo"/>
                        </fieldset>
                        <button type="submit" class="btn btn-secondary btn-sm">Subir</button>
                    </form>
                </div>
            </#if>
        </div>
    </#if>
    
    <!--
        Formatos
    -->
    <!-- Visualiza los formatos seleccionados -->
    <#if mode.formatos_view >
    	<div class="card">
    		<#if (documento.formatos?size > 0) >
                <div class="card-block">
                    <h5 class="m-b">Formatos actuales</h5>
                    <#list documento.formatos as form>
                        <hr/>
                        <em>Subido el ${yyyymmdd.format(form.cuando)} por ${utilController.nombre(from.quien)}</em><br/>
                        <a href="#" onclick="visualizar('/ofs/viewer?file=/ofs/download/${from.contenido}')">
                            <img src="/ofs/download/tmb/${from.contenido}" /><br/>
                            ${from.original}
                        </a><br/>
                    </#list>
                </div>
            </#if>
            
            <#if mode.formatos_edit>
                <div class="card-block cus-gray-bg">
                    <form action="">
                        <fieldset class="form-group">
                            <label>Plantillas disponibles ( .docx )</label>   
                            	<div class="card-block">
                            	<#list plantillas as pla>
                            	                            		
							            <strong>${pla.nombre}</strong>
							            ||
							            <#if pla.docx4jDocumento?has_content>		                                    
		                                	<a href="/documento/download/${pla.docx4jDocumento}"> Descargar </a> <br/>
		                                <#else>
		                                    <label style="color:#d9534f;">
												SIN PLANTILLA. 
											</label>
											<br/>
		                                </#if>
		                        </#list>
		                        </div>
                        </fieldset>
                    </form>
                </div>
            </#if>
            <br />
            
            <!--Permite subir el formato -->
            <#if mode.formatos_edit>
                <div class="card-block cus-gray-bg">
                    <form action="">
                        <fieldset class="form-group">
                            <label for="destinatario">Formato</label>
                      
                            <input type="hidden" id="trd2" />
                            	<div class="input-group">
                      				
                      				<div class="input-group-btn">
                        				<button class="btn btn-primary" type="button" onclick='showModalSeries();'>
                            				Descargar formato
                        				</button>
                      				</div>
                    			</div>
                        </fieldset>
                    </form>
                </div>
            </#if>
            <!-- trdModal2 -->
            <div class="modal fade" id="trdFormModal" tabindex="-1" role="dialog" aria-labelledby="trdModalLabel2" aria-hidden="true">
              <div class="modal-dialog modal-lg" role="document">
                <div class="modal-content">
                  <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar">
                      <span aria-hidden="true">&times;</span>
                      <span class="sr-only">Cerrar</span>
                    </button>
                    <h4 class="modal-title" id="myModalLabel2">Selección de serie</h4>
                    <div class="seriesList"></div>
                  </div>
                  <div class="modal-body">
                  </div>
                </div>
              </div>
            </div>
           
         
        </div>
    </#if>
    
    <!--
        Sticker
    -->
    <#if mode.sticker_view && documento.sticker??>
        <div class="card">
            <div class="card-header">
                Sticker
            </div>
            <iframe src="/ofs/viewer?file=/ofs/download/${documento.sticker}" width="100%" height="230px"></iframe>
        </div>
    </#if>
        
</div>

<!-- 

    Visor de PDF
-->

<div class="modal fade" id="visualizador">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
          <span class="sr-only">Close</span>
        </button>
      </div>        
      <iframe id="iframeVisor" width="100%" height="600px"></iframe>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
	
<script type="text/javascript">
<!--
    function visualizar(url) {
        var frame = $("#visualizador iframe");
        frame.attr('src', url);
        $('#visualizador').width('100%');
        $('#visualizador .modal-dialog').width('95%');
        h = $(window).height();
        $('#visualizador').height(h + 'px');        
        $('#visualizador').modal('show');
        $('#visualizador iframe').height((h - 125) + 'px');
    }
-->
</script>
<script type="text/javascript">
 <!--
    function showModalSeries() {
    	
       $("#trdFormModal .seriesList").empty();                            
       $('#trdFormModal').modal("show");
       $("#myModalLabel2").text("Selección de serie");
       $.getJSON("/trd/series.json", function(series) {
           	var divTagList = [];
            	$.each(series, function(i, item){
                                                
                        divTagList.push('<div><a href="#" id="serie2' + item.id + '" class="mark-serie2"');
                        divTagList.push(' '); 
                        divTagList.push('onclick="showModalSubserie(');  
                        divTagList.push(item.id);
                        divTagList.push(')"');
                        divTagList.push('>' + item.codigo + " - " + item.nombre + '</a></div>');
                });
                $("<div/>", { html: divTagList.join("") }).appendTo("#trdFormModal .seriesList");

                   	$("#trdFormModal .seriesList .mark-serie");
        });
                            
      };
      
      function showModalSubserie(e) {
                  
          var id = e;
          var nombre = $(this).text();
		  $("#trdFormModal .seriesList").empty();
		  $('#trdFormModal').modal("show");
          $("#myModalLabel2").text("Selección de subserie");

          $.getJSON("/trd/subseries.json?serieId=" + id, function(subseries) {
               var subseriesList = [];
               $.each(subseries, function(i, item){
                      subseriesList.push('<div><a href="#" id="serie2' + item.id + '" class="mark-serie2">' + item.codigo + " - " + item.nombre + '</a></div>');
               });

               $("<h5/>", { html: nombre }).appendTo("#trdFormModal .seriesList");
               $("<a/>", {html: "Regresar", id:'btnRegresar'}).appendTo("#trdFormModal .seriesList");
               $("<div/>", {html: subseriesList.join("") }).appendTo("#trdFormModal .seriesList");
                                       
               $("#btnRegresar").click(showModalSeries);
                                       
			   $("#trdFormModal .seriesList .mark-serie2").click(function(e) {
			   
			   		$("#trdFormModal .seriesList").empty(); 
			   		$("#myModalLabel2").text("Selección de formato");
                    var id = $(this).attr("id").substring(6);
                    var nombre = $(this).text();
                                            
                    $("#trd2").val(id);
                    $("#trdNombre2").text(nombre);
                    
                    $.get('/formato/get-list', {
                    
                    	
	 			
						subId : id,
					}, function(formatos) {
					
						var formatosList = [];
						$.each(formatos, function(i, item){
							
							formatosList.push('<div><a href="/formato/download/' + item.contenido + '">' + item.original + " - " + item.descripcion + '</a></div>');
							formatosList.push('<p id="formContent" hidden>');
							formatosList.push(item.contenido);
							formatosList.push(' </p>');
               			});
               		
               		
               			$("<a/>", {html: "Regresar", id:'btnRegresar'}).appendTo("#trdFormModal .seriesList");
               			$("<div/>", {html: formatosList.join("") }).appendTo("#trdFormModal .seriesList");
               			$("#btnRegresar").click(showModalSeries);
               			
               	});
                    
             });
           });
           
       };

                        
 -->
</script>
<script>

    function cargarHtmlAplantilla() {    	    	
    	$("#contenido").val( tinyMCE.get('id_text_area_html').getContent() );
    	//CKEDITOR.replace( 'contenido' );
    	location.reload();
    }
	tinymce.init(
		{ 
			selector:'#id_text_area_html',
			readonly : 1,
			menubar:false,
    		statusbar:false,
    		toolbar:false
		});
</script>

<#include "bandeja-footer.ftl" />
