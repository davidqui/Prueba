<!-- 2017-04-25 jgarcia@controltechcg.com Issue #53 (SICDI-Controltech): Se asigna formato numerico para manejo de los miles -->
<#setting number_format="computer">

<#assign pageTitle = "Reasignar" />

<#include "bandeja-header.ftl" />

<form action="/documento/reasignar?pin=${pin}" method="POST">
<#if superdependencias?? && (superdependencias?size > 0) >
    <div class="card">
        <div class="card-header">
            <strong>Dependencias padre</strong>
        </div>
        <div class="card-block">
            <#list superdependencias as x>
                <label class="c-input c-radio">
                    <input type="radio" name="depId" value="${x.id}">
                    <span class="c-indicator"></span>
                    ${x}
                </label>
                <br/>
            </#list>
            <div class="m-y">
                <fieldset class="form-group">
                    <textarea name="observacion" placeholder="Escriba un comentario para el nuevo asignado..." class="form-control"></textarea>
                </fieldset>
            </div>
            <button type="submit" class="btn btn-success btn-sm">Asignar</button>
            <a href="/proceso/instancia?pin=${pin}" class="btn btn-link btn-sm">Cancelar</a>
        </div>
    </div>
</#if>
<#if dependenciasHermanas?? && (dependenciasHermanas?size > 0) >
    <div class="card">
        <div class="card-header">
            <strong>Dependencias hermanas</strong>
        </div>
        <div class="card-block">
            <#list dependenciasHermanas as x>
                <label class="c-input c-radio">
                    <input type="radio" name="depId" value="${x.id}">
                    <span class="c-indicator"></span>
                    ${x}
                </label>
                <br/>
            </#list>
            <div class="m-y">
                <fieldset class="form-group">
                    <textarea name="observacion" placeholder="Escriba un comentario para el nuevo asignado..." class="form-control"></textarea>
                </fieldset>
            </div>
            <button type="submit" class="btn btn-success btn-sm">Asignar</button>
            <a href="/proceso/instancia?pin=${pin}" class="btn btn-link btn-sm">Cancelar</a>
        </div>
    </div>
</#if>
<#if subdependencias?? && (subdependencias?size > 0) > 
    <div class="card">
        <div class="card-header">
            <strong>Subdependencias</strong>
        </div>
        <div class="card-block">
            <#list subdependencias as x>
                <label class="c-input c-radio">
                    <input type="radio" name="depId" value="${x.id}">
                    <span class="c-indicator"></span>
                    ${x}
                </label>
                <br/>
            </#list>
            <div class="m-y">
                <fieldset class="form-group">
                    <textarea name="observacion" placeholder="Escriba un comentario para el nuevo asignado..." class="form-control"></textarea>
                </fieldset>
            </div>
            <button type="submit" class="btn btn-success btn-sm">Asignar</button>
            <a href="/proceso/instancia?pin=${pin}" class="btn btn-link btn-sm">Cancelar</a>
        </div>
    </div>
</#if>
<#if dependencias?? && (dependencias?size > 0) >
    <div class="card">
        <div class="card-header">
            <strong>Dependencias</strong>
        </div>
        <div class="card-block">
            <#list dependencias as x>
                <label class="c-input c-radio">
                    <input type="radio" name="depId" value="${x.id}">
                    <span class="c-indicator"></span>
                    ${x}
                </label>
                <br/>
            </#list>
            <div class="m-y">
                <fieldset class="form-group">
                    <textarea name="observacion" placeholder="Escriba un comentario para el nuevo asignado..." class="form-control"></textarea>
                </fieldset>
            </div>
            <button type="submit" class="btn btn-success btn-sm">Asignar</button>
            <a href="/proceso/instancia?pin=${pin}" class="btn btn-link btn-sm">Cancelar</a>
        </div>
    </div>
</#if>
<#if usuarios?? && (usuarios?size > 0) >
    <div class="card">
        <div class="card-header">
            <strong>Usuarios de mi dependencia</strong>
        </div>
        <div class="card-block">
            <#list usuarios as x>
                <label class="c-input c-radio">
                    <input type="radio" name="usuId" value="${x.id}">
                    <span class="c-indicator"></span>
                    ${x}
                </label>
                <br/>
            </#list>
            <div class="m-y">
                <fieldset class="form-group">
                    <textarea name="observacion" placeholder="Escriba un comentario para el nuevo asignado..." class="form-control"></textarea>
                </fieldset>
            </div>
            <button type="submit" class="btn btn-success btn-sm">Asignar</button>
            <a href="/proceso/instancia?pin=${pin}" class="btn btn-link btn-sm">Cancelar</a>
        </div>
    </div>
</#if>
</form>

<#include "bandeja-footer.ftl" />