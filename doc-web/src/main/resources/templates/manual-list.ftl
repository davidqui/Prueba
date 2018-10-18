<#assign pageTitle = "Manual de Usuario SICDI">
<#assign activePill = "none">
<#include "header.ftl">
<link href="/css/manual.css" rel="stylesheet" type="text/css"/>
<div>
    <form action="/recursoMultimedia/find" method="GET">
        <div class="input-group mb-3">
            <input type="text" id="id" name="id" class="form-control" placeholder="" aria-label="" aria-describedby="basic-addon1">
            <div class="input-group-append">
                <button type="submit" class="btn btn-primary">Buscar</button>
                </div>
            </div>
        </form>
    </div>
<div>
    <table class="table table-hover">
        <thead class="thead-dark">
            <tr>
                <th scope="col">Nombre</th>
                <th scope="col">Descripcion</th>
                <th scope="col">Fuente</th>
                <th scope="col">Tipo</th>
                <th scope="col">Ubicacion</th>
                </tr>
            </thead>
        <tbody>
            <#list recursoMultimedias as recursoMultimedia>
            <tr>
                <td>${recursoMultimedia.nombre}</td>
                <td> ${recursoMultimedia.descripcion}</td>
                <td>${recursoMultimedia.fuente}</td>
                <td>${recursoMultimedia.tipo}</td>
                <td>${recursoMultimedia.ubicacion}</td>
                <td>
                    <div class="btn-group btn-group-lg">
                        <a class="btn btn-warning" href="view/${recursoMultimedia.id}"><i class="fas fa-eye"></i></a>
                        </div>
                    </td>
                </tr>                
            </#list>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>
