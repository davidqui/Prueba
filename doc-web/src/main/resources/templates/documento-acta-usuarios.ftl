<#--
    2018-05-15 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
    feature-162.
-->
<#include "documento-acta-config.ftl">

<div class="col-md-8">    
    <#if estadoModo == "SELECCION_USUARIOS">
    <table class="table table-sm">    	
        <tbody>
            <tr><th>Asunto</th><td>${documento.asunto}</td></tr>
            <tr><th>Lugar</th><td>${documento.actaLugar}</td></tr>
            <tr><th>Fecha de elaboración</th><td>${yyyymmdd.format(documento.actaFechaElaboracion)}</td></tr>
            <tr class="table-danger"><th>Nivel de clasificación</th><td>${documento.clasificacion.nombre}</td></tr>
            <tr><th>TRD</th><td>${documento.trd.codigo} - ${documento.trd.nombre}</td></tr>
            <tr><th>Número de folios</th><td>${documento.numeroFolios}</td></tr>
        </tbody>            
    </table>
    
    <form action="/documento-acta/registrar-usuarios" method="POST" id="formdoc" enctype='multipart/form-data'>
        <input type="hidden" id="pin" name="pin" value="${procesoInstancia.id}" />
        
        <#if debeSeleccionarUsuarios >
        <div class="card">
            <div class="card-header">
                <button type="button" class="btn btn-secondary btn-sm" data-toggle="modal" data-target="#exampleModal">
                    Agregar usuario
                </button>
                <strong>Usuarios (*)</strong>                
            </div>
            <div class="card-body">
                <table class="table table-bordered">
                    <thead>
                        <tr>
                            <th>Usuario</th>
                            <th>Cargo</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        <#list usuariosAsignados as usuarioAsignado >
                        <tr>
                            <td>${usuarioAsignado.usuario.usuGrado}. ${usuarioAsignado.usuario.nombre}</td>
                            <td>${usuarioAsignado.cargo.carNombre}</td>
                            <td><button type="button" class="btn btn-danger btn-sm">Eliminar</button></td>
                        </tr>
                        </#list>
                    </tbody>
                </table>
                
                <div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
                    <div class="modal-dialog modal-dialog-centered" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="exampleModalLabel">Agregar usuario</h5>
                            </div>
                            <div class="modal-body">
                                <div class="card">
                                    <div class="card-body">
                                        <form>
                                            <div class="form-group">
                                                <label for="usuarioAsignado">Usuario (*)</label>
                                                <div class="input-group">
                                                    <input type="text" id="destinoUsuario_visible" name="destinoUsuario_visible" class="form-control" value="" disabled />
                                                    <div class="input-group-btn">
                                                        <button type="button" class="btn btn-primary" onclick="openUsuariosFinderWindow()">Buscar</button>
                                                    </div> 
                                                </div> 
                                                <input type="hidden" id="destinoUsuario" name="destinoUsuario" value="" />
                                            </div>
                                            <div class="form-group">
                                                <label for="cargoAsignado">Cargo (*)</label>
                                                <select class="form-control" id="cargoAsignado" name="cargoAsignado">
                                                </select>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-primary" onclick="agregarUsuarioActa()">Agregar</button>                                
                                <button type="button" class="btn btn-secondary" data-dismiss="modal">Cerrar</button>
                            </div>
                        </div>
                    </div>
                </div>                
            </div>
        </div>        
        <#else>
        <div class="alert alert-info" role="alert">La subserie TRD seleccionada no requiere selección de usuarios.</div>
        </#if>
        
        <nav class="navbar navbar-default navbar-fixed-bottom text-xs-center hermes-bottombar">
            <#if debeSeleccionarUsuarios >
            <button id="registrar-usuarios-btn" type="submit" class="btn btn-success btn-sm">Registrar</button>
            </#if>
            <#if !debeSeleccionarUsuarios || (usuariosAsignados?? && (usuariosAsignados?size > 0)) >
                <#list procesoInstancia.transiciones() as transicion >
                <button id="trx_${transicion.id}" class="btn ${getTransicionStyle(transicion)} btn-sm" type="button" onclick="processTransition(this, '${transicion.replace(procesoInstancia)}')">
                    ${transicion.nombre}
                </button>
                </#list>
            </#if>
        </nav>

    </form> <#-- Cierra formulario principal -->
    </#if>

    <#-- Observaciones -->       	
    <@presentarObservaciones documentoObservaciones utilController estadoModo "observacion" />    
</div>

<div class="col-md-4">
    <@presentarInformacionProcesoInstancia procesoInstancia documento />

    <#-- Adjuntos -->    
    <@presentarCargaAdjuntos documento procesoInstancia utilController estadoModo "SELECCION_USUARIOS" tipologias "archivo" />
    <br />
</div>

<#include "bandeja-footer.ftl" />
