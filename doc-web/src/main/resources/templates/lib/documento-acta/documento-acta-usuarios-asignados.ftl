<#--
    2018-05-28 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
    feature-162: Presenta las opciones para asociar usuarios al documento acta
    y/o presentar la información de los usuarios asociados.

    usuariosAsignados Lista de usuarios previamente asignados al documento.
-->
<#macro presentarUsuariosAsignados usuariosAsignados >
        <div class="form-group">
            <#assign campo = "usuariosAsignados" />
            <label for="${campo}">Usuarios asignados (*)</label>
            <div id="usuarios-a-asignar-0" style="display: none;">
                Hola Mundo 0!
            </div>
            <div id="usuarios-a-asignar-1" style="display: none;">
                Hola Mundo 1!
            </div>
            <div id="usuarios-a-asignar-n" style="display: none;">
                Hola Mundo n!
            </div>
        </div>   
</#macro>