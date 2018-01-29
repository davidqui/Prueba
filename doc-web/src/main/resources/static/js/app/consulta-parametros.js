function initParameters(dependenciaOrigenDescripcion, dependenciaDestinoDescripcion){
    var depOri = dependenciaOrigenDescripcion;
    
    if(depOri === null || depOri.length === 0){
        $("#dependenciaOrigenNombre").text("Por favor seleccione una dependencia origen...");
    }else{
        $("#dependenciaOrigenNombre").text(depOri);
    }

    var depDes = dependenciaDestinoDescripcion;
    if(depDes === null || depDes.length === 0){
        $("#dependenciaDestinoNombre").text("Por favor seleccione una dependencia destino...");
    }else{
        $("#dependenciaDestinoNombre").text(depDes);
    }
};

$('#fechaInicio').prop('readonly', true);
$('#fechaFin').prop('readonly', true);

$('#fechaInicio').each(function() {
    var buton = "<div onclick='document.getElementById(\""+ $(this).attr("id")+ "\").value = \"\"' class=\"input-group-addon btn\" >Limpiar</div>";
    $(this).parent().append(buton);
});
$('#fechaFin').each(function() {
    var buton = "<div onclick='document.getElementById(\""+ $(this).attr("id")+ "\").value = \"\"' class=\"input-group-addon btn\" >Limpiar</div>";
    $(this).parent().append(buton);
}); 

$('#clasificacionSelect').change(function () {
    $("#clasificacion").val($('#clasificacionSelect').val());
    $("#clasificacionNombre").val($("#clasificacionSelect option:selected").text());
});

$('#dependenciaOrigenLimpiar').on('click', function() {
    $("#dependenciaOrigen").val("");
    $("#dependenciaOrigenNombre").text("Por favor seleccione una dependencia origen...");
    $("#dependenciaOrigenDescripcion").val("");
});

$('#dependenciaDestinoLimpiar').on('click', function() {
  $("#dependenciaDestino").val("");
  $("#dependenciaDestinoNombre").text("Por favor seleccione una dependencia destino...");
  $("#dependenciaDestinoDescripcion").val("");   
});

