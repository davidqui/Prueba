
<#assign pageTitle = ""/>
<#setting number_format="computer" />
<#include "bandeja-header.ftl">
<#include "gen-arbol-trd.ftl">
<#import "spring.ftl" as spring />

<div class="container-fluid">
    <ol class="breadcrumb">
        <li><a href="/expediente/listarExpedientes?">Inicio</a></li>
        <li class="active"><a href="/expediente/listarDocumentos?expId=${expediente.expId}">${expediente.expNombre}</a></li>
        <li class="active"><a href="/expediente/administrarExpediente?expId=${expediente.expId}">Detalle del expediente</a></li>
        <#if !expediente.indAprobadoInicial && expediente.expTipo == 2><li class="active"><a href="/expediente/trds-expediente/${expediente.expId}">Seleccionar trds</a></li></#if>
        <li class="active">Asignar Usuarios</li>
    </ol>
</div>

<h4 style="text-align: center;">Agregar usuarios al Expediente "${(expediente.expNombre)!""}"</h4>

<button type="button" class="btn btn-primary btn-sm" data-toggle="modal" data-target="#exampleModal" style="float: left; margin-left: 25px;" onclick="limpiarModales()">
    Agregar usuario
</button>
<#if esJefeDependencia==true>
    <a href="/expediente/administrarExpediente?expId=${(expediente.expId)!""}" class="btn btn-primary btn-sm" style="float: right;">
        Regresar a Expediente
    </a>
<#else>
    <button type="button" class="btn btn-success btn-sm" data-toggle="modal" data-target="#enviarJefeModal" style="float: right;">
        Enviar para aprobar
    </button>
