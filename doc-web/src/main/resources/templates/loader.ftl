<div class="div-loader">
    <div class="outer">
        <div class="middle">
            <div class="inner">
                <div class="lds-spinner">
                    <div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div>
                </div>
                <h3>
                    SICDI Procesando ....
                </h3>
            </div>
        </div>
    </div>
</div>

<script>
    function loading(evnt) {
        if (
            evnt.ctrlKey || 
            evnt.shiftKey || 
            evnt.metaKey || 
            (evnt.button && evnt.button == 1)
        ){
            return true;
        }
        $(".div-loader").css({ display: "block" });
        return true;
    }
</script>