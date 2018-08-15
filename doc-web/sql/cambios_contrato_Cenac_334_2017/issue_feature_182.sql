--
-- Author:  edisson.gonzalez@controltechcg.com
-- Created: 25/07/2018 Issue #182 (SICDI-Controltech) feature-182
--

-- -----------------------------------------------------------------------------
-- TABLA: NOTIFICACION_X_USUARIO
-- -----------------------------------------------------------------------------

create TABLE NOTIFICACION_X_USUARIO(
    NOT_USU_ID      NUMBER NOT NULL,
    USU_ID          NUMBER NOT NULL,
    TNF_ID          NUMBER NOT NULL,
    PRIMARY KEY (NOT_USU_ID), 
    CONSTRAINT NOT_USUARIO_UNI_1 
        UNIQUE (USU_ID,TNF_ID),
    CONSTRAINT "FK_NOT_USUARIO" FOREIGN KEY ("USU_ID")
	  REFERENCES "DOC"."USUARIO" ("USU_ID"),
    CONSTRAINT "FK_NOT_NOTIFICACION" FOREIGN KEY ("TNF_ID")
	  REFERENCES "DOC"."TIPO_NOTIFICACION" ("TNF_ID")
);

-- -----------------------------------------------------------------------------
-- SEQUENCE: NOTIFICACION_X_USUARIO_SEQ
-- -----------------------------------------------------------------------------
CREATE SEQUENCE  "DOC"."NOTIFICACION_X_USUARIO_SEQ"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1;

UPDATE "DOC"."TIPO_NOTIFICACION" SET VALOR = '-100' WHERE TNF_ID = 60;
UPDATE "DOC"."TIPO_NOTIFICACION" SET VALOR = '-100' WHERE TNF_ID = 70;

update TIPO_NOTIFICACION set nombre = 'NOTIFICACIÓN DOCUMENTO ANULADO' where TNF_ID = 100;

declare
    cursor c_usuario is
    select *
    from Usuario
    where activo = 1;
    
    cursor c_tip_notificacion is    
    select *
    from TIPO_NOTIFICACION
    where valor > -2
    and activo = 1;
    aux_total  NUMBER := 0;
begin
    for auxc_usuario in c_usuario loop
        aux_total := aux_total + 1;
        for aux_c_tip_notificacion in c_tip_notificacion loop
            begin
                INSERT INTO NOTIFICACION_X_USUARIO VALUES(NOTIFICACION_X_USUARIO_SEQ.NEXTVAL, auxc_usuario.usu_id, aux_c_tip_notificacion.tnf_id);
            exception when others then
                DBMS_OUTPUT.PUT_LINE(' Error desactivando la notificación '||aux_c_tip_notificacion.nombre||'.'||'al usuario '||auxc_usuario.usu_grado||' '||auxc_usuario.usu_nombre);
            end;
        end loop;
        DBMS_OUTPUT.PUT_LINE('Desactivando notificaciones al usuario '||auxc_usuario.usu_grado||' '||auxc_usuario.usu_nombre);
    end loop;
    DBMS_OUTPUT.PUT_LINE('TOTAL DE USUARIOS: '||aux_total);
end;
/
