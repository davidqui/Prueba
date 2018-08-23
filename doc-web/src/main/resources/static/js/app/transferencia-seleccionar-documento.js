function selectAllDocuments(form, button){
    var currentState = $("#selected-all-documentos").val() === 'true';
    
    $("[id='info-doc']").each(function(){
        if ($(this).css('display') != 'none') {
            $(this).find("[name='documentos']").prop('checked', currentState);
        }
    });
    
    $(".subTrd").each(function(){
        if ($(this).css('display') != 'none') {
            $(this).find("[id='trd-documentos']").prop('checked', currentState);
        }
    });
    
    $(".trdPadre").each(function(){
        if ($(this).css('display') != 'none') {
            $(this).find("[id='trd-documentos']").prop('checked', currentState);
        }
    });
    
    if(currentState){
        $("#selected-all-documentos").val('false');
        $(button).html('Retirar todos');  
    } else {
        $("#selected-all-documentos").val('true');                
        $(button).html('Seleccionar todos');
    }
    resetCheckTrd();
}

function selectdivCheckbox(idDiv, idCheck){
    var currentState = idCheck.checked;
    
    $("#"+idDiv).find("#info-doc").each(function(){
        if ($(this).css('display') != 'none') {
            $(this).find("[name='documentos']").prop('checked', currentState);
        }
    });
    
    $("#"+idDiv).find(".subTrd").each(function(){
        if ($(this).css('display') != 'none') {
            $(this).find("[id='trd-documentos']").prop('checked', currentState);
        }
    });
    resetCheckTrd();
}


function finderDocument(){
    var criterio = $("#documentos-buscar").val();
    $('.trdPadre').hide();
    $('.trdPadre').each(function(){
        console.log("TRD "+this);
       $(this).find(".subTrd").hide();
       var counterSubTrds = 0;
       $(this).find(".subTrd").each(function(){
           var counterDocuments = 0;
           $(this).find("[name='info-doc']").hide();
           $(this).find("[name='info-doc']").each(function(){
               var asunto = $(this).find("[name='radicado']");
                var radicado = $(this).find("[name='asunto']");
                if ($(asunto).text().toUpperCase().indexOf(criterio.toUpperCase()) != -1 || 
                        $(radicado).text().toUpperCase().indexOf(criterio.toUpperCase()) != -1) {
                    counterDocuments++;
                    $(this).show();
                }
           });
            if (counterDocuments > 0) {
                $(this).show();
                counterSubTrds++;
            }
       });
        if (counterSubTrds > 0) {
            $(this).show();
        }
    });
    resetCheckTrd();
}


function resetCheckTrd(){
    var flagTodos = true;
    var counterDoc = 0;
    $('.trdPadre').each(function(){
        var flagSelect = true;
        if ($(this).css('display') != 'none') {
            $(this).find(".subTrd").each(function(){
                var aux = $(this).find("[name='documentos']:checked").length
                counterDoc += aux;
                if ($(this).css('display') != 'none') {
                    if ( aux === $(this).find("[name='documentos']").length) {
                       $(this).find("#trd-documentos").first().prop('checked', true);
                    }else{
                        flagSelect = false;
                        $(this).find("#trd-documentos").first().prop('checked', false);
                    }
                }
            });
            if (flagSelect) {
                $(this).find("#trd-documentos").first().prop('checked', true);
            }else{
                flagTodos = false;
                $(this).find("#trd-documentos").first().prop('checked', false);
            }
        }   
    });
    console.log("QUE PASA", flagTodos);
    $("#counterDoc").html(counterDoc+" documentos")
    if(flagTodos){
        $("#selected-all-documentos").val('false');
        $("#select-all-documents").html('Retirar todos');  
    } else {
        $("#selected-all-documentos").val('true');                
        $("#select-all-documents").html('Seleccionar todos');
    }
}