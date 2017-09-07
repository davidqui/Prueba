<#setting number_format="computer">

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
            <form action = "/transferencia-archivo/formulario-buscar-usuario" method="POST">
                <div class="form-group row">
                    <div class="input-group">
                        <input type="text" id="criteria" name="criteria" class="form-control" value="${criteria!""}" placeholder="Ingrese los valores de búsqueda..."/>
                        <div class="input-group-btn">
                            <button type="submit" class="btn btn-primary">Buscar</button>
                            </div> 
                        </div> 
                    </div>
                <div>
                    <table class="table table-striped table-bordered table-hover">
                        <thead>
                            <tr>
                                <th></th>
                                <th>Nombre</th>
                                <th>Dependencia</th>
                                <th>Clasificación</th>
                                </tr>
                            </thead>
                        <tbody>
                            <#list usuarios as usuario >
                            <tr>
                                <td><a href="javascript:selectFindResult(${usuario.id});">Seleccionar</a></td>
                                <td>${usuario.nombre}</td>
                                <td>${usuario.dependencia.nombre}</td>
                                <td>${usuario.clasificacion.nombre}</td>
                                </tr>
                            <tr>
                            </#list>
                            </tbody>
                        </table>
                    </div>
                </form>


            <center>
            <#if pageIndex gt 0>
                <a href="/transferencia-archivo/formulario-buscar-usuario?criteria=${criteria!""}&pageIndex=${pageIndex - 1}" class="btn btn-primary btn-sm">Anterior</a>
                    </#if>

            <#if pageIndex lt (totalPages - 1)>
                <a href="/transferencia-archivo/formulario-buscar-usuario?criteria=${criteria!""}&pageIndex=${pageIndex + 1}" class="btn btn-primary btn-sm">Siguiente</a>
            </#if>
                </center>
            
            </div>

        <script src="/js/app/transferencia-archivo-crear.js"></script>

    </html>
