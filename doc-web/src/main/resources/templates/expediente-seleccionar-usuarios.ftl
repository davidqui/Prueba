
<#assign pageTitle = "Crear Expediente"/>
<#include "bandeja-header.ftl">
<#include "gen-arbol-trd.ftl">
<#import "spring.ftl" as spring />


<div class="container-fluid" style="max-width: 800px; margin-left: inherit;">
    
    <form action="/expediente/crear" method="POST" enctype="multipart/form-data" >
        <fieldset class="form-group">
            <label for="textoObservacion">Texto</label>
            <input type="text" class="form-control" id="nombre" name="nombre" value="${(expediente.nombre)!""}"/>
        </fieldset>
    </form>
<script src="/js/app/gen-arbol.js"></script>
<script src="/js/jstree.min.js"></script>

<script type="text/javascript">
    validarArbol("#arbol_list_trd",false);
</script>

<#include "bandeja-footer.ftl">
