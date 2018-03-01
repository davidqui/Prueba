/**
 * Cambia el último parámetro de la URL indicada en el enlace del botón de la
 * transición "Firmar y Enviar", el cual se espera sea "cargoIdFirma".
 * @param {type} select Object HTML SELECT de selección de cargo.
 * @param {type} trxId ID de la transacción para poder ubicar el botón.
 * @returns {undefined}
 */
function onChangeDocumentoCargoFirma(select, trxId) {
    var selectedValue = select.options[select.selectedIndex].value;
    var trxAnchor = document.getElementById("trx_" + trxId);
    var lastIndex = trxAnchor.href.lastIndexOf("=");
    var href = trxAnchor.href.substr(0, lastIndex + 1) + selectedValue;
    trxAnchor.href = href;
}

