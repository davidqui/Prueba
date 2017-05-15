package com.laamware.ejercito.doc.web.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.laamware.ejercito.doc.web.entity.Expediente;
import com.laamware.ejercito.doc.web.entity.ExpedienteEstado;

public interface ExpedienteEstadoRepository extends
		GenJpaRepository<ExpedienteEstado, Integer> {

	@Query("select ee from ExpedienteEstado ee where ee.activo=?1 and ee.estado.nombre = ?2 ")
	List<ExpedienteEstado> findByActivoAndEstado(boolean activo, String nombre);

	@Query("select ee from ExpedienteEstado ee where ee.activo=?1 and (ee.estado.nombre = ?2 or ee.estado.nombre = ?3) order by ee.expediente.nombre ASC")
	List<ExpedienteEstado> findByActivoAndEstados(boolean activo,
			String nombre, String nombre2);

	ExpedienteEstado findByExpediente(Expediente e);

}
