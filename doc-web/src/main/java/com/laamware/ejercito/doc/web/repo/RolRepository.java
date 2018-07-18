package com.laamware.ejercito.doc.web.repo;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;

import com.laamware.ejercito.doc.web.entity.Rol;
import org.springframework.data.repository.query.Param;

public interface RolRepository extends GenJpaRepository<Rol, String> {

	@Query(nativeQuery = true, value = "select r.* from usuario_rol ur join usuario u on ur.USU_ID = u.USU_ID join rol r on ur.rol_id = r.rol_id where lower(u.USU_LOGIN) = ?")
	List<Rol> allByLogin(String login);
	
        @Query(nativeQuery = true, value = "SELECT r.* "
                + "FROM USUARIO u JOIN PERFIL p ON u.PER_ID = p.PER_ID "
                + "JOIN PERFIL_ROL pr ON pr.PER_ID = p.PER_ID "
                + "JOIN ROL r ON r.ROL_ID = pr.ROL_ID "
                + "WHERE u.USU_ID = :rolId")
	List<Rol> allByUserID(@Param("rolId") Integer rolId);
}
