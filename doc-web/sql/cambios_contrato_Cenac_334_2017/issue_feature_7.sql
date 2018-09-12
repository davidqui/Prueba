--
-- Author:  samuel.delgado@controltechcg.com
-- Created: 15/08/2018 Issue gogs #7 (SICDI-Controltech) feature-gogs-7
--


-- -----------------------------------------------------------------------------
-- TABLA: RAZON_INHABILITAR
-- -----------------------------------------------------------------------------
SET DEFINE OFF
CREATE SEQUENCE RAZON_INHABILITAR_SEQ MINVALUE 0 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1;

CREATE TABLE RAZON_INHABILITAR (
    RAZ_ID              NUMBER(38)  NOT NULL,
    TEXTO_RAZON         VARCHAR2(64 CHAR)   NOT NULL,
    ACTIVO              NUMBER(1)           NOT NULL,
    QUIEN               NUMBER(38)          NOT NULL,
    CUANDO              DATE                NOT NULL,
    QUIEN_MOD           NUMBER(38)          NOT NULL,
    CUANDO_MOD          DATE                NOT NULL,
    PRIMARY KEY (RAZ_ID)
);

ALTER TABLE RAZON_INHABILITAR
ADD CONSTRAINT RAZON_INHABILITAR_QUIEN_FK
FOREIGN KEY (QUIEN)
REFERENCES USUARIO (USU_ID);

ALTER TABLE RAZON_INHABILITAR
ADD CONSTRAINT RAZON_INHABILITAR_QUIEN_MOD_FK
FOREIGN KEY (QUIEN_MOD)
REFERENCES USUARIO (USU_ID);

CREATE INDEX RAZON_INHABILITAR_ACTIVO_IDX 
ON RAZON_INHABILITAR (ACTIVO);

-- -----------------------------------------------------------------------------
-- TABLA: USUARIO
-- -----------------------------------------------------------------------------

ALTER TABLE USUARIO ADD (
    USU_ACTIVO NUMBER(1,0) DEFAULT 1 NULL, 
    RAZ_ID NUMBER(38) NULL
);

ALTER TABLE USUARIO
ADD CONSTRAINT USUARIO_RAZ_ID_FK
FOREIGN KEY (RAZ_ID)
REFERENCES RAZON_INHABILITAR (RAZ_ID);


-- -----------------------------------------------------------------------------
-- TABLA: H_USUARIO_ACTIVO
-- -----------------------------------------------------------------------------

CREATE TABLE H_USUARIO_ACTIVO (
    HUA_ID              NUMBER(38)          NOT NULL,
    USU_ID              NUMBER(38)          NOT NULL,
    USU_ACTIVO          NUMBER(1)           NOT NULL,
    RAZ_ID              NUMBER(38)              NULL,
    CUANDO              DATE                NOT NULL,
    PRIMARY KEY (HUA_ID)
);

CREATE SEQUENCE H_USUARIO_ACTIVO_SEQ MINVALUE 0 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1;

COMMENT ON TABLE H_USUARIO_ACTIVO
IS 'Historico de activación de un usuario.';

-- -----------------------------------------------------------------------------
-- TRIGGER: TRG_REGISTRO_H_USUARIO_ACTIVO
-- -----------------------------------------------------------------------------


CREATE OR REPLACE TRIGGER TRG_REGISTRO_H_USUARIO_ACTIVO
AFTER UPDATE OF USU_ACTIVO ON USUARIO
FOR EACH ROW
BEGIN
    INSERT INTO H_USUARIO_ACTIVO (HUA_ID, USU_ID, USU_ACTIVO, RAZ_ID, CUANDO)
    VALUES (H_USUARIO_ACTIVO_SEQ.NEXTVAL, :OLD.USU_ID, :NEW.USU_ACTIVO, :NEW.RAZ_ID, SYSDATE);
END;
/

-- -----------------------------------------------------------------------------
-- TABLA: ROL
-- -----------------------------------------------------------------------------

INSERT INTO ROL 
    (ROL_ID, QUIEN, CUANDO, QUIEN_MOD, CUANDO_MOD, ACTIVO, ROL_NOMBRE) 
VALUES 
    ('ADMIN_RAZON_INHABILITAR', 3390, SYSDATE, 3390, SYSDATE, 1, 'Administrar Razones de inhabilitar usuario')
;

-- -----------------------------------------------------------------------------
-- TABLA: TIPO_NOTIFICACION
-- -----------------------------------------------------------------------------

