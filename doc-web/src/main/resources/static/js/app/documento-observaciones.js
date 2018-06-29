/**
 * Establece la observación por defecto seleccionada en el área de texto de las
 * observaciones.
 * @param {type} select HTML Select de las observaciones por defecto.
 * @param {type} observacionesTextAreaID ID del área de texto de las observaciones.
 * @returns {undefined}
 */
function setObservacionDefecto(select, observacionesTextAreaID) {
    var texto = $.trim($(select).children("option").filter(":selected").text());
    if (texto !== '') {
        $("#" + observacionesTextAreaID).val(texto);
    }
    
    $(select).find('option:eq(0)').prop('selected', true);
}

