package com.laamware.ejercito.doc.web.repo;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.laamware.ejercito.doc.web.entity.Dependencia;
import com.laamware.ejercito.doc.web.entity.Usuario;

public interface UsuarioRepository extends GenJpaRepository<Usuario, Integer> {

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	Usuario getByLogin(String login);

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
}
