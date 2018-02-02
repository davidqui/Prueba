// 2018-02-02 edison.gonzalez@controltechcg.com Issue #147 (SICDI-Controltech)
// feature-147
function subserie(e) {
    var id = $(this).attr("id").substring(5);
    var nombre = $(this).text();

    $("#trdModal .modal-body").empty();
    $("#myModalLabel").text("Selección de subserie");

    $.getJSON("/trd/subseries.json?serieId=" + id, function(subseries) {
        var subseriesList = [];
        $.each(subseries, function(i, item) {
            subseriesList.push('<div><a href="#" id="serie' + item.id + '" class="mark-serie">' + item.codigo + " - " + item.nombre + '</a></div>');
        });

        $("<h5/>", {
            html: nombre
        }).appendTo("#trdModal .modal-body");

        // 2017-03-15 jgarcia@controltechcg.com Issue #21: Cambio de id del botón Regresar de la TRD
        // para evitar confusiones con el botón Regresar de las dependencias.

        $("<a/>", {
            html: "Regresar",
            id: 'btnRegresarSerie'
        }).appendTo("#trdModal .modal-body");
        $("<div/>", {
            html: subseriesList.join("")
        }).appendTo("#trdModal .modal-body");

        // 2017-03-15 jgarcia@controltechcg.com Issue #21: Cambio de id del botón Regresar de la TRD
        // para evitar confusiones con el botón Regresar de las dependencias.
        $("#btnRegresarSerie").click(serie);



        $("#trdModal .modal-body .mark-serie").click(function(e) {
            var id = $(this).attr("id").substring(5);
            var nombre = $(this).text();

            $("#trd").val(id);
            $("#trdNombre").text(nombre);
            $("#trdModal").modal('hide');
        });
    });
};

function serie(e) {
    $("#trdModal .modal-body").empty();
    $("#myModalLabel").text("Selección de serie");
    $.getJSON("/trd/series.json", function(series) {
        var divTagList = [];
        $.each(series, function(i, item) {
            divTagList.push('<div><a href="#" id="serie' + item.id + '" class="mark-serie">' + item.codigo + " - " + item.nombre + '</a></div>');
        });
        $("<div/>", {
            html: divTagList.join("")
        }).appendTo("#trdModal .modal-body");

        $("#trdModal .modal-body .mark-serie").click(subserie);
    });
};
$('#trdModal').on('show.bs.modal', serie);