<#--  2017-03-28 jgarcia@controltechcg.com Issue #14: Implementación de controlador para presentación de mensajes de error con el estilo de la aplicación. -->

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
<title>HTTP STATUS ${status}</title>

<script src="/css/jquery.min.js"></script>

</head>
<body class="cus-login-body">
	<div class="container">
		<div class="row">
			<h1 class="display-1 hidden-md-down">&nbsp;</h1>
			<div class="card card-inverse"
				style="color: #fff; background-color: #2e73a1; border-color: #2e73a1">
				<div class="card-block">
				    <#-- 2017-03-31 jgarcia@controltechcg.com Issue #34 (SIGDI-Controltech) Cambio de nombre de aplicación a SICDI. -->
				    <#-- 2017-04-07 jgarcia@controltechcg.com Issue #44 (SIGDI-Controltech) Cambio de nombre de aplicación a Sistema de Clasificación Documental. -->
				    <#-- 2017-04-10 jgarcia@controltechcg.com Issue #44 (SIGDI-Controltech) Cambio de nombre de aplicación a SISTEMA CLASIFICADO DE DOCUMENTOS DE INTELIGENCIA MILITAR. -->
					<h1>SICDI</h1>
					<p class="lead">SISTEMA CLASIFICADO DE DOCUMENTOS DE INTELIGENCIA MILITAR</p>
				</div>
			</div>
			<div class="card"
				style="background-color: #fff; border-color: #94ce18">
				<div class="card-block lead">
					<p>
						<b>STATUS ${status}</b>
					</p>
					<#if statusMessage??>
					<p>${statusMessage}</p>
					</#if>
					
					<#if exceptionMessage??>
					<p style="font-size:small">Mensaje generado por la aplicación: ${exceptionMessage}</p>
					</#if>
					
				</div>
			</div>
		</div>
	</div>
</body>
</html>