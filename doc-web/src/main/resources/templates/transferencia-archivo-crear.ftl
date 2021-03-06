<#setting number_format="computer">

<#assign pageTitle = "TRANSFERENCIA DE ARCHIVOS" />
<#assign deferredJS = "" />

<#import "spring.ftl" as spring />
<#include "header.ftl" />
<#include "lib/transferencia-archivo_functions.ftl" />

<div class="container-fluid">
    <h4>${pageTitle}</h4>

    <div class="container-fluid">
        <form method="POST">

            <div class="form-group row">
                <label for="origenUsuario" class="col-sm-2 col-form-label text-xs-right">Usuario Origen</label>
                <div class="col-sm-10">
                    <input type="text" name="origenUsuario_visible" class="form-control" value="${getUsuarioDescripcion(origenUsuario)}" disabled />
                    <input type="hidden" name="origenUsuario" value="${origenUsuario.id}" />
                    </div>
                </div>
            
            <#--
                2018-03-12 edison.gonzalez@controltechcg.com Issue #151 (SICDI-Controltech) 
                feature-151: Se agrega el campo del cargo del usuario origen.
            -->
            <div class="form-group row">
                <label for="cargoOrigen" class="col-sm-2 col-form-label text-xs-right">Cargo</label>
                <div class="col-sm-10">
                    <select class="form-control" id="cargoOrigen" name="cargoOrigen">
                        <#if cargosXusuario??>   
                            <#list cargosXusuario as cla>
                            <#if cla.id?string == (cargoOrigen!"")?string >
                                <option value="${cla.id}" selected="selected">${cla.nombre}</option>
                            <#else>
                                <option value="${cla.id}">${cla.nombre}</option>
                            </#if>
                            </#list>
                        </#if>
                    </select>
                </div>
            </div>

<!--            Busqueda con número de documento -->
<!--            <div class="form-group row">
                <label for="destinoUsuario" class="col-sm-2 col-form-label text-xs-right">Usuario Destino</label>
                <div class="col-sm-10">
                    <div class="input-group">
                        <input type="text" id="destinoUsuario_buscar" name="destinoUsuario_buscar" class="form-control" value="<#if destinoUsuario??>${destinoUsuario.documento}</#if>" placeholder="Ingrese el documento de identidad del usuario a buscar..." />
                        <div class="input-group-btn">
                            <button type="button" class="btn btn-primary" onclick="buscarUsuarioActivo()">Buscar</button>
                            </div> 
                        </div> 
                    <input type="text" id="destinoUsuario_visible" name="destinoUsuario_visible" class="form-control" value="<#if destinoUsuario??>${getUsuarioDescripcion(destinoUsuario)}</#if>" disabled />
                    </div>
                <input type="hidden" id="destinoUsuario" name="destinoUsuario" value="<#if destinoUsuario??>${destinoUsuario.id}</#if>" />
                </div>            -->

<!--            Búsqueda con finder -->
            <div class="form-group row">
                <label for="destinoUsuario" class="col-sm-2 col-form-label text-xs-right">Usuario Destino</label>
                <div class="col-sm-10">
                    <div class="input-group">
                        <input type="text" id="destinoUsuario_visible" name="destinoUsuario_visible" class="form-control" value="<#if destinoUsuario??>${getUsuarioDescripcion(destinoUsuario)}</#if>" disabled />
                        <div class="input-group-btn">
                            <button type="button" class="btn btn-primary" onclick="openFinderWindow()">Buscar</button>
                            </div> 
                        </div> 
                    </div>
                <input type="hidden" id="destinoUsuario" name="destinoUsuario" value="<#if destinoUsuario??>${destinoUsuario.id}</#if>" />
                </div> 
            
            <#--
                2018-03-12 edison.gonzalez@controltechcg.com Issue #151 (SICDI-Controltech) 
                feature-151: Se agrega el campo del cargo del usuario destino.
            -->
            <div class="form-group row" id="divCargoDestino">
                <label for="cargoDestino" class="col-sm-2 col-form-label text-xs-right">Cargo</label>
                <div class="col-sm-10">
                    <select class="form-control" id="cargoDestino" name="cargoDestino">
                        <#if cargosXusuarioDestino??>   
                            <#list cargosXusuarioDestino as cla>
                                <#if cla.id?string == (cargoDestino!"")?string >
                                    <option value="${cla.id}" selected="selected">${cla.nombre}</option>
                                <#else>
                                    <option value="${cla.id}">${cla.nombre}</option>
                                </#if>
                            </#list>
                        </#if>
                    </select>
                </div>
            </div>

            <#assign tipoTransferenciaTotal = !tipoTransferencia?? || transferenciasRecibidas?size == 0 || tipoTransferencia == "T" />

            <div class="form-group row">
                <label for="tipoTransferencia" class="col-sm-2 col-form-label text-xs-right">Tipo Transferencia</label>
                <div class="col-sm-10">
                    <input type="radio" name="tipoTransferencia" value="T" <#if  tipoTransferenciaTotal> checked="checked" </#if> >${getTipoDescripcion("T")} (Núm. Documentos: ${numArchivosRegistrosTotal})<br>
                    <input type="radio" name="tipoTransferencia" value="P" <#if !tipoTransferenciaTotal> checked="checked" </#if> 
                        <#if transferenciasRecibidas?size == 0> disabled </#if> >${getTipoDescripcion("P")}<br>
                    </div>    
                </div>

            <#if transferenciasRecibidas?size == 0 >
            <h6><span class="label label-default font-weight-normal">No tiene transferencias previas recibidas.</span></h6>
            <#else>
            <h5>
                <span class="label label-info font-weight-normal">Transferencias previas recibidas:<strong>${transferenciasRecibidas?size}</strong></span>
                </h5>
            <div>
                <table class="table table-striped">
                    <thead>
                        <tr>
                            <th>Seleccionar</th>
                            <th>Fecha</th>
                            <th>Usuario Origen</th> 
                            <th>Clasificación Original</th> 
                            <th>Núm. Documentos</th>
                            </tr>
                        </thead>
                    <tbody>
                        <#list transferenciasRecibidas as transferenciasRecibida >
                        <tr>
                            <th><input type="radio" name="transferenciaAnterior" value="${transferenciasRecibida.id}" 
                                <#if transferenciaAnterior?? && transferenciasRecibida.id == transferenciaAnterior.id>checked="checked"</#if> /></th>
                            <td>${transferenciasRecibida.fechaAprobacion?string('yyyy-MM-dd hh:mm:ss a')}</td>
                            <#--
                                2017-11-10 edison.gonzalez@controltechcg.com Issue #131 (SICDI-Controltech) 
                                feature-131: Cambio en la entidad usuario, se coloca llave foranea el grado.
                            -->
                            <td>${transferenciasRecibida.origenUsuario.usuGrado.id + " " + transferenciasRecibida.origenUsuario.nombre}</td>
                            <td>${transferenciasRecibida.origenClasificacion.nombre}</td>
                            <td>${transferenciasRecibida.numeroDocumentos}</td>
                            </tr>
                        </#list>
                        </tbody>
                    </table>
                </div>
            </#if>


            <div class="text-center">
                <button type="submit" class="btn btn-success btn-lg">Aceptar</button>
                </div>

            </form>
        </div>
    </div>

    <script src="/js/app/buscar-usuario.js"></script>

<#include "footer.ftl" />
