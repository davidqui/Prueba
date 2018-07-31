<#assign pageTitle = ""/>
<#include "bandeja-header.ftl">
<#include "gen-paginacion.ftl">


<div class="container-fluid">
    <ol class="breadcrumb">
        <li><a href="/expediente/listarExpedientes?">Inicio</a></li>
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
                <a title="Administrar Usuarios" href="#">
                    <img class="card-img-top" src="/img/users.svg" alt="">
                </a>
                <a title="Administrar Trds" href="#">
                    <img class="card-img-top" src="/img/plus.svg" alt="">
                </a>
            </#if>
            <#if expediente.indUsuCreador || expediente.indJefeDependencia || expediente.indIndexacion>
                <a title="indexar Documento" href="#">
                    <img class="card-img-top" src="/img/file-plus.svg" alt="">
                </a>
            </#if>
            <#if expediente.indUsuarioAsignado == 1>
                <a title="Cambios pendientes por revisar" href="#">
                    <img class="card-img-top" src="/img/alert-circle.svg" alt="">
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
<script src="/js/app/expediente-administrar.js"></script>
<script src="/js/app/documento-observaciones.js"></script>
<#include "bandeja-footer.ftl">
