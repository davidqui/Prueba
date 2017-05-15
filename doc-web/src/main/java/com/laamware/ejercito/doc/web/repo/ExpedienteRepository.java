package com.laamware.ejercito.doc.web.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;

import com.laamware.ejercito.doc.web.entity.Expediente;

public interface ExpedienteRepository extends
		GenJpaRepository<Expediente, Integer> {

	List<Expediente> findByActivo(boolean activo, Sort sort);

	List<Expediente> findByActivoAndDependenciaId(boolean activo,
			Integer dependenciaId, Sort sort);

	@Query(nativeQuery = true, value = "SELECT * FROM EXPEDIENTE WHERE ACTIVO=1 AND DEP_ID=? and EXP_ID!=? ORDER BY EXP_NOMBRE DESC")
	List<Expediente> findAllOthers(Integer dependenciaId, Integer expedienteId);

	@Query("select e from Expediente e where e.dependencia.id=?1  and e.activo=?2 and (e.estado.nombre = ?3 or e.estado.nombre = ?4) order by e.nombre ASC")
	List<Expediente> findByDependenciaAndEstadosAndActivo(Integer depId,
			Boolean activo, String nombre, String nombre2);

	/**
	 * Obtiene los expedientes por estados, si está activo y si tiene documentos
	 * asociados y por la dependencia del usuario
	 * 
	 * @param depId
	 * @param activo
	 * @param nombre
	 *            nombre del estado del expediente
	 * @param nombre2
	 *            nombre del estado del expediente
	 * @return
	 */
	@Query(nativeQuery = true, value = "SELECT EXP.EXP_ID, EXP.EXP_NOMBRE, EXP.CUANDO,EXP.DEP_ID, EXP.TRD_ID, EXP.ESEX_ID, " 
			+ " count(doc.doc_id), sum(PES.PES_FINAL) "
			+ " FROM EXPEDIENTE EXP "
			+ " JOIN ESTADO_EXPEDIENTE EE ON EE.ESEX_ID = EXP.ESEX_ID " 
			+ " JOIN DEPENDENCIA DEP ON EXP.DEP_ID=DEP.DEP_ID " 
			+ " JOIN DOCUMENTO DOC ON EXP.EXP_ID = DOC.EXP_ID "
			+ " JOIN PROCESO_INSTANCIA PIN ON DOC.PIN_ID = PIN.PIN_ID " 
			+ " JOIN PROCESO_ESTADO PES ON PIN.PES_ID = PES.PES_ID "
			+ " WHERE DEP.DEP_ID=?1 "
			+ " AND  EXP.ACTIVO=?2 "
			+ " AND (EE.ESEX_NOMBRE=?3 OR EE.ESEX_NOMBRE=?4) "
			+ " GROUP BY EXP.EXP_ID, EXP.EXP_NOMBRE, EXP.CUANDO,EXP.DEP_ID, EXP.TRD_ID, EXP.ESEX_ID "
			+ " having count(doc.doc_id) = sum(PES.PES_FINAL) "
			+ " ORDER BY EXP.EXP_NOMBRE desc")
	List<Expediente> findByDependenciaAndDocEstadoFinal(
			Integer depId, Boolean activo, String nombre, String nombre2);

	@Query("select e from Expediente e where e.activo=?1 and (e.estado.nombre = ?2 or e.estado.nombre = ?3) order by e.nombre ASC")
	List<Expediente> findByActivoAndEstados(boolean activo, String nombre,
			String nombre2);

	/**
	 * Realiza una búsqueda de los expedientes por estados,si está activo y si
	 * tiene documentos asociados que estén en estado final de proceso.
	 * 
	 * @param activo
	 * @param nombre
	 *            El nombre un estado del expediente
	 * @param nombre2
	 *            El nombre de otro estado del expediente
	 * @return
	 */

	@Query(nativeQuery = true, value = "SELECT EXP.EXP_ID, EXP.EXP_NOMBRE, EXP.CUANDO,EXP.DEP_ID, EXP.TRD_ID, EXP.ESEX_ID, "
			+" count(doc.doc_id), sum(PES.PES_FINAL) "
			+ " FROM EXPEDIENTE EXP "
			+ " JOIN ESTADO_EXPEDIENTE EE ON EE.ESEX_ID = EXP.ESEX_ID "
			+ " JOIN DEPENDENCIA DEP ON EXP.DEP_ID=DEP.DEP_ID "
			+ " JOIN DOCUMENTO DOC ON EXP.EXP_ID = DOC.EXP_ID "
			+ " JOIN PROCESO_INSTANCIA PIN ON DOC.PIN_ID = PIN.PIN_ID "
			+ " JOIN PROCESO_ESTADO PES ON PIN.PES_ID = PES.PES_ID " 
			+ " WHERE EXP.ACTIVO=?1 "
			+ " AND (EE.ESEX_NOMBRE=?2 OR EE.ESEX_NOMBRE=?3) "
			+ " GROUP BY EXP.EXP_ID, EXP.EXP_NOMBRE, EXP.CUANDO,EXP.DEP_ID, EXP.TRD_ID, EXP.ESEX_ID " 
			+ " having count(doc.doc_id) = sum(PES.PES_FINAL) "
			+ " ORDER BY EXP.EXP_NOMBRE desc")
	List<Expediente> findByActivoAndDocEstadoFinal(boolean activo,
			String nombre, String nombre2);

	List<Expediente> findByCuandoBetween(Date d1, Date d2);

}
