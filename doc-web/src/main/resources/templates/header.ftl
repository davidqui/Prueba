<#include "util-macros.ftl" />
<!DOCTYPE html>
<html lang="en">
    <head>
      <!-- Required meta tags always come first -->
    <#import "spring.ftl" as spring />
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

        <link rel="stylesheet" href="/css/cdnjscloudfire.style.min.css" />

        <link rel="stylesheet" href="/css/jquery-ui.css">
        <script src="/jquery/jquery.min.js"></script>
        <title>${pageTitle}</title>

        <script src="/ckeditor/ckeditor.js"></script>
         <!-- loader -->
        <link href='/css/loader.css' rel='stylesheet' type='text/css'>

    <#if headScripts??>
      ${headScripts}
    </#if>


        </head>
    <body>
    <#include "loader.ftl">
    <#if username?? >
        <div class="cus-top-bar">
            <div class="pull-xs-right">
                <form action="/logout" method="POST" class="form-inline" id="logout-form">            	
                    <span class="hidden-lg-down">Usuario: ${username} - ${userprofile}</span>
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
                <!--#181 se agrega loader --> 
                <a href="/admin" onclick="loading();">Administración</a>
          	</#if>
          </#if>

          <#if utilController??>
          	<#if utilController.hasArchivoRole()>
              <!-- <a href="/archivo/solicitud/list-solicitud">Archivo</a> -->
          	</#if>
          </#if>
                <!--#181 se agrega loader --> 
                <a href="/capacitacion-juego/intro" onclick="loading();">Capacitación</a>
                <a href="/video/manual_registro1_player.html" target="_blank">Manual  </a>

    <#--
        2018-05-02 jgarcia@controltechcg.com Issue #159 (SICDI-Controltech)
        feature-159: Presentación de enlace al OWA, si el usuario en sesión
        tiene activado el acceso en su configuración de dominio.
    -->
    <#if user_can_use_owa_link>
                <a href="#" data-toggle="modal" data-target="#modal-owa-link">Cambiar Contraseña</a>

                <div class="modal fade" id="modal-owa-link" role="dialog">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal">&times;</button>
                                <h4 class="modal-title" style="color: blue;">Cambiar Contraseña</h4>
                                </div>
                            <div class="modal-body" style="color: black;">
                                <p>Al dar clic al botón "Continuar", acepta acceder al sistema OWA para realizar allí el proceso de cambio de contraseña.</p>
                                <p>Debe permitir en su navegador activar popups provenientes de la URL de SICDI.</p>
                                <p>Finalizado el procedimiento en el sistema mencionado, se recomienda cerrar la sesión en SICDI y acceder con la nueva contraseña.</p>
                                </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-success" data-dismiss="modal" onclick="goToOWA('${owa_link_url}');">Continuar</button>
                                <button type="button" class="btn btn-danger" data-dismiss="modal">Cancelar</button>
                                </div>
                            </div>
                        </div>
                    </div>
    </#if>

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
                            <!--#181 se agrega loader --> 
                            <a href="/bandeja/entrada" class="dropdown-item" onclick="loading();">Bandeja de entrada</a>
                            <a href="/bandeja/enviados" class="dropdown-item" onclick="loading();">Bandeja de enviados</a>
                            <a href="/bandeja/entramite" class="dropdown-item" onclick="loading();">Bandeja en trámite</a>

	  				  		<#--
	  				  		    2017-04-18 jgarcia@controltechcg.com Issue #50 (SICDI-Controltech):
	  				  		    Opción de bandeja de apoyo y consulta. 
	  				  		 -->	  				  			  				  			  				  			  				  			  				  		
                            <a href="/bandeja/apoyo-consulta" class="dropdown-item">Bandeja de consulta</a>
                            </div>
                        </div>
                    </li>
                <li class="nav-item">
                    <!--#181 se agrega loader --> 
                    <a href="/proceso/list" class="btn btn-success btn-sm" onclick="loading();"><span class="hidden-md-down">Registro</span><span class="hidden-lg-up">Reg</span></a>
                    </li>
                <li class="nav-item">
                    <!--#181 se agrega loader --> 
                    <a href="/expediente/listarExpedientes?" class="btn btn-secondary btn-sm" onclick="loading();"><span class="hidden-md-down">Expedientes</span><span class="hidden-lg-up">Exp</span></a>
                    </li>
                <li class="nav-item">
                    <!--#181 se agrega loader --> 
                    <a href="/expediente/carpeta" class="btn btn-secondary btn-sm" onclick="loading();"><span class="hidden-md-down">Archivos</span><span class="hidden-lg-up">Car</span></a>
                </li>

                <!--
                    2017-08-29 jgarcia@controltechcg.com Issue #120 (SICDI-Controltech) feature-120:
                    Botón para acceder al módulo de transferencia de archivo.
                -->
                <li class="nav-item">
                    <!--#181 se agrega loader --> 
                    <a href="/transferencia-archivo/crear" class="btn btn-warning btn-sm" onclick="loading();">
                        <span class="hidden-md-down">Transferencia de Archivo</span>
                        <span class="hidden-lg-up">TAR</span>
                        </a>
                    </li>

                <li class="nav-item hidden-xs-down">
                    <form action="/consulta" method="GET" class="form-inline" id="consulta-form">
                        <div class="input-group">
                            <input type="text" class="form-control form-control-sm" id="paramConsulta" name="term"/>
                            <!--#181 se agrega loader --> 
                            <a href="#" class="btn btn-secondary btn-sm input-group-addon" onclick="loading(); return submitConsulta();">Buscar</a>
                            </div><br/>
                        <a href="/consulta/parametros" onclick="loading();"><small>Búsqueda avanzada</small></a>
                        </form>
                    </li>

                <li class="nav-item">
                    <!--#181 se agrega loader --> 
                    <a href="/reporteDependencia/init" class="btn btn-warning btn-sm" onclick="loading();">
                        <span class="hidden-md-down">Reporte</span>
                        <span class="hidden-lg-up">TAR</span>
                        </a>
                    </li>
                </ul>
		</#if>
            </nav>
        <nav class="navbar navbar-light hidden-sm-up" style="background-color: #e3f2fd;">
            <form action="/consulta" method="POST" class="form-inline" id="consulta-form">
                <div class="input-group">
                    <input type="text" class="form-control form-control-sm" id="paramConsulta" name="term"/>
                    <a href="#" class="btn btn-secondary btn-sm input-group-addon" onclick="return submitConsulta();">Buscar</a>
                    </div>
                </form>
            </nav>
        <script type="text/javascript">
                function submitConsulta() {
                        $('#consulta-form').submit();
                 return true;
                }
            </script>
	<@flash/>   
        <div class="container-fluid">
            <div class="row cus-root-container">
                <script>
                    $( window ).on( "load", function() {
                        $(".div-loader").css({ display: "none" });
                    });
                </script>