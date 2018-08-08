$("#obsButton").click(function(event){
    event.preventDefault();
    var expId = $("#expId").val();
    console.log("expId? "+expId);
    var expObs = $.trim( $("#observacion").val() );
    if( expObs.length <= 0 ){
        alert("Debe ingresar una observación");
    }else{
        $.ajax({
            type: "POST",
            url: "/expediente/observacion?expId="+expId,
            data: $("#obsForm").serialize(),
            success: function(data) {
                
                var hr = $("<hr/>");
                $("#obsDiv").prepend(hr);
                
                var p = $("<p/>");
                p.html(data.texto);
                $("#obsDiv").prepend(p);
                
                var em = $("<em/>");
                em.text(data.cuando);
                $("#obsDiv").prepend(em);
                
                var strong = $("<strong/>");
                strong.text(data.quien + ", ");
                $("#obsDiv").prepend(strong);
                
                $("#observacion").val('');
            }
        });
    }
});

function mostrarCambiosPendientes(expId){
    $(".div-loader").css({ display: "block" });
    $.ajax({
        type: "GET",
        url: "/expediente/cambios-pendientes/"+ expId
    }).done(function (data) {
        $(".div-loader").css({ display: "none" });
        var arrayLength = data.length;
        console.log($("#enviarJefeModal .modal-body"));
        $("#enviarJefeModal .modal-contenido").empty();
        for (var i = 0; i < arrayLength; i++) {
            var expUsuario = data[i];
            var content = " \n\
         <div class=\"card\" style=\"margin: 10px;\">\n\
                <div class=\"card-header\">"+
                  expUsuario.permiso+
                "</div>\n\
                <div class=\"card-body\">\n\
                  <blockquote class=\"blockquote mb-0\">\n\
                    <h5 class=\"card-title\">"+expUsuario.nombre+"</h5>\n\
                    <p style=\"font-size:12px;\">"+expUsuario.cargo+"</p>\n\
                    <footer class=\"blockquote-footer\" style=\"font-size:12px;\">Agregado <cite title=\"Source Title\">"+expUsuario.fecCreacion+"</cite></footer>";
                    if(expUsuario.fecModificacion !== null){
                        content = content + "<footer class=\"blockquote-footer\" style=\"font-size:12px;\">Modificado <cite title=\"Source Title\">"+expUsuario.fecModificacion+"</cite></footer>";
                    }
                  content = content +"</blockquote>\n\
                </div>\n\
                <span style=\"\n\
                      position: absolute;\n\
                      right: 5px;\n\
                      bottom: 3px;\n\
                      font-size: 18px;\n\
                      color: #d8d8d8;\">"+
                expUsuario.clasificacion+"</span>\n\
            </div>\n\
             ";   
           $("#enviarJefeModal .modal-contenido").append(content);     
        };
    }).fail(function () {
        $(".div-loader").css({ display: "none" });
        console.log();
    });
};

function aprobarOrechazarCambios(tipo){
    var expId = $("#expId").val();
    console.log("expId? "+expId);
    var expObs = $.trim( $("#observacion2").val() );
    if( expObs.length <= 0 ){
        alert("Debe ingresar una observación");
    }else{
        $.ajax({
            type: "POST",
            url: "/expediente/aprobar-rechazar/"+expId+"/"+tipo+"/"+expObs,
            success: function() {
                $("#enviarJefeModal").modal("hide");
                location.reload();
            }
        });
    }
}

function modificarTipo(expId){
    console.log("expId? "+expId);
    $(".div-loader").css({ display: "block" });
    $.ajax({
        type: "POST",
        url: "/expediente/modifica-tipo-expediente?expId="+expId,
        success: function() {
            $(".div-loader").css({ display: "none" });
            location.reload();
        },
        error: function (data) {
            $(".div-loader").css({ display: "none" });
            var dataJSON = jQuery.parseJSON(data.responseText);
            var arrayLength = dataJSON.length;
            if(arrayLength > 0){
                $('#title-modal').html("Advertencia");
                var content = "<h5> Para realizar esta acción el expediente solo debe tener documentos con una sola TRD. </br></br>Las siguientes TRDS se encuentran asociadas a documentos: </h5></br>";
                content = content +" <ul>";
                for (var i = 0; i < arrayLength; i++) {
                    var trd = dataJSON[i];
                    content = content + "<li> "+trd.codigo+" "+ trd.nombre+"</li>"
                }
                content = content + "</ul>";
                $('#modal-body-info').html(content);
                $('#info-modal').modal('show');
            }

        }
    });
}


function limpiarModalDocumento(){
    $("#destinoDocumento").val("");
    $("#destinoDocumento_visible").val("");
}

function agregarDocumentoExpediente(expId){
    var docId = $("#destinoDocumento").val();
    $(".div-loader").css({ display: "block" });
    $.ajax({
        method: "POST",
        url: "/expediente/agregar-documento-expediente/" + expId+"/" + docId
    }).then(function() {
        $(".div-loader").css({ display: "none" });
        location.reload();
      }, function(message) {
        $(".div-loader").css({ display: "none" });
        $('#agregarDocumento').modal('hide');
        $('#info-modal').modal('show');
        $('#title-modal').html("Advertencia");
        $('#modal-body-info').html("<h5>"+message.responseText+"</h5>");
        $('#info-modal').modal('show');
    });
}
