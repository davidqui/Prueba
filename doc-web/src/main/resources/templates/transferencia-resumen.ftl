<#setting number_format="computer">

<#assign pageTitle = "Selección de expediente" />
<#assign deferredJS = "" />

<#import "spring.ftl" as spring />
<#include "header.ftl" />
<#include "lib/transferencia-archivo_functions.ftl" />
<#assign selectAllText = "Seleccionar todos" />
<#assign removeAllText = "Retirar todos" />
<style>
    i {
        border: solid red;
        border-width: 0 10px 10px 0;
        display: inline-block;
        padding: 10px;
    }
    
    .down {
        transform: rotate(45deg);
        -webkit-transform: rotate(45deg);
    }
    
    .object {
        animation: MoveUpDown 1s linear infinite;
        position: absolute;
        left: 50%;
        bottom: 0;
    }

    @keyframes MoveUpDown {
      0%, 100% {
        bottom: 0;
      }
      50% {
        bottom: 5px;
      }
    }
    
    .card-block-scroll{
        overflow: hidden;
        overflow-y: auto;
    }
    
    p{
        text-align: justify;
    }
</style>
<div class="container-fluid">
<h4>${pageTitle}</h4>
<div class="row">
  <div class="col-xs-6 col-sm-4">
   <div class="card">
    <div class="card-header">
      Documentos
    </div>
    <div class="card-block" style="padding:0;">
         <table class="table" style="margin: 0px;">
            <thead>
                <tr>
                    <th>Radicado</th>
                    <th>Asunto</th>
                </tr>
            </thead>
        </table>
        <div class="card-block-scroll" style="height: 538px;">
            <table class="table">
                <tbody>
                    <#if documentos??>
                        <#list documentos as doc>
                            <td >${doc.asunto!"&lt;Sin asunto&gt;"}</td>
                            <td nowrap name="radicado">${doc.radicado!"&lt;Sin radicado&gt;"}</td>
                        </#list>
                    </#if>
                </tbody>
            </table> 
        </div>
    </div>
  </div>
 </div>
<div class="col-xs-6 col-sm-4">
    <div class="card">
        <div class="card-header">
          Expedientes
        </div>
        <div class="card-block" style="padding:0;">
            <table class="table" style="margin: 0px;">
                <thead>
                    <tr>
                        <th style="width:50%;">Nombre</th>
                        <th>Dependencia</th>
                    </tr>
                </thead>
            </table>
            <div class="card-block-scroll" style="height: 538px;">
                <table class="table">
                    <tbody>
                        <#if expedientes??>
                            <#list expedientes as exp>
                                <tr>
                                    <td>${exp.expNombre!"&lt;Sin asunto&gt;"}</td>
                                    <td>${exp.depId.nombre}</td>
                                </tr>
                            </#list>
                        </#if>
                    </tbody>
                </table> 
          </div>
        </div>
    </div>
</div>
<div class="col-xs-6 col-sm-4">
     <div class="card">
        <div class="card-header" style="text-align: center; padding: 16px; background-color: #f5f5f5; border-bottom: 1px solid #e5e5e5;">
            <h4><b>MY. Robinson Gomez</b></h4>
            <p style="position:relative; margin-top: 25px; margin-bottom: 35px;"><i class="down object"></i></p>
            <h4><b>CP. Alejando Herrera</b></h4>
        </div>
        <div class="card-block card-block-scroll" style="height: 500px;">
        <div>
            <div><span>Número de documentos <b>7</b></span> <span style="float:right;">Número de expedientes <b>7</b></span></div>
        </div>
        </br>
        <p><b>Fecha:</b> 24/08/2018</p>
        <p><b>Estado:</b> Por Aprobar</p>
        <p><b>Justificación:</b> This rather long paragraph consists of exactly 4,000 characters. Spaces, punctuation marks and carriage returns do count. With regard to those spaces, note that if you typically type a double-space at the end of a sentence as I do before beginning a new sentence in the same paragraph, the second space will be deleted automatically, i.e., all but the first space will be ignored. The total number of words in this paragraph is 733 and the average word length is 4.4 characters. The average number of words per sentence is 22. At the end of this paragraph is simply the alphabet starting with the letter A and ending with the letter H, in bold, to make it come out to exactly 4,000 characters and so you'll know when to stop scrolling down in order to see how big 4,000 character is if you want to add a message to the guestbook. Whew. This paragraph begins to repeat at this point, so you can stop reading now if you want to, which I would suggest, or you can choose to read it all again. This rather long paragraph consists of exactly 4,000 characters. Spaces, punctuation marks and carriage returns do count. With regard to those spaces, note that if you typically type a double-space at the end of a sentence as I do before beginning a new sentence in the same paragraph, the second space will be deleted automatically, i.e., all but the first space will be ignored. The total number of words in this paragraph is 733 and the average word length is 4.4 characters. The average number of words per sentence is 22. At the end of this paragraph is simply the alphabet starting with the letter A and ending with the letter H, in bold, to make it come out to exactly 4,000 characters and so you'll know when to stop scrolling down in order to see how big 4,000 character is if you want to add a message to the guestbook. Whew. This paragraph begins to repeat at this point, so you can stop reading now if you want to, which I would suggest, or you can choose to read it all again. This rather long paragraph consists of exactly 4,000 characters. Spaces, punctuation marks and carriage returns do count. With regard to those spaces, note that if you typically type a double-space at the end of a sentence as I do before beginning a new sentence in the same paragraph, the second space will be deleted automatically, i.e., all but the first space will be ignored. The total number of words in this paragraph is 733 and the average word length is 4.4 characters. The average number of words per sentence is 22. At the end of this paragraph is simply the alphabet starting with the letter A and ending with the letter H, in bold, to make it come out to exactly 4,000 characters and so you'll know when to stop scrolling down in order to see how big 4,000 character is if you want to add a message to the guestbook. Whew. This paragraph begins to repeat at this point, so you can stop reading now if you want to, which I would suggest, or you can choose to read it all again. This rather long paragraph consists of exactly 4,000 characters. Spaces, punctuation marks and carriage returns do count. With regard to those spaces, note that if you typically type a double-space at the end of a sentence as I do before beginning a new sentence in the same paragraph, the second space will be deleted automatically, i.e., all but the first space will be ignored. The total number of words in this paragraph is 733 and the average word length is 4.4 characters. The average number of words per sentence is 22. At the end of this paragraph is simply the alphabet starting with the letter A and ending with the letter H, in bold, to make it come out to exactly 4,000 characters and so you'll know when to stop scrolling down in order to see how big 4,000 character is if you want to add a message to the guestbook. Whew. This paragraph begins to repeat at this point, so you can stop reading now if you want to, which I would suggest, or you can choose to read it all again. ABCDEFGH</p>
        </div>
    </div>
</div>
    
<div class="navbar navbar-default navbar-fixed-bottom text-xs-center hermes-bottombar">
    <button type="submit" class="btn btn-primary">
      Aprobar
    </button>
</div>
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
</script>
<#include "footer.ftl" />
