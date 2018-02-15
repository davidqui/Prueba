var getUrlParameter = function getUrlParameter(sParam) {
    var sPageURL = decodeURIComponent(window.location.search.substring(1)),
        sURLVariables = sPageURL.split('&'),
        sParameterName,
        i;

    for (i = 0; i < sURLVariables.length; i++) {
        sParameterName = sURLVariables[i].split('=');

        if (sParameterName[0] === sParam) {
            return sParameterName[1] === undefined ? true : sParameterName[1];
        }
    }
};

$("#arbol_list_dependenciasj").jstree().bind('ready.jstree', function(event, data) {
    var idS = getUrlParameter('idseleccionado');
    if (idS !== undefined && idS !== null) {
        data.instance._open_to(idS);
    } else {
        abrirArbolXdependencia();
    }

});

$('#arbol_list_dependenciasj')
    .on("select_node.jstree", function(e, data) {
        var newLoc = data.instance.get_node(data.node, true).children('a').attr('href');
        var id = data.instance.get_node(data.node, true).attr('id');
        if (window.location.href !== newLoc) {
            document.location = newLoc + ("&idseleccionado=" + id);
        }
    })
    .jstree();

function abrirArbolXdependencia() {
    $.ajax({
        url: "/documento/seleccionarDependencia",
        success: function(data) {
            var ret;
            $('#arbol_list_dependenciasj').jstree().open_all();
            $('#arbol_list_dependenciasj li').each(function(index, value) {
                var node = $("#arbol_list_dependenciasj").jstree().get_node(this.id);
                if (data === node.data.jstree.id) {
                    ret = node.id;
                }
            });
            $('#arbol_list_dependenciasj').jstree().close_all();
            expandNode(ret);
        }
    });
}

function expandNode(nodeID) {
    while (nodeID !== '#') {
        $("#arbol_list_dependenciasj").jstree("open_node", nodeID);
        var thisNode = $("#arbol_list_dependenciasj").jstree("get_node", nodeID);
        nodeID = $("#arbol_list_dependenciasj").jstree("get_parent", thisNode);
    }
}