$("#obsButton").click(function(event){
    event.preventDefault();
    var expId = $("#expId").val();
    console.log("expId? "+expId);
    var expObs = $.trim( $("#observacion").val() );
    if( expObs.length <= 0 ){
        alert("Debe ingresar una observación");
    }else{
        console.log('pasando');
        $.ajax({
            type: "POST",
            url: "/expediente/observacion?expId="+expId,
            data: $("#obsForm").serialize(),
            success: function(data) {
                var hr = $("<hr/>");
                hr.appendTo("#obsDiv");
                var strong = $("<strong/>");
                strong.text(data.quien + ", ");
                strong.appendTo("#obsDiv");
                var em = $("<em/>");
                em.text(data.cuando);
                em.appendTo("#obsDiv");
                var p = $("<p/>");
                p.html(data.texto);
                p.appendTo("#obsDiv");
                $("#observacion").val('');
            }
        });
    }
});

