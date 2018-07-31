<#assign pageTitle = "Subseries - Expediente " + expediente.expNombre />
<#include "header.ftl">

<#--
    2018-05-21 jgarcia@controltechcg.com Issue #170 (SICDI-Controltech)
    feature-170: Textos para el botón de selección para todas las subseries.
    Constante del número de columnas a presentar.
-->
<#assign selectAllText = "Seleccionar todos" />
<#assign removeAllText = "Retirar todos" />
<#assign numeroColumnas = 6 />

<div class="container-fluid">
    <h1 class="cus-h1-page-title">${pageTitle}</h1>

    <form method="POST">
        <div class="row" style="padding:16px;">
            <#assign hasAllSelected = controller.hasAllSubseriesSelected(trdsPreseleccionadas, trds) />            
            <button id="select-all-trd" name="select-all-trd" type="button" class="btn btn-default btn-sm slAllTrd" onclick="return selectAllTrd(this.form, this);">
            <#if hasAllSelected >
                ${removeAllText}
            <#else>
                ${selectAllText}
            </#if>
            </button>
            <input id="selected-all-trd" name="selected-all-trd" type="hidden" value="${hasAllSelected?c}" />
        </div>
        </br>
        
        <#list trds as trdP >
            <h4>${trdP.nombre} 
                <#assign hasAllSelectedPadre = controller.hasAllSubseriesSelectedByPadre(trdsPreseleccionadas, trds, trdP.id) />            
                <button id="select-all-trd" name="select-all-trd" type="button" class="btn btn-default btn-sm slAllTrd" onclick="return selectAllTrdPadre(this.form, this, ${trdP.id});">
                <#if hasAllSelectedPadre >
                    ${removeAllText}
                <#else>
                    ${selectAllText}
                </#if>
            </h4>
            <input id="selected-all-trd-${trdP.id}" name="selected-all-trd" type="hidden" value="${hasAllSelectedPadre?c}" />
            <#assign trdCount = 0 />
            <table class="table table-bordered">
                <tbody>
                    <#list trdP.subs as trd >
                        <#if (trdCount % numeroColumnas) == 0 >
                        <tr>
                        </#if>
                            <td>
                                <div class="checkbox">
                                    <label class="checkbox-inline">
                                        <input name="trd" type="checkbox" class="trd-${trdP.id}" value="${trd.id}" <#if controller.has(trd.id, trdsPreseleccionadas)>checked="checked"</#if>>${trd.nombre}</input>                    
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
        </#list>
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
                var buttons = $(form).find(".slAllTrd");
                buttons.html('${selectAllText}');   
            } else {
                $("#selected-all-trd").val('true');                
                $(button).html('${removeAllText}');
                var buttons = $(form).find(".slAllTrd");
                buttons.html('${removeAllText}'); 
            }
                
        }
            
        function selectAllTrdPadre(form, button, id){
            var currentState = $("#selected-all-trd-"+id).val() === 'true';
            
            var checkboxes = $(form).find(".trd-"+id);
            checkboxes.prop('checked', !currentState);
                
            if(currentState){
                $("#selected-all-trd-"+id).val('false');
                $(button).html('${selectAllText}');
            } else {
                $("#selected-all-trd-"+id).val('true');                
                $(button).html('${removeAllText}');
            }
        }
    </script>
</div>

<#include "admin-footer.ftl">