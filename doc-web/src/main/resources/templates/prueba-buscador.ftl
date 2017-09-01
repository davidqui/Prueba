<#setting number_format="computer">

<#assign pageTitle = "Prueba Buscador" />
<#assign deferredJS = "" />

<#import "spring.ftl" as spring />
<#include "header.ftl" />

<div class="container-fluid">
    <h4>${pageTitle}</h4>

    <div class="container-fluid">
        <form method="POST">
            <div class="form-group row">

                <div class="form-group row">
                    <label for="destinoUsuario" class="col-sm-2 col-form-label text-xs-right">Usuario Destino</label>
                    <div class="col-sm-10">
                        <div class="input-group">
                            <input type="text" id="destinoUsuario_visible" name="destinoUsuario_visible" class="form-control" value="" disabled />
                            <div class="input-group-btn">
                                <button type="button" class="btn btn-primary" onclick="openFinderWindow()">Buscar</button>
                                </div> 
                            </div> 
                        </div>
                    <input type="hidden" id="destinoUsuario" name="destinoUsuario" value="" />
                    </div>            
            </form>
        </div>
    </div>

<div id="resultado">
    </div>

<script src="/js/lib/finder.js"></script>

<#include "footer.ftl" />
