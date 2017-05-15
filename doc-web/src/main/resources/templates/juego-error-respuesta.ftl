<#assign pageTitle = "Juego error respuesta">
<#include "header.ftl">
<div class="jumbotron cus-juego-jumbotron">

	<h6 class="juego-intro-titles"> ${username}</h6>
    <p class="lead cus-lead">Ha fallado la respuesta, debe reiniciar el nivel.</p>
    
    <a href="/capacitacion-juego/puntuacion/${jjuId}/0" class="btn btn-success">Jugar</a></button>
</div>
<#include "admin-footer.ftl">