<#assign pageTitle = "Crear Expediente"/>
<#include "bandeja-header.ftl">
<#include "gen-arbol-trd.ftl">
<#import "spring.ftl" as spring />


<div class="container-fluid" style="max-width: 800px; margin-left: inherit;">
    <form action="/expediente/crear" method="POST" enctype="multipart/form-data" >
    <fieldset class="form-group">
        <label for="textoObservacion">Texto</label>
        <input type="text" class="form-control" id="nombre" name="nombre" value="${(expediente.nombre)!""}"/>
    </fieldset>
        
    <div class="btn-group" data-toggle="buttons" id="topButtonDiv" style="width: 800px;">
        <button type="button" class="btn btn-primary" id="btn-simple" style="width: 50%;" onclick="selectExpediente(1)">SIMPLE
        <input type="radio" id="radio1" name="expTipo" value="simple" checked></button>
        <button type="button" class="btn btn-default" id="btn-complejo" style="width: 50%;" onclick="selectExpediente(2)">COMPLEJO
        <input type="radio" id="radio2" name="expTipo" value="complejo"> </button>              
    </div>
        
    <div class="tab-content" id="myTabContent">
            <fieldset class="form-group">
            <label for="trd">Tabla de retención documental</label>
            <input type="hidden" id="trd" value="${(expediente.trd.id)!""}" />
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

<script src="/js/app/gen-arbol.js"></script>
<script src="/js/jstree.min.js"></script>

<script type="text/javascript">
    validarArbol("#arbol_list_trd",false);
</script>

<#include "bandeja-footer.ftl">
