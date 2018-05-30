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

function setUsuarioActa(usuarioID) {
    $.ajax({
        method: "POST",
        url: "/rest/usuario/buscar/activo/id/" + usuarioID
    }).done(function (busquedaDTO) {
        if (!busquedaDTO.ok) {
            $("#destinoUsuario_visible").val(busquedaDTO.mensajeBusqueda);
            return;
        }

        descripcion = busquedaDTO.grado + " " + busquedaDTO.nombre + " ["
                + busquedaDTO.clasificacionNombre + "]";
        $("#destinoUsuario_visible").val(descripcion);
        $("#destinoUsuario").val(busquedaDTO.id);

        $("#cargoAsignado option").remove();
        $.each(busquedaDTO.cargosDestino, function (index, item) {
            $("#cargoAsignado").append("<option value=" + item.id + ">" + item.nombre + "</option>");
        });
    });
}

function agregarUsuarioActa() {
    var usuarioID = $("#destinoUsuario").val();
    if (usuarioID === undefined || $.trim(usuarioID) === "") {
        alert("Debe seleccionar un usuario.");
        return;
    }

    var cargoID = $("#cargoAsignado").val();
    if (cargoID === undefined || $.trim(cargoID) === "") {
        alert("Debe seleccionar un cargo del usuario.");
        return;
    }

    var pin = $("#pin").val();

    $.ajax({
        method: "POST",
        url: "/documento-acta/asignar-usuario-acta/" + pin + "/" + usuarioID + "/" + cargoID
    }).always(function (id) {
        console.log("usuario-documento-acta: " + id);
        location.reload();
    });
}