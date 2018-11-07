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
        
        <!-- favicon -->
        <link rel="icon" type="image/ico" href="/img/favicon.ico" />
        
        <link rel="stylesheet" href="/css/jquery-ui.css">
        <script src="/jquery/jquery.min.js"></script>
        <script src="/js/app/header.js"></script>
        <title>${pageTitle}</title>

        <script src="/ckeditor/ckeditor.js"></script>
        
        <!-- loader -->
        <link href='/css/loader.css' rel='stylesheet' type='text/css'>
        
        <!-- header -->
        <link href='/css/header.css' rel='stylesheet' type='text/css'>
        
        
        <!-- toggle -->
        <link href='/css/toggle.css' rel='stylesheet' type='text/css'>
        <!-- search selector -->
        <link rel="stylesheet" href="/css/bootstrap-select.css" />
        <script src="/js/bootstrap-select.min.js"></script>

    <#if headScripts??>
      ${headScripts}
    </#if>


        </head>
    <body>
    <#include "loader.ftl">
    <#if username?? >
        <div class="cus-top-bar" style="height: 30px;">
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
       </#if>   
        <#if usuActivo??>
            <#if usuActivo>
                <#if username?? >
                    <#if utilController??>
                        <#if utilController.hasAdminRole()>
                <a href="/admin" onclick="loading(event);">Administración</a>
                        </#if>
                    </#if>

          <#if utilController??>
          	<#if utilController.hasArchivoRole()>
              <!-- <a href="/archivo/solicitud/list-solicitud">Archivo</a> -->
          	</#if>
          </#if>
                <!--#181 se agrega loader --> 
                <a href="/capacitacion/intro" onclick="loading(event);" target="_blank">Capacitación</a>
                <a href="/manual/intro" onclick="loading(event);" target="_blank">Manual</a>

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
                                <h4 class="modal-title" style="color: #0275d8;">Cambiar Contraseña</h4>
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


                <a href="#" data-toggle="modal" data-target="#modal-notificaciones" onclick="notificar()">Notificaciones</a>
                <!--modal admin notificaciones-->
                <div class="modal fade" id="modal-notificaciones" role="dialog">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal">&times;</button>
                                <h4 class="modal-title" style="color: #0275d8;">Configuración Notificaciones</h4>
                                </div>
                                <div class="modal-body" style="height: 600px; overflow-y: scroll; color: black;">

                                </div>
                            </div>
                        </div>
                    </div>
            
                <a href="#" data-toggle="modal" data-target="#modal-inhabilitar-usuario" onclick="razonesInhabilitacion();">Inactivar Usuario</a>
                <!--modal inhabilitar Usuario-->
                <div class="modal fade" id="modal-inhabilitar-usuario" role="dialog">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal">&times;</button>
                                <h4 class="modal-title" style="color: #0275d8;">Inactivar Usuario</h4>
                                </div>
                                <div class="modal-body" style="color: black;">
                                    <p>“Señor Usuario recuerde que al inactivar su usuario, bloqueara inmediatamente el acceso a las funcionalidades de SICDI, además no permitirá que otros usuarios le asignen documentos, es su responsabilidad activarse nuevamente una vez concluida su ausencia laboral.”</p>
                                    <div class="form-group">
                                      <label for="comment">Motivo de la ausencia laboral:</label>
                                      <select class="form-control" id="razonInhabilitar" name="razonInhabilitar">
                                      </select>
                                    </div>
                                </div>
                                <div class="modal-footer">
                                  <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancelar</button>
                                  <button type="button" class="btn btn-danger" onclick="deshabilitarUsuario()">Inactivar</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
                </#if>
        <nav class="navbar navbar-light" style="background-color: #e3f2fd;">
                <#-- 2017-03-31 jgarcia@controltechcg.com Issue #34 Cambio de nombre de aplicación a SICDI. -->
            <a class="navbar-brand hidden-md-down" href="#"><h3>SICDI</h3></a>
                        <#if username?? >
            <ul class="nav navbar-nav">
                <li class="nav-item mb-header-sicdi">
                    <div class="btn-group mb-header-sicdi">
                        <button type="button" class="btn btn-primary dropdown-toggle btn-sm mb-header-sicdi" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            <span class="md-down">Bandejas</span>
                            </button>
                        <div class="dropdown-menu">
                            <!--#181 se agrega loader --> 
                            <a href="/bandeja/entrada" class="dropdown-item" onclick="loading(event);">Bandeja de entrada</a>
                            <a href="/bandeja/enviados" class="dropdown-item" onclick="loading(event);">Bandeja de enviados</a>
                            <a href="/bandeja/entramite" class="dropdown-item" onclick="loading(event);">Bandeja en trámite</a>

                                                                <#--
                                                                    2017-04-18 jgarcia@controltechcg.com Issue #50 (SICDI-Controltech):
                                                                    Opción de bandeja de apoyo y consulta. 
                                                                 -->	  				  			  				  			  				  			  				  			  				  		
                            <a href="/bandeja/apoyo-consulta" class="dropdown-item">Bandeja de consulta</a>
                            </div>
                        </div>
                    </li>
                <li class="nav-item mb-header-sicdi">
                    <!--#181 se agrega loader --> 
                    <a href="/proceso/list" class="btn btn-success btn-sm" onclick="loading(event);"><span class="md-down">Registro</span></a>
                    </li>
                <li class="nav-item mb-header-sicdi">
                    <!--#181 se agrega loader --> 
                    <a href="/expediente/listarExpedientes?" class="btn btn-secondary btn-sm" onclick="loading(event);"><span class="md-down">Expedientes</span></a>
                    </li>
                <li class="nav-item mb-header-sicdi">
                    <!--#181 se agrega loader --> 
                    <a href="/expediente/carpeta" class="btn btn-secondary btn-sm" onclick="loading(event);"><span class="md-down">Archivos</span></a>
                </li>

                        <!--
                            2017-08-29 jgarcia@controltechcg.com Issue #120 (SICDI-Controltech) feature-120:
                            Botón para acceder al módulo de transferencia de archivo.
                -->
                <li class="nav-item mb-header-sicdi">
                    <!--#181 se agrega loader --> 
                    <a href="/transferencia-archivo/listar" class="btn btn-warning btn-sm" onclick="loading(event);">
                        <span class="md-down">Transferencia de Archivo</span>
                        </a>
                    </li>

                <li class="nav-item hidden-xs-down">
                    <form action="/consulta" method="GET" class="form-inline" id="consulta-form">
                        <div class="input-group">
                            <input type="text" class="form-control form-control-sm" id="paramConsulta" name="term"/>
                            <!--#181 se agrega loader --> 
                            <a href="#" class="btn btn-secondary btn-sm input-group-addon" onclick="loading(event); return submitConsulta();">Buscar</a>
                            </div><br/>
                        <a href="/consulta/parametros" onclick="loading(event);"><small>Búsqueda avanzada</small></a>
                        </form>
                    </li>

                <li class="nav-item mb-header-sicdi">
                    <!--#181 se agrega loader --> 
                    <a href="/reporteDependencia/init" class="btn btn-warning btn-sm" onclick="loading(event);">
                        <span class="md-down">Reporte</span>
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
            <#else>
        </div>
    </div>
        <div class="container">
            <div class="row">
                <h1 class="display-1 hidden-md-down">&nbsp;</h1>
                <div class="card card-inverse"
                     style="color: #fff; background-color: #2e73a1; border-color: #2e73a1">
                    <div class="card-block">
                        <h1>SICDI</h1>
                        <p class="lead">SISTEMA CLASIFICADO DE DOCUMENTOS DE INTELIGENCIA MILITAR</p>
                        </div>
                    </div>
                <div class="card"
                     style="background-color: #fff; border-color: #94ce18">
                    <div class="card-block lead">
                        <p>
                            Usuario actualmente <b>Inactivo.</b>
                            </p>
                        <p> Para retomar operación en el sistema SICDI debe activar su usuario.</p>
                        <button type="button" class="btn btn-primary btn-sm" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" onclick="habilitarUsuario()">
                            Activar
                            </button>
                        </div>
                    </div>
                </div>
            </div>
            </#if>
        </#if>
	<@flash/>
        <div class="container-fluid" id="masterdiv">
            <div class="row cus-root-container">
                <script>
                        
                    $("#consulta-form").on("submit", function(){
                     $(".div-loader").css({ display: "block" });
                     return true;
                   });
                       
                   $('.selectpicker').selectpicker();
                </script>