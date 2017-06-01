package com.laamware.ejercito.doc.web.serv;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.laamware.ejercito.doc.web.entity.Dependencia;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.DependenciaRepository;

/**
 * Servicio para las operaciones de negocio de dependencias.
 * 
 * @author jgarcia@controltechcg.com
 * @since May 15, 2017
 */
// 2017-05-15 jgarcia@controltechcg.com Issue #78 (SICDI-Controltech) feature-78
@Service
public class DependenciaService {

	@Autowired
	private DependenciaRepository dependenciaRepository;

	/**
	 * Busca la unidad de una dependencia.
	 * 
	 * @param dependencia
	 *            Dependencia.
	 * @return Dependencia unidad.
	 */
	public Dependencia buscarUnidad(Dependencia dependencia) {
		Dependencia unidad = dependencia;
		Integer padreID = dependencia.getPadre();

		while (padreID != null) {
			unidad = dependenciaRepository.findOne(padreID);
			padreID = unidad.getPadre();
		}

		return unidad;
	}

	/**
	 * Retira el usuario como jefe asignado (jefe principal o jefe encargado) de
	 * las dependencias asociadas.
	 * 
	 * @param usuario
	 *            Usuario a retirar como jefe encargado.
	 * @return Lista de las dependencias activas de las cuales el usuario se
	 *         retiró como jefe asignado.
	 */
	// 2017-06-01 jgarcia@controltechcg.com Issue #99 (SICDI-Controltech)
	// hotfix-99
	public List<Dependencia> retirarUsuarioComoJefeAsignado(Usuario usuario) {
		List<Dependencia> dependenciasAsignadas = dependenciaRepository.findActivoByJefeAsignado(usuario.getId());

		for (Dependencia dependencia : dependenciasAsignadas) {
			System.out.println(dependencia);
		}

		return dependenciasAsignadas;
	}
}
