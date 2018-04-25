<#setting number_format="computer">
<#assign pageTitle = "Carpetas"/>
<#include "bandeja-header.ftl">

<div class="container-fluid">
    </br>
    <#--
        2018-03-07 edison.gonzalez@controltechcg.com Issue #151 (SICDI-Controltech) feature-151:
        Modificación en la pantalla de presentación de registros de archivo, para realizar
        el filtro por el cargo que archiva el documento.
    -->
    <form action = "/expediente/carpeta" method="POST">
        <div class="form-inline">
            <input type="hidden" name="sub" id="sub" value="${(sub)!""}" />
            <input type="hidden" name="ser" id="ser" value="${(ser)!""}" />
            <label for="cargoIdFirma" class="col-sm-2 text-xs-right">Cargo con el cual se filtrara el archivo</label>
            <select class="form-control" id="cargoFiltro" name="cargoFiltro">
                <#if cargosXusuario??>
                    <#list cargosXusuario as cla>
                        <#if cla.id?string == ((cargoFiltro)!"")?string >
                <option value="${cla.id}" selected="selected">${cla.nombre}</option>
                        <#else>
                <option value="${cla.id}">${cla.nombre}</option>
                        </#if>
                    </#list>
                </#if>    
                </select>
            <button type="submit" class="btn btn-primary">Buscar</button>
            </div>
        </form>
    </br>
    </br>
    <#if retornaSerie??>
    <#--
        2018-04-25 jgarcia@controltechcg.com Issue #151 (SICDI-Controltech)
        feature-151: Cambio en presentación de enlace "Regresar" para que tenga
        la apariencia de un botón.
    -->
    <a class="btn btn-primary" href="carpeta?ser=${retornaSerie}&cargoFiltro=${cargoFiltro!"0"}">Regresar</a>
    </#if>
    </br>
    </br>

    <#--
        2018-04-25 jgarcia@controltechcg.com Issue #151 (SICDI-Controltech)
        feature-151: Asignación de variables de existencia de los componentes
        de modelo para hacer validaciones en diversos puntos del template.
    -->
    <#assign tieneSeries = series?? && (series?size > 0) />
    <#assign tieneSubseries = subseries?? && (subseries?size > 0) />

    <#if tieneSeries >
    <#--
        2018-04-25 jgarcia@controltechcg.com Issue #151 (SICDI-Controltech)
        feature-151: Mejora en presentación de las tablas.
    -->
    <table class="table table-bordered table-hover">
        <thead>
            <tr>
                <th>Código</th>
                <th>Nombre</th>
                </tr>
            </thead>
        <tbody>
        	<#list series as s>
            <tr>
                <td nowrap>${s.codigo}</td>
                <td><a href="carpeta?ser=${s.id}&cargoFiltro=${cargoFiltro!"0"}">${s.nombre}</a></td> 
                </tr>
            </#list>            
            </tbody>
        </table>
    <#elseif !tieneSubseries >
        <#--
            2018-04-25 jgarcia@controltechcg.com Issue #151 (SICDI-Controltech)
            feature-151: Banner que indica que el usuario en sesión no tiene
            documentos archivados en las series TRD (y cargo, en caso de ser
            seleccionado).
        -->
        <#assign noSeriesMsg="No tiene documentos en archivo en ninguna de las Series TRD"/>
        <#if cargoFiltro != 0>
            <#assign noSeriesMsg = noSeriesMsg + " para el cargo seleccionado"/>
        </#if>
    <div class="alert alert-info" role="alert">
        ${noSeriesMsg}.
        </div>
    </#if>

    <#if tieneSubseries >
    <h5>Subserie: ${serie.codigo}. ${serie.nombre}</h5>
    <hr>
    <#--
        2018-04-25 jgarcia@controltechcg.com Issue #151 (SICDI-Controltech)
        feature-151: Mejora en presentación de las tablas.
    -->
    <table class="table table-bordered table-hover">
        <thead>
            <tr>
                <th>Código</th>
                <th>Nombre</th>
                </tr>
            </thead>
        <tbody>
            <#list subseries as x>
            <tr>
                <td nowrap="">${x.codigo}</td>

                    <#if x.documentos?? && (x.documentos?size > 0) >                		
                <td><a href="carpeta?sub=${x.id}&cargoFiltro=${cargoFiltro!"0"}">${x.nombre} ( ${x.documentos?size} ) </a></td>
                	<#else>
                <td><a href="carpeta?sub=${x.id}&cargoFiltro=${cargoFiltro!"0"}">${x.nombre} ( 0 )</a></td>
                	</#if> 
                </tr>
            </#list>            
            </tbody>
        </table>
    </#if>
    <#if docs?? >
    <h5>${subserie.codigo}. ${subserie.nombre}</h5>
        <#if (docs?size > 0) >
        	<#--
        	    2017-05-15 jgarcia@controltechcg.com Issue #82 (SICDI-Controltech) feature-82:
        	    Modificación en la pantalla de presentación de registros de archivo, para presentar
        	    la fecha de creación y fecha de archivo.
        	-->
    <table class="table">
        <thead>
            <tr>
                <th>Radicado</th>
                <th>Asunto</th>
                <th>Fecha Creación</th>
                <th>Fecha Archivo</th>
                <th>Cargo</th>
                </tr>
            </thead>
        <tbody>
                <#list docs as x>
            <tr>
                <td nowrap>${x.radicado!"&lt;Sin radicado&gt;"}</td>
                <td><a href="/documento?pin=${x.instancia.id}">${x.asunto!"&lt;Sin asunto&gt;"}</a></td>
                <td nowrap>${x.cuando?string('yyyy-MM-dd hh:mm aa')}</td>
                        <#assign registroArchivo = registrosArchivoMapa[x.id]>
                <td nowrap>${registroArchivo.cuando?string('yyyy-MM-dd hh:mm aa')}</td>
                        <#if registroArchivo.cargo??>
                <td nowrap>${registroArchivo.cargo.carNombre!""}</td>
                        <#else>
                <td nowrap></td>
                        </#if>
                </tr>
                </#list>            
            </tbody>
        </table>
        </#if>
        <#if (docs?size == 0) >
    <div class="jumbotron">
        <h3>No hay documentos</h3>
        <p>En este momento no hay documentos relacionados a esta carpeta</p>
        </div>
        </#if>
    </#if>
    </div>

<#include "bandeja-footer.ftl">
