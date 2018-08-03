function seleccionarExpediente(expId){
    var expedientes = $("#modal-enviar-expediente").find(".expediente-list");
    expedientes.removeClass("active");
    $("#expediente-"+expId).addClass("active");
    $("#expedienteDestino").val(expId);
    $("#submit-button").css('display', 'initial');
}

function submitSeleccionarExpediente(pinId){
    console.log("ENVIA");
    var expId = $("#expedienteDestino").val();
    $.ajax({
        method: "POST",
        url: "/documento/addDocExpediente/"+pinId+"/"+expId
    }).always(function () {
        location.reload();
    });
}