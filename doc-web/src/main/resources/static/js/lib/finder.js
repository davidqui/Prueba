function openFinderWindow() {
    var w = 800;
    var h = 600;

    var dualScreenLeft = window.screenLeft !== undefined ?
            window.screenLeft : screen.left;
    var dualScreenTop = window.screenTop !== undefined ?
            window.screenTop : screen.top;

    var width = window.innerWidth ? window.innerWidth :
            document.documentElement.clientWidth ?
            document.documentElement.clientWidth : screen.width;
    var height = window.innerHeight ? window.innerHeight :
            document.documentElement.clientHeight ?
            document.documentElement.clientHeight : screen.height;

    var left = ((width / 2) - (w / 2)) + dualScreenLeft;
    var top = ((height / 2) - (h / 2)) + dualScreenTop;

    window.open("/transferencia-archivo/formulario-busqueda", "_blank",
            "width=" + w + ", height=" + h + ", top=" + top + ", left=" + left
            + ",location=no,menubar=no,resizable=no,status=no,titlebar=no,toolbar=no");
}

function setFindResult(id) {
    $("#destinoUsuario_visible").val("Nombre");
    $("#destinoUsuario").val(id);
}

function selectFindResult(id) {
    opener.setFindResult(id);
    window.close();
}