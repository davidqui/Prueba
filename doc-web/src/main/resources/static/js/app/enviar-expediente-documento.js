function seleccionarExpediente(expId){
    var expedientes = $("#modal-enviar-expediente").find(".expediente-list");
    expedientes.removeClass("active");
    $("#expediente-"+expId).addClass("active");
    $("#expedienteDestino").val(expId);
}

function postSeleccionarExpediente(expId, pinId){
    $.ajax({
        method: "POST",
        url: "/documento/agregar-documento-exp/"pinId+"/"+expId
    }).always(function () {
        location.reload();
    });
}