package com.laamware.ejercito.doc.web.repo;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.laamware.ejercito.doc.web.entity.Dependencia;
import com.laamware.ejercito.doc.web.entity.Usuario;

public interface UsuarioRepository extends GenJpaRepository<Usuario, Integer> {

    /**
     * Obtiene el usuario activo correspondiente al login.
     *
     * @param login Login.
     * @return Usuario activo o {@code null} en caso que no exista un usuario
     * activo correspondiente.
     */
    /*
     * 2017-09-12 jgarcia@controltechcg.com Issue #123 (SICDI-Controltech) 
     * hotfix-123: Corrección en búsqueda de usuarios para que únicamente 
     * presente información de usuarios activos.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    Usuario getByLoginAndActivoTrue(String login);

    /**
     * Obtiene los usuarios que pertenecen a la dependencia
     *
     * @param dep
     * @return
     */
    // 2017-02-27 jgarcia@controltechcg.com Issue #148: Corrección para que
    // únicamente se puedan seleccionar usuarios activos.
    List<Usuario> findByDependenciaAndActivoTrue(Dependencia dep, Sort sort);

    @Query(nativeQuery = true, value = "select pr.rol_id from perfil_rol pr join usuario u on u.PER_ID = pr.PER_ID where lower(u.USU_LOGIN) = ?")
    List<String> allByLogin(String login);

    Usuario findByLoginAndActivo(String login, Boolean activo);

    List<Usuario> findByLogin(String login);

    Usuario findByNombre(String nombre);

    @Query(nativeQuery = true, value = "SELECT u.USU_LOGIN, vb.CUANDO, u.USU_NOMBRE, u.USU_GRADO FROM USUARIO_HISTORIAL_FIRMA_IMG vb INNER JOIN USUARIO u ON vb.QUIEN_MOD = u.USU_ID WHERE vb.USU_ID = ? ORDER BY vb.CUANDO DESC")
    List<Object[]> findHistorialFirmaUsuario(Integer id);

    /**
     * Obtiene una lista de los ID de los roles activos asignados a un usuario.
     *
     * @param login Login de usuario.
     * @return Lista de los ID de los roles activos.
     */
    /*
	 * 2017-06-22 jgarcia@controltechcg.com Issue #111 (SICDI-Controltech)
	 * hotfix-111: Implementación de función que obtiene la lista de roles
	 * activos asignados a un usuario, según el perfil asociado.
     */
    @Query(nativeQuery = true, value = ""
            + " SELECT DISTINCT                                                                  "
            + " ROL.ROL_ID                                                                       "
            + " FROM                                                                             "
            + " USUARIO                                                                          "
            + " JOIN PERFIL ON (PERFIL.PER_ID = USUARIO.PER_ID)                                  "
            + " JOIN PERFIL_ROL ON (PERFIL_ROL.PER_ID = PERFIL.PER_ID)                           "
            + " JOIN ROL ON (ROL.ROL_ID = PERFIL_ROL.ROL_ID)                                     "
            + " WHERE                                                                            "
            + " USUARIO.USU_LOGIN = :login                                                       "
            + " AND PERFIL.ACTIVO = 1                                                            "
            + " AND PERFIL_ROL.ACTIVO = 1                                                        "
            + " AND ROL.ACTIVO = 1                                                               "
            + " ORDER BY ROL.ROL_ID                                                              ")
    List<String> findAllActiveRolByLogin(@Param("login") String login);
}
