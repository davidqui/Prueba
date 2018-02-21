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

function validarArbol(myVar, selectVar = true, dependenciaPropia = false){
    
    $(myVar).jstree().bind('ready.jstree', function(event, data) {
        var idS = getUrlParameter('idseleccionado');
        if (idS !== undefined && idS !== null && !dependenciaPropia) {
            data.instance._open_to(idS);
        } 
        
        if(dependenciaPropia){
            abrirArbolXdependencia(idS);
        }
    });
    
    if(selectVar){
        $(myVar)
            .on("select_node.jstree", function(e, data) {
                var newLoc = data.instance.get_node(data.node, true).children('a').attr('href');
                var id = data.instance.get_node(data.node, true).attr('id');
                if (window.location.href !== newLoc) {
                    document.location = newLoc + ("&idseleccionado=" + id);
                }
            })
            .jstree();
    }
    
    function abrirArbolXdependencia(selId) {
        $.ajax({
            url: "/documento/seleccionarDependencia",
            success: function(data) {
                var ret;
                $(myVar).jstree().open_all();
                $(myVar+' li').each(function(index, value) {
                    var node = $(myVar).jstree().get_node(this.id);
                    if (data === node.data.jstree.id) {
                        var $style = "color: blue; font-weight:bold";
                        node.a_attr['style'] = $style;
                        if(selId === undefined){
                            ret = node.id;
                        }
                    }
                    if(selId !== undefined && selId === node.id){
                        ret = node.id;
                    }
                });
                $(myVar).jstree().close_all();
                if(ret !== undefined){
                    expandNode(ret);
                }
            }
        });
    };
    
    function expandNode(nodeID) {
        while (nodeID !== '#') {
            $(myVar).jstree("open_node", nodeID);
            var thisNode = $(myVar).jstree("get_node", nodeID);
            nodeID = $(myVar).jstree("get_parent", thisNode);
        }
    };
};