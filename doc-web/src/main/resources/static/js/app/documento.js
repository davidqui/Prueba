$('#arbol_list_dependenciasj').on("select_node.jstree", function (e, data) {
    var newLoc = data.instance.get_node(data.node, true).children('a').attr('href');
    var id = data.instance.get_node(data.node, true).attr('id');
    if (window.location.href !== newLoc) {
        document.location = newLoc + ("&idseleccionado=" + id);
    }
    $("#dependenciaDestino").val(data.node.data.jstree.id);
    $("#dependenciaDestinoNombre").text(data.node.text);
    $("#dependenciaDestinoModalArbol").modal('hide');
}).jstree();

$('#arbol_list_dependenciasadi').on("select_node.jstree", function (e, data) {
    var newLoc = data.instance.get_node(data.node, true).children('a').attr('href');
    var id = data.instance.get_node(data.node, true).attr('id');
    if (window.location.href !== newLoc) {
        document.location = newLoc + ("&idseleccionado=" + id);
    }
    var idDocumento = $("#idDocumentoDependenciaDestinoAdicionalModal").val();
    var idDependencia = data.node.data.jstree.id;

    adicionarDepenendeciaMultidestino(idDocumento, idDependencia);

    $("#dependenciaDestinoAdicionalModal").modal('hide');
}).jstree();

/**
 * Adiciona una dependencia multidestino a un documento original.
 * @param {type} idDocumento ID del documento original.
 * @param {type} idDependencia ID de la dependencia copia destino.
 */
function adicionarDepenendeciaMultidestino(idDocumento, idDependencia) {
    $.ajax({
        type: "POST",
        url: "/dependencia-copia-multidestino/" + idDocumento + "/" + idDependencia,
        success: function (data) {
            console.log(data.message);
            window.location.reload(true);
        },
        error: function (data) {
            alert("ERROR: " + data.responseJSON.message);
        }
    });
}


/**
 * Elimina en un registro de dependencia copia multidestino.
 * @param {type} idCopiaMultidestino ID dle registro de dependencia copia multidestino.
 * @returns {Boolean} Siempre retorna false.
 */
function eliminarDocumentoDependenciaAdicional(idCopiaMultidestino) {
    $.ajax({
        type: "DELETE",
        url: "/dependencia-copia-multidestino/" + idCopiaMultidestino,
        success: function (data) {
            console.log(data.message);
            window.location.reload(true);
        },
        error: function (data) {
            alert("ERROR: " + data.responseJSON.message);
        }
    });
    return false;
}
