<#assign pageTitle = ""/>
<#include "loader.ftl">
<#include "bandeja-header.ftl">
<#include "gen-paginacion.ftl">


<div class="container-fluid">
    <ol class="breadcrumb">
        <li><a href="/expediente/listarExpedientes?">Inicio</a></li>
        <li class="active"><a href="/expediente/listarDocumentos?expId=${expediente.expId}">${expediente.expNombre}</a></li>
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
        <h1 class="cus-h1-page-title">Detalle del expediente</h1>
        </br>
        <div>
            <div class="form-group">
                <label style="font-weight:bold;" class="control-label col-sm-2">Nombre:</label>
                <label>${expediente.expNombre}</label>
            </div>
            
            <div class="form-group">
                <label style="font-weight:bold;" class="control-label col-sm-2">Dependencia</label>
                <label>${expediente.depNombre}</label>
            </div>
            
            <div class="form-group">
                <label style="font-weight:bold;" class="control-label col-sm-2">Fecha de Creación</label>
                <label>${expediente.fecCreacion?string('yyyy-MM-dd')}</label>
            </div>
            
            <div class="form-group">
                <label style="font-weight:bold;" class="control-label col-sm-2">Tipo Expediente</label>
                <label>${expediente.expTipo}</label>
            </div>
            
            <div class="form-group">
                <label style="font-weight:bold;" class="control-label col-sm-2">Trd Principal</label>
                <label>${expediente.trdNomIdPrincipal}</label>
            </div>
            
            <div class="form-group">
                <label style="font-weight:bold;" class="control-label col-sm-2">Usuario administrador</label>
                <label>${expediente.usuarioCreador}</label>
            </div>
            
            <div class="form-group">
                <label style="font-weight:bold;" class="control-label col-sm-2">Jefe Dependencia</label>
                <label>${expediente.jefeDependencia}</label>
            </div>
            
            <div class="form-group">
                <label style="font-weight:bold;" class="control-label col-sm-2">Descripción</label>
                <label>${expediente.expDescripcion}</label>
            </div>
            
            <div class="form-group">
                <label style="font-weight:bold;" class="control-label col-sm-2">Fecha Mínima</label>
                <label>
                <#if expediente.fecMinDocumento??>
                    ${expediente.fecMinDocumento?string('yyyy-MM-dd')}
                <#else>
                    No definido
                </#if>
                </label>
            </div>
            
            <div class="form-group">
                <label style="font-weight:bold;" class="control-label col-sm-2">Fecha Máxima</label>
                <label>
                <#if expediente.fecMaxDocumento??>
                    ${expediente.fecMaxDocumento?string('yyyy-MM-dd')}
                <#else>
                    No definido                    
                </#if>
                </label>
            </div>
            
            <div class="form-group">
                <label style="font-weight:bold;" class="control-label col-sm-2">Número de usuarios</label>
                <label>${expediente.numUsuarios}</label>
            </div>
            
            <div class="form-group">
                <label style="font-weight:bold;" class="control-label col-sm-2">Número de Documentos</label>
                <label>${expediente.numDocumentos}</label>
            </div>
            
            <br/>
            <#if (expediente.indUsuCreador || expediente.indJefeDependencia) && !expediente.indCerrado>
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
                        </div>
                    </form>
                </div>
            </#if>
        </div>
    </div>
    <div class="navbar navbar-default navbar-fixed-bottom text-xs-center hermes-bottombar">
        <#if (expediente.indUsuCreador || expediente.indJefeDependencia) && !expediente.indCerrado>
            <a href="/expediente/asignar-usuario-expediente/${expediente.expId}" class="btn btn-warning" >
                Modificar Usuarios
            </a>
            <#if expediente.expTipoId == 2>
                <a href="/expediente/trds-expediente/${expediente.expId}" class="btn btn-warning" >
                    Modificar Trds
                </a>
            </#if>
        </#if>
        <#if (expediente.indUsuCreador || expediente.indJefeDependencia) && !expediente.indCerrado>
            <a href="#" onclick="modificarTipo(${expediente.expId})" class="btn btn-warning" >
                Modificar Tipo de expediente
            </a>
        </#if>
        
        <#if expediente.indJefeDependencia && !expediente.indCerrado>
            <a  onclick="cerrarExpediente(${expediente.expId})" class="btn btn-danger">
                Cerrar Expediente
            </a>
        </#if>
        
        <#if expediente.indJefeDependencia && expediente.indCerrado>
            <a onclick="abrirExpediente(${expediente.expId})" class="btn btn-danger" style="width: 100%; margin: 5px 0;">
                Abrir Expediente
            </a>
        </#if>
    </div>
</div>

    <!-- Modal info -->
    <div class="modal fade" id="info-modal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLongTitle" aria-hidden="true" style="overflow-y: auto;">
      <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
          <div class="modal-header" style="background-color: #0275d8; color: white;">
            <h5 class="modal-title" id="title-modal"></h5>
          </div>
          <div class="modal-body" id="modal-body-info" style="height: 300px; overflow-y: scroll;">
          </div>
          <div class="modal-footer">
            <a class="btn btn-primary" data-dismiss="modal" >Aceptar</a>
          </div>
        </div>  
      </div>
    </div>
<script src="/js/app/expediente-administrar.js"></script>
<script src="/js/app/documento-observaciones.js"></script>
<script>
    $( window ).on( "load", function() {
        $(".div-loader").css({ display: "none" });
    });
</script>
<#include "bandeja-footer.ftl">
