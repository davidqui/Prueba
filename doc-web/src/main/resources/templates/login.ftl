<#assign pageTitle = "Login">
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
    <title>${pageTitle}</title>

	<script src="/css/jquery.min.js"></script>
    
    <script type="text/javascript">
    <!--
        $(document).ready(function(){
        	$("#username").focus();
        });
    -->
    </script>

  </head>
  <body class="cus-login-body">
    <div class="container">
      <div class="row">
		<h1 class="display-1 hidden-md-down">&nbsp;</h1>
        <div class="card card-inverse" style="color: #fff; background-color: #2e73a1; border-color: #2e73a1">
          <div class="card-block">
            <#-- 2017-03-31 jgarcia@controltechcg.com Issue #34 (SIGDI-Controltech) Cambio de nombre de aplicación a SICDI. -->
            <#-- 2017-04-07 jgarcia@controltechcg.com Issue #44 (SIGDI-Controltech) Cambio de nombre de aplicación a Sistema de Clasificación Documental. -->
            <#-- 2017-04-10 jgarcia@controltechcg.com Issue #44 (SIGDI-Controltech) Cambio de nombre de aplicación a SISTEMA CLASIFICADO DE DOCUMENTOS DE INTELIGENCIA MILITAR. -->
            <h1>SICDI</h1>
            <p class="lead">SISTEMA CLASIFICADO DE DOCUMENTOS DE INTELIGENCIA MILITAR</p>
          </div>
        </div>
        <#if !compatibleBrowser >
	        <!--  
	        <div class="card-block alert alert-danger">
	        		El navegador no es completamente compatible con la aplicación. Use el navegador Mozilla Firefox en su última versión.
	        </div>
	         -->
        </#if>
        <div class="card" style="background-color: #fff;border-color:#94ce18 ">
        <#--
        	2017-06-15 jgarcia@controltechcg.com Issue #23 (SICDI-Controltech) hotfix-23:
        	Manejo de condicionales en presentación de mensaje de error en login, ya que
        	las condiciones implementadas no funcionan con login contra LDAP. 
         -->
          <#if error??>
          	<div class="card-block lead">
          		<h1>Lo sentimos...</h1>
          		<p class="alert alert-danger">
          			Nombre de usuario o contraseña no son válidos
          		</p>
          		<#if Session.SPRING_SECURITY_LAST_EXCEPTION?? && Session.SPRING_SECURITY_LAST_EXCEPTION.message?has_content>
	          		<p class="small">
	          			<span class="label label-danger">Mensaje servicio autenticación:</span> ${Session.SPRING_SECURITY_LAST_EXCEPTION.message}
	          		</p>
          		</#if>
          		<p>
          			<a href="/login" class="btn btn-primary">Intentar nuevamente</a>
          		</p>
          	</div>
          <#else>
              <#if logout??>
              	 <div class="card-block lead alert alert-success">
              	 	Ha cerrado la sesión correctamente
              	 </div>
              </#if>
	          <div class="card-block lead">
	            <form action="/login" method="POST">
	              <fieldset class="form-group">
	                <label for="username">Nombre de usuario</label>
	                <input type="text" name="username" id="username" class="form-control"/>
	              </fieldset>
	              <fieldset class="form-group">
	                <label for="password">Contraseña</label>
	                <input type="password" name="password" id="password" class="form-control"/>
	              </fieldset>
	              <button onclick="return submitLogin();" class="btn btn-primary">Entrar</button>
	            </form>
	            <script type="text/javascript">
	                $('#username').blur( function() {
	                	var v = $(this).val();
	                	$(this).val( v.toLowerCase() );
	                });
		            function submitLogin() {
		              var valor = $('#username').val();
		              $('#username').val( valor.toLowerCase() );	
		              $('#logout-form').submit();
		              return true;
		            }
		            </script>
	          </div>
          </#if>
        </div>
      </div>
    </div>
    
  </body>
</html>