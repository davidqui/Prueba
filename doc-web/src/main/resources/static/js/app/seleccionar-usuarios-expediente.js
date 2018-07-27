function agregarUsuario() {

        var usuarioID = $("#destinoUsuario").val();
        if (usuarioID === undefined || $.trim(usuarioID) === "") {
            alert("Debe seleccionar un usuario.");
            return;
        }

        var cargoID = $("#cargoAsignado").val();
        if (cargoID === undefined || $.trim(cargoID) === "") {
            alert("Debe seleccionar un cargo del usuario.");
            return;
        }


        var exp = $("#pin").val();
        exp = 1;
        var permiso = $(".permsiso:checked").val();

        $.ajax({
            method: "POST",
            url: "/expediente/asignar-usuario-expediente/" + exp + "/" + permiso + "/" + usuarioID + "/" + cargoID
        }).always(function (id) {
            console.log("usuario-documento-acta: " + id);
            location.reload();
        });
}

function selectPermiso(value){
    var btn1 = document.getElementById("btn-lectura");
    var btn2 = document.getElementById("btn-escritura");
    if (value == 1){
        btn1.classList.remove('btn-default');
        btn1.classList.add('btn-primary');
        btn2.classList.remove('btn-primary');
        btn2.classList.add('btn-default');
    }else{
        btn1.classList.remove('btn-primary');
        btn1.classList.add('btn-default');
        btn2.classList.remove('btn-default');
        btn2.classList.add('btn-primary');
    }
}

function eliminarUsuario(usuarioID) {
    $.ajax({
        method: "POST",
        url: "/expediente/eliminar-usuario-expediente/" + 1 + "/" + usuarioID      
    }).always(function (id) {
        console.log("usuario-documento-acta: " + id);
        location.reload();
    });
}

function editarUsuarios() {
    var els = document.getElementsByClassName("eliminar");
    Array.prototype.forEach.call(els, function(el) {
        el.style.display = "initial";
    });
}


function editarUsuario(id, nombre){
    console.log(id);
    $.ajax({
        method: "POST",
        url: "/expediente/cargos-usuario/" + id 
    }).always(function (cargos) {
        console.log("nombre "+id +" + "+nombre);
        $("#destinoUsuario_visible2").val(nombre);
        $("#cargoAsignado2 option").remove();
        $("#cargoDestino2 option").remove();
        $.each(cargos, function(index, item) {
            console.log(item.id);
            $("#cargoDestino2").append("<option value="+item.id+">"+item.nombre+"</option>");
            $("#cargoAsignado2").append("<option value=" + item.id + ">" + item.nombre + "</option>");
        });
    });
}


