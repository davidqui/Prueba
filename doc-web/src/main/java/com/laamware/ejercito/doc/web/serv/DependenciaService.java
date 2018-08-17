package com.laamware.ejercito.doc.web.serv;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.laamware.ejercito.doc.web.entity.Dependencia;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.DependenciaRepository;
import com.laamware.ejercito.doc.web.util.DateUtil;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.data.domain.Sort;

/**
 * Servicio para las operaciones de negocio de dependencias.
 *
 * @author jgarcia@controltechcg.com
 * @since May 15, 2017
 */
// 2017-05-15 jgarcia@controltechcg.com Issue #78 (SICDI-Controltech) feature-78
@Service
public class DependenciaService {

    private static final Logger LOG = Logger.getLogger(DependenciaService.class.getName());

    @Autowired
    private DependenciaRepository dependenciaRepository;
    
    /*
     * 2018-07-11 samuel.delgado@controltechcg.com Issue #179 (SICDI-Controltech)
     * feature-179: Servicio de cache.
     */
    @Autowired
    CacheService cacheService;
    
    //issue-179 constante llave del cache
    private static final String DEPENDENCIAS_CACHE_KEY = "dependencias";
    private static final String PDEPENDENCIAS_CACHE_KEY = "dependencias_padre";


    /**
     * Busca la unidad de una dependencia.
     *
     * @param dependencia Dependencia.
     * @return Dependencia unidad.
     */
    public Dependencia buscarUnidad(Dependencia dependencia) {
        Dependencia unidad = dependencia;
        Integer padreID = dependencia.getPadre();

        /*
        * 2018-01-30 edison.gonzalez@controltechcg.com Issue #147: Se realiza la validacion
        * para que muestre la unidad segun el indicador de envio documentos.
         */
        if (dependencia.getDepIndEnvioDocumentos() != null && dependencia.getDepIndEnvioDocumentos()) {
            return dependencia;
        }

        while (padreID != null) {
            unidad = dependenciaRepository.findOne(padreID);
            if (unidad.getDepIndEnvioDocumentos() != null && unidad.getDepIndEnvioDocumentos()) {
                return unidad;
            }

            padreID = unidad.getPadre();
        }

        return unidad;
    }

    /**
     * Retira el usuario como jefe asignado (jefe principal o jefe encargado) de
     * las dependencias asociadas.
     *
     * @param usuario Usuario a retirar como jefe encargado.
     * @return Lista de las dependencias activas de las cuales el usuario se
     * retiró como jefe asignado.
     */
    // 2017-06-01 jgarcia@controltechcg.com Issue #99 (SICDI-Controltech)
    // hotfix-99
    public List<Dependencia> retirarUsuarioComoJefeAsignado(Usuario usuario) {
        final Integer usuarioId = usuario.getId();
        List<Dependencia> dependenciasAsignadas = dependenciaRepository.findActivoByJefeAsignado(usuarioId);

        for (Dependencia dependencia : dependenciasAsignadas) {
            try {
                retirarUsuarioComoJefeAsignado(usuarioId, dependencia);
            } catch (Exception ex) {
                LOG.log(Level.SEVERE, "" + usuarioId, ex);
            }
        }

        return dependenciasAsignadas;
    }

    /**
     * Retira el usuario como jefe asignado (jefe principal o jefe encargado) de
     * la dependencia asociada.
     *
     * @param usuarioId ID del usuario a retirar como jefe encargado.
     * @param dependencia Dependencia.
     */
    // 2017-06-01 jgarcia@controltechcg.com Issue #99 (SICDI-Controltech)
    // hotfix-99
    private void retirarUsuarioComoJefeAsignado(final Integer usuarioId, Dependencia dependencia) {
        final Usuario jefe = dependencia.getJefe();
        if (jefe != null && jefe.getId().equals(usuarioId)) {
            dependencia.setJefe(null);
        }

        final Usuario jefeEncargado = dependencia.getJefeEncargado();
        if (jefeEncargado != null && jefeEncargado.getId().equals(usuarioId)) {
            dependencia.setJefeEncargado(null);
        }

        dependenciaRepository.saveAndFlush(dependencia);
    }

    /**
     * Obtiene el jefe activo de la dependencia.
     *
     * @param dependencia Dependencia.
     * @return En caso que la dependencia tenga un jefe encargado y la fecha del
     * sistema se encuentre dentro del rango de asignación del jefe encargado y
     * este se encuentre activo en el sistema, se retorna el jefe encargado; de
     * lo contrario, se retorna el jefe principal de la dependencia.
     */
    /*
     * 2017-02-09 jgarcia@controltechcg.com Issue #11 (SIGDI-Incidencias01)
     *
     * 2017-02-09 jgarcia@controltechcg.com Issue #11 (SIGDI-Incidencias01):
     * Paso a static.
     *
     * 2018-04-12 jgarcia@controltechcg.com Issue #156 (SICDI-Controltech)
     * feature-156: Extracción desde DocumentoController para poder utilizar el
     * método desde otros componentes.
     */
    public Usuario getJefeActivoDependencia(final Dependencia dependencia) {
        final Usuario jefe = dependencia.getJefe();
        final Usuario jefeEncargado = dependencia.getJefeEncargado();

        if (jefeEncargado == null) {
            return jefe;
        }

        if (!jefeEncargado.getActivo()) {
            return jefe;
        }

        Date fechaInicioJefeEncargado = dependencia.getFchInicioJefeEncargado();
        if (fechaInicioJefeEncargado == null) {
            return jefe;
        }

        fechaInicioJefeEncargado = DateUtil.setTime(fechaInicioJefeEncargado, DateUtil.SetTimeType.START_TIME);

        Date fechaFinJefeEncargado = dependencia.getFchFinJefeEncargado();
        if (fechaFinJefeEncargado == null) {
            return jefe;
        }

        fechaFinJefeEncargado = DateUtil.setTime(fechaFinJefeEncargado, DateUtil.SetTimeType.END_TIME);

        Date fechaActual = new Date(System.currentTimeMillis());

        if (fechaInicioJefeEncargado.compareTo(fechaActual) <= 0 && fechaActual.compareTo(fechaFinJefeEncargado) <= 0) {
            return jefeEncargado;
        }

        return jefe;
    }

