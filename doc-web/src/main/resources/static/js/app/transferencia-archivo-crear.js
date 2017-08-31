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
        url: "/rest/usuario/buscar/activo/" + documento
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

