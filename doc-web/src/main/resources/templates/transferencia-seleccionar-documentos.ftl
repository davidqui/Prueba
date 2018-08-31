<#setting number_format="computer">

<#assign pageTitle = "Selección de documentos" />
<#assign deferredJS = "" />

<#import "spring.ftl" as spring />
<#include "header.ftl" />
<#include "lib/transferencia-archivo_functions.ftl" />
<#assign selectAllText = "Seleccionar todos" />
<#assign removeAllText = "Retirar todos" />
<style>
    .card{
        border: none;
        border-left: 1px solid #e5e5e5;
        margin: 0 !important;   
    }
    
    .card-header{
        height: 50px;
        padding: 10px;
        background-color: white;
    }
    
    .card-body{
        padding-left: 30px;
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
    
    .range-picker{
        width: 450px;
        float: right;
        font-size: 22px;
    }
    
    .range{
        display: flex;
    }
    
    @media only screen and (max-width: 600px) {
        .range-picker{
            width: 100%;
        }
    }
</style>
<div class="container-fluid" style="padding-bottom: 100px;">
    <div>
        <ol class="breadcrumb">
            <li><a href="/transferencia-archivo/listar?">Inicio</a></li>
            <li><a href="/transferencia-archivo/resumen/${transId}">Resumen</a></li>
            <li class="active">Selección de documentos</li>
            
        </ol>
    </div>
    
<h4>${pageTitle}</h4>
<div id="accordion">
  <#assign hasAllSelected = false />            
  <button id="select-all-documents" name="select-all-documents" type="button" class="btn btn-default btn-sm slAllTrd" onclick="return selectAllDocuments(this.form, this);">
    <#if hasAllSelected >
        ${removeAllText}
    <#else>
        ${selectAllText}
    </#if>
  </button>
  <div class="range-picker">
    <label for="fechaInicio"  class="col-form-label" style="font-size: 16px;">Filtrar por fecha</label>
    <div class="form-group range">
      <div class="form-inline input-group">
        <input onchange="finderDocument()" type="text" id="fechaInicio" name="fechaInicio" class="form-control datepicker" autocomplete="off" readonly/>
        <div onclick="document.getElementById(&quot;fechaInicio&quot;).value = &quot;&quot;; finderDocument()" class="input-group-addon btn">Limpiar</div>
      </div>
      <span style="padding: 0px 5px;">a</span>
      <div class="form-inline input-group">
        <input onchange="finderDocument()" type="text" id="fechafin" name="fechafin" class="form-control datepicker" autocomplete="off" readonly/>
        <div onclick="document.getElementById(&quot;fechafin&quot;).value = &quot;&quot;; finderDocument()" class="input-group-addon btn">Limpiar</div>
      </div>
    </div>
  </div>
  <input id="selected-all-documentos" name="selected-all-documentos" type="hidden" value="true" />
<input class="form-control" type="text" id="documentos-buscar" onkeyup="finderDocument()" placeholder="Buscar por nombre o número de radicación" title="Buscar" style="margin-top: 30px;">
<form method="POST" id="formDocumentos">
  <#assign i = 0 >
    <#list trds as trd>
      <#if trd.subSeries??>
          <div class="card trdPadre">
            <div class="card-header" id="headingOne">
              <h5 class="mb-0">
                <input type="checkbox" id="trd-documentos" onclick="selectdivCheckbox('collapse_${(i)!""}', this)">
                <button class="btn btn-link" data-toggle="collapse" data-target="#collapse_${(i)!""}" aria-expanded="true" aria-controls="collapse_${(i)!""}">
                  <b>${(trd.trdNombre)!""}</b>
                </button>
              </h5>
            </div>

            <div id="collapse_${(i)!""}" class="collapse show" aria-labelledby="headingOne" data-parent="#accordion">
              <div class="card-body">
                  <#assign j = 0 >
                  <#list trd.subSeries as strd>
                      <div id="accordion_${(i)!""}">
                        <div class="card subTrd">
                          <div class="card-header" id="headingOne">
                            <h5 class="mb-0">
                              <input type="checkbox" id="trd-documentos" onclick="selectdivCheckbox('collapse_sub_${(i)!""}_${(j)!""}', this)">
                              <button class="btn btn-link" data-toggle="collapse" data-target="#collapse_sub_${(i)!""}_${(j)!""}" aria-expanded="true" aria-controls="collapse_sub_${(i)!""}_${(j)!""}">
                                ${(strd.trdNombre)!""}
                              </button>
                            </h5>
                          </div>

                          <div id="collapse_sub_${(i)!""}_${(j)!""}" class="collapse show" aria-labelledby="headingOne" data-parent="#accordion_${(i)!""}">
                            <div class="card-body">
                                <table class="table">
                                  <thead>
                                      <tr>
                                          <th></th>
                                          <th>Radicado</th>
                                          <th>Asunto</th>
                                          <th>Fecha Radicación</th>
                                          <th>Cargo</th>
                                      </tr>
                                  </thead>
                                  <tbody>
                                      <#list strd.documentosDependencia as doc>
                                          <tr 
                                              name="info-doc"
                                              id="info-doc"
                                              <#if controller.hasDocumentoEnTransferencia(doc.id, documentosEnTransferencia)>
                                                class="bd-popover"
                                                style="background-color: #00000047; cursor: not-allowed;"
                                                data-toggle="popover" data-trigger="hover" data-placement="top" title="Documento en Transferencia" data-content="Este documento ya se encuentra asociado a una transferencia, desasócielo para poder agregarlo a este."
                                              </#if>
                                              >
                                              <td><input type="checkbox"
                                                         onclick="resetCheckTrd();"
                                                         value="${doc.id}"
                                                         <#if controller.hasDocumento(doc.id, documentosXTransferenciaArchivo)>checked="checked"</#if>
                                                         <#if controller.hasDocumentoEnTransferencia(doc.id, documentosEnTransferencia)>
                                                         style="border: 1px solid red;"
                                                         disabled
                                                         <#else>
                                                            name="documentos"
                                                         </#if>
                                                         ></td>
                                              <td nowrap name="radicado">${doc.documento.radicado!"&lt;Sin radicado&gt;"}</td>
                                              <td name="asunto">${doc.documento.asunto!"&lt;Sin asunto&gt;"}</td>
                                              <td name="fecha" nowrap>${(doc.documento.docFecRadicado?string('yyyy-MM-dd'))!""}</td>
                                              <td nowrap>${(doc.documento.cargoIdElabora.carNombre)!""}</td>
                                          </tr>
                                      </#list>
                                  </tbody>
                              </table>
                            </div>
                          </div>
                        </div>
                      </div>
                      <#assign j = j + 1 >
                  </#list> 
              </div>
          </div>
        </div>
        <#assign i = i + 1 >
      </#if>
    </#list>
    <div class="navbar navbar-default navbar-fixed-bottom text-xs-center hermes-bottombar">
        <button type="submit" class="btn btn-primary" onclick="loading(event);">
          Transferir <span class="span-badge" id="counterDoc">0 documentos</span>
        </button>
    </div>
  </form>
<div>
<script src="/js/app/transferencia-seleccionar-documento.js"></script>
<#include "footer.ftl" />
