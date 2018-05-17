function enviarComentario(idAreaObservaciones, documentoID) {
    $.ajax({
        method: "POST",
        url: "/documento-acta/observacion",
        data: {
            documentoID: documentoID,
            observacionTexto: document.getElementById(idAreaObservaciones).value
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

