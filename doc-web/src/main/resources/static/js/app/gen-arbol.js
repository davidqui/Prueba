function getUrlParameter(sParam) {
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

function validarArbol(myVar){
    $(myVar).jstree().bind('ready.jstree', function(event, data) {
        alert('ready generico');
        var idS = getUrlParameter('idseleccionado');
        if (idS !== undefined && idS !== null) {
            data.instance._open_to(idS);
        } 
    });

    $(myVar)
        .on("select_node.jstree", function(e, data) {
            alert('select_node generico');
            var newLoc = data.instance.get_node(data.node, true).children('a').attr('href');
            var id = data.instance.get_node(data.node, true).attr('id');
            if (window.location.href !== newLoc) {
                document.location = newLoc + ("&idseleccionado=" + id);
            }
        })
        .jstree();
};