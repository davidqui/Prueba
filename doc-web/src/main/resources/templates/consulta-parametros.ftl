<#setting number_format="computer">
<#assign pageTitle = "Parámetros de busqueda" />

<#assign deferredJS = "" />

<#import "spring.ftl" as spring />
<#include "header.ftl" />
<#include "consulta-parametros-util.ftl"/>
<#include "gen-arbol-dependencias.ftl">
<link href='/css/consulta-parametros.css' rel='stylesheet' type='text/css'>

<div class="container-fluid">

    <h4>Consulta de documentos ${(destinoExterno)!""}</h4>
    <div class="container-fluid">
        <div id="container-2" class="form-group row fade-in container-form" style="position: relative; margin-top: 70px;">
            <div style="position: absolute; top: -36px;">
                <div class="btn-group btn-toggle" style="border-top-left-radius: 30px; border-top-right-radius: 30px; overflow: hidden;"> 
                    <a id="toggle-1" class="btn btn-default" onclick="selected(0)" style="color: black; background-color:<#if tipoBusqueda == 0>#f0f0f0<#else>#dddddd</#if>">Interno</a>
                    <a id="toggle-2" class="btn btn-default" onclick="selected(1)" style="color: black; background-color:<#if tipoBusqueda == 0>#dddddd<#else>#f0f0f0</#if>">Externo</a>
                </div>
            </div>
            <form method="POST" action="/consulta/parametros">
                <div class="form-group row">
                    <label for="radicado" class="col-sm-1 col-form-label text-xs-right">Radicado</label>
                    <div class="col-sm-2">
                        <input type="text" name="radicado" class="form-control" value="${radicado}"/>
                    </div>
                    <label for="asunto" class="col-sm-1 col-form-label text-xs-right">Asunto</label>
                    <div class="col-sm-2">
                        <input type="text" name="asunto" class="form-control" value="${asunto}"/>
                    </div>

                    <label for="fechaInicio"  class="col-sm-1 col-form-label text-xs-right">Fecha Inicial</label>
                    <div class="form-inline col-sm-2 input-group">
                        <input type="text" id="fechaInicio" name="fechaInicio" class="form-control datepicker" value="${fechaInicio}"/>
                    </div>
                    <label for="fechaFin" class="col-sm-1 col-form-label text-xs-right">Fecha Final</label>
                    <div class="form-inline col-sm-2 input-group">
                        <input type="text" name="fechaFin" id="fechaFin" class="form-control datepicker" value="${fechaFin}"/>
                    </div>
                </div>
                <input type="hidden" name="tipoBusqueda" id="tipoBusqueda" value="${(busqueda)!""}">
                <div class="form-group row">
                    <label for="origen" class="col-sm-1 col-form-label text-xs-right">Dependencia origen</label>
                    <input type="hidden" name="dependenciaOrigen" id="dependenciaOrigen" value="${dependenciaOrigen}"/>
                    <input type="hidden" name="dependenciaOrigenDescripcion" id="dependenciaOrigenDescripcion" value="${dependenciaOrigenDescripcion}"/>
                    <div class="col-sm-5">
                        <div class="input-group">
                            <div class="form-control" id="dependenciaOrigenNombre">Por favor seleccione una dependencia origen...</div>
                            <span class="input-group-btn">
                                <button class="btn btn-primary" type="button" data-toggle="modal" data-target="#dependenciaOrigenModal">
                                    <span class="hidden-md-down">Seleccionar</span><span class="hidden-lg-up"><img class="card-img-top" src="/img/search.svg" alt=""></span>
                                    </button>
                                <button class="btn" type="button" id="dependenciaOrigenLimpiar">
                                    <span>Limpiar</span>
                                    </button>
                                </span>
                            </div>
                        </div>
                    <div id="con-externo" <#if tipoBusqueda == 0> style="display:none;"</#if>>
                        <label for="destinatario" class="col-sm-1 col-form-label text-xs-right">Destino externo</label>
                        <div class="col-sm-5">
                            <div class="input-group" style="width: 100%;">
                                <select class="selectpicker" id="destinoExterno" name="destinoExterno" data-live-search="true">
                                    <#if destinosExternos??>
                                <option value=""></option>
                                        <#list destinosExternos as dte>
                                        <#if dte.id?string == ((destinoExterno)!"")?string >
                                <option value="${dte.id}" selected="selected">${dte.nombre}</option>
                                        <#else>
                                <option value="${dte.id}">${dte.nombre}</option>
                                        </#if>
                                        </#list>
                                    </#if>
                                </select>
                            </div>
                        </div>
                    </div>

                    <div id="con-interno" <#if tipoBusqueda == 1> style="display:none;"</#if>>
                        <label for="destinatario" class="col-sm-1 col-form-label text-xs-right">Dependencia destino</label>
                        <input type="hidden" name="dependenciaDestino" id="dependenciaDestino" value="${dependenciaDestino}"/>
                        <input type="hidden" name="dependenciaDestinoDescripcion" id="dependenciaDestinoDescripcion" value="${dependenciaDestinoDescripcion}"/>
                        <div class="col-sm-5">
                            <div class="input-group">
                                <div class="form-control" id="dependenciaDestinoNombre">Por favor seleccione una dependencia destino...</div>
                                <span class="input-group-btn">
                                    <button class="btn btn-primary" type="button" data-toggle="modal" data-target="#dependenciaDestinoModal">
                                        <span class="hidden-md-down">Seleccionar</span><span class="hidden-lg-up"><img class="card-img-top" src="/img/search.svg" alt=""></span>
                                    </button>
                                    <button class="btn" type="button" id="dependenciaDestinoLimpiar" >
                                        <span >Limpiar</span>
                                    </button>
                                </div>
                            </span>
                            </div>
                        </div>
                    </div>
                <#if permisoAdministradorArchivo>
                <div class="form-group row">    
                <label for="buscar" class="col-sm-1 col-form-label text-xs-right">Buscar en todo SICDI</label>
                    <div class="form-inline col-sm-2 input-group" style="
                         margin-left: auto;
                         margin-right: auto;
                         display:flex;
                         display: flex;
                         font-size: 25px;
                         color: #cccccc;">
                        <span id="buscartodo-no" style="
                            margin-right:  10px;
                        ">NO</span>
                        <label class="switchBtn">
                            <input type="checkbox" name="buscarTodo" id="buscarTodo" onchange="changeToggleBuscarTodos()" <#if buscarTodo?? && buscarTodo>checked="checked"</#if>>
                            <div class="slide round"></div>
                        </label>
                        <span id="buscartodo-si" style="
                            margin-left:  10px;
                        ">SI</span>
                    </div>
                </div>
                </#if>
                <div class="form-group row">
                    <button type="submit" class="btn btn-primary btn-lg" onclick="loading(event);">Buscar</button>
                </div>
                </div>
            </form>
    </form>

