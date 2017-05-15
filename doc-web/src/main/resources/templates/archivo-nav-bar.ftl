<nav class="navbar navbar-light" style="background-color: #e3f2fd;">
	  <#-- 2017-03-31 jgarcia@controltechcg.com Issue #34 Cambio de nombre de aplicación a SICDI. -->
      <a class="navbar-brand" href="#"><h3 class="cus-pap-font">SICDI</h3></a>
      <#if username?? >
        <ul class="nav navbar-nav">
          <li class="nav-item">
            <a href="/bandeja" class="btn btn-primary">Bandeja</a>
          </li>
          <li class="nav-item">
            <a href="/proceso/list" class="btn btn-success">Prestamos</a>
          </li>
          <li class="nav-item">
            <a href="/expediente/list?arch=1" class="btn btn-secondary">Archivo</a>
          </li>
          <li class="nav-item">
            <a href="/expediente/carpeta" class="btn btn-secondary">Busqueda</a>
          </li>
          <li class="nav-item">
            <a href="/archivo/menu" class="btn btn-secondary">Administrables</a>
          </li>
          <li class="nav-item">
          <form action="/consulta" method="POST" class="form-inline" id="consulta-form">
            <input type="text" class="form-control" id="paramConsulta" name="term"/>
            <a href="#" class="btn btn-secondary" onclick="return submitConsulta();">Buscar</a>
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