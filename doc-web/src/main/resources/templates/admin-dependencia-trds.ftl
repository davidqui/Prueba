<#assign pageTitle = "Subseries " + dependencia.nombre />
<#include "admin-header.ftl">

<#--
    2018-05-21 jgarcia@controltechcg.com Issue #170 (SICDI-Controltech)
    feature-170: Textos para el botón de selección para todas las subseries.
    Constante del número de columnas a presentar.
-->
<#assign selectAllText = "Seleccionar todos" />
<#assign removeAllText = "Retirar todos" />
<#assign numeroColumnas = 4 />

<div class="container-fluid">
    <h1 class="cus-h1-page-title">${pageTitle}</h1>
    <p>
        <a href="/dependencias" class="btn btn-link btn-sm">Regresar</a>
    </p>

    <form method="POST">
        <#--
            2018-05-21 jgarcia@controltechcg.com Issue #170 (SICDI-Controltech)
            feature-170: Opción para seleccionar todas las Subseries TRD al 
            mismo tiempo. Cambio en la presentación.
        -->        
        
        <div class="row">
            <#assign hasAllSelected = controller.hasAllSubseriesSelected(dependencia, trds) />            
            <button id="select-all-trd" name="select-all-trd" type="button" class="btn btn-default btn-sm" onclick="return selectAllTrd(this.form, this);">
            <#if hasAllSelected >
                ${removeAllText}
            <#else>
                ${selectAllText}
            </#if>
            </button>
            <input id="selected-all-trd" name="selected-all-trd" type="hidden" value="${hasAllSelected?c}" />
        </div>
        </br>
        
        <#assign trdCount = 0 />
        <table class="table table-bordered">
            <tbody>
                <#list trds as trd >
                    <#if (trdCount % numeroColumnas) == 0 >
                    <tr>
                    </#if>
                        <td>
                            <div class="checkbox">
                                <label class="checkbox-inline">
                                    <input name="trd" type="checkbox" value="${trd.id}" <#if controller.has(trd.id, dependencia.trds)>checked="checked"</#if>>${trd.nombre}</input>                    
                                </label>
                            </div>
                        </td>
                    <#assign trdCount = trdCount + 1 />
                    <#if (trdCount % numeroColumnas) == 0 >
                    </tr>
                    </#if>
                </#list>
            </tbody>
        </table>                    
        <br /><br />
        
        <nav class="navbar navbar-default navbar-fixed-bottom text-xs-center hermes-bottombar">
            <button type="submit" class="btn btn-primary">Guardar</button>
        </div>
    </form>
    
    <script type="text/javascript">
        function selectAllTrd(form, button){
            var currentState = $("#selected-all-trd").val() === 'true';
            
            var checkboxes = $(form).find("[name='trd']");
            checkboxes.prop('checked', !currentState);
                
            if(currentState){
                $("#selected-all-trd").val('false');
                $(button).html('${selectAllText}');
            } else {
                $("#selected-all-trd").val('true');                
                $(button).html('${removeAllText}');
            }
        }
    </script>
</div>

<#include "admin-footer.ftl">