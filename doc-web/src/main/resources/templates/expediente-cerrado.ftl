<#assign pageTitle="Expediente cerrado"/>
<#include "header.ftl" />
  <div class="jumbotron">
    <h1>Expediente Cerrado</h1>
    <p class="lead">La opción a la que desea ingresar corresponde a un expediente que ha sido cerrado por su creador, si desea acceder al mismo debe solicitar acceso al Señor(a)
        ${(usuario.usuGrado.nombre)!""} ${(usuario.nombre)!""} ${(usuario.usuCargoPrincipalId.carNombre)!""}</p>
    <a href="/expediente/listarExpedientes" class="btn btn-lg btn-primary">Volver</a>
  </div>
<#include "footer.ftl" />
