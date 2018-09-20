<#setting number_format="computer">
<#assign pageTitle = "Crear Transferencia de archivos y expedientes en gestión" />
<#import "spring.ftl" as spring />
<#include "bandeja-header.ftl">
<#include "lib/transferencia-archivo_functions.ftl" />

<div class="container-fluid">
    <div>
        <ol class="breadcrumb">
            <li><a href="/transferencia-archivo/listar?">Inicio</a></li>
            <li class="active">Crear Transferencia de archivos y expedientes en gestión</li>
        </ol>
    </div>
    
    <#if dependencia?? && dependencia.jefe??>
        <form method="POST">
            <div class="form-group row">
                <label for="origenUsuario" class="col-sm-1 col-form-label text-xs-right">Usuario Origen</label>
                <div class="col-sm-9">
                    <input type="text" name="origenUsuario_visible" class="form-control" value="${getUsuarioDescripcion(origenUsuario)}" disabled />
                    <input type="hidden" name="origenUsuario" value="${origenUsuario.id}" />
                </div>
            </div>

            <div class="form-group row">
                <label for="cargoOrigen" class="col-sm-1 col-form-label text-xs-right">Cargo</label>
                <div class="col-sm-9">
                    <select class="form-control" id="cargoOrigen" name="cargoOrigen">
                        <#if cargosXusuario??>   
                            <#list cargosXusuario as cla>
                                <#if cla.id?string == (cargoOrigen!"")?string >
                                    <option value="${cla.id}" selected="selected">${cla.nombre}</option>
                                <#else>
                                    <option value="${cla.id}">${cla.nombre}</option>
                                </#if>
                            </#list>
                        </#if>
                    </select>
                </div>
            </div>

            <!--Búsqueda con finder -->
            <div class="form-group row">
                <label for="destinoUsuario" class="col-sm-1 col-form-label text-xs-right">Usuario Destino</label>
                <div class="col-sm-9">
                    <div class="input-group">
                        <input type="text" id="destinoUsuario_visible" name="destinoUsuario_visible" class="form-control" value="<#if destinoUsuario??>${getUsuarioDescripcion(destinoUsuario)}</#if>" disabled />
                        <div class="input-group-btn">
                            <button type="button" class="btn btn-primary" onclick="openFinderWindow()">Buscar</button>
                        </div> 
                    </div> 
                </div>
                <input type="hidden" id="destinoUsuario" name="destinoUsuario" value="<#if destinoUsuario??>${destinoUsuario.id}</#if>" />
            </div>

            <div class="form-group row">
                <label class="col-sm-1 col-form-label text-xs-right">Justificación </label>
                <div class="col-sm-9">
                    <div class="card-block cus-gray-bg">
                        <fieldset class="form-group">
                            <textarea class="form-control" rows="5" id="justificacion" name="justificacion" required>${justificacion!""}</textarea>
                        </fieldset>
                        <div class="row">
                            <div class="col-xs-8">
                                <select id="doc-obs-defecto-select" name="doc-obs-defecto-select" class="form-control input-sm" onchange="setObservacionDefecto(this, 'justificacion')">
                                    <option value="">Lista de justificaciones por defecto:</option>
                                    <#list justificacionesDefecto as justificacionDefecto >
                                    <option value="${justificacionDefecto.tjdId}">${justificacionDefecto.textoObservacion}</option>
                                    </#list>
                                </select>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="text-center">
                <button type="submit" class="btn btn-success btn-lg">Crear</button>
            </div>
        </form>
    <#else>
        <h5>Su dependencia no tiene un jefe asignado, comuníquese con su administrador para que le asigne uno.</h5>
    </#if>
</div>
<script src="/js/app/buscar-usuario.js"></script>

<script type="text/javascript">
    /**
     * Establece la justificación por defecto seleccionada en el área de texto de las
     * justificaciones.
     * @param {type} select HTML Select de las justificaciones por defecto.
     * @param {type} observacionesTextAreaID ID del área de texto de las justificaciones.
     * @returns {undefined}
     */
    function setObservacionDefecto(select, observacionesTextAreaID) {
        var texto = $.trim($(select).children("option").filter(":selected").text());
        if (texto !== '') {
            $("#" + observacionesTextAreaID).val(texto);
        }

        $(select).find('option:eq(0)').prop('selected', true);
    }
        
    $(document).on('submit','form',function(){
        loading(event);
    });
</script>
<#include "footer.ftl" />
