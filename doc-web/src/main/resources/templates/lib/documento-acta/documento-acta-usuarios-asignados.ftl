<#--
    2018-05-28 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
    feature-162: Presenta las opciones para asociar usuarios al documento acta
    y/o presentar la información de los usuarios asociados.

    usuariosAsignados Lista de usuarios previamente asignados al documento.
-->
<#macro agregarUsuariosAsignados usuariosAsignados >
        <div class="form-group">
            <#assign campo = "usuariosAsignados" />
            <label for="${campo}">Usuarios asignados (*)</label>            
            <div id="usuarios-a-asignar-0" style="display: none;">
                <div class="alert alert-info" role="alert">La subserie TRD no necesita selección de usuarios para asociar al acta.</div>
            </div>
            
            <div id="usuarios-a-asignar-1" style="display: none;">
                <div class="input-group">
                    <input type="text" class="form-control" disabled />
                    <span class="input-group-btn">
                        <button class="btn btn-primary" type="button">Buscar</button>
                    </span>
                 </div>
                <select class="form-control">
                </select>
            </div>
            
            <div id="usuarios-a-asignar-n" style="display: none;">
                <button type="button" class="btn btn-primary" onclick="agregarUsuarioActa()">Agregar usuario</button>
                <div id="usuarios-a-asignar-n-content">
                </div>
            </div>
        </div>   
</#macro>