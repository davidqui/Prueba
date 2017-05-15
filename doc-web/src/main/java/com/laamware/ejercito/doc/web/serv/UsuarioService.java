package com.laamware.ejercito.doc.web.serv;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.laamware.ejercito.doc.web.entity.Dependencia;
import com.laamware.ejercito.doc.web.entity.Usuario;

/**
 * Servicio para las operaciones de negocio de usuario.
 * 
 * @author jgarcia@controltechcg.com
 * @since May 15, 2017
 */
// 2017-05-15 jgarcia@controltechcg.com Issue #78 (SICDI-Controltech) feature-78
@Service
public class UsuarioService {

	@Autowired
	private DependenciaService dependenciaService;

	/**
	 * Mapa de unidades por dependencia.
	 */
	private Map<Integer, Dependencia> unidadesMap = new LinkedHashMap<>();

	/**
	 * Obtiene la información básica del usuario (Grado, Nombre, Cargo,
	 * Dependencia).
	 * 
	 * @param usuario
	 *            Usuario.
	 * @return Información básica, o texto vacío en caso que el usuario sea
	 *         {@code null}.
	 */
	public String mostrarInformacionBasica(Usuario usuario) {
		if (usuario == null) {
			return "";
		}

		StringBuilder builder = new StringBuilder();

		final String grado = usuario.getGrado();
		if (grado != null && !grado.trim().isEmpty()) {
			builder.append(grado).append(". ");
		}

		final String nombre = usuario.getNombre();
		builder.append(nombre).append(" ");

		final String cargo = usuario.getCargo();
		if (cargo != null && !cargo.trim().isEmpty()) {
			builder.append(" - ").append(cargo).append(" ");
		}

		final Dependencia dependencia = usuario.getDependencia();
		if (dependencia != null) {
			final Dependencia unidad = buscarUnidad(dependencia);
			builder.append("(").append(unidad.getSigla()).append(")");
		}

		return builder.toString().trim();
	}

	/**
	 * Busca la unidad de la dependencia.
	 * 
	 * @param dependencia
	 *            Dependencia.
	 * @return Dependencia unidad.
	 */
	private Dependencia buscarUnidad(Dependencia dependencia) {
		final Integer dependenciaID = dependencia.getId();
		if (unidadesMap.containsKey(dependenciaID)) {
			return unidadesMap.get(dependenciaID);
		}

		final Dependencia unidad = dependenciaService.buscarUnidad(dependencia);
		unidadesMap.put(dependenciaID, unidad);
		return unidad;
	}
}
