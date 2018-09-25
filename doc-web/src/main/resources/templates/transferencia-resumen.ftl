<#setting number_format="computer">

<#assign pageTitle = "Resumen de Transferencia" />
<#assign deferredJS = "" />

<#import "spring.ftl" as spring />
<#include "header.ftl" />
<#include "lib/transferencia-archivo_functions.ftl" />
<#assign selectAllText = "Seleccionar todos" />
<#assign removeAllText = "Retirar todos" />
<style>
    i {
        border: solid #2e73a1;
        border-width: 0 10px 10px 0;
        display: inline-block;
        padding: 10px;
    }
    
    .down {
        transform: rotate(45deg);
        -webkit-transform: rotate(45deg);
    }
    
    .object {
        animation: MoveUpDown 1s linear infinite;
        position: absolute;
        left: 50%;
        bottom: 0;
    }

    @keyframes MoveUpDown {
      0%, 100% {
        bottom: 0;
      }
      50% {
        bottom: 5px;
      }
    }
    
    .card-block-scroll{
        overflow: hidden;
        overflow-y: auto;
    }
    
    p{
        text-align: justify;
    }
    
    .card{
        margin:0px;
    }
    
    .modal-header{
        background-color: #2e73a1;
        color: white;
    }
    
    .span-badge{
        font-size: 14px;
        padding: 4px 6px;
        background-color: white;
        color: #0275d8;
        margin-left: 10px;
        border-radius: 20px;
        font-weight: bold;
    }
    
    @media only screen and (max-width: 800px) {
        .col-sm-4{
            width: 100%;
        }
        
        .card{
            margin: 20px 0px;
        }
    }
    
}
</style>
<div class="container-fluid">
    <div>
        <ol class="breadcrumb">
            <li><a href="/transferencia-archivo/listar?">Inicio</a></li>
            <li class="active">Resumen</li>
        </ol>
    </div>
<h4>${pageTitle}</h4>
<div class="row" style="padding-bottom: 70px;">
  <div class="col-xs-6 col-sm-4">
   <div class="card">
    <div class="card-header">
      Documentos
    </div>
    <div class="card-block" style="padding:0;">
         <table class="table" style="margin: 0px;">
            <thead>
                <tr>
                    <th>Radicado</th>
                    <th>Asunto</th>
                    <#if documentosNoPosesionTransferencia??>
                        <th></th>
                    </#if>   
                </tr>
            </thead>
        </table>
        <div class="card-block-scroll" style="height: 538px;">
            <table class="table">
                <tbody>
                    <#if documentosXTransferenciaArchivo??>
                        <#list documentosXTransferenciaArchivo as doc>
                            <tr>
                                <td nowrap name="radicado">${doc.documentoDependencia.documento.radicado!"&lt;Sin radicado&gt;"}</td>
                                <td >${doc.documentoDependencia.documento.asunto!"&lt;Sin asunto&gt;"}</td>
                                <#if documentosNoPosesionTransferencia??>
                                    <td>
                                        <#if controller.hasDocumentoTransferido(doc.id, documentosNoPosesionTransferencia)>
                                            <img class="svg" src="/img/check.svg" alt=""/>
                                        <#else>
                                            <img class="svg" src="/img/x.svg" alt=""/>  
                                        </#if>
                                    </td>
                                </#if>
                            <tr>
                        </#list>
                    </#if>
                </tbody>
            </table> 
        </div>
    </div>
  </div>
  <#if transferenciaArchivo.usuarioAsignado = 0 && transferenciaArchivo.origenUsuario.id == usuario.id>
    <a class="btn btn-primary" style="
      width:  100%;
      border-bottom-left-radius: 100px;
      border-bottom-right-radius: 100px;
    "
     href="/transferencia-archivo/seleccionar-documentos/${(transferenciaArchivo.id)!""}">
      Editar Documentos
     </a>
  </#if>
 </div>
