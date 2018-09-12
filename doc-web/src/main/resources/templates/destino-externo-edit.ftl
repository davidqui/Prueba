<#setting number_format="computer">
<#assign pageTitle = "Observación por defecto" />
<#-- <#assign mode = observacionDefecto.mode!"" /> -->
<#assign deferredJS = "" />
<#import "spring.ftl" as spring />
<#include "admin-header.ftl">
<#include "util-macros.ftl" />

<div class="container">
    <div class="row">
        <h1 class="cus-h1-page-title">${pageTitle}</h1>
        <form action="/admin/destino-externo/actualizar" method="POST" enctype="multipart/form-data" >
            <input type="hidden" id="id" name="id" value="${destinoExterno.id}" />
            <fieldset class="form-group">
                <label for="textoObservacion">Nombre</label>
                <input type="text" class="form-control" id="nombre" name="nombre" value="${(destinoExterno.nombre)!""}" style="text-transform: uppercase;"/>
            </fieldset>
            <fieldset class="form-group">
                <label for="textoObservacion">Sigla</label>
                <input type="text" class="form-control" id="sigla" name="sigla" value="${(destinoExterno.sigla)!""}" style="text-transform: uppercase;"/>
            </fieldset>
            <fieldset class="form-group">
                <label for="tipo">Tipo:</label>
                <div style="padding-left: 20px;">
                        <div class="radio">
                            <label><input type="radio" name="tipo" value="5" <#if destinoExterno.tipo?? && destinoExterno.tipo == 5>checked</#if>>Ejercito</label>
                         </div>
                        <div class="radio">
                          <label><input type="radio" name="tipo" value="3" <#if destinoExterno.tipo?? && destinoExterno.tipo == 3>checked</#if>>Fuerza aérea</label>
                        </div>
                        <div class="radio">
                          <label><input type="radio" name="tipo" value="4" <#if  destinoExterno.tipo?? && destinoExterno.tipo == 4>checked</#if>>Armada</label>
                        </div>
                        <div class="radio">
                          <label><input type="radio" name="tipo" value="6" <#if  destinoExterno.tipo?? && destinoExterno.tipo == 6>checked</#if>>Policía Nacional</label>
                        </div>
                        <div class="radio">
                          <label><input type="radio" name="tipo" value="1" <#if  destinoExterno.tipo?? && destinoExterno.tipo == 1>checked</#if>>Estatal</label>
                        </div>
                        <div class="radio">
                          <label><input type="radio" name="tipo" value="2" <#if  destinoExterno.tipo?? && destinoExterno.tipo == 2>checked</#if>>Privada</label>
                        </div>
                        <div class="radio">
                          <label><input type="radio" name="tipo" value="7" <#if  destinoExterno.tipo?? && destinoExterno.tipo == 7>checked</#if>>Otros</label>
                        </div>
                    </div>
                </div>
            </fieldset>
            <div class="m-y">
                <button id="btnGuardar" type="submit" class="btn btn-primary">Guardar</button>
                <a href="/admin/destino-externo" class="btn btn-secondary">Cancelar</a>
                </div>
            </form>
        </div>
    </div>
<#include "footer.ftl">