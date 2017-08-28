<#assign pageTitle = "TRANSFERENCIA DE ARCHIVOS" />
<#assign deferredJS = "" />

<#import "spring.ftl" as spring />
<#include "header.ftl" />

<div class="container-fluid">
    <h4>${pageTitle}</h4>

    <div class="container-fluid">
        <form method="POST">

            <div class="form-group row">
                <label for="origenUsuario" class="col-sm-2 col-form-label text-xs-right">Usuario Origen</label>
                <div class="col-sm-10">
                    <input type="text" name="origenUsuario" class="form-control" value="${usuario.id}"/>
                    </div>
                </div>

            <div class="form-group row">
                <label for="destinoUsuario" class="col-sm-2 col-form-label text-xs-right">Usuario Destino</label>
                <div class="col-sm-10">
                    <input type="text" name="destinoUsuario" class="form-control" value="1147" />
                    </div>
                </div>

            <div class="form-group row">
                <label for="tipoTransferencia" class="col-sm-2 col-form-label text-xs-right">Tipo Transferencia</label>
                <div class="col-sm-10">
                    <input type="radio" name="tipoTransferencia" value="T">Total<br>
                    <input type="radio" name="tipoTransferencia" value="P">Parcial (Recibida previamente)<br>
                    </div>    
                </div>

            <div class="text-center">
                <button type="submit" class="btn btn-success btn-lg">Aceptar</button>
                </div>

            </form>
        </div>

    </div>
<#include "footer.ftl" />
