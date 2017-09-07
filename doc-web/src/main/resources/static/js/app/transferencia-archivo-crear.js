// 2017-08-30 jgarcia@controltechcg.com Issue #120 (SICDI-Controltech)
// feature-120

/**
 * Procesa la búsqueda de usuario activo.
 * @returns {undefined}
 */
function buscarUsuarioActivo() {
    documento = $("#destinoUsuario_buscar").val();

    if ($.trim(documento) === "") {
        alert("Debe ingresar el número de documento del usuario.");
        return;
    }

    $.ajax({
        method: "POST",
        url: "/rest/usuario/buscar/activo/documento/" + documento
    }).done(function (busquedaDTO) {
        if (!busquedaDTO.ok) {
            $("#destinoUsuario_visible").val(busquedaDTO.mensajeBusqueda);
            return;
        }

        descripcion = busquedaDTO.grado + " " + busquedaDTO.nombre + " ["
                + busquedaDTO.clasificacionNombre + "]";
        $("#destinoUsuario_visible").val(descripcion);
        $("#destinoUsuario").val(busquedaDTO.id);
    });
}

/**
 * Abre la ventana del finder de usuarios.
 * @returns {undefined}
 */
function openFinderWindow() {
    var w = 1200;
    var h = 600;

    var dualScreenLeft = window.screenLeft !== undefined ?
            window.screenLeft : screen.left;
    var dualScreenTop = window.screenTop !== undefined ?
            window.screenTop : screen.top;

    var width = window.innerWidth ? window.innerWidth :
            document.documentElement.clientWidth ?
            document.documentElement.clientWidth : screen.width;
    var height = window.innerHeight ? window.innerHeight :
            document.documentElement.clientHeight ?
            document.documentElement.clientHeight : screen.height;

    var left = ((width / 2) - (w / 2)) + dualScreenLeft;
    var top = ((height / 2) - (h / 2)) + dualScreenTop;

    window.open("/transferencia-archivo/formulario-buscar-usuario", "_blank",
            "width=" + w + ", height=" + h + ", top=" + top + ", left=" + left
            + ",location=no,menubar=no,resizable=no,status=no,titlebar=no,toolbar=no");
}

/**
 * Ejecuta la acción sobre el usuario seleccionado y cierra la ventana del finder.
 * @param {type} id Id del usuario seleccionado.
 * @returns {undefined}
 */
function selectFindResult(id) {
    opener.setFindResult(id);
    window.close();
}

/**
 * Establece los valores del usuario indicado. 
 * @param {type} id Id del usuario.
 * @returns {undefined}
 */
function setFindResult(id) {
    $.ajax({
        method: "POST",
        url: "/rest/usuario/buscar/activo/id/" + id
    }).done(function (busquedaDTO) {
        if (!busquedaDTO.ok) {
            $("#destinoUsuario_visible").val(busquedaDTO.mensajeBusqueda);
            return;
        }

        descripcion = busquedaDTO.grado + " " + busquedaDTO.nombre + " ["
                + busquedaDTO.clasificacionNombre + "]";
        $("#destinoUsuario_visible").val(descripcion);
        $("#destinoUsuario").val(busquedaDTO.id);
    });
}
