<#assign pageTitle = "Expedientes"/>
<#include "bandeja-header.ftl">
<#include "gen-paginacion.ftl">

<div class="container-fluid">
    <div class="container-fluid">
        <ol class="breadcrumb">
            <li><a href="/expediente/listarExpedientes?">Inicio</a></li>
            <li class="active">${expediente.expNombre}</li>
        </ol>
    </div>
    <div>
        <#if !expediente.indCerrado>
            <span>
                <a  class="btn btn-success btn-sm bd-popover float-right" role="button" data-toggle="modal" data-trigger="hover" data-placement="right" title="Indexar documento" data-content="Pulse para indexar documento" style="float:left;" data-target="#agregarDocumento">Indexar Doocumento</a>
            </span>
        </#if>
        <span>
            <a title="Administrar expediente" href="/expediente/administrarExpediente?expId=${expediente.expId}" style="float:right;">
                <img class="card-img-top" src="/img/settings.svg" alt=""/>
            </a>  
        </span>
    </div>
    </br>
	
    <#if documentos?size = 0 >
        </br>
        </br>
        <div class="jumbotron">
            <h1 class="display-1">No hay registros</h1>
       </div>
    <#else>
        </br>
        </br>
        <table class="table">
            <thead>
                <tr>
                    <th>Asunto</th>
                    <th>Número de Radicado</th>
                    <th>Clasificación</th>
                    <th>Acción</th>
                </tr>
            </thead>
            <tbody>
                <#list documentos as doc>
                    <tr>
                        <#if doc.indVisualizacion>
                            <td><a href="/proceso/instancia?pin=${doc.pinId}">${doc.asunto!""}</a></td>
                        <#else>
                            <td>${doc.asunto!""}</td>
                        </#if>
                        <td>${doc.radicado!""}</td>
                        <td>${doc.clasificacion!""}</td>
                        <td>
                            <#if doc.indJefeDependencia && !expediente.indCerrado>
                            <a title="Desvincular documento." onclick="desvinculaDocumento(${expediente.expId},'${doc.docId}')" href="#">
                                <img class="card-img-top" src="/img/x-circle.svg" alt="">
                            </a>
                            </#if>
                        </td>
                 </tr>
            </#list>
        </tbody>
    </table>
        
        <#if totalPages gt 0>
            <@printBar url="/expediente/listarDocumentos" params={"expId":expId} metodo="get"/>
        </#if>
  </#if>
</div>


<!-- Modal agregar documento -->
<div class="modal fade bd-example-modal-lg" id="agregarDocumento" tabindex="-1" role="dialog" aria-labelledby="exampleModalLongTitle" aria-hidden="true">
    <div class="modal-dialog modal-lg" role="document">
      <div class="modal-content">
        <div class="div-loader"><div class="web-loader"><div></div><div></div><div></div><div></div></div></div>
        <div class="modal-header">
          <h5 class="modal-title" id="exampleModalLongTitle">Agregar Documento</h5>
          <button type="button" class="close" data-dismiss="modal" aria-label="Close">
            <span aria-hidden="true">&times;</span>
          </button>
        </div>
        <div class="modal-body">
           <div class="input-group">
                <input type="text" id="destinoDocumento_visible" name="destinoDocumento_visible" class="form-control" value="" disabled />
                <div class="input-group-btn">
                    <button type="button" class="btn btn-primary" onclick="openFinderWindow()">Buscar</button>
                </div>
                <script src="/js/app/buscar-documento.js"></script>
            </div>
            <input type="hidden" id="destinoDocumento" name="destinoDocumento" value="" />
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
          <button type="button" class="btn btn-primary" onclick="agregarDocumentoExpediente(${expediente.expId})">Agregar</button>
        </div>
      </div>
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
<#include "bandeja-footer.ftl">
