/**
 * Función a ejecutar cuando el selector del TRD del acta ha sido cambiado con 
 * respecto a su valor seleccionado.
 * 
 * @param {type} select Selector HTML.
 * @returns {undefined}
 */
/*
 * 2018-05-28 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech) 
 * feature-162.
 */
function trdActaSelectOnChange(select) {
    var nivelClasificacion = $("#clasificacion").find(":selected").val();
    if (nivelClasificacion === undefined || $.trim(nivelClasificacion) === "") {
        alert("Debe seleccionar primero un nivel de clasificación.");
        $(select).find('option:first-child').attr("selected", "selected");
        return;
    }

    var subserie = $(select).find(":selected").val();
    console.log(subserie);

    var seleccionUsuarioDivIDs = [
        "usuarios-a-asignar-0",
        "usuarios-a-asignar-1",
        "usuarios-a-asignar-n"
    ];

    var seleccionUsuario = getSeleccionUsuario(subserie);
    console.log("seleccionUsuario=" + seleccionUsuario);
    if (seleccionUsuario === "SELECCION_0_0") {
        showAndHideSeleccionUsuarioDivs(seleccionUsuarioDivIDs, "usuarios-a-asignar-0");
    } else if (seleccionUsuario === "SELECCION_1_1") {
        showAndHideSeleccionUsuarioDivs(seleccionUsuarioDivIDs, "usuarios-a-asignar-1");
    } else if (seleccionUsuario === "SELECCION_1_N") {
        showAndHideSeleccionUsuarioDivs(seleccionUsuarioDivIDs, "usuarios-a-asignar-n");
    } else {
        showAndHideSeleccionUsuarioDivs(seleccionUsuarioDivIDs, "");
    }
}

/**
 * Indica si la subserie corresponde al tipo de selección de usuario con máx y 
 * mín 1 usuario.
 * @param {type} subserie ID de la subserie.
 * @returns {Boolean|response} true si hay correspondencia entre la subserie y 
 * la selección de usuario ; de lo contrario, false.
 */
function getSeleccionUsuario(subserie) {
    var seleccion = undefined;

    $.ajax({
        async: false,
        method: "GET",
        url: "/documento-acta/seleccion-subserie/" + subserie
    }).done(function (response) {
        seleccion = response;
    }).fail(function (err) {
        console.log("Error: " + subserie + "\t" + err);
        seleccion = undefined;
    });

    return seleccion;
}

/**
 * Muestra el DIV de selección de usuario indicado, y esconde los demás DIVs de 
 * selección.
 * @param {type} seleccionUsuarioDivIDs Arreglo de ID de todos los DIVs de 
 * selección de usuario.
 * @param {type} divShowID ID del DIV de selección de usuario a presentar.
 * @returns {undefined}
 */
function showAndHideSeleccionUsuarioDivs(seleccionUsuarioDivIDs, divShowID) {
    for (var i = 0; i < seleccionUsuarioDivIDs.length; i++) {
        var seleccionUsuarioDivID = seleccionUsuarioDivIDs[i];
        if (seleccionUsuarioDivID === divShowID) {
            $("#" + seleccionUsuarioDivID).show();
        } else {
            $("#" + seleccionUsuarioDivID).hide();
        }
    }
}

function agregarUsuarioActa() {
    $("#usuarios-a-asignar-n-content").append(
            $("<div>")
            .attr("id", "usuario_x")
            .css({"margin-top": "10px"})
            .append(
                    $("<div>")
                    .addClass("input-group")
                    .css({"margin-bottom": "5px"})
                    .append(
                            $("<input/>")
                            .attr({type: 'text', id: 'test', name: 'test', disabled: 'disabled'})
                            .addClass("form-control")
                            )
                    .append(
                            $("<span>")
                            .addClass("input-group-btn")
                            .append(
                                    $("<button>")
                                    .attr({type: 'button'})
                                    .addClass("btn")
                                    .addClass("btn-primary")
                                    .addClass("btn-sm")
                                    .css({"height": "38px"})
                                    .text("Buscar")
                                    )
                            )
                    )
            .append(
                    $("<div>")
                    .addClass("input-group")
                    .append(
                            $("<select>")
                            .addClass("form-control")
                            )
                    .append(
                            $("<span>")
                            .addClass("input-group-btn")
                            .append(
                                    $("<button>")
                                    .attr({type: 'button'})
                                    .addClass("btn")
                                    .addClass("btn-danger")
                                    .addClass("btn-sm")
                                    .css({"height": "33px", "width": "71px", "padding": "2px"})
                                    .text("Eliminar")
                                    )
                            )
                    )
            );
}

/**
 * Abre la ventana del finder de usuarios.
 * @returns {undefined}
 */
function openUsuariosFinderWindow() {
    var minWidth = 1200;
    var minHeight = 600;

    var dualScreenLeft = window.screenLeft !== undefined ? window.screenLeft : screen.left;
    var dualScreenTop = window.screenTop !== undefined ? window.screenTop : screen.top;

    var width = window.innerWidth ? window.innerWidth : document.documentElement.clientWidth ? document.documentElement.clientWidth : screen.width;
    var height = window.innerHeight ? window.innerHeight : document.documentElement.clientHeight ? document.documentElement.clientHeight : screen.height;

    var left = ((width / 2) - (minWidth / 2)) + dualScreenLeft;
    var top = ((height / 2) - (minHeight / 2)) + dualScreenTop;

    window.open("/finder/usuario/finder-buscar-usuario?type=ACTA", "_blank", "width=" + minWidth + ", height=" + minHeight + ", top=" + top + ", left=" + left
            + ",location=no,menubar=no,resizable=no,status=no,titlebar=no,toolbar=no");
}