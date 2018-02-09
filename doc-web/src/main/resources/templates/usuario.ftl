<!-- 2017-02-15 jgarcia@controltechcg.com Issue #144: Se asigna formato numerico para manejo de los miles -->
<#setting number_format="computer">

<#assign pageTitle = usuario.nombre!"Usuario" />
<#assign mode = usuario.mode!"" />
<#assign deferredJS = "" />
<#import "spring.ftl" as spring />
<#include "admin-header.ftl">
<#include "util-macros.ftl" />

<div class="container">
    <div class="row">
        <h1 class="cus-h1-page-title">${pageTitle}</h1>
	<@flash/>
        <form action="/usuarios/guardar" method="POST" enctype="multipart/form-data" >
    <#if usuario.id??>
            <input type="hidden" name="id" id="id" value="${usuario.idString}" />
    </#if>

            <fieldset class="form-group">
                <label for="login">Login</label>
        <#if usuario.id??>
                <input type="text" class="form-control" id="login" name="login" value="${(usuario.login)!""}" readonly="readonly" /><br />
	    <#else>
                <input type="text" class="form-control" id="login" name="login" value="${(usuario.login)!""}" /><br />
	    </#if>

                <a href="#" onclick="consultaUsuario()" class="btn btn-primary">Consultar</a>
        <#assign deferredJS>
                <script type="text/javascript">
                        function consultaUsuario() {
                                var loginBuscar = $("#login").val();
                                if(loginBuscar == ''){
                                        $('#alert-modal').modal("show");
                                }else{
                                        $.get('/usuarios/consultar-ldap?login=' + loginBuscar, function(data){
           					
           				
                                                if(data === null || data == ''){
                                                        $('#alert-modal-nodata').modal("show");
                                                }else if (data.dependencia === null || data.dependencia == ''){
                                                        $('#alert-modal-nodependencia').modal("show");
                                                }
                                                else{
                                                        $("#login").val(data.login);
                                                        $("#nombre").val(data.nombre);
                                                        $("#documento").val(data.documento);
                                                        if(data.documento === null || data.documento == ''){
                                                        }
                                                        $("#telefono").val(data.telefono);
                                                        if(data.telefono === null || data.telefono == ''){
                                                        }
                                                        $("#usuGrado\\.id").val(data.grado);
                                                        $("#gradosel").val(data.grado);
                                                        if(data.grado === null || data.grado == ''){
           						
                                                        }
                                                        $("#dependencia").val(data.dependencia);
                                                        $("#dependenciasel").val(data.dependencia);
           						
                                                        $("#email").val(data.email);
                                                        if(data.email === null || data.email == ''){
                                                        }
                                                        $("#cargo").val(data.cargo);
                                                        if(data.cargo === null || data.cargo == ''){
                                                        }  
                                                        $('#btnGuardar').prop('disabled', false);
           						
                                                }
           					
                                        });
                                }
              	
                }
                    </script>
        </#assign>
                </fieldset>

            <fieldset class="form-group">
                <label for="nombre">Nombre</label>
                <input type="text" class="form-control" id="nombre" name="nombre" value="${(usuario.nombre)!""}" readonly="readonly"/>
                </fieldset>
            <fieldset class="form-group">
                <label for="telefono">Teléfono</label>
                <input type="text" class="form-control" id="telefono" name="telefono" value="${(usuario.telefono)!""}" readonly="readonly"/>
                </fieldset>
            <fieldset class="form-group">
                <label for="email">Email</label>
                <input type="text" class="form-control" id="email" name="email" value="${(usuario.email)!""}"  readonly="readonly" />
                </fieldset>

    <#--
        2017-11-10 edison.gonzalez@controltechcg.com Issue #131 (SICDI-Controltech) 
        feature-131: Cambio en la entidad usuario, se coloca llave foranea el grado.
    -->
            <fieldset class="form-group">
        <@spring.bind "usuario.usuGrado.id" />
         <#if usuario.usuGrado.id??>
        <#--
            2017-17-10 edison.gonzalez@controltechcg.com Issue #133 (SICDI-Controltech) 
            issue-131: Ajuste del id del grado.
        -->
                <input type="hidden" name="${spring.status.expression}" id="${spring.status.expression}" value="${(usuario.usuGrado.id)}" />
        <#else>
                <input type="hidden" name="${spring.status.expression}" id="${spring.status.expression}" />
        </#if>
                <label for="${spring.status.expression}">Grado</label>
                <select class="form-control" id="gradosel" name="gradosel" readonly="readonly" disabled>
        <#if grados??>
                    <option value=""></option>
            <#list grados as gr>
	            <#if gr.id?string == ((usuario.usuGrado.id)!"")?string >
                    <option value="${gr.id}" selected="selected">${gr.nombre}</option>
	            <#else>
                    <option value="${gr.id}">${gr.nombre}</option>
	            </#if>
             </#list>
        </#if>
                    </select>
                </fieldset>


            <fieldset class="form-group">
        <@spring.bind "usuario.dependencia" />
        <#if usuario.dependencia??>
                <input type="hidden" name="${spring.status.expression}" id="${spring.status.expression}" value="${(usuario.dependencia.id)}" />
        <#else>
                <input type="hidden" name="${spring.status.expression}" id="${spring.status.expression}"  />
        </#if>
                <label for="${spring.status.expression}">Dependencia</label>
                <select class="form-control" id="dependenciasel" name="dependenciasel" readonly="readonly" disabled>

        <#if dependencias??>
                    <option value=""></option>
            <#list dependencias as dep>
	            <#if dep.id?string == ((usuario.dependencia.id)!"")?string >
                    <option value="${dep.idString}" selected="selected">${dep.nombre}</option>
	            <#else>
                    <option value="${dep.idString}">${dep.nombre}</option>
	            </#if>
             </#list>
        </#if>
                    </select>
                </fieldset>

            <fieldset class="form-group">
       <@spring.bind "usuario.documento" />
                <label for="documento">Documento de Identidad</label>
                <input type="text" class="form-control" id="documento" name="documento" value="${(usuario.documento)!""}" readonly="readonly"/>
                <div class="error">
        	<@spring.showErrors "<br>"/>
                    </div>
                </fieldset>

            <fieldset class="form-group">       
                <label for="cargo">Cargo</label>
                <input type="text" class="form-control" id="cargo" name="cargo" value="${(usuario.cargo)!""}" readonly="readonly"/>
                <div class="error">
        	<@spring.showErrors "<br>"/>
                    </div>
                </fieldset>

            <fieldset class="form-group">
        <@spring.bind "usuario.perfil" />
                <label for="${spring.status.expression}">Perfil</label>
                <select class="form-control" id="${spring.status.expression}" name="${spring.status.expression}" >
        <#if perfiles??>
                    <option value=""></option>
            <#list perfiles as per>
	            <#if per.id?string == ((usuario.perfil.id)!"")?string >
                    <option value="${per.id}" selected="selected">${per.nombre}</option>
	            <#else>
                    <option value="${per.id}">${per.nombre}</option>
	            </#if>
             </#list>
        </#if>
                    </select>
                </fieldset>

            <fieldset class="form-group">
        <@spring.bind "usuario.clasificacion" />
                <label for="${spring.status.expression}">Nivel de Acceso</label>
                <select class="form-control" id="${spring.status.expression}" name="${spring.status.expression}" >
        <#if clasificaciones??>
                    <option value=""></option>
            <#list clasificaciones as cla>
	            <#if cla.id?string == ((usuario.clasificacion.id)!"")?string >
                    <option value="${cla.id}" selected="selected">${cla.nombre}</option>
	            <#else>
                    <option value="${cla.id}">${cla.nombre}</option>
	            </#if>
             </#list>
        </#if>
                    </select>
                </fieldset>

            <fieldset class="form-group">
                <label for="file">Imagen de la firma <#if usuario.usuarioTieneFirmaCargada() > <label style="color:#d9534f;">(Exíste una firma cargada.) </label> </#if>  </label>
                <input class="form-control" type="file" name="file" id="file">
                </fieldset>
            
            <#if usuario.usuCargoPrincipalId??>
                <input type="text" class="form-control" id="login" name="usuCargoPrincipalId" value="${(usuario.usuCargoPrincipalId.carNombre)!""}" readonly="readonly" /><br />
	    
	    </#if>
                        
            <fieldset class="form-group">
                <@spring.bind "usuario.usuCargoPrincipalId" />
                    <label for="${spring.status.expression}">Cargo principal</label>
                    <select class="form-control" id="${spring.status.expression}" name="${spring.status.expression}">
                <#if cargos??>
                    <#list cargos as cla>
                       <#if cla.id?string == ((usuario.usuCargoPrincipalId.id)!"")?string >
                            <option value="${cla.id}" selected="selected">${cla.carNombre}</option>
                        </#if>
                    </#list>
                </#if>
                </select>
            </fieldset>
                        
            <fieldset class="form-group">
                <@spring.bind "usuario.usuCargo1Id" />
                    <label for="${spring.status.expression}">Cargo Alterno # 1</label>
                    <select class="form-control" id="${spring.status.expression}" name="${spring.status.expression}" >
                <#if cargos??>
                    <option value=""></option>
                    <#list cargos as cla>
                       <#if cla.id?string == ((usuario.usuCargo1Id.id)!"")?string >
                            <option value="${cla.id}" selected="selected">${cla.carNombre}</option>
                        <#else>
                            <option value="${cla.id}">${cla.carNombre}</option>
                        </#if>
                    </#list>
                </#if>
                </select>
            </fieldset>
            
            <fieldset class="form-group">
                <@spring.bind "usuario.usuCargo2Id" />
                    <label for="${spring.status.expression}">Cargo Alterno # 2</label>
                    <select class="form-control" id="${spring.status.expression}" name="${spring.status.expression}" >
                <#if cargos??>
                    <option value=""></option>
                    <#list cargos as cla>
                       <#if cla.id?string == ((usuario.usuCargo2Id.id)!"")?string >
                            <option value="${cla.id}" selected="selected">${cla.carNombre}</option>
                        <#else>
                            <option value="${cla.id}">${cla.carNombre}</option>
                        </#if>
                    </#list>
                </#if>
                </select>
            </fieldset>
            
            <fieldset class="form-group">
                <@spring.bind "usuario.usuCargo3Id" />
                    <label for="${spring.status.expression}">Cargo Alterno # 3</label>
                    <select class="form-control" id="${spring.status.expression}" name="${spring.status.expression}" >
                <#if cargos??>
                    <option value=""></option>
                    <#list cargos as cla>
                       <#if cla.id?string == ((usuario.usuCargo3Id.id)!"")?string >
                            <option value="${cla.id}" selected="selected">${cla.carNombre}</option>
                        <#else>
                            <option value="${cla.id}">${cla.carNombre}</option>
                        </#if>
                    </#list>
                </#if>
                </select>
            </fieldset>
            
            <fieldset class="form-group">
                <@spring.bind "usuario.usuCargo4Id" />
                    <label for="${spring.status.expression}">Cargo Alterno # 4</label>
                    <select class="form-control" id="${spring.status.expression}" name="${spring.status.expression}" >
                <#if cargos??>
                    <option value=""></option>
                    <#list cargos as cla>
                       <#if cla.id?string == ((usuario.usuCargo4Id.id)!"")?string >
                            <option value="${cla.id}" selected="selected">${cla.carNombre}</option>
                        <#else>
                            <option value="${cla.id}">${cla.carNombre}</option>
                        </#if>
                    </#list>
                </#if>
                </select>
            </fieldset>
            
            <fieldset class="form-group">
                <@spring.bind "usuario.usuCargo5Id" />
                    <label for="${spring.status.expression}">Cargo Alterno # 5</label>
                    <select class="form-control" id="${spring.status.expression}" name="${spring.status.expression}" >
                <#if cargos??>
                    <option value=""></option>
                    <#list cargos as cla>
                       <#if cla.id?string == ((usuario.usuCargo5Id.id)!"")?string >
                            <option value="${cla.id}" selected="selected">${cla.carNombre}</option>
                        <#else>
                            <option value="${cla.id}">${cla.carNombre}</option>
                        </#if>
                    </#list>
                </#if>
                </select>
            </fieldset>
            
            <fieldset class="form-group">
                <@spring.bind "usuario.usuCargo6Id" />
                    <label for="${spring.status.expression}">Cargo Alterno # 6</label>
                    <select class="form-control" id="${spring.status.expression}" name="${spring.status.expression}" >
                <#if cargos??>
                    <option value=""></option>
                    <#list cargos as cla>
                       <#if cla.id?string == ((usuario.usuCargo6Id.id)!"")?string >
                            <option value="${cla.id}" selected="selected">${cla.carNombre}</option>
                        <#else>
                            <option value="${cla.id}">${cla.carNombre}</option>
                        </#if>
                    </#list>
                </#if>
                </select>
            </fieldset>
            
            <fieldset class="form-group">
                <@spring.bind "usuario.usuCargo7Id" />
                    <label for="${spring.status.expression}">Cargo Alterno # 7</label>
                    <select class="form-control" id="${spring.status.expression}" name="${spring.status.expression}" >
                <#if cargos??>
                    <option value=""></option>
                    <#list cargos as cla>
                       <#if cla.id?string == ((usuario.usuCargo7Id.id)!"")?string >
                            <option value="${cla.id}" selected="selected">${cla.carNombre}</option>
                        <#else>
                            <option value="${cla.id}">${cla.carNombre}</option>
                        </#if>
                    </#list>
                </#if>
                </select>
            </fieldset>
            
            <fieldset class="form-group">
                <@spring.bind "usuario.usuCargo8Id" />
                    <label for="${spring.status.expression}">Cargo Alterno # 8</label>
                    <select class="form-control" id="${spring.status.expression}" name="${spring.status.expression}" >
                <#if cargos??>
                    <option value=""></option>
                    <#list cargos as cla>
                       <#if cla.id?string == ((usuario.usuCargo8Id.id)!"")?string >
                            <option value="${cla.id}" selected="selected">${cla.carNombre}</option>
                        <#else>
                            <option value="${cla.id}">${cla.carNombre}</option>
                        </#if>
                    </#list>
                </#if>
                </select>
            </fieldset>
            
            <fieldset class="form-group">
                <@spring.bind "usuario.usuCargo9Id" />
                    <label for="${spring.status.expression}">Cargo Alterno # 9</label>
                    <select class="form-control" id="${spring.status.expression}" name="${spring.status.expression}" >
                <#if cargos??>
                    <option value=""></option>
                    <#list cargos as cla>
                       <#if cla.id?string == ((usuario.usuCargo9Id.id)!"")?string >
                            <option value="${cla.id}" selected="selected">${cla.carNombre}</option>
                        <#else>
                            <option value="${cla.id}">${cla.carNombre}</option>
                        </#if>
                    </#list>
                </#if>
                </select>
            </fieldset>
            
            <fieldset class="form-group">
                <@spring.bind "usuario.usuCargo10Id" />
                    <label for="${spring.status.expression}">Cargo Alterno # 10</label>
                    <select class="form-control" id="${spring.status.expression}" name="${spring.status.expression}" >
                <#if cargos??>
                    <option value=""></option>
                    <#list cargos as cla>
                       <#if cla.id?string == ((usuario.usuCargo10Id.id)!"")?string >
                            <option value="${cla.id}" selected="selected">${cla.carNombre}</option>
                        <#else>
                            <option value="${cla.id}">${cla.carNombre}</option>
                        </#if>
                    </#list>
                </#if>
                </select>
            </fieldset>

            <div class="m-y">
                <#--
                    2017-17-10 edison.gonzalez@controltechcg.com Issue #133 (SICDI-Controltech) 
                    Issue-133: Ajuste por inconsistencia de validacion.
                -->
            	<button id="btnGuardar" type="submit" class="btn btn-primary">Guardar</button>
                <a href="/usuarios" class="btn btn-sm btn-danger">Regresar</a>
                </div>

            <br />
	      <#if (usuario.historialUsuarios?size > 0) >
            <div class="card">
                <div class="card-header">
                    Historial de firmas cargada
                    </div>
                <div class="card-block">
		            <#list usuario.historialUsuarios as vb>                	                        
                    <strong>Fecha:</strong> ${vb.fecha?string('yyyy-MM-dd hh:mm a:ss')}<br/>
                    <strong>Modificado por:</strong> ${vb}<br/>
                    <hr/>
	                </#list>
                    </div>
                </div>
	    </#if>

            </form>

        </div>
    </div>

<br />

<div id="alert-modal" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">Dato</h4>
                </div>
            <div class="modal-body">
                <p class="alert alert-warning">Debe ingresar un login para buscar el usuario </p>
                </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal" >Cerrar</button>

                </div>
              </div><!-- /.modal-content -->
            </div><!-- /.modal-dialog -->
          </div><!-- /.modal -->

<div id="alert-modal-nodata" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">Usuario No Encontrado</h4>
                </div>
            <div class="modal-body">
                <p class="alert alert-warning">El usuario no se encuentra en el Directorio Activo</p>
                </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>

                </div>
              </div><!-- /.modal-content -->
            </div><!-- /.modal-dialog -->
          </div><!-- /.modal -->


<div id="alert-modal-nodependencia" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">Usuario Sin Dependencia</h4>
                </div>
            <div class="modal-body">
                <p class="alert alert-warning">El usuario no se encuentra asociado a una dependencia en el Directorio Activo o la dependencia a la que pertenece no se encuentra configurada</p>
                </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>

                </div>
              </div><!-- /.modal-content -->
            </div><!-- /.modal-dialog -->
          </div><!-- /.modal -->

<#include "footer.ftl">