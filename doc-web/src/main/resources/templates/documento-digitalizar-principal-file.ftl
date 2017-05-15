<#assign pageTitle = documento.asunto!"Documento" />

<#include "bandeja-header.ftl" />

<h5 class="m-y">Subir archivo de documento principal</h5>

<p class="m-y">
    El documento principal es el oficio que se encuentra al inicio del documento que se está radicando.
</p>

<div class="row m-y">
    <div class="col-md-12">
        <div class="jumbotron">
            <h1>Subir el archivo</h1>
            <form action="/documento/digitalizar-principal-file?doc=${documento.id}&tid=${tid}" method="post" enctype="multipart/form-data">
                <fieldset class="form-group">
                    <label for="archivo">Archivo</label>
                    <input type="file" class="form-control" id="archivo" name="archivo"/>
                </fieldset>                
                <button type="submit" class="btn btn-primary">Subir</button>
            </form>
        </div>
    </div>
</div>

<#include "bandeja-footer.ftl" />