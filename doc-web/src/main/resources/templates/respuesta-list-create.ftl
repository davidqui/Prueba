<#setting number_format="computer">
<#assign pageTitle = "Respuesta" />
<#assign deferredJS = "" />
<#import "spring.ftl" as spring />
<#include "admin-header.ftl">
<#include "util-macros.ftl" />
<#include "gen-macros.ftl" />

<div class="container">
    <div class="row">
        <h1 class="cus-h1-page-title">Crear ${pageTitle} para :  <b>${preguntasCrear.pregunta?capitalize}</b></h1>
        <#if descriptor.description?? >
        <p class="lead">${descriptor.description}</p>
        </#if>
	<@flash/>
        <form action="/admin/respuesta/crear" method="POST" >
            <#list descriptor.createProperties() as p>

        <#if p.name == 'textoRespuesta' >
            <fieldset class="form-group">
                <label for="${p.name}">Texto Respuesta</label>
                <@input p />
                </fieldset>
        <#else>
            <fieldset class="form-group">
                <label for="${p.name}">${p.label}</label>
               <@input p />
                </fieldset>
        </#if>
        </#list>
            <input type="hidden" class="form-control" id="pregunta" name="pregunta" value="${(preguntasCrear.id)!""}"/>
            
            <div class="m-y">
                <button id="btnGuardar" type="submit" class="btn btn-primary">Guardar</button>
                <a href="/admin/respuesta/list/${preguntasCrear.id}" class="btn btn-secondary">Cancelar</a>
                </div>
            
            </form>

        </div>
    <#include "footer.ftl">
