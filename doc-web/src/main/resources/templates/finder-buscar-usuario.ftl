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
<#macro getSelectionJavaScript type usuario >
    <#switch type >
        <#case "ACTA" >
            <a href="javascript:selectUsuarioActa(${usuario.id});">Seleccionar</a>
        <#break>
        <#case "TRANSFERENCIA_ARCHIVO" >
        <#default>
            <a href="javascript:selectFindResult(${usuario.id});">Seleccionar</a>
        <#break>
    </#switch>
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
            <form action = "/finder/usuario/finder-buscar-usuario" method="POST">
                <input type="hidden" id="type" name="type" value="${type}" />
                <#if pin??>
                <input type="hidden" id="pin" name="pin" value="${pin}" />
                </#if>
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
                                <td><@getSelectionJavaScript type usuario /></td>
                                <#--
                                    2018-05-29 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech) feature-162:
                                    Presentación de grado de usuario.
                                -->
                                <td><#if usuario.usuGrado?? >${usuario.usuGrado.id}. </#if>${usuario.nombre}</td>
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
                <a href="/finder/usuario/finder-buscar-usuario?criteria=${criteria!""}&pageIndex=${pageIndex - 1}&type=${type}&pin=${pin!''}" class="btn btn-primary btn-sm">Anterior</a>
            </#if>

            <#if pageIndex lt (totalPages - 1)>
                <a href="/finder/usuario/finder-buscar-usuario?criteria=${criteria!""}&pageIndex=${pageIndex + 1}&type=${type}&pin=${pin!''}" class="btn btn-primary btn-sm">Siguiente</a>
            </#if>
            </center>

        </div>

        <script src="/js/app/transferencia-archivo-crear.js"></script>
        <#--
            2018-05-30 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
            feature-162: Javascript genérico para el finder de usuarios.
        -->
        <script src="/js/app/finder-usuarios.js"></script>
    </body>
</html>
