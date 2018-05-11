package com.laamware.ejercito.doc.web.serv;

import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.persistence.EntityManager;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
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
import com.laamware.ejercito.doc.web.util.AuthorityUtil;
import com.laamware.ejercito.doc.web.util.ICondicion;
import com.laamware.ejercito.doc.web.util.ProcesoUtils;
import com.laamware.ejercito.doc.web.util.RolConstants;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ProcesoService {

    /*
     * 2018-05-11 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
     * feature-162: Logger.
     */
    private static final Logger LOG = Logger.getLogger(ProcesoService.class.getName());

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
     * @param procesoId Identificador del proceso
     * @param usuario El usuario que crea la instancia
     * @return El identificador de la nueva instancia
     * @throws com.laamware.ejercito.doc.web.serv.DatabaseException
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
     * por el usuario en sesión.
     *
     * @return Lista de procesos habilitados para el usuario en sesión.
     */
    /*
     * 2018-05-11 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
     * feature-162: Corrección en la eficiencia del algoritmo implementado en el
     * método. Validación de la autoridad del usuario en sesión para utilizar el
     * proceso de "Registro de actas". Cambio de nombre a #getProcesosAutorizados().
     */
    public List<Proceso> getProcesosAutorizados() {
        final List<Proceso> procesosActivos = procesoRepository.findByActivoTrue();
        final List<Proceso> procesosAutorizados = new LinkedList<>();

        for (final Proceso proceso : procesosActivos) {
            if (isProcesoAutorizado(proceso)) {
                procesosAutorizados.add(proceso);
            }
        }

        return procesosAutorizados;
    }

    /**
     * Indica si el proceso se encuentra autorizado para el usuario en sesión.
     *
     * @param proceso Proceso.
     * @return {@code true} si el proceso está autorizado para el usuario en
     * sesión; de lo contrario, {@code false}.
     */
    /*
     * 2018-05-11 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
     * feature-162.
     */
    private boolean isProcesoAutorizado(final Proceso proceso) {
        final RolConstants rol = ProcesoUtils.PROCESOS_ROLES_MAP.get(proceso.getId());
        if (rol == null) {
            return true;
        }

        return AuthorityUtil.hasAuthority(rol.name());
    }

    /**
     * Obtiene el objeto de instancia de proceso
     *
     * @param pin Identificador único de la instancia
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
        if (StringUtils.isBlank(facadeName)) {
            return null;
        }
        Class<?> clazz;
        try {
            clazz = Class.forName(facadeName);
        } catch (ClassNotFoundException ex) {
            /*
             * 2018-05-11 jgarcia@controltechcg.com Issue #162
             * (SICDI-Controltech) feature-162: Corrección en manejo de registro
             * de excepciones.
             */
            LOG.log(Level.SEVERE, null, ex);
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
