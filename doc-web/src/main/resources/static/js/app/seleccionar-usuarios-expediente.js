
        .function agregarUsuario(expId) {

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

        var permiso = $(".permsiso:checked").val();

        $.ajax({
            method: "POST",
            url: "/expediente/asignar-usuario-expediente/" + expId + "/" + permiso + "/" + usuarioID + "/" + cargoID
        }).then(function() {
            location.reload();
          }, function(message) {
            console.log("mensaje", message);
            $('#info-modal').modal('show');
            $('#title-modal').html("Advertencia");
            $('#modal-body-info').html("<h5>"+message.responseText+"</h5>");
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

function selectPermiso2(value){
    var btn1 = document.getElementById("btn-lectura2");
    var btn2 = document.getElementById("btn-escritura2");
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

function eliminarUsuario(usuarioID, expId) {
    $.ajax({
        method: "POST",
        url: "/expediente/eliminar-usuario-expediente/" + expId + "/" + usuarioID    
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


function editarUsuario(id, nombre, expId, permiso){
    console.log(id);
    $.ajax({
        method: "POST",
        url: "/expediente/cargos-usuario/" + id 
    }).always(function (cargos) {
        console.log("nombre "+id +" + "+nombre);
        $("#destinoUsuario_visible2").val(nombre);
        $("#cargoAsignado2 option").remove();
        $("#cargoDestino2 option").remove();
        $("#expUsuario").val(expId);
        
        radiobtn = document.getElementById("radio2"+permiso);
        radiobtn.checked = true;
        selectPermiso2(permiso);
        $.each(cargos, function(index, item) {
            console.log(item.id);
            $("#cargoDestino2").append("<option value="+item.id+">"+item.nombre+"</option>");
            $("#cargoAsignado2").append("<option value=" + item.id + ">" + item.nombre + "</option>");
        });
    });
}

function editarUsuarioCreador(nombre){
    $("#actual_usuario_administrador").val(nombre);
}

function editarUsuarioPost(expId) {
        var usuarioID = $("#expUsuario").val();
        if (usuarioID === undefined || $.trim(usuarioID) === "") {
            alert("Debe seleccionar un usuario.");
            return;
        }

        var cargoID = $("#cargoAsignado2").val();
        if (cargoID === undefined || $.trim(cargoID) === "") {
            alert("Debe seleccionar un cargo del usuario.");
            return;
        }

        var permiso = $(".permsiso:checked").val();

        $.ajax({
            method: "POST",
            url: "/expediente/editar-usuario-expediente/" + expId + "/" + permiso + "/" + usuarioID + "/" + cargoID
        }).always(function (id) {
            location.reload();
        });
}

/**
 * Establece los valores del usuario indicado. 
 * @param {type} id Id del usuario.
 * @returns {undefined}
 */
function setFindResult2(id) {
    $.ajax({
        method: "POST",
        url: "/rest/usuario/buscar/activo/id/" + id
    }).done(function (busquedaDTO) {
        if (!busquedaDTO.ok) {
            $("#destinoUsuario_visible").val(busquedaDTO.mensajeBusqueda);
            return;
        }

        descripcion = busquedaDTO.grado + " " + busquedaDTO.nombre + " ["
                + busquedaDTO.clasificacionNombre + "]";

        $("#destinoUsuario_visible").val(descripcion);
        $("#destinoUsuario").val(busquedaDTO.id);
        $("#divCargoDestino").show();
        
        $("#cargoAsignado option").remove();
        $("#cargoDestino option").remove();
        $.each(busquedaDTO.cargosDestino, function(index, item) {
            console.log(item.id);
            $("#cargoDestino").append("<option value="+item.id+">"+item.nombre+"</option>");
            $("#cargoAsignado").append("<option value=" + item.id + ">" + item.nombre + "</option>");
        });
    });
}

function limpiarModales(){
    $("#cargoAsignado option").remove();
    $("#cargoDestino option").remove();
    $("#destinoUsuario").val("");
    $("#destinoUsuario_visible").val("");
}

function cambiarUsuarioCreador(expId){
    var usuarioID = $("#destinoUsuario3").val();
    if (usuarioID === undefined || $.trim(usuarioID) === "") {
        alert("Debe seleccionar un usuario.");
        return;
    }
    var result = confirm("Esta seguro de cambiar el usuario Creador?")
    if (result){
        $.ajax({
            method: "POST",
            url: "/expediente/cambiar-creador/" + expId+"/" + usuarioID
        }).then(function() {
            location.reload();
          }, function(message) {
            $('#cambiarUsuarioCreador').modal('hide');
            $('#info-modal').modal('show');
            $('#title-modal').html("Advertencia");
            $('#modal-body-info').html("<h5>"+message.responseText+"</h5>");
            $('#info-modal').modal('show'); 
        });
    }else{
        $('#cambiarUsuarioCreador').modal('hide');
    }
}
