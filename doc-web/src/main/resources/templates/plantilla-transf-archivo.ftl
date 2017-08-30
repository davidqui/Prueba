<#setting number_format="computer">

<#assign pageTitle = "Plantilla de Transferencia de Archivo" />
<#include "gen-macros.ftl">
<#if templatePrefix??>
    <#include templatePrefix + "-header.ftl">
<#else>
    <#include "admin-header.ftl">
</#if>

<div class="container-fluid">
    <h1 class="cus-h1-page-title">${pageTitle}</h1>

    <#if plantilla?? >
    <table class="table table-striped">
        <tbody>
            <tr>
                <td><strong>Nombre de archivo</strong></td>
                <td>${plantilla.nombreArchivo}</td>
                </tr>
            <tr>
                <td><strong>Tamaño de archivo</strong></td>
                <td>${plantilla.tamanyoArchivo}</td>
                </tr>
            <tr>
                <td><strong>Firma MD5</strong></td>
                <td>${plantilla.firmaMD5}</td>
                </tr>
            <tr>
                <td><strong>Cargado por</strong></td>
                <td>${plantilla.usuario.grado} ${plantilla.usuario.nombre}</td>
                </tr>
            <tr>
                <td><strong>Fecha de carga</strong></td>
                <td>${plantilla.fecha?string('dd-MMMM-yyyy hh:mm a')}</td>
                </tr>
            <tr>
                <td colspan="2"><strong><a href="/documento/download/${plantilla.codigoOFS}" >Descargar</a></strong></td>
                </tr>
            </tbody>
        </table>
    <#else> 
    <div class="alert alert-danger" role="alert">
        No hay plantilla activa. Debe agregarse una nueva plantilla.
        </div>
    </#if>

    <form action="/admin/plantilla-transf-archivo" method="POST" enctype="multipart/form-data" >    
        <fieldset class="form-group">	
            <label for="file">
                Plantilla de Office Word (*.docx) 
                </label>
            <input class="form-control" type="file" name="file" id="file">
            </fieldset>

        <div class="m-y">
            <button id="btnGuardar" type="submit" class="btn btn-primary">Guardar</button>
            <a href="/admin/plantilla-transf-archivo" class="btn btn-secondary">Cancelar</a>
            </div>
        </form>

<#if templatePrefix??>
    <#include templatePrefix + "-footer.ftl">
<#else>
    <#include "footer.ftl">
</#if>
