<#assign pageTitle = ""/>
<#include "bandeja-header.ftl">
<#include "gen-paginacion.ftl">


<div class="container-fluid">
    <ol class="breadcrumb">
        <li><a href="/expediente/listarExpedientes?">Inicio</a></li>
        <li class="active"><a href="/expediente/${expediente.expId}">${expediente.expNombre}</a></li>
        <li class="active">Detalle del expediente</li>
    </ol>
</div>
<div class="container-fluid">
    <div class="col-md-4 col-lg-3" >
        <div class="card">
            <div class="card-header">
                Transiciones
            </div>
            <#if (expTransicion??) && (expTransicion?size > 0) >
                <ul class="list-group list-group-flush" style="height: 300px; overflow-y: scroll;">
                    <#list expTransicion as h>
                        <li class="list-group-item">
                            <strong>${h.expEstId.estNombre}</strong>
                            <br/>
                            <small>${h.fecCreacion}</small>
                            <#if h.usuModificado??>
                                <br/>
                                Usuario: ${h.usuModificado?string}
                            </#if>
                            <#if h.docId??>
                                <br/>
                                Documento: ${h.docId.asunto?string}
                            </#if>
                            <br/>
                            <ul>
                                <li><i><b>Realizó</b></i>: ${h.usuCreacion?string}</li>
                            </ul>
                        </li>
                    </#list>
                </ul>
            </#if>
        </div>
        <#if expediente.indUsuCreador || expediente.indJefeDependencia>
            <div class="card">
                <div class="card-header">
                    Observaciones
                </div>

                <#if (observaciones)??>
                    <div class="card m-y">                           		        
                        <div class="card-block" id="obsDiv" style="height: 200px; overflow-y: scroll;">
	                    <#list observaciones as obs>
                                <strong>${utilController.nombre(obs.usuId.id)}</strong>, <em> ${obs.fecCreacion?string('yyyy-MM-dd hh:mm a:ss')}</em>
                                <p>${obs.expObservacion}</p>
                                <hr/>
	                    </#list>
                        </div>
                    </div>
                <#else>
                    <div class="card-block">
                        No hay Observaciones
                   </div>
	        </#if>
                
            </div>
        </#if>
    </div>
        
    <div class="col-md-8 col-lg-9">
        <div class="pull-xs-right">
            <#if expediente.indUsuCreador || expediente.indJefeDependencia>
                <a title="Administrar Usuarios" href="/expediente/asignar-usuario-expediente/${expediente.expId}">
                    <img class="card-img-top" src="/img/users.svg" alt=""/>
                </a>
                <a title="Modificar Tipo de expediente" href="#">
                    <img class="card-img-top" src="/img/edit-2.svg" alt=""/>
                </a>
                <#if expediente.expTipoId == 2>
                    <a title="Administrar Trds" href="/expediente/trds-expediente/${expediente.expId}">
                        <img class="card-img-top" src="/img/plus.svg" alt=""/>
                    </a>
                </#if>
            </#if>
            <#if (expediente.indUsuCreador || expediente.indJefeDependencia || expediente.indIndexacion) && expediente.indAprobadoInicial && !expediente.indCerrado>
                <a title="indexar Documento" href="#">
                    <img class="card-img-top" src="/img/file-plus.svg" alt=""/>
                </a>
            </#if>
            <#if expediente.indUsuarioAsignado == 1 && expediente.indJefeDependencia>
                <a id="btnAprobar" title="El expediente se encuentra con cambios sin aprobar" onclick="mostrarCambiosPendientes(${expediente.expId})" data-toggle="modal" href="#enviarJefeModal">
                    <img class="card-img-top" src="/img/alert-circle.svg" alt=""/>
                </a>
            </#if>
        </div>
        <h1 class="cus-h1-page-title">Detalle del expediente</h1>
        
        <div>
            <label>Nombre</label>
            <input type="text"  value="${expediente.expNombre}" class="form-control" disabled/>
            <label>Dependencia</label>
            <input type="text"  value="${expediente.depNombre}" class="form-control" disabled/>
            <label>Fecha de Creación</label>
            <input type="text"  value="${expediente.fecCreacion?string('yyyy-MM-dd')}" class="form-control" disabled/>
            <label>Tipo Expediente</label>
            <input type="text"  value="${expediente.expTipo}" class="form-control" disabled/>
            <label>Trd Principal</label>
            <input type="text"  value="${expediente.trdNomIdPrincipal}" class="form-control" disabled/>
            <label>Usuario administrador</label>
            <input type="text"  value="${expediente.usuarioCreador}" class="form-control" disabled/>
            <label>Jefe Dependencia</label>
            <input type="text"  value="${expediente.jefeDependencia}" class="form-control" disabled/>
            <label>Descripción</label>
            <input type="text"  value="${expediente.expDescripcion}" class="form-control" disabled/>
            <label>Número de usuarios</label>
            <input type="text"  value="${expediente.numUsuarios}" class="form-control" disabled/>
            <label>Número de Documentos</label>
            <input type="text"  value="${expediente.numDocumentos}" class="form-control" disabled/>
            <br/>
            <br/>
            <#if expediente.indUsuCreador || expediente.indJefeDependencia>
                <div class="card-block cus-gray-bg">
                    <form method="post" id="obsForm">
                        <fieldset class="form-group">
                            <input type="hidden" id="expId" value="${expediente.expId}">
                            <textarea class="form-control" id="observacion" name="observacion"></textarea>
                        </fieldset>
                        <div class="row">
                            <div class="col-xs-4">
                                <a href="#" class="btn btn-secondary btn-sm" id="obsButton">Comentar</a>
                            </div>

                            <div class="col-xs-8">
                                <select id="doc-obs-defecto-select" name="doc-obs-defecto-select" class="form-control input-sm" onchange="setObservacionDefecto(this, 'observacion')">
                                    <option value="">Lista de observaciones por defecto:</option>
                                    <#list observacionesDefecto as observacionDefecto >
                                        <option value="${observacionDefecto.id}">${observacionDefecto.textoObservacion}</option>
                                    </#list>
                                </select>
                            </div>
                        </div>
                    </form>
                </div>
            </#if>
        </div>
    </div>
