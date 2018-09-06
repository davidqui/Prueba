<#setting number_format="computer">
<#assign pageTitle = "Parámetros de busqueda" />

<#assign deferredJS = "" />

<#import "spring.ftl" as spring />
<#include "header.ftl" />
<#include "consulta-parametros-util.ftl"/>
<#include "gen-arbol-dependencias.ftl">

<div class="container-fluid">
    <h4>Consulta de documentos</h4>
    <div class="container-fluid">
        <form method="POST" action="/consulta/parametros">

        <#--
            2017-02-13 jgarcia@controltechcg.com #78: Alinear el formulario de la búsqueda avanzada.
            2017-02-13 jgarcia@controltechcg.com #142: Reordenamiento de los campos del formulario para que no se crucen entre ellos.
            2017-10-27 edison.gonzalez@controltechcg.com #136: Ajuste visual y paginación de los resultados.
        -->

            <div style="border-radius: 10px; margin-bottom: 10px; padding: 10px;">
                <fieldset>
                    <legend>Parámetros de filtro</legend>

                    <div class="form-group row">
                        <label for="fechaInicio"  class="col-sm-1 col-form-label text-xs-right">Fecha Inicial</label>
                        <div class="form-inline col-sm-2 input-group">
                            <input type="text" id="fechaInicio" name="fechaInicio" class="form-control datepicker" value="${fechaInicio}"/>
                            </div>
                        <label for="fechaFin" class="col-sm-1 col-form-label text-xs-right">Fecha Final</label>
                        <div class="form-inline col-sm-2 input-group">
                            <input type="text" name="fechaFin" id="fechaFin" class="form-control datepicker" value="${fechaFin}"/>
                            </div>
                        <label for="asignado" class="col-sm-1 col-form-label text-xs-right">Enviado por</label>
                        <div class="col-sm-2">
                            <input type="text" name="asignado" class="form-control" value="${asignado}"/>
                            </div>
                        <label for="asunto" class="col-sm-1 col-form-label text-xs-right">Asunto</label>
                        <div class="col-sm-2">
                            <input type="text" name="asunto" class="form-control" value="${asunto}"/>
                            </div>
                        </div>
                    <div class="form-group row">
                        <label for="radicado" class="col-sm-1 col-form-label text-xs-right">Radicado</label>
                        <div class="col-sm-2">
                            <input type="text" name="radicado" class="form-control" value="${radicado}"/>
                            </div>

                        <label for="destinatario" class="col-sm-1 col-form-label text-xs-right">Destinatario</label>
                        <div class="col-sm-2">
                            <input type="text" name="destinatario" class="form-control" value="${destinatario}"/>
                            </div>

                        <label for="clasificacion" class="col-sm-1 col-form-label text-xs-right">Clasificación</label>
                        <input type="hidden" name="clasificacion" id="clasificacion" value="${clasificacion}"/>
                        <input type="hidden" name="clasificacionNombre" id="clasificacionNombre" value="${clasificacionNombre}"/>
                        <div class="col-sm-2">
                            <select class="form-control" id="clasificacionSelect" name="clasificacionSelect">
                        <#if clasificaciones??>
                                <option value=""></option>
                            <#list clasificaciones as cla>
                                <#if clasificacion?has_content && clasificacion == cla.id>
                                <option value="${clasificacion}" selected="selected">${clasificacionNombre}</option>
                                <#else> 
                                <option value="${cla.id}">${cla.nombre}</option>
                                </#if>
                            </#list>
                        </#if>
                                </select>
                            </div>
                        
                        <label for="tipoProceso" class="col-sm-1 col-form-label text-xs-right">Tipo de proceso</label>
                        <div class="col-sm-2">
                            <select class="form-control" id="tipoProceso" name="tipoProceso">
                                <#if tiposProcesos??>
                                        <option value=""></option>
                                    <#list tiposProcesos as tipoPros>
                                        <#if tipoProceso?has_content && tipoProceso == tipoPros.id>
                                        <option value="${tipoPros.id}" selected="selected">${tipoPros.nombre}</option>
                                        <#else>
                                        <option value="${tipoPros.id}">${tipoPros.nombre}</option>
                                        </#if>
                                    </#list>
                                </#if>
                            </select>
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-group col">
                            <#--
                                2018-05-08 jgarcia@controltechcg.com Issue #160 (SICDI-Controltech) feature-160:
                                Campo para búsqueda por Firma UUID.

                                2018-05-09 jgarcia@controltechcg.com Issue #160 (SICDI-Controltech) feature-160:
                                Cambio de etiqueta a Firma Digital, indicado en correo "TEXTO PDF" (Wed, 09 May 2018 13:55:22 -0500)

                            -->
                            <#if puedeBuscarXDocFirmaEnvioUUID >
                            <label for="firmaUUID" class="col-sm-1 col-form-label text-xs-right">Firma Digital</label>
                            <div class="col-sm-2">
                                <input type="text" name="firmaUUID" class="form-control" value="${firmaUUID}"/>
                                </div>
                            </#if>

                        </div>
                        <div class="form-group col">
                            <#--
                                2018-05-08 jgarcia@controltechcg.com Issue #160 (SICDI-Controltech) feature-160:
                                Campo para búsqueda por Firma UUID.
                            -->
                            <#if permisoAdministradorArchivo>
                                <label for="buscarTodo" class="col-sm-1 col-form-label text-xs-right">Buscar en todo el sistema</label>
                                <div class="col-sm-2">
                                    <input type="checkbox" name="buscarTodo" id="buscarTodo" class="form-check-input" style="width: 35px; height: 35px;"/>
                                </div>
                            </#if>
                        </div>
                    </div>
                    <div class="form-group row" style="margin-top:20px;">
                        <label for="origen" class="col-sm-1 col-form-label text-xs-right">Dependencia origen</label>
                        <input type="hidden" name="dependenciaOrigen" id="dependenciaOrigen" value="${dependenciaOrigen}"/>
                        <input type="hidden" name="dependenciaOrigenDescripcion" id="dependenciaOrigenDescripcion" value="${dependenciaOrigenDescripcion}"/>
                        <div class="col-sm-11">
                            <div class="input-group">
                                <div class="form-control" id="dependenciaOrigenNombre">Por favor seleccione una dependencia origen...</div>
                                <span class="input-group-btn">
                                    <button class="btn btn-primary" type="button" data-toggle="modal" data-target="#dependenciaOrigenModal">
                                        <span class="hidden-md-down">Seleccionar</span><span class="hidden-lg-up">S</span>
                                        </button>
                                    <button class="btn" type="button" id="dependenciaOrigenLimpiar">
                                        <span>Limpiar</span>
                                        </button>
                                    </span>
                                </div>
                            </div>
                        </div>
                    <div class="form-group row">
                        <!--2017-02-14 jgarcia@controltechcg.com Issue #105: Se cambia de posición el campo de búsqueda "Dependencia destino", para que la selección de las 
                                fechas se vea completa. -->
                        <!-- 2017-02-13 jgarcia@controltechcg.com Issue #77 »»»»»»»»»» -->
                        <label for="destinatario" class="col-sm-1 col-form-label text-xs-right">Dependencia destino</label>
                        <input type="hidden" name="dependenciaDestino" id="dependenciaDestino" value="${dependenciaDestino}"/>
                        <input type="hidden" name="dependenciaDestinoDescripcion" id="dependenciaDestinoDescripcion" value="${dependenciaDestinoDescripcion}"/>
                        <div class="col-sm-11">
                            <div class="input-group">
                                <div class="form-control" id="dependenciaDestinoNombre">Por favor seleccione una dependencia destino...</div>
                                <span class="input-group-btn">
                                    <button class="btn btn-primary" type="button" data-toggle="modal" data-target="#dependenciaDestinoModal">
                                        <span class="hidden-md-down">Seleccionar</span><span class="hidden-lg-up">S</span>
                                        </button>
                                    <button class="btn" type="button" id="dependenciaDestinoLimpiar" >
                                        <span >Limpiar</span>
                                        </button>

                                </div>
                            </span>
                            </div>
                        </div>
                </div>
            <div class="text-center">
                <button type="submit" class="btn btn-success btn-lg" onclick="loading(event);">Buscar</button>
                </div>
            </fieldset>
        </div>

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