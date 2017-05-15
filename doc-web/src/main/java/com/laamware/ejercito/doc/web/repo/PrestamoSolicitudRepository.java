package com.laamware.ejercito.doc.web.repo;

import java.util.List;

import org.springframework.data.domain.Sort;

import com.laamware.ejercito.doc.web.entity.PrestamoSolicitud;

public interface PrestamoSolicitudRepository extends
		GenJpaRepository<PrestamoSolicitud, Integer> {

	List<PrestamoSolicitud> findByActivo(Boolean activo, Sort sort);

	PrestamoSolicitud findByExpedienteId(Integer eid);

}
