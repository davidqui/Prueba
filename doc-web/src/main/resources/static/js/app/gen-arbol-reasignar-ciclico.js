$('#arbol_list_dependenciasj').on("select_node.jstree", function(e, data) {
    $("#depId").val(data.node.data.jstree.id);
    var newLoc = data.instance.get_node(data.node, true).children('a').attr('href');
    var id = data.instance.get_node(data.node, true).attr('id');
    if (window.location.href !== newLoc) {
        document.location = newLoc + ("&idseleccionado=" + id);
    }
}).jstree();