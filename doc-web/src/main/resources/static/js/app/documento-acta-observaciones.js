function enviarComentario(idAreaObservaciones, documentoID) {

    var textoObservaciones = $("#" + idAreaObservaciones).val();
    if ($.trim(textoObservaciones) === "") {
        var errMsg = "Debe ingresar un comentario";
        $("#msg-enviar-comentario").text(errMsg);
        console.log(errMsg);
        return;
    }

    $.ajax({
        method: "POST",
        url: "/documento-acta/observacion",
        data: {
            documentoID: documentoID,
            observacionTexto: textoObservaciones
        }
    }).done(function () {
        location.reload();
    }).fail(function () {
    }).always(function (msg) {
        document.getElementById(idAreaObservaciones).value = '';
        $("#msg-enviar-comentario").text(msg);
        console.log(msg);
    });
}

