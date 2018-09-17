<#include "util-macros.ftl" />
<!DOCTYPE html>
<html lang="en">
  <head>
    <!-- Required meta tags always come first -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
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
    
    <!-- favicon -->
    <link rel="icon" type="image/ico" href="/img/favicon.ico" />

    <link rel="stylesheet" href="/css/jquery-ui.css">
	<script src="/jquery/jquery.min.js"></script>
    <title>Archivo</title>

    <script src="/ckeditor/ckeditor.js"></script>
    
    <!-- 2017-02-24 jgarcia@controltechcg.com: Comentado para evitar presentación de alertas vacias.
    
    <script>
    
    $(document).ready(function(){
    
    	$("input").each(function(){
    	alert($(this).val());
    		$(this).val($(this).val().replace("&quot;",'"'));
    	});
    
    });
    
    </script>
    
    -->

    <#if headScripts??>
      ${headScripts}
    </#if>

  </head>
  <body>
    <#if username?? >
      <div class="cus-top-bar">
        <div class="pull-xs-right">
            <form action="/login?logout" method="POST" class="form-inline" id="logout-form">
              Usuario: ${username} - ${userprofile}
              <a href="#" onclick="return submitLogout();" style="color:red; font-weight: bold">Cerrar sesión</a>
            </form>
            <script type="text/javascript">
            function submitLogout() {
              $('#logout-form').submit();
              return true;
            }
            </script>
        </div>        
        <div>
          <#if utilController??>
          	<#if utilController.hasAdminRole()>
          		<a href="/admin">Administración</a>
          	</#if>
          </#if>
          <a href="/informes">Informes</a>
          <a href="/archivo/solicitud/list-solicitud">Archivo</a>
          <a href="/capacitacion-juego/intro">Capacitación</a>
        </div>
      </div>
    </#if>
   <nav class="navbar navbar-light" style="background-color: #e3f2fd;">
   <#-- 2017-03-31 jgarcia@controltechcg.com Issue #34 Cambio de nombre de aplicación a SICDI. -->
   <a class="navbar-brand hidden-md-down" href="#"><h3>SICDI</h3></a>
      <#if username?? >
        <ul class="nav navbar-nav">
          <li class="nav-item">
      		<div class="btn-group">
				<button type="button" class="btn btn-primary dropdown-toggle btn-sm" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
					<span class="hidden-md-down">Bandejas</span><span class="hidden-lg-up">Ban</span>
				</button>
				<div class="dropdown-menu">
		  	         <a href="/archivo/solicitud/list-solicitud" class="dropdown-item">Solicitudes prestamo</a>
		  	         <a href="#" class="dropdown-item">En transito</a>
				</div>
			</div>
      	  </li>
          <li class="nav-item">
            <a href="#" class="btn btn-success btn-sm">Archivar</a>
          </li>
          <li class="nav-item">
            <a href="/archivo/list-archivados-doc" class="btn btn-secondary btn-sm">Documentos</a>
          </li>
          <li class="nav-item">
            <a href="/archivo/list-archivados-exp" class="btn btn-secondary btn-sm">Expedientes</a>
          </li>
          <li class="nav-item">
            <a href="#" class="btn btn-secondary btn-sm">Búsqueda Avanzada</a>
          </li>
          <li class="nav-item">
            <a href="/archivo/menu" class="btn btn-secondary btn-sm">Administrables</a>
          </li>
          <li class="nav-item">
          <form action="/consulta" method="POST" class="form-inline" id="consulta-form">
            <div class="input-group">
        		<input type="text" class="form-control form-control-sm" id="paramConsulta" name="term"/>
       				<a href="#" class="btn btn-secondary btn-sm input-group-addon" onclick="return submitConsulta();">Buscar</a>
       		</div>
          </form>
          <script type="text/javascript">
            function submitConsulta() {
              $('#consulta-form').submit();
              return true;
            }
           </script>
          </li>
        </ul>
      </#if>
    </nav>
    <div class="container-fluid">
      <div class="row cus-root-container">
