--
-- Author:  samuel.delgado@controltechcg.com
-- Created: 23/05/2018 Issue #177 (SICDI-Controltech) feature-177
--

-- -----------------------------------------------------------------------------
-- TABLA: ROL
-- -----------------------------------------------------------------------------

INSERT INTO ROL 
    (ROL_ID, QUIEN, CUANDO, QUIEN_MOD, CUANDO_MOD, ACTIVO, ROL_NOMBRE) 
VALUES 
    ('ADMIN_ARCHIVO', 3390, SYSDATE, 3390, SYSDATE, 1, 'Administrador Archivo')
;