<div class="col-xs-6 col-sm-4">
    <div class="card">
        <div class="card-header">
          Expedientes
        </div>
        <div class="card-block" style="padding:0;">
            <table class="table" style="margin: 0px;">
                <thead>
                    <tr>
                        <th style="width:50%;">Nombre</th>
                        <th>Dependencia</th>
                        <#if expedientesNoPosesionTransferencia??>
                            <th></th>
                        </#if>
                    </tr>
                </thead>
            </table>
            <div class="card-block-scroll" style="height: 538px;">
                <table class="table">
                    <tbody>
                        <#if expedientesSeleccionados??>
                            <#list expedientesSeleccionados as exp>
                                <tr>
                                    <td>${exp.expId.expNombre!"&lt;Sin asunto&gt;"}</td>
                                    <td>${exp.expId.depId.nombre}</td>
                                    <#if expedientesNoPosesionTransferencia??>
                                        <td>
                                            <#if controller.hasExpedienteTransferido(exp.expId.expId ,expedientesNoPosesionTransferencia)>
                                                <img class="svg" src="/img/check.svg" alt=""/>
                                            <#else>
                                                <img class="svg" src="/img/x.svg" alt=""/>
                                            </#if>
                                        </td>
                                    </#if>
                                </tr>
                            </#list>
                        </#if>
                    </tbody>
                </table> 
          </div>
        </div>
    </div>
    <#if transferenciaArchivo.usuarioAsignado = 0 && transferenciaArchivo.origenUsuario.id == usuario.id>
        <a class="btn btn-primary" style="
        width:  100%;
        border-bottom-left-radius: 100px;
        border-bottom-right-radius: 100px;
        "
        href="/transferencia-archivo/seleccionar-expediente/${(transferenciaArchivo.id)!""}">
            Editar Expedientes
        </a>
    </#if>
</div>
<div class="col-xs-6 col-sm-4">
     <div class="card">
        <div class="card-header" style="text-align: center; padding: 16px; background-color: #f5f5f5; border-bottom: 1px solid #e5e5e5;">
            <h4 style="margin:0;"><b>${(transferenciaArchivo.origenUsuario.usuGrado.id)!""} ${(transferenciaArchivo.origenUsuario.nombre)!""}</b></h4>
            <span>${(transferenciaArchivo.origenDependencia.nombre)!""}</span>
            <p style="position:relative; margin-top: 25px; margin-bottom: 35px;"><i class="down object"></i></p>
            <h4 style="margin:0;"><b>${(transferenciaArchivo.destinoUsuario.usuGrado.id)!""} ${(transferenciaArchivo.destinoUsuario.nombre)!""}</b></h4>
            <span>${(transferenciaArchivo.destinoDependencia.nombre)!""}</span>
        </div>
        <div class="card-block card-block-scroll">
        <div>
            <div><span>Número de documentos <b>${(documentosXTransferenciaArchivo?size)?string}</b></span> <span style="float:right;">Número de expedientes <b>${(expedientesSeleccionados?size)?string}</b></span></div>
        </div>
        </br>
        <p><b>Fecha Creación:</b> ${(transferenciaArchivo.fechaCreacion?string('dd/MM/yyyy HH:mm'))!""}</p>
        <p><b>Justificación:</b> ${(transferenciaArchivo.justificacion)!""}</p>
      </div>
    </div>
    <div class="card">
        <div class="card-header">
          Transiciones
        </div>
        <#if (transiciones??) && (transiciones?size > 0) >
        <ul class="list-group list-group-flush" style="max-height: 165px;overflow: hidden;overflow-y: auto;">
            <#list transiciones as tr>
            <li class="list-group-item">
                <strong>${(tr.traEstId.traEstNombre)!""}</strong>
                <br/>
                <small>${(tr.fecCreacion?string('dd/MM/yyyy HH:mm'))!""}</small>
                <br/>
                <ul>
                    <li><b>Realizó</b>: ${(tr.usuCreacion.usuGrado.id)!""} ${(tr.usuCreacion.nombre)!""}</li>
                </ul>
            </li>
            </#list>
        </ul>
        </#if>
    </div>
        <div class="card">
        <div class="card-header">
          Observaciones
        </div>
        <#if (observaciones??) && (observaciones?size > 0) >
        <ul class="list-group list-group-flush" style="max-height: 165px;overflow: hidden;overflow-y: auto;">
            <#list observaciones as observacion>
            <li class="list-group-item">
                <strong>${(observacion.usuId.usuGrado.id)!""}${(observacion.usuId.nombre)!""}</strong> --
                <small>${(observacion.fecCreacion?string('dd/MM/yyyy HH:mm'))!""}</small>
                <br/>
                <p>${(observacion.traObservacion)!""}</p>
            </li>
            </#list>
        </ul>
        </#if>
    </div>
