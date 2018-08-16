function seleccionarExpediente(expId){
    var expedientes = $("#modal-enviar-expediente").find(".expediente-list");
    expedientes.removeClass("active");
    $("#expediente-"+expId).addClass("active");
    $("#expedienteDestino").val(expId);
    $("#submit-button").css('display', 'initial');
}

function submitSeleccionarExpediente(pinId){
    var expId = $("#expedienteDestino").val();
    $.ajax({
        method: "POST",
        url: "/documento/addDocExpediente/"+pinId+"/"+expId
    }).always(function () {
        location.reload();
    });
}

$(document).ready(function(){
  $("#buscardor-expediente").on("keyup", function() {
    var value = $(this).val().toLowerCase();
    $("#lista-expedientes button").filter(function() {
      $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
    });
  });
});