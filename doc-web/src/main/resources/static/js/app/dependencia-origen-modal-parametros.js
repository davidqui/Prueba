// 2018-01-29 edison.gonzalez@controltechcg.com Issue #147 (SICDI-Controltech)
// feature-147
var depPadre = -1;
var depStack = new Array();

function loadSubDependencia(id) {
    if (depStack.length === 0) {
        depStack.push(id);
    } else if (depStack.length > 0 && depStack[depStack.length - 1] !== id) {
        depStack.push(id);
    }

    var nombre = $(this).attr("nombre");
    $("#dependenciaOrigenModal .modal-body").empty();
    $("#dependenciaOrigenModalLabel").text(nombre);

    $.getJSON("/dependencias/dependencias.json?id=" + id, function(deps) {
        modalBody = $("#dependenciaOrigenModal .modal-body");
        $("<a/>", {
            html: "Regresar",
            id: 'btnRegresar',
            class: 'btn btn-secondary btn-sm'
        }).appendTo(modalBody);

        if (id !== null && id !== 'undefined') {
            $("#btnRegresar").click(function() {
                if (depStack.length > 1) {
                    // 2017-02-13 jgarcia@controltechcg.com Issue #68
                    // Se corrige el manejo del splice() en el arreglo de pila de dependencias.
                    depStack.splice(depStack.length - 1, 1);
                } else {
                    depStack = new Array();
                }

                if (depStack.length > 0) {
                    prev = depStack[depStack.length - 1];
                    loadSubDependencia(prev);
                } else {
                    loadDependencia();
                }
            });

        } else {
            $("#btnRegresar").click(loadDependencia);
        }

        // Si no hay resultados
        if (deps.length === 0) {
            msg = $("<div/>", {
                html: "<br/><h6>No tiene dependencias subordinadas</h6>"
            });
            msg.appendTo(modalBody);
        }

        table = $("<table/>", {
            class: "table"
        });
        $.each(deps, function(i, item) {
            tr = $("<tr/>");
            tr.appendTo(table);

            linkDiv = '<a href="#" nombre="' + item.nombre + '" id="dep' + item.id + '" class="btn btn-primary btn-sm mark-sel"><span class="hidden-md-down">Seleccionar</span><span class="hidden-lg-up">S</span></a> <strong>' + item.nombre + '</strong>&nbsp;&nbsp;&nbsp;<a href="#" id="dep' + item.id + '" nombre="' + item.nombre + '" class="mark-dep">Subdependencias...</a>';
            td = $("<td/>", {
                html: linkDiv
            });
            td.appendTo(tr);

            buttonDiv = '';
            td = $("<td/>", {
                html: buttonDiv
            });
            td.appendTo(tr);
        });

        table.appendTo(modalBody);

        $("#dependenciaOrigenModal .modal-body .mark-dep").click(function() {
            var id = $(this).attr("id").substring(3);
            loadSubDependencia(id);
        });

        $("#dependenciaOrigenModal .modal-body .mark-sel").click(function(e) {
            var id = $(this).attr("id").substring(3);
            var nombre = $(this).attr("nombre");
            $("#dependenciaOrigen").val(id);
            $("#dependenciaOrigenNombre").text(nombre);
            $("#dependenciaOrigenDescripcion").val(nombre);
            $("#dependenciaOrigenModal").modal('hide');
        });
    });
};

function loadDependencia(e) {
    $("#dependenciaOrigenModalLabel").text("Dependencias");
    $("#dependenciaOrigenModal .modal-body").empty();

    $.getJSON("/dependencias/dependencias.json", function(deps) {
        table = $("<table/>", {
            class: "table"
        });

        $.each(deps, function(i, item) {
            tr = $("<tr/>");
            tr.appendTo(table);

            linkDiv = '<a href="#" nombre="' + item.nombre + '" id="dep' + item.id + '" class="btn btn-primary btn-sm mark-sel"><span class="hidden-md-down">Seleccionar</span><span class="hidden-lg-up">S</span></a> <strong>' + item.nombre + '</strong>&nbsp;&nbsp;&nbsp;<a href="#" id="dep' + item.id + '" nombre="' + item.nombre + '" class="mark-dep">Subdependencias...</a>';
            td = $("<td/>", {
                html: linkDiv
            });
            td.appendTo(tr);

            buttonDiv = '';
            td = $("<td/>", {
                html: buttonDiv
            });
            td.appendTo(tr);
        });

        modalBody = $("#dependenciaOrigenModal .modal-body");
        table.appendTo(modalBody);

        $("#dependenciaOrigenModal .modal-body .mark-dep").click(function() {
            var id = $(this).attr("id").substring(3);
            loadSubDependencia(id);
        });

        $("#dependenciaOrigenModal .modal-body .mark-sel").click(function(e) {
            id = $(this).attr("id").substring(3);
            nombre = $(this).attr("nombre");
            $("#dependenciaOrigen").val(id);
            $("#dependenciaOrigenNombre").text(nombre);
            $("#dependenciaOrigenDescripcion").val(nombre);
            $("#dependenciaOrigenModal").modal('hide');
        });
    });
};
$('#dependenciaOrigenModal').on('show.bs.modal', loadDependencia);