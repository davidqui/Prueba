/**
 * Metodo que se llama cada vez que se selecciona un nodo del arbol
 * de la dependencia destino.
 * @param e  Variable que corresponde al evento
 * @param data Variable que contiene la información del arbol
 */
$('#arbol_list_dependenciasj').on("select_node.jstree", function (e, data) {
    $("#dependenciaDestino").val(data.node.data.jstree.id);
    $("#dependenciaDestinoNombre").text(data.node.text);
    $("#dependenciaDestinoModalArbol").modal('hide');
}).jstree();

/**
 * Metodo que se llama cada vez que se selecciona un nodo del arbol
 * de la dependencia adicionales.
 * @param e  Variable que corresponde al evento
 * @param data Variable que contiene la información del arbol
 */
$('#arbol_list_dependenciasadi').on("select_node.jstree", function (e, data) {
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

/**
 * Metodo que se llama cada vez que se selecciona un nodo del arbol
 * de TRDS.
 * @param e  Variable que corresponde al evento
 * @param data Variable que contiene la información del arbol
 */
$('#arbol_list_trd').on("select_node.jstree", function (e, data) {
    if (data.node.parents.length === 1) {
        $(this).jstree("open_node", data.node.id);
    } else {
        $("#trd").val(data.node.data.jstree.id);
        $("#trdNombre").text(data.node.text);
        $("#trdModalArbol").modal('hide');
    }
}).jstree();