</div>
    
<div class="navbar navbar-default navbar-fixed-bottom text-xs-center hermes-bottombar">
    <#if transferenciaArchivo.destinoUsuario.id == usuario.id>
        <#if transferenciaArchivo.usuarioAsignado = 1 && transferenciaArchivo.indAprobado = 0>
            <button class="btn btn-primary" onclick="modalDestinatario(${(usuario.id)!""}, '${(usuario.nombre)!""}')" data-toggle="modal"  data-target="#destinatarioRecibir">
              Recibir
            </button>
        </#if>
        <#if transferenciaArchivo.usuarioAsignado = 2 && transferenciaArchivo.indAprobado = 1 && transferenciaArchivo.activo = true
            && ((documentosXTransferenciaArchivo?size != documentosNoPosesionTransferencia?size && documentosXTransferenciaArchivo?size>0) ||
                (expedientesSeleccionados?size != expedientesNoPosesionTransferencia?size && expedientesSeleccionados?size > 0))>
            <a class="btn btn-warning" data-toggle="modal"  data-target="#modalReenviar" >
              Reenviar Transferencia  
            </a>
        </#if>
    </#if>
    <#if transferenciaArchivo.usuarioAsignado = 2  && transferenciaArchivo.origenUsuario.dependencia.jefe.id == usuario.id && transferenciaArchivo.indAprobado = 0>
        <a href="/transferencia-archivo/aprobar/${(transferenciaArchivo.id)!""}" class="btn btn-primary" onclick="loading(event);">
          Aprobar
        </a>
    </#if>
    <#if transferenciaArchivo.usuarioAsignado = 0 && transferenciaArchivo.origenUsuario.id == usuario.id>
        <#if (documentosXTransferenciaArchivo?size > 0 || expedientesSeleccionados?size > 0) >
            <a href="/transferencia-archivo/enviar/${(transferenciaArchivo.id)!""}" class="btn btn-primary">
              Transferir <span class="span-badge" id="counterExp">${(documentosXTransferenciaArchivo?size)?string} Documentos</span> <span class="span-badge" id="counterExp">${(expedientesSeleccionados?size)?string} expedientes</span>
            </a>
        </#if>
        <a href="/transferencia-archivo/anular/${(transferenciaArchivo.id)!""}" class="btn btn-danger" onclick="loading(event);">
          Anular
        </a>
    </#if>
    <#if (transferenciaArchivo.usuarioAsignado = 1 && transferenciaArchivo.destinoUsuario.id == usuario.id) || 
(transferenciaArchivo.usuarioAsignado = 2 && transferenciaArchivo.origenUsuario.dependencia.jefe.id == usuario.id) && transferenciaArchivo.indAprobado = 0>
        <button class="btn btn-danger" data-toggle="modal"  data-target="#rechazar-modal">
          Rechazar
        </button>
    </#if>
    
    <#if transferenciaArchivo.fuid??>
        <a class="btn btn-success" onclick="mostrarFuid('${transferenciaArchivo.fuid}')" data-toggle="modal" href="#visualizarFuid">
            Ver Fuid
        </a>
    </#if>
    <#if transferenciaArchivo.docId??>
        <a class="btn btn-success" href="/documento?pin=${transferenciaArchivo.docId.instancia.id}" target="_blank">Ver Acta</a>
    </#if>
</div>

