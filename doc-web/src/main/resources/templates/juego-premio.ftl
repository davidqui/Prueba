<#assign pageTitle = "Procesos">
<#assign activePill = "proceso">
<#include "header.ftl">
<div class="jumbotron cus-juego-jumbotron">

 
    <h6 class="juego-titles"> ${username}</h6>
    <p class="lead cus-lead">¡Felicitaciones! Ha superado el nivel: ${juegoNivel.nombre}</p>
    <h6 class="juego-titles"> ¡Ha ganado un ${juegoNivel.premio}</h6>
    <a href="/capacitacion-juego/puntuacion/${jjuId}/0" class="btn btn-success">¡Jugar!</a>
</div>
<#include "admin-footer.ftl">