INSERT INTO TIPO_NOTIFICACION (TNF_ID,NOMBRE,VALOR,ACTIVO,QUIEN,CUANDO,QUIEN_MOD,CUANDO_MOD) VALUES ('300','NOTIFICACIÓN USUARIO INACTIVO','-1','1','3390',sysdate,'3390',sysdate);
INSERT INTO TIPO_NOTIFICACION (TNF_ID,NOMBRE,VALOR,ACTIVO,QUIEN,CUANDO,QUIEN_MOD,CUANDO_MOD) VALUES ('301','NOTIFICACIÓN USUARIO ACTIVADO','-1','1','3390',sysdate,'3390',sysdate);
INSERT INTO TIPO_NOTIFICACION (TNF_ID,NOMBRE,VALOR,ACTIVO,QUIEN,CUANDO,QUIEN_MOD,CUANDO_MOD) VALUES ('302','NOTIFICACIÓN MULTIPLE INACTIVACIÓN EN UNA SEMANA','-1','1','3390',sysdate,'3390',sysdate);
-- -----------------------------------------------------------------------------
-- TABLA: WILDCARD_NOTIFICACION
-- -----------------------------------------------------------------------------

INSERT INTO WILDCARD_NOTIFICACION (WNF_ID,NOMBRE,VALOR) VALUES ('301','Nombre Jefe dependencia','jefe.nombre');
INSERT INTO WILDCARD_NOTIFICACION (WNF_ID,NOMBRE,VALOR) VALUES ('302','Documento Jefe dependencia','jefe.documento');
INSERT INTO WILDCARD_NOTIFICACION (WNF_ID,NOMBRE,VALOR) VALUES ('303','Teléfono Jefe dependencia','jefe.telefono');
INSERT INTO WILDCARD_NOTIFICACION (WNF_ID,NOMBRE,VALOR) VALUES ('304','Grado Jefe dependencia','jefe.usuGrado.nombre');
INSERT INTO WILDCARD_NOTIFICACION (WNF_ID,NOMBRE,VALOR) VALUES ('305','Clasificación Jefe dependencia','jefe.clasificacion.nombre');
INSERT INTO WILDCARD_NOTIFICACION (WNF_ID,NOMBRE,VALOR) VALUES ('306','Email Jefe dependencia','jefe.email');
INSERT INTO WILDCARD_NOTIFICACION (WNF_ID,NOMBRE,VALOR) VALUES ('307','Motivo inactivación','razon.textoRazon');
INSERT INTO WILDCARD_NOTIFICACION (WNF_ID,NOMBRE,VALOR) VALUES ('308','fecha y hora','fecha');
INSERT INTO WILDCARD_NOTIFICACION (WNF_ID,NOMBRE,VALOR) VALUES ('309','Sigla grado Jefe dependencia','jefe.usuGrado.id');

-- -----------------------------------------------------------------------------
-- TABLA: WILDCARD_TIPO_NOTIFICACION
-- -----------------------------------------------------------------------------

INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('300','10');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('300','11');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('300','20');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('300','21');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('300','30');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('300','31');

INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('300','301');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('300','302');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('300','303');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('300','304');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('300','305');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('300','306');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('300','307');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('300','308');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('300','309');

INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('301','10');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('301','11');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('301','20');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('301','21');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('301','30');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('301','31');

INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('301','301');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('301','302');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('301','303');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('301','304');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('301','305');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('301','306');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('301','308');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('301','309');

INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('302','10');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('302','11');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('302','20');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('302','21');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('302','30');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('302','31');

INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('302','301');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('302','302');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('302','303');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('302','304');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('302','305');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('302','306');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('302','307');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('302','308');
INSERT INTO WILDCARD_TIPO_NOTIFICACION (TNF_ID,WNF_ID) VALUES ('302','309');

