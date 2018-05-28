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
        alert("Error en la selección de usuarios para la TRD.");
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
            .append(
                    $("<div>")
                    .addClass("input-group")
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
                                    .text("Eliminar")
                                    )
                            )
                    )
            );
}