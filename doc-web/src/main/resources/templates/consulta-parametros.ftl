<#assign pageTitle = "Parámetros de busqueda" />

<#assign deferredJS = "" />

<#import "spring.ftl" as spring />
<#include "header.ftl" />
<#include "consulta-parametros-util.ftl"/>

<div class="container-fluid">
    <h4>Consulta de documentos</h4>
    <div class="container-fluid">
        <form method="POST" action="/consulta/parametros">

    <!--2017-02-13 jgarcia@controltechcg.com #78: Alinear el formulario de la búsqueda avanzada -->
    <!--2017-02-13 jgarcia@controltechcg.com #142: Reordenamiento de los campos del formulario para que no se crucen entre ellos. -->
    <!--2017-10-27 edison.gonzalez@controltechcg.com #136: Ajuste visual y paginación de los resultados. -->

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
                        </div>
                    <div class="form-group row">
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
                                        <span class="hidden-md-down">Seleccionar</span>
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
                <button type="submit" class="btn btn-success btn-lg">Buscar</button>
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
                    </div>
                </div>
            </div>
        </div>
    <#assign deferredJSDepDestino>
    <script src="/js/app/dependencia-destino-modal-parametros.js"></script>
    <script src="/js/app/dependencia-origen-modal-parametros.js"></script>
    </#assign>
    <#assign deferredJS = deferredJS + " " + deferredJSDepDestino>
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