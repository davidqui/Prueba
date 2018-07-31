<#assign pageTitle = "Crear Expediente"/>
<#include "bandeja-header.ftl">
<#include "gen-arbol-trd.ftl">
<#import "spring.ftl" as spring />
<#include "util-macros.ftl" />

<script src="/js/tinymce.min.js"></script>

<div class="container-fluid" style="max-width: 800px; margin-left: inherit;">
    <form action="/expediente/crear" method="POST" enctype="multipart/form-data" >
    <fieldset class="form-group">
        <label for="expNombre">Nombre</label>
        <input type="text" class="form-control" id="expNombre" name="expNombre" value="${(expediente.expNombre)!""}"/>
    </fieldset>
        
    <div class="btn-group" data-toggle="buttons" id="topButtonDiv" style="width: 100%;">
        <button type="button" class="btn btn-primary" id="btn-simple" style="width: 50%;" onclick="selectExpediente(1)">SIMPLE
        <input type="radio" id="radio1" name="expTipo" value="1" checked></button>
        <button type="button" class="btn btn-default" id="btn-complejo" style="width: 50%;" onclick="selectExpediente(2)">COMPLEJO
        <input type="radio" id="radio2" name="expTipo" value="2"> </button>              
    </div>
        
    <div class="tab-content" id="myTabContent">
            <fieldset class="form-group">
            <label for="trdIdPrincipal">Tabla de retención documental</label>
            <input type="hidden" id="trdIdPrincipal" name="trdIdPrincipal" value="${(expediente.trd.id)!""}" />
            <div class="row">
                <div class="col-lg-12">
                    <div class="input-group">
                        <div class="form-control" id="trdNombre">${(documento.trd)!"Por favor seleccione una subserie..."}</div>
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
        
    <fieldset class="form-group">
        <label for="expNombre">Descripción</label>
        <textarea class="form-control" rows="5" id="expDescripcion" name="expDescripcion">${(expediente.expDescripcion)!""}</textarea>
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
        <a href="/expediente/list" class="btn btn-secondary">Cancelar</a>
    </div>    
    </form>
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
            $("#trdModalArbol").modal('hide');
        }
    }).jstree();
</script>

<#include "bandeja-footer.ftl">
