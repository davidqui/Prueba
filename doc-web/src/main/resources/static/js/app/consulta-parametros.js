// 2018-01-29 edison.gonzalez@controltechcg.com Issue #147 (SICDI-Controltech)
// feature-147
function initParameters(dependenciaOrigenDescripcion, dependenciaDestinoDescripcion) {
    var depOri = dependenciaOrigenDescripcion;

    if (depOri === null || depOri.length === 0) {
        $("#dependenciaOrigenNombre").text("Por favor seleccione una dependencia origen...");
    } else {
        $("#dependenciaOrigenNombre").text(depOri);
    }

    var depDes = dependenciaDestinoDescripcion;

    if (depDes === null || depDes.length === 0) {
        $("#dependenciaDestinoNombre").text("Por favor seleccione una dependencia destino...");
    } else {
        $("#dependenciaDestinoNombre").text(depDes);
    }
}
;

$('#fechaInicio').prop('readonly', true);
$('#fechaFin').prop('readonly', true);

$('#fechaInicio').each(function () {
    var buton = "<div onclick='document.getElementById(\"" + $(this).attr("id") + "\").value = \"\"' class=\"input-group-addon btn\" >Limpiar</div>";
    $(this).parent().append(buton);
});
$('#fechaFin').each(function () {
    var buton = "<div onclick='document.getElementById(\"" + $(this).attr("id") + "\").value = \"\"' class=\"input-group-addon btn\" >Limpiar</div>";
    $(this).parent().append(buton);
});

$('#clasificacionSelect').change(function () {
    $("#clasificacion").val($('#clasificacionSelect').val());
    $("#clasificacionNombre").val($("#clasificacionSelect option:selected").text());
});

$('#dependenciaOrigenLimpiar').on('click', function () {
    $("#dependenciaOrigen").val("");
    $("#dependenciaOrigenNombre").text("Por favor seleccione una dependencia origen...");
    $("#dependenciaOrigenDescripcion").val("");
});

$('#dependenciaDestinoLimpiar').on('click', function () {
    $("#dependenciaDestino").val("");
    $("#dependenciaDestinoNombre").text("Por favor seleccione una dependencia destino...");
    $("#dependenciaDestinoDescripcion").val("");
});

// 2018-02-19 edison.gonzalez@controltechcg.com Issue #157 (SICDI-Controltech)
// feature-150 Se centraliza en un solo archivo por template
/**
 * Evento que se dispara en el momento que se selecciona un nodo del arbol
 * @param e Variables del evento del componente
 * @param data Variable que contiene la información del arbol
 */
$('#arbol_list_dependenciasjOrigen').on("select_node.jstree", function (e, data) {
    $("#dependenciaOrigen").val(data.node.data.jstree.id);
    $("#dependenciaOrigenDescripcion").val(data.node.text);
    $("#dependenciaOrigenNombre").text(data.node.text);
    $("#dependenciaOrigenModal").modal('hide');
}).jstree();

/**
 * Evento que se dispara en el momento que se selecciona un nodo del arbol
 * @param e Variables del evento del componente
 * @param data Variable que contiene la información del arbol
 */
$('#arbol_list_dependenciasj').on("select_node.jstree", function (e, data) {
    $("#dependenciaDestino").val(data.node.data.jstree.id);
    $("#dependenciaDestinoDescripcion").val(data.node.text);
    $("#dependenciaDestinoNombre").text(data.node.text);
    $("#dependenciaDestinoModal").modal('hide');
}).jstree();