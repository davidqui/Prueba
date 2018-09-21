function modalDestinatario(id, nombre) {    
    $(".div-loader").css({ display: "block" });
    $.ajax({
        method: "POST",
        url: "/expediente/cargos-usuario/" + id 
    }).always(function (cargos) {
        $(".div-loader").css({ display: "none" });
        console.log("nombre "+id +" + "+nombre);
        $("#destinoUsuario_visible2").val(nombre);
        $("#cargoAsignado2 option").remove();
        $.each(cargos, function(index, item) {
            console.log(item.id);
            $("#cargoAsignado2").append("<option value=" + item.id + ">" + item.nombre + "</option>");
        });
    });
}

function recibirDestinatario(idTransferencia) {
    var cargoID = $("#cargoAsignado2").val();
    if (cargoID === undefined || $.trim(cargoID) === "") {
        alert("Debe seleccionar un cargo del usuario.");
        return;
    }
    $.ajax({
        method: "POST",
        url: "/transferencia-archivo/recibir-destinatario/" + idTransferencia+"/" + cargoID
    }).then(function() {
        $(".div-loader").css({ display: "none" });
        location.reload();
      }, function(message) {
        $(".div-loader").css({ display: "none" });
        $('#cambiarUsuarioCreador').modal('hide');
        $('#info-modal').modal('show');
        $('#title-modal').html("Advertencia");
        $('#modal-body-info').html("<h5>"+message.responseText+"</h5>");
        $('#info-modal').modal('show'); 
    });
}

function rechazarTransferencia(idTransferencia) {
    var pObservacion = $("#observacion").val();
    
    if (pObservacion === undefined || $.trim(pObservacion) === "") {
        alert("Señor usuario debe justificar el porqué de su rechazo.");
        return;
    }
    
    $.ajax({
        method: "POST",
        url: "/transferencia-archivo/rechazar/" + idTransferencia,
        data: {observacion: pObservacion},
        dataType: 'html'
    }).then(function() {
        $(".div-loader").css({ display: "none" });
        location.reload();
      }, function(message) {
          console.log(message);
    });
}

//function reenviarTransferencia(idTransferencia) {
//    $.ajax({
//        method: "POST",
//        url: "/transferencia-archivo/verificar-transferencia/" + idTransferencia,
//        dataType: 'html'
//    }).then(function(docs) {
//        if (docs != null && docs.length > 0) {
//            var text = " los siguientes documentos ya no se encuentran en su custodia: </br> </br>";
//            $.each(docs, function(index, item) {
//                text += item.asunto +" -- "+ item.fecha+"</br>";
//            }
//            $('#confirmar-reenvio-modal').modal('show');
//            $('#title-modal').html("Documentos sin custodia");
//            $('#modal-body-info').html("<h5>"+message.responseText+"</h5>");
//            
//        }else{
//            reenviarTransferenciaUsuario(idTransferencia)
//        }
//        $(".div-loader").css({ display: "none" });
//        location.reload();
//      }, function(message) {
//          console.log(message);
//    });
//}


function reenviarTransferenciaUsuario(idTransferencia) {
    var usuario = $("#destinoUsuario").val();
    var pJustificacion = $("#justificacion").val();
    
    if (usuario === undefined || $.trim(usuario) === "") {
        alert("Debe seleccionar un usuario.");
        return;
    }
    
    if (pJustificacion === undefined || $.trim(pJustificacion) === "") {
        alert("Debe especificar una justificación.");
        return;
    }
    
    $.ajax({
        method: "POST",
        url: "/transferencia-archivo/devolver/" + idTransferencia+"/"+usuario,
        data: {justificacion: pJustificacion},
        dataType: 'html'
    }).then(function() {
        location.reload();
      }, function(message) {
          console.log("ESTE ES EL MENSAJE ",message);
            $('#title-modal-message').html("Advertencia");
            $('#modal-body-info-message').html("<h5>"+message.responseText+"</h5>");
            $('#info-modal').modal('show'); 
    });
}

function setObservacionDefecto(select, observacionesTextAreaID) {
    var texto = $.trim($(select).children("option").filter(":selected").text());
    if (texto !== '') {
        $("#" + observacionesTextAreaID).val(texto);
    }

    $(select).find('option:eq(0)').prop('selected', true);
}

function mostrarFuid(fuid){
    $("#visualizarFuid .modal-body").empty();
    $("#visualizarFuid .modal-body").append("<div class=\"card\">");
    $("#visualizarFuid .modal-body").append("<iframe src=\"/ofs/viewer?file=/ofs/download/"+fuid+"\" width=\"100%\" height=\"700px\"></iframe>");
    $("#visualizarFuid .modal-body").append("</div>");
}