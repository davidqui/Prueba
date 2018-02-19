$('#arbol_list_dependenciasj').on("select_node.jstree", function(e, data) {
    var newLoc = data.instance.get_node(data.node, true).children('a').attr('href');
    var id = data.instance.get_node(data.node, true).attr('id');
    if (window.location.href !== newLoc) {
        document.location = newLoc + ("&idseleccionado=" + id);
    }
    $("#dependenciaDestino").val(data.node.data.jstree.id);
    $("#dependenciaDestinoNombre").text(data.node.text);
    $("#dependenciaDestinoModalArbol").modal('hide');
}).jstree();