    /**
     * Busca una dependencia por su ID.
     *
     * @param id ID.
     * @return Instancia de la dependencia correspondiente al ID, o {@code null}
     * en caso que no exista en el sistema.
     */
    /*
     * 2018-04-12 jgarcia@controltechcg.com Issue #156 (SICDI-Controltech)
     * feature-156: Se crea esta función con el fin de poder utilizar el
     * servicio sin necesidad de invocar el repositorio.
     */
    public Dependencia findOne(final Integer id) {
        return dependenciaRepository.findOne(id);
    }

    /**
     * Busca la súper dependencia de una dependencia.
     *
     * @param dependencia Dependencia.
     * @return Súper dependencia.
     */
    /*
     * 2018-05-18 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
     * feature-162: Se mueve este método proveniente de DocumentoController.
     */
    public Dependencia getSuperDependencia(Dependencia dependencia) {
        /*
	 * 2018-01-30 edison.gonzalez@controltechcg.com Issue #147: Validacion para que tenga en cuenta el
	 * campo Indicador de envio documentos.
         */
        if (dependencia.getPadre() == null || (dependencia.getDepIndEnvioDocumentos() != null && dependencia.getDepIndEnvioDocumentos())) {
            return dependencia;
        }

        Dependencia jefatura = dependencia;
        Integer jefaturaId = dependencia.getPadre();
        while (jefaturaId != null) {
            jefatura = dependenciaRepository.getOne(jefaturaId);

            if (jefatura.getDepIndEnvioDocumentos() != null && jefatura.getDepIndEnvioDocumentos()) {
                return jefatura;
            }

            jefaturaId = jefatura.getPadre();
        }
        return jefatura;
    }
    
    /*
	 * 2017-04-11 jvargas@controltechcg.com Issue #45: DEPENDENCIAS:
	 * Ordenamiento por peso. Modificación: variable y orden en que se presentan
	 * las dependencias.
     */
    public synchronized List<Dependencia> depsHierarchy() {
        /*
         * 2018-07-11 samuel.delgado@controltechcg.com Issue #179 (SICDI-Controltech)
         * feature-179: se agrega cache a ta respuesta.
         */
        List<Dependencia> root = (List<Dependencia>) cacheService.getKeyCache(DEPENDENCIAS_CACHE_KEY);
        if (root == null) {
            root = dependenciaRepository.findByActivoAndPadreIsNull(true,
                new Sort(Sort.Direction.ASC, "pesoOrden", "nombre"));
            for (Dependencia d : root) {
                depsHierarchy(d);
            }
            cacheService.setKeyCache(DEPENDENCIAS_CACHE_KEY, root);
        }
        return root;
    }

    /*
	 * 2017-04-11 jvargas@controltechcg.com Issue #45: DEPENDENCIAS:
	 * Ordenamiento por peso. Modificación: variable y orden en que se presentan
	 * las dependencias.
     */
    public void depsHierarchy(Dependencia d) {
        List<Dependencia> subs = dependenciaRepository.findByActivoAndPadre(true, d.getId(),
                new Sort(Sort.Direction.ASC, "pesoOrden", "nombre"));
        d.setSubs(subs);
        for (Dependencia x : subs) {
            depsHierarchy(x);
        }
    }

    public synchronized List<Dependencia> depsHierarchyPadre() {
                /*
         * 2018-07-11 samuel.delgado@controltechcg.com Issue #179 (SICDI-Controltech)
         * feature-179: se agrega cache a ta respuesta.
         */
        List<Dependencia> root = (List<Dependencia>) cacheService.getKeyCache(DEPENDENCIAS_CACHE_KEY);
        if (root == null) {
            root = this.dependenciaRepository.findByActivoAndPadreIsNull(true, new Sort(Sort.Direction.ASC, new String[]{"pesoOrden", "nombre"}));
            for (Dependencia d : root) {
                depsHierarchyPadre(d);
            }
            cacheService.setKeyCache(PDEPENDENCIAS_CACHE_KEY, root);
        }

        return root;
    }

    public void depsHierarchyPadre(Dependencia d) {
        List<Dependencia> subs = this.dependenciaRepository.findByActivoAndPadreAndDepIndEnvioDocumentos(true, d.getId(), true, new Sort(Sort.Direction.ASC, new String[]{"pesoOrden", "nombre"}));

        d.setSubs(subs);
        for (Dependencia x : subs) {
            depsHierarchyPadre(x);
        }
    }
    
    /*
    * 2018-08-17 samuel.delgado@controltechcg.com issue #7 gogs
    * (SICDI-Controltech) feature-gogs-7: Lista las dependencias que tengan un 
    *  usuario registro dado por un usuario.
    */
    public List<Dependencia> dependenciasUsuarioRegistro(Usuario usuario){
        return dependenciaRepository.findActivoByUsuarioRegistro(usuario);
    }

}