-- -----------------------------------------------------------------------------
-- DATOS DE PLANTILLAS DE NUEVAS NOTIFICACIONES
-- -----------------------------------------------------------------------------
DECLARE
    CURSOR C_NOT IS
    select '300' TNF_ID,
           '3' CLA_ID,
           '<!-- NOTIFICACIÓN USUARIO INACTIVACION--> 
									<br>
                                    <p style="margin-top: 20px;">Cordial Saludo,</p>
                                    <p style="margin:0; text-transform: uppercase;"><b>${(jefe.usuGrado.nombre)!""}. ${(jefe.nombre)!""}</b></p>
                                    
                                    <p style="text-align: justify;">Le informamos que el señor(a) ${(usuario.usuGrado.nombre)!""}. ${(usuario.nombre)!""} ha realizado satisfactoriamente su <b>"Inactivación"</b> en el sistema SICDI, ahora <b>NO</b> estará disponible para tramitar documentos y demás funciones autorizadas dentro del aplicativo.
                                    </p>
                                    
                                    <br>
                                    <br>
                                    
                                   
                                    <table border=''1'' id="t01" cellspacing=''0''>
                                        <thead>
                                        <tr>
                                            <td colspan="4" style="background-color: #d5d5d5; text-align:  center;"><h3><b>Información Del Usuario</b></h3></td>
                                        </tr>    
                                        </thead>
                                        
                                        <tr>
                                            <th>Funcionario</th>
                                            <th>Estado</th>
											<th>Justificación</th>
                                            <th>Fecha y Hora</th>
											
                                        </tr>
                                        <tr>
                                            <td>${(usuario.usuGrado.nombre)!""}. ${(usuario.nombre)!""}</td>
                                            <td>Inactivo</td>
                                            <td>${(razon.textoRazon)!""}</td>
											<td>${(fecha)!""}</td>
											
                                        </tr>
                                    </table>
                                    <br/>
                                    <br>
                                    <div>
                                    <p>En caso de haber recibido este mensaje por error, le solicitamos comunicarse con el equipo de soporte <b>SICDI</b> a la linea (057)350 653 14 74
                                    o al correo electronico soporte.sicdi@imi.mil.co</p>
                                    
                                    <p>La Información contenida en este correo es para uso exclusivo del destinatario y puede ser confidencial. En caso de recibir este correo por error, 
                                        por favor no imprima, copie, reenvíe o divulgue de manera total o parcial este mensaje. Borre este correo y todas las copias y avise al remitente. Gracias.</p>
                                    
                                    <p>Atentamente,</p>
                                    <br>Equipo de Soporte
                                    <p><b>Sistema Clasificado de Documentos de Inteligencia Militar</b></p>
                                </td>
                            </tr>
                        </table>
                </td>
                <td><td>
            </tr>
            <!-- END MAIN CONTENT AREA -->
        </table>' CUERPO,
            'Notificación SICDI - Usuario Inactivo' ASUNTO
    from dual
    union
    select '301' TNF_ID,
           '3' CLA_ID,
           '<!-- NOTIFICACIÓN USUARIO ACTIVO--> 
									<br>
                                    <p style="margin-top: 20px;">Cordial Saludo,</p>
                                    <p style="margin:0; text-transform: uppercase;"><b>${(jefe.usuGrado.nombre)!""}. ${(jefe.nombre)!""}</b></p>
                                    <br>
                                    <p style="text-align: justify;">Le informamos que el señor(a) ${(usuario.usuGrado.nombre)!""}. ${(usuario.nombre)!""} ha realizado satisfactoriamente su <b>"Activacion"</b> en el sistema SICDI, ahora está disponible para tramitar documentos y demás funciones autorizadas dentro del aplicativo.
                                    </p>
                                    
                                    <br>
                                    <br>
                                    
                                   
                                    <table border=''1'' id="t01" cellspacing=''0''>
                                        <thead>
                                        <tr>
                                            <td colspan="4" style="background-color: #d5d5d5; text-align:  center;"><h3><b>Información Del Usuario</b></h3></td>
                                        </tr>    
                                        </thead>
                                        
                                        <tr>
                                            <th>Funcionario</th>
                                            <th>Estado</th>
											<th>Justificación</th>
                                            <th>Fecha y Hora</th>
                                        </tr>
                                        <tr>
                                            <td>${(usuario.usuGrado.nombre)!""}. ${(usuario.nombre)!""}</td>
                                            <td>Activo</td>
                                            <td>${(razon.textoRazon)!""}</td>
											<td>${(fecha)!""}</td>
                                        </tr>
                                    </table>
                                    <br/>
                                    <br>
                                    <div>
                                    <p>En caso de haber recibido este mensaje por error, le solicitamos comunicarse con el equipo de soporte <b>SICDI</b> a la linea (057)350 653 14 74
                                    o al correo electronico soporte.sicdi@imi.mil.co</p>
                                    
                                    <p>La Información contenida en este correo es para uso exclusivo del destinatario y puede ser confidencial. En caso de recibir este correo por error, 
                                        por favor no imprima, copie, reenvíe o divulgue de manera total o parcial este mensaje. Borre este correo y todas las copias y avise al remitente. Gracias.</p>
                                    
                                    <p>Atentamente,</p>
                                    <br>Equipo de Soporte
                                    <p><b>Sistema Clasificado de Documentos de Inteligencia Militar</b></p>
                                </td>
                            </tr>
                        </table>
                </td>
                <td>td>
            </tr>
            <!-- END MAIN CONTENT AREA -->
        </table>' CUERPO,
            'Notificación SICDI - Usuario Activado' ASUNTO
    from dual
    union
    select '302' TNF_ID,
           '3' CLA_ID,
           '<!-- NOTIFICACIÓN USUARIO CON RECURRENTES INACTIVACIONES--> 
									<br>
                                    <p style="margin-top: 20px;">Cordial Saludo,</p>
                                    <p style="margin:0; text-transform: uppercase;"><b>${(jefe.usuGrado.nombre)!""}. ${(jefe.nombre)!""}</b></p>
                                    
                                    <p style="text-align: justify;">Le informamos que el señor(a) ${(usuario.usuGrado.nombre)!""}. ${(usuario.nombre)!""} ha realizado recurrentes Activaciones e Inactivaciones en un periodo de tiempo inferior a siete días en el sistema SICDI, por favor validar que esto sea un procedimiento autorizado para el usuario.
                                    </p>
                                    
                                    <br>
                                    <br>
                                  
                                    <div>
                                    <p>En caso de haber recibido este mensaje por error, le solicitamos comunicarse con el equipo de soporte <b>SICDI</b> a la linea (057)350 653 14 74
                                    o al correo electronico soporte.sicdi@imi.mil.co</p>
                                    
                                    <p>La Información contenida en este correo es para uso exclusivo del destinatario y puede ser confidencial. En caso de recibir este correo por error, 
                                        por favor no imprima, copie, reenvie o divulgue de manera total o parcial este mensaje. Borre este correo y todas las copias y avise al remitente. Gracias.</p>
                                    
                                    <p>Atentamente,</p>
                                    <br>Equipo de Soporte
                                    <p><b>Sistema Clasificado de Documentos de Inteligencia Militar</b></p>
                                </td>
                            </tr>
                        </table>
                </td>
                <td><td>
            </tr>
            <!-- END MAIN CONTENT AREA -->
        </table>' CUERPO,
            'Notificación SICDI - MULTIPLE INACTIVACIÓN EN UNA SEMANA' ASUNTO
    from dual;
    aux_count   NUMBER;