<#-- Modal de aceptar por destinatario. -->
<div class="modal fade" id="destinatarioRecibir" tabindex="-1" role="dialog" aria-labelledby="destinatarioRecibir" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="exampleModalLabel">Recibir Transferencia</h5>
            </div>
            <div class="modal-body">
                <div class="card">
                    <div class="card-body">                                        
                        <div class="form-group">
                            <label for="usuarioAsignado">Usuario (*)</label>
                            <div class="input-group" style="width: 100%;">
                                <input type="text" id="destinoUsuario_visible2" name="destinoUsuario_visible" class="form-control" value="" disabled />
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="cargoAsignado2">Cargo (*)</label>
                            <select class="form-control" id="cargoAsignado2" name="cargoAsignado">
                            </select>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" onclick="recibirDestinatario(${(transferenciaArchivo.id)!""})">Recibir</button>                                
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancelar</button>
            </div>
        </div>
    </div>
</div>

<!-- Modal Rechazar -->
<div class="modal fade" id="rechazar-modal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLongTitle" aria-hidden="true" style="overflow-y: auto;">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="title-modal">Rechazar Transferencia</h5>
      </div>
      <div class="modal-body" id="modal-body-info">
          <div class="form-group">
            <label for="expDescripcion">Observación:</label>
            <textarea class="form-control" rows="3" id="observacion" name="observacion" maxlength="1000"></textarea>
          </div>
      </div>
      <div class="modal-footer">
        <a class="btn btn-danger" onclick="rechazarTransferencia(${(transferenciaArchivo.id)!""})">Rechazar</a>
        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancelar</button>
      </div>
    </div>
  </div>
</div>
    
    
<!--modal reenviar transferencia-->
<div class="modal fade" id="modalReenviar" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="exampleModalLabel">Reenviar Transferencia a usuario</h5>
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
                        <div class="card-block cus-gray-bg">
                            <fieldset class="form-group">
                                <textarea class="form-control" rows="5" id="justificacion" name="justificacion" required>${justificacion!""}</textarea>
                            </fieldset> 
                            <div class="row">
                                <div class="col-xs-8">
                                    <select id="doc-obs-defecto-select" name="doc-obs-defecto-select" class="form-control input-sm" onchange="setObservacionDefecto(this, 'justificacion')">
                                        <option value="">Lista de justificaciones por defecto:</option>
                                        <#list justificacionesDefecto as justificacionDefecto >
                                        <option value="${justificacionDefecto.tjdId}">${justificacionDefecto.textoObservacion}</option>
                                        </#list>
                                    </select>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" onclick="reenviarTransferenciaUsuario(${(transferenciaArchivo.id)!""})">Reenviar</button>                                
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Cerrar</button>
            </div>
        </div>
    </div>
</div>
    
<!-- Confirmar reenvio info -->
<div class="modal fade" id="confirmar-reenvio-modal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLongTitle" aria-hidden="true" style="overflow-y: auto;">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header" style="background-color: #0275d8; color: white;">
        <h5 class="modal-title" id="title-modal"></h5>
      </div>
      <div class="modal-body" id="modal-body-info">
      </div>
        <div class="modal-footer">
            <button type="button" class="btn btn-primary" onclick="reenviarTransferencia(${(transferenciaArchivo.id)!""})">Continuar</button>                                
            <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancelar</button>
        </div>
    </div>
  </div>
</div>   
    
    
<!-- Modal para visualizar Fuid -->
<div class="modal fade" id="visualizarFuid" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-lg" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="exampleModalLabel">Formato único de inventario documental</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close" style="position: absolute; right: 7px; top: 5px;">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
      </div>
    </div>
  </div>
</div>
    
<!-- Modal info -->
<div class="modal fade" id="info-modal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLongTitle" aria-hidden="true" style="overflow-y: auto;">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header" style="background-color: #0275d8; color: white;">
        <h5 class="modal-title" id="title-modal-message"></h5>
      </div>
      <div class="modal-body" id="modal-body-info-message">
      </div>
      <div class="modal-footer">
        <a class="btn btn-primary" data-dismiss="modal" >Aceptar</a>
      </div>
    </div>
  </div>
</div>
    
    
<script src="/js/app/transferencia-resumen.js"></script>
<#include "footer.ftl" />
