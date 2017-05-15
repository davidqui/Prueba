package com.laamware.ejercito.doc.web.serv;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.laamware.ejercito.doc.web.entity.Condicion;
import com.laamware.ejercito.doc.web.entity.HProcesoInstancia;
import com.laamware.ejercito.doc.web.entity.Instancia;
import com.laamware.ejercito.doc.web.entity.Proceso;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.entity.Variable;
import com.laamware.ejercito.doc.web.repo.HProcesoInstanciaRepository;
import com.laamware.ejercito.doc.web.repo.InstanciaRepository;
import com.laamware.ejercito.doc.web.repo.ProcesoRepository;
import com.laamware.ejercito.doc.web.repo.VariableRepository;
import com.laamware.ejercito.doc.web.util.ICondicion;
import com.laamware.ejercito.doc.web.util.ProcesoUtils;

@Service
public class ProcesoService {

	@Autowired
	EntityManager em;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	ApplicationContext applicationContext;

	@Autowired
	ProcesoRepository procesoRepository;

	@Autowired
	public InstanciaRepository instanciaRepository;

	@Autowired
	VariableRepository variableRepository;

	@Value("${docweb.condiciones.root}")
	public String condicionesRoot;

	@Autowired
	HProcesoInstanciaRepository hiR;

	/**
	 * Crea una nueva instancia de un proceso. Tener en cuenta que la instancia
	 * que devuelve no contiene las dependencias cargadas.
	 * 
	 * @param procesoId
	 *            Identificador del proceso
	 * @param usuario
	 *            El usuario que crea la instancia
	 * @return El identificador de la nueva instancia
	 */
	public String instancia(Integer procesoId, Usuario usuario) throws DatabaseException {

		// Crea la instancia. La base de datos se encarga de asignar el estado
		// inicial y las variables de proceso con sus respectivos valores
		// iniciales
		Instancia i = Instancia.create(procesoId);
		i.setAsignado(usuario);
		instanciaRepository.saveAndFlush(i);
		String id = i.getId();
		return id;
	}

	/**
	 * Obtiene la lista de procesos que están habilitados para ser instanciados
	 * 
	 * @return
	 */
	public List<Map<String, Object>> procesos() {
		List<Proceso> procesos = procesoRepository.findByActivo(true);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>) SecurityContextHolder
				.getContext().getAuthentication().getAuthorities();

		// System.out.println("authorities=" + authorities);
		
		boolean aplica = false;

		for (Proceso proceso : procesos) {

			for (SimpleGrantedAuthority simpleGrantedAuthority : authorities) {
				if (simpleGrantedAuthority.getAuthority().equals("REGISTRO")) {
					aplica = true;
					break;
				}
			}

			if (!aplica && Proceso.ID_TIPO_PROCESO_REGISTRAR_Y_CONSULTAR_DOCUMENTOS.equals(proceso.getId())) {
				continue;
			}

			Map<String, Object> map = new HashMap<String, Object>();
			list.add(map);
			map.put("id", proceso.getId());
			map.put("nombre", proceso.getNombre());
			map.put("descripcion", proceso.getDescripcion());
			map.put("imagen", proceso.getImagen());
		}

		return list;
	}

	/**
	 * Obtiene el objeto de instancia de proceso
	 * 
	 * @param pin
	 *            Identificador único de la instancia
	 * @return El objeto de instancia de proceso
	 */
	public Instancia instancia(String pin) {
		Instancia i = instanciaRepository.getOne(pin);
		i.setService(this);
		return i;
	}

	/**
	 * Obtiene el objeto compilado con el programa de la condición
	 * 
	 * @param e
	 * @return
	 */
	public ICondicion getConditionObject(Condicion e) {
		try {
			FileUtils.forceMkdir(new File(this.condicionesRoot));
		} catch (IOException e1) {
			throw new RuntimeException("Creando el directorio de condiciones", e1);
		}
		return ProcesoUtils.getConditionObject(e, this.condicionesRoot);
	}

	/**
	 * Obtiene el objeto facade relacionado al proceso
	 * 
	 * @param proceso
	 * @return
	 */
	public Object getFacade(Proceso proceso) {
		String facadeName = proceso.getFacade();
		if (StringUtils.isBlank(facadeName))
			return null;
		Class<?> clazz;
		try {
			clazz = Class.forName(facadeName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		Object facade = applicationContext.getBean(clazz);
		return facade;
	}

	public List<HProcesoInstancia> getHistoria(String pin) {
		return hiR.findById(pin, new Sort(Direction.DESC, "cuandoMod"));
	}

	public Variable setVariable(Instancia instancia, String key, String value) {
		Variable v = instancia.findVariable(key);
		if (v != null) {
			v.setValue(value);
			variableRepository.save(v);
		} else {
			v = new Variable(key, value, instancia);
			instancia.getVariables().add(v);
			variableRepository.save(v);
		}
		return v;
	}

	public void asignar(Instancia instancia, Usuario usuario) {
		instancia.setAsignado(usuario);
		instanciaRepository.save(instancia);
	}

}
