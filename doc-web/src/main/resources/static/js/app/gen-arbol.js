/**
 * Dunción que se encarga de retornar el identificador del nodo seleccionado.
 * @param {type} sParam
 * @returns {getUrlParameter.sParameterName|Boolean}
 */
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

/**
 * Metodo que permite asociar los eventos al arbol
 * @param {type} myVar Identificador del conponente del arbol
 * @param {type} selectVar Variable que identifica si se debe seleccionar un nodo
 * @param {type} abrirNodoDependencia Variable que identifica si debe seleccionar
 * la dependencia del usuario en sesion.
 * @returns {undefined}
 */
function validarArbol(myVar, selectVar = true, abrirNodoDependencia = false){
    
    /**
     * Evento que se dispara en el momento crear el componente del arboñ
     */
    $(myVar).jstree().bind('ready.jstree', function(event, data) {
        var idS = getUrlParameter('idseleccionado');
        if (idS !== undefined && idS !== null && !abrirNodoDependencia) {
            data.instance._open_to(idS);
        } 
        
        if(abrirNodoDependencia){
            abrirArbolXdependencia(idS);
        }
    });
    
    if(selectVar){
        /**
         * Evento que se dispara en el momento que se selecciona un nodo del arbol
         */
        $(myVar).on("select_node.jstree", function(e, data) {
            var newLoc = data.instance.get_node(data.node, true).children('a').attr('href');
            var id = data.instance.get_node(data.node, true).attr('id');
            if (window.location.href !== newLoc) {
                document.location = newLoc + ("&idseleccionado=" + id);
            }
        }).jstree();
    }
    
    /**
     * Función que se encarga de abrir y seleccionar la dependencia del usuario
     * en sesión
     * @param {type} selId
     * @returns {undefined}
     */
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
    
    /**
     * Función que se encarga de expandir un nodo.
     * @param {type} nodeID Identificador del nodo
     * @returns {undefined}
     */
    function expandNode(nodeID) {
        while (nodeID !== '#') {
            $(myVar).jstree("open_node", nodeID);
            var thisNode = $(myVar).jstree("get_node", nodeID);
            nodeID = $(myVar).jstree("get_parent", thisNode);
        }
    };
};