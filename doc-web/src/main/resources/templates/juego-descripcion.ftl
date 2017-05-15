<#assign pageTitle = "Capacitación">
<#include "capacitacion-opciones.ftl">

<div class="container-fluid">
    <h3>Bienvenido a la capacitación sobre  ${juego.nombre}</h3>

    <#if juegoNiveles?size = 0 >
    <div class="jumbotron">
        <h1 class="display-1">No hay registros</h1>
        <p class="lead">En este momento no existen niveles para este juego de capacitación</p>
    </div>
    <#else>
    <div class="alert alert-info">A continuación se presenta una descripción del juego, sus niveles, el número de preguntas por nivel y el premio que se entregará al completar cada nivel.</div>
    <table class="table">
        <thead>
            <tr>
                <th>Nombre del Nivel</th>
                <th>Numero de preguntas</th>
                <th>Dificultad</th>
                <th>Premio</th>
            </tr>
        </thead>
        <tbody>
            <#list juegoNiveles as jn>
            <tr>

               <td nowrap>${jn.nombre}</td>
               <td>${jn.numeroPregutas}</td>
               <td>${jn.dificultad}</td>
               <td>${jn.premio}</td>
           </tr>
           </#list>
       </tbody>
   </table>
   </#if>
   <a href="/capacitacion-juego/puntuacion/${juego.id}/${numPregunta}" class="btn btn-success">¡Jugar!</a>
</div>
</div>
<#include "admin-footer.ftl">