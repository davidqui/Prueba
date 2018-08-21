<#assign pageTitle = "Crear Expediente"/>
<#setting number_format="computer" />
<#include "bandeja-header.ftl">
<#include "gen-arbol-trd.ftl">
<#import "spring.ftl" as spring />
<#include "util-macros.ftl" />

<script src="/js/tinymce.min.js"></script>

<div class="container-fluid" style="max-width: 900px; margin-left: inherit;">
    <#if dependencia?? && dependencia.jefe??>
        <#if nombreExpediente?? && nombreExpediente?size &gt; 0>
            <form action="/expediente/crear" method="POST" enctype="multipart/form-data" >

            <div class="tab-content" id="myTabContent">
                    <fieldset class="form-group">
                    <label for="trdIdPrincipal">Tabla de retención documental</label>
                    <input type="hidden" id="trdIdPrincipal" name="trdIdPrincipal" value="${(expediente.trd.id)!""}" />
                    <div class="row">
                        <div class="col-lg-12">
                            <div class="input-group">
                                <div class="form-control" id="trdNombre">${(expediente.trd)!"Por favor seleccione una subserie..."}</div>
                                <span class="input-group-btn">
                                    <button class="btn btn-primary" type="button" data-toggle="modal" data-target="#trdModalArbol">
                                        <span class="hidden-md-down">Seleccionar</span><span class="hidden-lg-up">S</span>
                                        </button>
                                    </span>
                                </div>
                            </div>
                        </div>
                    </fieldset>
            </div>
                <label for="expNombre">Nombre</label>
                <div class="form-group" style="display:flex;">
                    <input type="text" class="form-control" id="trdSigla" name="trdSigla" disabled style="width: 10%;"/>
                    <select class="selectpicker" id="parNombreExpediente" name="parNombreExpediente" style="width: 50%;" data-live-search="true">
                        <#list nombreExpediente as nombreEx>
                            <option value="${(nombreEx.parId)!""}" data-text="${(nombreEx.parNombre)!""}">${(nombreEx.parNombre)!""}</option>
                        </#list>
                    </select>
                    <input type="number" class="form-control" id="numberExpediente" name="numberExpediente" style="width: 10%;" placeholder="000" required max="999" min="1"/>
                    <input type="text" class="form-control" id="anoActual" name="anoActual" value="${(year)!""}" disabled style="width: 10%;"/>
                    <input type="text" class="form-control" id="opcionalNombre" name="opcionalNombre" style="width: 20%; text-transform:uppercase" placeholder="opcional"/>
                </div>
            <p style="text-transform:uppercase"><b>Nombre:</b> <span id="input-nombre-1"></span>-<span id="input-nombre-4"></span>-<span id="input-nombre-3"></span>-<span id="input-nombre-2">${(year)!""}</span><span id="input-nombre-5"></span><p>    
            <label for="tipoExp">Tipo de expediente:</label>
            <div class="btn-group" data-toggle="buttons" id="topButtonDiv" style="width: 100%;">
                <button type="button" class="btn btn-primary" id="btn-simple" style="width: 50%;" onclick="selectExpediente(1)">SIMPLE
                <input type="radio" id="radio1" name="expTipo" value="1" checked></button>
                <button type="button" class="btn btn-default" id="btn-complejo" style="width: 50%;" onclick="selectExpediente(2)">COMPLEJO
                <input type="radio" id="radio2" name="expTipo" value="2"> </button>              
            </div>
            
            <p style="margin-top:10px;">
                <b>Series documentales Simples:</b> Acuerdos, Decretos, Circulares, Resoluciones
                </br>
                <b>Series documentales Complejas:</b> Contratos, Historias Laborales, Investigaciones Disciplinarias, Procesos Jurídicos
            </p>

            <fieldset class="form-group" style="margin-top:10px;">
                <label for="expNombre">Descripción</label>
                <textarea class="form-control" rows="5" id="expDescripcion" name="expDescripcion" required>${(expediente.expDescripcion)!""}</textarea>
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
                            <h4 class="modal-title" id="myModalLabel">Selección de serie</h4>
                        </div>
                        <div class="modal-body">
                        </div>
                    </div>
                </div>
            </div>

            <div class="modal fade" id="trdModalArbol" tabindex="-1" role="dialog" aria-labelledby="trdModalArbolLabel" aria-hidden="true">
                <div class="modal-dialog modal-lg" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar">
                                <span aria-hidden="true">&times;</span>
                                <span class="sr-only">Cerrar</span>
                            </button>
                            <h4 class="modal-title" id="trdModalLabel">Selección de tabla de retención documental</h4>
                        </div>
                        <div class="modal-body">
                            <div class="card">
                                <div class="card-block">
                                    <div class="row">
                                        <div class="col-md-7">
                                            <div id="arbol_list_trd">
                                                <@listTrds trds=trds/>
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
            <div class="m-y">
                <button id="btnGuardar" type="submit" class="btn btn-primary">Guardar</button>
                <a href="/expediente/listarExpedientes" class="btn btn-secondary" onclick="loading(event);">Cancelar</a>
            </div>    
            </form>
        <#else>
            <h5>Por favor comuníquese con su administrador e indíquele que no existen nombres para los expedientes.</h5>
        </#if>
    <#else>
        <h5>Su dependencia no tiene un jefe asignado, comuníquese con su administrador para que le asigne uno.</h5>
    </#if>
</div>

<script>
    function selectExpediente(value){
        var btn1 = document.getElementById("btn-simple");
        var btn2 = document.getElementById("btn-complejo");
        if (value == 1){
            btn1.classList.remove('btn-default');
            btn1.classList.add('btn-primary');
            btn2.classList.remove('btn-primary');
            btn2.classList.add('btn-default');
        }else{
            btn1.classList.remove('btn-primary');
            btn1.classList.add('btn-default');
            btn2.classList.remove('btn-default');
            btn2.classList.add('btn-primary');
        }
    }
</script>

<script src="/js/jstree.min.js"></script>
<script src="/js/app/gen-arbol.js"></script>

<script type="text/javascript">
    validarArbol("#arbol_list_trd",false);
    $('#arbol_list_trd').on("select_node.jstree", function (e, data) {
        if (data.node.parents.length === 1) {
            $(this).jstree("open_node", data.node.id);
        } else {
            $("#trdIdPrincipal").val(data.node.data.jstree.id);
            $("#trdNombre").text(data.node.text);
            var sigla = data.node.text.split("-")
            $("#trdSigla").val(sigla[0]);
            $('#input-nombre-1').text($.trim(sigla[0]));
            $("#trdModalArbol").modal('hide');
        }
    }).jstree();
        
    $('#numberExpediente').on('input',function(e){
        var texIn = $('#numberExpediente').val()
        $('#input-nombre-3').text(('000' + texIn).slice(-3));
    });
        
    $('#opcionalNombre').on('input',function(e){
        var texIn = $('#opcionalNombre').val()
        $('#input-nombre-5').text("-"+texIn);
    });
    
    $("#parNombreExpediente").change(function() {
        $("#input-nombre-4").text($(this).children(':selected').data('text'));
    }).change();
    
    $(document).on('submit','form',function(){
        loading(event);
    });
</script>

<#include "bandeja-footer.ftl">
