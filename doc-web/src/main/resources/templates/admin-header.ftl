<#include "util-macros.ftl" />
<#include "header.ftl">
<div class="col-md-4 col-lg-3">
    <div class="card">
        <div class="card-header">
            Administrables
            </div>
        <div class="card-block">
            <ul class="nav nav-pills nav-stacked">

              	<@link_admin  "ADMIN_PERFILES" "/admin/profiles" 'profiles' 'Perfiles de usuario'/>

              	<@link_admin  "ADMIN_DEPENDENCIAS" "/dependencias" 'dependencia' 'Dependencias'/>

                <@link_admin  "ADMIN_USUARIOS" "/usuarios" 'users' 'Usuarios'/>

                <#-- bitbucket.org/ascontroltech/sigdi-deploy
                Issue #1 : Quitar del módulo de administración las opciones Destinatarios, Tipos de destinatario y Bandejas.	12/09/2016 -->                               
                <#-- <@link_admin  "ADMIN_TIPOS_DESTINATARIO" "/admin/tipo-destinatario" 'tipo-destinatario' 'Tipos de destinatarios'/> -->


                <#-- bitbucket.org/ascontroltech/sigdi-deploy
                Issue #1 : Quitar del módulo de administración las opciones Destinatarios, Tipos de destinatario y Bandejas.	12/09/2016 -->
                <#--<@link_admin  "ADMIN_DESTINATARIOS" "/admin/destinatarios" 'destinatarios' 'Destinatarios'/> -->


                <@link_admin  "ADMIN_TRD" "/admin/trd" 'trd' 'Tabla de retención documental'/>                 

                <@link_admin  "ADMIN_EXPEDIENTES" "/admin/expediente" 'records' 'Expedientes'/> 

                <@link_admin  "ADMIN_AUDITORIA" "/admin/audit-security" 'audit-security' 'Auditoría y seguridad'/>

           		<@link_admin  "ADMIN_CLASIFICACIONES" "/admin/clasificacion" 'clasificacion' 'Clasificaciones'/>

                <@link_admin  "ADMIN_PLANTILLAS" "/admin/plantilla" 'plantilla' 'Plantillas'/>
  <#--
                <@link_admin  "ADMIN_PROCESOS" "/admin/proceso" 'proceso' 'Procesos'/>
                
                <@link_admin  "ADMIN_PROCESOS" "/admin/proceso-transicion-tipo" 'proceso-transicion-tipo' 'Tipos de transicion de proceso'/>
-->
                <@link_admin  "ADMIN_TIPOLOGIA" "/admin/tipologia" 'tipologia' 'Tipología de documento'/>

                <@link_admin  "ADMIN_TIPO_DOCUMENTO" "/admin/tipo-documento" 'tipo-documento' 'Tipo documento'/>

                <#-- bitbucket.org/ascontroltech/sigdi-deploy
                Issue #1 : Quitar del módulo de administración las opciones Destinatarios, Tipos de destinatario y Bandejas.	12/09/2016 -->
                <#-- <@link_admin  "ADMIN_BANDEJAS" "/admin/bandeja" 'bandeja' 'Bandejas'/> -->

                <@link_admin  "ADMIN_CAPACITACION" "/admin/juego" 'capacitacion' 'Capacitación'/>

                <@link_admin  "ADMIN_GRADOS" "/grados" 'grados' 'Grados'/>

                <@link_admin  "ADMIN_CARGOS" "/admin/cargos" 'cargos' 'Cargos'/>

                <@link_admin  "ADMIN_DOMINIO" "/admin/dominio" 'Dominios' 'Dominios'/>
                
                <#--
                    2018-05-24 samuel.delgado@controltechcg.com Issue #172 (SICDI-Controltech) feature-172:
                    Enlace para la opción de obsevación por defecto.
                -->
                <@link_admin  "ADMIN_DOC_OBSERVACION_DEFECTO" "/admin/doc-observacion-defecto" 'doc-observacion-defecto' 'Observaciones por defecto'/>

                <@link_admin  "ADMIN_INFORMES" "/admin/informes/init" 'informes' 'Informes'/>

                <@link_admin  "ADMIN_FORMATOS" "/admin/formatos" 'formatos' 'Formatos'/>

                <@link_admin  "ADMIN_LOG" "/admin/logs" 'logs' 'Logs'/>

                <@link_admin  "ADMIN_PLANTILLA_TRANSF_ARCHIVO" "/admin/plantilla-transf-archivo" 'plantilla-transf-archivo' 'Plantilla Transf. Archivo'/>

                <#--
                    2018-05-09 jgarcia@controltechcg.com Issue #160 (SICDI-Controltech) feature-160:
                    Enlace para la opción de validación de PDF por UUID.
                -->
                <@link_admin  "VALIDAR_PDF_DOCFIRMAENVIO_UUID" "/admin/file-validation" 'file-validation' 'Validación integridad documento electrónico'/>

                <#--
                    2018-05-30 samuel.delgado@controltechcg.com Issue #169 (SICDI-Controltech) feature-169:
                    Enlace para la opción de notificaciones.
                -->
                <@link_admin  "ADMIN_NOTIFICACIONES" "/admin/notificacion" 'notificaciones' 'Notificaciones'/>
                
                <@link_admin  "ADMIN_PAR_NOMBRE_EXPEDIENTE" "/admin/parnombrexpediente" 'parnombrexpediente' 'Parametro Nombre Expediente'/>
                
                <#--
                    2018-08-16 samuel.delgado@controltechcg.com Issue #7 (SICDI-Controltech) feature-gogs-7:
                    Enlace para la opción de razon de inhabilitar usuario.
                -->
                <@link_admin  "ADMIN_RAZON_INHABILITAR" "/admin/razon-inhabilitar" 'razones-inhabilitar' 'Razones Inhabilitar Usuario'/>
                
                <#--
                    2018-08-31 samuel.delgado@controltechcg.com Issue #10 (SICDI-Controltech) feature-gogs-10:
                    Enlace para la opción de destino externo.
                -->
                <@link_admin  "ADMIN_DESTINO_EXTERNO" "/admin/destino-externo" 'destinos-externos' 'Destinos Externos'/>
                </ul>
            </div>
        </div>
    </div>
<div class="col-md-8 col-lg-9">
