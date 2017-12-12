<#setting number_format="computer">
<#if !pageTitle??>
  <#assign pageTitle = "Reporte" />
</#if>
<#include "header.ftl" />
<div class="text-center">
    <form method="GET" action="/reporteDependencia/consultar" class="form-inline">

        <legend>Parámetros</legend>

        <label for="fechaInicial">Fecha inicial</label>
        <div class="form-inline input-group">
            <input class="form-control datepicker" id="fechaInicial" name="fechaInicial" value="${fechaInicial!""}" required="true"/>
            </div>

        <label for="fechaFinal">Fecha Final</label>
        <div class="form-inline input-group">
            <input class="form-control datepicker" id="fechaFinal" name="fechaFinal" value="${fechaFinal!""}" required="true"/>
            </div>



        <button type="submit" class="btn btn-success btn-lg">Consultar</button>
        <br>
        </form>

    <#if ((!dependencias?? || dependencias == 0) && (!dependenciasTrd?? || dependenciasTrd == 0)) >
    <div class="jumbotron">
        <p class="lead">No se encontraron resultados</p>
        </div>
    <#else>
    <br>
    <br>
    <img src="/reporteDependencia/barraDependencia?fechaInicial=${fechaInicial!""}&fechaFinal=${fechaFinal!""}"/>
    <br>
    <br>
    <br>
    <img src="/reporteDependencia/barraDependenciaTrd?fechaInicial=${fechaInicial!""}&fechaFinal=${fechaFinal!""}"/>
    <br>
    <br>
    <br>
    <a href="/reporteDependencia/generarReporte?fechaInicial=${fechaInicial!""}&fechaFinal=${fechaFinal!""}" >Exportar</a>
    </#if>
    </div>
<script type="text/javascript">
    $(document).ready(function() {
        $('#fechaInicial').prop('readonly', true);
        $('#fechaFinal').prop('readonly', true);

        $('#fechaInicial').each(function() {
            var buton = "<div onclick='document.getElementById(\""+ $(this).attr("id")+ "\").value = \"\"' class=\"input-group-addon btn btn-warning\" >Limpiar</div>";
            $(this).parent().append(buton);
        });
        $('#fechaFinal').each(function() {
            var buton = "<div onclick='document.getElementById(\""+ $(this).attr("id")+ "\").value = \"\"' class=\"input-group-addon btn btn-warning\" >Limpiar</div>";
            $(this).parent().append(buton);
        });
    });
    </script>
<#include "footer.ftl" />