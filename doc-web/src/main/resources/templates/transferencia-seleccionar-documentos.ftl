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
</style>
<div class="container-fluid">
    <h4>${pageTitle}</h4>
<div id="accordion">
  <#assign hasAllSelected = true />            
  <button id="select-all-documents" name="select-all-documents" type="button" class="btn btn-default btn-sm slAllTrd" onclick="return selectAllDocuments(this.form, this);">
    <#if hasAllSelected >
        ${removeAllText}
    <#else>
        ${selectAllText}
    </#if>
  </button
  <#assign i = 0 >
  <form method="POST">
    <#list trds as trd>
      <#if trd.subSeries??>
          <div class="card">
            <div class="card-header" id="headingOne">
              <h5 class="mb-0">
                <input type="checkbox">
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
                        <div class="card">
                          <div class="card-header" id="headingOne">
                            <h5 class="mb-0">
                              <input type="checkbox" id="">
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
                                          <th>Fecha Creación</th>
                                          <th>Cargo</th>
                                      </tr>
                                  </thead>
                                  <tbody>
                                      <#list strd.documentosDependencia as doc>
                                          <tr>
                                              <td><input type="checkbox" name="documentos"></td>
                                              <td nowrap>${doc.radicado!"&lt;Sin radicado&gt;"}</td>
                                              <td>${doc.asunto!"&lt;Sin asunto&gt;"}</td>
                                              <td nowrap>${(doc.cuando?string('yyyy-MM-dd hh:mm aa'))!""}</td>
                                              <td nowrap>${(doc.cargoIdElabora.carNombre)!""}</td>
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
  </form>
<div>
<script src="/js/app/transferencia-seleccionar-documento.js"></script>
<#include "footer.ftl" />
