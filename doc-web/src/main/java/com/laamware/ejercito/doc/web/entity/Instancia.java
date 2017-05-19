package com.laamware.ejercito.doc.web.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.laamware.ejercito.doc.web.ctrl.ProcesoController;
import com.laamware.ejercito.doc.web.serv.ProcesoService;
import com.laamware.ejercito.doc.web.util.ICondicion;
import com.laamware.ejercito.doc.web.util.ProcesoUtils;

@Entity
@Table(name = "PROCESO_INSTANCIA")
public class Instancia extends AuditModifySupport {

	public static final String CONTEXT_NAME = "instancia";

	public static Instancia create(Integer proId) {
		Instancia x = new Instancia();
		x.id = UUID.randomUUID().toString().replaceAll("-", "");
		x.proceso = new Proceso(proId);
		return x;
	}

	@Id
	@Column(name = "PIN_ID")
	private String id;

	@ManyToOne
	@JoinColumn(name = "PRO_ID")
	private Proceso proceso;

	@ManyToOne
	@JoinColumn(name = "PES_ID")
	private Estado estado;

	@OneToMany
	@JoinColumn(name = "PIN_ID")
	private List<Variable> variables = new ArrayList<Variable>();

	@OneToMany
	@JoinColumn(name = "PIN_ID")
	private List<Observacion> observaciones = new ArrayList<Observacion>();

	@ManyToOne
	@JoinColumn(name = "USU_ID_ASIGNADO")
	private Usuario asignado;

	@Transient
	private ProcesoService service;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Proceso getProceso() {
		return proceso;
	}

