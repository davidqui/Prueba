<#--
    2018-05-28 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
    feature-162: Presenta las opciones para asociar usuarios al documento acta
    y/o presentar la información de los usuarios asociados.

    usuariosAsignados Lista de usuarios previamente asignados al documento.
-->
<#macro agregarUsuariosAsignados usuariosAsignados seleccionUsuarioSubserieActa >
<input type="hidden" id="usuarios-asignados-count" name="usuarios-asignados-count" value="${usuariosAsignados?size}"/>
    <#if seleccionUsuarioSubserieActa == "SELECCION_1_N" >
    <button type="button" class="btn btn-primary" onclick="agregarUsuarioActa()">Agregar usuario</button>
    </#if>
<table id="table-usuarios-asignados" class="table table-bordered">
    <thead>
        <tr>
            <th>Usuario</th>
            <th>Cargo</th>
            <th>Acciones</th>
        </tr>
    </thead>
    <tbody>
        <#if seleccionUsuarioSubserieActa == "SELECCION_1_1" >
        <tr>
            <td>
                <div class="input-group">
                    <input type="hidden" id="usuario-asignado-1" name="usuario-asignado-1" value="">
                    <input type="text" class="form-control" disabled />
                    <span class="input-group-btn">
                        <button class="btn btn-primary" type="button" onclick="openUsuariosFinderWindow()">Buscar</button>
                    </span>
                 </div>                
            </td>
            <td>
                <select class="form-control">
                </select>                
            </td>
            <td>&nbsp;</td>
        </tr>
        </#if>
    </tbody>
</table>
</#macro>