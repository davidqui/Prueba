function notificar(){
    $.ajax({
    url: "/notificaciousuario/retorna-notificaciones",
    success: function(data) {
        $("#modal-notificaciones .modal-body").empty();
            var content = '<p>En este panel de configuración puede desactivar las notificaciones del sistema.</p>';
            content = content + '<table class="table">';
                content = content +'<tr> <th>TIPO DE NOTIFICACIÓN</th><th>ESTADO</th><th>ACCIÓN</th></tr>';
                $.each(data, function(i, item) {
                    content = content +'<tr> <td>'+item[1]+'</td>';
                    
                    if(item[2] === 1){
                        content = content +'<td> <span id="span_'+item[0]+'" class="label label-success">Activo</span></td>';
                        content = content +'<td><button id="button_'+item[0]+'" type="button" class="btn btn-default" onclick="cambioEstado('+item[0]+')">Desactivar</button></td></tr>';
                    }else{
                        content = content +'<td> <span id="span_'+item[0]+'" class="label label-danger">Inactivo</span></td>';
                        content = content +'<td><button id="button_'+item[0]+'" type="button" class="btn btn-primary" onclick="cambioEstado('+item[0]+')">Activar</button></td></tr>';
                    }
                });
            content = content +'</table>';        
            $("#modal-notificaciones .modal-body").append(content);
    }
    });
};

function cambioEstado(tnfId){
    $.ajax({
    type: "POST",
    url: "/notificaciousuario/cambiaEstado/"+ tnfId,
    success: function(data) {
        if(data === 0){
            $('#span_'+tnfId).removeClass("label label-success");
            $('#span_'+tnfId).addClass("label label-danger");
            $("#span_"+tnfId).html("Inactivo");
            
            $('#button_'+tnfId).removeClass("btn btn-default");
            $('#button_'+tnfId).addClass("btn btn-primary");
            $("#button_"+tnfId).text("Activar");
        }else{
            $('#span_'+tnfId).removeClass("label label-danger");
            $('#span_'+tnfId).addClass("label label-success");
            $("#span_"+tnfId).html("Activo");
            
            $('#button_'+tnfId).removeClass("btn btn-primary");
            $('#button_'+tnfId).addClass("btn btn-default");
            $("#button_"+tnfId).text("Desactivar");
        }    
    }
    });
};

