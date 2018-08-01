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
                $("#obsDiv").prepend(hr);
                
                var p = $("<p/>");
                p.html(data.texto);
                $("#obsDiv").prepend(p);
                
                var em = $("<em/>");
                em.text(data.cuando);
                $("#obsDiv").prepend(em);
                
                var strong = $("<strong/>");
                strong.text(data.quien + ", ");
                $("#obsDiv").prepend(strong);
                
                $("#observacion").val('');
            }
        });
    }
});

