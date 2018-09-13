<#setting number_format="computer">
<#assign pageTitle = "Parámetros de busqueda" />

<#assign deferredJS = "" />

<#import "spring.ftl" as spring />
<#include "header.ftl" />
<#include "consulta-parametros-util.ftl"/>
<#include "gen-arbol-dependencias.ftl">

<style>
    .card{
        padding: 60px;
        border-radius: 46px;
        width: 260px;
        overflow: hidden;
        -webkit-box-shadow: 0 10px 6px -6px #777;
	-moz-box-shadow: 0 10px 6px -6px #777;
	box-shadow: 0 10px 6px -6px #777;
    }
    .card:first-child{
        margin-left: auto;
        margin-right: 30px;
    }
    .card:last-child{
        margin-right: auto;
        margin-left: 30px;
    }
    .card a{
        position: absolute;
        bottom: 0px;
        width: 100%;
        left: 0px;
    }
    
    .container-1-appear {
        position: relative;
        -webkit-animation: appear 0.5s;  /* Safari 4.0 - 8.0 */
        -webkit-animation-fill-mode: forwards; /* Safari 4.0 - 8.0 */
        animation: appear 0.5s;
        animation-fill-mode: forwards;
    }
    
    .container-1-dissapper {
        position: relative;
        -webkit-animation: dis-appear 0.5s;  /* Safari 4.0 - 8.0 */
        -webkit-animation-fill-mode: forwards; /* Safari 4.0 - 8.0 */
        animation: dis-appear 0.5s;
        animation-fill-mode: forwards;
    }
    
    .fade-in{
        margin-top: 25px;
        text-align: center;
        animation: fadein 2s;
        -moz-animation: fadein 2s; /* Firefox */
        -webkit-animation: fadein 2s; /* Safari and Chrome */
        -o-animation: fadein 2s; /* Opera */
    }
    
    .container-form{
        padding: 20px 40px;
        margin: 20px;
        background-color: #f0f0f0;
        border-radius: 20px;
    }

    /* Safari 4.0 - 8.0 */
    @-webkit-keyframes appear {
        from {top: 0px;}
        to {top: 200px;}
    }

    @keyframes appear {
        from {top: 0px;}
        to {top: 200px;}
    }
    
    @-webkit-keyframes dis-appear {
        from {top: 0px;}
        to {top: 0px;}
    }

    @keyframes dis-appear {
        from {top: 200px;}
        to {top: 0px;}
    }
    
    @keyframes fadein {
        from {
            opacity:0;
        }
        to {
            opacity:1;
        }
    }
    @-moz-keyframes fadein { /* Firefox */
        from {
            opacity:0;
        }
        to {
            opacity:1;
        }
    }
    @-webkit-keyframes fadein { /* Safari and Chrome */
        from {
            opacity:0;
        }
        to {
            opacity:1;
        }
    }
    @-o-keyframes fadein { /* Opera */
        from {
            opacity:0;
        }
        to {
            opacity: 1;
        }
    }
    
    @media only screen and (max-width: 500px) {
        .card{
            padding: 20px;
        }
        
        .card:first-child{
            margin-right: 10px;
        }
        .card:last-child{
            margin-left: 10px;
        }
        
        .col-sm-5 .input-group{
            display: inline-block;
        }
       
    }
</style>

<div class="container-fluid">
    <h4>Consulta de documentos</h4>
    <div class="container-fluid">
        <div id="container-1" class="row container-1-appear" style="display: flex;">
              <div class="card">
                <div class="card-body">
                  <h5 class="card-title">Internos</h5>
                  <p class="card-text">Registros de externos, Documentos internos inteligencia, Actas</p>
                </div>
                  <a href="#" class="btn btn-primary" onclick="selected(0)">Seleccionar</a>
              </div>
              <div class="card">
                <div class="card-body">
                  <h5 class="card-title">Externos</h5>
                  <p class="card-text">Documentos hacia otras entidades.</p>
                </div>
                <a href="#" class="btn btn-primary" onclick="selected(1)">Seleccionar</a>
              </div>
        </div>
        <div id="container-2" class="form-group row fade-in container-form" style="display:none; position: relative; margin-top: 70px;">
            <div style="position: absolute; top: -36px;">
                <div class="btn-group btn-toggle" style="border-top-left-radius: 30px; border-top-right-radius: 30px; overflow: hidden;"> 
                    <a id="toggle-1" class="btn btn-default" onclick="selected(0)" style="color: black;">Interno</a>
                    <a id="toggle-2" class="btn btn-default" onclick="selected(1)" style="color: black;">Externo</a>
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
                    <div id="con-externo">
                        <label for="destinatario" class="col-sm-1 col-form-label text-xs-right">Destino externo</label>
                        <div class="col-sm-5">
                            <div class="input-group" style="width: 100%;">
                                <select class="selectpicker" id="destinoExterno" name="destinoExterno" data-live-search="true">
                                    <#if destinosExternos??>
                                <option value=""></option>
                                        <#list destinosExternos as dte>
                                        <#if dte.id?string == ((documento.destinoExterno.id)!"")?string >
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
                    <div id="con-interno">
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
                <div class="form-group row">    
                <label for="buscar" class="col-sm-1 col-form-label text-xs-right">Buscar en todo SICDI</label>
                    <div class="form-inline col-sm-2 input-group" style="margin-left: auto; margin-right: auto;">
                        <div class="btn-group btn-toggle"> 
                            <button class="btn btn-lg btn-default">Si</button>
                            <button class="btn btn-lg btn-primary active">No</button>
                        </div>
                    </div>
                </div>
                <div class="form-group row">
                    <button type="submit" class="btn btn-primary btn-lg" onclick="loading(event);">Buscar</button>
                </div>
                </div>
            </form>
        </div>
            
    
    
<br><br>
</div>

</div>
<#include "footer.ftl" />
<script src="/js/app/consulta-parametros.js"></script>

<script type="text/javascript">
    function selected(select) {
        $("#container-1").attr("class","container-1-dissapper");
        
        if (select == 0) {
            $("#con-externo").css("display","none"); 
            $("#con-interno").css("display","block");
            $("#toggle-1").css("background-color", "#f0f0f0");
            $("#toggle-2").css("background-color", "#dddddd");
        }else{
            $("#con-interno").css("display","none"); 
            $("#con-externo").css("display","block");
            $("#toggle-2").css("background-color", "#f0f0f0");
            $("#toggle-1").css("background-color", "#dddddd");
        }
         
        setTimeout(function(){
            $("#container-1").css("display","none"); 
            $("#container-2").css("display","block");
        }, 300);
    }
</script>