	public void setProceso(Proceso proceso) {
		this.proceso = proceso;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public List<Variable> getVariables() {
		return variables;
	}

	public void setVariables(List<Variable> variables) {
		this.variables = variables;
	}

	public Map<String, String> getVariablesAsMap() {
		Map<String, String> map = new HashMap<String, String>();
		if (variables != null) {
			for (Variable v : variables) {
				if (v.getActivo())
					map.put(v.getKey(), v.getValue());
			}
		}
		return map;
	}

	public Variable findVariable(String key) {
		for (Variable variable : variables) {
			if (variable.getKey().equals(key))
				return variable;
		}
		return null;
	}

	public Variable setVariable(String key, String value) {
		return service.setVariable(this, key, value);
	}

	public List<Observacion> getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(List<Observacion> observaciones) {
		this.observaciones = observaciones;
	}

	public Usuario getAsignado() {
		return asignado;
	}

	public void setAsignado(Usuario asignado) {
		this.asignado = asignado;
	}

	/**
	 * Obtiene el valor de una variable de instancia
	 * 
	 * @param key
	 * @return
	 */
	public String getVariable(String key) {
		Variable v = findVariable(key);
		if (v != null)
			return v.getValue();
		return null;
	}

	/**
	 * Obtiene la URL en la que se encuentra la instancia dependiendo del estado
	 * en el que se encuentre
	 * 
	 * @return
	 */
	public String url() {
		String location = estado.getLocation();
		if (location == null) {
			return String.format("%s/instancia/detalle?pin=%s", ProcesoController.PATH, id);
		} else {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("instancia", this);
			return ProcesoUtils.resolveUrl(location, map);
		}
	}

	/**
	 * Intenta realizar la siguiente transición verificando las condiciones de
	 * todas las transiciones que parten del estado en el que se encuentra la
	 * instancia
	 * 
	 */
	public boolean forward() {
		List<Transicion> transiciones = transiciones();
		if (transiciones.size() == 1) {
			return forward(transiciones.get(0), true);
		}
		return false;
	}

	/**
	 * Intenta realizar la transición indicada
	 * 
	 * @param tid
	 */
	public boolean forward(Integer tid) {
		for (Transicion transicion : transiciones()) {
			if (transicion.getId() == tid) {
				return forward(transicion, true);
			}
		}
		return false;
	}

	/**
	 * Intenta realizar la transición indicada
	 * 
	 * @param transicion
	 */
	private boolean forward(Transicion transicion, boolean checked) {
		boolean cumple = true;
		if (checked) {
			cumple = cumple(transicion);
		}
		if (cumple) {
			Estado estadoFinal = transicion.getEstadoFinal();
			if (estadoFinal != null) {
				this.estado = estadoFinal;
				service.instanciaRepository.saveAndFlush(this);
				return true;
			}
		}
		return false;
	}

	/**
	 * Obtiene las transiciones que se pueden realizar en una instancia
	 * dependiendo del estado en el que se encuentra y las condiciones para cada
	 * transición
	 * 
	 * @param instancia
	 * @return
	 * @throws Exception
	 */
	public List<Transicion> transiciones() {
		List<Transicion> transicionesCumple = new ArrayList<Transicion>();

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		/*
		 * 2017-02-09 jgarcia@controltechcg.com Issue #138: Modificación en la
		 * generación de transiciones, dado que los estados del proceso de
		 * documentos internos, son los mismos de los documentos externos.
		 */
		boolean procesoExterno = getProceso()
				.getId() == Proceso.ID_TIPO_PROCESO_GENERAR_DOCUMENTOS_PARA_ENTES_EXTERNOS_O_PERSONAS;
		boolean estadoEnviado = getEstado().getId() == Estado.ENVIADO;
		String nombreTransicionDarRespuesta = "Dar respuesta";

		if (authentication != null && authentication.isAuthenticated()) {
			String username = authentication.getName();
			if (this.getAsignado().getLogin().equals(username)) {
				List<Transicion> transiciones = estado.getTransicionesActivas();

				for (Transicion transicion : transiciones) {
					boolean cumple = cumple(transicion);
					if (cumple) {
						/*
						 * 2017-02-09 jgarcia@controltechcg.com Issue #138:
						 * Modificación en la generación de transiciones, dado
						 * que los estados del proceso de documentos internos,
						 * son los mismos de los documentos externos.
						 */
						if (!(procesoExterno && estadoEnviado
								&& transicion.getNombre().trim().equalsIgnoreCase(nombreTransicionDarRespuesta))) {
							transicionesCumple.add(transicion);
						}
					}
				}

			}

		}

		/*
		 * 2017-05-19 jgarcia@controltechcg.com Issue #73 (SICDI-Controltech)
		 * feature-73: Ordenamiento de las transiciones a presentar por
		 * instancia de documento, según el ID de los mismos.
		 */
		Collections.sort(transicionesCumple, new Comparator<Transicion>() {

			@Override
			public int compare(Transicion transicion1, Transicion transicion2) {
				return transicion1.getId().compareTo(transicion2.getId());
			}

		});

		return transicionesCumple;
	}

	/**
	 * Verifica si una transición se puede hacer
	 * 
	 * @param transicion
	 * @return
	 */
	private boolean cumple(Transicion transicion) {
		boolean cumple = true;

		Object facade = service.getFacade(proceso);
		if (StringUtils.isNotBlank(proceso.getFacade()) && facade == null) {
			throw new RuntimeException("El proceso tiene el facade " + proceso.getFacade()
					+ " pero no se encuentra ningún bean con esa clase");
		}

		for (Condicion condicion : transicion.getCondiciones()) {
			if (!condicion.getActivo()) {
				continue;
			}
			try {
				ICondicion icondicion = service.getConditionObject(condicion);
				if (icondicion == null)
					continue;
				cumple = (cumple && icondicion.cumple(this.getVariablesAsMap(), facade));
			} catch (Exception e) {
				cumple = false;
				e.printStackTrace();
				break;
			}
		}
		return cumple;
	}

	public ProcesoService getService() {
		return service;
	}

	public void setService(ProcesoService service) {
		this.service = service;
	}

	public List<HProcesoInstancia> historia() {
		return this.service.getHistoria(this.id);
	}

	public void asignar(Usuario usuario) {
		this.service.asignar(this, usuario);
	}

}