<!-- dependenciaDestinoModal -->            
    <div class="modal fade" id="dependenciaDestinoModal" tabindex="-1" role="dialog" aria-labelledby="dependenciaDestinoModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar">
                        <span aria-hidden="true">&times;</span>
                        <span class="sr-only">Cerrar</span>
                        </button>
                    <h4 class="modal-title" id="dependenciaDestinoModalLabel">Selección de dependencia destino</h4>
                    </div>
                <div class="modal-body">
                    <div class="card">
                        <div class="card-block">
                            <div class="row">
                                <div class="col-md-7">
                                    <div id="arbol_list_dependenciasj">
                                            <#if did??>
                                                <@listDependencias dependencias=dependencias selected=did href=false/>
                                            <#else>
                                                <@listDependencias dependencias=dependencias href=false/>
                                            </#if>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        <div class="clearfix"></div>
                        </div>
                    <br /><br />
                    </div>
                </div>
            </div>
        </div>
        <!-- dependenciaOrigenModal -->            
    <div class="modal fade" id="dependenciaOrigenModal" tabindex="-1" role="dialog" aria-labelledby="dependenciaOrigenModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar">
                        <span aria-hidden="true">&times;</span>
                        <span class="sr-only">Cerrar</span>
                        </button>
                    <h4 class="modal-title" id="dependenciaOrigenModalLabel">Selección de dependencia origen</h4>
                    </div>
                <div class="modal-body">
                    <div class="card">
                        <div class="card-block">
                            <div class="row">
                                <div class="col-md-7">
                                    <div id="arbol_list_dependenciasjOrigen">
                                            <#if did??>
                                                <@listDependencias dependencias=dependencias selected=did href=false/>
                                            <#else>
                                                <@listDependencias dependencias=dependencias href=false/>
                                            </#if>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        <div class="clearfix"></div>
                        </div>
                    <br /><br />
                    </div>
                </div>
            </div>
        </div>
    <#assign deferredJSDepOrigen>
    <script src="/js/jstree.min.js"></script>
    <script src="/js/app/gen-arbol.js"></script>

    <script type="text/javascript">
        validarArbol("#arbol_list_dependenciasjOrigen",false);
    </script>
    <script type="text/javascript">
        validarArbol("#arbol_list_dependenciasj",false);
    </script>

    </#assign>
    <#assign deferredJS = deferredJS + " " + deferredJSDepOrigen>
    </form>

<br><br>
</div>
<#include "consulta.ftl" />
</div>
<#include "footer.ftl" />
<script src="/js/app/consulta-parametros.js"></script>
<script type="text/javascript">
    initParameters("${dependenciaOrigenDescripcion}","${dependenciaDestinoDescripcion}");
</script>