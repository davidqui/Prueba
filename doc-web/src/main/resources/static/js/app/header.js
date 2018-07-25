function notificar(){
    $.ajax({
    url: "/notificaciousuario/retorna-notificaciones",
    success: function(data) {
        $("#modal-notificaciones .modal-body").empty();
            var content = '<p>En este panel de configuración puede desactivar las notificaciones del sistema.</p>';
            content = content + '<table class="table">';
                $.each(data, function(i, item) {
                        content = content +'<tr> <td>'+item[1]+'</td>';
                    if(item[2] === 1){
                        content = content +'<td><button id="button_'+item[0]+'" type="button" class="btn btn-success" onclick="cambioEstado('+item[0]+')">Activo</button></td></tr>';
                    }else{
                        content = content +'<td><button id="button_'+item[0]+'" type="button" class="btn btn-warning" onclick="cambioEstado('+item[0]+')">Inactivo</button></td></tr>';
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
        var buttonVerificar = $('button_'+tnfId);
        if(data === 0){
            $('#button_'+tnfId).removeClass("btn btn-success");
            $('#button_'+tnfId).addClass("btn btn-warning");
            $("#button_"+tnfId).text("Inactivo");
        }else{
            $('#button_'+tnfId).removeClass("btn btn-warning");
            $('#button_'+tnfId).addClass("btn btn-success");
            $("#button_"+tnfId).text("Activo");
        }    
    }
    });
};

