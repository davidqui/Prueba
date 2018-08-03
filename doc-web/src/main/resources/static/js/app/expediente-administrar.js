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
    $.ajax({
        type: "GET",
        url: "/expediente/cambios-pendientes/"+ expId
    }).done(function (data) {
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