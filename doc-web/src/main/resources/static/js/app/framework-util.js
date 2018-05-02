/**
 * Aplica el submit sobre el formulario del select.
 * @param {type} select HTML Select. Obligatorio.
 * @returns {undefined}
 */
/*
 * 2018-04-26 jgarcia@controltechcg.com Issue #151 (SICDI-Controltech) 
 * feature-151.
 */
function submitSelect(select) {
    $(select).closest("form").submit();
}

/**
 * Realiza el registro de la aceptación del usuario en sesión de la redirección 
 * a OWA y aplica dicha redirección a la URL indicada.
 * @param {type} url URL a navegar. Se espera que sea la URL del OWA.
 * @returns {undefined}
 */
/*
 * 2018-05-02 jgarcia@controltechcg.com Issue #159 (SICDI-Controltech) 
 * feature-159.
 */
function goToOWA(url) {
    $.ajax({
        type: "POST",
        url: "/link-owa-registro-uso/registrar",
        data: JSON.stringify({"url": url}),
        contentType: "application/json",
        success: function (data) {
            window.open(url, '_blank');
        }
        ,
        failure: function (errMsg) {
            alert(errMsg);
        }
    }
    );
}