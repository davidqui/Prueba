<#assign pageTitle = plantilla.nombre!"Plantilla" />
<#assign activePill = "plantilla" />
<#assign deferredJS = "" />
<#import "spring.ftl" as spring />
<#include "admin-header.ftl">
<#include "util-macros.ftl" />
<#assign selectAllText = "Seleccionar todos" />
<#assign removeAllText = "Retirar todos" />
<#assign numeroColumnas = 4 />
<div class="container">
    <h1 class="cus-h1-page-title">${pageTitle}</h1>
    <form action="/admin/plantilla/seleccionar" method="POST" enctype="multipart/form-data" >
        <#if plantilla.id??>
            <input type="hidden" name="id" id="id" value="${plantilla.id}" />
        </#if>

        <#assign checkCount = 0 />
            <table class="table table-bordered">
            <tbody>
                <#if !wildcards??>
                    <#list fieldNames as name >
                        <#if (checkCount % numeroColumnas) == 0 >
                        <tr>
                        </#if>
                            <td>
                                <div class="checkbox">
                                    <label class="checkbox-inline">
                                        <input name="wildcards" type="checkbox" value="${name}">${name}</input>                    
                                    </label>
                                </div>
                            </td>
                        <#assign checkCount = checkCount + 1 />
                        <#if (checkCount % numeroColumnas) == 0 >
                        </tr>
                        </#if>
                    </#list>
                </#if>
                <#if wildcards??>
                    <#list wildcard as wildcards >
                        <#if (checkCount % numeroColumnas) == 0 >
                        <tr>
                        </#if>
                            <td>
                                <div class="checkbox">
                                    <label class="checkbox-inline">
                                        <input name="trd" type="checkbox" value="wildcard.id" <#if controller.has(wildcard.texto, plantilla.wildcards)>checked="checked"</#if>>${name}</input>                    
                                    </label>
                                </div>
                            </td>
                        <#assign checkCount = checkCount + 1 />
                        <#if (checkCount % numeroColumnas) == 0 >
                        </tr>
                        </#if>
                    </#list>
                </#if>            
            </tbody>
        </table>
        <div class="m-y">
             <button id="btnGuardar" type="submit" class="btn btn-primary">Guardar</button>
        </div>
        <script type="text/javascript">
            function selectAllTrd(form, button){
                var currentState = $("#selected-all-wildcards").val() === 'true';

                var checkboxes = $(form).find("[name='trd']");
                checkboxes.prop('checked', !currentState);

                if(currentState){
                    $("#selected-all-wildcards").val('false');
                    $(button).html('${selectAllText}');
                } else {
                    $("#selected-all-wildcards").val('true');                
                    $(button).html('${removeAllText}');
                }
            }
        </script>
    </form>
</div>

<#include "footer.ftl">