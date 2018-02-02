// 2018-01-29 edison.gonzalez@controltechcg.com Issue #147 (SICDI-Controltech)
// feature-147
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
    console.log("idseleccionado = " + getUrlParameter('idseleccionado'));
    var idS = getUrlParameter('idseleccionado');
    if (idS !== undefined && idS !== null) {
        data.instance._open_to(idS);
    }
});

$('#arbol_list_dependenciasj')
    .on("select_node.jstree", function(e, data) {
        var newLoc = data.instance.get_node(data.node, true).children('a').attr('href');
        var id = data.instance.get_node(data.node, true).attr('id');
        if (window.location.href !== newLoc) {
            document.location = newLoc + ("&idseleccionado=" + id);
        }
        
        $("#dependenciaDestino").val(data.node.data.jstree.id);
        $("#dependenciaDestinoDescripcion").val(data.node.text);
        $("#dependenciaDestinoNombre").text(data.node.text);
        $("#dependenciaDestinoModal").modal('hide');
    })
    .jstree();