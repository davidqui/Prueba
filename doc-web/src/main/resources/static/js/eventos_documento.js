$(document).ready(function() {
	$("#seleccion_tipo_documento").change(function() {
		$(this).find("option:selected").each(function() {
			var valor = $(this).attr("value");			
			if ( valor == "121" ) {
				$("#contenidoTextoEnArchivo").hide();
				$("#contenidoEnArchivo").show();
				$("#id_div_mostra_text_area_html").hide();	
				$("#contenido").val("");
			} else if( valor >= 122 ){
				$("#contenidoTextoEnArchivo").show();
				$("#contenidoEnArchivo").hide();
				$("#id_div_mostra_text_area_html").show();								
			}else{
				$("#contenidoTextoEnArchivo").hide();
				$("#contenidoEnArchivo").hide();
				$("#id_div_mostra_text_area_html").hide();
				$("#contenido").val("");
			}
			$.get( "/documento/documento_html_seleccionado?id="+valor, function( data  ) {
				$("#id_text_area_html").val( data );
				tinyMCE.get('id_text_area_html').setContent(data);				
			});	 			
		});		
		
	}).change();
});