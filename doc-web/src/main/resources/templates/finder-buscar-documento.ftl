<#--
    2018-05-29 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
    feature-162: Cambio de nombre del template "transferencia-archivo-buscar-usuario.ftl"
    a "finder-buscar-usuario.ftl" para unificar el componente.
-->
<#setting number_format="computer">

<#--
    2018-05-29 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech) 
    feature-162: Macro que permite imprimir el enlace con la función Javascript 
    a aplicar según el tipo de finder.

    type: Tipo de finder.
    usuario: Usuario correspondiente a la selección.
-->
<#macro getSelectionJavaScript documento >
    <a href="javascript:selectFindResult('${documento.id}', '${documento.asunto}');">Seleccionar</a>
</#macro>

<!DOCTYPE html>
<html>
    <head>
        <title>Búsqueda</title>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="x-ua-compatible" content="ie=edge">

<!-- Bootstrap CSS -->
        <link rel="stylesheet" href="/css/bootstrap.css">
        <!-- Fonts -->
        <link href='/fonts/roboto.css' rel='stylesheet' type='text/css'>
        <link href='/fonts/lobster.css' rel='stylesheet' type='text/css'>
        <link href='/fonts/raleway.css' rel='stylesheet' type='text/css'>

    <!-- Custom -->
        <link href='/css/custom.css' rel='stylesheet' type='text/css'>
        <link href='/css/juego.css' rel='stylesheet' type='text/css'>
        <link href='/css/vis.min.css' rel='stylesheet' type='text/css'>

        <link rel="stylesheet" href="/css/cdnjscloudfire.style.min.css" />

        <link rel="stylesheet" href="/css/jquery-ui.css">
        <script src="/jquery/jquery.min.js"></script>

    </head>
    <body>
        <div class="container-fluid">
            <form action = "/consulta/finder-buscar-documento" method="POST">
                <input type="hidden" id="type" name="type" value="${(type)!""}" />
                <#if pin??>
                <input type="hidden" id="pin" name="pin" value="${(pin)!""}" />
                </#if>
                <div class="form-group row">
                    <div class="input-group">
                        <input type="text" id="criteria" name="criteria" class="form-control" value="${criteria!""}" placeholder="Buscar documento por asunto, radicado"/>
                        <div class="input-group-btn">
                            <button type="submit" class="btn btn-primary">Buscar</button>
                        </div> 
                    </div> 
                </div>
                <div>
                    <#if documentos??>
                        <table class="table table-striped table-bordered table-hover">
                            <thead>
                                <tr>
                                    <th></th>
                                    <th>Asunto</th>
                                    <th>Radicado</th>
                                    <th>Usuario Creador</th>
                                    <th>Dependencia Destino</th>
                                    <th>Clasificación</th>
                                    <th>Fecha Modificación</th>
                                </tr>
                            </thead>
                            <tbody>
                                <#list documentos as documento >
                                <tr>
                                    <td><@getSelectionJavaScript documento /></td>
                                    <#--
                                        2018-05-29 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech) feature-162:
                                        Presentación de grado de usuario.
                                    -->
                                    <td>${(documento.asunto)!""}</td>
                                    <td>${(documento.numeroRadicado)!""}</td>
                                    <td>${(documento.nombreUsuarioElabora)!""}</td>
                                    <td>${(documento.unidadDestino)!""}</td>
                                    <td>${(documento.nombreClasificacion)!""}</td>
                                    <td>${(documento.cuandoMod)!""}</td>
                                </tr>
                                <tr>
                                </#list>
                            </tbody>
                        </table>
                    </#if>
                </div>
            </form>

            <center>
            <#if pageIndex gt 0>
                <a href="/consulta/finder-buscar-documento?criteria=${criteria!""}&pageIndex=${pageIndex - 1}&type=${type!""}&pin=${pin!''}" class="btn btn-primary btn-sm">Anterior</a>
            </#if>

            <#if pageIndex lt (totalPages - 1)>
                <a href="/consulta/finder-buscar-documento?criteria=${criteria!""}&pageIndex=${pageIndex + 1}&type=${type!""}&pin=${pin!''}" class="btn btn-primary btn-sm">Siguiente</a>
            </#if>
            </center>

        </div>

        <script src="/js/app/buscar-documento.js"></script>
        
    </body>
</html>