</#if>
<div class="container-fluid">

    <div style="display: grid; grid-gap: 5px; grid-template-columns: repeat(auto-fit, 450px); grid-template-rows: repeat(2, 180px); width: 100%;">
        <#if usuCreador??>
            <div class="card" style="margin: 10px;">
                <#if esJefeDependencia==true>
                    <button type="button" 
                            class="btn btn-primary bmd-btn-fab" style="position: absolute;
                            right: 0px;height: 43px;" title="Modificar" data-toggle="modal" 
                            data-target="#cambiarUsuarioCreador" onclick="editarUsuarioCreador('${(usuCreador.usuGrado.id)!""} ${(usuCreador.nombre)!""}')">
                        M
                    </button>
                </#if>
                <div class="card-header">
                  ADMINISTRADOR
                </div>
                <div class="card-body">
                  <blockquote class="blockquote mb-0">
                    <h5 class="card-title">${(usuCreador.usuGrado.id)!""} ${(usuCreador.nombre)!""}</h5>
                    <p style="font-size:12px;">ADMINISTRADOR EXPEDIENTE</p>
                  </blockquote>
                </div>
                <span style="
                      position: absolute;
                      right: 5px;
                      bottom: 3px;
                      font-size: 18px;
                      color: #d8d8d8;">
                ${(usuCreador.clasificacion.nombre)!""}</span>
            </div>
        </#if>
        <#if jefeDependencia??>
            <div class="card" style="margin: 10px;">
                <div class="card-header">
                  JEFE DEPENDENCIA
                </div>
                <div class="card-body">
                  <blockquote class="blockquote mb-0">
                    <h5 class="card-title">${(jefeDependencia.usuGrado.id)!""} ${(jefeDependencia.nombre)!""}</h5>
                    <p style="font-size:12px;">ADMINISTRADOR EXPEDIENTE</p>
                  </blockquote>
                </div>
                <span style="
                      position: absolute;
                      right: 5px;
                      bottom: 3px;
                      font-size: 18px;
                      color: #d8d8d8;">
                ${(jefeDependencia.clasificacion.nombre)!""}</span>
            </div>
        </#if>
        <#if usuarios??>
            <#list usuarios as usuario> 
                <div class="card" style="margin: 10px;">
                    <#if !usuario.indAprobado>
                        <div style="
                            position:  absolute;
                            width:  100%;
                            height:  100%;
                            background-color: rgba(37, 39, 27, 0.39);
                        ">
                            <p style="
                            color:  white;
                            font-size:  20px;
                            text-align:  center;
                            font-weight:  bold;
                        ">POR APROBAR</p>

                        </div>
                    </#if>
                    <button type="button" 
                            class="btn btn-primary bmd-btn-fab" style="position: absolute;
                            right: 45px;height: 43px;" title="Modificar" data-toggle="modal" 
                            data-target="#usuarioEditModal" 
                            onclick="editarUsuario(${(usuario.usuId.id)}, '${(usuario.usuId.usuGrado.id)!""} ${(usuario.usuId.nombre)!""}', ${(usuario.expUsuId)!""}, ${(usuario.permiso)!""})">
                        M
                    </button>
                    <button type="button" class="btn btn-danger bmd-btn-fab eliminar" 
                            style="position: absolute;right: 0px;height: 43px;" 
                            onclick="eliminarUsuario(${(usuario.expUsuId)!""}, ${(expediente.expId)!""})"
                            data-toggle="tooltip" data-placement="top" title="Eliminar">
                        X
                    </button>
                    <div class="card-header">
                      <#if usuario.permiso == 1>
                        Letura
                      </#if>
                      <#if usuario.permiso == 2>
                        Indexación
                      </#if>
                    </div>
                    <div class="card-body">
                      <blockquote class="blockquote mb-0">
                        <h5 class="card-title">${(usuario.usuId.usuGrado.id)!""} ${(usuario.usuId.nombre)!""}</h5>
                        <p style="font-size:12px;">${(usuario.carId.carNombre)!""}</p>
                        <footer class="blockquote-footer" style="font-size:12px;">Agregado <cite title="Source Title">${(usuario.fecCreacion)!""}</cite></footer>
                      </blockquote>
                    </div>
                    <span style="
                          position: absolute;
                          right: 5px;
                          bottom: 3px;
                          font-size: 18px;
                          color: #d8d8d8;">
                    ${(usuario.usuId.clasificacion.nombre)!""}</span>
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
                                    <button type="button" class="btn btn-default" id="btn-escritura" style="width: 50%;" onclick="selectPermiso(2)">Indexación
                                    <input class="permsiso" type="radio" id="radio2" name="permiso" value="2"> </button>              
                                </div>
                            </fieldset>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary" onclick="agregarUsuario(${(expediente.expId)!""})">Agregar</button>                                
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
                                <input type="hidden" id="expUsuario" name="destinoUsuario" value="" />
                            </div>
                            <div class="form-group">
                                <label for="cargoAsignado2">Cargo (*)</label>
                                <select class="form-control" id="cargoAsignado2" name="cargoAsignado">
                                </select>
                            </div>
                            <fieldset class="form-group">
                                <label for="trd">Permiso</label>
                                <div class="btn-group" data-toggle="buttons" id="topButtonDiv" style="width: 100%;">
                                    <button type="button" class="btn btn-primary" id="btn-lectura2" style="width: 50%;" onclick="selectPermiso2(1)">Lectura
                                    <input class="permsiso" type="radio" id="radio21" name="permiso" value="1" checked></button>
                                    <button type="button" class="btn btn-default" id="btn-escritura2" style="width: 50%;" onclick="selectPermiso2(2)">Indexación
                                    <input class="permsiso" type="radio" id="radio22" name="permiso" value="2"> </button>              
                                </div>
                            </fieldset>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary" onclick="editarUsuarioPost(${(expediente.expId)!""})">Guardar</button>                                
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Cerrar</button>
                </div>
            </div>
        </div>
    </div>
    
     <#-- Modal cambiar usuario creador. -->
    <div class="modal fade" id="cambiarUsuarioCreador" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">Cambiar Administrador</h5>
                </div>
                <div class="modal-body">
                    <div class="card">
                        <div class="card-body">
                            <fieldset class="form-group">
                                <label for="trd">Administrador Anterior</label>
                                <input disabled type="text" class="form-control" id="actual_usuario_administrador" name="actual_usuario_administrador" value="${(observacionDefecto.textoObservacion)!""}"/>
                            </fieldset> 
                            <div class="form-group">
                                <label for="usuarioAsignado">Usuario (*)</label>
                                <div class="input-group">
                                    <input type="text" id="destinoUsuario_visible3" name="destinoUsuario_visible3" class="form-control" value="" disabled />
                                    <div class="input-group-btn">
                                        <button type="button" class="btn btn-primary" onclick="openFinderWindow()">Buscar</button>
                                    </div>
                                    <script src="/js/app/buscar-usuario.js"></script>
                                </div>
                                <input type="hidden" id="destinoUsuario3" name="destinoUsuario" value="" />
                            </div>
                            <p><span style="color: red;">Advertencia</span>, el cambio de usuario administrador de expediente a un usuario ajeno a su dependencia. Esto generará cambio de jefe de sección del expediente, lo que implica ceder el control total del expediente a la dependencia destino.</p>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary" onclick="cambiarUsuarioCreador(${(expediente.expId)!""})">Cambiar</button>                                
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Cerrar</button>
                </div>
            </div>
        </div>
    </div>
    
    
    
    <!-- Modal enviar a jefe dependencia -->
    <div class="modal fade" id="enviarJefeModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
      <div class="modal-dialog" role="document">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="exampleModalLabel">Enviar para revisión</h5>
            <button type="button" class="close" data-dismiss="modal" aria-label="Close" style="position: absolute; right: 7px; top: 5px;">
              <span aria-hidden="true">&times;</span>
            </button>
          </div>
          <div class="modal-body">
            Al enviar para aprobación los cambios que halla realizado no tomaran efecto hasta que el jefe de su dependencia los apruebe.
          </div>
          <div class="modal-footer">
            <a class="btn btn-secondary" data-dismiss="modal" >Cancelar</a>
            <a href="/expediente/enviarAprobar/${(expediente.expId)!""}" type="button" class="btn btn-primary">Enviar</a>
          </div>
        </div>
      </div>
    </div>
    
    <!-- Modal info -->
    <div class="modal fade" id="info-modal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLongTitle" aria-hidden="true" style="overflow-y: auto;">
      <div class="modal-dialog" role="document">
        <div class="modal-content">
          <div class="modal-header" style="background-color: #0275d8; color: white;">
            <h5 class="modal-title" id="title-modal"></h5>
          </div>
          <div class="modal-body" id="modal-body-info">
          </div>
          <div class="modal-footer">
            <a class="btn btn-primary" data-dismiss="modal" >Aceptar</a>
          </div>
        </div>
      </div>
    </div>
    

<script src="/js/app/gen-arbol.js"></script>
<script src="/js/jstree.min.js"></script>
<script src="/js/app/seleccionar-usuarios-expediente.js"></script>

<#include "bandeja-footer.ftl">
