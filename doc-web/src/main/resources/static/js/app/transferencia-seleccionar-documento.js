function selectAllDocuments(form, button){
    var currentState = $("#selected-all-trd").val() === 'true';

    var checkboxes = $(form).find("[name='documentos']");
    checkboxes.prop('checked', !currentState);

    if(currentState){
        $("#select-all-documents").val('false');
        $(button).html('${selectAllText}');
        var buttons = $(form).find(".slAllTrd");
        buttons.html('${selectAllText}');   
        var values = $('[id^="selected-all-trd-"]');
        values.val('false'); 
    } else {
        $("#select-all-documents").val('true');                
        $(button).html('${removeAllText}');
        var buttons = $(form).find(".slAllTrd");
        buttons.html('${removeAllText}');
        var values = $('[id^="selected-all-trd-"]');
        values.val('true'); 
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