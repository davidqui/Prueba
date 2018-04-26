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

