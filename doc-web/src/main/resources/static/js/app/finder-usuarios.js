/*
 * 2018-05-30 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech) 
 * feature-162.
 */

/**
 * Aplica la selección del usuario para el formulario de asignación para el 
 * acta.
 * @param {type} usuarioID ID del usuario seleccionado.
 * @returns {undefined}
 */
function selectUsuarioActa(usuarioID) {
    opener.setUsuarioActa(usuarioID);
    window.close();
}

