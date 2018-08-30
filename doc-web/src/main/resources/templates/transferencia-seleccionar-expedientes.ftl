<#setting number_format="computer">

<#assign pageTitle = "Selección de expediente" />
<#assign deferredJS = "" />

<#import "spring.ftl" as spring />
<#include "header.ftl" />
<#include "lib/transferencia-archivo_functions.ftl" />
<#assign selectAllText = "Seleccionar todos" />
<#assign removeAllText = "Retirar todos" />
<style>
    .card{
        border: none;
        border-left: 1px solid #e5e5e5;
        margin: 0 !important;   
    }
    
    .card-header{
        height: 50px;
        padding: 10px;
        background-color: white;
    }
    
    .card-body{
        padding-left: 30px;
    }
    
    .span-badge{
        font-size: 14px;
        padding: 4px 6px;
        background-color: white;
        color: #0275d8;
        margin-left: 10px;
        border-radius: 20px;
        font-weight: bold;
    }
</style>
<div class="container-fluid">
<h4>${pageTitle}</h4>
<div id="accordion">
  <#assign hasAllSelected = false />            
  <button id="select-all-exp" name="select-all-exp" type="button" class="btn btn-default btn-sm slAllTrd" onclick="return selectAllExpedientes(this.form, this);">
    <#if hasAllSelected >
        ${removeAllText}
    <#else>
        ${selectAllText}
    </#if>
  </button>
  <input id="selected-all-expedientes" name="selected-all-expedientes" type="hidden" value="true" />
<input class="form-control" type="text" id="expediente-buscar" placeholder="Buscar por nombre" title="Buscar" style="margin-top: 30px;">
<form method="POST" id="formExpediente">
    <table class="table" id="table_expediente">
        <thead>
            <tr>
                <th></th>
                <th>Nombre</th>
                <th>Fecha</th>
                <th>Dependencia</th>
                <th>TRD principal</th>
            </tr>
        </thead>
        <tbody>
        <#list expedientes as exp>
            <#assign vclasses = "" />
            <#if !exp.indCerrado?? || exp.indCerrado == true >
                <#assign vclasses = "text-danger" />
            </#if>
             <tr 
                id="table-tr"
                <#if controller.hasExpediente(exp.expId, expedientesEnOtrasTransferencias)>
                 class="bd-popover"
                 style="background-color: #00000047; cursor: not-allowed;"
                 data-toggle="popover" data-trigger="hover" data-placement="top" title="Expediente en Transferencia" data-content="Este expediente ya se encuentra asociado a una transferencia, desasócielo para poder agregarlo a este."
               </#if>
            >
                <td>
                    <input 
                        type="checkbox"  
                        onclick="onSelectCounter(true);" 
                        value="${exp.expId}"
                        <#if controller.hasExpediente(exp.expId, expedientesSeleccionados)>checked="checked"</#if>
                        <#if controller.hasExpediente(exp.expId, expedientesEnOtrasTransferencias)>
                            disabled
                        <#else>
                            name="expedientes"
                        </#if>
                        />
                </td>
                <td class="${vclasses}"><a href="/expediente/listarDocumentos?expId=${exp.expId}" target="_blank">${exp.expNombre!""}</a></td>
                <td class="${vclasses}">${exp.fecCreacion?string('yyyy-MM-dd')}</td>
                <td class="${vclasses}">${exp.depId.nombre!""}</td>
                <td class="${vclasses}">${exp.trdIdPrincipal.nombre!""}</td>
            </tr>
        </#list>
        </tbody>
    </table>
    <div class="navbar navbar-default navbar-fixed-bottom text-xs-center hermes-bottombar">
        <button type="submit" class="btn btn-primary" onclick="loading(event);">
          Transferir <span class="span-badge" id="counterExp">0 expedientes</span>
        </button>
    </div>
  </form>
<div>
<script>
    function selectAllExpedientes(form, button){
        var currentState = $("#selected-all-expedientes").val() === 'true';

        $("[id='table-tr']").each(function(){
            if ($(this).css('display') != 'none') {
                $(this).find("[name='expedientes']").prop('checked', !currentState);
            }
        });

        if(currentState){
            $("#selected-all-expedientes").val('false');
            $(button).html('${selectAllText}');  
        } else {
            $("#selected-all-expedientes").val('true');                
            $(button).html('${removeAllText}');
        }
        onSelectCounter(false);
    }
        
    function onSelectCounter(changes){
        var aux = $("[name='expedientes']:checked").length;
        var aux2 = 0;
        var aux3 = 0;
        
        if(changes){
            $("[id='table-tr']").each(function(){
                if ($(this).css('display') != 'none') {
                    aux2 = $(this).find("[name='expedientes']").length;
                    aux3 = $(this).find("[name='expedientes']:checked").length;
                }
            });
            if(aux2 !== aux3 && changes){
                $("#selected-all-expedientes").val('false');
                $("#select-all-exp").html('${selectAllText}');
            } else {
                $("#selected-all-expedientes").val('true');                
                $("#select-all-exp").html('${removeAllText}');
            }
        }
        $("#counterExp").html(aux+" expedientes");
    }
        
    $(document).ready(function(){
        $("#expediente-buscar").on("keyup", function(){
          var value = $(this).val().toLowerCase();
          $("#table_expediente tr").filter(function() {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
          });
          onSelectCounter(true);
        });
    });   
    $( document ).ready(function() {
        onSelectCounter();
    });
</script>
<#include "footer.ftl" />