</div>


<!-- Modal aprobar cambios por jefe de dependencia -->
<div class="modal fade" id="enviarJefeModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="exampleModalLabel">Aprobación</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close" style="position: absolute; right: 7px; top: 5px;">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
        Aqui se listaran los cambios pendientes para su aprobación. Recuerde diligenciar la observación:
            <div class="card-block cus-gray-bg">
                <form method="post" id="obsForm">
                    <fieldset class="form-group">
                        <input type="hidden" id="expId" value="${expediente.expId}">
                        <textarea class="form-control" id="observacion2" name="observacion2"></textarea>
                    </fieldset>
                    <div class="row">
                        <div class="col-xs-8">
                            <select id="doc-obs-defecto-select" name="doc-obs-defecto-select" class="form-control input-sm" onchange="setObservacionDefecto(this, 'observacion2')">
                                <option value="">Lista de observaciones por defecto:</option>
                                <#list observacionesDefecto as observacionDefecto >
                                    <option value="${observacionDefecto.id}">${observacionDefecto.textoObservacion}</option>
                                </#list>
                            </select>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-contenido">
            </div>
        
        
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" onclick="aprobarOrechazarCambios(0)">Rechazar</button>
        <button type="button" class="btn btn-primary" onclick="aprobarOrechazarCambios(1)" >Aprobar</button>
      </div>
    </div>
  </div>
</div>
<script src="/js/app/expediente-administrar.js"></script>
<script src="/js/app/documento-observaciones.js"></script>
<#include "bandeja-footer.ftl">
