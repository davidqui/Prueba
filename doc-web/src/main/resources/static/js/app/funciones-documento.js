/**
 * Deshabilita el boton cuando llama al evento onclick
 * @param {button} Boton sobre el cual se realiza la transición del documento.
 * @param {url} url de la transición.
 * @returns {undefined}
 */
function processTransition(button, url) {
    $(button).attr("disabled", "disabled");
    $(button).text("Procesando...");
    // 2017-02-17 jgarcia@controltechcg.com Issue #143: Corrección para funcionalidad en Google Chrome.
    $(button).closest("form").submit();
    window.location.href = url;
}


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
    var lastIndex = trxAnchor.getAttribute('onclick').lastIndexOf("=");
    /*
     * 2018-05-18 jgarcia@controltechcg.com Issue #167 (SICDI-Controltech)
     * hotfix-167: Corrección en construcción del valor onclick, encerrando
     * correctamente la URL a invocar durante la acción de firma y envío.
     */
    var href = trxAnchor.getAttribute('onclick').substr(0, lastIndex + 1) + selectedValue + "')";
    trxAnchor.setAttribute('onclick', href);
}