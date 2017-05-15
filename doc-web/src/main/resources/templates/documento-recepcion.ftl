<#assign pageTitle = documento.asunto!"Documento" />

<#import "spring.ftl" as spring />

<#include "bandeja-header.ftl" />

<div class="col-md-12">
    <#assign transiciones = instancia.transiciones() />
    <#if transiciones??>
        <div class="btn-group text-right m-y " role="group" aria-label="Transiciones">
            <#list transiciones as x>
                <a href="${x.replace(instancia)}" class="btn btn-primary">${x.nombre}</a>
            </#list>
        </div>
        <div class="clearfix"></div>
    </#if>
    <!--


        Sticker

    -->
    <#if documento.sticker??>
        <iframe src="/ofs/viewer?file=/ofs/download/${documento.sticker}" width="100%" height="300px"></iframe>
    </#if>
    <form action="/documento/recepcion?pin=${instancia.id}" method="POST">
        <@spring.bind "documento.id" />
        <input type="hidden" name="${spring.status.expression}" id="${spring.status.expression}" value="${(documento.id)!""}" />
        <!--


            TRD
        -->
        <fieldset class="form-group">
            <label for="trd">Tabla de retención documental (*)</label>
            <@spring.bind "documento.trd" />
            <input type="hidden" name="${spring.status.expression}" id="trd" value="${(documento.trd.id)!""}" />
            <div class="row">
              <div class="col-lg-12">
                <div class="input-group">
                  <div class="form-control" id="trdNombre">${(documento.trd.nombre)!"Por favor seleccione una subserie..."}</div>
                  <span class="input-group-btn">
                    <button class="btn btn-primary" type="button" data-toggle="modal" data-target="#trdModal">
                        Seleccionar
                    </button>
                  </span>
                </div>
              </div>
            </div>
            <div class="error">
                <@spring.showErrors "<br>"/>
            </div>
        </fieldset>
        <!-- trdModal -->
        <div class="modal fade" id="trdModal" tabindex="-1" role="dialog" aria-labelledby="trdModalLabel" aria-hidden="true">
          <div class="modal-dialog modal-lg" role="document">
            <div class="modal-content">
              <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar">
                  <span aria-hidden="true">&times;</span>
                  <span class="sr-only">Cerrar</span>
                </button>
                <h4 class="modal-title" id="myModalLabel">Selección de subserie</h4>
              </div>
              <div class="modal-body">
              </div>
            </div>
          </div>
        </div>
        <#assign deferredJS>
            <script type="text/javascript">
            <!--
                $('#trdModal').on('show.bs.modal', function(e) {
                    $("#trdModal .modal-body").empty();
                    $.getJSON("/trd/series.json", function(series) {
                        var divTagList = [];
                        $.each(series, function(i, item){
                            divTagList.push('<div><a href="#" id="serie' + item.codigo + '" class="mark-serie">' + item.nombre + '</a></div>');
                        });
                        $("<div/>", { html: divTagList.join("") }).appendTo("#trdModal .modal-body");

                        $("#trdModal .modal-body .mark-serie").click(function(e) {
                            var id = $(this).attr("id").substring(5);
                            var nombre = $(this).text();

                            $("#trdModal .modal-body").empty();

                            $.getJSON("/trd/subseries.json?serieId=" + id, function(subseries) {
                                var subseriesList = [];
                                $.each(subseries, function(i, item){
                                    subseriesList.push('<div><a href="#" id="serie' + item.id + '" class="mark-serie">' + item.nombre + '</a></div>');
                                });

                                $("<h5/>", { html: nombre }).appendTo("#trdModal .modal-body");
                                $("<div/>", { html: subseriesList.join("") }).appendTo("#trdModal .modal-body");

                                $("#trdModal .modal-body .mark-serie").click(function(e) {
                                    var id = $(this).attr("id").substring(5);
                                    var nombre = $(this).text();
                                    
                                    $("#trd").val(id);
                                    $("#trdNombre").text(nombre);
                                    
                                    $("#trdModal").modal('hide');
                                });
                            });
                        });
                    });
                });
            -->
            </script>
        </#assign>
        <!--


            Destinatario
        -->
        <fieldset class="form-group">
            <label for="destinatario">Destinatario (*)</label>
        <#if documento.soloLectura >
            <div class="form-control">${(documento.dependencia.nombre)!""}</div>
        <#else>
            <@spring.bind "documento.destinatario" />
            <select class="form-control" id="destinatario" name="${spring.status.expression}">
                <#if dependencias??>
                    <option value=""></option>
                    <#list dependencias as dep>
                    <#if spring.status.value?? && dep == spring.status.value >
                        <option value="${dep.id}" selected="selected">${dep.nombre}</option>
                    <#else>
                        <option value="${dep.id}">${dep.nombre}</option>
                    </#if>
                    </#list>
                </#if>
            </select>
            <div class="error">
                <@spring.showErrors "<br>"/>
            </div>
        </#if>
        </fieldset>
        <!--


            Asunto
        -->
        <fieldset class="form-group">
            <label for="asunto">Asunto (*)</label>
        <#if documento.soloLectura >
            <div class="form-control">${documento.asunto}</div>
        <#else>
            <@spring.formInput "documento.asunto" 'class="form-control"' />
            <small class="text-muted">El asunto del documento es un texto corto que describe el documento, preferiblemente de manera única.</small>
            <div class="error">
                <@spring.showErrors "<br>"/>
            </div>
        </#if>
        </fieldset>
        <!--


            Remintente
        -->
        <fieldset class="form-group">
            <label for="remitente">Remitente (*)</label>
        <#if documento.soloLectura >
            <div class="form-control">${documento.remitente!""}</div>
        <#else>
            <@spring.formInput "documento.remitente" 'class="form-control"' />
            <small class="text-muted">El remitente del documento es la persona, dependencia o institución que envía el documento</small>
            <div class="error">
                <@spring.showErrors "<br>"/>
            </div>
        </#if>
        </fieldset>
        <!--


            Número de oficio y fecha de oficio
        -->
        <div class="row">
            <div class="col-md-6">
                <fieldset class="form-group">
                    <label for="numeroOficio">Número de oficio (*)</label>
                <#if documento.soloLectura >
                    <div class="form-control">${documento.numeroOficio!""}</div>
                <#else>
                    <@spring.formInput "documento.numeroOficio" 'class="form-control"' />
                    <div class="error">
                        <@spring.showErrors "<br>"/>
                    </div>
                </#if>
                </fieldset>
            </div>
            <div class="col-md-6">
                <fieldset class="form-group">
                    <label for="fechaOficio">Fecha del oficio (*)</label>
                <#if documento.soloLectura >
                    <div class="form-control"><#if documento.fechaOficio?? >${yyyymmdd.format(documento.fechaOficio)}</#if></div>
                <#else>
                    <@spring.bind "documento.fechaOficio" />
                    <input class="form-control datepicker" id="${spring.status.expression}" name="${spring.status.expression}" value="<#if documento.fechaOficio??>${yyyymmdd.format(documento.fechaOficio)}</#if>"/>
                    <div class="error">
                        <@spring.showErrors "<br>"/>
                    </div>
                </#if>
                </fieldset>
            </div>
        </div>
        <!--


            Número de folios y plazo
        -->
        <div class="row">
            <div class="col-md-6">
                <fieldset class="form-group">
                    <label for="numeroFolios">Número de folios (*)</label>
                <#if documento.soloLectura >
                    <div class="form-control">${documento.numeroFolios!""}</div>
                <#else>
                    <@spring.formInput "documento.numeroFolios" 'class="form-control"' />
                    <div class="error">
                        <@spring.showErrors "<br>"/>
                    </div>
                </#if>
                </fieldset>
            </div>
            <div class="col-md-6">
                <fieldset class="form-group">
                    <label for="plazo">Plazo (*)</label>
                <#if documento.soloLectura >
                    <div class="form-control"><#if documento.plazo?? >${yyyymmdd.format(documento.plazo)}</#if></div>
                <#else>
                    <@spring.bind "documento.plazo" />
                    <input type="text" class="form-control datepicker" id="${spring.status.expression}" name="${spring.status.expression}"  value="<#if documento.plazo??>${yyyymmdd.format(documento.plazo)}</#if>" />
                    <div class="error">
                        <@spring.showErrors "<br>"/>
                    </div>
                </#if>
                </fieldset>
            </div>
        </div>
        <!--


            Clasificación
        -->
        <fieldset class="form-group">
            <label for="clasificacion">Nivel de clasificación (*)</label>
            <#if documento.soloLectura >
                <div class="form-control">${(documento.clasificacion.nombre)!""}</div>
            <#else>
                <@spring.bind "documento.clasificacion" />
                <select class="form-control" id="${spring.status.expression}" name="${spring.status.expression}">
                    <#if clasificaciones??>
                        <option value=""></option>
                        <#list clasificaciones as cla>
                        <#if cla.id?string == ((documento.clasificacion.id)!"")?string >
                            <option value="${cla.id}" selected="selected">${cla.nombre}</option>
                        <#else>
                            <option value="${cla.id}">${cla.nombre}</option>
                        </#if>
                        </#list>
                    </#if>
                </select>
                <div class="error">
                    <@spring.showErrors "<br>"/>
                </div>
            </#if>
        </fieldset>

        <button type="submit" class="btn btn-primary">Guardar</button>
    </form>
</div>

<#include "bandeja-footer.ftl" />