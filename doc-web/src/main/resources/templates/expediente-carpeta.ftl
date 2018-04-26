<#setting number_format="computer">
<#assign pageTitle = "Documentos Archivados"/>
<#include "bandeja-header.ftl">

<div class="container-fluid">
    </br>
    <#--
        2018-03-07 edison.gonzalez@controltechcg.com Issue #151 (SICDI-Controltech) feature-151:
        Modificación en la pantalla de presentación de registros de archivo, para realizar
        el filtro por el cargo que archiva el documento.

        2018-04-26 jgarcia@controltechcg.com Issue #151 (SICDI-Controltech) feature-151:
        Acción de submit del formulario de filtro de cargo, al cambiar la opción en el
        selector. Mejoras de presentación UI.
    -->
    <form action = "/expediente/carpeta" method="GET">
        <div class="form-group">
            <label for="cargoIdFirma">Cargo:</label>
            <select class="form-control" id="cargoFiltro" name="cargoFiltro" onchange="submitSelect(this)">
                <#if cargosXusuario??>
                    <#list cargosXusuario as cargoXusuario>
                        <#if cargoXusuario.id?string == ((cargoFiltro)!"")?string >
                <option value="${cargoXusuario.id}" selected="selected">${cargoXusuario.nombre}</option>
                        <#else>
                <option value="${cargoXusuario.id}">${cargoXusuario.nombre}</option>
                        </#if>
                    </#list>
                </#if>    
                </select>
            </div>

        <input type="hidden" name="sub" id="sub" value="${(sub)!""}" />
        <input type="hidden" name="ser" id="ser" value="${(ser)!""}" />

        <#if retornaSerie??>
        <#--
            2018-04-25 jgarcia@controltechcg.com Issue #151 (SICDI-Controltech)
            feature-151: Cambio en presentación de enlace "Regresar" para que tenga
            la apariencia de un botón.
        -->
        <a class="btn btn-info btn-sm" href="carpeta?ser=${retornaSerie}&cargoFiltro=${cargoFiltro!"0"}">Regresar</a>
        </#if>
        </form>

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
                <th>Núm. Documentos Archivados</th>
                </tr>
            </thead>
        <tbody>
        	<#list series as serie>
            <tr>
                <td nowrap>${serie.codigo}</td>
                <td><a href="carpeta?ser=${serie.id}&cargoFiltro=${cargoFiltro!"0"}">${serie.nombre}</a></td>
                <td>${serie.numeroDocumentosArchivados}</td>
                </tr>
            </#list>            
            </tbody>
        </table>
    <#elseif !subseries?? && !docs?? >
        <#--
            2018-04-25 jgarcia@controltechcg.com Issue #151 (SICDI-Controltech)
            feature-151: Banner que indica que el usuario en sesión no tiene
            documentos archivados en las series TRD (y cargo, en caso de ser
            seleccionado).
        -->
        <#assign noSeriesMsg="Actualmente, no tiene documentos en archivo en ninguna de las Series TRD"/>
        <#if cargoFiltro != 0>
            <#assign noSeriesMsg = noSeriesMsg + " para el cargo seleccionado"/>
        </#if>
    <#--
        2018-04-26 jgarcia@controltechcg.com Issue #151 (SICDI-Controltech)
        feature-151: Mensaje de advertencia (sin documentos archivados) usando
        jumbotron.
    -->
    <div class="jumbotron">
        <h3>No tiene ${pageTitle}</h3>
        <p>${noSeriesMsg}.</p>
        </div>
    </#if>

    <#if tieneSubseries >
    <h5>Serie: ${serie.codigo}. ${serie.nombre}</h5>
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
                <th>Núm. Documentos Archivados</th>
                </tr>
            </thead>
        <tbody>
            <#--
                2018-04-25 jgarcia@controltechcg.com Issue #151 (SICDI-Controltech)
                feature-151: Uso de DTO para la presentación de información.
            -->
            <#list subseries as subserie>
            <tr>
                <td nowrap="">${subserie.codigo}</td>
                <td><a href="carpeta?sub=${subserie.id}&cargoFiltro=${cargoFiltro!"0"}">${subserie.nombre}</a></td>                	
                <td>${subserie.numeroDocumentosArchivados}</td>
                </tr>
            </#list>            
            </tbody>
        </table>
    <#elseif !series?? && !docs?? >
        <#--
            2018-04-25 jgarcia@controltechcg.com Issue #151 (SICDI-Controltech)
            feature-151: Banner que indica que el usuario en sesión no tiene
            documentos archivados en las subseries TRD (y cargo, en caso de ser
            seleccionado).
        -->
        <#assign noSubseriesMsg="No tiene documentos en archivo en ninguna de las Subseries TRD"/>
        <#if cargoFiltro != 0>
            <#assign noSubseriesMsg = noSubseriesMsg + " para el cargo seleccionado"/>
        </#if>
        <#--
            2018-04-26 jgarcia@controltechcg.com Issue #151 (SICDI-Controltech)
            feature-151: Mensaje de advertencia (sin documentos archivados) usando
            jumbotron.
        -->
    <div class="jumbotron">
        <h3>No tiene ${pageTitle}</h3>
        <p>${noSubseriesMsg}.</p>
        </div>
    </#if>


    <#if docs?? >
    <h5>Subserie: ${subserie.codigo}. ${subserie.nombre}</h5>
    <hr>
        <#if (docs?size > 0) >
    <#--
        2017-05-15 jgarcia@controltechcg.com Issue #82 (SICDI-Controltech) feature-82:
        Modificación en la pantalla de presentación de registros de archivo, para presentar
        la fecha de creación y fecha de archivo.

        2018-04-25 jgarcia@controltechcg.com Issue #151 (SICDI-Controltech)
        feature-151: Mejora en presentación de las tablas. Retiro del mapa 
        "registrosArchivoMapa" gracias al uso de DTOs.
    -->
    <table class="table table-bordered table-hover">
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
                <#list docs as documentoArchivado>
            <tr>
                <td nowrap>${documentoArchivado.numeroRadicado!"&lt;Sin radicado&gt;"}</td>
                <td><a href="/documento?pin=${documentoArchivado.procesoInstanciaID}">${documentoArchivado.documentoAsunto!"&lt;Sin asunto&gt;"}</a></td>
                <td nowrap>${documentoArchivado.fechaCreacionDocumento?string('yyyy-MM-dd hh:mm aa')}</td>
                <td nowrap>${documentoArchivado.fechaCreacionArchivo?string('yyyy-MM-dd hh:mm aa')}</td>
                <td nowrap>${documentoArchivado.cargoNombre!""}</td>
                </tr>
                </#list>            
            </tbody>
        </table>
        </#if>
        <#if (docs?size == 0) >
    <#--
        2018-04-26 jgarcia@controltechcg.com Issue #151 (SICDI-Controltech)
        feature-151: Mensaje de advertencia (sin documentos archivados) usando
        jumbotron.
    -->    
    <div class="jumbotron">
        <h3>No hay documentos</h3>
        <p>En este momento no hay documentos relacionados a esta Subserie<#if cargoFiltro != 0> para el cargo seleccionado</#if></p>
        </div>
        </#if>
    </#if>
    </div>

<#include "bandeja-footer.ftl">