BEGIN
    FOR AUX_C_NOT IN C_NOT LOOP
        SELECT COUNT(1) INTO aux_count FROM NOTIFICACION WHERE TNF_ID = AUX_C_NOT.TNF_ID AND ACTIVO = 1;
        IF aux_count = 0 THEN
            Insert into NOTIFICACION (NTF_ID, TNF_ID, CLA_ID, CUERPO, ASUNTO, ACTIVO, QUIEN, CUANDO) VALUES
            (NOTIFICACION_SEQ.NEXTVAL, AUX_C_NOT.TNF_ID, AUX_C_NOT.CLA_ID, AUX_C_NOT.CUERPO, AUX_C_NOT.ASUNTO, 1, 3390, SYSDATE);
        END IF;
    END LOOP;
END;
/

Insert into RAZON_INHABILITAR (RAZ_ID,TEXTO_RAZON,ACTIVO,QUIEN,CUANDO,QUIEN_MOD,CUANDO_MOD) values (RAZON_INHABILITAR_SEQ.nextval,'Salida a Permiso','1','3390',sysdate,'3390',sysdate);
Insert into RAZON_INHABILITAR (RAZ_ID,TEXTO_RAZON,ACTIVO,QUIEN,CUANDO,QUIEN_MOD,CUANDO_MOD) values (RAZON_INHABILITAR_SEQ.nextval,'Comisión de Estudios','1','3390',sysdate,'3390',sysdate);
Insert into RAZON_INHABILITAR (RAZ_ID,TEXTO_RAZON,ACTIVO,QUIEN,CUANDO,QUIEN_MOD,CUANDO_MOD) values (RAZON_INHABILITAR_SEQ.nextval,'Comisiones al Exterior','1','3390',sysdate,'3390',sysdate);
Insert into RAZON_INHABILITAR (RAZ_ID,TEXTO_RAZON,ACTIVO,QUIEN,CUANDO,QUIEN_MOD,CUANDO_MOD) values (RAZON_INHABILITAR_SEQ.nextval,'Por traslado','1','3390',sysdate,'3390',sysdate);
Insert into RAZON_INHABILITAR (RAZ_ID,TEXTO_RAZON,ACTIVO,QUIEN,CUANDO,QUIEN_MOD,CUANDO_MOD) values (RAZON_INHABILITAR_SEQ.nextval,'Vacaciones','1','3390',sysdate,'3390',sysdate);
Insert into RAZON_INHABILITAR (RAZ_ID,TEXTO_RAZON,ACTIVO,QUIEN,CUANDO,QUIEN_MOD,CUANDO_MOD) values (RAZON_INHABILITAR_SEQ.nextval,'Incapacidad Medica','1','3390',sysdate,'3390',sysdate);

COMMIT;