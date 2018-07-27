
<#assign pageTitle = "Crear Expediente"/>
<#setting number_format="computer">
<#include "bandeja-header.ftl">
<#include "gen-arbol-trd.ftl">
<#import "spring.ftl" as spring />

<h4>${(expediente.nombre)!""}</h4>

<button type="button" class="btn btn-primary btn-sm" data-toggle="modal" data-target="#exampleModal" style="float: right;">
    Agregar usuario
</button>

<div class="container-fluid">

    <div style="display: grid; grid-gap: 5px; grid-template-columns: repeat(auto-fit, 450px); grid-template-rows: repeat(2, 180px); width: 100%;">
        <#if usuario1??>
            <#list usuario1?keys as key>
                <div class="card" style="margin: 10px;">
                    <button type="button" 
                            class="btn btn-primary bmd-btn-fab" style="position: absolute;
                            right: 45px;height: 43px;" title="Modificar" data-toggle="modal" 
                            data-target="#usuarioEditModal" onclick="editarUsuario(${(key.id)}, '${(key.usuGrado.id)!""} ${(key.nombre)!""}')">
                        M
                    </button>
                    <button type="button" class="btn btn-danger bmd-btn-fab eliminar" 
                            style="position: absolute;right: 0px;height: 43px;" 
                            onclick="eliminarUsuario(${(key.id)!""})"
                            data-toggle="tooltip" data-placement="top" title="Eliminar">
                        X
                    </button>
                    <div class="card-header">
                      Lectura
                    </div>
                    <div class="card-body">
                      <blockquote class="blockquote mb-0">
                        <h5 class="card-title">${(key.usuGrado.id)!""} ${(key.nombre)!""}</h5>
                        <p>${(usuario1[key].carNombre)!""}</p>
                        <footer class="blockquote-footer">Agregado <cite title="Source Title">27/07/2018</cite></footer>
                      </blockquote>
                    </div>
                    <span style="
                          position: absolute;
                          right: 5px;
                          bottom: 3px;
                          font-size: 18px;
                          color: #d8d8d8;;">
                    ${(key.clasificacion.nombre)!""}</span>
                </div>
            </#list>
        </#if>
        <#if usuario2??>
            <#list usuario2?keys as key> 
                <div class="card" style="width: 450px; margin: 10px;">
                    <button type="button" class="btn btn-primary bmd-btn-fab eliminar" style="position: absolute;right: 45px;height: 43px;" onclick="eliminarUsuario(${(key.id)!""})">
                        M
                    </button>
                    <button type="button" class="btn btn-danger bmd-btn-fab eliminar" style="position: absolute;right: 0px;height: 43px;" onclick="eliminarUsuario(${(key.id)!""})">
                        X
                    </button>
                    <div class="card-header">
                      Escritura
                    </div>
                    <div class="card-body">
                      <blockquote class="blockquote mb-0">
                        <h5 class="card-title">${(key.usuGrado.nombre)!""} ${(key.nombre)!""}</h5>
                        <p>${(usuario2[key].carNombre)!""}</p>
                        <footer class="blockquote-footer">Agregado <cite title="Source Title">27/07/2018</cite></footer>
                      </blockquote>
                    </div>
                </div>
            </#list>
        </#if>
    </div>
    
    <#-- Modal de selección de usuarios. -->
    <div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">Agregar usuario</h5>
                </div>
                <div class="modal-body">
                    <div class="card">
                        <div class="card-body">                                        
                            <div class="form-group">
                                <label for="usuarioAsignado">Usuario (*)</label>
                                <div class="input-group">
                                    <input type="text" id="destinoUsuario_visible" name="destinoUsuario_visible" class="form-control" value="" disabled />
                                    <div class="input-group-btn">
                                        <button type="button" class="btn btn-primary" onclick="openFinderWindow()">Buscar</button>
                                    </div>
                                    <script src="/js/app/buscar-usuario.js"></script>
                                </div> 
                                <input type="hidden" id="destinoUsuario" name="destinoUsuario" value="" />
                            </div>
                            <div class="form-group">
                                <label for="cargoAsignado">Cargo (*)</label>
                                <select class="form-control" id="cargoAsignado" name="cargoAsignado">
                                </select>
                            </div>
                            <fieldset class="form-group">
                                <label for="trd">Permiso</label>
                                <div class="btn-group" data-toggle="buttons" id="topButtonDiv" style="width: 100%;">
                                    <button type="button" class="btn btn-primary" id="btn-lectura" style="width: 50%;" onclick="selectPermiso(1)">Lectura
                                    <input class="permsiso" type="radio" id="radio1" name="permiso" value="1" checked></button>
                                    <button type="button" class="btn btn-default" id="btn-escritura" style="width: 50%;" onclick="selectPermiso(2)">Escritura
                                    <input class="permsiso" type="radio" id="radio2" name="permiso" value="2"> </button>              
                                </div>
                            </fieldset>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary" onclick="agregarUsuario()">Agregar</button>                                
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Cerrar</button>
                </div>
            </div>
        </div>
    </div>
    
    <#-- Modal de modificación de usuarios. -->
    <div class="modal fade" id="usuarioEditModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">Modificar usuario</h5>
                </div>
                <div class="modal-body">
                    <div class="card">
                        <div class="card-body">                                        
                            <div class="form-group">
                                <label for="usuarioAsignado">Usuario (*)</label>
                                <div class="input-group" style="width: 100%;">
                                    <input type="text" id="destinoUsuario_visible2" name="destinoUsuario_visible" class="form-control" value="" disabled />
                                </div> 
                                <input type="hidden" id="destinoUsuario" name="destinoUsuario" value="" />
                            </div>
                            <div class="form-group">
                                <label for="cargoAsignado2">Cargo (*)</label>
                                <select class="form-control" id="cargoAsignado2" name="cargoAsignado">
                                </select>
                            </div>
                            <fieldset class="form-group">
                                <label for="trd">Permiso</label>
                                <div class="btn-group" data-toggle="buttons" id="topButtonDiv" style="width: 100%;">
                                    <button type="button" class="btn btn-primary" id="btn-lectura" style="width: 50%;" onclick="selectPermiso(1)">Lectura
                                    <input class="permsiso" type="radio" id="radio1" name="permiso" value="1" checked></button>
                                    <button type="button" class="btn btn-default" id="btn-escritura" style="width: 50%;" onclick="selectPermiso(2)">Escritura
                                    <input class="permsiso" type="radio" id="radio2" name="permiso" value="2"> </button>              
                                </div>
                            </fieldset>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary" onclick="agregarUsuario()">Guardar</button>                                
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Cerrar</button>
                </div>
            </div>
        </div>
    </div>

<script src="/js/app/gen-arbol.js"></script>
<script src="/js/jstree.min.js"></script>
<script src="/js/app/seleccionar-usuarios-expediente.js"></script>

<#include "bandeja-footer.ftl">
